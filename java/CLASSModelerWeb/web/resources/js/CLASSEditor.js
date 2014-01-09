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
CLASSEditor = function (config) {
  mxEditor.call(this, config);
};
mxUtils.extend(CLASSEditor, mxEditor);

/**
 * Function called on initializing the mxEditor component.
 * 
 * @author Gabriel Leonardo Diaz, 01.12.2013.
 */
CLASSEditor.prototype.onInit = function () {
  var selfEditor = this;
  var selftGraph = this.graph;
  
  // 1. CELLS RENDERING
  selftGraph.convertValueToString = function (cell) {
    return selfEditor.convertNodeToString(cell.value);
  };
  
  // 2. SECOND LABEL
  // TODO
  
  // 3. POPUP MENU
  selftGraph.panningHandler.factoryMethod = function (menu, cell, evt) {
    return selfEditor.createPopupMenu(menu, cell, evt);
  };
};

/**
 * Provides a suitable name for the XML node template.
 * 
 * @param node
 *          The XML node of the template.
 * @returns The String representation of the node.
 * @author Gabriel Leonardo Diaz, 03.12.2013.
 */
CLASSEditor.prototype.convertNodeToString = function (node) {
  if (this.isClass(node) || this.isEnumeration(node) || this.isInterface(node)) {
    return node.getAttribute("name");
  }
  
  if (this.isPackage(node)) {
    return node.getAttribute("body");
  }
  
  return "";
};

/**
 * Provides a suitable second label for the XML node template.
 * 
 * @param node
 *          The XML node of the template
 * @returns A string representing the second label.
 * @author Gabriel Leonardo Diaz, 04.12.2013.
 */
CLASSEditor.prototype.getSecondLabel = function (node) {
  if (this.isEnumeration(node)) {
    return "<<Enumeration>>";
  }
  
  if (this.isInterface(node)) {
    return "<<Interface>>";
  }
  
  return null;
};

/**
 * Creates the pop-up menu for the given selected cell.
 * 
 * @param menu
 * @param cell
 * @param evt
 * @author Gabriel Leonardo Diaz, 08.01.2014.
 */
CLASSEditor.prototype.createPopupMenu = function (menu, cell, evt) {
  if (cell != null) {
    menu.addItem('Cell Item', 'editors/images/image.gif', function() {
      mxUtils.alert('MenuItem1');
    });
  }
  else {
    menu.addItem('No-Cell Item', 'editors/images/image.gif', function() {
      mxUtils.alert('MenuItem2');
    });
  }
  
  menu.addItem("Propiedades", "/CLASSModeler/resources/images/accept.png", this.getSecondLabel);
  
  menu.addSeparator();
  
  menu.addItem('MenuItem3', '../src/images/warning.gif', function() {
    mxUtils.alert('MenuItem3: '+ this.graph.getSelectionCount() + ' selected');
  });
};

/**
 * 
 * @param node
 * @returns {Boolean}
 */
CLASSEditor.prototype.isClass = function (node) {
  return node.nodeName.toLowerCase() == "class";
};

/**
 * 
 * @param node
 * @returns {Boolean}
 */
CLASSEditor.prototype.isPackage = function (node) {
  return node.nodeName.toLowerCase() == "package";
};

/**
 * 
 * @param node
 * @returns {Boolean}
 */
CLASSEditor.prototype.isEnumeration = function (node) {
  return node.nodeName.toLowerCase() == "enumeration";
};

/**
 * 
 * @param node
 * @returns {Boolean}
 */
CLASSEditor.prototype.isInterface = function (node) {
  return node.nodeName.toLowerCase() == "interface";
};

//selftGraph.getSecondLabel = function (cell) {
//  return selfEditor.getSecondLabel(cell.value);
//};
//
//var createShape = selftGraph.cellRenderer.createShape;
//selftGraph.cellRenderer.createShape = function(state) {
//  createShape.apply(this, arguments);
//  
//  if (!state.cell.geometry.relative) {
//    var secondLabel = selftGraph.getSecondLabel(state.cell);
//    
//    if (secondLabel != null && state.shape != null && state.secondLabel == null) {
//      state.secondLabel = new mxText(secondLabel, new mxRectangle(), mxConstants.ALIGN_LEFT, mxConstants.ALIGN_BOTTOM);
//      
//      // Styles the label
//      state.secondLabel.color = 'black';
//      state.secondLabel.family = 'Verdana';
//      state.secondLabel.size = 8;
//      state.secondLabel.fontStyle = mxConstants.FONT_ITALIC;
//      state.secondLabel.dialect = state.shape.dialect;
//      state.secondLabel.init(state.view.getDrawPane());
//    }
//  }
//};
//
//var redraw = selftGraph.cellRenderer.redraw;
//selftGraph.cellRenderer.redraw = function (state) {
//  redraw.apply(this, arguments);
//  
//  if (state.shape != null && state.secondLabel != null) {
//    var scale = selftGraph.getView().getScale();
//    state.secondLabel.value = selftGraph.getSecondLabel(state.cell);
//    state.secondLabel.scale = scale;
//    state.secondLabel.bounds = new mxRectangle(state.x + state.width - 65 * scale, state.y + 12 * scale, 0, 0);
//    state.secondLabel.redraw();
//  }
//};



