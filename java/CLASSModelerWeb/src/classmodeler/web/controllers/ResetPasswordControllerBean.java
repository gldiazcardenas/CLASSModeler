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
import classmodeler.web.resources.JSFResourceBundle;
import classmodeler.web.util.JSFFormControllerBean;
import classmodeler.web.util.JSFGenericBean;
import classmodeler.web.util.JSFMessageBean;
import classmodeler.web.util.JSFOutcomeUtil;

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
  private String email;
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
  
  /**
   * Loads the parameters from the request and verifies if the values are valid.
   * 
   * @author Gabriel Leonardo Diaz, 24.05.2013.
   */
  public void configure () {
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    
    String codeParameter = request.getParameter("code");
    String emailParameter = request.getParameter("email");
    
    try {
      valid = userService.isValidToResetPassword(emailParameter, codeParameter);
      email = emailParameter;
      if (!valid) {
        addErrorMessage("resetMessage", JSFResourceBundle.getLocalizedMessage("INVALID_VERIFICATION_CODE_USED_MESSAGE"), null);
      }
    }
    catch (InvalidUserAccountException e) {
      valid = false;
      if (e.getType() == EInvalidAccountErrorType.NON_EXISTING_ACCOUNT) {
        addErrorMessage("resetMessage", JSFResourceBundle.getLocalizedMessage("INVALID_ACCOUNT_NON_EXISTING_MESSAGE"), null);
      }
      else if (e.getType() == EInvalidAccountErrorType.DEACTIVATED_ACCOUNT) {
        addErrorMessage("resetMessage", JSFResourceBundle.getLocalizedMessage("INVALID_ACCOUNT_DEACTIVATED_MESSAGE"), null);
      }
      else {
        addErrorMessage("resetMessage", JSFResourceBundle.getLocalizedMessage("UNEXPECTED_EXCEPTION_MESSAGE"), e.getLocalizedMessage());
      }
    }
    catch (InvalidVerificationCodeException e) {
      valid = false;
      addErrorMessage("resetMessage", JSFResourceBundle.getLocalizedMessage("INVALID_VERIFICATION_CODE_MESSAGE"), null);
    }
    catch (ExpiredVerificationCodeException e) {
      valid = false;
      addErrorMessage("resetMessage", JSFResourceBundle.getLocalizedMessage("INVALID_VERIFICATION_CODE_EXPIRED_MESSAGE"), null);
    }
    catch (SendEmailException e) {
      valid = false;
      addErrorMessage("resetMessage", JSFResourceBundle.getLocalizedMessage("SEND_RESET_PASSWORD_EMAIL_MESSAGE"), null);
    }
  }

  @Override
  public String process() {
    String outcome = null;
    
    if (isAllValid()) {
      try {
        userService.resetPassword(email, password);
        outcome = JSFOutcomeUtil.INDEX + JSFOutcomeUtil.REDIRECT_SUFIX;
        addInformationMessage(JSFMessageBean.GENERAL_MESSAGE_ID,
                              JSFResourceBundle.getLocalizedMessage("RESET_PASSWORD_CONFIRMATION_MESSAGE"), null);
      }
      catch (InvalidUserAccountException e) {
        if (e.getType() == EInvalidAccountErrorType.NON_EXISTING_ACCOUNT) {
          addErrorMessage(JSFMessageBean.GENERAL_MESSAGE_ID,
                          JSFResourceBundle.getLocalizedMessage("INVALID_ACCOUNT_NON_EXISTING_MESSAGE"), null);
        }
        else if (e.getType() == EInvalidAccountErrorType.DEACTIVATED_ACCOUNT) {
          addErrorMessage(JSFMessageBean.GENERAL_MESSAGE_ID,
                          JSFResourceBundle.getLocalizedMessage("INVALID_ACCOUNT_DEACTIVATED_MESSAGE"), null);
        }
        else {
          addErrorMessage(JSFMessageBean.GENERAL_MESSAGE_ID,
                          JSFResourceBundle.getLocalizedMessage("UNEXPECTED_EXCEPTION_MESSAGE"), e.getLocalizedMessage());
        }
      }
    }
    
    return outcome;
  }
  
  @Override
  public void processAJAX() {
    // Not used.
  }

  @Override
  public boolean isAllValid() {
    return GenericUtils.isValidEmail(email) && !GenericUtils.isEmptyString(password) && GenericUtils.equals(password, confirmation);
  }

}
