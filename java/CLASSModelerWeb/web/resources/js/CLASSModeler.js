/*******************************************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS 
 * Cúcuta, Colombia 
 * (c) 2013 by UFPS. All rights reserved.
 * 
 * @author: Gabriel Leonardo Diaz, 01.05.2013.
 * 
 ******************************************************************************/

/**
 * Singleton class that encapsulates the client side application for
 * CLASSModeler software.
 * 
 * @author Gabriel Leonardo Diaz, 26.10.2013.
 */
var CLASSModeler = (function () {
  
  var editor        = null;
  var outline       = null;
  var toolbox       = null;
  var properties    = null;
  
  function stopLoading () {
    // Fades-out the splash screen
    var loading = document.getElementById('loading');
    
    if (loading != null) {
      try {
        mxEvent.release(loading);
        mxEffects.fadeOut(loading, 100, true);
      }
      catch (e) {
        loading.parentNode.removeChild(loading);
      }
    }
  }
  
  return {
    init : function (enabled) {
      if (!mxClient.isBrowserSupported()) {
        mxUtils.error('Browser is not supported for mxGraph!', 200, false);
        return;
      }
      
      // Disables built-in context menu
      mxEvent.disableContextMenu(document.body);
      
      // Vertexes selection color
      mxConstants.HANDLE_FILLCOLOR = "#FC8D98";
      mxConstants.HANDLE_STROKECOLOR = "#E1061A";
      mxConstants.VERTEX_SELECTION_COLOR = "#E1061A";
      mxConstants.EDGE_SELECTION_COLOR = "#E1061A";
      
      editor = new CLASSEditor(mxUtils.load(mxBasePath + "/config/editor.xml").getDocumentElement(), enabled);
      editor.addListener(mxEvent.ROOT, stopLoading);
      
      toolbox = new CLASSToolbox(editor);
      toolbox.init(document.getElementById("toolbox"));
      
      outline = new mxOutline(editor.graph);
      outline.init(document.getElementById("outline"));
      outline.updateOnPan = true;
      
      properties = new CLASSPropertyGrid(editor);
      properties.init();
    },
    
    execute : function (actionName) {
      editor.execute(actionName);
    },
  };
  
})();

// GD, 13.01.2014 Workaround for IE.
if (!document.getElementsByClassName) {
  document.getElementsByClassName = function(className) {
      return this.querySelectorAll("." + className);
  };
  Element.prototype.getElementsByClassName = document.getElementsByClassName;
}

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
  
  // REALIZATION: Copy operations in the class
  if (this.graph.isRealization(edge.value)) {
    var operations = this.graph.getOperations(target);
    var operation;
    
    for (var i = 0; i < operations.length; i++) {
      operation       = this.graph.model.cloneCell(this.getTemplate("operation"));
      operation.value = operations[i].value.cloneNode(true);
      this.graph.addOperation(source, operation);
    }
  }
  
  // AGGREGATION, COMPOSITION, ASSOCIATION: Generate properties for source and target
  else if (this.graph.isAssociation(edge.value)) {
    var targetProp = model.cloneCell(this.getTemplate("property"));
    targetProp.setAttribute("type", target.id);
    targetProp.setAttribute("visibility", "private");
    
    if (this.graph.isAggregation(edge.value) || this.graph.isComposition(edge.value)) {
      targetProp.setAttribute("name", target.getAttribute("name").toLowerCase() + "Lista");
      targetProp.setAttribute("lower", "1");
      targetProp.setAttribute("upper", "*");
      targetProp.setAttribute("collection", "ArrayList");
    }
    else {
      targetProp.setAttribute("name", target.getAttribute("name").toLowerCase());
      targetProp.setAttribute("upper", "1");
    }
    
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
  
  if (modified && this.enabled) {
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
  menu.addItem("Generar Imagen", null, function () { self.execute("generateImage"); }, subMenu, null, this.enabled);
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
  
  this.addAction("generateImage", function (editor) {
    editor.generateImage();
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
    this.attrDialog = new CLASSAttributeDialog(this);
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
    this.operDialog = new CLASSOperationDialog(this);
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
    this.relationDialog = new CLASSRelationshipDialog(this);
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
  var uName = name.charAt(0).toUpperCase() + name.substring(1);
  var type = cell.getAttribute("type");
  var collection = cell.getAttribute("collection");
  if (collection == null) {
    collection = "";
  }
  
  var get = this.graph.model.cloneCell(this.getTemplate("operation"));
  get.setAttribute("name", "get" + uName);
  get.setAttribute("visibility", "public");
  get.setAttribute("type", type);
  get.setAttribute("collection", collection);
  
  var set = this.graph.model.cloneCell(this.getTemplate("operation"));
  set.setAttribute("name", "set" + uName);
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

/**
 * Generates an image representation of the current diagram.
 * 
 * @author Gabriel Leonardo Diaz, 30.03.2014.
 */
CLASSEditor.prototype.generateImage = function () {
  var scale = 1;
  var bounds = this.graph.getGraphBounds();
  
  // Creates XML node to hold output
  var xmlDoc = mxUtils.createXmlDocument();
  var root   = xmlDoc.createElement("output");
  xmlDoc.appendChild(root);
  
  // Creates interface for rendering output
  var xmlCanvas = new mxXmlCanvas2D(root);
  xmlCanvas.scale(scale);
  xmlCanvas.translate(Math.round(-bounds.x * scale), Math.round(-bounds.y * scale));
  
  // Renders output to interface
  var imgExport = new mxImageExport();
  imgExport.drawState(this.graph.getView().getState(this.graph.model.root), xmlCanvas);
  
  // Puts request data together
  var filename = 'export.png';
  var format = 'png';
  var bg = '#FFFFFF';
  var w = Math.round((bounds.width + 4) * scale);
  var h = Math.round((bounds.height + 4) * scale);
  var xml = mxUtils.getXml(root);
  
  // Compression is currently not used in this example
  // Requires base64.js and redeflate.js
  // xml = encodeURIComponent(Base64.encode(RawDeflate.deflate(xml), true));
  new mxXmlRequest(this.urlImage, 'filename=' + filename +
                                  '&format=' + format + 
                                  '&bg=' + bg + 
                                  '&w=' + w + 
                                  '&h=' + h + 
                                  '&xml=' + encodeURIComponent(xml)).simulate(document, '_blank');
};

/**
 * Constructor that instances a graph component.
 * @param container
 * @param model
 * @param renderHint
 * @param stylesheet
 * @returns {CLASSGraph}
 */
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
    if (isNavigable) {
      label += " : ";
    }
    label += this.convertMultiplicity(property.getAttribute("lower"), property.getAttribute("upper"));
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
  jSonData.push({id:"Date",    text:"Date"});
  jSonData.push({id:"Time",    text:"Time"});
  
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
  jSonData.push({id: "0..*",  text: "0..*"});
  jSonData.push({id: "0..1",  text: "0..1"});
  jSonData.push({id: "1",     text: "1"});
  jSonData.push({id: "1..*",  text: "1..*"});
  
  return jSonData;
};

/**
 * Gets the multiplicity value for the given lower and upper values.
 * 
 * @param lower
 * @param upper
 */
CLASSGraph.prototype.convertMultiplicity = function (lower, upper) {
  var multiplicity = "";
  
  if (lower) {
    multiplicity += lower;
    
    if (upper) {
      multiplicity += ".." + upper;
    }
  }
  else if (upper) {
    multiplicity += upper;
  }
  
  return multiplicity;
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

/**
 * Defines the shape for UML package element.
 * 
 * @author Gabriel Leonardo Diaz, 01.05.2013.
 */
ShapePackage = function () {};
ShapePackage.prototype = new mxCylinder();
ShapePackage.prototype.constructor = ShapePackage;
ShapePackage.prototype.tabPosition = 'left';
ShapePackage.prototype.redrawPath  = function (path, x, y, w, h, isForeground) {
  
  var tp = mxUtils.getValue(this.style, 'tabPosition', this.tabPosition);
  var tw = w * 0.50;
  var th = h * 0.20;
  
  if (th > 20) {
    th = 20; // Not more than 20px.
  }
  
  if (tw > 80) {
    tw = 80; // Not more than 80px.
  }
  
  var dx = Math.min(w, tw);
  var dy = Math.min(h, th);

  if (isForeground) {
    if (tp == 'left') {
      path.moveTo(0, dy);
      path.lineTo(dx, dy);
    }
    // Right is default
    else {
      path.moveTo(w - dx, dy);
      path.lineTo(w, dy);
    }
    
    path.end();
  }
  else {
    if (tp == 'left') {
      path.moveTo(0, 0);
      path.lineTo(dx, 0);
      path.lineTo(dx, dy);
      path.lineTo(w, dy);
    }
    // Right is default
    else {
      path.moveTo(0, dy);
      path.lineTo(w - dx, dy);
      path.lineTo(w - dx, 0);
      path.lineTo(w, 0);
    }
    
    path.lineTo(w, h);
    path.lineTo(0, h);
    path.lineTo(0, dy);
    path.close();
    path.end();
  }
};
mxCellRenderer.prototype.defaultShapes['package'] = ShapePackage;

/**
 * Defines the shape for a common UML comment appended to any element.
 * 
 * @returns {ShapeNote} The ShapeNote created.
 * @author Gabriel Leonardo Diaz, 16.06.2013.
 */
ShapeComment = function () {};
ShapeComment.prototype = new mxCylinder();
ShapeComment.prototype.constructor = ShapeComment;
ShapeComment.prototype.redrawPath  = function (path, x, y, w, h, isForeground) {
  
  var s = w * 0.20;
  if (s > 10) {
    s = 10; // No more than 10px.
  }
  
  this.size = s;
  
  if (isForeground) {
    path.moveTo(w - s, 0);
    path.lineTo(w - s, s);
    path.lineTo(w, s);
    path.end();
  }
  else {
    path.moveTo(0, 0);
    path.lineTo(w - s, 0);
    path.lineTo(w, s);
    path.lineTo(w, h);
    path.lineTo(0, h);
    path.lineTo(0, 0);
    path.close();
    path.end();
  }
};
mxCellRenderer.prototype.defaultShapes['comment'] = ShapeComment;

/**
 * JavaScript CLASS that defines the designer toolBox.
 */
CLASSToolbox = function (editor) {
  this.editor = editor;
  this.graph  = editor.graph;
};

/**
 * The instance of mxGraph component.
 */
CLASSToolbox.prototype.graph;


/**
 * The CSS style for selected toolBox items.
 */
CLASSToolbox.prototype.selectedStyle = "selected";

/**
 * The CSS style for all toolBox items.
 */
CLASSToolbox.prototype.toolboxItemStyle = "toolbox_item";

/**
 * The CSS style for all draggable items in the toolBox.
 */
CLASSToolbox.prototype.draggableItemStyle = "draggable";

/**
 * Map that stores the quantity of instances created by object kind. Allows to adjust the 
 */
CLASSToolbox.prototype.instanceQuantity = {};

/**
 * Initializer method for the CLASS Modeler toolBox.
 */
CLASSToolbox.prototype.init = function (container) {
  this.container = container;
  
  // Configure drag object
  var draggableItems = document.getElementsByClassName(this.draggableItemStyle);
  for (var i = 0; i < draggableItems.length; i++) {
    this.configureDnD(draggableItems[i], this.editor.getTemplate(draggableItems[i].id));
  }
  
  // Configure selection
  var toolboxItems = document.getElementsByClassName(this.toolboxItemStyle);
  for (var i = 0; i < toolboxItems.length; i++) {
    this.configureSelection(toolboxItems[i]);
  }
  
  // Initialize the instances quantity
  this.instanceQuantity["class"] = 0;
  this.instanceQuantity["interface"] = 0;
  this.instanceQuantity["enumeration"] = 0;
  this.instanceQuantity["package"] = 0;
};

/**
 * Add a cell template to the ToolBox container element.
 * 
 * @param template
 *          The cell template.
 * @param imageURL
 *          The image used to identify the template
 * @param name
 *          The name of the template
 * @param tooltip
 *          The tooltip of the template
 * @author Gabriel Leonardo Diaz, 17.10.2013.
 */
CLASSToolbox.prototype.configureDnD = function (draggableItem, element) {
  var self = this;
  
  // Drop handler
  var doDrop = function (graph, evt, overCell) {
    if (!graph.isEnabled()) {
      return;
    }
    
    graph.stopEditing(true);
    
    var model = graph.getModel();
    model.beginUpdate();
    
    try {
      var point   = graph.getPointForEvent(evt);
      var parent  = graph.getDefaultParent();
      
      var newCell = model.cloneCell(element);
      
      newCell.geometry.x = point.x;
      newCell.geometry.y = point.y;
      
      // CLASS sections
      if (graph.isClass(newCell.value)) {
        var attrSection = model.cloneCell(self.editor.getTemplate("section"));
        attrSection.setAttribute("attribute", "1");
        attrSection.setVertex(true);
        
        var operSection = model.cloneCell(self.editor.getTemplate("section"));
        operSection.setAttribute("attribute", "0");
        operSection.setVertex(true);
        
        newCell.insert(attrSection);
        newCell.insert(operSection);
        
        // Adjust the name
        var nameNumber = self.instanceQuantity["class"] + 1;
        var uniqueName = graph.getUniqueName(newCell.getAttribute("name") + nameNumber);
        newCell.setAttribute("name", uniqueName);
        self.instanceQuantity["class"] = nameNumber;
      }
      
      // INTERFACE sections
      else if (graph.isInterface(newCell.value)) {
        var attrSection = model.cloneCell(self.editor.getTemplate("section"));
        attrSection.setAttribute("attribute", "0");
        attrSection.setVertex(true);
        
        newCell.insert(attrSection);
        
        // Adjust the name
        var nameNumber = self.instanceQuantity["interface"] + 1;
        var uniqueName = graph.getUniqueName(newCell.getAttribute("name") + nameNumber);
        newCell.setAttribute("name", uniqueName);
        self.instanceQuantity["interface"] = nameNumber;
      }
      
      // ENUMERATION sections
      else if (graph.isEnumeration(newCell.value)) {
        var attrSection = model.cloneCell(self.editor.getTemplate("section"));
        attrSection.setAttribute("attribute", "1");
        attrSection.setVertex(true);
        
        newCell.insert(attrSection);
        
        // Adjust the name
        var nameNumber = self.instanceQuantity["enumeration"] + 1;
        var uniqueName = graph.getUniqueName(newCell.getAttribute("name") + nameNumber);
        newCell.setAttribute("name", uniqueName);
        self.instanceQuantity["enumeration"] = nameNumber;
      }
      else if (graph.isPackage(newCell.value)) {
        // Adjust the name
        var nameNumber = self.instanceQuantity["package"] + 1;
        newCell.setAttribute("name", newCell.getAttribute("name") + nameNumber);
        self.instanceQuantity["package"] = nameNumber;
      }
      
      graph.addCell(newCell, parent);
      graph.cellSizeUpdated(newCell, false);
      graph.adjustStyleCell(newCell);
      graph.setSelectionCell(newCell);
    }
    finally {
      model.endUpdate();
    }
  };
  
  // Drag handler
  var dragSource = mxUtils.makeDraggable(draggableItem, this.graph, doDrop, draggableItem.cloneNode(true));
  dragSource.highlightDropTargets = true;
  dragSource.getDropTarget = function (graph, x, y) {
    if (graph.isSwimlane(element)) {
      return null;
    }
    
    var cell = graph.getCellAt(x, y);
    
    if (graph.isSwimlane(cell)) {
      return cell;
    }
    
    var parent = graph.getModel().getParent(cell);
    
    if (graph.isSwimlane(parent)) {
      return parent;
    }
  };
};

/**
 * Configures the selection listener of the toolBox items.
 * 
 * @param toolboxItem
 *          The toolBox item to configure.
 * @author Gabriel Leonardo Diaz, 30.11.2013.
 */
CLASSToolbox.prototype.configureSelection = function (toolboxItem) {
  var self= this;
  toolboxItem.onclick = function () {
    if (self.isSelected(toolboxItem)) {
      self.removeSelection(toolboxItem);
    }
    else {
      self.addSelection(toolboxItem);
    }
  };
};

/**
 * Allows to mark the given toolbox item as selected. This removes the selection from all other
 * items.
 * 
 * @param toolboxItem
 *          The item to be selected.
 * @author Gabriel Leonardo Diaz, 01.12.2013.
 */
CLASSToolbox.prototype.addSelection = function (toolboxItem) {
  var toolboxItems = document.getElementsByClassName(this.toolboxItemStyle);
  for (var i = 0; i < toolboxItems.length; i++) {
    if (toolboxItem != toolboxItems[i]) {
      this.removeSelection (toolboxItems[i]);
    }
  }
  
  toolboxItem.className = toolboxItem.className + " " + this.selectedStyle;
  var cellTemplate = this.editor.getTemplate(toolboxItem.id);
  if (cellTemplate && cellTemplate.isEdge()) {
    this.editor.defaultEdge = cellTemplate;
  }
};

/**
 * Allows to remove the selection on the given item
 * 
 * @param toolboxItem
 *          The item to be unselected.
 * @author Gabriel Leonardo Diaz, 01.12.2013.
 */
CLASSToolbox.prototype.removeSelection = function (toolboxItem) {
  toolboxItem.className = toolboxItem.className.replace(" " + this.selectedStyle, "");
  this.editor.defaultEdge = this.editor.getTemplate("association");
};

/**
 * Allows to know whether the given item is selected or not.
 * 
 * @param toolboxItem
 *          The item to be evaluated.
 * @author Gabriel Leonardo Diaz, 01.12.2013.
 */
CLASSToolbox.prototype.isSelected = function (toolboxItem) {
  return toolboxItem.className != null && toolboxItem.className.indexOf(this.selectedStyle) != -1;
};

CLASSPropertyGrid = function (editor) {
  this.editor = editor;
  this.graph  = editor.graph;
};

/**
 * Instance of the editor component (CLASSEditor).
 */
CLASSPropertyGrid.prototype.editor;

/**
 * Instance of the mxGraph cell being edited.
 */
CLASSPropertyGrid.prototype.cell;

/**
 * Initializes the property grid component.
 * 
 * @author Gabriel Leonardo Diaz, 14.01.2014.
 */
CLASSPropertyGrid.prototype.init = function () {
  var self = this;
  
  this.graph.getSelectionModel().addListener(mxEvent.CHANGE, function(sender, evt) {
    self.selectionChanged(sender, evt);
  });
  
  this.clearProperties();
};

/**
 * Process the selection changed event of the Graph Model.
 * 
 * @param sender
 *          The object sender.
 * @param evt
 *          The event.
 * @author Gabriel Leonardo Diaz, 14.01.2014.
 */
CLASSPropertyGrid.prototype.selectionChanged = function (sender, evt) {
  var cells = this.graph.getSelectionCells();
  
  if (cells.length == 1) {
    this.configureProperties(cells[0]);
  }
  else {
    this.clearProperties();
  }
};

/**
 * Clears the property grid and removes the edited element.
 * 
 * @author Gabriel Leonardo Diaz, 14.01.2014.
 */
CLASSPropertyGrid.prototype.clearProperties = function () {
  this.configureProperties(null);
};

/**
 * Clears the Property Grid by setting empty values and removing the editor.
 * 
 * @param cell
 *          The cell to be edited. If the cell is null the grid is cleared.
 * @author Gabriel Leonardo Diaz, 14.01.2014.
 */
CLASSPropertyGrid.prototype.configureProperties = function (cell) {
  var self               = this;
  var selfEditor         = this.editor;
  
  var nameValue          = null;
  var visibilityValue    = null;
  var packageValue       = null;
  var abstractValue      = null;
  var staticValue        = null;
  var finalValue         = null;
  
  var nameEditor         = null;
  var abstractEditor     = null;
  var staticEditor       = null;
  var finalEditor        = null;
  var visibilityEditor   = null;
  var attributesEditor   = null;
  var operationsEditor   = null;
  var relationshipEditor = null;
  var attributesName     = "Atributos";
  
  if (this.isCellEditable(cell)) {
    this.cell       = cell;
    var node        = cell.value;
    
    if (this.graph.isNamedElement(node)) {
      nameValue  = node.getAttribute("name");
      nameEditor = "text";
    }
    
    if (this.graph.isClass(node) || this.graph.isFeature(node)) {
      staticValue  = this.convertBooleanToString(node.getAttribute("static"));
      finalValue  = this.convertBooleanToString(node.getAttribute("final"));
      
      if (cell.parent == null || cell.parent.parent == null || !this.graph.isInterface(cell.parent.parent.value)) {
        staticEditor = {"type":"checkbox", "options": {"on":"Si", "off":"No"}};
        finalEditor = {"type":"checkbox", "options": {"on":"Si", "off":"No"}};
      }
    }
    
    if (this.graph.isClassifier(node) || this.graph.isFeature(node)) {
      visibilityValue  = node.getAttribute("visibility");
      visibilityEditor = {"type":"combobox", "options": {
        "valueField":"id",
        "textField":"text",
        "panelHeight":"90",
        "data": this.graph.getVisibilityJSon()
      }};
      
      if (this.graph.isClassifier(node)) {
        attributesEditor = {"type": "button", "options": {"onclick": function () {
          selfEditor.showAttributes(cell);
        }}};
        
        if (this.graph.isClass(node) || this.graph.isInterface(node)) {
          operationsEditor = {"type":"button", "options": {"onclick": function () {
            selfEditor.showOperations(cell);
          }}};
        }
        
        if (this.graph.isEnumeration(node)) {
          attributesName = "Literales";
        }
        
        packageValue = node.getAttribute("package");
        if (packageValue != null && packageValue.length > 0) {
          var packageCell = this.graph.model.getCell(packageValue);
          if (packageCell != null) {
            packageValue = packageCell.getAttribute("name");
          }
        }
      }
    }
    
    if (this.graph.isClass(node) || this.graph.isOperation(node)) {
      abstractValue = this.convertBooleanToString(node.getAttribute("abstract"));
      abstractEditor = {"type":"checkbox", "options": {"on":"Si", "off":"No"}};
    }
    
    if (this.graph.isAssociation(node)) {
      relationshipEditor = {"type":"button", "options": {"onclick": function () {
        selfEditor.showRelationship(cell);
      }}};
    }
  }
  else {
    this.cell = null;
  }
  
  var jSonData = [
      // GENERAL
      {"name":"Nombre", "value":nameValue, "group":"General", "editor":nameEditor},
      {"name":"Visibilidad", "value":visibilityValue, "group":"General", "editor":visibilityEditor},
      {"name":"Paquete", "value":packageValue, "group":"General", "editor":null},
      
      // ADVANCED
      {"name":"Es Abstracto", "value":abstractValue, "group":"Avanzado", "editor":abstractEditor},
      {"name":"Es Estatico", "value":staticValue, "group":"Avanzado", "editor":staticEditor},
      {"name":"Es Final", "value":finalValue, "group":"Avanzado", "editor":finalEditor},
      
      // VIEW
      {"name":attributesName, "value":null, "group":"Ver", "editor":attributesEditor},
      {"name":"Operaciones", "value":null, "group":"Ver", "editor":operationsEditor},
      {"name":"Relaci&oacute;n", "value":null, "group":"Ver", "editor":relationshipEditor}
  ];
  
  $("#propertyTable").propertygrid({
      data: jSonData,
      showGroup: true,
      showHeader: false,
      scrollbarSize: 0,
      onAfterEdit: function (rowIndex, rowData, changes) {
        self.processChanges(rowIndex, rowData, changes);
      },
      rowStyler: function (idx, row) {
        if (row.editor == null) {
          return "color: #CCCCCC;";
        }
        return "color: #000000;";
      }
  });
};

/**
 * Converts the boolean to string.
 * @param booleanValue
 * @returns
 */
CLASSPropertyGrid.prototype.convertBooleanToString = function (booleanValue) {
  if (booleanValue != null) {
    return booleanValue == "1" ? "Si" : "No";
  }
  return "";
};

/**
 * Convert the string to boolean.
 * @param stringValue
 * @returns
 */
CLASSPropertyGrid.prototype.convertStringToBoolean = function (stringValue) {
  return stringValue == "Si" ? "1" : "0";
};

/**
 * Checks if the given cell is editable.
 * @param cell
 * @returns {Boolean}
 */
CLASSPropertyGrid.prototype.isCellEditable = function (cell) {
  return cell != null && cell.value != null;
};

/**
 * Processes the changes after editing a property in the table.
 * 
 * @param rowIndex
 *          The index of the row edited.
 * @param rowData
 *          The initial data of the row.
 * @param changes
 *          The changes made.
 * @author Gabriel Leonardo Diaz, 18.01.2014.
 */
CLASSPropertyGrid.prototype.processChanges = function (rowIndex, rowData, changes) {
  if (changes.value != null) {
    switch (rowIndex) {
    case 0: // Name
      this.graph.cellEditProperty(this.cell, "name", changes.value, true);
      break;
      
    case 1: // Visibility
      this.graph.cellEditProperty(this.cell, "visibility", changes.value, true);
      break;
    
    case 3: // Abstract
      this.graph.cellEditProperty(this.cell, "abstract", this.convertStringToBoolean(changes.value), true);
      break;
      
    case 4: // Static
      this.graph.cellEditProperty(this.cell, "static", this.convertStringToBoolean(changes.value), true);
      break;
      
    case 5: // Final
      this.graph.cellEditProperty(this.cell, "final", this.convertStringToBoolean(changes.value), true);
      break;
    }
  }
};

/**
 * Defining a button editor for the property sheet.
 * 
 * @author Gabriel Leonardo Diaz, 18.01.2014.
 */
$.extend($.fn.datagrid.defaults.editors, {
  button: {
      init: function (container, options) {
        var input = $("<button type='button'>...</button>").appendTo(container);
        input[0].onclick = options.onclick;
        return input;
      },
      
      destroy: function (target) {
        $(target).remove();
      },
      
      getValue: function (target) {
        return $(target).val();
      },
      
      setValue: function (target, value) {
        $(target).val(value);
      },
      
      resize: function (target, width) {
        $(target)._outerWidth(width);
      }
  }
});

/**
 * Dialog that encapsulates the edition of the classifier's attributes.
 * 
 * @author Gabriel Leonardo Diaz, 16.01.2014.
 */
CLASSAttributeDialog = function (editor) {
  this.editor = editor;
  this.graph  = editor.graph;
  this.dialog = dlgAttributes; // Defined by a PrimeFaces dialog.
  this.title  = dlgAttributes.titlebar.children("span.ui-dialog-title").html();
  
  this.configureVisibilityCombo();
  this.configureCollectionsCombo();
  this.configureTypeCombo();
  this.configureAttributesTable();
};

/**
 * Instance of the editor component.
 */
CLASSAttributeDialog.prototype.editor;

/**
 * Instance of the graph component.
 */
CLASSAttributeDialog.prototype.graph;

/**
 * Instance of the dialog to handle.
 */
CLASSAttributeDialog.prototype.dialog;

/**
 * Instance of the classifier edited.
 */
CLASSAttributeDialog.prototype.classifierCell;

/**
 * Instance of the attribute edited.
 */
CLASSAttributeDialog.prototype.attributeCell;

/**
 * Index on the table of the attribute being edited.
 */
CLASSAttributeDialog.prototype.attributeIndex;

/**
 * The localized title text.
 */
CLASSAttributeDialog.prototype.title;

/**
 * Initializes the dialog with the attributes of the given cell containing a
 * classifier XML node.
 * 
 * @param cell
 *          The cell containing the classifier to be edited.
 * @author Gabriel Leonardo Diaz, 16.01.2014.
 */
CLASSAttributeDialog.prototype.init = function (cell) {
  this.classifierCell = cell;
  this.attributeCell  = null;
  this.attributeIndex = null;
  
  this.configureGUI();
  this.loadTableData();
  this.loadTypesData();
  this.clearSelection();
  this.clearFields();
  this.setTitle();
};

/**
 * Configures the special GUI component behavior depending on the classifier
 * cell being edited.
 * 
 * @author Gabriel Leonardo Diaz, 03.03.2014.
 */
CLASSAttributeDialog.prototype.configureGUI = function () {
  if (this.graph.isEnumeration(this.classifierCell.value)) {
    $("#staticCheck").attr("disabled", true);
    $("#finalCheck").attr("disabled", true);
    $("#attrType").combobox("disable");
    $("#attrVisibility").combobox("disable");
    $("#attrCollection").combobox("disable");
    $("#attrInitValue").attr("disabled", true);
  }
  else if (this.graph.isInterface(this.classifierCell.value)) {
    $("#staticCheck").attr("disabled", true);
    $("#finalCheck").attr("disabled", true);
    $("#attrType").combobox("enable");
    $("#attrVisibility").combobox("enable");
    $("#attrCollection").combobox("enable");
    $("#attrInitValue").removeAttr("disabled");
  }
  else {
    $("#staticCheck").removeAttr("disabled");
    $("#finalCheck").removeAttr("disabled");
    $("#attrType").combobox("enable");
    $("#attrVisibility").combobox("enable");
    $("#attrCollection").combobox("enable");
    $("#attrInitValue").removeAttr("disabled");
  }
};

/**
 * Sets the title of the dialog by appending the name of the classifier being
 * edited.
 * 
 * @author Gabriel Leonardo Diaz, 10.02.2014.
 */
CLASSAttributeDialog.prototype.setTitle = function () {
  this.dialog.titlebar.children("span.ui-dialog-title").html(this.title.replace("{0}", this.classifierCell.getAttribute("name")));
};

/**
 * Populates and sets up the combo box component for type edition.
 * 
 * @author Gabriel Leonardo Diaz, 17.01.2014.
 */
CLASSAttributeDialog.prototype.configureTypeCombo = function () {
  $("#attrType").combobox({
      valueField:"id",
      textField:"text",
      panelHeight: 200,
      data: this.graph.getTypesJSon()
  });
  
  $("#attrType").combobox("setValue", "int"); // default value
  
  // Workaround: The panel is shown behind of the PrimeFaces modal dialog.
  var comboPanel = $("#attrType").combobox("panel");
  comboPanel.panel("panel").css("z-index", "2000");
};

/**
 * Populates and sets up the combo box component for visibility edition.
 * 
 * @author Gabriel Leonardo Diaz, 17.01.2014.
 */
CLASSAttributeDialog.prototype.configureVisibilityCombo = function () {
  $("#attrVisibility").combobox({
      valueField:"id",
      textField:"text",
      panelHeight: 90,
      data: this.graph.getVisibilityJSon()
  });
  
  $("#attrVisibility").combobox("setValue", "private"); // default value
  
  // Workaround: The panel is shown behind of the PrimeFaces modal dialog.
  var comboPanel = $("#attrVisibility").combobox("panel");
  comboPanel.panel("panel").css("z-index", "2000");
};

/**
 * Populates and sets up the combo box component for Collection selection.
 * 
 * @author Gabriel Leonardo Diaz, 03.03.2014.
 */
CLASSAttributeDialog.prototype.configureCollectionsCombo = function () {
  $("#attrCollection").combobox({
      valueField:"id",
      textField:"text",
      panelHeight: 90,
      data: this.graph.getCollectionsJSon()
  });
  
  $("#attrCollection").combobox("setValue", ""); // default value
  
  // Workaround: The panel is shown behind of the PrimeFaces modal dialog.
  var comboPanel = $("#attrCollection").combobox("panel");
  comboPanel.panel("panel").css("z-index", "2000");
};

/**
 * Populates the attributes table component.
 * 
 * @author Gabriel Leonardo Diaz, 17.01.2014.
 */
CLASSAttributeDialog.prototype.configureAttributesTable = function () {
  var self = this;
  
  $("#attributesTable").datagrid({
      toolbar: "#attrTbToolbar",
      singleSelect: true,
      
      onSelect: function (rowIndex, rowData) {
        self.selectionChanged(rowIndex, true);
      },
      
      onUnselectAll: function (rows) {
        self.selectionChanged(rows[0], false);
      },
      
      onUnselect: function (rowIndex, rowData) {
        self.selectionChanged(rowIndex, false);
      },
      
      columns:[[
          {field:"name",  title:"Nombre",        width:150},
          {field:"type",  title:"Tipo",          width:150},
          {field:"value", title:"Valor Inicial", width:150}
      ]]
  });
  
  $(function() {
      $("#newAttributeBtn").bind("click", function() {
          self.newAttribute();
      });
  });
  
  $(function() {
      $("#delAttributeBtn").bind("click", function() {
          self.deleteAttribute();
      });
  });
  
  $(function() {
      $("#saveAttributeBtn").bind("click", function() {
          self.saveAttribute();
      });
  });
};

/**
 * Loads the types data once again.
 */
CLASSAttributeDialog.prototype.loadTypesData = function () {
  $("#attrType").combobox({ data: this.graph.getTypesJSon() });
};

/**
 * Loads the attributes of the edited classifier and fills the table data.
 * @author Gabriel Leonardo Diaz, 25.01.2014.
 */
CLASSAttributeDialog.prototype.loadTableData = function () {
  var jSonData = [];
  
  var attributes = this.graph.getAttributes(this.classifierCell);
  
  if (attributes) {
    for (var i = 0; i < attributes.length; i++) {
      var visibility   = attributes[i].getAttribute("visibility");
      var nameValue    = attributes[i].getAttribute("name");
      var typeValue    = this.graph.escape(this.graph.convertTypeToString(attributes[i]));
      var initialValue = this.graph.escape(attributes[i].getAttribute("initialValue"));
      
      jSonData.push({ name: this.graph.getVisibilityChar(visibility) + " " + nameValue, type: typeValue, value: initialValue });
    }
  }
  
  $("#attributesTable").datagrid({ data : jSonData });
  $("#attributesTable").datagrid("reload");
};

/**
 * Processes the selection changed event.
 * 
 * @param rowIndex
 *          The row selected or unselected.
 * @param selected
 *          A flag indicating the event type (selection or unselection).
 * @author Gabriel Leonardo Diaz, 24.01.2014.
 */
CLASSAttributeDialog.prototype.selectionChanged = function (rowIndex, selected) {
  this.clearFields();
  
  if (selected) {
    this.attributeCell  = this.graph.getAttributes(this.classifierCell)[rowIndex];
    this.attributeIndex = rowIndex;
    
    $("#attrName").val(this.attributeCell.getAttribute("name"));
    $("#attrType").combobox("setValue", this.attributeCell.getAttribute("type"));
    $("#attrCollection").combobox("setValue", this.attributeCell.getAttribute("collection"));
    $("#attrVisibility").combobox("setValue", this.attributeCell.getAttribute("visibility"));
    $("#attrInitValue").val(this.attributeCell.getAttribute("initialValue"));
    $("#staticCheck").prop("checked", this.attributeCell.getAttribute("static") == "1");
    $("#finalCheck").prop("checked", this.attributeCell.getAttribute("final") == "1");
  }
};

/**
 * Adds a new attribute to the edited classifier.
 * 
 * @author Gabriel Leonardo Diaz, 18.01.2014.
 */
CLASSAttributeDialog.prototype.newAttribute = function () {
  this.clearSelection();
  this.clearFields();
  $("#attrName" ).focus();
};

/**
 * Deletes the selected attribute over the table.
 * 
 * @author Gabriel Leonardo Diaz, 18.01.2014.
 */
CLASSAttributeDialog.prototype.deleteAttribute = function () {
  if (this.attributeCell) {
    this.graph.removeCells([this.attributeCell]);
    $("#attributesTable").datagrid("deleteRow", this.attributeIndex);
    this.clearSelection();
  }
};

/**
 * Saves the changes over the edited attribute.
 * 
 * @author Gabriel Leonardo Diaz, 18.01.2014.
 */
CLASSAttributeDialog.prototype.saveAttribute = function () {
  var nameValue       = $("#attrName").val();
  var typeValue       = $("#attrType").combobox("getValue");
  var visibilityValue = $("#attrVisibility").combobox("getValue");
  var initialValue    = $("#attrInitValue").val();
  var staticValue     = $("#staticCheck").is(":checked") ? "1" : "0";
  var finalValue      = $("#finalCheck").is(":checked") ? "1" : "0";
  var collectionValue = $("#attrCollection").combobox("getValue");
  var isEnum          = this.graph.isEnumeration(this.classifierCell.value);
  
  if (nameValue == null || nameValue.length == 0) {
    // Invalid Name
    return;
  }
  
  if (!isEnum) {
    if (typeValue == null || typeValue.length == 0) {
      // Invalid Type
      return;
    }
    
    if (visibilityValue == null || visibilityValue.length == 0) {
      // Invalid Visibility
      return;
    }
  }
  
  if (this.graph.isInterface(this.classifierCell.value)) {
    if (staticValue == "0") {
      // Invalid static
      return;
    }
    
    if (finalValue == "0") {
      // Invalid final
      return;
    }
    
    if (initialValue == null || initialValue.length == 0) {
      // Invalid constant for interface
      return;
    }
  }
  
  // Prepare attribute
  var attribute;
  if (this.attributeCell) {
    attribute = this.attributeCell.clone(true);
  }
  else {
    if (isEnum) {
      attribute = this.graph.model.cloneCell(this.editor.getTemplate("literal"));
    }
    else {
      attribute = this.graph.model.cloneCell(this.editor.getTemplate("property"));
    }
  }
  
  // Set values
  attribute.setAttribute("name", nameValue);
  
  if (this.graph.isProperty(attribute.value)) {
    attribute.setAttribute("type", typeValue);
    attribute.setAttribute("visibility", visibilityValue);
    attribute.setAttribute("static", staticValue);
    attribute.setAttribute("final", finalValue);
    attribute.setAttribute("initialValue", initialValue);
    attribute.setAttribute("collection", collectionValue);
  }
  
  var attrName = this.graph.getVisibilityChar(attribute.getAttribute("visibility")) + " " + attribute.getAttribute("name");
  var attrType = this.graph.escape(this.graph.convertTypeToString(attribute));
  var attrVal  = this.graph.escape(attribute.getAttribute("initialValue"));
  
  // Apply changes
  if (this.attributeCell) {
    this.graph.editAttribute(this.attributeCell, attribute);
    $("#attributesTable").datagrid("updateRow", {index: this.attributeIndex, row: { name: attrName, type: attrType, value: attrVal }});
  }
  else {
    this.graph.addAttribute (this.classifierCell, attribute);
    $("#attributesTable").datagrid("insertRow", {row: { name: attrName, type: attrType, value: attrVal }});
    $("#attributesTable").datagrid("selectRow", $("#attributesTable").datagrid("getRows").length - 1);
  }
  
  $("#attributesTable").datagrid("reload");
};

/**
 * Clears the fields to create/edit attributes.
 * 
 * @author Gabriel Leonardo Diaz, 25.01.2014.
 */
CLASSAttributeDialog.prototype.clearFields = function () {
  $("#attrName").val("");
  
  if (this.graph.isEnumeration(this.classifierCell.value)) {
    $("#attrType").combobox("clear");
    $("#attrVisibility").combobox("clear");
    $("#attrCollection").combobox("clear");
  }
  else {
    $("#attrType").combobox("setValue", "int");
    $("#attrVisibility").combobox("setValue", "private");
    $("#attrCollection").combobox("setValue", "");
  }
 
  $("#attrInitValue").val("");
  $("#staticCheck").prop("checked", this.graph.isInterface(this.classifierCell.value));
  $("#finalCheck").prop("checked", this.graph.isInterface(this.classifierCell.value));
};

/**
 * Clears the selection on the attributes table.
 * 
 * @author Gabriel Leonardo Diaz, 25.01.2014.
 */
CLASSAttributeDialog.prototype.clearSelection = function () {
  $("#attributesTable").datagrid("unselectAll");
  this.attributeIndex = null;
  this.attributeCell  = null;
};

/**
 * Shows up the dialog to the user.
 * 
 * @author Gabriel Leonardo Diaz, 16.01.2014.
 */
CLASSAttributeDialog.prototype.show = function () {
  this.dialog.show();
};

/**
 * Dialog that encapsulates the edition of the classifier's operations.
 * 
 * @author Gabriel Leonardo Diaz, 26.01.2014.
 */
CLASSOperationDialog = function (editor) {
  this.editor = editor;
  this.graph  = editor.graph;
  this.dialog = dlgOperations; // Defined by a PrimeFaces dialog.
  this.title  = dlgOperations.titlebar.children("span.ui-dialog-title").html();
  
  var self    = this;
  this.dialog.content[0].onclick = function () {
    self.stopEditingParameter();
  };
  
  this.configureVisibilityComboBox();
  this.configureCollectionsComboBox();
  this.configureReturnTypeComboBox();
  this.configureOperationsTable();
  this.configureParametersTable();
};

/**
 * Instance of the editor component.
 */
CLASSOperationDialog.prototype.editor;

/**
 * Instance of the graph component.
 */
CLASSOperationDialog.prototype.graph;

/**
 * The dialog to show.
 */
CLASSOperationDialog.prototype.dialog;

/**
 * The instance of the classifier being edited.
 */
CLASSOperationDialog.prototype.classifierCell;

/**
 * The instance of the operation being edited.
 */
CLASSOperationDialog.prototype.operationCell;

/**
 * The index on the table of the operation being edited.
 */
CLASSOperationDialog.prototype.operationIndex;

/**
 * The index on the table of the parameter being edited.
 */
CLASSOperationDialog.prototype.parameterIndex;

/**
 * The localized title text.
 */
CLASSOperationDialog.prototype.title;

/**
 * Initializes the dialog to edit the operations of the given classifier cell.
 * @param cell
 */
CLASSOperationDialog.prototype.init = function (cell) {
  this.classifierCell = cell;
  this.operationCell  = null;
  this.operationIndex = null;
  this.parameterIndex = null;
  
  this.loadOperationTableData();
  this.loadTypesData();
  this.clearSelection();
  this.clearFields();
  this.setTitle();
};

/**
 * Sets the title of the dialog by appending the name of the classifier being
 * edited.
 * 
 * @author Gabriel Leonardo Diaz, 10.02.2014.
 */
CLASSOperationDialog.prototype.setTitle = function () {
  this.dialog.titlebar.children("span.ui-dialog-title").html(this.title.replace("{0}", this.classifierCell.getAttribute("name")));
};

/**
 * Creates the operations table component.
 * 
 * @author Gabriel Leonardo Diaz, 17.01.2014.
 */
CLASSOperationDialog.prototype.configureOperationsTable = function () {
  var self = this;
  
  $("#operationsTable").datagrid({
      toolbar: "#operTbToolbar",
      singleSelect: true,
      
      onSelect: function (rowIndex, rowData) {
        self.selectionChanged(rowIndex, true);
      },
      
      onUnselectAll: function (rows) {
        self.selectionChanged(rows[0], false);
      },
      
      onUnselect: function (rowIndex, rowData) {
        self.selectionChanged(rowIndex, false);
      },
      
      columns:[[
          {field:"name",       title:"Nombre",          width:150},
          {field:"type",       title:"Tipo de Retorno", width:150},
          {field:"parameters", title:"Parametros",      width:150}
      ]]
  });
  
  $(function() {
      $("#newOperationBtn").bind("click", function() {
          self.newOperation();
      });
  });
  
  $(function() {
      $("#delOperationBtn").bind("click", function() {
          self.deleteOperation();
      });
  });
  
  $(function() {
      $("#saveOperationBtn").bind("click", function() {
          self.saveOperation();
      });
  });
};

/**
 * Loads the data in the operations table.
 * 
 * @author Gabriel Leonardo Diaz, 29.01.2014.
 */
CLASSOperationDialog.prototype.loadOperationTableData = function () {
  var jSonData = [];
  
  var operations = this.graph.getOperations(this.classifierCell);
  
  if (operations) {
    for (var i = 0; i < operations.length; i++) {
      var nameValue    = this.graph.getVisibilityChar(operations[i].getAttribute("visibility")) + " " + operations[i].getAttribute("name");
      var typeValue    = this.graph.escape(this.graph.convertTypeToString(operations[i]));
      var paramValue   = this.graph.escape(this.graph.convertParametersToString(operations[i]));
      
      jSonData.push({name: nameValue, type: typeValue, parameters: paramValue});
    }
  }
  
  $("#operationsTable").datagrid({ data : jSonData });
  $("#operationsTable").datagrid("reload");
};

/**
 * Updates the types in the return type field and the parameters table.
 */
CLASSOperationDialog.prototype.loadTypesData = function () {
  $("#operReturnType").combobox({ data: this.graph.getTypesJSon(true) });
  this.configureParametersTable();
};

/**
 * Populates and sets up the combo box component for visibility edition.
 * 
 * @author Gabriel Leonardo Diaz, 17.01.2014.
 */
CLASSOperationDialog.prototype.configureVisibilityComboBox = function () {
  $("#operVisibility").combobox({
      valueField:"id",
      textField:"text",
      panelHeight: 90,
      data: this.graph.getVisibilityJSon()
  });
  
  $("#operVisibility").combobox("setValue", "public"); // default value
  
  // Workaround: The panel is shown behind of the PrimeFaces modal dialog.
  var comboPanel = $("#operVisibility").combobox("panel");
  comboPanel.panel("panel").css("z-index", "2000");
};

/**
 * Constructs the table component to handle parameters of the operation being
 * edited.
 * 
 * @author Gabriel Leonardo Diaz, 27.01.2014.
 */
CLASSOperationDialog.prototype.configureParametersTable = function () {
  var self      = this;
  
  $("#parametersTable").datagrid({
      toolbar: "#paramsTbToolbar",
      singleSelect: true,
      
      onClickRow: function (index) {
        self.onClickParameter(index);
      },
      
      columns:[[
          { field:"name", title:"Nombre", width:150, editor: { type: "validatebox", options: { required: true, missingMessage: "" }}},
          
          { field:"type", title:"Tipo",   width:100, editor: {
              type:"combobox",
              options: { valueField:"id",  textField:"text", panelHeight: 200, data:this.graph.getTypesJSon(), required: true, missingMessage: "" }
          }},
          
          { field:"collection", title:"Coleccion", width:100, editor: {
              type:"combobox",
              options: { valueField:"id",  textField:"text", panelHeight: 200, data:this.graph.getCollectionsJSon() }
            },
            
            formatter: function (value, row, index) {
              return self.graph.getCollectionName(value);
            }
          },
      ]]
  });
  
  $(function() {
      $("#newParameterBtn").bind("click", function() {
          self.newParameter();
      });
  });
  
  $(function() {
      $("#delParameterBtn").bind("click", function() {
          self.deleteParameter();
      });
  });
  
  $(function() {
      $("#saveParameterBtn").bind("click", function() {
          self.saveParameter();
      });
  });
};

/**
 * Constructs the combo box component for return type field.
 * 
 * @author Gabriel Leonardo Diaz, 26.01.2014.
 */
CLASSOperationDialog.prototype.configureReturnTypeComboBox = function () {
  $("#operReturnType").combobox({
      valueField:"id",
      textField:"text",
      panelHeight: 200,
      data: this.graph.getTypesJSon(true)
  });
  
  $("#operReturnType").combobox("setValue", "void"); // default value
  
  // Workaround: The panel is shown behind of the PrimeFaces modal dialog.
  var comboPanel = $("#operReturnType").combobox("panel");
  comboPanel.panel("panel").css("z-index", "2000");
};

/**
 * Constructs the combo box component for collections field.
 * 
 * @author Gabriel Leonardo Diaz, 03.03.2014.
 */
CLASSOperationDialog.prototype.configureCollectionsComboBox = function () {
  $("#operCollection").combobox({
      valueField:"id",
      textField:"text",
      panelHeight: 90,
      data: this.graph.getCollectionsJSon()
  });
  
  $("#operCollection").combobox("setValue", ""); // default value
  
  // Workaround: The panel is shown behind of the PrimeFaces modal dialog.
  var comboPanel = $("#operCollection").combobox("panel");
  comboPanel.panel("panel").css("z-index", "2000");
};

/**
 * Processes the selection changes on the operations table.
 * 
 * @param rowIndex
 *          The row index selected or unselected.
 * @param selected
 *          A flag indicating the action performed.
 * @author Gabriel Leonardo Diaz, 26.01.2014.
 */
CLASSOperationDialog.prototype.selectionChanged = function (rowIndex, selected) {
  this.clearFields();
  
  if (selected) {
    this.operationCell  = this.graph.getOperations(this.classifierCell)[rowIndex];
    this.operationIndex = rowIndex;
    
    $("#operName").val(this.operationCell.getAttribute("name"));
    $("#operStaticCheck").prop("checked", this.operationCell.getAttribute("static") == "1");
    $("#operFinalCheck").prop("checked", this.operationCell.getAttribute("final") == "1");
    $("#operAbstractCheck").prop("checked", this.operationCell.getAttribute("abstract") == "1");
    $("#operSyncCheck").prop("checked", this.operationCell.getAttribute("synchronized") == "1");
    $("#operReturnType").combobox("setValue", this.operationCell.getAttribute("type"));
    $("#operCollection").combobox("setValue", this.operationCell.getAttribute("collection"));
    
    var jSonData = [];
    
    if (this.operationCell.value.childNodes) {
      var param;
      for (var i = 0; i < this.operationCell.value.childNodes.length; i++) {
        param = this.operationCell.value.childNodes[i];
        
        var nameValue       = param.getAttribute("name");
        var typeValue       = param.getAttribute("type");
        var collectionValue = param.getAttribute("collection");
        
        jSonData.push({name: nameValue, type: typeValue, collection: collectionValue});
      }
    }
    
    $("#parametersTable").datagrid({data: jSonData});
    $("#parametersTable").datagrid("reload");
  }
};

/**
 * Clears the selection on the operations table.
 * 
 * @author Gabriel Leonardo Diaz, 25.01.2014.
 */
CLASSOperationDialog.prototype.clearSelection = function () {
  $("#operationsTable").datagrid("unselectAll");
  this.operationIndex = null;
  this.operationCell  = null;
};

/**
 * Clears the fields used to edit or create operations.
 * 
 * @author Gabriel Leonardo Diaz, 27.01.2014.
 */
CLASSOperationDialog.prototype.clearFields = function () {
  $("#operName").val("");
  $("#operStaticCheck").prop("checked", false);
  $("#operFinalCheck").prop("checked", false);
  $("#operAbstractCheck").prop("checked", false);
  $("#operSyncCheck").prop("checked", false);
  $("#parametersTable").datagrid({data:[]});
  $("#parametersTable").datagrid("reload");
  $("#operReturnType").combobox("setValue", "void");
  $("#operVisibility").combobox("setValue", "public");
  $("#operConcurrency").combobox("setValue", "secuencial");
  $("#operCollection").combobox("setValue", "");
};

/**
 * Clears the fields and prepares the dialog to create a new operation.
 * 
 * @author Gabriel Leonardo Diaz, 26.01.2014.
 */
CLASSOperationDialog.prototype.newOperation = function () {
  this.clearSelection();
  this.clearFields();
  $("#operName" ).focus();
};

/**
 * Deletes the selected operation.
 * 
 * @author Gabriel Leonardo Diaz, 26.01.2014.
 */
CLASSOperationDialog.prototype.deleteOperation = function () {
  if (this.operationCell) {
    this.graph.removeCells([this.operationCell]);
    $("#operationsTable").datagrid("deleteRow", this.operationIndex);
    this.clearSelection();
  }
};

/**
 * Saves the changes made over an operation, or creates a new one.
 * 
 * @author Gabriel Leonardo Diaz, 26.01.2014.
 */
CLASSOperationDialog.prototype.saveOperation = function () {
  var nameValue       = $("#operName").val();
  var visValue        = $("#operVisibility").combobox("getValue");
  var retTypeValue    = $("#operReturnType").combobox("getValue");
  var staticValue     = $("#operStaticCheck").is(":checked") ? "1" : "0";
  var finalValue      = $("#operFinalCheck").is(":checked") ? "1" : "0";
  var abstractValue   = $("#operAbstractCheck").is(":checked") ? "1" : "0";
  var syncValue       = $("#operSyncCheck").is(":checked") ? "1" : "0";
  var collectionValue = $("#operCollection").combobox("getValue");
  
  if (nameValue == null || nameValue.length == 0) {
    // Invalid NAME
    return;
  }
  
  if (visValue == null || visValue.length == 0) {
    // Invalid VISIBILITY
    return;
  }
  
  if (this.graph.isInterface(this.classifierCell.value)) {
    if (retTypeValue == null || retTypeValue.length == 0) {
      // Invalid Return Type
      return;
    }
  }
  
  if (!this.stopEditingParameter()) {
    // Invalid Parameter
    return;
  }
  
  // Prepare the operation
  var operation;
  if (this.operationCell) {
    operation = this.operationCell.clone(true);
  }
  else {
    operation = this.graph.model.cloneCell(this.editor.getTemplate("operation"));
  }
  
  // Set values
  operation.setAttribute("name", nameValue);
  operation.setAttribute("visibility", visValue);
  operation.setAttribute("type", retTypeValue);
  operation.setAttribute("static", staticValue);
  operation.setAttribute("final", finalValue);
  operation.setAttribute("abstract", abstractValue);
  operation.setAttribute("synchronized", syncValue);
  operation.setAttribute("collection", collectionValue);
  operation.value.innerHTML = ""; // Remove all child nodes
  
  var paramRows = $("#parametersTable").datagrid("getRows");
  if (paramRows) {
    for (var i = 0; i < paramRows.length; i++) {
      var row   = paramRows[i];
      var cell  = this.graph.model.cloneCell(this.editor.getTemplate("parameter"));
      
      var param = cell.value;
      param.setAttribute("name", row.name);
      param.setAttribute("type", row.type);
      param.setAttribute("collection", row.collection);
      
      operation.value.appendChild(param);
    }
  }
  
  var operName    = this.graph.getVisibilityChar(visValue) + " " + operation.getAttribute("name");
  var operRetType = this.graph.escape(this.graph.convertTypeToString(operation));
  var operParams  = this.graph.escape(this.graph.convertParametersToString(operation));
  
  // Apply changes
  if (this.operationCell) {
    this.graph.editOperation(this.operationCell, operation);
    $("#operationsTable").datagrid("updateRow", {index: this.operationIndex, row: { name: operName, type: operRetType, parameters:  operParams}});
  }
  else {
    this.graph.addOperation(this.classifierCell, operation);
    $("#operationsTable").datagrid("insertRow", {row: { name: operName, type: operRetType, parameters: operParams }});
    $("#operationsTable").datagrid("selectRow", $("#operationsTable").datagrid("getRows").length - 1);
  }
  
  $("#operationsTable").datagrid("reload");
};

/**
 * Creates a new empty parameter row in the table.
 * 
 * @author Gabriel Leonardo Diaz, 27.01.2014.
 */
CLASSOperationDialog.prototype.newParameter = function () {
  if (this.stopEditingParameter()) {
    $("#parametersTable").datagrid("insertRow", {row: { name: "", type: "", collection: "" }});
    var newParamIndex = $("#parametersTable").datagrid("getRows").length - 1;
    this.startEditingParameter(newParamIndex);
    var ed = $("#parametersTable").datagrid("getEditor", {index : newParamIndex, field: "name" });
    $(ed.target).focus();
  }
};

/**
 * Deletes the selected parameter on the parameters table.
 * 
 * @author Gabriel Leonardo Diaz, 27.01.2014.
 */
CLASSOperationDialog.prototype.deleteParameter = function () {
  var selected = $("#parametersTable").datagrid("getSelected");
  if (selected == null) {
    return;
  }
  
  var index = $("#parametersTable").datagrid("getRowIndex", selected);
  if (index == null) {
    return;
  }
  
  $("#parametersTable").datagrid("cancelEdit", index);
  $("#parametersTable").datagrid("deleteRow", index);
};

/**
 * Saves the changes on the parameter being edited.
 * 
 * @author Gabriel Leonardo Diaz, 29.01.2014.
 */
CLASSOperationDialog.prototype.saveParameter = function () {
  if (this.stopEditingParameter()) {
    $("#parametersTable").datagrid("acceptChanges");
  }
};

/**
 * Starts the edition of the row on the table representing a parameter.
 * 
 * @author Gabriel Leonardo Diaz, 29.01.2014.
 */
CLASSOperationDialog.prototype.onClickParameter = function (index) {
  if (index == null) {
    return;
  }
  
  if (this.stopEditingParameter()) {
    this.startEditingParameter(index);
  }
};

/**
 * Begins the edition of the parameter represented by the given row. DO NOT CALL
 * THIS DIRECTLY.
 * 
 * @param index
 * @author Gabriel Leonardo Diaz, 29.01.2014.
 */
CLASSOperationDialog.prototype.startEditingParameter = function (index) {
  $("#parametersTable").datagrid("beginEdit", index);
  $("#parametersTable").datagrid("selectRow", index);
  
  this.parameterIndex = index;
  
  var editor;
  var comboPanel;
  
  // Workaround: The panel is shown behind of the PrimeFaces modal dialog.
  editor = $("#parametersTable").datagrid("getEditor", {index:index, field:"type"});
  comboPanel = $(editor.target).combobox("panel");
  comboPanel.panel("panel").css("z-index", "2000");
  
  editor = $("#parametersTable").datagrid("getEditor", {index:index, field:"collection"});
  comboPanel = $(editor.target).combobox("panel");
  comboPanel.panel("panel").css("z-index", "2000");
};

/**
 * Stops the edition of the parameter, applies the changes on the table.
 * 
 * @author Gabriel Leonardo Diaz, 29.01.2014.
 * @returns {Boolean}
 */
CLASSOperationDialog.prototype.stopEditingParameter = function () {
  if (this.parameterIndex == null) {
    return true;
  }
  
  if ($("#parametersTable").datagrid("validateRow", this.parameterIndex)) {
    $("#parametersTable").datagrid("endEdit", this.parameterIndex);
    this.parameterIndex = null;
    return true;
  }
  
  return false;
};

/**
 * Shows up the dialog to the user.
 * 
 * @author Gabriel Leonardo Diaz, 26.01.2014.
 */
CLASSOperationDialog.prototype.show = function () {
  this.dialog.show();
};

/**
 * Dialog that encapsulates the edition of the classifier's operations.
 * 
 * @author Gabriel Leonardo Diaz, 26.01.2014.
 */
CLASSRelationshipDialog = function (editor) {
  this.editor      = editor;
  this.graph       = editor.graph;
  this.dialog      = dlgRelationship; // Defined by a PrimeFaces dialog.
  this.title       = dlgRelationship.titlebar.children("span.ui-dialog-title").html();
  this.dialog.save = mxUtils.bind(this, function () {
    if (this.saveRelationship()) {
      this.dialog.hide();
    }
  });
  
  this.configureVisibilityCombo("sourceVisibility");
  this.configureVisibilityCombo("targetVisibility");
  this.configureCollectionsCombo("sourceCollection");
  this.configureCollectionsCombo("targetCollection");
  this.configureMultiplicityCombo("sourceMultiplicity");
  this.configureMultiplicityCombo("targetMultiplicity");
  this.configureNavigableCheckBoxes();
};

/**
 * The instance of the relationship being edited.
 */
CLASSRelationshipDialog.prototype.relationshipCell;

/**
 * Reference to the source property of the relationship.
 */
CLASSRelationshipDialog.prototype.sourceProperty;

/**
 * Reference to the target property of the relationship.
 */
CLASSRelationshipDialog.prototype.targetProperty;

/**
 * The localized text for the dialog title.
 */
CLASSRelationshipDialog.prototype.title;

/**
 * Initializes the dialog to edit relationship contained by the given cell.
 * 
 * @param cell
 * @author Gabriel Leonardo Diaz, 19.02.2014.
 */
CLASSRelationshipDialog.prototype.init = function (cell) {
  this.relationshipCell = cell;
  this.loadRelationship();
  this.loadSource();
  this.loadTarget();
  this.setTitle();
};

/**
 * Loads the name of the relationship in the text field.
 * 
 * @author Gabriel Leonardo Diaz, 16.03.2014.
 */
CLASSRelationshipDialog.prototype.loadRelationship = function () {
  $("#relName").val(this.relationshipCell.getAttribute("name"));
};

/**
 * Loads the fields in the source section.
 * 
 * @author Gabriel Leonardo Diaz, 14.03.2014.
 */
CLASSRelationshipDialog.prototype.loadSource = function () {
  this.sourceProperty = null;
  
  if (this.relationshipCell.children) {
    for (var i = 0; i < this.relationshipCell.children.length; i++) {
      var child = this.relationshipCell.children[i];
      if (child.getAttribute("type") == this.relationshipCell.source.id) {
        this.sourceProperty = child;
        break;
      }
    }
  }
  
  var sourceName         = "";
  var sourceVisibility   = "";
  var sourceCollection   = "";
  var sourceLower        = "";
  var sourceUpper        = "";
  var sourceNavigable    = false;
  
  if (this.sourceProperty) {
    sourceName         = this.sourceProperty.getAttribute("name");
    sourceVisibility   = this.sourceProperty.getAttribute("visibility");
    sourceCollection   = this.sourceProperty.getAttribute("collection");
    sourceLower        = this.sourceProperty.getAttribute("lower");
    sourceUpper        = this.sourceProperty.getAttribute("upper");
    sourceNavigable    = this.graph.isNavigable(this.sourceProperty);
  }
  
  $("#sourceName").val(sourceName);
  $("#sourceVisibility").combobox("setValue", sourceVisibility);
  $("#sourceType").val(this.graph.convertTypeIdToNameString(this.relationshipCell.source.id));
  $("#sourceCollection").combobox("setValue", sourceCollection);
  $("#sourceMultiplicity").combobox("setValue", this.graph.convertMultiplicity(sourceLower, sourceUpper));
  $("#sourceNavigable").prop("checked", sourceNavigable);
  $("#sourceNavigable").attr("disabled", !this.graph.isClass(this.relationshipCell.target.value));
  this.setEnabledSource(sourceNavigable);
};

/**
 * Enables/Disables the source role controls.
 * 
 * @author Gabriel Leonardo Diaz, 15.03.2014.
 */
CLASSRelationshipDialog.prototype.setEnabledSource = function (enabled) {
  if (enabled) {
    $("#sourceName").removeAttr("disabled");
    $("#sourceVisibility").combobox("enable");
    $("#sourceCollection").combobox("enable");
    if (!$("#sourceVisibility").combobox("getValue")) {
      $("#sourceVisibility").combobox("setValue", "private");
    }
  }
  else {
    $("#sourceName").attr("disabled", true);
    $("#sourceVisibility").combobox("disable");
    $("#sourceCollection").combobox("disable");
  }
};

/**
 * Loads the fields in the target section.
 * 
 * @author Gabriel Leonardo Diaz, 14.03.2014.
 */
CLASSRelationshipDialog.prototype.loadTarget = function () {
  this.targetProperty = null;
  
  if (this.relationshipCell.children) {
    for (var i = 0; i < this.relationshipCell.children.length; i++) {
      var child = this.relationshipCell.children[i];
      if (child.getAttribute("type") == this.relationshipCell.target.id) {
        this.targetProperty = child;
        break;
      }
    }
  }
  
  var targetName         = "";
  var targetVisibility   = "";
  var targetCollection   = "";
  var targetLower        = "";
  var targetUpper        = "";
  var targetNavigable    = false;
  
  if (this.targetProperty) {
    targetName         = this.targetProperty.getAttribute("name");
    targetVisibility   = this.targetProperty.getAttribute("visibility");
    targetCollection   = this.targetProperty.getAttribute("collection");
    targetLower        = this.targetProperty.getAttribute("lower");
    targetUpper        = this.targetProperty.getAttribute("upper");
    targetNavigable    = this.graph.isNavigable(this.targetProperty);
  }
  
  $("#targetName").val(targetName);
  $("#targetVisibility").combobox("setValue", targetVisibility);
  $("#targetType").val(this.graph.convertTypeIdToNameString(this.relationshipCell.target.id));
  $("#targetCollection").combobox("setValue", targetCollection);
  $("#targetMultiplicity").combobox("setValue", this.graph.convertMultiplicity(targetLower, targetUpper));
  $("#targetNavigable").prop("checked", targetNavigable);
  $("#targetNavigable").attr("disabled", !this.graph.isClass(this.relationshipCell.source.value));
  this.setEnabledTarget(targetNavigable);
};

/**
 * Saves the relationship and its properties.
 * 
 * @returns {Boolean}
 * @author Gabriel Leonardo Diaz, 16.03.2014.
 */
CLASSRelationshipDialog.prototype.saveRelationship = function () {
  var sourceName         = $("#sourceName").val();
  var sourceVisibility   = $("#sourceVisibility").combobox("getValue");
  var sourceCollection   = $("#sourceCollection").combobox("getValue");
  var sourceMultiplicity = $("#sourceMultiplicity").combobox("getValue");
  var sourceNavigable    = $("#sourceNavigable").is(":checked");
  
  var targetName         = $("#targetName").val();
  var targetVisibility   = $("#targetVisibility").combobox("getValue");
  var targetCollection   = $("#targetCollection").combobox("getValue");
  var targetMultiplicity = $("#targetMultiplicity").combobox("getValue");
  var targetNavigable    = $("#targetNavigable").is(":checked");
  
  // Validation
  if (!this.validProperty(sourceName, sourceVisibility, sourceCollection, sourceMultiplicity, sourceNavigable) ||
      !this.validProperty(targetName, targetVisibility, targetCollection, targetMultiplicity, targetNavigable)) {
    return false;
  }
  
  // 1. SAVE RELATIONSHIP
  var relationshipName = $("#relName").val();
  if (relationshipName != this.relationshipCell.getAttribute("name")) {
    this.graph.cellEditProperty(this.relationshipCell, "name", relationshipName, true);
  }
  
  
  // 2. SAVE SOURCE
  if (this.needsProperty(sourceNavigable, sourceMultiplicity)) {
    var property;
    if (this.sourceProperty) {
      property = this.sourceProperty.clone(true);
    }
    else {
      property = this.graph.model.cloneCell(this.editor.getTemplate("property"));
    }
    
    if (!sourceNavigable) {
      sourceName = "";
      sourceCollection = "";
    }
    
    var multiplicities = this.splitMultiplicity(sourceMultiplicity);
    property.setAttribute("name", sourceName);
    property.setAttribute("visibility", sourceVisibility);
    property.setAttribute("collection", sourceCollection);
    property.setAttribute("lower", multiplicities[0]);
    property.setAttribute("upper", multiplicities[1]);
    property.setAttribute("type", this.relationshipCell.source.id);
    
    if (this.sourceProperty) {
      this.graph.editAttribute(this.sourceProperty, property);
      if (sourceName) {
        this.graph.addAssociationNavigableStyle(true, this.relationshipCell);
      }
      else {
        this.graph.removeAssociationNavigableStyle(true, this.relationshipCell);
      }
    }
    else {
      this.graph.addAssociationAttribute(this.relationshipCell, property, true, false);
    }
  }
  else if (this.sourceProperty) {
    this.graph.removeAssociationAttribute(this.relationshipCell, this.sourceProperty, true);
  }
  
  // 3. SAVE TARGET
  if (this.needsProperty(targetNavigable, targetMultiplicity)) {
    var property;
    if (this.targetProperty) {
      property = this.targetProperty.clone(true);
    }
    else {
      property = this.graph.model.cloneCell(this.editor.getTemplate("property"));
    }
    
    if (!targetNavigable) {
      targetName = "";
      targetCollection = "";
    }
    
    var multiplicities = this.splitMultiplicity(targetMultiplicity);
    property.setAttribute("name", targetName);
    property.setAttribute("visibility", targetVisibility);
    property.setAttribute("collection", targetCollection);
    property.setAttribute("lower", multiplicities[0]);
    property.setAttribute("upper", multiplicities[1]);
    property.setAttribute("type", this.relationshipCell.target.id);
    
    if (this.targetProperty) {
      this.graph.editAttribute(this.targetProperty, property);
      if (targetName) {
        this.graph.addAssociationNavigableStyle(false, this.relationshipCell);
      }
      else {
        this.graph.removeAssociationNavigableStyle(false, this.relationshipCell);
      }
    }
    else {
      this.graph.addAssociationAttribute(this.relationshipCell, property, false, false);
    }
  }
  else if (this.targetProperty) {
    this.graph.removeAssociationAttribute(this.relationshipCell, this.targetProperty, false);
  }
  
  return true;
};

/**
 * Validates the property fields.
 * @param name
 * @param visibility
 * @param collection
 * @param multiplicity
 * @param navigable
 * @returns {Boolean}
 */
CLASSRelationshipDialog.prototype.validProperty = function (name, visibility, collection, multiplicity, navigable) {
  if (navigable) {
    if (!name) {
      return false;
    }
    
    if (!visibility) {
      return false;
    }
  }
  
  if (!this.validMultiplicity(multiplicity)) {
    return false;
  }
  
  return true;
};

/**
 * Validates the multiplicity of the property.
 * @param multiplicity
 * @returns {Boolean}
 */
CLASSRelationshipDialog.prototype.validMultiplicity = function (multiplicity) {
  if (multiplicity) {
    var values = multiplicity.split("..");
    
    if (values.length > 2) {
      return false;
    }
    
    if ((!this.isInteger(values[0]) && values[0] != "*") || parseInt(values[0]) < 0) {
      return false;
    }
    
    if (values.length > 1) {
      if ((!this.isInteger(values[1]) && values[1] != "*") || parseInt(values[1]) < 0) {
        return false;
      }
      
      if (this.isInteger(values[1]) && parseInt(values[0]) > parseInt(values[1])) {
        return false;
      }
    }
  }
  
  return true;
};

/**
 * Separates the multiplicity value into two values (lower and upper).
 * 
 * @param multiplicity
 * @author Gabriel Leonardo Diaz, 12.04.2014.
 */
CLASSRelationshipDialog.prototype.splitMultiplicity = function (multiplicity) {
  var values = [];
  
  if (multiplicity) {
    var separated = multiplicity.split("..");
    
    // Lower
    values.push(separated[0]); 
    
    // Upper
    values.push(separated.length > 1 ? separated[1] : "");
  }
  
  return values;
};

/**
 * Checks if the relationship end needs a property.
 * 
 * @param navigable
 *          If the end is navigable.
 * @param multiplicity
 *          The multiplicity value selected for the end.
 * @author Gabriel Leonardo Diaz, 30.03.2014.
 */
CLASSRelationshipDialog.prototype.needsProperty = function (navigable, multiplicity) {
  if (navigable || multiplicity) {
    return true;
  }
  return false;
};

/**
 * Check if the value is a number.
 * @param number
 */
CLASSRelationshipDialog.prototype.isInteger = function (number) {
  return !isNaN(number) && parseInt(number) % 1 == 0;
};

/**
 * 
 * @param enabled
 */
CLASSRelationshipDialog.prototype.setEnabledTarget = function (enabled) {
  if (enabled) {
    $("#targetName").removeAttr("disabled");
    $("#targetVisibility").combobox("enable");
    $("#targetCollection").combobox("enable");
    if (!$("#targetVisibility").combobox("getValue")) {
      $("#targetVisibility").combobox("setValue", "private");
    }
  }
  else {
    $("#targetName").attr("disabled", true);
    $("#targetVisibility").combobox("disable");
    $("#targetCollection").combobox("disable");
  }
};

/**
 * Sets the title of the dialog by appending the name of the relationship being
 * edited.
 * 
 * @author Gabriel Leonardo Diaz, 10.02.2014.
 */
CLASSRelationshipDialog.prototype.setTitle = function () {
  var relationshipName = "";
  
  if (this.graph.isAggregation(this.relationshipCell.value)) {
    relationshipName = "Agregaci&oacute;n";
  }
  else if (this.graph.isComposition(this.relationshipCell.value)) {
    relationshipName = "Composici&oacute;n";
  }
  else {
    relationshipName = "Asociaci&oacute;n";
  }
  
  this.dialog.titlebar.children("span.ui-dialog-title").html(this.title.replace("{0}", relationshipName));
};

/**
 * Configures the visibility combo box for the element identified by the given
 * ID.
 * 
 * @param elementId
 * @author Gabriel Leonardo Diaz, 20.02.2014.
 */
CLASSRelationshipDialog.prototype.configureVisibilityCombo = function (elementId) {
  elementId = "#" + elementId;
  
  $(elementId).combobox({
      valueField:"id",
      textField:"text",
      panelHeight: 90,
      width: 380,
      data: this.graph.getVisibilityJSon()
  });
  
  $(elementId).combobox("setValue", "private"); // default value
  
  // Workaround: The panel is shown behind of the PrimeFaces modal dialog.
  var comboPanel = $(elementId).combobox("panel");
  comboPanel.panel("panel").css("z-index", "2000");
};

/**
 * Configures the collections combo box for the element identified by the given
 * ID.
 * 
 * @param elementId
 * @author Gabriel Leonardo Diaz, 20.02.2014.
 */
CLASSRelationshipDialog.prototype.configureCollectionsCombo = function (elementId) {
  elementId = "#" + elementId;
  
  $(elementId).combobox({
      valueField:"id",
      textField:"text",
      panelHeight: 90,
      width: 220,
      data: this.graph.getCollectionsJSon()
  });
  
  // Workaround: The panel is shown behind of the PrimeFaces modal dialog.
  var comboPanel = $(elementId).combobox("panel");
  comboPanel.panel("panel").css("z-index", "2000");
};

/**
 * Configures combo box for multiplicities for the element identified by the
 * given ID.
 * 
 * @param elementId
 *          The element id.
 * @author Gabriel Leonardo Diaz, 10.04.2014.
 */
CLASSRelationshipDialog.prototype.configureMultiplicityCombo = function (elementId) {
  elementId = "#" + elementId;
  
  $(elementId).combobox({
      valueField:"id",
      textField:"text",
      panelHeight: 90,
      width: 150,
      data: this.graph.getMultiplicitiesJSon()
  });
  
  // Workaround: The panel is shown behind of the PrimeFaces modal dialog.
  var comboPanel = $(elementId).combobox("panel");
  comboPanel.panel("panel").css("z-index", "2000");
};

/**
 * Configures the checkboxes to enable/disable the fields when checked.
 * 
 * @author Gabriel Leonardo Diaz, 15.03.2014.
 */
CLASSRelationshipDialog.prototype.configureNavigableCheckBoxes = function () {
  var self = this;
  
  $("#sourceNavigable").change(function() {
    self.setEnabledSource($(this).is(":checked"));
  });
  
  $("#targetNavigable").change(function() {
    self.setEnabledTarget($(this).is(":checked"));
  });
};

/**
 * Shows up the dialog to the user.
 * 
 * @author Gabriel Leonardo Diaz, 26.01.2014.
 */
CLASSRelationshipDialog.prototype.show = function () {
  this.dialog.show();
};
