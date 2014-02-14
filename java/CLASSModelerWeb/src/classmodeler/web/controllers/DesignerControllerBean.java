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

import classmodeler.domain.diagram.Diagram;
import classmodeler.domain.diagram.EDiagramPrivilege;
import classmodeler.domain.user.User;
import classmodeler.service.DiagramService;
import classmodeler.service.util.GenericUtils;
import classmodeler.web.util.JSFGenericBean;
import classmodeler.web.util.JSFOutcomeUtil;

/**
 * JSF bean that handles the interactions of the user with the CLASS
 * designer.
 * 
 * @author Gabriel Leonardo Diaz, 14.04.2013.
 */
@ManagedBean(name="designerController")
@SessionScoped
public class DesignerControllerBean extends JSFGenericBean {

  private static final long serialVersionUID = 1L;
  
  @EJB
  private DiagramService diagramService;
  
  private User user;
  private Diagram diagram;
  private EDiagramPrivilege privilege;
  
  public DesignerControllerBean() {
    super();
  }
  
  public Diagram getDiagram () {
    return diagram;
  }
  
  public User getUser() {
    return user;
  }
  
  public boolean isReadOnly() {
    return privilege == EDiagramPrivilege.READ;
  }
  
  /**
   * Begins the edition of the diagram by user.
   * 
   * @param diagram
   *          The diagram to be designed.
   * @param user
   *          The user how is editing the diagram.
   * @return The outcome to the designer page, null when the user is not
   *         privileged to edit the diagram.
   * @author Gabriel Leonardo Diaz, 13.02.2014
   */
  public String draw (Diagram diagram, User user) {
    this.privilege = diagramService.checkDiagramPrivilege(diagram, user);
    
    // Unprivileged user
    if (privilege == null) {
      addErrorMessage(GENERAL_MESSAGE_ID, GenericUtils.getLocalizedMessage("UNPRIVILEGED_USER_MESSAGE", user.getName(), diagram.getName()), null);
      return null;
    }
    
    this.user = user;
    this.diagram = diagram;
    
    return JSFOutcomeUtil.DESIGNER + JSFOutcomeUtil.REDIRECT_SUFIX;
  }
  
}
