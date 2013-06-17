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
  var graph = this.editor.graph;
  
  this.addAction('selectAll', function() { graph.selectAll(); }, true, null, 'Ctrl+A');
};

/**
 * Creates a new action and adds it to the map.
 * 
 * @param key
 *          The key of the action, this is an identifier in the map.
 * @param callbackFunction
 *          The function to invoke when the action is executed.
 * @param enabled
 *          A flag to indicate the initial state of the action.
 * @param iconCSSClass
 *          The CSS class for the icon of the action.
 * @param shortcut
 *          The keyboard shortcut to execute the action.
 */
CLASSActionHandler.prototype.addAction = function (key, callbackFunction, enabled, iconCSSClass, shortcut) {
  return this.put(key, new CLASSAction('label', callbackFunction, enabled, iconCSSClass, shortcut));
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
  DOWN_KEY          : 40
};



