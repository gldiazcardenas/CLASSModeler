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
import classmodeler.service.exception.InvalidUserAccountException;
import classmodeler.service.exception.InvalidUserAccountException.EInvalidAccountErrorType;
import classmodeler.service.exception.SendEmailException;
import classmodeler.service.util.GenericUtils;
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
  public String process() {
    String outcome = null;
    
    if (isAllValid()) {
      try {
        userService.sendResetPasswordEmail(email);
        outcome = JSFOutcomeUtil.INDEX + JSFOutcomeUtil.REDIRECT_SUFIX;
        addInformationMessage(JSFGenericBean.GENERAL_MESSAGE_ID, GenericUtils.getLocalizedMessage("FORGOT_PASSWORD_CONFIRMATION_MESSAGE"), null);
      }
      catch (InvalidUserAccountException e) {
        if (e.getType() == EInvalidAccountErrorType.NON_EXISTING_ACCOUNT) {
          addErrorMessage(JSFGenericBean.GENERAL_MESSAGE_ID, GenericUtils.getLocalizedMessage("INVALID_ACCOUNT_NON_EXISTING_MESSAGE"), null);
        }
        else if (e.getType() == EInvalidAccountErrorType.DEACTIVATED_ACCOUNT) {
          addErrorMessage(JSFGenericBean.GENERAL_MESSAGE_ID, GenericUtils.getLocalizedMessage("INVALID_ACCOUNT_DEACTIVATED_MESSAGE"), null);
        }
        else {
          // Should not happen
          addErrorMessage(JSFGenericBean.GENERAL_MESSAGE_ID, GenericUtils.getLocalizedMessage("UNEXPECTED_EXCEPTION_MESSAGE"), e.getLocalizedMessage());
        }
      }
      catch (SendEmailException e) {
        addErrorMessage(JSFGenericBean.GENERAL_MESSAGE_ID, GenericUtils.getLocalizedMessage("SEND_RESET_PASSWORD_EMAIL_MESSAGE"), null);
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
    return !GenericUtils.isEmptyString(email);
  }

}
