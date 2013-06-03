/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.controllers;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import classmodeler.domain.project.Project;
import classmodeler.web.util.JSFFormControllerBean;
import classmodeler.web.util.JSFGenericBean;

/**
 * JSF Bean controller to handle user interactions about sharing projects.
 * 
 * @author Gabriel Leonardo Diaz, 02.06.2013.
 */
@ManagedBean(name="shareController")
@ViewScoped
public class ShareControllerBean extends JSFGenericBean implements JSFFormControllerBean {

  private static final long serialVersionUID = 1L;
  
  @ManagedProperty("#{dashBoardController}")
  private DashboardControllerBean dashBoardController;
  
  @ManagedProperty("#{sessionController}")
  private SessionControllerBean sessionController;
  
  private Project project;
  
  public ShareControllerBean() {
    super();
  }
  
  public void setDashBoardController(DashboardControllerBean dashBoardController) {
    this.dashBoardController = dashBoardController;
  }
  
  public void setSessionController(SessionControllerBean sessionController) {
    this.sessionController = sessionController;
  }

  @Override
  public boolean isAllValid() {
    return project != null;
  }

  @Override
  public void processAJAX() {
    // TODO Auto-generated method stub
  }

  @Override
  public String process() {
    return null; // Not used.
  }
  
  
}
