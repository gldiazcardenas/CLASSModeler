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
 * Instance of the dialog used to edit attributes.
 */
CLASSEditor.prototype.attrDialog;

/**
 * Instance of the dialog used to edit operations.
 */
CLASSEditor.prototype.operDialog;

/**
 * The id of the selected edge.
 */
CLASSEditor.prototype.selectedEdge;

/**
 * Function called on initializing the editor component.
 * @author Gabriel Leonardo Diaz, 29.01.2014.
 */
CLASSEditor.prototype.onInit = function () {
  // Set the section template used to create attributes and operations.
  this.graph.sectionTemplate = this.getTemplate("section");
  
  // Default edge is Association
  this.defaultEdge = this.getTemplate("association");
};

/**
 * Overrides createGraph() in mxEditor. Allows to create a custom instance of
 * the Graph component.
 * 
 * @author Gabriel Leonardo Diaz, 18.01.2014.
 */
CLASSEditor.prototype.createGraph = function () {
  var graph = new CLASSGraph(null, null, this.graphRenderHint);
  
  // Enables rubberband, tooltips, panning
  graph.setTooltips(false);
  graph.setPanning(true);
  graph.setConnectable(false);
  graph.setAllowDanglingEdges(false);
  graph.setDisconnectOnMove(false);

  // Overrides the dblclick method on the graph to invoke the dblClickAction
  // for a cell and reset the selection tool in the toolbar
  this.installDblClickHandler(graph);
  
  // Installs the command history
  this.installUndoHandler(graph);

  // Installs the handlers for the root event
  this.installDrillHandler(graph);
  
  // Installs the handler for validation
  this.installChangeHandler(graph);

  // Installs the handler for calling the insert function and consume the
  // event if an insert function is defined
  this.installInsertHandler(graph);
  
  // Redirects the function for creating the popupmenu items
  graph.panningHandler.autoExpand = true;
  graph.panningHandler.useGrid = true;
  graph.panningHandler.factoryMethod = mxUtils.bind(this, function(menu, cell, evt) {
    return this.createPopupMenu(menu, cell, evt);
  });

  // Redirects the function for creating new connections in the diagram
  graph.connectionHandler.factoryMethod = mxUtils.bind(this, function(source, target) {
    return this.createEdge(source, target);
  });
  
  // Connect preview for elbow edges
  graph.connectionHandler.createEdgeState = function (me) {
    var edge = this.graph.createEdge(null, null, null, null, null, "edgeStyle=elbowEdgeStyle");
    return new mxCellState(this.graph.view, edge, this.graph.getCellStyle(edge));
  };
  
  // Maintains swimlanes and installs autolayout
  this.layoutSwimlanes = true;
  this.createSwimlaneManager(graph);
  var layoutManager = this.createLayoutManager(graph);
  
  // Override method to layout internal elements of a classifier
  layoutManager.layoutCells = function (cells) {
    if (cells.length > 0) {
      var model = this.getGraph().getModel();
      model.beginUpdate();
      
      try {
        var last = null;
        
        for (var i = 0; i < cells.length; i++) {
          if (cells[i] != model.getRoot() && cells[i] != last) {
            last = cells[i];
            this.executeLayout(this.getLayout(last), last);
            
            // GD, 25.10.2013. Applying layout to also child swimlane nodes
            if (last.children != null) {
              this.layoutCells(last.children);
            }
          }
        }
        
        this.fireEvent(new mxEventObject(mxEvent.LAYOUT_CELLS, "cells", cells));
      }
      finally {
        model.endUpdate();
      }
    }
  };
  
  return graph;
};

/**
 * Overrides setModified() in mxEditor. This was extended to visually mark the
 * diagram as edited.
 * 
 * @author Gabriel Leonardo Diaz, 17.02.2014.
 */
CLASSEditor.prototype.setModified = function (modified) {
  mxEditor.prototype.setModified.call(this, arguments);
  
  var pendingChangesLbl = document.getElementById("pendingChanges");
  
  if (modified) {
    pendingChangesLbl.innerHTML = "*";
  }
  else {
    pendingChangesLbl.innerHTML = "";
  }
};

/**
 * Overrides createEdge() in mxEditor. Configures the new edge cell from source
 * to target.
 * 
 * @param source
 * @param target
 * @author Gabriel Leonardo Diaz, 04.02.2014.
 */
CLASSEditor.prototype.createEdge = function (source, target) {
  return mxEditor.prototype.createEdge.call(this, arguments);
};

/**
 * Overrides createSwimlaneLayout() in mxEditor. Configures the layout for UML
 * classifiers (classes, interfaces and enumerations).
 * 
 * @author Gabriel Leonardo Diaz, 16.10.2013.
 */
CLASSEditor.prototype.createSwimlaneLayout = function () {
  var layout             = new mxStackLayout(this.graph, false);
  
  layout.fill            = true;
  layout.resizeParent    = true;
  layout.isVertexMovable = function (cell) {
    return true;
  };
  
  return layout;
};

/**
 * Creates the pop-up menu for the given selected cell.
 * 
 * @param menu
 *          The menu to append items.
 * @param cell
 *          The cell selected.
 * @param evt
 *          The mouse event.
 * @author Gabriel Leonardo Diaz, 08.01.2014.
 */
CLASSEditor.prototype.createPopupMenu = function (menu, cell, evt) {
  var self = this;
  
  var undo = function () {
    self.execute("undo");
  };
  
  var redo = function () {
    self.execute("redo");
  };
  
  var copy = function () {
    self.execute("copy");
  };
  
  var cut = function () {
    self.execute("cut");
  };
  
  var paste = function () {
    self.execute("paste");
  };
  
  var deleteAll = function () {
    self.execute("delete");
  };
  
  var selectAll = function () {
    self.execute("selectAll");
  };
  
  var showAttributes = function () {
    self.execute("showAttributes");
  };
  
  var showOperations = function () {
    self.execute("showOperations");
  };
  
  var editConnector = function () {
    self.execute("showRelationship");
  };
  
  var generateCode = function () {
    self.execute("generateCode");
  };
  
  var exportImage = function () {
    self.execute("exportImage");
  };
  
  var exportXMI = function () {
    self.execute("exportXMI");
  };
  
  var viewXML = function () {
    self.execute("viewXML");
  };
  
  var toFront = function () {
    self.execute("toFront");
  };
  
  var toBack = function () {
    self.execute("toBack");
  };
  
  menu.addItem("Deshacer", null, undo, null, null, true);
  menu.addItem("Rehacer", null, redo, null, null, true);
  
  menu.addSeparator();
  
  menu.addItem("Cortar", null, cut, null, null, this.isClassifierCell(cell));
  menu.addItem("Copiar", null, copy, null, null, this.isClassifierCell(cell));
  menu.addItem("Pegar", null, paste, null, null, true);
  
  menu.addSeparator();
  
  menu.addItem("Eliminar", null, deleteAll, null, null, cell != null);
  menu.addItem("Seleccionar Todo", null, selectAll, null, null, true);
  
  menu.addSeparator();
  
  var subMenu = menu.addItem("Orden Z");
  menu.addItem("Traer adelante", null, toFront, subMenu, null, self.isElementVertexCell(cell));
  menu.addItem("Enviar atras", null, toBack, subMenu, null, self.isElementVertexCell(cell));
  
  menu.addSeparator();
  
  menu.addItem("Atributos", null, showAttributes, null, null, self.isClassifierCell(cell));
  menu.addItem("Operaciones", null, showOperations, null, null, self.isClassifierCell(cell));
  menu.addItem("Editar Relacion", null, editConnector, null, null, self.isRelationshipCell(cell));
  
  menu.addSeparator();
  
  var subMenu = menu.addItem("Herramientas");
  menu.addItem("Generar Codigo", null, generateCode, subMenu, null, true);
  menu.addItem("Generar Imagen", null, exportImage, subMenu, null, true);
  menu.addItem("Generar Metodos GET/SET", null, exportImage, subMenu, null, self.isPropertyCell(cell));
  menu.addSeparator(subMenu);
  menu.addItem("Exportar XMI", null, exportXMI, subMenu, null, true);
  menu.addItem("Mostrar XML", null, viewXML, subMenu, null, true);
};

/**
 * Determines if the user object (node) of the given cell is a Property UML.
 * @param cell
 * @returns
 */
CLASSEditor.prototype.isPropertyCell = function (cell) {
  if (cell == null) {
    return false;
  }
  return this.graph.isProperty(cell.value);
};

/**
 * Determines if the user object (node) of the cell is a classifier UML.
 */
CLASSEditor.prototype.isClassifierCell = function (cell) {
  if (cell == null) {
    return false;
  }
  return this.graph.isClassifier(cell.value);
};

/**
 * Check if the given cell 
 * @param cell
 * @returns {Boolean}
 */
CLASSEditor.prototype.isElementVertexCell = function (cell) {
  if (cell == null) {
    return false;
  }
  return this.graph.isElementVertex(cell.value);
};

/**
 * Determines if the given cell represents a connector UML.
 * 
 * @author Gabriel Leonardo Diaz, 04.02.2014.
 */
CLASSEditor.prototype.isRelationshipCell = function (cell) {
  if (cell == null) {
    return false;
  }
  return this.graph.isRelationship(cell.value);
};

/**
 * Overrides the addActions() function in mxEditor in order to add some custom
 * actions to the array.
 * 
 * @author Gabriel Leonardo Diaz, 12.01.2014.
 */
CLASSEditor.prototype.addActions = function () {
  // super.addActions();
  mxEditor.prototype.addActions.call(this);
  
  this.addAction("zoom25", function (editor) {
    editor.graph.zoomTo(25/100);
  });
  
  this.addAction("zoom50", function (editor) {
    editor.graph.zoomTo(50/100);
  });
  
  this.addAction("zoom75", function (editor) {
    editor.graph.zoomTo(75/100);
  });
  
  this.addAction("zoom100", function (editor) {
    editor.graph.zoomTo(100/100);
  });
  
  this.addAction("zoom150", function (editor) {
    editor.graph.zoomTo(150/100);
  });
  
  this.addAction("zoom200", function (editor) {
    editor.graph.zoomTo(200/100);
  });
  
  this.addAction("zoom400", function (editor) {
    editor.graph.zoomTo(400/100);
  });
  
  this.addAction("moveLeft", function (editor) {
    editor.moveCells(37);
  });
  
  this.addAction("moveUp", function (editor) {
    editor.moveCells(38);
  });
  
  this.addAction("moveRight", function (editor) {
    editor.moveCells(39);
  });
  
  this.addAction("moveDown", function (editor) {
    editor.moveCells(40);
  });
  
  this.addAction("showAttributes", function (editor) {
    editor.showAttributes(editor.graph.getSelectionCell());
  });
  
  this.addAction("showOperations", function (editor) {
    editor.showOperations(editor.graph.getSelectionCell());
  });
  
  this.addAction("generateCode", function (editor) {
    editor.generateCode();
  });
  
  this.addAction("showRelationship", function (editor) {
    editor.showRelationship(editor.graph.getSelectionCell());
  });
  
  this.addAction("exportXMI", function (editor) {
    editor.exportXMI();
  });
  
  this.addAction("viewXML", function (editor) {
    var encoder = new mxCodec();
    var node = encoder.encode(editor.graph.getModel());
    mxUtils.popup(mxUtils.getPrettyXml(node), true);
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
 * Shows the attributes of one classifier.
 * 
 * @param cell
 *          The cell containing the classifier XML representation.
 * @author Gabriel Leonardo Diaz, 16.01.2014.
 */
CLASSEditor.prototype.showAttributes = function (cell) {
  if (!this.attrDialog) {
    this.attrDialog = new CLASSAttributes(this);
  }
  this.attrDialog.init(cell);
  this.attrDialog.show();
};

/**
 * Shows the operations of one classifier.
 * 
 * @param cell
 *          The cell containing the classifier XML representation.
 * @author Gabriel Leonardo Diaz, 16.01.2014.
 */
CLASSEditor.prototype.showOperations = function (cell) {
  if (!this.operDialog) {
    this.operDialog = new CLASSOperations(this);
  }
  this.operDialog.init(cell);
  this.operDialog.show();
};

/**
 * Edits the UML connector contained into the given cell.
 * 
 * @param cell
 * @author Gabriel Leonardo Diaz, 04.02.2014.
 */
CLASSEditor.prototype.showRelationship = function (cell) {
  // TODO GD
};

/**
 * Exports the current diagram into an XML file with XMI standard format.
 * 
 * @author Gabriel Leonardo Diaz, 17.02.2014.
 */
CLASSEditor.prototype.exportXMI = function () {
  // TODO GD
};

