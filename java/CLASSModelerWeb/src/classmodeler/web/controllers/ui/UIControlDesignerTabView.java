/****************************************************

  Universidad Francisco de Paula Santander UFPS
  C�cuta, Colombia
  (c) 2013 by UFPS. All rights reserved.

 ****************************************************/
package classmodeler.web.controllers.ui;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;


/**
 * Graphical user interface component controller for the tab view that handles
 * the diagrams currently edited by the user.
 * 
 * @author Gabriel Leonardo Diaz, 19.01.2013.
 */
@Named("tabViewDiagrams")
@SessionScoped
public class UIControlDesignerTabView implements Serializable {

  private static final long serialVersionUID = 1L;

  public UIControlDesignerTabView() {
    super();
  }

}
