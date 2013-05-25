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
import classmodeler.service.exception.ExpiredVerificationCodeException;
import classmodeler.service.exception.InvalidUserAccountException;
import classmodeler.service.exception.SendEmailException;
import classmodeler.web.resources.JSFResourceBundle;
import classmodeler.web.util.JSFGenericBean;

/**
 * JSF Bean used to activate the user account.
 * 
 * @author Gabriel Leonardo Diaz, 20.05.2013.
 */
@ManagedBean(name="activateAccountController")
@ViewScoped
public class ActivateAccountControllerBean extends JSFGenericBean {
  
  private static final long serialVersionUID = 1L;
  
  private String message;
  
  @EJB
  private UserService userService;
  
  public ActivateAccountControllerBean() {
    super();
  }
  
  public String getMessage() {
    return message;
  }
  
  /**
   * Executes the activation process, takes the request parameters and invokes
   * the activation service.
   * 
   * @author Gabriel Leonardo Diaz, 24.05.2013.
   */
  public void process () {
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    
    String code = request.getParameter("code");
    String email = request.getParameter("email");
    
    try {
      userService.activateUserAccount(email, code);
      message = JSFResourceBundle.getLocalizedMessage("ACCOUNT_ACTIVATION_SUCCESSFUL_MESSAGE");
    }
    catch (ExpiredVerificationCodeException e) {
      message = JSFResourceBundle.getLocalizedMessage("ACCOUNT_ACTIVATION_EXPIRED_CODE_MESSAGE");
    }
    catch (InvalidUserAccountException e) {
      message = JSFResourceBundle.getLocalizedMessage("ACCOUNT_ACTIVATION_INVALID_STATE_MESSAGE");
    }
    catch (SendEmailException e) {
      message = JSFResourceBundle.getLocalizedMessage("SIGN_UP_FORM_ACTIVATION_EMAIL_MESSAGE");
    }
  }

}
