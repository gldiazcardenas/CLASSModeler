/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.controllers;

import javax.faces.bean.ManagedProperty;

import classmodeler.domain.user.User;
import classmodeler.web.util.JSFFormControllerBean;
import classmodeler.web.util.JSFGenericBean;

/**
 * JSF Form Bean controller that handles user interactions with the edit user
 * profile page.
 * 
 * @author Gabriel Leonardo Diaz, 03.06.2013.
 */
public class UserProfileControllerBean extends JSFGenericBean implements JSFFormControllerBean {

  private static final long serialVersionUID = 1L;
  
  @ManagedProperty("#{sessionController.loggedRegisteredUser}")
  private User loggedUser;
  
  public UserProfileControllerBean() {
    super();
  }
  
  public void setLoggedUser(User loggedUser) {
    this.loggedUser = loggedUser;
  }

  @Override
  public boolean isAllValid() {
    if (loggedUser == null) {
      return false;
    }
    
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void processAJAX() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public String process() {
    return null; // Not used
  }
  
}
