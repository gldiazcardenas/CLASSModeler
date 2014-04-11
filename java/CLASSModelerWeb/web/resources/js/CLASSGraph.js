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
    return this.convertPropertyToString(node, this.isAssociation(cell.parent.value));
  }
  
  if (this.isOperation(node)) {
    return this.convertOperationToString(cell);
  }
  
  if (this.isClassifier(node)) {
    return this.convertClassifierToString(node);
  }
  
  if (this.isNamedElement(node)) {
    return node.getAttribute("name");
  }
  
  return "";
};

/**
 * Converts the XML classifier to a string representation.
 * @param classifier
 * @returns
 */
CLASSGraph.prototype.convertClassifierToString = function (classifier) {
  var packageId = classifier.getAttribute("package");
  if (packageId) {
    var packageCell = this.model.getCell(packageId);
    return packageCell.getAttribute("name") + "::" + classifier.getAttribute("name");
  }
  return classifier.getAttribute("name");
};

/**
 * Converts the XML property node to a string representation.
 * 
 * @param property
 * @return {String}
 * @author Gabriel Leonardo Diaz, 25.02.2014.
 */
CLASSGraph.prototype.convertPropertyToString = function (property, isAssociation) {
  var visibility   = property.getAttribute("visibility");
  var name         = property.getAttribute("name");
  var isNavigable  = this.isNavigable(property);
  
  var label        = "";
  
  // Properties of association might not have name or visibility.
  if (isNavigable) {
    label = this.getVisibilityChar(visibility) + " " + name;
  }
  
  if (isAssociation) {
    var multiplicity = property.getAttribute("multiplicity");
    
    if (isNavigable) {
      label += " : ";
    }
    
    if (multiplicity) {
      label += multiplicity;
    }
  }
  else {
    label += " : " + this.convertTypeToString(property);
    
    var initialValue = property.getAttribute("initialValue");
    if (initialValue) {
      label += " = " + initialValue;
    }
  }
  
  return label;
};

/**
 * Converts the XML operation node to a string representation.
 * 
 * @param operation
 * @returns {String}
 * @author Gabriel Leonardo Diaz, 25.02.2014.
 */
CLASSGraph.prototype.convertOperationToString = function (operationCell) {
  var visibilityChar = this.getVisibilityChar(operationCell.getAttribute("visibility"));
  var name           = " " + operationCell.getAttribute("name");
  var parameters     = "(" + this.convertParametersToString(operationCell) + ")";
  var returnType     = "";
  
  if (operationCell.getAttribute("type")) {
    returnType = " : " + this.convertTypeToString(operationCell.value);
  }
  
  return visibilityChar + name + parameters + returnType;
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
    parameterString += separator + this.convertTypeToString(param);
    separator = ", ";
  }
  
  return parameterString;
};

/**
 * Converts the type of the given property to string.
 * @param feature
 */
CLASSGraph.prototype.convertTypeToString = function (feature) {
  var typeName = this.convertTypeIdToNameString(feature.getAttribute("type"));
  
  var collection = feature.getAttribute("collection");
  if (collection) {
    var collectionName = this.getCollectionName(collection);
    if (collection == "array") {
      typeName = typeName + collectionName;
    }
    else {
      typeName = collectionName + "<" + typeName + ">";
    }
  }
  
  return typeName;
};

/**
 * Gets the type name of the given id.
 * @param typeId
 * @returns {String}
 */
CLASSGraph.prototype.convertTypeIdToNameString = function (typeId) {
  var typeName = "";
  if (typeId) {
    var cell = this.model.getCell(typeId);
    if (cell) {
      typeName = cell.getAttribute("name");
    }
    else {
      typeName = typeId;
    }
  }
  return typeName;
};

/**
 * Gets the collection name identified by the given collection id.
 * 
 * @param collectionId
 * @author Gabriel Leonardo Diaz, 04.03.2014.
 */
CLASSGraph.prototype.getCollectionName = function (collectionId) {
  var collections = this.getCollectionsJSon();
  for (var i = 0; i < collections.length; i++) {
    if (collections[i].id == collectionId) {
      return collections[i].text;
    }
  }
  return collectionId;
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
    if (section.getAttribute("attribute") == "1") {
      if (section.children == null) {
        return [];
      }
      
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
    if (section.getAttribute("attribute") == "0") {
      if (section.children == null) {
        return [];
      }
      
      return section.children;
    }
  }
  
  return [];
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
    if (section.getAttribute("attribute") == "1") {
      for (var j = 0; j < section.children.length; j++) {
        child = section.getChildAt(j);
        if (this.isLiteral(child.value)) {
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
  else if (this.isComment(cell.value)) {
    this.cellEditProperty(cell, "body", newValue, true);
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
  else if (this.isComment(cell.value)) {
    return cell.value.getAttribute("body");
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
  if (attrName == "name" && attrValue == null || attrValue.length == 0) {
    // Invalid name
    return;
  }
  
  this.model.beginUpdate();
  
  try {
    var node  = cell.value;
    
    // Validates the name of the classifier
    if (attrName == "name" && this.isClassifier(node)) {
      attrValue = this.getUniqueName(attrValue);
    }
    
    // Clones the value for correct UNDO/REDO
    var clone = node.cloneNode(true);
    clone.setAttribute(attrName, attrValue);
    
    // Set the user object of the cell
    this.model.setValue(cell, clone);
    
    // Adjust the styles
    if (attrName == "abstract" || attrName == "static") {
      this.adjustStyleCell(cell);
    }
    
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
 * Validates the initial name does not exist, in case this is already used this
 * transforms the name in order to get a unique one by adding '1' character at
 * the end.
 * 
 * @param initialName
 *          The initial name.
 * @author Gabriel Leonardo Diaz, 26.03.2014.
 */
CLASSGraph.prototype.getUniqueName = function (initialName) {
  var cell;
  for (var id in this.model.cells) {
    cell = this.model.cells[id];
    if (this.isClassifier(cell.value)) {
      if (cell.getAttribute("name") == initialName) {
        return this.getUniqueName(initialName + "1");
      }
    }
  }
  return initialName;
};

/**
 * Overrides the function cellsRemoved() in mxGraph. This allows to adjust the
 * data in the model after deleting some objects.
 * 
 * @param cells
 * @author Gabriel Leonardo Diaz, 02.03.2014.
 */
CLASSGraph.prototype.cellsRemoved = function (cells) {
  for (var i = 0; i < cells.length; i++) {
    cell = cells[i];
    
    if (this.isProperty(cell.value) && cell.parent.isEdge() && this.isAssociation(cell.parent.value)) {
      this.removeAssociationNavigableStyle(cell.getAttribute("type") == cell.parent.source.id, cell.parent);
    }
  }
  
  mxGraph.prototype.cellsRemoved.apply(this, arguments);
  
  var cell;
  
  for (var i = 0; i < cells.length; i++) {
    cell = cells[i];
    
    if (this.isPackage(cell.value)) {
      this.removeClassifiersPackage(cell.id);
    }
    else if (this.isClassifier(cell.value)) {
      this.resetElementsType(cell.id);
    }
  }
};

/**
 * Resets the element type to 'int' for properties, operations and parameters referencing the deleted classifier.
 * @param classifierCell
 * @author Gabriel Leonardo Diaz, 03.03.2014.
 */
CLASSGraph.prototype.resetElementsType = function (typeId) {
  var cell;
  for (var key in this.model.cells) {
    cell = this.model.getCell(key);
    
    if (cell.getAttribute("type") == typeId && (this.isProperty(cell.value) || this.isOperation(cell.value) || this.isParameter(cell.value))) {
      var node  = cell.value;
      
      // Clones the value for correct UNDO/REDO
      var clone = node.cloneNode(true);
      clone.setAttribute("type", "int");
      
      // Set the user object of the cell
      this.model.setValue(cell, clone);
    }
  }
};

/**
 * Removes the package of the classifiers assigned to the given one name.
 * 
 * @param packageName
 * @author Gabriel Leonardo Diaz, 03.03.2014.
 */
CLASSGraph.prototype.removeClassifiersPackage = function (packageId) {
  var cell;
  for (var key in this.model.cells) {
    cell = this.model.getCell(key);
    
    if (this.isClassifier(cell.value) && cell.getAttribute("1") == packageId) {
      var node  = cell.value;
      
      // Clones the value for correct UNDO/REDO
      var clone = node.cloneNode(true);
      clone.setAttribute("package", "");
      
      // Set the user object of the cell
      this.model.setValue(cell, clone);
    }
  }
};

/**
 * Adjusts the cell style.
 * @param cell
 */
CLASSGraph.prototype.adjustStyleCell = function (cell) {
  var styleValue = 0;
  
  if (this.isClassifier(cell.value)) {
    styleValue += mxConstants.FONT_BOLD;
  }
  
  if (cell.getAttribute("abstract") == "1") {
    styleValue += mxConstants.FONT_ITALIC;
  }
  
  if (cell.getAttribute("static") == "1") {
    styleValue += mxConstants.FONT_UNDERLINE;
  }
  
  this.setCellStyles(mxConstants.STYLE_FONTSTYLE, styleValue, [cell]);
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
      this.privateCellSizeUpdated(cell, ignoreChildren);
    }
    finally {
      this.model.endUpdate();
    }
  }
};

/**
 * Private method that does not start an update in the model, just updates the cell size.
 * @param cell
 * @param ignoreChildren
 * @author Gabriel Leonardo Diaz, 14.03.2014.
 */
CLASSGraph.prototype.privateCellSizeUpdated = function (cell, ignoreChildren) {
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
 * Checks if the given property is navigable.
 * 
 * @param property
 * @author Gabriel Leonardo Diaz, 30.03.2014.
 */
CLASSGraph.prototype.isNavigable = function (property) {
  return property != null && property.getAttribute("name") != null && property.getAttribute("name").length > 0;
};

/**
 * Avoid to lock properties cells on associations.
 * @param cell
 * @returns {Boolean}
 */
CLASSGraph.prototype.isCellLocked = function (cell) {
  if (this.isProperty(cell.value)) {
    return this.isCellsLocked();
  }
  return mxGraph.prototype.isCellLocked.apply(this, arguments);
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
      if (child.getAttribute("attribute") == "1") {
        attrSection = child;
      }
    }
    
    if (attrSection == null) {
      attrSection = this.model.cloneCell(this.sectionTemplate);
      attrSection.setAttribute("attribute", "1");
      attrSection.insert(attributeCell);
      this.addCell(attrSection, classifierCell, 0);
    }
    else {
      this.addCell(attributeCell, attrSection);
    }
    
    this.adjustStyleCell(attributeCell);
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
      
      if (child.getAttribute("attribute") == "0") {
        operSection = child;
      }
    }
    
    if (operSection == null) {
      operSection = this.model.cloneCell(this.sectionTemplate);
      operSection.setAttribute("attribute", "0");
      operSection.insert(operationCell);
      this.addCell(operSection, classifierCell, 1);
    }
    else {
      this.addCell(operationCell, operSection);
    }
    
    this.adjustStyleCell(operationCell);
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
    this.adjustStyleCell(operationCell);
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
    this.adjustStyleCell(attributeCell);
    this.cellSizeUpdated(attributeCell, false);
  }
  finally {
    this.model.endUpdate();
  }
};

/**
 * Adds the attribute to the association.
 * 
 * @param associationCell
 * @param attributeCell
 * @param isSource
 * @param isInsert
 * @author Gabriel Leonardo Diaz, 16.03.2014.
 */
CLASSGraph.prototype.addAssociationAttribute = function (associationCell, attributeCell, isSource, isInsert) {
  isInsert = isInsert || false;
  isSource = isSource || false;
  
  // Adjust the attribute
  attributeCell.geometry.relative = true;
  if (isSource) {
    attributeCell.geometry.x = -1;
  }
  else {
    attributeCell.geometry.x = 1;
  }
  
  // Adjust the style of the association
  if (this.isNavigable(attributeCell.value)) {
    this.addAssociationNavigableStyle(isSource, associationCell);
  }
  
  // Add to the association
  if (isInsert) {
    associationCell.insert(attributeCell);
  }
  else {
    this.addCell(attributeCell, associationCell);
  }
  
  // Adjust styles
  this.setCellStyles(mxConstants.STYLE_MOVABLE, 1, [attributeCell]);
  this.privateCellSizeUpdated(attributeCell, false);
};

/**
 * Adds the style for a navigable association.
 * 
 * @author Gabriel Leonardo Diaz, 30.03.2014.
 */
CLASSGraph.prototype.addAssociationNavigableStyle = function (isSource, associationCell) {
  if (isSource) {
    if (!this.isAggregation(associationCell.value) && !this.isComposition(associationCell.value)) {
      this.setCellStyles(mxConstants.STYLE_STARTARROW, "open", [associationCell]);
    }
  }
  else {
    this.setCellStyles(mxConstants.STYLE_ENDARROW, "open", [associationCell]);
  }
};

/**
 * Removes the attribute from the model and adjusts the navigability of the
 * association.
 * 
 * @author Gabriel Leonardo Diaz, 16.03.2014.
 */
CLASSGraph.prototype.removeAssociationAttribute = function (associationCell, attributeCell, isSource) {
  this.removeCells([attributeCell]);
  this.removeAssociationNavigableStyle(isSource, associationCell);
};

/**
 * Removes the style for navigable association ends.
 * @param isSource
 * @param associationCell
 */
CLASSGraph.prototype.removeAssociationNavigableStyle = function (isSource, associationCell) {
  if (isSource) {
    if (!this.isAggregation(associationCell.value) && !this.isComposition(associationCell.value)) {
      this.setCellStyles(mxConstants.STYLE_STARTARROW, "", [associationCell]);
    }
  }
  else {
    this.setCellStyles(mxConstants.STYLE_ENDARROW, "", [associationCell]);
  }
};

/**
 * Gets the default UML types for classifiers and features in form of a JSon
 * object.
 * 
 * @author Gabriel Leonardo Diaz, 27.01.2014.
 * @returns A JSon object with the available types.
 */
CLASSGraph.prototype.getTypesJSon = function (includeVoid) {
  var jSonData   = [];
  
  // Fixed types
  if (includeVoid) {
    jSonData.push({id:"void",  text:"void"});
  }
  
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
      jSonData.push({ id: cell.id, text: cell.getAttribute("name") });
    }
  }
  
  return jSonData;
};

/**
 * Gets a JSon object with the available visibility kinds.
 * @returns {Array}
 * @author Gabriel Leonardo Diaz, 03.03.2014.
 */
CLASSGraph.prototype.getVisibilityJSon = function () {
  var jSonData   = [];
  
  jSonData.push({id: "public",    text: "public"});
  jSonData.push({id: "protected", text: "protected"});
  jSonData.push({id: "package",   text: "package"});
  jSonData.push({id: "private",   text: "private"});
  
  return jSonData;
};

/**
 * Gets a JSon object with the available collections in the system.
 * 
 * @returns {Array}
 * @author Gabriel Leonardo Diaz, 03.03.2014.
 */
CLASSGraph.prototype.getCollectionsJSon = function () {
  var jSonData   = [];
  
  jSonData.push({id: "array",         text: "[ ]"});
  jSonData.push({id: "ArrayList",     text: "ArrayList"});
  jSonData.push({id: "Collection",    text: "Collection"});
  jSonData.push({id: "EnumSet",       text: "EnumSet"});
  jSonData.push({id: "HashSet",       text: "HashSet"});
  jSonData.push({id: "List",          text: "List"});
  jSonData.push({id: "LinkedHashSet", text: "LinkedHashSet"});
  jSonData.push({id: "LinkedList",    text: "LinkedList"});
  jSonData.push({id: "Prioriqueue",   text: "PriorityQueue"});
  jSonData.push({id: "Queue",         text: "Queue"});
  jSonData.push({id: "Set",           text: "Set"});
  jSonData.push({id: "SortedSet",     text: "SortedSet"});
  jSonData.push({id: "Stack",         text: "Stack"});
  jSonData.push({id: "TreeSet",       text: "TreeSet"});
  jSonData.push({id: "Vector",        text: "Vector"});
  
  return jSonData;
};

/**
 * Gets the multiplicities JSon array.
 * 
 * @author Gabriel Leonardo Diaz, 10.04.2014.
 */
CLASSGraph.prototype.getMultiplicitiesJSon = function () {
  var jSonData   = [];
  
  jSonData.push({id: "*",     text: "*"});
  jSonData.push({id: "0",     text: "0"});
  jSonData.push({id: "0..*",  text: "0..*"});
  jSonData.push({id: "0..1",  text: "0..1"});
  jSonData.push({id: "1",     text: "1"});
  jSonData.push({id: "1..*",  text: "1..*"});
  
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
  return this.isClassifier(node) || this.isFeature(node) || this.isPackage(node) || this.isLiteral(node) ||
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
 * Checks if the given node is a parameter.
 * @param node
 * @returns {Boolean}
 */
CLASSGraph.prototype.isParameter = function (node) {
  if (node == null || node.nodeName == null) {
    return false;
  }
  return node.nodeName.toLowerCase() == "parameter";
};

/**
 * Checks if the given node is an enumeration literal.
 * 
 * @param node
 * @return {Boolean}
 */
CLASSGraph.prototype.isLiteral = function (node) {
  if (node == null || node.nodeName == null) {
    return false;
  }
  return node.nodeName.toLowerCase() == "literal";
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
 * Checks if the given node is an association UML element where one of its parts
 * is aggregate kind.
 * 
 * @param node
 * @returns {Boolean}
 */
CLASSGraph.prototype.isAggregation = function (node) {
  if (!this.isAssociation(node)) {
    return false;
  }
  return node.getAttribute("aggregation") == "shared";
};

/**
 * Checks if the given node is an association UML element where one of its parts
 * is composite kind.
 * 
 * @param node
 * @returns {Boolean}
 */
CLASSGraph.prototype.isComposition = function (node) {
  if (!this.isAssociation(node)) {
    return false;
  }
  return node.getAttribute("aggregation") == "composite";
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
  img.title = "Crear Relacion";
  mxEvent.addListener(img, "click", mxUtils.bind(this, function (evt) {
    var point = mxUtils.convertPoint(this.graph.container, mxEvent.getClientX(evt), mxEvent.getClientY(evt));
    if (this.graph.isEnabled()) {
      this.graph.connectionHandler.start(this.state, point.x, point.y);
      this.graph.isMouseDown = true;
    }
    mxEvent.consume(evt);
  }));
  this.domNode.appendChild(img);
  
  // 2. DELETE
  var img = this.createImage("/CLASSModeler/resources/images/delete_16x16.png");
  img.title = "Eliminar";
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
  img.style.margin = "2px";
  
  img.onmouseover = function () {
    this.style.border = "1px solid #CCCCCC";
    this.style.margin = "0px";
    this.style.backgroundColor = "#FFFFFF";
  };
  
  img.onmouseout = function () {
    this.style.border = "";
    this.style.margin = "2px";
    this.style.backgroundColor = "";
  };
  
  mxEvent.addGestureListeners(img, mxUtils.bind(this, function (evt) {
    // Disables dragging the image
    mxEvent.consume(evt);
  }));
  
  return img;
};

/**
 * Returns a new string value with the special characters converted to HTML.
 * 
 * @param stringValue
 * @author Gabriel Leonardo Diaz, 04.03.2014.
 */
CLASSGraph.prototype.escape = function (stringValue) {
  var escapedString = stringValue;
  if (escapedString) {
    escapedString = escapedString.replace("<", "&lt;");
    escapedString = escapedString.replace(">", "&gt;");
  }
  return escapedString;
};


