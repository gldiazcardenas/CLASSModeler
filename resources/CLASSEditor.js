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
CLASSEditor = function (urlInit, urlImage, urlPoll, urlNotify) {
  this.urlInit   = urlInit;
  this.urlImage  = urlImage;
  this.urlPoll   = urlPoll;
  this.urlNotify = urlNotify;
  
  // Call super
  mxEditor.call(this);
};

// CLASSEditor inherits from mxEditor
mxUtils.extend(CLASSEditor, mxEditor);

/**
 * Initializer method for the CLASSEditor.
 */
CLASSEditor.prototype.init = function (graphContainer) {
  this.layoutSwimlanes = true;
  
  // 1. Graph
  this.configureGraph(graphContainer);
  
  // 2. Actions
  this.configureActions();
  
  // 3. Undo Manager
  this.configureUndoManager();
  
  // 4. Key Manager
  this.configureKeyManager();
};

/**
 * Configures the graph component by setting special behaviors.
 * 
 * @param container
 *          The container HTML element for the graph.
 * @author Gabriel Leonardo Diaz, 17.10.2013.
 */
CLASSEditor.prototype.configureGraph = function (container) {
  this.setGraphContainer(container);
  this.graph.setCellsDisconnectable(false);
  this.graph.setCellsCloneable(false);
  this.graph.dropEnabled = true;
  
  // Determines if the cell is a class
  this.graph.isClassCell = function (cell) {
    return cell != null && cell.value != null && cell.value.nodeName.toLowerCase() == 'class';
  };
  
  // Determines if the cell is an enumeration
  this.graph.isEnumerationCell = function (cell) {
    return cell != null && cell.value != null && cell.value.nodeName.toLowerCase() == 'enumeration';
  };
  
  // Determines if the cell is an interface
  this.graph.isInterfaceCell = function (cell) {
    return cell != null && cell.value != null && cell.value.nodeName.toLowerCase() == 'interface';
  };
  
  // Determines if the cell is a package
  this.graph.isPackageCell = function (cell) {
    return cell != null && cell.value != null && cell.value.nodeName.toLowerCase() == 'package';
  };
  
  // Determines if the cell is a comment
  this.graph.isCommentCell = function (cell) {
    return cell != null && cell.value != null && cell.value.nodeName.toLowerCase() == 'comment';
  };
  
  // Determines if the cell is a comment
  this.graph.isAssociationCell = function (cell) {
    return cell != null && cell.value != null && cell.value.nodeName.toLowerCase() == 'association';
  };
  
  // Determines if the cell is a property
  this.graph.isPropertyCell = function (cell) {
    return cell != null && cell.value != null && cell.value.nodeName.toLowerCase() == 'property';
  };
  
  // Determines if the cell is a operation
  this.graph.isOperationCell = function (cell) {
    return cell != null && cell.value != null && cell.value.nodeName.toLowerCase() == 'operation';
  };
  
  // Determines if the cell is a named element
  this.graph.isNamedElementCell = function (cell) {
    return cell != null && cell.value != null && cell.value.nodeName.toLowerCase() == 'namedelement';
  };
  
  // Determines if the cell is an enumeration literal
  this.graph.isEnumerationLiteralCell = function (cell) {
    return cell != null && cell.value != null && cell.value.nodeName.toLowerCase() == 'enumerationliteral';
  };
  
  // Determines if the cell can be resized
  this.graph.isCellResizable = function (cell) {
    return this.isClassCell(cell) || this.isEnumerationCell(cell) || this.isInterfaceCell(cell) || this.isPackageCell(cell) || this.isCommentCell(cell);
  };
  
  // Determines if the cell can be moved
  this.graph.isCellMovable = function (cell) {
    return this.isClassCell(cell) || this.isEnumerationCell(cell) || this.isInterfaceCell(cell) || this.isPackageCell(cell) || this.isCommentCell(cell) || this.isAssociationCell(cell);
  };
  
  // Determines if the cell can be collapsed
  this.graph.isCellFoldable = function (cell) {
    return this.isClassCell(cell) || this.isEnumerationCell(cell) || this.isInterfaceCell(cell);
  };
  
  // Converts the DOM user object to a string representation
  this.graph.convertValueToString = function (cell) {
    if (this.isClassCell(cell) || this.isNamedElementCell(cell) || this.isEnumerationLiteralCell(cell) || this.isPackageCell(cell)) {
      return cell.getAttribute('name');
    }
    
    if (this.isEnumerationCell(cell)) {
      return '<<enum>>\n' + cell.getAttribute('name');
    }
    
    if (this.isInterfaceCell(cell)) {
      return '<<interfaz>>\n' + cell.getAttribute('name');
    }
    
    if (this.isCommentCell(cell)) {
      return cell.getAttribute('body');
    } 
    
    if (this.isPropertyCell(cell)) {
      return cell.getAttribute('visibility') + ' ' + cell.getAttribute('name') + ' : ' + cell.getAttribute('type');
    }
    
    if (this.isOperationCell(cell)) {
      // TODO GD include parameters
      return cell.getAttribute('visibility') + ' ' + cell.getAttribute('name') + '() : ' + cell.getAttribute('returnType');
    }
    
    // Super
    return mxGraph.prototype.convertValueToString.apply (this, arguments);
  };
};

/**
 * Configures the actions for the editor.
 * 
 * @author Gabriel Leonardo Diaz, 17.10.2013.
 */
CLASSEditor.prototype.configureActions = function () {
  this.actionHandler = new CLASSActionHandler(this);
  this.actionHandler.init();
};

/**
 * Configures the UNDO-REDO handler mechanism and installs it into the graph
 * editor.
 * 
 * @author Gabriel Leonardo Diaz, 16.10.2013.
 */
CLASSEditor.prototype.configureUndoManager = function () {
  var undoMgr = this.undoManager;
  var graph   = this.graph;
  
  // Installs the command history
  var undoListener = function(sender, evt) {
    undoMgr.undoableEditHappened(evt.getProperty('edit'));
  };
  
  graph.getModel().addListener(mxEvent.UNDO, undoListener);
  graph.getView().addListener(mxEvent.UNDO, undoListener);

  // Keeps the selection in sync with the history
  var undoHandler = function(sender, evt) {
    var cand = graph.getSelectionCellsForChanges(evt.getProperty('edit').changes);
    var cells = [];
    
    for (var i = 0; i < cand.length; i++) {
      if (graph.view.getState(cand[i]) != null) {
        cells.push(cand[i]);
      }
    }
    graph.setSelectionCells(cells);
  };
  
  undoMgr.addListener(mxEvent.UNDO, undoHandler);
  undoMgr.addListener(mxEvent.REDO, undoHandler);
};

/**
 * Function that initializes the key handler for the graph component,
 * this allows to process the key events made by the user.
 * 
 * @author Gabriel Leonardo Diaz, 16.06.2013.
 */
CLASSEditor.prototype.configureKeyManager = function () {
  // Instance the key handler object.
  var keyHandler = this.keyHandler.handler;
  
  // Binds keystrokes to actions
  var setActionKeyHandler = mxUtils.bind(this, function (keyCode, actionNameCode, control, shift, parameter) {
    var action = this.actionHandler.get(actionNameCode);
    
    if (action != null) {
      var f = function() {
        if (action.enabled) {
          if (parameter != null) {
            action.funct(parameter);
          }
          else {
            action.funct();
          }
        }
      };
      
      if (control) {
        if (shift) {
          keyHandler.bindControlShiftKey(keyCode, f);
        }
        else {
          keyHandler.bindControlKey(keyCode, f);
        }
      }
      else if (shift) {
        keyHandler.bindShiftKey(keyCode, f);
      }
      else {
        keyHandler.bindKey(keyCode, f);
      }
    }
  });
  
  // Binding the functions with the actions in CLASSActionHandler
  setActionKeyHandler(CLASSKeyCode.LEFT_KEY, CLASSActionName.MOVE_CELLS, false, false, CLASSKeyCode.LEFT_KEY);
  setActionKeyHandler(CLASSKeyCode.UP_KEY, CLASSActionName.MOVE_CELLS, false, false, CLASSKeyCode.UP_KEY);
  setActionKeyHandler(CLASSKeyCode.RIGHT_KEY, CLASSActionName.MOVE_CELLS, false, false, CLASSKeyCode.RIGHT_KEY);
  setActionKeyHandler(CLASSKeyCode.DOWN_KEY, CLASSActionName.MOVE_CELLS, false, false, CLASSKeyCode.DOWN_KEY);
  setActionKeyHandler(CLASSKeyCode.A_KEY, CLASSActionName.SELECT_ALL, true, false, null);
  setActionKeyHandler(CLASSKeyCode.DELETE_KEY, CLASSActionName.DELETE, false, false, null);
  setActionKeyHandler(CLASSKeyCode.Z_KEY, CLASSActionName.UNDO, true, false, null);
  setActionKeyHandler(CLASSKeyCode.Y_KEY, CLASSActionName.REDO, true, false, null);
  setActionKeyHandler(CLASSKeyCode.C_KEY, CLASSActionName.COPY, true, false, null);
  setActionKeyHandler(CLASSKeyCode.X_KEY, CLASSActionName.CUT, true, false, null);
  setActionKeyHandler(CLASSKeyCode.V_KEY, CLASSActionName.PASTE, true, false, null);
  
  return keyHandler;
};

/**
 * Configures the layout for swimlanes.
 * 
 * @author Gabriel Leonardo Diaz, 16.10.2013.
 */
CLASSEditor.prototype.createSwimlaneLayout = function () {
  var layout          = new mxStackLayout(this.graph, false);
  layout.fill         = true;
  layout.resizeParent = true;
  
  // Overrides the function to always return true
  layout.isVertexMovable = function(cell) {
    return true;
  };
  
  return layout;
};

/**
 * Override default layout cell to include child nested swimlanes.
 * 
 * @author Gabriel Leonardo Diaz, 17.10.2013.
 */
mxLayoutManager.prototype.layoutCells = function(cells) {
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
      
      this.fireEvent(new mxEventObject(mxEvent.LAYOUT_CELLS, 'cells', cells));
    }
    finally {
      model.endUpdate();
    }
  }
};