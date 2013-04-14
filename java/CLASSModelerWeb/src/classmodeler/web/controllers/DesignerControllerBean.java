/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.controllers;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import classmodeler.domain.user.IUser;
import classmodeler.web.util.JSFGenericBean;

/**
 * JSF bean that handles the interactions of the user with the CLASS
 * designer.
 * 
 * @author Gabriel Leonardo Diaz, 14.04.2013.
 */
@Named("designerController")
@SessionScoped
public class DesignerControllerBean extends JSFGenericBean {

  private static final long serialVersionUID = 1L;
  
  private IUser loggedUser;
  
  public DesignerControllerBean() {
    super();
  }
  
  public IUser getLoggedUser() {
    return loggedUser;
  }
  
}
