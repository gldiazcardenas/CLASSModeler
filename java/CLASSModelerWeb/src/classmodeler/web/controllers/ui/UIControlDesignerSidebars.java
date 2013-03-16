/****************************************************

  Universidad Francisco de Paula Santander UFPS
  Cúcuta, Colombia
  (c) 2013 by UFPS. All rights reserved.

 ****************************************************/

package classmodeler.web.controllers.ui;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

/**
 * Graphical user interface component controller that handles the order of the
 * explorers as dashboard component.
 * 
 * @author Gabriel Leonardo Diaz, 19.01.2013.
 */
@Named("sideBarPanels")
@SessionScoped
public class UIControlDesignerSidebars implements Serializable {

  private static final long serialVersionUID = 1L;
  
  private DefaultDashboardModel leftDashboard;
  private DefaultDashboardModel rightDashboard;
  
  public UIControlDesignerSidebars() {
    leftDashboard = new DefaultDashboardModel();
    
    DefaultDashboardColumn column = new DefaultDashboardColumn();
    column.addWidget("components");
    column.addWidget("zoom");
    column.addWidget("output");
    
    leftDashboard.addColumn(column);
    
    rightDashboard = new DefaultDashboardModel();
    
    column = new DefaultDashboardColumn();
    column.addWidget("project");
    column.addWidget("properties");
    column.addWidget("ratings");
    column.addWidget("sharing");
    
    rightDashboard.addColumn(column);
  }
  
  public DefaultDashboardModel getLeftDashboard() {
    return leftDashboard;
  }
  
  public DefaultDashboardModel getRightDashboard() {
    return rightDashboard;
  }

}
