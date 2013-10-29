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
  
  // 2. Undo Manager
  this.configureUndoManager();
  
  // 3. Actions
  this.configureActions();
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
  
  // TODO override functions for labels  and foldable
};

/**
 * Configures the actions for the editor.
 * 
 * @author Gabriel Leonardo Diaz, 17.10.2013.
 */
CLASSEditor.prototype.configureActions = function () {
  // TODO
};

/**
 * Configures the template components for the editor.
 * 
 * @author Gabriel Leonardo Diaz, 26.10.2013.
 */
CLASSEditor.prototype.configureTemplates = function () {
  // TODO
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
    var cand  = graph.getSelectionCellsForChanges(evt.getProperty('edit').changes);
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
          
          // GD, 25.10.2013. Applying layout to also child swimlane nodes.
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