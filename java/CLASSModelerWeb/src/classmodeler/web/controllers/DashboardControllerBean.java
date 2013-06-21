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
import classmodeler.domain.diagram.Shared;
import classmodeler.domain.user.User;
import classmodeler.service.DiagramService;
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
  private Shared shared;
  
  private List<Diagram> diagrams;
  private List<Shared> sharings;
  
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
   * Gets the projects belonging to the user or shared by other users. This
   * method loads the projects from the Service.
   * 
   * @return A list of projects.
   */
  public List<Diagram> getDiagrams() {
    if (diagrams == null) {
      diagrams = diagramService.getAllDiagramsByUser(loggedUser); 
    }
    return diagrams;
  }
  
  /**
   * Gets the sharings of the selected project.
   * 
   * @return A list of sharing bean.
   */
  public List<Shared> getSharings() {
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
  }
  
  public Shared getShared() {
    return shared;
  }
  
  public void setShared(Shared shared) {
    this.shared = shared;
  }
  
  public boolean isSelectedDiagram () {
    return diagram != null;
  }
  
  public void setLoggedUser(User loggedUser) {
    this.loggedUser = loggedUser;
  }
  
  public void setDesignerController(DesignerControllerBean designerController) {
    this.designerController = designerController;
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
  
  /**
   * Prepares the designer for the selected project.
   * 
   * @return The outcome to the DESIGNER page.
   * @author Gabriel Leonardo Diaz, 01.06.2013.
   */
  public String prepareEditDiagram () {
    designerController.setDiagram(diagram);
    return JSFOutcomeUtil.DESIGNER;
  }

}
