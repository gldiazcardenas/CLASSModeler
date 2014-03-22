/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * C�cuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.controllers;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import classmodeler.domain.diagram.Diagram;
import classmodeler.domain.user.Diagrammer;
import classmodeler.service.DiagramService;
import classmodeler.service.UserService;
import classmodeler.service.util.GenericUtils;
import classmodeler.web.util.FormatControllerBean;
import classmodeler.web.util.JSFFormControllerBean;
import classmodeler.web.util.JSFGenericBean;

/**
 * JSF Bean controller to manage the interactions of the user with the form to
 * create and edit projects.
 * 
 * @author Gabriel Leonardo Diaz, 30.05.2013.
 */
@ManagedBean(name="diagramController")
@ViewScoped
public class DiagramControllerBean extends JSFGenericBean implements JSFFormControllerBean {
  
  private static final long serialVersionUID = 1L;
  
  /**
   * Default representation of the diagram XML.
   */
  public static final String DEFAULT_XML_DIAGRAM = new StringBuilder("<mxGraphModel>")
                                                                .append("<root>")
                                                                   .append("<Workflow label=\"UML\" id=\"0\" />")
                                                                   .append("<Layer label=\"ClassDiagram\" id=\"1\"><mxCell parent=\"0\" /></Layer>")
                                                                .append("</root>")
                                                             .append("</mxGraphModel>")
                                                             .toString();
  
  private String name;
  private String description;
  private String title;
  private Diagram diagram;
  private EDiagramControllerMode mode;
  
  // Fields used to share a diagram
  private boolean writeable;
  private List<Diagrammer> availableUsers;
  private List<Diagrammer> selectedUsers;
  
  @ManagedProperty("#{dashBoardController}")
  private DashboardControllerBean dashBoardController;
  
  @ManagedProperty("#{sessionController.diagrammer}")
  private Diagrammer diagrammer;
  
  @ManagedProperty("#{formatController}")
  private FormatControllerBean formatController;
  
  @EJB
  private DiagramService diagramService;
  
  @EJB
  private UserService userService;
  
  public DiagramControllerBean() {
    super();
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getDescription() {
    return description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  public String getTitle() {
    return title;
  }
  
  public void setTitle(String title) {
    this.title = title;
  }
  
  public void setDashBoardController(DashboardControllerBean dashBoardController) {
    this.dashBoardController = dashBoardController;
  }
  
  public void setDiagrammer(Diagrammer diagrammer) {
    this.diagrammer = diagrammer;
  }
  
  public void setFormatController(FormatControllerBean formatController) {
    this.formatController = formatController;
  }
  
  public List<Diagrammer> getAvailableUsers() {
    return availableUsers;
  }
  
  public List<Diagrammer> getSelectedUsers() {
    return selectedUsers;
  }
  
  public void setSelectedUsers(List<Diagrammer> selectedUsers) {
    this.selectedUsers = selectedUsers;
  }
  
  public boolean isWriteable() {
    return writeable;
  }
  
  public void setWriteable(boolean writeable) {
    this.writeable = writeable;
  }
  
  /**
   * Retrieves the localized message for deleting a diagram (either by the owner
   * or another user). When the owner deletes the diagram this is in fact
   * deleted with all sharing, but when a user who had the diagram shared only
   * is able to delete the sharing.
   * 
   * @return The localized message.
   * @author Gabriel Leonardo Diaz, 10.08.2013.
   */
  public String getDeleteDiagramMessage () {
    StringBuilder sb = new StringBuilder();
    
    if (mode == EDiagramControllerMode.DELETE && diagram != null && diagrammer != null) {
      sb.append(GenericUtils.getLocalizedMessage("DIAGRAM_DELETE_CONFIRMATION_MESSAGE"));
    }
    
    return sb.toString();
  }
  
  /**
   * Prepares the controller to create a new diagram. Sets an empty object to
   * the controller.
   * 
   * @author Gabriel Leonardo Diaz, 28.05.2013.
   */
  public void prepareNewDiagram () {
    name        = null;
    description = null;
    diagram     = new Diagram();
    diagram.setXML(DEFAULT_XML_DIAGRAM);
    
    title       = GenericUtils.getLocalizedMessage("DIAGRAM_NEW_FORM_TITLE");
    mode        = EDiagramControllerMode.CREATE;
  }
  
  /**
   * Prepares the controller to edit a diagram by getting the selected diagram
   * instance {@link DashboardControllerBean#diagram}, if the object is null
   * this method does nothing.
   * 
   * @author Gabriel Leonardo Diaz, 28.05.2013.
   */
  public void prepareEditDiagram () {
    diagram = dashBoardController.getDiagram();
    if (diagram != null) {
      name        = diagram.getName();
      description = diagram.getDescription();
      title       = GenericUtils.getLocalizedMessage("DIAGRAM_EDIT_FORM_TITLE", name);
      mode        = EDiagramControllerMode.EDIT;
    }
  }
  
  /**
   * Prepares the controller to delete the selected diagram in the table, this
   * method takes the object from {@link DashboardControllerBean}.
   * 
   * @author Gabriel Leonardo Diaz, 01.06.2013.
   */
  public void prepareDeleteDiagram () {
    diagram = dashBoardController.getDiagram();
    if (diagram != null) {
      name   = diagram.getName();
      title  = GenericUtils.getLocalizedMessage("DIAGRAM_DELETE_FORM_TITLE", name);
      mode   = EDiagramControllerMode.DELETE;
    }
  }
  
  /**
   * Prepares the controller to create a new diagram based on the information
   * copied from the selected diagram.
   * 
   * @author Gabriel Leonardo Diaz, 02.06.2013.
   */
  public void prepareCopyDiagram () {
    diagram = dashBoardController.getDiagram();
    if (diagram != null) {
      name        = diagram.getName();
      description = diagram.getDescription();
      String copyXML = diagram.getXML();
      
      diagram = new Diagram();
      diagram.setXML(copyXML);
      
      title   = GenericUtils.getLocalizedMessage("DIAGRAM_COPY_FORM_TITLE", name);
      mode    = EDiagramControllerMode.COPY;
    }
  }
  
  /**
   * Prepares the controller to share a diagram with other users.
   * 
   * @author Gabriel Leonardo Diaz, 05.07.2013.
   */
  public void prepareShareDiagram () {
    diagram = dashBoardController.getDiagram();
    if (diagram != null) {
      name           = diagram.getName();
      availableUsers = userService.getDiagrammersAllowedToShareDiagram(diagram);
      
      title  = GenericUtils.getLocalizedMessage("DIAGRAM_SHARE_FORM_TITLE", name);
      mode   = EDiagramControllerMode.SHARE;
    }
  }
  
  @Override
  public boolean isAllValid() {
    return diagram != null && diagrammer != null && !GenericUtils.isEmptyString(name);
  }

  @Override
  public void processAJAX() {
    if (!isAllValid()) {
      return;
    }
    
    try {
      switch (mode) {
      case CREATE:
      case COPY:
        diagram.setName(name);
        diagram.setDescription(description);
        diagram.setCreatedBy(diagrammer);
        diagramService.insertDiagram(diagram);
        dashBoardController.addDiagram(diagram);
        break;
        
      case EDIT:
      
        diagram.setName(name);
        diagram.setDescription(description);
        diagram.setModifiedBy(diagrammer);
        diagramService.updateDiagram(diagram);
        
        break;
      case DELETE:
        diagramService.deleteDiagram(diagram.getKey());
        dashBoardController.deleteDiagram(diagram);
        break;
      
      case SHARE:
        diagramService.shareDiagram(diagram, selectedUsers, writeable);
        break;
        
      default:
        break;
      }
      
      diagram     = null;
      name        = null;
      description = null;
      title       = null;
      mode        = null;
    }
    catch (Exception e) {
      addErrorMessage(JSFGenericBean.GENERAL_MESSAGE_ID, GenericUtils.getLocalizedMessage("UNEXPECTED_EXCEPTION_MESSAGE"), e.getMessage());
    }
  }

  @Override
  public String process() {
    return null; // Not used.
  }
  
  /**
   * Generates an empty diagram with the default XML representation.
   * 
   * @return
   * @author Gabriel Leonardo Diaz, 16.02.2014.
   */
  public static Diagram generateEmpty() {
    Diagram diagram = new Diagram();
    diagram.setKey(-1);
    diagram.setName(GenericUtils.getLocalizedMessage("NEW_DIAGRAM_NAME"));
    diagram.setXML(DEFAULT_XML_DIAGRAM);
    return diagram;
  }
  
  /**
   * The operation modes of this controller.
   *
   * @author Gabriel Leonardo Diaz, 01.06.2013.
   */
  public enum EDiagramControllerMode {
    CREATE,
    EDIT,
    DELETE,
    COPY,
    SHARE
  }

}
