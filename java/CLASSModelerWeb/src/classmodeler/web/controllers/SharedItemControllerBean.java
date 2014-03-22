/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.controllers;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import classmodeler.web.util.JSFFormControllerBean;
import classmodeler.web.util.JSFGenericBean;

/**
 * JSF bean that handles operation over shared items.
 * 
 * @author Gabriel Leonardo Diaz, 22.03.2014.
 */
@ViewScoped
@ManagedBean(name="sharedItemController")
public class SharedItemControllerBean extends JSFGenericBean implements JSFFormControllerBean {
  
  private static final long serialVersionUID = 1L;
  
  public SharedItemControllerBean() {
    super();
  }
  
  /**
   * 
   */
  public void prepareDeletePrivilege () {
    
  }
  
  /**
   * 
   */
  public void prepareChangePrivilege () {
    
  }

  @Override
  public boolean isAllValid() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void processAJAX() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public String process() {
    return null;
  }
  
}
