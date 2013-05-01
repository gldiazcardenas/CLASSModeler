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
 * Shifts the thumbnail by 1px.
 */
CLASSToolBox.prototype.shiftThumbs = mxClient.IS_SVG;

/**
 * Initializer method for the CLASS Modeler toolBox.
 */
CLASSToolBox.prototype.init = function (container) {
  this.container = container;
  
  // Add class
  var classElement = new mxCell('<p style="margin:0px;margin-top:4px;text-align:center;"><b>Class</b></p><hr/><div style="height:2px;"></div>',
                                new mxGeometry(0, 0, 140, 60),
                                'verticalAlign=top;align=left;overflow=fill;fontSize=12;fontFamily=Helvetica;html=1');
  classElement.vertex = true;
  this.container.appendChild(this.createVertexTemplateFromCells([classElement], 140, 60));
  
  // Add package
  var packageElement = new mxCell('package',
                                  new mxGeometry(0, 0, 70, 50),
                                  'shape=folder;fontStyle=1;spacingTop=10;tabWidth=40;tabHeight=14;tabPosition=left;');
  packageElement.vertex = true;
  this.container.appendChild(this.createVertexTemplateFromCells([packageElement], 70, 50));
};


/**
 * Creates a drop handler for inserting the given cells.
 */
CLASSToolBox.prototype.createVertexTemplateFromCells = function(cells, width, height) {
  var item = this.createItem(cells);
  var dragSource = this.createDragSource(item, this.createDropHandler(cells, true), this.createDragPreview(width, height));

  // Uses guides for vertices only if enabled in graph
  dragSource.isGuidesEnabled = mxUtils.bind(this, function() {
    return this.editor.graph.graphHandler.guidesEnabled;
  });
  
  return item;
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
  var elt = document.createElement('div');
  elt.style.border = '1px dashed black';
  elt.style.width = width + 'px';
  elt.style.height = height + 'px';
  return elt;
};

/**
 * Creates and returns a new palette item for the given image.
 */
CLASSToolBox.prototype.createItem = function(cells) {
  var element = document.createElement('a');
  element.setAttribute('href', 'javascript:void(0);');
  
  // Blocks default click action
  mxEvent.addListener(element, 'click', function(evt) {
    mxEvent.consume(evt);
  });

  this.createThumb(cells, this.thumbWidth, this.thumbHeight, element);
  
  return element;
};

/**
 * Creates a thumbnail for the given cells.
 */
CLASSToolBox.prototype.createThumb = function(cells, width, height, element) {
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
  node.style.overflow = 'visible';
  node.style.cursor = 'pointer';
  node.style.left = (dx + dd) + 'px';
  node.style.top = (dy + dd) + 'px';
  node.style.width = width + 'px';
  node.style.height = height + 'px';
  
  element.appendChild(node);
  mxText.prototype.getTableSize = old;
};

