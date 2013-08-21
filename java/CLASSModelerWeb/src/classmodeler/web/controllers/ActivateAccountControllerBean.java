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
import classmodeler.service.exception.InvalidUserAccountException.EInvalidAccountErrorType;
import classmodeler.service.exception.InvalidVerificationCodeException;
import classmodeler.service.exception.SendEmailException;
import classmodeler.service.util.GenericUtils;
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
  
  @EJB
  private UserService userService;
  private boolean successful;
  private String message;
  
  public ActivateAccountControllerBean() {
    super();
  }
  
  public boolean isSuccessful() {
    return successful;
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
      
      successful = true;
      message = GenericUtils.getLocalizedMessage("ACCOUNT_ACTIVATION_SUCCESSFUL_MESSAGE");
    }
    catch (ExpiredVerificationCodeException e) {
      successful = false;
      message = GenericUtils.getLocalizedMessage("INVALID_VERIFICATION_CODE_EXPIRED_MESSAGE");
    }
    catch (InvalidVerificationCodeException e) {
      successful = false;
      message = GenericUtils.getLocalizedMessage("INVALID_VERIFICATION_CODE_MESSAGE");
    }
    catch (InvalidUserAccountException e) {
      successful = false;
      message = GenericUtils.getLocalizedMessage("INVALID_ACCOUNT_NON_EXISTING_MESSAGE");
      if (e.getType() == EInvalidAccountErrorType.NON_EXISTING_ACCOUNT) {
        message = GenericUtils.getLocalizedMessage("INVALID_ACCOUNT_NON_EXISTING_MESSAGE");
      }
      else if (e.getType() == EInvalidAccountErrorType.ACTIVATED_ACCOUNT) {
        message = GenericUtils.getLocalizedMessage("ACCOUNT_ACTIVATION_ACTIVATED_MESSAGE");
      }
      else if (e.getType() == EInvalidAccountErrorType.DEACTIVATED_ACCOUNT) {
        message = GenericUtils.getLocalizedMessage("INVALID_ACCOUNT_DEACTIVATED_MESSAGE");
      }
      else {
        // Should not happen
        message = GenericUtils.getLocalizedMessage("UNEXPECTED_EXCEPTION_MESSAGE");
      }
    }
    catch (SendEmailException e) {
      message = GenericUtils.getLocalizedMessage("SEND_ACTIVATION_EMAIL_MESSAGE");
    }
  }

}
