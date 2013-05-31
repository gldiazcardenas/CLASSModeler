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
import javax.faces.bean.ViewScoped;

import classmodeler.domain.project.Project;
import classmodeler.service.ProjectService;
import classmodeler.service.util.GenericUtils;
import classmodeler.web.resources.JSFResourceBundle;
import classmodeler.web.util.JSFFormControllerBean;
import classmodeler.web.util.JSFGenericBean;

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
  
  public Project getProject() {
    return project;
  }
  
  public void setProject(Project project) {
    this.project = project;
    
    if (project != null) {
      name = project.getName();
      description = project.getDescription();
      
      if (project.getKey() > 0) {
        title = JSFResourceBundle.getLocalizedMessage("PROJECT_NEW_FORM_TITLE");
      }
      else {
        title = JSFResourceBundle.getLocalizedMessage("PROJECT_EDIT_FORM_TITLE", name);
      }
    }
  }
  
  @Override
  public boolean isAllValid() {
    return project != null && !GenericUtils.isEmptyString(project.getName());
  }

  @Override
  public void processAJAX() {
    if (!isAllValid()) {
      return;
    }
    
    project.setName(name);
    project.setDescription(description);
    
    if (project.getKey() > 0) {
      projectService.updateProject(project);
    }
    else {
      projectService.insertProject(project);
    }
  }

  @Override
  public String process() {
    return null; // Not used.
  }

}
