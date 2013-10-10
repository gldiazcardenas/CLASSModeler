/*******************************************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS 
 * C�cuta, Colombia 
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
CLASSEditor.prototype.init = function (graphContainer, paletteContainer, outlineContainer) {
  // Gets the containers
  this.graphContainer   = graphContainer;
  this.paletteContainer = paletteContainer;
  this.outlineContainer = outlineContainer;
  
  // Creates the Graph
  this.graph = new CLASSGraph();
  this.graph.init(this.graphContainer);
  this.graph.refresh();
  
  // Creates the outline (zoom panel)
  this.outline = new mxOutline(this.graph);
  this.outline.init(this.outlineContainer);
  this.outline.updateOnPan = true;
  
  // Creates the Palette component.
  this.palette = new CLASSPalette(this);
  this.palette.init(this.paletteContainer);
  
  // Creates the Actions Handler
  this.actionHandler = new CLASSActionHandler(this);
  this.actionHandler.init();
  
  // Creates the UnDo-ReDo Handler
  this.undoManager = this.createUndoRedoManager();
  
  // Creates the Key Action Handler
  this.keyHandler = this.createKeyManager();
};

/**
 * Creates the UNDO-REDO handler mechanism and installs it into the graph
 * editor.
 */
CLASSEditor.prototype.createUndoRedoManager = function () {
  var undoRedoManager = new mxUndoManager();
  var graph           = this.graph;
  
  // Installs the command history
  var undoListener = function(sender, evt) {
    undoRedoManager.undoableEditHappened(evt.getProperty('edit'));
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
  
  undoRedoManager.addListener(mxEvent.UNDO, undoHandler);
  undoRedoManager.addListener(mxEvent.REDO, undoHandler);
  
  // Update actions handler
  var undoAction    = this.actionHandler.get(CLASSActionName.UNDO);
  var redoAction    = this.actionHandler.get(CLASSActionName.REDO);
  var updateActions = function() {
    undoAction.setEnabled(undoRedoManager.canUndo());
    redoAction.setEnabled(undoRedoManager.canRedo());
  };

  undoRedoManager.addListener(mxEvent.ADD, updateActions);
  undoRedoManager.addListener(mxEvent.CLEAR, updateActions);
  undoRedoManager.addListener(mxEvent.UNDO, updateActions);
  undoRedoManager.addListener(mxEvent.REDO, updateActions);

  // Updates the button states once
  updateActions();
  
  return undoRedoManager;
};

/**
 * Function that initializes the key handler for the graph component,
 * this allows to process the key events made by the user.
 * 
 * @author Gabriel Leonardo Diaz, 16.06.2013.
 */
CLASSEditor.prototype.createKeyManager = function () {
  var graph = this.graph;
  
  // Instance the key handler object.
  var keyHandler = new mxKeyHandler(graph);
  
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
 * Executes the action represented by the given action name code. This allows to
 * use the action externally to this object.
 * 
 * @author Gabriel Leonardo Diaz, 26.06.2013.
 */
CLASSEditor.prototype.executeAction = function (actionName, parameters) {
  this.actionHandler.executeAction (actionName, parameters);
};