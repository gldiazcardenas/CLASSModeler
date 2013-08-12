/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import classmodeler.domain.diagram.Diagram;
import classmodeler.domain.diagram.EDiagramPrivilege;
import classmodeler.domain.diagram.Shared;
import classmodeler.domain.user.User;
import classmodeler.service.DiagramService;
import classmodeler.service.util.CollectionUtils;
import classmodeler.web.util.JSFGenericBean;

/**
 * JSF Bean controller for the DashBoard page, this handles all interaction of
 * the user with the GUI components of the page.
 * 
 * @author Gabriel Leonardo Diaz, 28.05.2013.
 */
@ManagedBean(name="dashBoardController")
@ViewScoped
public class DashboardControllerBean extends JSFGenericBean {

  private static final long serialVersionUID = 1L;
  
  private Diagram diagram;
  private Shared shared;
  
  private List<Shared> sharings;
  private List<Diagram> diagrams;
  
  @ManagedProperty("#{sessionController.loggedRegisteredUser}")
  private User loggedUser;
  
  @ManagedProperty("#{designerController}")
  private DesignerControllerBean designerController;
  
  @EJB
  private DiagramService diagramService;
  
  public DashboardControllerBean() {
    super();
  }
  
  /**
   * Gets the diagrams belonging to the user or shared by other users. This
   * method loads them from the Service.
   * 
   * @return A list of diagrams.
   */
  public List<Diagram> getDiagrams () {
    if (diagrams == null) {
      diagrams = diagramService.getAllDiagramsByUser(loggedUser); 
    }
    return diagrams;
  }
  
  /**
   * Gets the sharings of the selected diagram.
   * 
   * @return A list of shared objects.
   * @author Gabriel Leonardo Diaz, 26.07.2013.
   */
  public List<Shared> getSharings () {
    if (sharings == null) {
      sharings = new ArrayList<Shared>();
    }
    return sharings;
  }
  
  public Diagram getDiagram() {
    return diagram;
  }
  
  public void setDiagram(Diagram diagram) {
    this.diagram = diagram;
    this.shared = null;
    
    if (diagram != null) {
      sharings = diagramService.getSharingsByDiagram(diagram);
    }
    else {
      sharings = new ArrayList<Shared>();
    }
  }
  
  public Shared getShared() {
    return shared;
  }
  
  public void setShared(Shared shared) {
    this.shared = shared;
  }
  
  public void setLoggedUser(User loggedUser) {
    this.loggedUser = loggedUser;
  }
  
  public void setDesignerController(DesignerControllerBean designerController) {
    this.designerController = designerController;
  }
  
  /**
   * Determines if the logged user is able to share the selected diagram.
   * 
   * @return True if the user is owner of has SHARE privilege over the diagram.
   * @author Gabriel Leonardo Diaz, 27.07.2013.
   */
  public boolean isAllowedShareDiagram () {
    if (diagram == null || loggedUser == null) {
      return false;
    }
    
    if (!CollectionUtils.isEmptyCollection(sharings)) {
      for (Shared sharing : sharings) {
        if (diagram.equals(sharing.getDiagram()) && sharing.getPrivilege().isGreaterThan(EDiagramPrivilege.EDIT)) {
          return true;
        }
      }
    }
    
    return false;
  }
  
  /**
   * Determines if the logged user is able to edit the selected diagram.
   * 
   * @return True if the user is the owner or has EDITION privilege.
   * @author Gabriel Leonardo Diaz, 27.07.2013.
   */
  public boolean isAllowedEditDiagram () {
    if (diagram == null || loggedUser == null) {
      return false;
    }
    
    if (!CollectionUtils.isEmptyCollection(sharings)) {
      for (Shared sharing : sharings) {
        if (diagram.equals(sharing.getDiagram()) && sharing.getPrivilege().isGreaterThan(EDiagramPrivilege.READ)) {
          return true;
        }
      }
    }
    
    return false;
  }
  
  /**
   * Determines if the logged user is able to copy the selected diagram.
   * 
   * @return True if the user is the owner or has EDITION privileges.
   * @author Gabriel Leonardo Diaz, 27.07.2013.
   */
  public boolean isAllowedCopyDiagram () {
    return isAllowedEditDiagram();
  }
  
  /**
   * Determines if the logged user is able to delete the selected diagram.
   * 
   * @return True if there is a diagram selected.
   * @author Gabriel Leonardo Diaz, 27.07.2013.
   */
  public boolean isAllowedDeleteDiagram () {
    return diagram != null && loggedUser != null;
  }
  
  /**
   * Determines if the logged user is able to change the privilege of the
   * diagram shared.
   * 
   * @return True if the user is the owner of the diagram, but the selected
   *         object has not OWNER privilege itself.
   * @author Gabriel Leonardo Diaz, 27.07.2013.
   */
  public boolean isAllowedChangePrivilege () {
    if (shared == null || loggedUser == null) {
      return false;
    }
    
    return shared.getPrivilege() != EDiagramPrivilege.OWNER && loggedUser.equals(shared.getDiagram().getCreatedBy());
  }
  
  /**
   * Determines if the logged user is able to delete the privilege of the
   * diagram shared.
   * 
   * @return True if the user is the owner of the diagram, but the selected
   *         object has not OWNER privilege itself.
   * @author Gabriel Leonardo Diaz, 27.07.2013.
   */
  public boolean isAllowedDeletePrivilege () {
    return false;
  }
  
  /**
   * Adds a single project to the current cached list.
   * 
   * @param newDiagram
   *          The project to add.
   * @author Gabriel Leonardo Diaz, 01.06.2013.
   */
  public void addDiagram (Diagram newDiagram) {
    if (newDiagram == null) {
      return;
    }
    
    diagrams.add(newDiagram);
  }
  
  /**
   * Removes the given project from the current cached list.
   * 
   * @param deletedProject
   *          The project to delete.
   * @author Gabriel Leonardo Diaz, 01.06.2013.
   */
  public void deleteDiagram (Diagram deletedDiagram) {
    if (deletedDiagram == null) {
      return;
    }
    
    // Remove from the list
    diagrams.remove(deletedDiagram);
    
    // Remove the selected object
    if (deletedDiagram.equals(diagram)) {
      diagram = null;
    }
  }
}
