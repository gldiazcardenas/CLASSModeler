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

import classmodeler.service.UserService;
import classmodeler.service.util.GenericUtils;
import classmodeler.web.util.JSFFormControllerBean;
import classmodeler.web.util.JSFGenericBean;

/**
 * JSF Bean controller for the reset password form.
 * 
 * @author Gabriel Leonardo Diaz, 23.05.2013.
 */
@ManagedBean(name="resetPasswordController")
@ViewScoped
public class ResetPasswordControllerBean extends JSFGenericBean implements JSFFormControllerBean {

  private static final long serialVersionUID = 1L;

  private String email;
  private String hashCode;
  private String password;
  private String confirmation;
  
  @EJB
  private UserService userService;
  
  public ResetPasswordControllerBean() {
    super();
  }
  
  public String getPassword() {
    return password;
  }
  
  public void setPassword(String password) {
    this.password = password;
  }
  
  public String getConfirmation() {
    return confirmation;
  }
  
  public void setConfirmation(String confirmation) {
    this.confirmation = confirmation;
  }
  
  public String getEmail() {
    return email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
  public String getHashCode() {
    return hashCode;
  }
  
  public void setHashCode(String hashCode) {
    this.hashCode = hashCode;
  }

  @Override
  public String process() {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void processAJAX() {
    // TODO Auto-generated method stub
  }

  @Override
  public boolean isAllValid() {
    return !GenericUtils.isEmptyString(password) && GenericUtils.equals(password, confirmation);
  }

}
