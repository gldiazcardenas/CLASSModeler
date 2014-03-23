/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.controllers;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import classmodeler.domain.share.SharedItem;
import classmodeler.service.DiagramService;
import classmodeler.service.util.GenericUtils;
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
  
  @ManagedProperty("#{dashBoardController}")
  private DashboardControllerBean dashBoardController;
  
  @EJB
  private DiagramService diagramService;
  
  private SharedItem sharedItem;
  private EShareItemControllerMode mode;
  private String title;
  
  public SharedItemControllerBean() {
    super();
  }
  
  public void setDashBoardController(DashboardControllerBean dashBoardController) {
    this.dashBoardController = dashBoardController;
  }
  
  public String getTitle() {
    return title;
  }
  
  /**
   * Prepares the controller for deleting the privilege.
   * 
   * @author Gabriel Leonardo Diaz, 23.03.2014.
   */
  public void prepareDeletePrivilege () {
    sharedItem = dashBoardController.getSharedItem();
    if (sharedItem != null) {
      mode  = EShareItemControllerMode.DELETE;
      title = GenericUtils.getLocalizedMessage("SHARED_ITEM_DELETE_FORM_TITLE", sharedItem.getDiagram().getName(), sharedItem.getDiagrammer().getFirstName());
    }
  }
  
  /**
   * Prepares the controller for modifying the privilege.
   * 
   * @author Gabriel Leonardo Diaz, 23.03.2014.
   */
  public void prepareChangePrivilege () {
    sharedItem = dashBoardController.getSharedItem();
    if (sharedItem != null) {
      mode = EShareItemControllerMode.EDIT;
      title = GenericUtils.getLocalizedMessage("SHARED_ITEM_EDIT_FORM_TITLE", sharedItem.getDiagram().getName(), sharedItem.getDiagrammer().getFirstName());
    }
  }

  @Override
  public boolean isAllValid() {
    return sharedItem != null;
  }

  @Override
  public void processAJAX() {
    if (!isAllValid()) {
      return;
    }
    
    try {
      switch (mode) {
      case EDIT:
        sharedItem.setWriteable(!sharedItem.isWriteable());
        diagramService.updatePrivilege(sharedItem);
        break;
        
      case DELETE:
        diagramService.deleteSharedItem(sharedItem.getKey());
        dashBoardController.deleteSharedItem(sharedItem);
        break;
        
      default:
        break;
      }
    }
    catch (Exception e) {
      addErrorMessage(JSFGenericBean.GENERAL_MESSAGE_ID, GenericUtils.getLocalizedMessage("UNEXPECTED_EXCEPTION_MESSAGE"), e.getMessage());
    }
  }

  @Override
  public String process() {
    return null;
  }
  
  /**
   * The operating mode of this controller.
   * 
   * @author Gabriel Leonardo Diaz, 23.03.2014.
   */
  private static enum EShareItemControllerMode {
    EDIT,
    DELETE
  }
  
}
