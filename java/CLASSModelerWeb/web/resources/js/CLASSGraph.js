/*******************************************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ******************************************************************************/

CLASSGraph = function (container, model, renderHint, stylesheet) {
  mxGraph.call(this, container, model, renderHint, stylesheet);
};
mxUtils.extend(CLASSGraph, mxGraph);

/**
 * Overrides convertNodeToString() in mxGraph.
 * 
 * @param node
 *          The XML node of the template.
 * @returns The String representation of the node.
 * @author Gabriel Leonardo Diaz, 03.12.2013.
 */
CLASSGraph.prototype.convertValueToString = function (cell) {
  var node = cell.value;
  
  if (this.isComment(node)) {
    return node.getAttribute("body");
  }
  
  if (this.isNamedElement(node)) {
    return node.getAttribute("name");
  }
  
  return "";
};

/**
 * Overrides cellLabelChanged() in mxGraph. Allows to apply the new value (name)
 * of the node element.
 * 
 * @param cell
 *          The cell edited.
 * @param newValue
 *          The new value.
 * @param autoSize
 * @author Gabriel Leonardo Diaz, 18.01.2014.
 */
CLASSGraph.prototype.cellLabelChanged = function (cell, newValue, autoSize) {
  if (this.isNamedElement(cell.value)) {
    this.cellEditAttribute(cell, 'name', newValue, true);
  }
};

/**
 * Edits the cell user object attribute by assigning the new value.
 * 
 * @param cell
 *          The cell whose node is going to be edited.
 * @param attrName
 *          The name of the node attribute to edit.
 * @param attrValue
 *          The new value of the attribute.
 * @param autoSize
 *          A flag indicating the size of the cell needs to be adjusted after
 *          editing.
 * @author Gabriel Leonardo Diaz, 18.01.2014.
 */
CLASSGraph.prototype.cellEditAttribute = function (cell, attrName, attrValue, autoSize) {
  this.model.beginUpdate();
  
  try {
    var node  = cell.value;
    
    // Clones the value for correct UNDO/REDO
    var clone = node.cloneNode(true);
    clone.setAttribute(attrName, attrValue);
    
    // Set the user object of the cell
    this.model.setValue(cell, clone);
    
    // Adjust the cell size
    if (autoSize) {
      this.cellSizeUpdated(cell, false);
    }
  }
  finally {
    this.model.endUpdate();
  }
};

// SECOND LABEL TODO

/**
 * Check if the given node is a UML classifier.
 * 
 * @param node
 * @returns {Boolean}
 */
CLASSGraph.prototype.isClassifier = function (node) {
  return this.isClass(node) || this.isEnumeration(node) || this.isInterface(node);
};

/**
 * Checks if the given node is a UML named element.
 * @param node
 * @returns {Boolean}
 */
CLASSGraph.prototype.isNamedElement = function (node) {
  return this.isClassifier(node) || this.isPackage(node);
};

/**
 * Checks if the given node is a class element.
 * 
 * @param node
 * @returns {Boolean}
 */
CLASSGraph.prototype.isClass = function (node) {
  if (node == null || node.nodeName == null) {
    return false;
  }
  return node.nodeName.toLowerCase() == "class";
};

/**
 * Checks if the given node is a package element.
 * 
 * @param node
 * @returns {Boolean}
 */
CLASSGraph.prototype.isPackage = function (node) {
  if (node == null || node.nodeName == null) {
    return false;
  }
  return node.nodeName.toLowerCase() == "package";
};

/**
 * Checks if the given node is an enumeration element.
 * 
 * @param node
 * @returns {Boolean}
 */
CLASSGraph.prototype.isEnumeration = function (node) {
  if (node == null || node.nodeName == null) {
    return false;
  }
  return node.nodeName.toLowerCase() == "enumeration";
};

/**
 * Checks if the given node is an interface element.
 * 
 * @param node
 * @returns {Boolean}
 */
CLASSGraph.prototype.isInterface = function (node) {
  if (node == null || node.nodeName == null) {
    return false;
  }
  return node.nodeName.toLowerCase() == "interface";
};

/**
 * Checks if the given node is a comment element.
 * 
 * @param node
 * @returns {Boolean}
 */
CLASSGraph.prototype.isComment = function (node) {
  if (node == null || node.nodeName == null) {
    return false;
  }
  return node.nodeName.toLowerCase() == "comment";
};
