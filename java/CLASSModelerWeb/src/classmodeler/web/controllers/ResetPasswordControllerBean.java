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
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import classmodeler.service.UserService;
import classmodeler.service.exception.InvalidUserAccountException;
import classmodeler.service.exception.InvalidVerificationCodeException;
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
  
  private boolean valid;
  private String message;
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
  
  public boolean isValid() {
    return valid;
  }
  
  public String getMessage() {
    return message;
  }
  
  /**
   * Loads the parameters from the request and verifies if the values are valid.
   * 
   * @author Gabriel Leonardo Diaz, 24.05.2013.
   */
  public void configure () {
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    
    String code = request.getParameter("code");
    String email = request.getParameter("email");
    
    try {
      valid = userService.isValidToResetPassword(email, code);
    }
    catch (InvalidUserAccountException e) {
      valid = false;
    }
    catch (InvalidVerificationCodeException e) {
      valid = false;
    }
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
