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
import classmodeler.web.resources.JSFResourceBundle;
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
  
  public void setDiagram(Diagram diagram) {
    this.diagram = diagram;
  }
  
  public TreeNode getTree() {
    return tree;
  }
  
  /**
   * Initializes this controller to edit the given diagram, this loads the XMI
   * of the diagram and configures the components.
   * 
   * @param diagramToEdit
   *          The diagram to edit.
   * @author Gabriel Leonardo Diaz, 23.06.2013.
   */
  public String initEditDiagram () {
    if (diagram == null) {
      diagram = new Diagram();
      diagram.setName(JSFResourceBundle.getLocalizedMessage("NEW_DIAGRAM_NAME"));
    }
    
    // Re-creates the tree
    tree = new DefaultTreeNode();
    DefaultTreeNode root = new DefaultTreeNode(diagram.getName(), tree);
    root.setParent(root);
    
    return JSFOutcomeUtil.DESIGNER + JSFOutcomeUtil.REDIRECT_SUFIX;
  }
  
  public String getExecuteOnLoad () {
    StringBuilder sb = new StringBuilder();
    sb.append("main(\"");
    sb.append("<?xml version='1.0' encoding='UTF-8'?>");
    sb.append("<mxGraphModel>");
    sb.append("<root>");
    sb.append("<mxCell id='0'/>");
    sb.append("<mxCell id='1' parent='0'/>");
    sb.append("<mxCell id='2' vertex='1' parent='1' value='Interval 1'>");
    sb.append("<mxGeometry x='380' y='0' width='140' height='30' as='geometry'/>");
    sb.append("</mxCell>");
    sb.append("<mxCell id='3' vertex='1' parent='1' value='Interval 2'>");
    sb.append("<mxGeometry x='200' y='80' width='380' height='30' as='geometry'/>");
    sb.append("</mxCell>");
    sb.append("<mxCell id='4' vertex='1' parent='1' value='Interval 3'>");
    sb.append("<mxGeometry x='40' y='140' width='260' height='30' as='geometry'/>");
    sb.append("</mxCell>");
    sb.append("<mxCell id='5' vertex='1' parent='1' value='Interval 4'>");
    sb.append("<mxGeometry x='120' y='200' width='240' height='30' as='geometry'/>");
    sb.append("</mxCell>");
    sb.append("<mxCell id='6' vertex='1' parent='1' value='Interval 5'>");
    sb.append("<mxGeometry x='420' y='260' width='80' height='30' as='geometry'/>");
    sb.append("</mxCell>");
    sb.append("<mxCell id='7' edge='1' source='2' target='3' parent='1' value='Transfer1'>");
    sb.append("<mxGeometry as='geometry'>");
    sb.append("<Array as='points'><Object x='420' y='60'/></Array>");
    sb.append("</mxGeometry>");
    sb.append("</mxCell>");
    sb.append("<mxCell id='8' edge='1' source='2' target='6' parent='1' value='Transfer2'>");
    sb.append("<mxGeometry as='geometry' relative='1' y='-30'>");
    sb.append("<Array as='points'><Object x='600' y='60'/></Array>");
    sb.append("</mxGeometry>");
    sb.append("</mxCell>");
    sb.append("<mxCell id='9' edge='1' source='3' target='4' parent='1' value='Transfer3'>");
    sb.append("<mxGeometry as='geometry'>");
    sb.append("<Array as='points'><Object x='260' y='120'/></Array>");
    sb.append("</mxGeometry>");
    sb.append("</mxCell>");
    sb.append("<mxCell id='0' edge='1' source='4' target='5' parent='1' value='Transfer4'>");
    sb.append("<mxGeometry as='geometry'>");
    sb.append("<Array as='points'><Object x='200' y='180'/></Array>");
    sb.append("</mxGeometry>");
    sb.append("</mxCell>");
    sb.append("<mxCell id='11' edge='1' source='4' target='6' parent='1' value='Transfer5'>");
    sb.append("<mxGeometry as='geometry' relative='1' y='-10'>");
    sb.append("<Array as='points'><Object x='460' y='155'/></Array>");
    sb.append("</mxGeometry>");
    sb.append("</mxCell>");
    sb.append("</root>");
    sb.append("</mxGraphModel>");
    sb.append("\")");
    return sb.toString();
  }
  
}
