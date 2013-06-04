/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.controllers;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import classmodeler.domain.project.Project;
import classmodeler.domain.user.User;
import classmodeler.service.ProjectService;
import classmodeler.service.util.GenericUtils;
import classmodeler.web.resources.JSFResourceBundle;
import classmodeler.web.util.JSFFormControllerBean;
import classmodeler.web.util.JSFGenericBean;
import classmodeler.web.util.JSFMessageBean;

/**
 * JSF Bean controller to manage the interactions of the user with the form to
 * create and edit projects.
 * 
 * @author Gabriel Leonardo Diaz, 30.05.2013.
 */
@ManagedBean(name="projectController")
@ViewScoped
public class ProjectControllerBean extends JSFGenericBean implements JSFFormControllerBean {

  private static final long serialVersionUID = 1L;
  
  private String name;
  private String description;
  private String title;
  private Project project;
  private EProjectControllerMode mode;
  
  @ManagedProperty("#{dashBoardController}")
  private DashboardControllerBean dashBoardController;
  
  @ManagedProperty("#{sessionController.loggedRegisteredUser}")
  private User loggedUser;
  
  @EJB
  private ProjectService projectService;
  
  public ProjectControllerBean() {
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
  
  public void setLoggedUser(User loggedUser) {
    this.loggedUser = loggedUser;
  }
  
  /**
   * Prepares the controller to create a new project. Sets an empty object to
   * the controller.
   * 
   * @author Gabriel Leonardo Diaz, 28.05.2013.
   */
  public void prepareNewProject () {
    name        = null;
    description = null;
    project     = new Project();
    title       = JSFResourceBundle.getLocalizedMessage("PROJECT_NEW_FORM_TITLE");
    mode        = EProjectControllerMode.CREATE;
  }
  
  /**
   * Prepares the controller to edit a project by getting the selected project
   * instance {@link DashboardControllerBean#project}, if the object is null
   * this method does nothing.
   * 
   * @author Gabriel Leonardo Diaz, 28.05.2013.
   */
  public void prepareEditProject () {
    project = dashBoardController.getProject();
    if (project != null) {
      name        = project.getName();
      description = project.getDescription();
      title       = JSFResourceBundle.getLocalizedMessage("PROJECT_EDIT_FORM_TITLE", name);
      mode        = EProjectControllerMode.EDIT;
    }
  }
  
  /**
   * Prepares the controller to delete the selected project in the table, this
   * method takes the object from {@link DashboardControllerBean}.
   * 
   * @author Gabriel Leonardo Diaz, 01.06.2013.
   */
  public void prepareDeleteProject () {
    project = dashBoardController.getProject();
    if (project != null) {
      name   = project.getName();
      title  = JSFResourceBundle.getLocalizedMessage("PROJECT_DELETE_FORM_TITLE", name);
      mode   = EProjectControllerMode.DELETE;
    }
  }
  
  /**
   * Prepares the controller to create a new project based on the information
   * copied from the selected project.
   * 
   * @author Gabriel Leonardo Diaz, 02.06.2013.
   */
  public void prepareCopyProject () {
    project = dashBoardController.getProject();
    if (project != null) {
      name        = project.getName();
      description = project.getDescription();
      title       = JSFResourceBundle.getLocalizedMessage("PROJECT_COPY_FORM_TITLE", name);
      mode        = EProjectControllerMode.COPY;
    }
  }
  
  @Override
  public boolean isAllValid() {
    return project != null && loggedUser != null && !GenericUtils.isEmptyString(name);
  }

  @Override
  public void processAJAX() {
    if (!isAllValid()) {
      return;
    }
    
    try {
      switch (mode) {
      case CREATE:
      case EDIT:
      case COPY:
        project.setName(name);
        project.setDescription(description);
        project.setModifiedBy(loggedUser);
        
        if (mode == EProjectControllerMode.EDIT) {
          projectService.updateProject(project);
        }
        else {
          // Create mode set an empty XMI representation, In Copy Mode keep the XMI of the project.
          if (mode == EProjectControllerMode.CREATE) {
            project.setProjectXMI("");
          }
          
          project.setCreatedBy(loggedUser);
          projectService.insertProject(project);
          dashBoardController.addProject(project);
        }
        
        break;
      case DELETE:
        projectService.deleteProject(project.getKey());
        dashBoardController.deleteProject(project);
        break;
        
      default:
        break;
      }
      
      project     = null;
      name        = null;
      description = null;
      title       = null;
      mode        = null;
    }
    catch (Exception e) {
      addErrorMessage(JSFMessageBean.GENERAL_MESSAGE_ID, JSFResourceBundle.getLocalizedMessage("UNEXPECTED_EXCEPTION_MESSAGE"), e.getMessage());
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
  public enum EProjectControllerMode {
    CREATE,
    EDIT,
    DELETE,
    COPY,
  }

}
