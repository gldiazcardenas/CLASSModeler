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
import javax.faces.bean.SessionScoped;

import classmodeler.domain.project.Project;
import classmodeler.domain.project.Shared;
import classmodeler.service.ProjectService;
import classmodeler.web.util.JSFGenericBean;

/**
 * JSF Bean controller for the DashBoard page, this handles all interaction of
 * the user with the GUI components of the page.
 * 
 * @author Gabriel Leonardo Diaz, 28.05.2013.
 */
@ManagedBean(name="dashBoardController")
@SessionScoped
public class DashboardControllerBean extends JSFGenericBean {

  private static final long serialVersionUID = 1L;
  
  private Project project;
  private Shared shared;
  
  private List<Project> projects;
  private List<Shared> sharings;
  
  @ManagedProperty("#{sessionController}")
  private SessionControllerBean sessionController;
  
  @EJB
  private ProjectService projectService;
  
  public DashboardControllerBean() {
    super();
    
    projects = new ArrayList<Project>();
    sharings = new ArrayList<Shared>();
  }
  
  public List<Project> getProjects() {
    return projects;
  }
  
  public List<Shared> getSharings() {
    return sharings;
  }
  
  public Project getProject() {
    return project;
  }
  
  public void setProject(Project project) {
    this.project = project;
  }
  
  public Shared getShared() {
    return shared;
  }
  
  public void setShared(Shared shared) {
    this.shared = shared;
  }
  
  public boolean isSelectedProject () {
    return project != null;
  }
  
  public void setSessionController(SessionControllerBean sessionController) {
    this.sessionController = sessionController;
  }
  
  /**
   * Adds a single project to the current cached list.
   * 
   * @param newProject
   *          The project to add.
   * @author Gabriel Leonardo Diaz, 01.06.2013.
   */
  public void addProject (Project newProject) {
    if (newProject == null) {
      return;
    }
    
    projects.add(newProject);
  }
  
  /**
   * Removes the given project from the current cached list.
   * 
   * @param deletedProject
   *          The project to delete.
   * @author Gabriel Leonardo Diaz, 01.06.2013.
   */
  public void deleteProject (Project deletedProject) {
    if (deletedProject == null) {
      return;
    }
    
    projects.remove(deletedProject);
  }
  
  /**
   * Loads the initial configurations and data for the DashBoard page.
   * 
   * @author Gabriel Leonardo Diaz, 28.05.2013.
   */
  public void configure () {
    projects = projectService.getAllProjectsByUser(sessionController.getLoggedRegisteredUser());
  }

}
