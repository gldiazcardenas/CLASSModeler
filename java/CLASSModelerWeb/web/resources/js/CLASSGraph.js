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
  
  this.setConnectable(true);
  this.setDropEnabled(true);
  this.setPanning(true);
  this.setTooltips(!mxClient.IS_TOUCH);
  this.setAllowLoops(true);
  this.allowAutoPanning = true;
};

// Graph inherits from mxGraph
mxUtils.extend(CLASSGraph, mxGraph);

/**
 * Allows to all values in fit.
 */
CLASSGraph.prototype.minFitScale = null;

/**
 * Allows to all values in fit.
 */
CLASSGraph.prototype.maxFitScale = null;

/**
 * Initializer method for the graph.
 */
CLASSGraph.prototype.init = function (container) {
  // call super.init(container)
  mxGraph.prototype.init.call(this, container);
  
  this.setPanning(true);
  this.setTooltips(true);
  this.setConnectable(true);
  this.connectionHandler.setCreateTarget(true);
  
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
  
  // Disables the default popUpMenu, this avoids the default navigator's popUp is showed up.
  mxEvent.disableContextMenu(container);
  
  // Installs the popUpMenu creator
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
  var callbackFunction = function() {
    mxUtils.alert('MenuItem1');
  };
  menu.addItem('MyItem', null, callbackFunction, null, 'ui-icon-gear', true);
};

/**
 * Determines if the cell should be rendered as HTML.
 */
CLASSGraph.prototype.isHtmlLabel = function (cell) {
  var state = this.view.getState(cell);
  var style = state != null ? state.style : this.getCellStyle(cell);
  return style['html'] == '1';
};
