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
};

/**
 * Creates a new action and adds it to the map.
 * 
 * @param key
 *          The key of the action, this is an identifier in the map.
 * @param enabled
 *          A flag to indicate the initial state of the action.
 * @param callbackFunction
 *          The function to invoke when the action is executed.
 */
CLASSActionHandler.prototype.addAction = function (key, enabled, callbackFunction) {
  return this.put(key, new CLASSAction('Action', callbackFunction, enabled));
};

/**
 * Puts the given action in the map with the respective key.
 * 
 * @param key
 *          The key used as identifier in the map.
 * @param action
 *          The action to put into the map.
 * @returns The same action received.
 */
CLASSActionHandler.prototype.put = function (key, action) {
  this.actions[key] = action;
  return action;
};

/**
 * Gets the action represented by the given key.
 * 
 * @param key
 *          The key to look for the action in the map.
 * @returns The respective action, null if there is not any action in the map
 *          with the given key.
 */
CLASSActionHandler.prototype.get = function (key) {
  return this.actions[key];
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
  REDO              : 'redo'
};



