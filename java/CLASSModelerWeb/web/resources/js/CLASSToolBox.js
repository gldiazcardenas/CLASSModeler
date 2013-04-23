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
 * JavaScript CLASS that defines the designer toolBox.
 */
CLASSToolBox = function (editor) {
  this.editor = editor;
};

/**
 * Initializer method for the CLASS Modeler toolBox.
 */
CLASSToolBox.prototype.init = function (container) {
  this.container = container;
  
  content.appendChild(this.createVertexTemplate('', 110, 50, 'Object'));
};

/**
 * Creates a drop handler for inserting the given cells.
 */
CLASSToolBox.prototype.createVertexTemplate = function(style, width, height, value)
{
  var cells = [new mxCell(value != null ? value : '', new mxGeometry(0, 0, width, height), style)];
  cells[0].vertex = true;
  
  return this.createVertexTemplateFromCells(cells, width, height);
};

/**
 * Creates a drop handler for inserting the given cells.
 */
CLASSToolBox.prototype.createVertexTemplateFromCells = function(cells, width, height) {
  var elt = this.createItem(cells);
  var ds = this.createDragSource(elt, this.createDropHandler(cells, true), this.createDragPreview(width, height));
  this.addClickHandler(elt, ds);

  // Uses guides for vertices only if enabled in graph
  ds.isGuidesEnabled = mxUtils.bind(this, function() {
    return this.editor.graph.graphHandler.guidesEnabled;
  });

  // Shows a tooltip with the rendered cell
  if (!touchStyle) {
    mxEvent.addListener(elt, 'mousemove', mxUtils.bind(this, function(evt) {
      this.showTooltip(elt, cells);
    }));
  }
  
  return elt;
};

/**
 * Creates a drop handler for inserting the given cells.
 */
CLASSToolBox.prototype.createEdgeTemplate = function(style, width, height, value) {
  var cells = [new mxCell((value != null) ? value : '', new mxGeometry(0, 0, width, height), style)];
  cells[0].geometry.setTerminalPoint(new mxPoint(0, height), true);
  cells[0].geometry.setTerminalPoint(new mxPoint(width, 0), false);
  cells[0].edge = true;
  return this.createEdgeTemplateFromCells(cells, width, height);
};

/**
 * Creates a drop handler for inserting the given cells.
 */
CLASSToolBox.prototype.createEdgeTemplateFromCells = function(cells, width, height) {
  var elt = this.createItem(cells);
  this.createDragSource(elt, this.createDropHandler(cells, false), this.createDragPreview(width, height));

  // Installs the default edge
  var graph = this.editor.graph;
  mxEvent.addListener(elt, 'click', mxUtils.bind(this, function(evt) {
    if (this.installEdges) {
      // Uses edge template for connect preview
      graph.connectionHandler.createEdgeState = function(me) {
        return graph.view.createState(cells[0]);
      };
  
      // Creates new connections from edge template
      graph.connectionHandler.factoryMethod = function() {
        return graph.cloneCells([cells[0]])[0];
      };
    }
    
    // Highlights the entry for 200ms
    elt.style.backgroundColor = '#ffffff';
    
    window.setTimeout(function() {
      elt.style.backgroundColor = '';
    }, 200);
      
      mxEvent.consume(evt);
  }));

  // Shows a tooltip with the rendered cell
  if (!touchStyle) {
    mxEvent.addListener(elt, 'mousemove', mxUtils.bind(this, function(evt) {
      this.showTooltip(elt, cells);
    }));
  }
  
  return elt;
};

