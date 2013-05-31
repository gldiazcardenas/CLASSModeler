/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import classmodeler.domain.project.Project;
import classmodeler.domain.project.Shared;
import classmodeler.domain.user.User;
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
  
  /**
   * Loads the initial configurations and data for the DashBoard page.
   * 
   * @author Gabriel Leonardo Diaz, 28.05.2013.
   */
  public void configure () {
    // TODO
    
    Project project = new Project();
    project.setName("MyProject");
    project.setDescription("MyDescription");
    
    User user = new User();
    
    project.setCreatedBy(user);
    project.setModifiedBy(user);
    project.setModifiedDate(Calendar.getInstance().getTime());
    project.setCreatedDate(Calendar.getInstance().getTime());
    
    projects.add(project);
  }
  
  /**
   * Prepares the controller ({@link ProjectControllerBean}) to create a
   * new project. Sets an empty object to the controller.
   * 
   * @author Gabriel Leonardo Diaz, 28.05.2013.
   */
  public void prepareNewProject () {
    ProjectControllerBean projectController = getJSFBean("projectController", ProjectControllerBean.class);
    if (projectController != null) {
      projectController.setProject(new Project());
    }
  }
  
  /**
   * Prepares the controller ({@link ProjectControllerBean}) to edit a
   * project by setting the selected project instance {@link #project}, if the
   * object is null this method does nothing.
   * 
   * @author Gabriel Leonardo Diaz, 28.05.2013.
   */
  public void prepareEditProject () {
    ProjectControllerBean projectController = getJSFBean("projectController", ProjectControllerBean.class);
    if (projectController != null) {
      if (project != null) {
        projectController.setProject(project);
      }
      else {
      }
    }
  }

}
