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
  public String logInGuest () throws InactivatedUserAccountException {
    sessionController.login(Guest.GUEST_EMAIL, Guest.GUEST_PASSWORD);
    
    // Redirect to the designer page
    return "pages/designer/designer.xhtml?faces-redirect=true";
  }
  
  /**
   * Logs into the system as a registered user.
   * 
   * @return The OUTCOME redirecting to the dashboard page.
   * @throws InactivatedUserAccountException
   *           If the user has not activate its account.
   */
  public String logIn () throws InactivatedUserAccountException {
    if (!isAllValid()) {
      // stay at the same page
      return null;
    }
    
    sessionController.login(nickname, password);
    
    // Redirects to the dashboard
    return "pages/dashboard/dashboard.xhtml?faces-redirect=true";
  }

  @Override
  public String actionPerformed() {
    // Not used
    return null;
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
  
}
