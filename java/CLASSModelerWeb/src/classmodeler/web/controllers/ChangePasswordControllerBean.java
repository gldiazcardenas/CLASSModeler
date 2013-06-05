/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * C�cuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.controllers;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import classmodeler.domain.user.User;
import classmodeler.service.UserService;
import classmodeler.service.util.GenericUtils;
import classmodeler.web.resources.JSFResourceBundle;
import classmodeler.web.util.JSFFormControllerBean;
import classmodeler.web.util.JSFGenericBean;
import classmodeler.web.util.JSFMessageBean;

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
  
  @ManagedProperty("#{sessionController.loggedRegisteredUser}")
  private User loggedUser;
  
  @EJB
  private UserService userService;
  
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
  
  public void setLoggedUser(User loggedUser) {
    this.loggedUser = loggedUser;
  }

  @Override
  public boolean isAllValid() {
    if (loggedUser == null) {
      return false;
    }
    
    if (!GenericUtils.equals(oldPassword, loggedUser.getPassword())) {
      return false;
    }
    
    if (GenericUtils.isEmptyString(newConfirmation) || !GenericUtils.equals(newPassword, newConfirmation)) {
      return false;
    }
    
    return true;
  }

  @Override
  public void processAJAX() {
    if (!isAllValid()) {
      return;
    }
    
    loggedUser.setPassword(newPassword);
    userService.updateUser(loggedUser);
    
    addInformationMessage(JSFMessageBean.GENERAL_MESSAGE_ID, JSFResourceBundle.getLocalizedMessage("SAVED_SUCCESSFULLY_MESSAGE"), null);
  }

  @Override
  public String process() {
    return null; // Not null.
  }
  
}