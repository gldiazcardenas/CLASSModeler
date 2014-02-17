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
import classmodeler.domain.diagram.SharedItem;
import classmodeler.domain.user.Diagrammer;
import classmodeler.service.DiagramService;
import classmodeler.service.util.CollectionUtils;
import classmodeler.web.util.JSFGenericBean;
import classmodeler.web.util.JSFOutcomeUtil;

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
  private SharedItem shared;
  
  private List<SharedItem> sharings;
  private List<Diagram> diagrams;
  
  @ManagedProperty("#{sessionController.diagrammer}")
  private Diagrammer diagrammer;
  
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
      diagrams = diagramService.getDiagramsByDiagrammer(diagrammer); 
    }
    return diagrams;
  }
  
  /**
   * Gets the sharings of the selected diagram.
   * 
   * @return A list of shared objects.
   * @author Gabriel Leonardo Diaz, 26.07.2013.
   */
  public List<SharedItem> getSharings () {
    if (sharings == null) {
      sharings = new ArrayList<SharedItem>();
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
      sharings = diagramService.getSharedItemsByDiagram(diagram);
    }
    else {
      sharings = new ArrayList<SharedItem>();
    }
  }
  
  public SharedItem getShared() {
    return shared;
  }
  
  public void setShared(SharedItem shared) {
    this.shared = shared;
  }
  
  public void setDiagrammer(Diagrammer diagrammer) {
    this.diagrammer = diagrammer;
  }
  
  public void setDesignerController(DesignerControllerBean designerController) {
    this.designerController = designerController;
  }
  
  /**
   * Redirects the user to the Dashboard page.
   * 
   * @return The OUTCOME to the Dashboard page
   */
  public String goToDashboard () {
    designerController.tearDown();
    return JSFOutcomeUtil.DASHBOARD + JSFOutcomeUtil.REDIRECT_SUFIX;
  }
  
  /**
   * Determines if the logged user is able to share the selected diagram.
   * 
   * @return True if the user is owner the diagram.
   * @author Gabriel Leonardo Diaz, 27.07.2013.
   */
  public boolean isAllowedToShareDiagram () {
    if (this.diagram == null || this.diagrammer == null) {
      return false;
    }
    
    // This is the owner of the diagram
    return this.diagram.isOwner(diagrammer);
  }
  
  /**
   * Determines if the logged user is able to edit the selected diagram.
   * 
   * @return True if the user is the owner or has EDITION privilege.
   * @author Gabriel Leonardo Diaz, 27.07.2013.
   */
  public boolean isAllowedEditDiagram () {
    if (diagram == null || diagrammer == null) {
      return false;
    }
    
    // The owner is able to edit always
    if (this.diagram.isOwner(diagrammer)) {
      return true;
    }
    
    if (!CollectionUtils.isEmptyCollection(sharings)) {
      for (SharedItem sharing : sharings) {
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
    return diagram != null && diagrammer != null;
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
    if (shared == null || diagrammer == null) {
      return false;
    }
    
    return diagrammer.equals(shared.getDiagram().getCreatedBy());
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
