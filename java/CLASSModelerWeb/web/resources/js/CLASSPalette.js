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
CLASSPalette = function (editor) {
  this.editor = editor;
  
  this.graph = new CLASSGraph(document.createElement('div'), null, null, this.editor.graph.getStylesheet());
  this.graph.foldingEnabled = false;
  this.graph.autoScroll = false;
  this.graph.setTooltips(false);
  this.graph.setConnectable(false);
  this.graph.resetViewOnRootChange = false;
  this.graph.view.setTranslate(this.thumbBorder, this.thumbBorder);
  this.graph.setEnabled(false);
  
  // Workaround for no rendering in 0 coordinate in FF 10
  if (this.shiftThumbs) {
    this.graph.view.canvas.setAttribute('transform', 'translate(1, 1)');
  }
};

/**
 * Specifies the width of the thumbnails (Component Template).
 */
CLASSPalette.prototype.thumbWidth = 50;

/**
 * Specifies the height of the vertex (Component Template).
 */
CLASSPalette.prototype.thumbHeightVertex = 40;

/**
 * Specifies the height of the edge (Component Template).
 */
CLASSPalette.prototype.thumbHeightEdge = 30;

/**
 * Specifies the delay for the tooltip. Default is 2px.
 */
CLASSPalette.prototype.thumbBorder = 2;

/**
 * Specifies the padding for the thumbnails. Default is 2px.
 */
CLASSPalette.prototype.thumbPadding = 2;

/**
 * Shifts the thumbnail by 1px.
 */
CLASSPalette.prototype.shiftThumbs = mxClient.IS_SVG;

/**
 * Specifies the size of the component titles.
 */
CLASSPalette.prototype.componentTitleSize = 9;

/**
 * Initializer method for the CLASS Modeler toolBox.
 */
CLASSPalette.prototype.init = function (container) {
  this.container = container;
  
 // Enumeration
  var enumerationElement = new mxCell('<p style="margin:0px;margin-top:4px;text-align:center;"><i>&lt;&lt;Enumeration&gt;&gt;</i><br/><b>Enumeration</b></p><hr/>' +
                                      '<p style="margin:0px;margin-left:4px;">+ field: Type</p>',
                                      new mxGeometry(0, 0, 160, 70),
                                      'align=left;overflow=fill;html=1');
  enumerationElement.vertex = true;
  this.container.appendChild(this.createVertexTemplateFromCells([enumerationElement], 160, 70, 'Enumeration'));
  
  // Class
  var classElement = new mxCell('<p style="margin: 4px 0px 0px 0px; text-align:center;"><b>Class</b></p><hr/>' +
                                '<p style="margin:0px 0px 0px 4px;">+ field: Type</p><hr/>' +
                                '<p style="margin:0px 0px 0px 4px;">+ method(): Type</p>',
                                new mxGeometry(0, 0, 160, 80),
                                'align=left;overflow=fill;html=1');
  classElement.vertex = true;
  this.container.appendChild(this.createVertexTemplateFromCells([classElement], 160, 80, 'Class'));
  
  // Interface
  var interfaceElement = new mxCell('<p style="margin:0px;margin-top:4px;text-align:center;"><i>&lt;&lt;Interface&gt;&gt;</i><br/><b>Interface</b></p><hr/>' +
                                    '<p style="margin:0px 0px 0px 4px;">+ field: Type</p><hr/>' +
                                    '<p style="margin:0px 0px 0px 4px;">+ method(): Type</p>',
                                    new mxGeometry(0, 0, 160, 70),
                                    'align=left;overflow=fill;html=1');
  interfaceElement.vertex = true;
  this.container.appendChild(this.createVertexTemplateFromCells([interfaceElement], 160, 70, 'Interface'));
  
  // Package
  var packageElement = new mxCell('Package',
                                  new mxGeometry(0, 0, 100, 70),
                                  'shape=UMLPackage;spacingTop=10;');
  packageElement.vertex = true;
  this.container.appendChild(this.createVertexTemplateFromCells([packageElement], 100, 70, 'Package'));
  
  
  // Note
  var noteElement = new mxCell('Note', new mxGeometry(0, 0, 60, 70),'shape=UMLNote');
  noteElement.vertex = true;
  this.container.appendChild(this.createVertexTemplateFromCells([noteElement], 60, 70, 'Note'));
  
  // Association
  var associationElement = new mxCell('', new mxGeometry(0, 0, 0, 0), 'endArrow=none;edgeStyle=orthogonalEdgeStyle;');
  associationElement.geometry.setTerminalPoint(new mxPoint(0, 0), true);
  associationElement.geometry.setTerminalPoint(new mxPoint(140, 0), false);
  associationElement.edge = true;
  
  var sourceLabel = new mxCell('parent', new mxGeometry(-1, 0, 0, 0), 'resizable=0;align=left;verticalAlign=bottom');
  sourceLabel.geometry.relative = true;
  sourceLabel.setConnectable(false);
  sourceLabel.vertex = true;
  associationElement.insert(sourceLabel);

  var targetLabel = new mxCell('child', new mxGeometry(1, 0, 0, 0), 'resizable=0;align=right;verticalAlign=bottom');
  targetLabel.geometry.relative = true;
  targetLabel.setConnectable(false);
  targetLabel.vertex = true;
  associationElement.insert(targetLabel);
  this.container.appendChild(this.createEdgeTemplateFromCells([associationElement], 140, 0, 'Association', true));
  
  // Aggregation
  var aggregationElement = new mxCell('', new mxGeometry(0, 0, 0, 0), 'endArrow=diamondThin;endSize=16;endFill=0;startArrow=open;startSize=12;edgeStyle=orthogonalEdgeStyle;');
  aggregationElement.geometry.setTerminalPoint(new mxPoint(0, 0), true);
  aggregationElement.geometry.setTerminalPoint(new mxPoint(140, 0), false);
  aggregationElement.geometry.relative = true;
  aggregationElement.geometry.x = -1;
  aggregationElement.edge = true;
  this.container.appendChild(this.createEdgeTemplateFromCells([aggregationElement], 140, 0, 'Aggregation'));
  
  // Composition
  var compositionElement = new mxCell('', new mxGeometry(0, 0, 0, 0), 'endArrow=diamondThin;endSize=16;endFill=1;startArrow=open;startSize=12;edgeStyle=orthogonalEdgeStyle;');
  compositionElement.geometry.setTerminalPoint(new mxPoint(0, 0), true);
  compositionElement.geometry.setTerminalPoint(new mxPoint(140, 0), false);
  compositionElement.edge = true;
  
  this.container.appendChild(this.createEdgeTemplateFromCells([compositionElement], 140, 0, 'Composition'));
  
  // Generalization
  var generalizationElement = new mxCell('', new mxGeometry(0, 0, 0, 0), 'endArrow=block;endFill=0;endSize=16;startArrow=none;dashed=0');
  generalizationElement.geometry.setTerminalPoint(new mxPoint(0, 0), true);
  generalizationElement.geometry.setTerminalPoint(new mxPoint(140, 0), false);
  generalizationElement.edge = true;
  this.container.appendChild(this.createEdgeTemplateFromCells([generalizationElement], 140, 0, 'Generalization'));
  
 // Realization
  var realizationElement = new mxCell('', new mxGeometry(0, 0, 0, 0), 'endArrow=block;endFill=0;endSize=16;startArrow=none;dashed=1');
  realizationElement.geometry.setTerminalPoint(new mxPoint(0, 0), true);
  realizationElement.geometry.setTerminalPoint(new mxPoint(140, 0), false);
  realizationElement.edge = true;
  this.container.appendChild(this.createEdgeTemplateFromCells([realizationElement], 140, 0, 'Realization'));
  
  // Note Link
  var noteLinkElement = new mxCell('', new mxGeometry(0, 0, 0, 0), 'endArrow=none;startArrow=none;edgeStyle=orthogonalEdgeStyle;dashed=1');
  noteLinkElement.geometry.setTerminalPoint(new mxPoint(0, 0), true);
  noteLinkElement.geometry.setTerminalPoint(new mxPoint(140, 0), false);
  noteLinkElement.edge = true;
  this.container.appendChild(this.createEdgeTemplateFromCells([noteLinkElement], 140, 0, 'Note Link'));
};

/**
 * Creates a drop handler for inserting the given cells.
 */
CLASSPalette.prototype.createVertexTemplateFromCells = function(cells, width, height, title) {
  var vertex = this.createItem(cells, title, true);
  var dragSource = this.createDragSource(vertex, this.createDropHandler(cells, true), this.createDragPreview(width, height));

  // Uses guides for vertices only if enabled in graph
  dragSource.isGuidesEnabled = mxUtils.bind(this, function() {
    return this.editor.graph.graphHandler.guidesEnabled;
  });
  
  return vertex;
};

/**
 * Creates a drop handler for inserting the given cells.
 */
CLASSPalette.prototype.createEdgeTemplateFromCells = function(cells, width, height, title) {
  var edge = this.createItem(cells, title, false);
  this.createDragSource(edge, this.createDropHandler(cells, false), this.createDragPreview(width, height));
  return edge;
};

/**
 * Creates a drag source for the given element.
 */
CLASSPalette.prototype.createDragSource = function(elt, dropHandler, preview) {
  var dragSource = mxUtils.makeDraggable(elt, this.editor.graph, dropHandler, preview, 0, 0, this.editor.graph.autoscroll, true, true);

  // Allows drop into cell only if target is a valid root
  dragSource.getDropTarget = function(graph, x, y) {
    var target = mxDragSource.prototype.getDropTarget.apply(this, arguments);
    
    if (!graph.isValidRoot(target)) {
      target = null;
    }
    
    return target;
  };
  return dragSource;
};

/**
 * Creates a drop handler for inserting the given cells.
 */
CLASSPalette.prototype.createDropHandler = function(cells, allowSplit) {
  return function (graph, evt, target, x, y) {
    cells = graph.getImportableCells(cells);
    
    if (cells.length > 0) {
      var validDropTarget = target != null ? graph.isValidDropTarget(target, cells, evt) : false;
      var select = null;
      
      if (target != null && !validDropTarget) {
        target = null;
      }
      
      // Splits the target edge or inserts into target group
      if (allowSplit && graph.isSplitEnabled() && graph.isSplitTarget(target, cells, evt)) {
        graph.splitEdge(target, cells, null, x, y);
        select = cells;
      }
      else if (cells.length > 0) {
        select = graph.importCells(cells, x, y, target);
      }
      
      if (select != null && select.length > 0) {
        graph.scrollCellToVisible(select[0]);
        graph.setSelectionCells(select);
      }
    }
  };
};

/**
 * Creates and returns a preview element for the given width and height.
 */
CLASSPalette.prototype.createDragPreview = function(width, height) {
  var div = document.createElement('div');
  div.style.border = '1px dashed black';
  div.style.width  = width + 'px';
  div.style.height = height + 'px';
  return div;
};

/**
 * Creates and returns a new palette item for the given image.
 */
CLASSPalette.prototype.createItem = function(cells, title, vertex) {
  var table   = document.createElement('table');
  var tbody   = document.createElement('tbody');
  var tr      = document.createElement('tr');
  var tdImage = document.createElement('td');
  var tdText  = document.createElement('td');
  
  table.appendChild(tbody);
  table.width = '100%';
  table.cellspacing = '0';
  table.cellpadding = '0';
  
  tbody.appendChild(tr);
  tr.appendChild(tdImage);
  tr.appendChild(tdText);

  if (vertex) {
    this.createThumb(cells, this.thumbWidth, this.thumbHeightVertex, tdImage, tdText, title);
  }
  else {
    this.createThumb(cells, this.thumbWidth, this.thumbHeightEdge, tdImage, tdText, title);
  }
  
  return table;
};

/**
 * Creates a thumbnail for the given cells.
 */
CLASSPalette.prototype.createThumb = function(cells, width, height, tdImage, tdText, title) {
  // Workaround for off-screen text rendering in IE
  var old = mxText.prototype.getTableSize;
  
  if (this.graph.dialect != mxConstants.DIALECT_SVG) {
    mxText.prototype.getTableSize = function(table) {
      var oldParent = table.parentNode;
      document.body.appendChild(table);
      var size = new mxRectangle(0, 0, table.offsetWidth, table.offsetHeight);
      oldParent.appendChild(table);
      return size;
    };
  }
  
  var prev = mxImageShape.prototype.preserveImageAspect;
  mxImageShape.prototype.preserveImageAspect = false;
  
  this.graph.view.rendering = false;
  this.graph.view.setScale(1);
  this.graph.addCells(cells);
  
  var bounds = this.graph.getGraphBounds();
  var corr = (this.shiftThumbs) ? this.thumbBorder + 1 : this.thumbBorder;
  var s = Math.min((width - 1) / (bounds.x + bounds.width + corr), (height - 1) / (bounds.y + bounds.height + corr));
  
  this.graph.view.setScale(s);
  this.graph.view.rendering = true;
  this.graph.refresh();
  
  bounds = this.graph.getGraphBounds();
  var dy = (height - bounds.height) / 2;
  
  mxImageShape.prototype.preserveImageAspect = prev;
  
  // For supporting HTML labels in IE9 standards mode the container is cloned instead
  var nodeImage = null;
  if (this.graph.dialect == mxConstants.DIALECT_SVG && !mxClient.IS_IE) {
    nodeImage = this.graph.view.getCanvas().ownerSVGElement.cloneNode(true);
  }
  else {
    nodeImage = this.graph.container.cloneNode(true);
  }
  
  this.graph.getModel().clear();
  
  // Adds the component image
  nodeImage.style.position   = 'relative';
  nodeImage.style.overflow   = 'hidden';
  nodeImage.style.cursor     = 'pointer';
  nodeImage.style.width      = width + 'px';
  nodeImage.style.height     = height + 'px';
  nodeImage.style.top        = dy + 'px';
  nodeImage.style.visibility = '';
  nodeImage.style.minWidth   = '';
  nodeImage.style.minHeight  = '';
  tdImage.appendChild(nodeImage);
  tdImage.style.width        = (width + 10) + 'px';
  
  //Adds the component title
  mxUtils.write(tdText, title);
  
  mxText.prototype.getTableSize = old;
};

