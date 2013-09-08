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

import classmodeler.domain.user.Diagrammer;
import classmodeler.service.UserService;
import classmodeler.service.util.GenericUtils;
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
  
  @ManagedProperty("#{sessionController.loggedRegisteredUser}")
  private Diagrammer loggedUser;
  
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
  
  public void setLoggedUser(Diagrammer loggedUser) {
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
    userService.updateDiagrammer(loggedUser);
    
    addInformationMessage(JSFGenericBean.GENERAL_MESSAGE_ID, GenericUtils.getLocalizedMessage("SAVED_SUCCESSFULLY_MESSAGE"), null);
  }

  @Override
  public String process() {
    return null; // Not null.
  }
  
}
