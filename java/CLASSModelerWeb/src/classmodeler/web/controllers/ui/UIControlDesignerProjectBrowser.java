/****************************************************

  Universidad Francisco de Paula Santander UFPS
  Cúcuta, Colombia
  (c) 2013 by UFPS. All rights reserved.

 ****************************************************/

package classmodeler.web.controllers.ui;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 * Graphical user interface component controller that handles the project
 * browser tree.
 * 
 * @author Gabriel Leonardo Diaz, 19.01.2013.
 */
@Named("projectBrowser")
@SessionScoped
public class UIControlDesignerProjectBrowser implements Serializable {

  private static final long serialVersionUID = 1L;
  
  public UIControlDesignerProjectBrowser() {
    super();
  }

}
