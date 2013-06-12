/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import classmodeler.domain.project.Project;
import classmodeler.web.data.PropertyValue;
import classmodeler.web.util.JSFGenericBean;

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
  
  private Project project;
  private TreeNode projectTree;
  private List<PropertyValue> propertyValues;
  
  public DesignerControllerBean() {
    super();
    
    // Creating the tree
    projectTree = new DefaultTreeNode("root", null);
    
    TreeNode rootNode = new DefaultTreeNode("Project Root", projectTree);
    rootNode.setExpanded(true);
    
    TreeNode childNode = new DefaultTreeNode("Folder 1", rootNode);
    childNode.setParent(rootNode);
    
    childNode = new DefaultTreeNode("Folder 2", rootNode);
    childNode.setParent(rootNode);
    
    // Creating the table
    propertyValues = new ArrayList<PropertyValue>();
    propertyValues.add(new PropertyValue(1, "Name", "Name"));
    propertyValues.add(new PropertyValue(2, "Scope", "Public"));
  }
  
  public Project getProject() {
    return project;
  }
  
  public void setProject(Project project) {
    this.project = project;
  }
  
  public TreeNode getProjectTree() {
    return projectTree;
  }
  
  public List<PropertyValue> getPropertyValues() {
    return propertyValues;
  }
  
  public String getXMLCurrentDiagram () {
    StringBuilder sb = new StringBuilder();
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
    sb.append("<mxCell id='8' edge='1' source='2' target='6' parent='1' value=''>");
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
    
    return sb.toString();
  }
  
}
