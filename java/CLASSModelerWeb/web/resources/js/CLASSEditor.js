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
 */
CLASSEditor = function (initialXML, urlInit, urlImage, urlPoll, urlNotify) {
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
CLASSEditor.prototype.init = function (initialXML, graphContainer, toolboxContainer, outlineContainer) {
  // Gets the containers
  this.graphContainer   = graphContainer;
  this.toolboxContainer = toolboxContainer;
  this.outlineContainer = outlineContainer;
  
  // Creates the Graph
  this.graph = new CLASSGraph();
  this.graph.init(this.graphContainer);
  this.graph.refresh();
  
  // Creates the outline (zoom panel)
  this.outline = new mxOutline(this.graph);
  this.outline.init(this.outlineContainer);
  this.outline.updateOnPan = true;
  
  // Creates the ToolBox component.
  this.toolbox = new CLASSToolBox(this);
  this.toolbox.init(this.toolboxContainer);
  
  // Creates the Actions Handler
  this.actionHandler = new CLASSActionHandler(this);
  this.actionHandler.init();
  
  // Creates the UnDo-ReDo Handler
  this.createUndoRedoManager();
  
  // Creates the Key Action Handler
  this.createKeyManager();
};

/**
 * Creates the UNDO-REDO handler mechanism and installs it into the graph
 * editor.
 */
CLASSEditor.prototype.createUndoRedoManager = function () {
  var undoRedoManager = new mxUndoManager();
  
  // Installs the command history
  var listener = function(sender, evt) {
    undoRedoManager.undoableEditHappened(evt.getProperty('edit'));
  };
  
  this.graph.getModel().addListener(mxEvent.UNDO, listener);
  this.graph.getView().addListener(mxEvent.UNDO, listener);
  
  // Keeps the selection in sync with the history
  var undoHandler = function(sender, evt) {
    var cand = this.graph.getSelectionCellsForChanges(evt.getProperty('edit').changes);
    var cells = [];
    
    for (var i = 0; i < cand.length; i++) {
      if (this.graph.view.getState(cand[i]) != null) {
        cells.push(cand[i]);
      }
    }
    
    this.graph.setSelectionCells(cells);
  };
  
  undoRedoManager.addListener(mxEvent.UNDO, undoHandler);
  undoRedoManager.addListener(mxEvent.REDO, undoHandler);
};

/**
 * Function that initializes the key handler for the graph component,
 * this allows to process the key events made by the user.
 * 
 * @author Gabriel Leonardo Diaz, 16.06.2013.
 */
CLASSEditor.prototype.createKeyManager = function () {
  var graph = this.graph;
  
  // Helper function to move cells with the cursor keys
  function moveCells(keyCodePress) {
    if (!graph.isSelectionEmpty()) {
      var dx = 0;
      var dy = 0;
      
      if (keyCodePress == CLASSKeyCode.LEFT_KEY) {
        dx = -5;
      }
      else if (keyCodePress == CLASSKeyCode.UP_KEY) {
        dy = -5;
      }
      else if (keyCodePress == CLASSKeyCode.RIGHT_KEY) {
        dx = 5;
      }
      else if (keyCodePress == CLASSKeyCode.DOWN_KEY) {
        dy = 5;
      }
      
      graph.moveCells(graph.getSelectionCells(), dx, dy);
      graph.scrollCellToVisible(graph.getSelectionCell());
    }
  };
  
  
  
  // Instance the key handler object.
  var keyHandler = new mxKeyHandler(graph);
  
  // Binding the functions executed on key pressed.
  keyHandler.bindKey(CLASSKeyCode.LEFT_KEY, function() { moveCells(CLASSKeyCode.LEFT_KEY); });
  keyHandler.bindKey(CLASSKeyCode.UP_KEY, function() { moveCells(CLASSKeyCode.UP_KEY); });
  keyHandler.bindKey(CLASSKeyCode.RIGHT_KEY, function() { moveCells(CLASSKeyCode.RIGHT_KEY); });
  keyHandler.bindKey(CLASSKeyCode.DOWN_KEY, function() { moveCells(CLASSKeyCode.DOWN_KEY); });
  
};