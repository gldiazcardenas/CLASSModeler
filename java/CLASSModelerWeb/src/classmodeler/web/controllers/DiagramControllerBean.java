/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.controllers;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import classmodeler.domain.diagram.Diagram;
import classmodeler.domain.diagram.EDiagramPrivilege;
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
  
  private String name;
  private String description;
  private String title;
  private Diagram diagram;
  private EDiagramControllerMode mode;
  
  // Fields used to share a diagram
  private EDiagramPrivilege privilege;
  private List<Diagrammer> availableUsers;
  private List<Diagrammer> selectedUsers;
  
  @ManagedProperty("#{dashBoardController}")
  private DashboardControllerBean dashBoardController;
  
  @ManagedProperty("#{sessionController.loggedRegisteredUser}")
  private Diagrammer loggedUser;
  
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
  
  public void setLoggedUser(Diagrammer loggedUser) {
    this.loggedUser = loggedUser;
  }
  
  public void setFormatController(FormatControllerBean formatController) {
    this.formatController = formatController;
  }
  
  public EDiagramPrivilege getPrivilege() {
    return privilege;
  }
  
  public void setPrivilege(EDiagramPrivilege privilege) {
    this.privilege = privilege;
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
  
  /**
   * Gets the items for Radio button group used to select a privilege.
   * 
   * @return An array with the select items.
   * @author Gabriel Leonardo Diaz, 27.07.2013.
   */
  public SelectItem[] getPrivilegesForSelectOneRadio () {
    SelectItem[] items = new SelectItem[EDiagramPrivilege.values().length - 1];
    
    int i = 0;
    for (EDiagramPrivilege p : EDiagramPrivilege.values()) {
      if (p == EDiagramPrivilege.OWNER) {
        continue;
      }
      
      items[i++] = new SelectItem(p, formatController.getDiagramPrivilegeName(p));
    }
    
    return items;
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
    
    if (mode == EDiagramControllerMode.DELETE && diagram != null && loggedUser != null) {
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
      String copyXMI = diagram.getXMI();
      
      diagram = new Diagram();
      diagram.setXMI(copyXMI);
      
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
    return diagram != null && loggedUser != null && !GenericUtils.isEmptyString(name);
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
        diagram.setCreatedBy(loggedUser);
        diagramService.insertDiagram(diagram);
        dashBoardController.addDiagram(diagram);
        break;
        
      case EDIT:
      
        diagram.setName(name);
        diagram.setDescription(description);
        diagram.setModifiedBy(loggedUser);
        diagramService.updateDiagram(diagram);
        
        break;
      case DELETE:
        diagramService.deleteDiagram(diagram.getKey());
        dashBoardController.deleteDiagram(diagram);
        break;
      
      case SHARE:
        diagramService.shareDiagram(diagram, selectedUsers, privilege);
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
