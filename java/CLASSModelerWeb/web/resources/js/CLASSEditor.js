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
  var selfGraph  = this.graph;
  
  // 1. CELLS RENDERING
  selfGraph.convertValueToString = function (cell) {
    return selfEditor.convertNodeToString(cell.value);
  };
  
  // 2. SECOND LABEL
  // TODO
  
  // 3. POPUP MENU
  selfGraph.panningHandler.factoryMethod = function (menu, cell, evt) {
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
  
  var deleteAll = function () {
    self.execute('delete');
  };
  
  var selectAll = function () {
    self.execute('selectAll');
  };
  
  var showAttributes = function () {
    self.execute('showAttributes');
  };
  
  var showOperations = function () {
    self.execute('showOperations');
  };
  
  var generateCode = function () {
    self.execute('generateCode');
  };
  
  var generateImage = function () {
    self.execute('generateImage');
  };
  
  menu.addItem("Deshacer", null, undo, null, null, true);
  menu.addItem("Rehacer", null, redo, null, null, true);
  
  menu.addSeparator();
  
  menu.addItem("Copiar", null, copy, null, null, true);
  menu.addItem("Cortar", null, cut, null, null, true);
  menu.addItem("Pegar", null, paste, null, null, true);
  
  menu.addSeparator();
  
  menu.addItem("Eliminar", null, deleteAll, null, null, true);
  menu.addItem("Seleccionar Todo", null, selectAll, null, null, true);
  
  menu.addSeparator();
  
  menu.addItem("Atributos", null, showAttributes, null, null, this.isClassifier(cell));
  menu.addItem("Operaciones", null, showOperations, null, null, this.isClassifier(cell));
  
  menu.addSeparator();
  
  var subMenu = menu.addItem("Herramientas");
  menu.addItem("Generar Codigo", null, generateCode, subMenu, null, true);
  menu.addItem("Generar Imagen", null, generateImage, subMenu, null, true);
};

/**
 * Check if the given node is allowed to show attributes.
 * 
 * @param node
 * @returns
 */
CLASSEditor.prototype.isClassifier = function (cell) {
  if (cell == null) {
    return false;
  }
  return this.isClass(cell.value) || this.isEnumeration(cell.value) || this.isInterface(cell.value);
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

/**
 * Overrides the addActions() function in mxEditor in order to add some custom
 * actions to the array.
 * 
 * @author Gabriel Leonardo Diaz, 12.01.2014.
 */
CLASSEditor.prototype.addActions = function () {
  mxEditor.prototype.addActions.call(this);
  
  this.addAction('zoom25', function (editor) {
    editor.graph.zoomTo(25/100);
  });
  
  this.addAction('zoom50', function (editor) {
    editor.graph.zoomTo(50/100);
  });
  
  this.addAction('zoom75', function (editor) {
    editor.graph.zoomTo(75/100);
  });
  
  this.addAction('zoom100', function (editor) {
    editor.graph.zoomTo(100/100);
  });
  
  this.addAction('zoom150', function (editor) {
    editor.graph.zoomTo(150/100);
  });
  
  this.addAction('zoom200', function (editor) {
    editor.graph.zoomTo(200/100);
  });
  
  this.addAction('zoom400', function (editor) {
    editor.graph.zoomTo(400/100);
  });
  
  this.addAction('moveLeft', function (editor) {
    editor.moveCells(37);
  });
  
  this.addAction('moveUp', function (editor) {
    editor.moveCells(38);
  });
  
  this.addAction('moveRight', function (editor) {
    editor.moveCells(39);
  });
  
  this.addAction('moveDown', function (editor) {
    editor.moveCells(40);
  });
  
  this.addAction('showAttributes', function (editor) {
    editor.showAttributes(editor.graph.getSelectionCell());
  });
  
  this.addAction('showOperations', function (editor) {
    editor.showOperations(editor.graph.getSelectionCell());
  });
  
  this.addAction('generateCode', function (editor) {
    editor.generateCode();
  });
  
  this.addAction('generateImage', function (editor) {
    editor.generateImage();
  });
};

/**
 * Allows to move the selected cells in the direction of the key pressed (LEFT,
 * RIGHT, UP, DOWN).
 * 
 * @param keyCode
 *          The JavaScript code of the key pressed.
 * @author Gabriel Leonardo Diaz, 12.01.2014.
 */
CLASSEditor.prototype.moveCells = function (keyCode) {
  if (!this.graph.isSelectionEmpty()) {
    var dx = 0;
    var dy = 0;
    
    if (keyCode == 37) {
      dx = -10;
    }
    else if (keyCode == 38) {
      dy = -10;
    }
    else if (keyCode == 39) {
      dx = 10;
    }
    else if (keyCode == 40) {
      dy = 10;
    }
    
    this.graph.moveCells(this.graph.getSelectionCells(), dx, dy);
    this.graph.scrollCellToVisible(this.graph.getSelectionCell());
  }
};

/**
 * Opens the code generation dialog.
 * 
 * @author Gabriel Leonardo Diaz, 14.01.2014.
 */
CLASSEditor.prototype.generateCode = function () {
  dlgGenerateCode.show();
};

/**
 * Generates a plain image of the current diagram.
 * 
 * @author Gabriel Leonardo Diaz, 14.01.2014.
 */
CLASSEditor.prototype.generateImage = function () {
  // TODO GD
};

/**
 * Shows the attributes of one classifier.
 * 
 * @param cell
 *          The cell containing the classifier XML representation.
 * @author Gabriel Leonardo Diaz, 16.01.2014.
 */
CLASSEditor.prototype.showAttributes = function (cell) {
  var attributesDialog = new CLASSAttributesDialog(this.graph);
  attributesDialog.init(cell);
  attributesDialog.show();
};

/**
 * Shows the operations of one classifier.
 * 
 * @param cell
 *          The cell containing the classifier XML representation.
 * @author Gabriel Leonardo Diaz, 16.01.2014.
 */
CLASSEditor.prototype.showOperations = function (cell) {
  // TODO GD
};