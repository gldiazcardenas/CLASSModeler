/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * C�cuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.controllers;

import java.awt.Graphics2D;
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
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import classmodeler.domain.diagram.Diagram;
import classmodeler.domain.diagram.EDiagramPrivilege;
import classmodeler.domain.user.User;
import classmodeler.service.DiagramService;
import classmodeler.service.util.GenericUtils;
import classmodeler.web.beans.SharedDiagram;
import classmodeler.web.beans.SharedDiagramSession;
import classmodeler.web.beans.SharedDiagramsCache;
import classmodeler.web.util.JSFGenericBean;
import classmodeler.web.util.JSFOutcomeUtil;

import com.mxgraph.canvas.mxGraphicsCanvas2D;
import com.mxgraph.reader.mxSaxOutputHandler;
import com.mxgraph.util.mxUtils;
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
    
    return JSFOutcomeUtil.DESIGNER + JSFOutcomeUtil.REDIRECT_SUFIX;
  }
  
  /**
   * Initializes the session and returns the current diagram XML representation.
   * 
   * @return The XML diagram representation.
   * @author Gabriel Leonardo Diaz, 16.02.2014.
   */
  public String init () {
    return this.diagramSession.init();
  }
  
  /**
   * Waits for the modifications of other users.
   * 
   * @return
   * @throws InterruptedException
   * @author Gabriel Leonardo Diaz, 16.02.2014.
   */
  public String poll () throws InterruptedException {
    return this.diagramSession.poll();
  }
  
  /**
   * Processes the notification of the changes made in the diagram.
   * 
   * @author Gabriel Leonardo Diaz, 16.02.2014.
   * @throws UnsupportedEncodingException 
   */
  public void process (String rawXML) throws UnsupportedEncodingException {
    String xml = URLDecoder.decode(rawXML, "UTF-8");
    Document doc = mxXmlUtils.parseXml(xml);
    this.diagramSession.receive(doc.getDocumentElement());
  }
  
  /**
   * Destroys the session created to edit the diagram.
   * 
   * @author Gabriel Leonardo Diaz, 16.02.2014.
   */
  public void tearDown () {
    this.diagramSession.destroy();
  }
  
  /**
   * 
   * @param rawXML
   * @param fileName
   * @param format
   * @param background
   * @param width
   * @param height
   * @param output
   * @throws SAXException
   * @throws ParserConfigurationException
   * @throws IOException
   */
  public void generateImage (String rawXML, String fileName, String format, String background, int width, int height, OutputStream output) throws SAXException, ParserConfigurationException, IOException {
    String xml = URLDecoder.decode(rawXML, "UTF-8");
    BufferedImage image = mxUtils.createBufferedImage(width, height, mxUtils.parseColor(background));
    Graphics2D g2 = image.createGraphics();
    mxUtils.setAntiAlias(g2, true, true);
    XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
    reader.setContentHandler(new mxSaxOutputHandler(new mxGraphicsCanvas2D(g2)));
    reader.parse(new InputSource(new StringReader(xml)));
    ImageIO.write(image, format, output);
  }
  
  public void save () {
    
  }
  
  public void generateCode () {
    
  }
  
  public void exportXMI () {
    
  }
  
}
