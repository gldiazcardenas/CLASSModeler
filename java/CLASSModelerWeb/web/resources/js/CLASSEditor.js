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
CLASSEditor = function (config, enabled) {
  this.enabled = enabled;
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
 * Instance of the dialog used to edit relationships.
 */
CLASSEditor.prototype.relationDialog;

/**
 * Flag that determines whether the editor should be read-only or not.
 */
CLASSEditor.prototype.enabled = true;

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
  graph.setAllowLoops(true);
  graph.setEnabled(this.enabled);

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
  
  // Handler to capture events after creating relationships
  graph.connectionHandler.addListener(mxEvent.CONNECT, mxUtils.bind(this, function(sender, evt) {
    if (this.graph.isAssociation(evt.properties.cell.value)) {
      //this.showRelationship(evt.properties.cell);
    }
  }));
  
  // Relationships validations
  graph.validateEdge = mxUtils.bind(this, function (edge, source, target) {
    return this.validateEdge(edge, source, target);
  });
  
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
            
            // GD, 25.10.2013. Applying layout to also child nodes
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
 * Function that validates the relationship to be created between the source and
 * target cells.
 * 
 * @param edge
 * @param source
 * @param target
 * @author Gabriel Leonardo Diaz, 04.03.2014.
 */
CLASSEditor.prototype.validateEdge = function (edge, source, target) {
  if (!this.enabled) {
    return "El diagrama se encuentra en modo solo lectura.";
  }
  
  edge = this.defaultEdge;
  
  if (this.graph.isPackage(source.value)) {
    edge = this.getTemplate("link");
    
    if (!this.graph.isClassifier(target.value)) {
      return "No se puede crear un vinculo entre estos elementos.";
    }
    
    if (target.getAttribute("package") != null) {
      return "Este elemento ya tiene un paquete.";
    }
  }
  
  if (this.graph.isPackage(target.value)) {
    edge = this.getTemplate("link");
    
    if (!this.graph.isClassifier(source.value)) {
      return "No se puede crear un vinculo entre estos elementos.";
    }
    
    if (source.getAttribute("package") != null) {
      return "Este elemento ya tiene un paquete.";
    }
  }
  
  if (this.graph.isRealization(edge.value) && (!this.graph.isClass(source.value) || !this.graph.isInterface(target.value))) {
    return "No se puede crear una Realizacion entre los elementos seleccionados.";
  }
  
  if (this.graph.isGeneralization(edge.value) && !((this.graph.isClass(source.value) && this.graph.isClass(target.value)) ||
                                                   (this.graph.isInterface(source.value) && this.graph.isInterface(target.value)))) {
    return "No se puede crear una Generalizacion entre los elementos seleccionados.";
  }
  
  if (this.graph.isAssociation(edge.value) && !this.graph.isClass(source.value)) {
    return "No se puede crear una Asociacion entre los elementos seleccionados.";
  }
  
  return null;
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
  var isSourcePackage = this.graph.isPackage(source.value);
  var isTargetPackage = this.graph.isPackage(target.value);
  
  // Save default edge
  var oldDefaultEdge = this.defaultEdge;
  if (this.graph.isComment(source.value) || this.graph.isComment(target.value) || isSourcePackage || isTargetPackage) {
    this.defaultEdge = this.getTemplate("link");
  }
  
  var model = this.graph.model;
  
  // super
  var edge = mxEditor.prototype.createEdge.call(this, arguments);
  
  // Checks if the source/target element is a package.
  if (isSourcePackage) {
    this.graph.cellEditProperty(target, "package", source.id, true);
  }
  else if (isTargetPackage) {
    this.graph.cellEditProperty(source, "package", target.id, true);
  }
  
  // Restore default edge
  this.defaultEdge = oldDefaultEdge;
  
  // Adjust the edge created
  if (this.graph.isRealization(edge.value)) {
    var operations = this.graph.getOperations(target);
    var operation;
    
    for (var i = 0; i < operations.length; i++) {
      operation       = this.graph.model.cloneCell(this.getTemplate("operation"));
      operation.value = operations[i].value.cloneNode(true);
      this.graph.addOperation(source, operation);
    }
  }
  else if (this.graph.isAggregation(edge.value)) {
    var targetProp = model.cloneCell(this.getTemplate("property"));
    targetProp.setAttribute("name", target.getAttribute("name").toLowerCase() + "s");
    targetProp.setAttribute("visibility", "private");
    targetProp.setAttribute("type", target.id);
    targetProp.setAttribute("collection", "ArrayList");
    targetProp.setAttribute("lower", "0");
    targetProp.setAttribute("upper", "*");
    this.graph.addAssociationAttribute(edge, targetProp, false, true);
  }
  else if (this.graph.isComposition(edge.value)) {
    var targetProp = model.cloneCell(this.getTemplate("property"));
    targetProp.setAttribute("name", target.getAttribute("name") + "s");
    targetProp.setAttribute("visibility", "private");
    targetProp.setAttribute("type", target.id);
    targetProp.setAttribute("collection", "ArrayList");
    targetProp.setAttribute("lower", "0");
    targetProp.setAttribute("upper", "*");
    this.graph.addAssociationAttribute(edge, targetProp, false, true);
  }
  else if (this.graph.isAssociation(edge.value)) {
    var targetProp = model.cloneCell(this.getTemplate("property"));
    targetProp.setAttribute("name", target.getAttribute("name").toLowerCase());
    targetProp.setAttribute("type", target.id);
    targetProp.setAttribute("upper", "1");
    this.graph.addAssociationAttribute(edge, targetProp, false, true);
  }
  
  return edge;
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
    pendingChangesLbl.innerHTML = "* ";
  }
  else {
    pendingChangesLbl.innerHTML = "";
  }
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
  
  menu.addItem("Deshacer", null, function () { self.execute("undo"); }, null, null, this.enabled);
  menu.addItem("Rehacer", null, function () { self.execute("redo"); }, null, null, this.enabled);
  
  menu.addSeparator();
  
  menu.addItem("Cortar", null, function () { self.execute("cut"); }, null, null, this.enabled && this.isClassifierCell(cell));
  menu.addItem("Copiar", null, function () { self.execute("copy"); }, null, null, this.enabled && this.isClassifierCell(cell));
  menu.addItem("Pegar", null, function () { self.execute("paste"); }, null, null, this.enabled);
  
  menu.addSeparator();
  
  menu.addItem("Eliminar", null, function () { self.execute("delete"); }, null, null, this.enabled && cell != null);
  menu.addItem("Seleccionar Todo", null, function () { self.execute("selectAll"); }, null, null, this.enabled);
  
  menu.addSeparator();
  
  var subMenu = menu.addItem("Orden Z");
  menu.addItem("Traer adelante", null, function () { self.execute("toFront"); }, subMenu, null, this.enabled && self.isElementVertexCell(cell));
  menu.addItem("Enviar atras", null, function () { self.execute("toBack"); }, subMenu, null, this.enabled && self.isElementVertexCell(cell));
  
  menu.addSeparator();
  var attributesName = self.isClassCell(cell) || self.isInterfaceCell(cell) || cell == null ? "Atributos" : "Literales";
  menu.addItem(attributesName, null, function () { self.execute("showAttributes"); }, null, null, this.enabled && self.isClassifierCell(cell));
  menu.addItem("Operaciones", null, function () { self.execute("showOperations"); }, null, null, this.enabled && (self.isClassCell(cell) || self.isInterfaceCell(cell)));
  menu.addItem("Editar Relacion", null, function () { self.execute("showRelationship"); }, null, null, this.enabled && self.isAssociationCell(cell));
  
  menu.addSeparator();
  
  var subMenu = menu.addItem("Herramientas");
  menu.addItem("Generar Codigo", null, function () { self.execute("generateCode"); }, subMenu, null, this.enabled);
  menu.addItem("Generar Imagen", null, function () { self.execute("exportImage"); }, subMenu, null, this.enabled);
  menu.addSeparator(subMenu);
  menu.addItem("Agregar Constructor", null, function () { self.execute("generateConstructor"); }, subMenu, null, this.enabled && self.isClassCell(cell));
  menu.addItem("Agregar Get/Set", null, function () { self.execute("generateGetSet"); }, subMenu, null, this.enabled && self.isPropertyCell(cell));
  menu.addSeparator(subMenu);
  menu.addItem("Mostrar XML", null, function () { self.execute("viewXML"); }, subMenu, null, this.enabled);
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
 * Determines if the user object (node) of the given cell is a Class UML.
 * @param cell
 * @returns
 */
CLASSEditor.prototype.isClassCell = function (cell) {
  if (cell == null) {
    return false;
  }
  return this.graph.isClass(cell.value);
};

/**
 * Determines if the user object (node) of the given cell is an Interface UML.
 * @param cell
 * @returns
 */
CLASSEditor.prototype.isInterfaceCell = function (cell) {
  if (cell == null) {
    return false;
  }
  return this.graph.isInterface(cell.value);
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
CLASSEditor.prototype.isAssociationCell = function (cell) {
  if (cell == null) {
    return false;
  }
  return this.graph.isAssociation(cell.value);
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
  
  this.addAction("generateConstructor", function (editor) {
    editor.generateConstructor(editor.graph.getSelectionCell());
  });
  
  this.addAction("generateGetSet", function (editor) {
    editor.generateGetSet(editor.graph.getSelectionCell());
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
  PrimeFaces.ab({ source:'userForm:generateCodeItem',
                  update:'generateCodeForm generalMessage', 
                  oncomplete: function (xhr, status, args) {
                    dlgGenerateCode.show();
                  },
                  formId:'userForm'});
};

/**
 * Shows the attributes of one classifier.
 * 
 * @param cell
 *          The cell containing the classifier XML representation.
 * @author Gabriel Leonardo Diaz, 16.01.2014.
 */
CLASSEditor.prototype.showAttributes = function (cell) {
  if (!this.isClassifierCell(cell)) {
    return;
  }
    
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
  if (!this.isClassCell(cell) && !this.isInterfaceCell(cell)) {
    return;
  }
  
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
  if (!this.isAssociationCell(cell)) {
    return;
  }
  
  if (!this.relationDialog) {
    this.relationDialog = new CLASSRelationship(this);
  }
  this.relationDialog.init(cell);
  this.relationDialog.show();
};

/**
 * Generates getter/setter method for the given property cell.
 * 
 * @author Gabriel Leonardo Diaz, 19.02.2014.
 */
CLASSEditor.prototype.generateGetSet = function (cell) {
  if (cell == null || cell.parent == null || !this.graph.isProperty(cell.value)) {
    return;
  }
  
  var name = cell.getAttribute("name");
  var upperName = name.charAt(0).toUpperCase() + name.substring(1);
  var type = cell.getAttribute("type");
  var collection = cell.getAttribute("collection");
  if (collection == null) {
    collection = "";
  }
  
  var get = this.graph.model.cloneCell(this.getTemplate("operation"));
  get.setAttribute("name", "get" + upperName);
  get.setAttribute("visibility", "public");
  get.setAttribute("type", type);
  get.setAttribute("collection", collection);
  
  var set = this.graph.model.cloneCell(this.getTemplate("operation"));
  set.setAttribute("name", "set" + upperName);
  set.setAttribute("visibility", "public");
  set.setAttribute("type", "void");
  
  var param = this.graph.model.cloneCell(this.getTemplate("parameter")).value;
  param.setAttribute("name", name);
  param.setAttribute("type", type);
  param.setAttribute("collection", collection);
  set.value.appendChild(param);
  
  var parentCell;
  if (this.graph.isAssociation(cell.parent.value)) {
    if (cell.getAttribute("type") == cell.parent.source.id) {
      parentCell = cell.parent.target;
    }
    else {
      parentCell = cell.parent.source;
    }
  }
  else {
    // Class or Interface
    parentCell = cell.parent.parent;
  }
  
  this.graph.addOperation(parentCell, get);
  this.graph.addOperation(parentCell, set);
};

/**
 * Generates default constructor for the given class cell.
 * 
 * @author Gabriel Leonardo Diaz, 19.02.2014.
 */
CLASSEditor.prototype.generateConstructor = function (cell) {
  if (cell == null || !this.graph.isClass(cell.value)) {
    return;
  }
  
  var constructor = this.graph.model.cloneCell(this.getTemplate("operation"));
  constructor.setAttribute("name", cell.getAttribute("name"));
  constructor.setAttribute("visibility", "public");
  this.graph.addOperation(cell, constructor);
};

