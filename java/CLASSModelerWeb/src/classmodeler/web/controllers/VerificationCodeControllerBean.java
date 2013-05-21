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
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import classmodeler.domain.email.EVerificationType;
import classmodeler.service.UserService;
import classmodeler.service.exception.ExpiredVerificationCodeException;
import classmodeler.web.resources.JSFResourceBundle;
import classmodeler.web.util.JSFGenericBean;

/**
 * JSF Bean used to activate the user account.
 * 
 * @author Gabriel Leonardo Diaz, 20.05.2013.
 */
@RequestScoped
@ManagedBean(name="verifyCodeController")
public class VerificationCodeControllerBean extends JSFGenericBean {
  
  @EJB
  private UserService userService;

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
    
    if (EVerificationType.ACTIVATE_ACCOUNT.toString().equals(action)) {
      try {
        userService.activateUserAccount(email, code);
        resultMessage = JSFResourceBundle.getLocalizedMessage("ACCOUNT_ACTIVATION_SUCCESSFUL_MESSAGE");
      }
      catch (ExpiredVerificationCodeException e) {
        resultMessage = JSFResourceBundle.getLocalizedMessage("ACCOUNT_ACTIVATION_EXPIRED_CODE_MESSAGE");
      }
    }
    
    return resultMessage;
  }

}
