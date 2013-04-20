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
  
  // Components
  private TreeNode projectTree;
  private List<PropertyValue> propertyValues;
  
  public DesignerControllerBean() {
    super();
    
    // Creating the tree
    projectTree = new DefaultTreeNode("root", null);
    
    TreeNode childNode = new DefaultTreeNode("Project Root", projectTree);
    childNode.setExpanded(true);
    childNode = new DefaultTreeNode("Folder 1", childNode);
    childNode = new DefaultTreeNode("Folder 2", childNode);
    
    // Creating the table
    propertyValues = new ArrayList<PropertyValue>();
    propertyValues.add(new PropertyValue(1, "Name", "Name"));
    propertyValues.add(new PropertyValue(2, "Scope", "Public"));
  }
  
  public TreeNode getProjectTree() {
    return projectTree;
  }
  
  public List<PropertyValue> getPropertyValues() {
    return propertyValues;
  }
  
}
