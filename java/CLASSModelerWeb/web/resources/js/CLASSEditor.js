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
  var self = this;
  
  this.graph.convertValueToString = function (cell) {
    return self.convertNodeToString(cell.value);
  };
  
  this.graph.getSecondLabel = function (cell) {
    return self.getSecondLabel(cell.value);
  };
  
  var myGraph = this.graph;
  var createShape = myGraph.cellRenderer.createShape;
  myGraph.cellRenderer.createShape = function(state) {
    createShape.apply(this, arguments);
    
    if (!state.cell.geometry.relative) {
      var secondLabel = myGraph.getSecondLabel(state.cell);
      
      if (secondLabel != null && state.shape != null && state.secondLabel == null) {
        state.secondLabel = new mxText(secondLabel, new mxRectangle(), mxConstants.ALIGN_LEFT, mxConstants.ALIGN_BOTTOM);
        
        // Styles the label
        state.secondLabel.color = 'black';
        state.secondLabel.family = 'Verdana';
        state.secondLabel.size = 8;
        state.secondLabel.fontStyle = mxConstants.FONT_ITALIC;
        state.secondLabel.dialect = state.shape.dialect;
        state.secondLabel.init(state.view.getDrawPane());
      }
    }
  };
  
  var redraw = myGraph.cellRenderer.redraw;
  myGraph.cellRenderer.redraw = function(state) {
    redraw.apply(this, arguments);
    
    if (state.shape != null && state.secondLabel != null) {
      var scale = myGraph.getView().getScale();
      state.secondLabel.value = myGraph.getSecondLabel(state.cell);
      state.secondLabel.scale = scale;
      state.secondLabel.bounds = new mxRectangle(state.x + state.width - 65 * scale, state.y + 12 * scale, 0, 0);
      state.secondLabel.redraw();
    }
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



