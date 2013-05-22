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
import classmodeler.web.resources.JSFResourceBundle;
import classmodeler.web.util.JSFFormControllerBean;
import classmodeler.web.util.JSFGenericBean;
import classmodeler.web.util.JSFOutcomeUtil;

/**
 * JSF Bean controller for Reset Password form.
 * 
 * @author Gabriel Leonardo Diaz, 21.05.2013.
 */
@ManagedBean (name="forgotPasswordController")
@ViewScoped
public class ForgotPasswordControllerBean extends JSFGenericBean implements JSFFormControllerBean {

  private static final long serialVersionUID = 1L;
  
  private String email;
  
  @EJB
  private UserService userService;
  
  public ForgotPasswordControllerBean() {
    super();
  }
  
  public String getEmail() {
    return email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String actionPerformed() {
    String outcome = null;
    
    if (isAllValid()) {
      userService.resetPassword(email);
      outcome = JSFOutcomeUtil.INDEX;
      addInformationMessage("customMessage", JSFResourceBundle.getLocalizedMessage("FORGOT_PASSWORD_CONFIRMATION_MESSAGE"), null);
    }
    
    return outcome;
  }

  @Override
  public boolean isAllValid() {
    return !GenericUtils.isEmptyString(email);
  }

}
