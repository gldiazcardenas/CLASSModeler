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

import classmodeler.domain.verification.EVerificationType;
import classmodeler.service.UserService;
import classmodeler.service.exception.ExpiredVerificationCodeException;
import classmodeler.service.exception.InvalidUserAccountException;
import classmodeler.service.util.GenericUtils;
import classmodeler.web.resources.JSFResourceBundle;
import classmodeler.web.util.JSFGenericBean;

/**
 * JSF Bean used to activate the user account.
 * 
 * @author Gabriel Leonardo Diaz, 20.05.2013.
 */
@ManagedBean(name="verifyCodeController")
@ViewScoped
public class VerificationCodeControllerBean extends JSFGenericBean {
  
  @EJB
  private UserService userService;
  
  private boolean validToReset;
  
  public VerificationCodeControllerBean() {
    super();
  }
  
  public void setValidToReset(boolean validToReset) {
    this.validToReset = validToReset;
  }
  
  public boolean isValidToReset() {
    return validToReset;
  }

  private static final long serialVersionUID = 1L;
  
  /**
   * Activates the user account, this takes the parameters of the request (code,
   * email and type).
   * 
   * @return The result message of this operation.
   */
  public String activateUserAccount () {
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    
    String code = request.getParameter("code");
    String email = request.getParameter("email");
    String action = request.getParameter("action");
    
    String resultMessage = JSFResourceBundle.getLocalizedMessage("ACCOUNT_ACTIVATION_MISSING_INFO_MESSAGE");
    
    if (GenericUtils.isValidEmail(email) && EVerificationType.ACTIVATE_ACCOUNT.toString().equals(action)) {
      try {
        userService.activateUserAccount(email, code);
        resultMessage = JSFResourceBundle.getLocalizedMessage("ACCOUNT_ACTIVATION_SUCCESSFUL_MESSAGE");
      }
      catch (ExpiredVerificationCodeException e) {
        resultMessage = JSFResourceBundle.getLocalizedMessage("ACCOUNT_ACTIVATION_EXPIRED_CODE_MESSAGE");
      }
      catch (InvalidUserAccountException e) {
        resultMessage = JSFResourceBundle.getLocalizedMessage("ACCOUNT_ACTIVATION_INVALID_STATE_MESSAGE");
      }
    }
    
    return resultMessage;
  }
  
  /**
   * Restores the pas
   * @return
   */
  public String resetPassword () {
    // TODO
    return null;
  }

}
