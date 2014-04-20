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
import classmodeler.service.exception.ExpiredSecurityCodeException;
import classmodeler.service.exception.InvalidDiagrammerAccountException;
import classmodeler.service.exception.InvalidDiagrammerAccountException.EInvalidAccountErrorType;
import classmodeler.service.exception.InvalidSecurityCodeException;
import classmodeler.service.exception.SendEmailException;
import classmodeler.service.util.GenericUtils;
import classmodeler.web.util.JSFFormControllerBean;
import classmodeler.web.util.JSFGenericBean;
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
  private String errorMessage;
  
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
  
  public String getErrorMessage() {
    return errorMessage;
  }
  
  /**
   * Loads the parameters from the request and verifies if the values are valid.
   * 
   * @author Gabriel Leonardo Diaz, 24.05.2013.
   */
  public void configure () {
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    
    String codeParameter       = request.getParameter("code");
    String emailParameter      = request.getParameter("email");
    
    valid                      = false;
    
    try {
      if (userService.isValidToResetPassword(emailParameter, codeParameter)) {
        valid = true;
        email = emailParameter;
      }
      else {
        errorMessage = GenericUtils.getLocalizedMessage("INVALID_VERIFICATION_CODE_USED_MESSAGE");
      }
    }
    catch (InvalidDiagrammerAccountException e) {
      if (e.getType() == EInvalidAccountErrorType.NON_EXISTING_ACCOUNT) {
        errorMessage = GenericUtils.getLocalizedMessage("INVALID_ACCOUNT_NON_EXISTING_MESSAGE");
      }
      else {
        errorMessage = GenericUtils.getLocalizedMessage("UNEXPECTED_EXCEPTION_MESSAGE");
      }
    }
    catch (InvalidSecurityCodeException e) {
      errorMessage = GenericUtils.getLocalizedMessage("INVALID_VERIFICATION_CODE_MESSAGE");
    }
    catch (ExpiredSecurityCodeException e) {
      errorMessage = GenericUtils.getLocalizedMessage("INVALID_VERIFICATION_CODE_EXPIRED_MESSAGE");
    }
    catch (SendEmailException e) {
      errorMessage = GenericUtils.getLocalizedMessage("SEND_RESET_PASSWORD_EMAIL_MESSAGE");
    }
  }

  @Override
  public String process() {
    String outcome = null;
    
    if (isAllValid()) {
      try {
        userService.resetPassword(email, password);
        outcome = JSFOutcomeUtil.INDEX + JSFOutcomeUtil.REDIRECT_SUFIX;
        addInformationMessage(JSFGenericBean.GENERAL_MESSAGE_ID, GenericUtils.getLocalizedMessage("RESET_PASSWORD_CONFIRMATION_MESSAGE"), null);
      }
      catch (InvalidDiagrammerAccountException e) {
        if (e.getType() == EInvalidAccountErrorType.NON_EXISTING_ACCOUNT) {
          addErrorMessage(JSFGenericBean.GENERAL_MESSAGE_ID, GenericUtils.getLocalizedMessage("INVALID_ACCOUNT_NON_EXISTING_MESSAGE"), null);
        }
        else {
          addErrorMessage(JSFGenericBean.GENERAL_MESSAGE_ID, GenericUtils.getLocalizedMessage("UNEXPECTED_EXCEPTION_MESSAGE"), e.getLocalizedMessage());
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
    return GenericUtils.isValidPassword(password) && GenericUtils.equals(password, confirmation);
  }

}
