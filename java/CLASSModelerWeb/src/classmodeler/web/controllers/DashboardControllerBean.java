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
import classmodeler.web.resources.JSFResourceBundle;
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
  
  private Project selectedProject;
  private Shared selectedShared;
  
  private List<Project> projects;
  private List<Shared> sharings;
  
  // Used for the project form
  private String operationTitle = JSFResourceBundle.getLocalizedMessage("BUTTON_NEW_PROJECT_LABEL");
  private String name;
  private String description;
  
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
  
  public Project getSelectedProject() {
    return selectedProject;
  }
  
  public void setSelectedProject(Project selectedProject) {
    this.selectedProject = selectedProject;
  }
  
  public Shared getSelectedShared() {
    return selectedShared;
  }
  
  public void setSelectedShared(Shared selectedShared) {
    this.selectedShared = selectedShared;
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
  
  public String getOperationTitle() {
    return operationTitle;
  }
  
  /**
   * Loads the initial configurations and data for the DashBoard page.
   * 
   * @author Gabriel Leonardo Diaz, 28.05.2013.
   */
  public void configure () {
    // TODO
    System.out.println();
    
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
   * Creates a new projects and adds it to the current list.
   * 
   * @author Gabriel Leonardo Diaz, 28.05.2013.
   */
  public void newProject () {
    // TODO
    System.out.println();
  }

}
