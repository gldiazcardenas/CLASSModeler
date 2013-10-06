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

import classmodeler.domain.user.Diagrammer;
import classmodeler.domain.user.User;
import classmodeler.service.SessionService;
import classmodeler.service.exception.InvalidDiagrammerAccountException;
import classmodeler.service.util.GenericUtils;
import classmodeler.web.util.JSFGenericBean;
import classmodeler.web.util.JSFOutcomeUtil;

/**
 * JSF bean controller for the user session. 
 *
 * @author Gabriel Leonardo Diaz, 14.04.2013.
 */
@ManagedBean(name="sessionController")
@SessionScoped
public class SessionControllerBean extends JSFGenericBean {
  
  private static final long serialVersionUID = 1L;
  
  private User loggedUser;
  
  @EJB
  private SessionService sessionService;

  public SessionControllerBean() {
    super();
  }
  
  /**
   * Gets the current user in session.
   * 
   * @return An instance of IUser.
   */
  public User getLoggedUser() {
    return loggedUser;
  }
  
  /**
   * Gets the logged registered user.
   * 
   * @return An instance of User.
   */
  public Diagrammer getLoggedRegisteredUser () {
    if (loggedUser instanceof Diagrammer) {
      return (Diagrammer) loggedUser;
    }
    return null;
  }
  
  /**
   * Gets the name of the user logged. If there is not user logged this return
   * <code>null</code>.
   * 
   * @return The name of the user logged.
   */
  public String getUserName () {
    if (loggedUser == null) {
      return null;
    }
    
    if (!loggedUser.isRegisteredUser()) {
      return GenericUtils.getLocalizedMessage(loggedUser.getName());
    }
    
    return loggedUser.getName();
  }
  
  public boolean isRegisteredUser () {
    return loggedUser != null && loggedUser.isRegisteredUser();
  }
  
  /**
   * Logs in the system the user represented by the given credentials.
   * 
   * @param email
   *          The user email.
   * @param password
   *          The password of the system.
   * @throws InactivatedUserAccountException 
   */
  public void login (String email, String password) throws InvalidDiagrammerAccountException {
    loggedUser = sessionService.logIn(email, password);
  }
  
  /**
   * Ends the current user session, this clears the local information and starts
   * saving the GUI preferences.
   * 
   * @return The OUTCOME to the index page.
   */
  public String logout () {
    // Remove the user specific information.
    sessionService.logOut(loggedUser);
    
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    if (session != null) {
      session.invalidate();
    }
    return JSFOutcomeUtil.INDEX + JSFOutcomeUtil.REDIRECT_SUFIX;
  }
  
  /**
   * Redirects the user to the Dashboard page.
   * 
   * @return The OUTCOME to the Dashboard page
   */
  public String goToDashboard () {
    return JSFOutcomeUtil.DASHBOARD + JSFOutcomeUtil.REDIRECT_SUFIX;
  }
  
}
