/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.controllers;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import classmodeler.domain.diagram.Diagram;
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
  
  // Data
  private Diagram diagram;
  
  // Components
  private TreeNode tree;
  
  public DesignerControllerBean() {
    super();
  }
  
  public Diagram getDiagram () {
    return diagram;
  }
  
  public TreeNode getTree() {
    return tree;
  }
  
  /**
   * Initializes this controller to edit the given diagram, this loads the XMI
   * of the diagram and configures the components.
   * 
   * @param aDiagram
   *          The diagram to edit.
   * @author Gabriel Leonardo Diaz, 23.06.2013.
   */
  public String designDiagram (Diagram aDiagram) {
    diagram = aDiagram;
    if (diagram == null) {
      diagram = new Diagram();
      diagram.setName(GenericUtils.getLocalizedMessage("NEW_DIAGRAM_NAME"));
    }
    
    // Re-creates the tree
    tree = new DefaultTreeNode();
    DefaultTreeNode root = new DefaultTreeNode(diagram.getName(), tree);
    root.setParent(root);
    
    return JSFOutcomeUtil.DESIGNER + JSFOutcomeUtil.REDIRECT_SUFIX;
  }
  
}
