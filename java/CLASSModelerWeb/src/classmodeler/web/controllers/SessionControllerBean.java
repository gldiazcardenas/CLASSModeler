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
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import classmodeler.domain.user.IUser;
import classmodeler.service.UserService;
import classmodeler.service.exception.InactivatedUserAccountException;
import classmodeler.web.resources.JSFMessageBundle;
import classmodeler.web.util.JSFGenericBean;

/**
 * JSF bean controller for the user session. 
 *
 * @author Gabriel Leonardo Diaz, 14.04.2013.
 */
@ManagedBean(name="sessionController")
@SessionScoped
public class SessionControllerBean extends JSFGenericBean {
  
  private static final long serialVersionUID = 1L;
  
  // Data
  private IUser loggedUser;
  
  // EJBs
  @EJB
  private UserService userServiceBean;

  public SessionControllerBean() {
    super();
  }
  
  public IUser getLoggedUser() {
    return loggedUser;
  }
  
  public String getUserName () {
    if (loggedUser == null) {
      return null;
    }
    
    if (!loggedUser.isRegisteredUser()) {
      return JSFMessageBundle.getLocalizedMessage(loggedUser.getName());
    }
    
    return loggedUser.getName();
  }
  
  public boolean isRegisteredUser () {
    return loggedUser != null && loggedUser.isRegisteredUser();
  }
  
  public String getUserAvatar () {
    if (loggedUser == null) {
      return null;
    }
    
    if (!loggedUser.isRegisteredUser()) {
      return "/resources/uploads/avatar.png";
    }
    
    return loggedUser.getAvatar();
  }
  
  /**
   * Logs in the system the user represented by the given credentials.
   * 
   * @param nickname
   *          The user nickname.
   * @param password
   *          The password of the system.
   * @throws InactivatedUserAccountException 
   */
  public void login (String nickname, String password) throws InactivatedUserAccountException {
    loggedUser = userServiceBean.logIn(nickname, password);
  }
  
  /**
   * Ends the current user session, this clears the local information and starts
   * saving the GUI preferences.
   * 
   * @return The OUTCOME to the index page.
   */
  public String logout () {
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    if (session != null) {
      session.invalidate();
    }
    return "/index.xhtml?faces-redirect=true";
  }
  
  /**
   * Redirects the user to the Dashboard page.
   * 
   * @return The OUTCOME to the Dashboard page
   */
  public String goToDashboard () {
    return "/pages/dashboard/dashboard.xhtml?faces-redirect=true";
  }
  
}
