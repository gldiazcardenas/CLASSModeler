/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.controllers;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import classmodeler.domain.diagram.Diagram;
import classmodeler.domain.diagram.EDiagramPrivilege;
import classmodeler.domain.user.EUserAccountStatus;
import classmodeler.domain.user.User;
import classmodeler.service.util.CollectionUtils;
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
  
  private Diagram diagram;
  private EDiagramPrivilege privilege;
  private List<User> users;
  
  public ShareControllerBean() {
    super();
  }
  
  public void setDashBoardController(DashboardControllerBean dashBoardController) {
    this.dashBoardController = dashBoardController;
  }
  
  public void setSessionController(SessionControllerBean sessionController) {
    this.sessionController = sessionController;
  }
  
  public Diagram getDiagram() {
    return diagram;
  }
  
  public void setDiagram(Diagram diagram) {
    this.diagram = diagram;
  }
  
  public EDiagramPrivilege getPrivilege() {
    return privilege;
  }
  
  public void setPrivilege(EDiagramPrivilege privilege) {
    this.privilege = privilege;
  }
  
  public List<User> getUsers() {
    return users;
  }
  
  public void setUsers(List<User> users) {
    this.users = users;
  }

  @Override
  public boolean isAllValid() {
    if (diagram == null || privilege == null || CollectionUtils.isEmptyCollection(users)) {
      return false;
    }
    
    for (User user : users) {
      if (user.getAccountStatus() != EUserAccountStatus.ACTIVATED) {
        return false;
      }
    }
    
    return true;
  }

  @Override
  public void processAJAX() {
    if (!isAllValid()) {
      return;
    }
    
    // TODO Auto-generated method stub
  }

  @Override
  public String process() {
    return null; // Not used.
  }
  
}
