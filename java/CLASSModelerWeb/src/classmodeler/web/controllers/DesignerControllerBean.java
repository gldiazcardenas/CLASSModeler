/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.controllers;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import classmodeler.domain.diagram.Diagram;
import classmodeler.domain.diagram.EDiagramPrivilege;
import classmodeler.domain.user.User;
import classmodeler.service.DiagramService;
import classmodeler.service.exception.UnprivilegedException;
import classmodeler.service.util.GenericUtils;
import classmodeler.web.beans.SharedDiagram;
import classmodeler.web.beans.SharedDiagramSession;
import classmodeler.web.beans.SharedDiagramsCache;
import classmodeler.web.converters.UMLConverter;
import classmodeler.web.util.JSFGenericBean;
import classmodeler.web.util.JSFOutcomeUtil;

import com.mxgraph.reader.mxGraphViewImageReader;
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
  
  @EJB
  private DiagramService diagramService;
  
  private User user;
  private SharedDiagram diagram;
  private SharedDiagramSession diagramSession;
  private EDiagramPrivilege privilege;
  private boolean pendingChanges;
  
  public DesignerControllerBean() {
    super();
  }
  
  public User getUser() {
    return user;
  }
  
  public SharedDiagram getDiagram() {
    return diagram;
  }
  
  public SharedDiagramSession getDiagramSession() {
    return diagramSession;
  }
  
  public boolean isReadOnly() {
    return privilege == EDiagramPrivilege.READ;
  }
  
  public boolean isPendingChanges() {
    return pendingChanges;
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
    this.privilege = this.diagramService.checkDiagramPrivilege(diagram, user);
    
    // Unprivileged user
    if (this.privilege == null) {
      addErrorMessage(GENERAL_MESSAGE_ID, GenericUtils.getLocalizedMessage("UNPRIVILEGED_USER_MESSAGE", user.getName(), diagram.getName()), null);
      return null;
    }
    
    this.user = user;
    this.diagram = SharedDiagramsCache.getInstance().putDiagram(diagram);
    this.diagramSession = new SharedDiagramSession(this.diagram);
    this.pendingChanges = false;
    
    return JSFOutcomeUtil.DESIGNER + JSFOutcomeUtil.REDIRECT_SUFIX;
  }
  
  /**
   * Initializes the session and returns the current diagram XML representation.
   * 
   * @return The XML diagram representation.
   * @author Gabriel Leonardo Diaz, 16.02.2014.
   */
  public String initialize () {
    String value = this.diagramSession.init();
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
    String value = this.diagramSession.poll();
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
    this.diagramSession.receive(document.getDocumentElement());
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
      this.diagramService.updateDiagram(this.diagram.getWrappedDiagram());
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
    this.diagramSession.destroy();
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
    UMLConverter converter = new UMLConverter(this.diagram.getModel());
    converter.convert();
  }
  
  /**
   * Exports the edited diagram to XMI format.
   * 
   * @author Gabriel Leonardo Diaz, 17.02.2014
   */
  public void exportXMI () {
    // TODO GD
  }
  
}
