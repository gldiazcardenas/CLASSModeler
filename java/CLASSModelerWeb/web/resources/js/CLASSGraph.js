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
 * JavaScript Class for the graph component, this inherits from mxGraph.
 */
CLASSGraph = function (container, model, renderHint, stylesheet) {
  // Call super
  mxGraph.call(this, container, model, renderHint, stylesheet);
};

// Graph inherits from mxGraph
mxUtils.extend(CLASSGraph, mxGraph);

/**
 * Initializer method for the graph.
 */
CLASSGraph.prototype.init = function (container) {
  // call super.init(container)
  mxGraph.prototype.init.call(this, container);
  
  this.setPanning(true);
  this.setTooltips(true);
  this.setConnectable(true);
  
  // Install the lasso feature
  var rubberband = new mxRubberband(this);
  this.getRubberband = function() {
    return rubberband;
  };
  
  // Install a key handler
  var keyHandler = new mxKeyHandler(this);
  this.getKeyHandler = function() {
    return keyHandler;
  };
  
  // Install the popUpMenu creator, and disables the default popUpMenu
  mxEvent.disableContextMenu(container);
  this.panningHandler.factoryMethod = function (menu, cell, evt) {
    return menu.graph.createPopupMenu(menu, cell, evt);
  };
};

/**
 * Method that creates the popUp menu after RBM Click.
 * 
 * @param menu
 *          The menu to append the items.
 * @param cell
 *          The cell clicked.
 * @param evt
 *          The mouse event.
 */
CLASSGraph.prototype.createPopupMenu = function (menu, cell, evt) {
  menu.addItem('MyItem', null, function() {
    mxUtils.alert('MenuItem1');
  }, null, 'ui-icon-gear', true);
};
