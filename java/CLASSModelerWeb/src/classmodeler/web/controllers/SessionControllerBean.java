/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.controllers;

import java.io.IOException;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import classmodeler.domain.user.Guest;
import classmodeler.domain.user.IUser;
import classmodeler.service.UserService;
import classmodeler.service.exception.InactivatedUserAccountException;
import classmodeler.web.util.JSFContextUtil;
import classmodeler.web.util.JSFGenericBean;

/**
 * JSF bean controller for the user session. 
 *
 * @author Gabriel Leonardo Diaz, 14.04.2013.
 */
@Named("sessionController")
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
  
  public String getUserLoggedName () {
    if (loggedUser == null) {
      FacesContext.getCurrentInstance().responseComplete();
      try {
        FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
      }
      catch (IOException e) {
        addErrorMessage("Unexpected exception: " + e.getMessage(), null);
      }
    }
    
    if (loggedUser instanceof Guest) {
      return JSFContextUtil.getLocalizedMessage(loggedUser.getName());
    }
    
    return loggedUser.getName();
  }
  
  /**
   * Logs in the system the user represented by the given credentials.
   * 
   * @param nickname
   *          The user nickname.
   * @param password
   *          The password of the system.
   */
  public void login (String nickname, String password) {
    if (Guest.GUEST_NICK_NAME.equals(nickname) && Guest.GUEST_PASSWORD.equals(password)) {
      loggedUser = new Guest();
    }
    else {
      try {
        loggedUser = userServiceBean.logIn(nickname, password);
      }
      catch (InactivatedUserAccountException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
  
  /**
   * Ends the current user session, this clears the local information and starts
   * saving the GUI preferences.
   */
  public void logout () {
    // TODO implement this method.
  }
  
}
