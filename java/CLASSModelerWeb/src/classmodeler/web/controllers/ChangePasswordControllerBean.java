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

import classmodeler.web.util.JSFFormControllerBean;
import classmodeler.web.util.JSFGenericBean;

/**
 * JSF Bean controller to process changing password operation for the logged
 * user.
 * 
 * @author Gabriel Leonardo Diaz, 02.06.2013.
 */
@ManagedBean(name="changePasswordController")
@ViewScoped
public class ChangePasswordControllerBean extends JSFGenericBean implements JSFFormControllerBean {

  private static final long serialVersionUID = 1L;
  
  private String oldPassword;
  private String newPassword;
  private String newConfirmation;
  
  @ManagedProperty("#{sessionController}")
  private SessionControllerBean sessionController;
  
  public ChangePasswordControllerBean() {
    super();
  }
  
  public String getOldPassword() {
    return oldPassword;
  }
  
  public void setOldPassword(String oldPassword) {
    this.oldPassword = oldPassword;
  }
  
  public String getNewConfirmation() {
    return newConfirmation;
  }
  
  public void setNewConfirmation(String newConfirmation) {
    this.newConfirmation = newConfirmation;
  }
  
  public String getNewPassword() {
    return newPassword;
  }
  
  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }
  
  public void setSessionController(SessionControllerBean sessionController) {
    this.sessionController = sessionController;
  }

  @Override
  public boolean isAllValid() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void processAJAX() {
    // TODO Auto-generated method stub
  }

  @Override
  public String process() {
    // TODO Auto-generated method stub
    return null;
  }
  
}
