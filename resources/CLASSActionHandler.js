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
 * Class that handles all interactions with the current diagram edited. E.g.
 * selecting all components, delete, undo, redo, ..etc.
 */
CLASSActionHandler = function (editor) {
  this.editor  = editor;
  this.actions = new Object();
};

/**
 * Initializes the actions for the application.
 */
CLASSActionHandler.prototype.init = function () {
  var editor = this.editor;
  var graph = this.editor.graph;
  
  // Select all 
  this.addAction(CLASSActionName.SELECT_ALL, true, function() {
    graph.selectAll();
  });
  
  // Move cell with navigation buttons
  this.addAction(CLASSActionName.MOVE_CELLS, true, function(keyCodePress) {
    if (!graph.isSelectionEmpty()) {
      var dx = 0;
      var dy = 0;
      
      if (keyCodePress == CLASSKeyCode.LEFT_KEY) {
        dx = -10;
      }
      else if (keyCodePress == CLASSKeyCode.UP_KEY) {
        dy = -10;
      }
      else if (keyCodePress == CLASSKeyCode.RIGHT_KEY) {
        dx = 10;
      }
      else if (keyCodePress == CLASSKeyCode.DOWN_KEY) {
        dy = 10;
      }
      
      graph.moveCells(graph.getSelectionCells(), dx, dy);
      graph.scrollCellToVisible(graph.getSelectionCell());
    }
  });
  
  // Delete cells
  this.addAction(CLASSActionName.DELETE, true, function() {
    // Handles special case where delete is pressed while connecting
    if (graph.connectionHandler.isConnecting()) {
      graph.connectionHandler.reset();
    }
    else {
      graph.removeCells();
    }
  });
  
  // Undo/Redo
  this.addAction(CLASSActionName.UNDO, true, function() { editor.undoManager.undo(); });
  this.addAction(CLASSActionName.REDO, true, function() { editor.undoManager.redo(); });
  
  // Copy/Cut/Paste
  this.addAction(CLASSActionName.COPY, true, function() { mxClipboard.copy(graph); });
  this.addAction(CLASSActionName.CUT, true, function() { mxClipboard.cut(graph); });
  this.addAction(CLASSActionName.PASTE, true, function() { mxClipboard.paste(graph); });
  
  // View
  this.addAction(CLASSActionName.ZOOM_RESTORE, true, function() { graph.zoomTo(1); });
  this.addAction(CLASSActionName.ZOOM_IN, true, function() { graph.zoomIn(); });
  this.addAction(CLASSActionName.ZOOM_OUT, true, function() { graph.zoomOut(); });
  this.addAction(CLASSActionName.ZOOM_CUSTOM, true, function (value) { graph.zoomTo(parseInt(value) / 100); });
};

/**
 * Creates a new action and adds it to the map.
 * 
 * @param actionName
 *          The name of the action, this is an identifier in the map.
 * @param enabled
 *          A flag to indicate the initial state of the action.
 * @param callbackFunction
 *          The function to invoke when the action is executed.
 */
CLASSActionHandler.prototype.addAction = function (actionName, enabled, callbackFunction) {
  return this.put(actionName, new CLASSAction(actionName, callbackFunction, enabled));
};

/**
 * Puts the given action in the map with the respective key.
 * 
 * @param actionName
 *          The name used as identifier in the map.
 * @param action
 *          The action to put into the map.
 * @returns The same action received.
 */
CLASSActionHandler.prototype.put = function (actionName, action) {
  this.actions[actionName] = action;
  return action;
};

/**
 * Gets the action represented by the given key.
 * 
 * @param actionName
 *          The name to look for the action in the map.
 * @returns The respective action, null if there is not any action in the map
 *          with the given name.
 */
CLASSActionHandler.prototype.get = function (actionName) {
  return this.actions[actionName];
};

/**
 * Executes the action identified by the code name.
 * 
 * @param actionNameCode
 *          The name of the action.
 * @param parameters
 *          The parameters for the action
 * @author Gabriel Leonardo Diaz, 08.10.2013.
 */
CLASSActionHandler.prototype.executeAction = function (actionName, parameters) {
  var action = this.get(actionName);
  if (action != null) {
    action.funct(parameters);
  }
};

/**
 * Constructs a new action for the given parameters.
 */
CLASSAction = function (name, funct) {
  mxEventSource.call(this);
  
  this.name     = name;
  this.funct    = funct;
  this.enabled  = true;
};

// CLASSAction inherits from mxEventSource
mxUtils.extend(CLASSAction, mxEventSource);

CLASSAction.prototype.setEnabled = function (enabled) {
  this.enabled = enabled;
};

CLASSAction.prototype.isEnabled = function () {
  return this.enabled;
};

/**
 * JavaScript Key codes, used to handle the key commands and short cuts.
 * 
 * @author Gabriel Leonardo Diaz, 16.06.2013.
 */
var CLASSKeyCode = {
  LEFT_KEY          : 37,
  UP_KEY            : 38,
  RIGHT_KEY         : 39,
  DOWN_KEY          : 40,
  
  BACKSPACE_KEY     : 8,
  DELETE_KEY        : 46,
  
  A_KEY             : 65,
  C_KEY             : 67,
  V_KEY             : 86,
  X_KEY             : 88,
  Y_KEY             : 89,
  Z_KEY             : 90
};

/**
 * Enumeration that contains all actions names in the Action Handler.
 * 
 * @author Gabriel Leonardo Diaz, 25.06.2013.
 */
var CLASSActionName = {
  SELECT_ALL        : 'selectAll',
  MOVE_CELLS        : 'moveCells',
  DELETE            : 'delete',
  UNDO              : 'undo',
  REDO              : 'redo',
  COPY              : 'copy',
  CUT               : 'cut',
  PASTE             : 'paste',
  ZOOM_IN           : 'zoomIn',
  ZOOM_OUT          : 'zoomOut',
  ZOOM_CUSTOM       : 'zoomCustom',
  ZOOM_RESTORE      : 'zoomRestore'
};



