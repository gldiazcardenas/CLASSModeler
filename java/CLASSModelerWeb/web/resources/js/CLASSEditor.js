/*******************************************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS 
 * Cúcuta, Colombia 
 * (c) 2013 by UFPS. All rights reserved.
 * 
 * @author: Gabriel Leonardo Diaz, 21.04.2013.
 * 
 ******************************************************************************/

/**
 * JavaScript Class for the editor, this inherits from mxEditor.
 * 
 * @author Gabriel Leonardo Diaz, 07.10.2013.
 */
CLASSEditor = function (config) {
  mxEditor.call(this, config);
};
mxUtils.extend(CLASSEditor, mxEditor);

/**
 * Function called on initializing the mxEditor component.
 * 
 * @author Gabriel Leonardo Diaz, 01.12.2013.
 */
CLASSEditor.prototype.onInit = function () {
  var selfEditor = this;
  var selftGraph = this.graph;
  
  // 1. CELLS RENDERING
  selftGraph.convertValueToString = function (cell) {
    return selfEditor.convertNodeToString(cell.value);
  };
  
  // 2. SECOND LABEL
  // TODO
  
  // 3. POPUP MENU
  selftGraph.panningHandler.factoryMethod = function (menu, cell, evt) {
    return selfEditor.createPopupMenu(menu, cell, evt);
  };
};

/**
 * Provides a suitable name for the XML node template.
 * 
 * @param node
 *          The XML node of the template.
 * @returns The String representation of the node.
 * @author Gabriel Leonardo Diaz, 03.12.2013.
 */
CLASSEditor.prototype.convertNodeToString = function (node) {
  if (this.isClass(node) || this.isEnumeration(node) || this.isInterface(node) || this.isPackage(node)) {
    return node.getAttribute("name");
  }
  
  if (this.isComment(node)) {
    return node.getAttribute("body");
  }
  
  return "";
};

/**
 * Provides a suitable second label for the XML node template.
 * 
 * @param node
 *          The XML node of the template
 * @returns A string representing the second label.
 * @author Gabriel Leonardo Diaz, 04.12.2013.
 */
CLASSEditor.prototype.getSecondLabel = function (node) {
  if (this.isEnumeration(node)) {
    return "<<Enumeration>>";
  }
  
  if (this.isInterface(node)) {
    return "<<Interface>>";
  }
  
  return null;
};

/**
 * Creates the pop-up menu for the given selected cell.
 * 
 * @param menu
 * @param cell
 * @param evt
 * @author Gabriel Leonardo Diaz, 08.01.2014.
 */
CLASSEditor.prototype.createPopupMenu = function (menu, cell, evt) {
  var self = this;
  
  var undo = function () {
    self.execute('undo');
  };
  
  var redo = function () {
    self.execute('redo');
  };
  
  var copy = function () {
    self.execute('copy');
  };
  
  var cut = function () {
    self.execute('cut');
  };
  
  var paste = function () {
    self.execute('paste');
  };
  
  var deleteC = function () {
    self.execute('delete');
  };
  
  var selectAll = function () {
    self.execute('selectAll');
  };
  
  var generateCode = function () {
    // TODO GD
  };
  
  var generateImage = function () {
    // TODO GD
  };
  
  var showAttributes = function () {
    // TODO GD
  };
  
  var showOperations = function () {
    // TODO GD
  };
  
  var showProperties = function () {
    // TODO GD
  };
  
  menu.addItem("Deshacer", null, undo, null, null, true);
  menu.addItem("Rehacer", null, redo, null, null, true);
  
  menu.addSeparator();
  
  menu.addItem("Copiar", null, copy, null, null, true);
  menu.addItem("Cortar", null, cut, null, null, true);
  menu.addItem("Pegar", null, paste, null, null, true);
  
  menu.addSeparator();
  
  menu.addItem("Eliminar", null, deleteC, null, null, true);
  menu.addItem("Seleccionar Todo", null, selectAll, null, null, true);
  
  menu.addSeparator();
  
  var subMenu = menu.addItem("Herramientas");
  menu.addItem("Generar Codigo", null, generateCode, subMenu, null, true);
  menu.addItem("Generar Imagen", null, generateImage, subMenu, null, true);
  
  menu.addSeparator();
  
  menu.addItem("Atributos", null, showAttributes, null, null, self.isClassifier(cell));
  menu.addItem("Operaciones", null, showOperations, null, null, self.isClassifier(cell));
  
  menu.addSeparator();
  
  menu.addItem("Propiedades", null, showProperties, null, null, true);
};

/**
 * Check if the given node is allowed to show attributes.
 * 
 * @param node
 * @returns
 */
CLASSEditor.prototype.isClassifier = function (node) {
  return this.isClass(node) || this.isEnumeration(node) || this.isInterface(node);
};

CLASSEditor.prototype.canShowOperations = function (node) {
  
};

/**
 * Checks if the given node is a class element.
 * 
 * @param node
 * @returns {Boolean}
 */
CLASSEditor.prototype.isClass = function (node) {
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
CLASSEditor.prototype.isPackage = function (node) {
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
CLASSEditor.prototype.isEnumeration = function (node) {
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
CLASSEditor.prototype.isInterface = function (node) {
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
CLASSEditor.prototype.isComment = function (node) {
  if (node == null || node.nodeName == null) {
    return false;
  }
  return node.nodeName.toLowerCase() == "comment";
};
