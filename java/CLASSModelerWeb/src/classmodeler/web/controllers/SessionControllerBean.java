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
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import classmodeler.domain.user.Diagrammer;
import classmodeler.domain.user.User;
import classmodeler.service.UserService;
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
public class SessionControllerBean extends JSFGenericBean implements HttpSessionBindingListener {
  
  private static final long serialVersionUID = 1L;
  
  private User user;
  
  @EJB
  private UserService userService;
  
  @ManagedProperty("#{designerController}")
  private DesignerControllerBean designerController;

  public SessionControllerBean() {
    super();
  }
  
  /**
   * Gets the current user in session.
   * 
   * @return An instance of IUser.
   */
  public User getUser() {
    return user;
  }
  
  /**
   * Gets the logged diagrammer.
   * 
   * @return An instance of Diagrammer.
   */
  public Diagrammer getDiagrammer () {
    if (user instanceof Diagrammer) {
      return (Diagrammer) user;
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
    if (user == null) {
      return null;
    }
    
    if (!user.isRegisteredUser()) {
      return GenericUtils.getLocalizedMessage(user.getName());
    }
    
    return user.getName();
  }
  
  public boolean isRegisteredUser () {
    return user != null && user.isRegisteredUser();
  }
  
  public void setDesignerController(DesignerControllerBean designerController) {
    this.designerController = designerController;
  }
  
  /**
   * Logs in the system the user represented by the given credentials.
   * 
   * @param email
   *          The user email.
   * @param password
   *          The password of the system.
   * @return The user logged in.
   * @throws InactivatedUserAccountException
   */
  public User login (String email, String password) throws InvalidDiagrammerAccountException {
    this.user = userService.logIn(email, password);
    return this.user;
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
    return JSFOutcomeUtil.INDEX + JSFOutcomeUtil.REDIRECT_SUFIX;
  }
  
  @Override
  public void valueBound(HttpSessionBindingEvent event) {
    // Do nothing
  }
  
  @Override
  public void valueUnbound(HttpSessionBindingEvent event) {
    if (designerController != null) {
      // Remove the session created for the diagram
      designerController.destroy();
    }
  }
  
}
