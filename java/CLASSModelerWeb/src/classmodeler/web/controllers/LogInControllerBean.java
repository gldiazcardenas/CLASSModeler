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
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import classmodeler.domain.user.Guest;
import classmodeler.domain.user.User;
import classmodeler.service.DiagramService;
import classmodeler.service.exception.InvalidDiagrammerAccountException;
import classmodeler.service.exception.InvalidDiagrammerAccountException.EInvalidAccountErrorType;
import classmodeler.service.util.GenericUtils;
import classmodeler.web.util.JSFFormControllerBean;
import classmodeler.web.util.JSFGenericBean;
import classmodeler.web.util.JSFOutcomeUtil;

@ManagedBean(name="logInController")
@ViewScoped
public class LogInControllerBean extends JSFGenericBean implements JSFFormControllerBean {

  private static final long serialVersionUID = 1L;
  
  private String email;
  private String password;
  
  @ManagedProperty("#{sessionController}")
  private SessionControllerBean sessionController;
  
  @ManagedProperty("#{designerController}")
  private DesignerControllerBean designerController;
  
  @EJB
  private DiagramService diagramService;

  public LogInControllerBean() {
    super();
  }
  
  public String getEmail() {
    return email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
  public String getPassword() {
    return password;
  }
  
  public void setPassword(String password) {
    this.password = password;
  }
  
  public void setSessionController(SessionControllerBean sessionController) {
    this.sessionController = sessionController;
  }
  
  public void setDesignerController(DesignerControllerBean designerController) {
    this.designerController = designerController;
  }
  
  /**
   * Logs into the system as a Guest user.
   * 
   * @return The OUTCOME redirecting to the designer main page.
   * @throws InactivatedUserAccountException
   *           As a Guest user this exception never happens.
   */
  public String processGuest () {
    email    = Guest.GUEST_EMAIL;
    password = Guest.GUEST_PASSWORD;
    return process();
  }

  /**
   * Logs into the system as a registered user.
   * 
   * @return The OUTCOME redirecting to the dashboard page.
   */
  @Override
  public String process() {
    String outcome = null;
    
    if (isAllValid()) {
      try {
        User user = sessionController.login(email, password);
        
        if (user.isRegisteredUser()) {
          // Redirects to the DashBoard
          outcome = JSFOutcomeUtil.DASHBOARD + JSFOutcomeUtil.REDIRECT_SUFIX;
        }
        else {
          // Redirects to the Designer Page
          outcome = designerController.openDiagram(DiagramControllerBean.generateEmpty(), user);
        }
      }
      catch (InvalidDiagrammerAccountException e) {
        if (e.getType() == EInvalidAccountErrorType.NON_EXISTING_ACCOUNT) {
          addWarningMessage(JSFGenericBean.GENERAL_MESSAGE_ID, GenericUtils.getLocalizedMessage("INVALID_ACCOUNT_NON_EXISTING_MESSAGE"), null);
        }
        else if (e.getType() == EInvalidAccountErrorType.NON_ACTIVATED_ACCOUNT) {
          addErrorMessage(JSFGenericBean.GENERAL_MESSAGE_ID, GenericUtils.getLocalizedMessage("INVALID_ACCOUNT_NON_ACTIVATED_MESSAGE"), null);
        }
        else {
          addErrorMessage(JSFGenericBean.GENERAL_MESSAGE_ID, GenericUtils.getLocalizedMessage("UNEXPECTED_EXCEPTION_MESSAGE"), e.getLocalizedMessage());
        }
      }
    }
    
    return outcome;
  }

  @Override
  public boolean isAllValid() {
    if (GenericUtils.isEmptyString(email) ||
        GenericUtils.isEmptyString(password)) {
      
      return false;
    }
    
    return true;
  }
  
  @Override
  public void processAJAX() {
    // Not used.
  }
  
}
