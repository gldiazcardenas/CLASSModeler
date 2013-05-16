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
  
  // Creates the toolBox component.
  this.toolbox = new CLASSToolBox(this);
  this.toolbox.init(this.toolboxContainer);
  
  // Creates the Undo-Redo Handler
  this.createUndoRedoManager();
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