/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.controllers;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import classmodeler.domain.user.Guest;
import classmodeler.service.exception.InactivatedUserAccountException;
import classmodeler.service.util.GenericUtils;
import classmodeler.web.util.JSFFormControllerBean;
import classmodeler.web.util.JSFGenericBean;

@ManagedBean(name="logInController")
@ViewScoped
public class LogInControllerBean extends JSFGenericBean implements JSFFormControllerBean {

  private static final long serialVersionUID = 1L;
  
  // Data
  private String nickname;
  private String password;
  private ELoginMode mode = ELoginMode.REGISTERED_USER;
  
  // Injected Beans
  @ManagedProperty("#{sessionController}")
  private SessionControllerBean sessionController;

  public LogInControllerBean() {
    super();
  }
  
  public String getNickname() {
    return nickname;
  }
  
  public void setNickname(String nickname) {
    this.nickname = nickname;
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
  
  /**
   * Logs into the system as a Guest user.
   * 
   * @return The OUTCOME redirecting to the designer main page.
   * @throws InactivatedUserAccountException
   *           As a Guest user this exception never happens.
   */
  public String processGuest () {
    nickname = Guest.GUEST_EMAIL;
    password = Guest.GUEST_PASSWORD;
    mode = ELoginMode.GUEST_USER;
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
        sessionController.login(nickname, password);
        
        if (mode == ELoginMode.REGISTERED_USER) {
          // Redirects to the DashBoard
          outcome = "pages/dashboard/dashboard.xhtml?faces-redirect=true";
        }
        else {
          // Redirects to the Designer Page
          outcome = "pages/designer/designer.xhtml?faces-redirect=true";
        }
      }
      catch (InactivatedUserAccountException e) {
        addErrorMessage("", "", null);
      }
    }
    
    return outcome;
  }

  @Override
  public boolean isAllValid() {
    if (GenericUtils.isEmptyString(nickname)) {
      return false;
    }
    
    if (GenericUtils.isEmptyString(password)) {
      return false;
    }
    
    return true;
  }
  
  @Override
  public void processAJAX() {
    // Not used.
  }
  
  /**
   * The type of user that is trying to login in.
   * @author Gabriel Leonardo Diaz, 23.05.2013.
   */
  private static enum ELoginMode {
    GUEST_USER,
    REGISTERED_USER
  }
  
}
