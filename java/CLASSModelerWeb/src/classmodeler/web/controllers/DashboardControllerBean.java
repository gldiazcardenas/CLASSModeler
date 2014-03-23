/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.controllers;

import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import classmodeler.domain.diagram.Diagram;
import classmodeler.domain.share.SharedItem;
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
  private SharedItem sharedItem;
  
  private List<SharedItem> sharedItems;
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
  public List<SharedItem> getSharedItems () {
    return sharedItems;
  }
  
  public Diagram getDiagram() {
    return diagram;
  }
  
  public void setDiagram(Diagram diagram) {
    this.diagram    = diagram;
    this.sharedItem = null;
    
    if (diagram != null) {
      sharedItems = diagramService.getSharedItemsByDiagram(diagram);
    }
    else {
      sharedItems = null;
    }
  }
  
  public SharedItem getSharedItem () {
    return sharedItem;
  }
  
  public void setSharedItem (SharedItem sharedItem) {
    this.sharedItem = sharedItem;
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
    designerController.destroy();
    return JSFOutcomeUtil.DASHBOARD + JSFOutcomeUtil.REDIRECT_SUFIX;
  }
  
  /**
   * Determines if the logged user is able to edit the selected diagram.
   * 
   * @return True if the user is the owner or has EDITION privilege.
   * @author Gabriel Leonardo Diaz, 27.07.2013.
   */
  public boolean isCanEditDiagram () {
    if (diagram == null) {
      return false;
    }
    
    // The owner is able to edit always
    if (this.diagram.isOwner(diagrammer)) {
      return true;
    }
    
    if (!CollectionUtils.isEmptyCollection(sharedItems)) {
      for (SharedItem item : sharedItems) {
        if (diagram.equals(item.getDiagram()) && item.isWriteable()) {
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
  public boolean isCanCopyDiagram () {
    return isCanEditDiagram();
  }
  
  /**
   * Determines if the logged user is able to delete the selected diagram.
   * 
   * @return True if there is a diagram selected.
   * @author Gabriel Leonardo Diaz, 27.07.2013.
   */
  public boolean isCanDeleteDiagram () {
    return this.diagram != null;
  }
  
  /**
   * Determines if the logged user is able to share the selected diagram.
   * 
   * @return True if the user is owner the diagram.
   * @author Gabriel Leonardo Diaz, 27.07.2013.
   */
  public boolean isCanShareDiagram () {
    return this.diagram != null && this.diagram.isOwner(diagrammer);
  }
  
  /**
   * Determines if the logged user is able to change the privilege of the
   * diagram shared.
   * 
   * @return True if the user is the owner of the diagram, but the selected
   *         object has not OWNER privilege itself.
   * @author Gabriel Leonardo Diaz, 27.07.2013.
   */
  public boolean isCanChangePrivilege () {
    return sharedItem != null && isCanShareDiagram();
  }
  
  /**
   * Determines if the logged user is able to delete the privilege of the
   * diagram shared.
   * 
   * @return True if the user is the owner of the diagram, but the selected
   *         object has not OWNER privilege itself.
   * @author Gabriel Leonardo Diaz, 27.07.2013.
   */
  public boolean isCanRemovePrivilege () {
    return isCanChangePrivilege();
  }
  
  /**
   * Adds a single diagram to the current cached list.
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
   * Removes the given diagram from the current cached list.
   * 
   * @param oldDiagram
   *          The diagram to delete.
   * @author Gabriel Leonardo Diaz, 01.06.2013.
   */
  public void deleteDiagram (Diagram oldDiagram) {
    if (oldDiagram == null) {
      return;
    }
    
    // Remove from the list
    diagrams.remove(oldDiagram);
    
    // Remove the selected object
    if (oldDiagram.equals(diagram)) {
      diagram = null;
    }
    
    // Remove list of shared items
    if (!CollectionUtils.isEmptyCollection(sharedItems)) {
      for (Iterator<SharedItem> iterator = sharedItems.iterator(); iterator.hasNext();) {
        if (iterator.next().getDiagram().equals(oldDiagram)) {
          iterator.remove();
        }
      }
    }
    
    // Remove shared item selected
    if (sharedItem != null && sharedItem.getDiagram().equals(oldDiagram)) {
      sharedItem = null;
    }
  }
  
  /**
   * Removes the shared item from the local list and removes the reference in
   * this controller.
   * 
   * @param oldSharedItem
   * @author Gabriel Leonardo Diaz, 23.03.2014.
   */
  public void deleteSharedItem (SharedItem oldSharedItem) {
    if (oldSharedItem == null) {
      return;
    }
    
    if (sharedItems != null) {
      sharedItems.remove(oldSharedItem);
    }
    
    if (oldSharedItem.equals(sharedItem)) {
      sharedItem = null;
    }
  }
}
