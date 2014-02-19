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
 * The template defined in CLASSEditor component for creating sections for
 * attributes and operations.
 */
CLASSGraph.prototype.sectionTemplate;

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
  
  if (this.isProperty (node)) {
    return this.getVisibilityChar(node.getAttribute("visibility")) + " " + node.getAttribute("name") + ": " + node.getAttribute("type");
  }
  
  if (this.isOperation(node)) {
    return this.getVisibilityChar(node.getAttribute("visibility")) + " " + node.getAttribute("name") + "(" + this.convertParametersToString(cell) + ")" + " : " + node.getAttribute("returnType");
  }
  
  if (this.isNamedElement(node)) {
    return node.getAttribute("name");
  }
  
  return "";
};

/**
 * Gets the string representation of the given property.
 * 
 * @param property
 *          The property XML node.
 * @author Gabriel Leonardo Diaz, 19.01.2014.
 */
CLASSGraph.prototype.getPropertyLabel = function (property) {
  var visibility = property.getAttribute("visibility");
  var name = property.getAttribute("name");
  var type = property.getAttribute("type");
  var initialValue = property.getAttribute("initialValue");
  
  var label = this.getVisibilityChar(visibility) + " " + name;
  
  if (type && type.length > 0) {
    label += ": " + type;
  }
  
  if (initialValue && initialValue.length > 0) {
    label += " " + initialValue;
  }
  
  return label;
};

/**
 * Gets the character representing the given visibility.
 * 
 * @param visibility
 *          The visibility name.
 * @author Gabriel Leonardo Diaz, 19.01.2014.
 */
CLASSGraph.prototype.getVisibilityChar = function (visibility) {
  if (visibility == "public") {
    return "+";
  }
  
  if (visibility == "private") {
    return "-";
  }
  
  if (visibility == "protected") {
    return "#";
  }
  
  if (visibility == "package") {
    return "~";
  }
  
  return "";
};

/**
 * Gets the attributes of one classifier.
 * 
 * @param classifier
 *          The cell containing a classifier element.
 * @author Gabriel Leonardo Diaz, 22.01.2014.
 * @returns The attributes cells.
 */
CLASSGraph.prototype.getAttributes = function (classifier) {
  if (classifier == null || !this.isClassifier(classifier.value) || classifier.children == null || classifier.children.length == 0) {
    return [];
  }
  
  var section;
  
  for (var i = 0; i < classifier.children.length; i++) {
    section = classifier.getChildAt(i);
    if (section.getAttribute("isAttribute") == "true") {
      return section.children;
    }
  }
  
  return [];
};

/**
 * Gets the operations of the given classifier.
 * 
 * @param classifier
 *          The cell containing a classifier element.
 * @author Gabriel Leonardo Diaz, 29.01.2014.
 * @returns
 */
CLASSGraph.prototype.getOperations = function (classifier) {
  if (classifier == null || !this.isClassifier(classifier.value) || classifier.children == null || classifier.children.length == 0) {
    return [];
  }
  
  for (var i = 0; i < classifier.children.length; i++) {
    section = classifier.getChildAt(i);
    if (section.getAttribute("isAttribute") == "false") {
      return section.children;
    }
  }
  
  return [];
};

/**
 * The operations we want to have the String representation of its parameters.
 * 
 * @param operation
 *          The operation cell.
 * @author Gabriel Leonardo Diaz, 29.01.2014.
 */
CLASSGraph.prototype.convertParametersToString = function (operation) {
  if (operation == null || !this.isOperation(operation.value)) {
    return "";
  }
  
  var parameterString = "";
  var separator = "";
  
  var param;
  var node = operation.value;
  
  for (var i = 0; i < node.childNodes.length; i++) {
    param = node.childNodes[i];
    parameterString += separator + param.getAttribute("type");
    separator = ", ";
  }
  
  return parameterString;
};

/**
 * Gets the literals of the given enumeration.
 * 
 * @param enumeration
 *          The cell containing an enumeration element.
 * @returns The literals of the enumeration.
 * @author Gabriel Leonardo Diaz, 25.01.2014.
 */
CLASSGraph.prototype.getLiterals = function (enumeration) {
  if (enumeration == null || !this.isEnumeration(enumeration.value) || enumeration.children == null || enumeration.children.length == 0) {
    return [];
  }
  
  var section;
  var child;
  var literals = [];
  
  for (var i = 0; i < enumeration.children.length; i++) {
    section = enumeration.getChildAt(i);
    if (section.getAttribute("isAttribute") == "true") {
      for (var j = 0; j < section.children.length; j++) {
        child = section.getChildAt(j);
        if (this.isEnumerationLiteral(child.value)) {
          literals.push(child);
        }
      }
      break;
    }
  }
  
  return literals;
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
    this.cellEditProperty(cell, "name", newValue, true);
  }
};

/**
 * Overrides getEditingValue() in mxGraph. Allows to get the value for inline
 * edition of the cell.
 * 
 * @param cell
 * @param evt
 * @author Gabriel Leonardo Diaz, 20.01.2014.
 */
CLASSGraph.prototype.getEditingValue = function (cell, evt) {
  if (this.isNamedElement(cell.value)) {
    return cell.value.getAttribute("name");
  }
  
  return mxGraph.prototype.getEditingValue.call(this, arguments);
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
CLASSGraph.prototype.cellEditProperty = function (cell, attrName, attrValue, autoSize) {
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

/**
 * Overrides cellSizeUpdated() in mxGraph.  Only to add 10px instead of 8px as the collapse/expanded icon size.
 * @param cell
 * @param ignoreChildren
 * @author Gabriel Leonardo Diaz, 04.02.2014.
 */
CLASSGraph.prototype.cellSizeUpdated = function(cell, ignoreChildren) {
  if (cell != null) {
    this.model.beginUpdate();
    
    try {
      var size = this.getPreferredSizeForCell(cell);
      var geo = this.model.getGeometry(cell);
      
      if (size != null && geo != null) {
        var collapsed = this.isCellCollapsed(cell);
        geo = geo.clone();
        
        if (this.isSwimlane(cell)) {
          var state = this.view.getState(cell);
          var style = (state != null) ? state.style : this.getCellStyle(cell);
          var cellStyle = this.model.getStyle(cell);
          
          if (cellStyle == null) {
            cellStyle = '';
          }
          
          if (mxUtils.getValue(style, mxConstants.STYLE_HORIZONTAL, true)) {
            cellStyle = mxUtils.setStyle(cellStyle, mxConstants.STYLE_STARTSIZE, size.height + 10);
            
            if (collapsed) {
              geo.height = size.height + 10;
            }
            
            geo.width = size.width;
          }
          else {
            cellStyle = mxUtils.setStyle(cellStyle, mxConstants.STYLE_STARTSIZE, size.width + 10);
            
            if (collapsed) {
              geo.width = size.width + 10;
            }
            
            geo.height = size.height;
          }
          
          this.model.setStyle(cell, cellStyle);
        }
        else {
          geo.width = size.width;
          geo.height = size.height;
        }
        
        if (!ignoreChildren && !collapsed) {
          var bounds = this.view.getBounds(this.model.getChildren(cell));
          
          if (bounds != null) {
            var tr = this.view.translate;
            var scale = this.view.scale;
            
            var width = (bounds.x + bounds.width) / scale - geo.x - tr.x;
            var height = (bounds.y + bounds.height) / scale - geo.y - tr.y;
            
            geo.width = Math.max(geo.width, width);
            geo.height = Math.max(geo.height, height);
          }
        }
        
        this.cellsResized([cell], [geo]);
      }
    }
    finally {
      this.model.endUpdate();
    }
  }
};

/**
 * Overrides isCellSelectable() in mxGraph. Determines if the given cell can be selected.
 * @param cell
 * @returns {Boolean}
 */
CLASSGraph.prototype.isCellSelectable = function (cell) {
  if (cell == null || cell.value == null) {
    return false;
  }
  return !this.isSection(cell.value);
};

/**
 * Adds the given attribute to the classifier element. This creates the cell and
 * appends it to the graph model.
 * 
 * @param classifierCell
 *          The cell containing the classifier.
 * @param attributeCell
 *          The cell containing the new attribute.
 * @author Gabriel Leonardo Diaz, 19.01.2014.
 */
CLASSGraph.prototype.addAttribute = function (classifierCell, attributeCell) {
  if (!this.isClassifier(classifierCell.value)) {
    return;
  }
  
  this.model.beginUpdate();
  
  try {
    var attrSection = null;
    
    if (classifierCell.children && classifierCell.children.length > 0) {
      var child = classifierCell.children[0];
      if (child.getAttribute("isAttribute") == "true") {
        attrSection = child;
      }
    }
    
    if (attrSection == null) {
      attrSection = this.model.cloneCell(this.sectionTemplate);
      attrSection.setAttribute("isAttribute", "true");
      
      this.addCell(attrSection, classifierCell, 0);
    }
    
    this.addCell(attributeCell, attrSection);
    this.cellSizeUpdated(attributeCell, false);
  }
  finally {
    this.model.endUpdate();
  }
};

/**
 * Adds the given operation to the classifier element.
 * 
 * @param classifierCell
 * @param operationCell
 * @author Gabriel Leonardo Diaz, 29.01.2014.
 */
CLASSGraph.prototype.addOperation = function (classifierCell, operationCell) {
  if (!this.isClassifier(classifierCell.value)) {
    return;
  }
  
  this.model.beginUpdate();
  
  try {
    var operSection = null;
    
    if (classifierCell.children && classifierCell.children.length > 0) {
      var child;
      if (classifierCell.children.length == 1) {
        child = classifierCell.children[0];
      }
      else {
        child = classifierCell.children[1];
      }
      
      if (child.getAttribute("isAttribute") == "false") {
        operSection = child;
      }
    }
    
    if (operSection == null) {
      operSection = this.model.cloneCell(this.sectionTemplate);
      operSection.setAttribute("isAttribute", "false");
      
      this.addCell(operSection, classifierCell, 1);
    }
    
    this.addCell(operationCell, operSection);
    this.cellSizeUpdated(operationCell, false);
  }
  finally {
    this.model.endUpdate();
  }
};

/**
 * Edits the given operation cell by replacing the objects. 
 * @param operationCell
 * @param newOperationCell
 * @author Gabriel Leonardo Diaz, 29.01.2014.
 */
CLASSGraph.prototype.editOperation = function (operationCell, newOperationCell) {
  this.model.beginUpdate();
  
  try {
    this.model.setValue(operationCell, newOperationCell.value);
    this.cellSizeUpdated(operationCell, false);
  }
  finally {
    this.model.endUpdate();
  }
};

/**
 * Replaces the user object of the given cell attribute.
 * 
 * @param attributeValue
 *          The cell to replace its user object.
 * @param attributeValue
 *          The new user object of the cell, must be a clone of the original
 *          one.
 * @author Gabriel Leonardo Diaz, 24.01.2014.
 */
CLASSGraph.prototype.editAttribute = function (attributeCell, newAttributeCell) {
  this.model.beginUpdate();
  
  try {
    this.model.setValue(attributeCell, newAttributeCell.value);
    this.cellSizeUpdated(attributeCell, false);
  }
  finally {
    this.model.endUpdate();
  }
};

/**
 * Gets the default UML types for classifiers and features.
 * 
 * @author Gabriel Leonardo Diaz, 27.01.2014.
 * @returns A JSon object with the available types.
 */
CLASSGraph.prototype.getTypes = function () {
  var jSonData   = [];
  
  // Fixed types
  jSonData.push({id:"boolean", text:"boolean"});
  jSonData.push({id:"byte",    text:"byte"});
  jSonData.push({id:"char",    text:"char"});
  jSonData.push({id:"double",  text:"double"});
  jSonData.push({id:"float",   text:"float"});
  jSonData.push({id:"int",     text:"int"});
  jSonData.push({id:"long",    text:"long"});
  jSonData.push({id:"short",   text:"short"});
  jSonData.push({id:"String",  text:"String"});
  
  var cell;
  
  // Dynamic types
  for (var key in this.model.cells) {
    cell = this.model.getCell(key);
    
    if (this.isClassifier(cell.value)) {
      jSonData.push({id:cell.getAttribute("name"), text: cell.getAttribute("name")});
    }
  }
  
  return jSonData;
};

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
 * Checks if the node is a UML classifier or Comment or Package.
 * @param node
 */
CLASSGraph.prototype.isElementVertex = function (node) {
  return this.isClassifier(node) || this.isComment(node) || this.isPackage(node);
};

/**
 * Checks if the given node is a UML named element.
 * @param node
 * @returns {Boolean}
 */
CLASSGraph.prototype.isNamedElement = function (node) {
  return this.isClassifier(node) || this.isFeature(node) || this.isPackage(node) || this.isEnumerationLiteral(node) ||
         this.isAssociation(node) || this.isDependency(node) || this.isRealization(node) || this.isGeneralization(node);
};

/**
 * Checks if the given node is a UML feature.
 * @param node
 * @returns {Boolean}
 */
CLASSGraph.prototype.isFeature = function (node) {
  return this.isProperty(node) || this.isOperation(node);
};

/**
 * Checks if the given node is a property element.
 * 
 * @param node
 * @returns {Boolean}
 */
CLASSGraph.prototype.isProperty = function (node) {
  if (node == null || node.nodeName == null) {
    return false;
  }
  return node.nodeName.toLowerCase() == "property";
};

/**
 * Checks if the given node is an operation element.
 * @param node
 * @returns {Boolean}
 */
CLASSGraph.prototype.isOperation = function (node) {
  if (node == null || node.nodeName == null) {
    return false;
  }
  return node.nodeName.toLowerCase() == "operation";
};

/**
 * Checks if the given node is an enumeration literal.
 * 
 * @param node
 * @return {Boolean}
 */
CLASSGraph.prototype.isEnumerationLiteral = function (node) {
  if (node == null || node.nodeName == null) {
    return false;
  }
  return node.nodeName.toLowerCase() == "enumerationliteral";
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

/**
 * Checks if the given node is a section of a classifier.
 * @param node
 * @returns {Boolean}
 */
CLASSGraph.prototype.isSection = function (node) {
  if (node == null || node.nodeName == null) {
    return false;
  }
  return node.nodeName.toLowerCase() == "section";
};

/**
 * Checks if the given node is a relationship UML element.
 * @param node
 * @returns {Boolean}
 */
CLASSGraph.prototype.isRelationship = function (node) {
  return this.isAssociation(node) || this.isDependency(node) || this.isRealization(node) || this.isGeneralization(node);
};

/**
 * Checks if the given node is an association UML element.
 * @param node
 * @returns {Boolean}
 */
CLASSGraph.prototype.isAssociation = function (node) {
  if (node == null || node.nodeName == null) {
    return false;
  }
  return node.nodeName.toLowerCase() == "association";
};

/**
 * Checks if the given node is an dependency UML element.
 * @param node
 * @returns {Boolean}
 */
CLASSGraph.prototype.isDependency = function (node) {
  if (node == null || node.nodeName == null) {
    return false;
  }
  return node.nodeName.toLowerCase() == "dependency";
};

/**
 * Checks if the given node is a realization UML element.
 * @param node
 * @returns {Boolean}
 */
CLASSGraph.prototype.isRealization = function (node) {
  if (node == null || node.nodeName == null) {
    return false;
  }
  return node.nodeName.toLowerCase() == "realization";
};

/**
 * Checks if the given node is a generalization UML element.
 * @param node
 * @returns {Boolean}
 */
CLASSGraph.prototype.isGeneralization = function (node) {
  if (node == null || node.nodeName == null) {
    return false;
  }
  return node.nodeName.toLowerCase() == "generalization";
};

/**
 * Checks if the given node is a UML comment link.
 * @param node
 * @returns {Boolean}
 */
CLASSGraph.prototype.isLink = function (node) {
  if (node == null || node.nodeName == null) {
    return false;
  }
  return node.nodeName.toLowerCase() == "link";
};

/**
 * Overrides createHandler() in mxGraph, this allows to configure the custom
 * handler for context icons.
 * 
 * @param state
 * @returns
 * @author Gabriel Leonardo Diaz, 18.02.2014.
 */
CLASSGraph.prototype.createHandler = function (state) {
  if (state && this.model.isVertex(state.cell)) {
    return new CLASSGraphContextIconHandler(state);
  }
  return mxGraph.prototype.createHandler.apply(this, arguments);
};

/**
 * Internal class to handle context icons on vertexes.
 * 
 * @author Gabriel Leonardo Diaz, 18.02.2014.
 */
CLASSGraphContextIconHandler = function (state) {
  mxVertexHandler.apply(this, arguments);
};
mxUtils.extend(CLASSGraphContextIconHandler, mxVertexHandler);

/**
 * The node that will contain the icons.
 */
CLASSGraphContextIconHandler.prototype.domNode;

/**
 * Initializes the component.
 * 
 * @author Gabriel Leonardo Diaz, 18.02.2014.
 */
CLASSGraphContextIconHandler.prototype.init = function () {
  mxVertexHandler.prototype.init.apply(this, arguments);
  
  if (this.state.cell == null || !this.graph.isElementVertex(this.state.cell.value)) {
    // Invalid cell user object.
    return;
  }
  
  this.domNode = document.createElement("div");
  this.domNode.style.position = "absolute";
  this.domNode.style.whiteSpace = "nowrap";
  
  // 1. CONNECT
  var img = this.createImage("/CLASSModeler/resources/images/next_16x16.png");
  mxEvent.addListener(img, "click", mxUtils.bind(this, function (evt) {
    var point = mxUtils.convertPoint(this.graph.container, mxEvent.getClientX(evt), mxEvent.getClientY(evt));
    this.graph.connectionHandler.start(this.state, point.x, point.y);
    this.graph.isMouseDown = true;
    mxEvent.consume(evt);
  }));
  this.domNode.appendChild(img);
  
  // 2. DELETE
  var img = this.createImage("/CLASSModeler/resources/images/delete_16x16.png");
  mxEvent.addListener(img, "click", mxUtils.bind(this, function (evt) {
    this.graph.removeCells([this.state.cell]);
    mxEvent.consume(evt);
  }));
  this.domNode.appendChild(img);
  
  // Finishes the configuration
  this.graph.container.appendChild(this.domNode);
  this.redrawTools();
};

/**
 * Overrides redraw() in mxGraph. Just to call another method redrawTools().
 * 
 * @author Gabriel Leonardo Diaz, 18.02.2014.
 */
CLASSGraphContextIconHandler.prototype.redraw = function (){
  mxVertexHandler.prototype.redraw.apply(this);
  this.redrawTools();
};

/**
 * Locates the dom element a side of the vertext graphic.
 * 
 * @author Gabriel Leonardo Diaz, 18.02.2014.
 */
CLASSGraphContextIconHandler.prototype.redrawTools = function() {
  if (this.state != null && this.domNode != null) {
    var dy = (mxClient.IS_VML && document.compatMode == "CSS1Compat") ? 20 : 2;
    this.domNode.style.left = (this.state.x + this.state.width + dy) + "px";
    this.domNode.style.top = (this.state.y + dy) + "px";
  }
};

/**
 * Removes the dom element from the document.
 * 
 * @param sender
 * @param me
 * @author Gabriel Leonardo Diaz, 18.02.2014.
 */
CLASSGraphContextIconHandler.prototype.destroy = function (sender, me) {
  mxVertexHandler.prototype.destroy.apply(this, arguments);
  if (this.domNode != null) {
    this.domNode.parentNode.removeChild(this.domNode);
    this.domNode = null;
  }
};

/**
 * Utility method to create images.
 * @param src
 * @returns
 */
CLASSGraphContextIconHandler.prototype.createImage = function (src) {
  var img;
  
  if (mxClient.IS_IE && !mxClient.IS_SVG) {
    img = document.createElement("div");
    img.style.backgroundImage = "url(" + src + ")";
    img.style.backgroundPosition = "center";
    img.style.backgroundRepeat = "no-repeat";
  }
  else {
    img = mxUtils.createImage(src);
  }
  
  img.style.cursor = "pointer";
  img.style.width  = "16px";
  img.style.height = "16px";
  img.style.display = "block";
  img.style.margin = "3px";
  
  img.onmouseover = function () {
    this.style.border = "1px solid #CCCCCC";
    this.style.margin = "1px";
    this.style.backgroundColor = "#FFFFFF";
  };
  
  img.onmouseout = function () {
    this.style.border = "";
    this.style.margin = "3px";
    this.style.backgroundColor = "";
  };
  
  mxEvent.addGestureListeners(img, mxUtils.bind(this, function (evt) {
    // Disables dragging the image
    mxEvent.consume(evt);
  }));
  
  return img;
};


