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
  
  public ActivateAccountControllerBean() {
    super();
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
      
      addInformationMessage("activationMessage", GenericUtils.getLocalizedMessage("ACCOUNT_ACTIVATION_SUCCESSFUL_MESSAGE"), null);
    }
    catch (ExpiredVerificationCodeException e) {
      addErrorMessage("activationMessage", GenericUtils.getLocalizedMessage("INVALID_VERIFICATION_CODE_EXPIRED_MESSAGE"), null);
    }
    catch (InvalidVerificationCodeException e) {
      addErrorMessage("activationMessage", GenericUtils.getLocalizedMessage("INVALID_VERIFICATION_CODE_MESSAGE"), null);
    }
    catch (InvalidUserAccountException e) {
      if (e.getType() == EInvalidAccountErrorType.NON_EXISTING_ACCOUNT) {
        addErrorMessage("activationMessage", GenericUtils.getLocalizedMessage("INVALID_ACCOUNT_NON_EXISTING_MESSAGE"), null);
      }
      else if (e.getType() == EInvalidAccountErrorType.ACTIVATED_ACCOUNT) {
        addErrorMessage("activationMessage", GenericUtils.getLocalizedMessage("ACCOUNT_ACTIVATION_ACTIVATED_MESSAGE"), null);
      }
      else if (e.getType() == EInvalidAccountErrorType.DEACTIVATED_ACCOUNT) {
        addErrorMessage("activationMessage", GenericUtils.getLocalizedMessage("INVALID_ACCOUNT_DEACTIVATED_MESSAGE"), null);
      }
      else {
        // Should not happen
        addErrorMessage("activationMessage", GenericUtils.getLocalizedMessage("UNEXPECTED_EXCEPTION_MESSAGE"), e.getLocalizedMessage());
      }
    }
    catch (SendEmailException e) {
      addErrorMessage("activationMessage", GenericUtils.getLocalizedMessage("SEND_ACTIVATION_EMAIL_MESSAGE"), null);
    }
  }

}
