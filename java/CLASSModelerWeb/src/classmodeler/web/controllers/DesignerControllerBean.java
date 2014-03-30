/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.eclipse.uml2.uml.NamedElement;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import classmodeler.domain.code.SourceCodeFile;
import classmodeler.domain.code.SourceCodeFileComparator;
import classmodeler.domain.diagram.Diagram;
import classmodeler.domain.user.User;
import classmodeler.service.CodeGenerationService;
import classmodeler.service.DiagramService;
import classmodeler.service.exception.UnprivilegedException;
import classmodeler.service.util.GenericUtils;
import classmodeler.web.beans.SharedDiagram;
import classmodeler.web.beans.SharedDiagramSession;
import classmodeler.web.converters.UMLConverter;
import classmodeler.web.util.JSFGenericBean;
import classmodeler.web.util.JSFOutcomeUtil;

import com.mxgraph.util.mxXmlUtils;

/**
 * JSF bean that handles the interactions of the user with the CLASS
 * designer.
 * 
 * @author Gabriel Leonardo Diaz, 14.04.2013.
 */
@ManagedBean(name="designerController")
@SessionScoped
public class DesignerControllerBean extends JSFGenericBean implements HttpSessionBindingListener {

  private static final long serialVersionUID = 1L;
  
  @EJB
  private DiagramService diagramService;
  
  @EJB
  private CodeGenerationService generateCodeService;
  
  @ManagedProperty(value="#{sharedDiagramController}")
  private SharedDiagramController sharedDiagramController;
  
  /** The user in session. */
  private User user;
  
  /** The diagram being edited. */
  private SharedDiagram diagram;
  
  /** The session created when the user opens a diagram. */
  private SharedDiagramSession session;
  
  /** Converter class for UML transformation. */
  private UMLConverter umlConverter;
  
  /** The source code files generated. */
  private List<SourceCodeFile> sourceCodeFiles = new ArrayList<SourceCodeFile>();
  
  /** A flag indicating the user can modify the diagram. */
  private boolean writeable;
  
  /** A flag indicating the user made changes on the diagram. */
  private boolean pendingChanges;
  
  public DesignerControllerBean() {
    super();
  }
  
  public User getUser() {
    return user;
  }
  
  public boolean isWriteable() {
    return writeable;
  }
  
  public List<SourceCodeFile> getSourceCodeFiles() {
    return sourceCodeFiles;
  }
  
  public void setSharedDiagramController(SharedDiagramController sharedDiagramController) {
    this.sharedDiagramController = sharedDiagramController;
  }
  
  public String getGenerateCodeTitle() {
    return GenericUtils.getLocalizedMessage("GENERATE_CODE_FORM_TITLE", this.session.getDiagram().getName());
  }
  
  public SharedDiagram getDiagram() {
    return diagram;
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
  public String openDiagram (Diagram diagram, User user) {
    destroy(); // Remove reference of the previous diagram.
    
    try {
      this.writeable = this.diagramService.canWriteDiagram(diagram, user);
    }
    catch (UnprivilegedException e) {
      addErrorMessage(GENERAL_MESSAGE_ID, GenericUtils.getLocalizedMessage("UNPRIVILEGED_USER_MESSAGE", user.getName(), diagram.getName()), null);
      return null;
    }
    
    this.user              = user;
    this.diagram           = this.sharedDiagramController.putDiagram(diagram);
    this.session           = new SharedDiagramSession(this.diagram, this.user);
    this.umlConverter      = new UMLConverter(this.diagram);
    this.pendingChanges    = false;
    this.generateCodeService.configure(this.user);
    
    return JSFOutcomeUtil.DESIGNER + JSFOutcomeUtil.REDIRECT_SUFIX;
  }
  
  /**
   * Initializes the session and returns the current diagram XML representation.
   * 
   * @return The XML diagram representation.
   * @author Gabriel Leonardo Diaz, 16.02.2014.
   */
  public String initialize () {
    if (this.pendingChanges) {
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
    this.session.receive(mxXmlUtils.parseXml(xml).getDocumentElement());
    this.pendingChanges = true;
  }
  
  /**
   * Saves the changes made on the edited diagram to database.
   * 
   * @author Gabriel Leonardo Diaz, 17.02.2014
   */
  public void save () {
    try {
      this.session.getSharedDiagram().publishChanges();
      if (this.user.isRegisteredUser()) {
        this.diagramService.updateDiagram(this.session.getDiagram());
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
    if (this.session == null) return;
    this.session.destroy(); // Removes the session from the listeners of the diagram.
    this.sharedDiagramController.purgeDiagrams(); // Removes the diagrams without any listener
  }
  
  /**
   * Generates the source code of the current diagram representation.
   * 
   * @author Gabriel Leonardo Diaz, 17.02.2014
   */
  public void generateCode () {
    List<NamedElement> elements = this.umlConverter.execute();
    
    this.sourceCodeFiles.clear();
    
    SourceCodeFile sourceFile;
    for (NamedElement element : elements) {
      sourceFile = new SourceCodeFile();
      sourceFile.setName(element.getName());
      sourceFile.setFormat(SourceCodeFile.JAVA_FORMAT);
      sourceFile.setElement(element);
      this.sourceCodeFiles.add(sourceFile);
    }
    
    Collections.sort(this.sourceCodeFiles, SourceCodeFileComparator.CASE_INSENSITIVE_COMPARATOR);
  }
  
  /**
   * Downloads the given file.
   * @param file
   * @return
   * @author Gabriel Leonardo Diaz, 5.03.2014.
   */
  public StreamedContent downloadFile (SourceCodeFile sourceFile) {
    String code = generateCodeService.generateSourceCode(sourceFile);
    return new DefaultStreamedContent(new ByteArrayInputStream(code.getBytes(Charset.forName("UTF-8"))), "text/plain", sourceFile.getFileName(), "UTF-8");
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
      
      String code;
      ZipEntry zipEntry;
      
      for (SourceCodeFile sourceFile : this.sourceCodeFiles) {
        code = generateCodeService.generateSourceCode(sourceFile);
        zipEntry = new ZipEntry(sourceFile.getFileName());
        zipFile.putNextEntry(zipEntry);
        zipFile.write(code.getBytes(Charset.forName("UTF-8")));
      }
      
      zipFile.flush();
      zipFile.close();
      file.setStream(new ByteArrayInputStream(outputStream.toByteArray()));
    }
    catch (IOException e) {
      file.setStream(new ByteArrayInputStream(new byte[0]));
      addErrorMessage(GENERAL_MESSAGE_ID, GenericUtils.getLocalizedMessage("UNEXPECTED_EXCEPTION_MESSAGE"), e.getLocalizedMessage());
    }
    
    return file;
  }
  
  @Override
  public void valueBound (HttpSessionBindingEvent event) {
    // Do nothing
  }
  
  
  @Override
  public void valueUnbound (HttpSessionBindingEvent event) {
    destroy();
  }
  
}
