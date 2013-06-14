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
CLASSToolBox.prototype.thumbWidth = 60;

/**
 * Specifies the height of the thumbnails (Component Template).
 */
CLASSToolBox.prototype.thumbHeight = 60;

/**
 * Specifies the delay for the tooltip. Default is 2px.
 */
CLASSToolBox.prototype.thumbBorder = 2;

/**
 * Specifies the padding for the thumbnails. Default is 2px.
 */
CLASSToolBox.prototype.thumbPadding = 2;

/**
 * Shifts the thumbnail by 1px.
 */
CLASSToolBox.prototype.shiftThumbs = mxClient.IS_SVG;

/**
 * Specifies the size of the component titles.
 */
CLASSToolBox.prototype.componentTitleSize = 9;

/**
 * Initializer method for the CLASS Modeler toolBox.
 */
CLASSToolBox.prototype.init = function (container) {
  this.container = container;
  
  //Package
  var packageElement = new mxCell('Package', new mxGeometry(0, 0, 70, 50), 'shape=UMLPackage;spacingTop=10;tabWidth=40;tabHeight=10;tabPosition=left;');
  packageElement.vertex = true;
  this.container.appendChild(this.createVertexTemplateFromCells([packageElement], 70, 50, 'Package'));
  
  // Class
  var classElement = new mxCell('<p style="margin:0px;margin-top:4px;text-align:center;"><b>Class</b></p><hr/><div style="height:2px;"></div>',
                                new mxGeometry(0, 0, 140, 60),
                                'verticalAlign=top;align=left;overflow=fill;fontSize=12;fontFamily=Helvetica;html=1');
  classElement.vertex = true;
  this.container.appendChild(this.createVertexTemplateFromCells([classElement], 140, 60, 'Class'));
  
  // Interface
  var interfaceElement = new mxCell('<p style="margin:0px;margin-top:4px;text-align:center;"><i>&lt;&lt;Interface&gt;&gt;</i><br/><b>Interface</b></p>' +
                                    '<hr/><p style="margin:0px;margin-left:4px;">+ field1: Type<br/>+ field2: Type</p>' +
                                    '<hr/><p style="margin:0px;margin-left:4px;">+ method1(Type): Type<br/>+ method2(Type, Type): Type</p>',
                                    new mxGeometry(0, 0, 190, 140),
                                    'verticalAlign=top;align=left;overflow=fill;fontSize=12;fontFamily=Helvetica;html=1');
  interfaceElement.vertex = true;
  this.container.appendChild(this.createVertexTemplateFromCells([interfaceElement], 190, 140, 'Interface'));
  
 // Enumeration
  var enumerationElement = new mxCell('<p style="margin:0px;margin-top:4px;text-align:center;"><b>Enumeration</b></p><hr/><div style="height:2px;"></div>',
                                      new mxGeometry(0, 0, 140, 60),
                                      'verticalAlign=top;align=left;overflow=fill;fontSize=12;fontFamily=Helvetica;html=1');
  enumerationElement.vertex = true;
  this.container.appendChild(this.createVertexTemplateFromCells([enumerationElement], 140, 60, 'Enumeration'));
  
  // Association
  var associationElement = new mxCell('name', new mxGeometry(0, 0, 0, 0), 'endArrow=block;endFill=1;edgeStyle=orthogonalEdgeStyle;align=left;verticalAlign=top;');
  associationElement.geometry.setTerminalPoint(new mxPoint(0, 0), true);
  associationElement.geometry.setTerminalPoint(new mxPoint(160, 0), false);
  associationElement.geometry.relative = true;
  associationElement.geometry.x = -1;
  associationElement.edge = true;
  
  var sourceLabel = new mxCell('1', new mxGeometry(-1, 0, 0, 0), 'resizable=0;align=left;verticalAlign=bottom;labelBackgroundColor=#ffffff;fontSize=10');
  sourceLabel.geometry.relative = true;
  sourceLabel.setConnectable(false);
  sourceLabel.vertex = true;
  associationElement.insert(sourceLabel);
  this.container.appendChild(this.createEdgeTemplateFromCells([associationElement], 160, 0, 'Relation'));
  
  // Aggregation
  var aggregationElement = new mxCell('1', new mxGeometry(0, 0, 0, 0), 'endArrow=open;endSize=12;startArrow=diamondThin;startSize=14;startFill=0;edgeStyle=orthogonalEdgeStyle;align=left;verticalAlign=bottom;');
  aggregationElement.geometry.setTerminalPoint(new mxPoint(0, 0), true);
  aggregationElement.geometry.setTerminalPoint(new mxPoint(160, 0), false);
  aggregationElement.geometry.relative = true;
  aggregationElement.geometry.x = -1;
  aggregationElement.edge = true;
  
  this.container.appendChild(this.createEdgeTemplateFromCells([aggregationElement], 160, 0, 'Aggregation'));
};

/**
 * Creates a drop handler for inserting the given cells.
 */
CLASSToolBox.prototype.createVertexTemplateFromCells = function(cells, width, height, title) {
  var vertex = this.createItem(cells, title);
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
CLASSToolBox.prototype.createEdgeTemplateFromCells = function(cells, width, height, title) {
  var edge = this.createItem(cells, title);
  this.createDragSource(edge, this.createDropHandler(cells, false), this.createDragPreview(width, height));
  
  // Installs the default edge
  var graph = this.editor.graph;
  mxEvent.addListener(edge, 'click', mxUtils.bind(this, function(evt) {
    if (this.installEdges) {
      graph.setDefaultEdge(cells[0]);
    }
    window.setTimeout(function() { edge.style.backgroundColor = ''; }, 300);
    mxEvent.consume(evt);
  }));
  
  return edge;
};

/**
 * Creates a drag source for the given element.
 */
CLASSToolBox.prototype.createDragSource = function(elt, dropHandler, preview) {
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
CLASSToolBox.prototype.createDropHandler = function(cells, allowSplit) {
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
CLASSToolBox.prototype.createDragPreview = function(width, height) {
  var div = document.createElement('div');
  div.style.border = '1px dashed black';
  div.style.width = width + 'px';
  div.style.height = height + 'px';
  return div;
};

/**
 * Creates and returns a new palette item for the given image.
 */
CLASSToolBox.prototype.createItem = function(cells, title) {
  var item = document.createElement('a');
  item.setAttribute('href', 'javascript:void(0);');
  item.style.display = 'inline-block';
  
  // Blocks default click action
  mxEvent.addListener(item, 'click', function(evt) {
    mxEvent.consume(evt);
  });

  this.createThumb(cells, this.thumbWidth, this.thumbHeight, element, title);
  
  return item;
};

/**
 * Creates a thumbnail for the given cells.
 */
CLASSToolBox.prototype.createThumb = function(cells, width, height, parent, title) {
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
  
  mxImageShape.prototype.preserveImageAspect = prev;

  bounds = this.graph.getGraphBounds();
  var dx = Math.max(0, Math.floor((width - bounds.width) / 2));
  var dy = Math.max(0, Math.floor((height - bounds.height) / 2));
  
  var node = null;
  
  // For supporting HTML labels in IE9 standards mode the container is cloned instead
  if (this.graph.dialect == mxConstants.DIALECT_SVG && !mxClient.IS_IE) {
    var canvas = this.graph.view.getCanvas();
    node = canvas.ownerSVGElement.cloneNode(true);
  }
  else {
    node = this.graph.container.cloneNode(true);
  }
  
  this.graph.getModel().clear();
  
  // Outer dimension is (32, 32)
  var dd = (this.shiftThumbs) ? 2 : 3;
  
  node.style.position = 'relative';
  node.style.overflow = 'hidden';
  node.style.cursor = 'pointer';
  node.style.left = (dx + dd) + 'px';
  node.style.top = (dy + dd) + 'px';
  node.style.width = width + 'px';
  node.style.height = height + 'px';
  node.style.visibility = '';
  node.style.minWidth = '';
  node.style.minHeight = '';
  
  parent.appendChild(node);
  
  //Adds title for the component
  if (title != null) {
    var border = (mxClient.IS_QUIRKS) ? 2 * this.thumbPadding + 2: 0;
    parent.style.height = (this.thumbHeight + border + this.componentTitleSize + 8) + 'px';
      
    var divTitle = document.createElement('div');
    divTitle.style.fontSize = this.componentTitleSize + 'px';
    divTitle.style.textAlign = 'center';
    divTitle.style.whiteSpace = 'nowrap';
      
    if (mxClient.IS_IE) {
      divTitle.style.height = (this.componentTitleSize + 12) + 'px';
    }
    
    divTitle.style.paddingTop = '4px';
    mxUtils.write(divTitle, title);
    parent.appendChild(divTitle);
  }
  
  mxText.prototype.getTableSize = old;
};

