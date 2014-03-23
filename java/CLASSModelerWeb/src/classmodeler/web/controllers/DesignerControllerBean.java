/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * C�cuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.controllers;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import classmodeler.domain.code.SourceCodeFile;
import classmodeler.domain.diagram.Diagram;
import classmodeler.domain.share.SharedDiagram;
import classmodeler.domain.share.SharedDiagramsCache;
import classmodeler.domain.user.User;
import classmodeler.service.DiagramService;
import classmodeler.service.SourceCodeService;
import classmodeler.service.exception.UnprivilegedException;
import classmodeler.service.util.GenericUtils;
import classmodeler.web.util.JSFGenericBean;
import classmodeler.web.util.JSFOutcomeUtil;

import com.mxgraph.reader.mxGraphViewImageReader;
import com.mxgraph.sharing.mxSession;
import com.mxgraph.util.mxXmlUtils;

/**
 * JSF bean that handles the interactions of the user with the CLASS
 * designer.
 * 
 * @author Gabriel Leonardo Diaz, 14.04.2013.
 */
@ManagedBean(name="designerController")
@SessionScoped
public class DesignerControllerBean extends JSFGenericBean {

  private static final long serialVersionUID = 1L;
  
  public static final String SHARED_DIAGRAM_SESSION_ID = "SharedSessionID";
  
  @EJB
  private DiagramService diagramService;
  
  @EJB
  private SourceCodeService sourceCodeService;
  
  private User user;
  private SharedDiagram diagram;
  private SharedDiagramSession session;
  private boolean writeable;
  private boolean pendingChanges;
  private List<SourceCodeFile> sourceCodeFiles = new ArrayList<SourceCodeFile>();
  
  public DesignerControllerBean() {
    super();
  }
  
  public User getUser() {
    return user;
  }
  
  public SharedDiagram getDiagram() {
    return diagram;
  }
  
  public boolean isWriteable() {
    return writeable;
  }
  
  public boolean isPendingChanges() {
    return pendingChanges;
  }
  
  public String getGenerateCodeTitle() {
    return GenericUtils.getLocalizedMessage("GENERATE_CODE_FORM_TITLE", this.diagram.getName());
  }
  
  public List<SourceCodeFile> getSourceCodeFiles() {
    return sourceCodeFiles;
  }
  
  /**
   * Begins the edition of the diagram by user.
   * 
   * @param diagram
   *          The diagram to be designed.
   * @param user
   *          The user how is editing the diagram.
   * @return The outcome to the designer page, null when the user is not
   *         privileged to edit the diagram.
   * @author Gabriel Leonardo Diaz, 13.02.2014
   */
  public String design (Diagram diagram, User user) {
    try {
      this.writeable = this.diagramService.canWriteDiagram(diagram, user);
    }
    catch (UnprivilegedException e) {
      addErrorMessage(GENERAL_MESSAGE_ID, GenericUtils.getLocalizedMessage("UNPRIVILEGED_USER_MESSAGE", user.getName(), diagram.getName()), null);
      return null;
    }
    
    this.user              = user;
    this.diagram           = SharedDiagramsCache.getInstance().putDiagram(diagram);
    this.session           = new SharedDiagramSession(this);
    this.pendingChanges    = false;
    
    return JSFOutcomeUtil.DESIGNER + JSFOutcomeUtil.REDIRECT_SUFIX;
  }
  
  /**
   * Initializes the session and returns the current diagram XML representation.
   * 
   * @return The XML diagram representation.
   * @author Gabriel Leonardo Diaz, 16.02.2014.
   */
  public String initialize () {
    if (isPendingChanges()) {
      save();
    }
    
    String value = this.session.init();
    return value;
  }
  
  /**
   * Waits for the modifications of other users.
   * 
   * @return
   * @throws InterruptedException
   * @author Gabriel Leonardo Diaz, 16.02.2014.
   */
  public String poll () throws InterruptedException {
    String value = this.session.poll();
    return value;
  }
  
  /**
   * Processes the notification of the changes made in the diagram.
   * 
   * @author Gabriel Leonardo Diaz, 16.02.2014.
   * @throws UnsupportedEncodingException 
   */
  public void notify (String rawXML) throws UnsupportedEncodingException {
    String xml = URLDecoder.decode(rawXML, "UTF-8");
    Document document = mxXmlUtils.parseXml(xml);
    this.session.receive(document.getDocumentElement());
    this.pendingChanges = true;
  }
  
  /**
   * Saves the changes made on the edited diagram to database.
   * 
   * @author Gabriel Leonardo Diaz, 17.02.2014
   */
  public void save () {
    try {
      this.diagram.publishChanges();
      if (this.user.isRegisteredUser()) {
        this.diagramService.updateDiagram(this.diagram.getWrappedDiagram());
      }
      this.pendingChanges = false;
    }
    catch (UnprivilegedException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Destroys the session created to edit the diagram.
   * 
   * @author Gabriel Leonardo Diaz, 16.02.2014.
   */
  public void destroy () {
    if (this.session != null) {
      this.session.destroy(); // Removes the session from the listeners of the diagram.
    }
    SharedDiagramsCache.getInstance().purgeCache(); // Removes the diagrams without any listener
  }
  
  /**
   * Generates the images in puts it into the output stream.
   * 
   * @param rawXML
   * @param output
   * @throws SAXException
   * @throws ParserConfigurationException
   * @throws IOException
   * @author Gabriel Leonardo Diaz, 17.02.2014.
   */
  public void generateImage (String rawXML, OutputStream output) throws SAXException, ParserConfigurationException, IOException {
    String xml = URLDecoder.decode(rawXML, "UTF-8");
    mxGraphViewImageReader reader = new mxGraphViewImageReader(Color.WHITE, 4, true, true);
    InputSource inputSource = new InputSource(new StringReader(xml));
    BufferedImage image = mxGraphViewImageReader.convert(inputSource, reader);
    ImageIO.write(image, "png", output);
  }
  
  /**
   * Generates the source code of the current diagram representation.
   * 
   * @author Gabriel Leonardo Diaz, 17.02.2014
   */
  public void generateCode () {
    // Clear previous generated
    this.sourceCodeFiles.clear();
    
    // Generate the sources
    this.sourceCodeFiles.addAll(sourceCodeService.generateCode(user, diagram));
    Collections.sort(this.sourceCodeFiles, SourceCodeService.SOURCE_FILES_COMPARATOR);
  }
  
  /**
   * Downloads the given file.
   * @param file
   * @return
   * @author Gabriel Leonardo Diaz, 5.03.2014.
   */
  public StreamedContent downloadFile (SourceCodeFile sourceFile) {
    return new DefaultStreamedContent(new ByteArrayInputStream(sourceFile.getCode().getBytes(Charset.forName("UTF-8"))), "text/plain", sourceFile.getFullName(), "UTF-8");
  }
  
  /**
   * Downloads a ZIP file containing all objects.
   * 
   * @return
   * @author Gabriel Leonardo Diaz, 05.03.2014.
   */
  public StreamedContent downloadZIP () {
    DefaultStreamedContent file = new DefaultStreamedContent();
    file.setName("sourceCode.zip");
    file.setContentType("application/x-compressed");
    
    try {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      ZipOutputStream zipFile = new ZipOutputStream(outputStream);
      
      ZipEntry zipEntry;
      
      for (SourceCodeFile sourceFile : this.sourceCodeFiles) {
        zipEntry = new ZipEntry(sourceFile.getFullName());
        zipFile.putNextEntry(zipEntry);
        zipFile.write(sourceFile.getCode().getBytes(Charset.forName("UTF-8")));
      }
      
      zipFile.flush();
      zipFile.close();
      file.setStream(new ByteArrayInputStream(outputStream.toByteArray()));
    }
    catch (IOException e) {
      file.setStream(new ByteArrayInputStream(new byte[0]));
    }
    
    return file;
  }
  
  /**
   * Creates a new session attached to the diagram in this controller.
   * 
   * @author Gabriel Leonardo Diaz, 23.03.2014.
   */
  public static class SharedDiagramSession extends mxSession {
    
    private DesignerControllerBean designerController;
    
    public SharedDiagramSession(DesignerControllerBean designerController) {
      super (SHARED_DIAGRAM_SESSION_ID, designerController.getDiagram());
      this.designerController = designerController;
      new Timer().schedule(new SharedDiagramsTimerTask(), 5 * 60 * 1000);
    }
    
    public User getUser () {
      return this.designerController.getUser();
    }
    
    @Override
    public void destroy() {
      super.destroy();
      SharedDiagramsCache.getInstance().purgeCache();
    }
    
    /**
     * Task used to remove the items not being used.
     * 
     * @author Gabriel Leonardo Diaz, 23.03.2014.
     */
    private class SharedDiagramsTimerTask extends TimerTask {
      @Override
      public void run() {
        // TODO
      }
    }
    
  }
  
}
