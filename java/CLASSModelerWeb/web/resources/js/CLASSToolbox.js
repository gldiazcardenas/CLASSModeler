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
CLASSToolbox = function (editor) {
  this.editor = editor;
  this.graph  = editor.graph;
  
  // Configures the styles
  this.configureStylesheet();
};

/**
 * Initializer method for the CLASS Modeler toolBox.
 */
CLASSToolbox.prototype.init = function (container) {
  this.container = container;
  
  // Add the class component
  this.addTemplate(this.createClassTemplate(), '../../resources/images/warning.png', 'Clase', 'Arrastrar hasta el diagrama para crear una nueva clase');
};

/**
 * Creates the template for Class component.
 * @author Gabriel Leonardo Diaz, 16.10.2013.
 */
CLASSToolbox.prototype.createClassTemplate = function () {
  var clazz = new Class('Clase');
  clazz.isAbstract = true;
  
  var property1 = new Property ('attributeOne');
  property1.visibility = VisibilityKind.PRIVATE;
  property1.type = 'int';
  
  var property2 = new Property ('attributeTwo');
  property2.visibility = VisibilityKind.PRIVATE;
  property2.type = 'String';
  
  var property3 = new Property ('attributeThree');
  property3.visibility = VisibilityKind.PRIVATE;
  property3.type = 'float';
  
  var propertyCell1 = new mxCell (property1, new mxGeometry(0, 0, 160, 30));
  propertyCell1.setVertex(true);
  propertyCell1.setConnectable(false);
  
  var propertyCell2 = new mxCell (property2, new mxGeometry(0, 0, 160, 30));
  propertyCell2.setVertex(true);
  propertyCell2.setConnectable(false);
  
  var propertyCell3 = new mxCell (property3, new mxGeometry(0, 0, 160, 30));
  propertyCell3.setVertex(true);
  propertyCell3.setConnectable(false);
  
  var sectionProperty = new mxCell('Properties', new mxGeometry(0, 0, 160, 60), 'Class');
  sectionProperty.setVertex(true);
  sectionProperty.insert(propertyCell1);
  sectionProperty.insert(propertyCell2);
  
  var sectionOperation = new mxCell('Operations', new mxGeometry(0, 0, 160, 60), 'Class');
  sectionOperation.setVertex(true);
  sectionOperation.insert(propertyCell3);
  
  var clazzCell = new mxCell (clazz, new mxGeometry(0, 0, 160, 180), 'Class');
  clazzCell.setVertex(true);
  clazzCell.insert(sectionProperty);
  clazzCell.insert(sectionOperation);
  
  return clazzCell;
};

/**
 * Add a cell template to the toolbox container element.
 * 
 * @param template
 *          The cell template.
 * @param imageURL
 *          The image used to identify the template
 * @param name
 *          The name of the template
 * @param tooltip
 *          The tooltip of the template
 * @author Gabriel Leonardo Diaz, 17.10.2013.
 */
CLASSToolbox.prototype.addTemplate = function (template, imageURL, name, tooltip) {
  var image = document.createElement('img');
  image.setAttribute('src', imageURL);
  image.style.display = 'block';
  image.style.margin = 'auto';
  image.style.cursor = 'pointer';
  
  var label = document.createElement('span');
  label.innerText = name;
  
  var element = document.createElement('div');
  element.appendChild(image);
  element.appendChild(label);
  element.title = tooltip;
  element.style.textAlign = 'center';
  
  var doDrop = function (graph, evt, overCell) {
    graph.stopEditing(false);
    
    var point  = graph.getPointForEvent(evt);
    var parent = graph.getDefaultParent();
    var model  = graph.getModel();
    
    var newCell = model.cloneCell(template);
    
    model.beginUpdate();
    try {
      newCell.geometry.x = point.x;
      newCell.geometry.y = point.y;
      
      graph.addCell(newCell, parent);
    }
    finally {
      model.endUpdate();
    }
    
    graph.setSelectionCell(newCell);
  };
  
  var dragSource = mxUtils.makeDraggable(image, this.graph, doDrop, image.cloneNode(true));
  dragSource.highlightDropTargets = true;
  dragSource.getDropTarget = function (graph, x, y) {
    if (graph.isSwimlane(template)) {
      return null;
    }
    
    var cell = graph.getCellAt(x, y);
    
    if (graph.isSwimlane(cell)) {
      return cell;
    }
    
    var parent = graph.getModel().getParent(cell);
    
    if (graph.isSwimlane(parent)) {
      return parent;
    }
  };
  
  // Append the image to the container
  this.container.appendChild(element);
};

/**
 * Configure the styles for vertexes and edges.
 */
CLASSToolbox.prototype.configureStylesheet = function () {
  // Default vertex
  var style = new Object();
  style[mxConstants.STYLE_SHAPE]              = mxConstants.SHAPE_RECTANGLE;
  style[mxConstants.STYLE_PERIMETER]          = mxPerimeter.RectanglePerimeter;
  style[mxConstants.STYLE_ALIGN]              = mxConstants.ALIGN_LEFT;
  style[mxConstants.STYLE_VERTICAL_ALIGN]     = mxConstants.ALIGN_MIDDLE;
  style[mxConstants.STYLE_FONTCOLOR]          = '#000000';
  style[mxConstants.STYLE_FONTSIZE]           = '11';
  style[mxConstants.STYLE_FONTSTYLE]          = 0;
  style[mxConstants.STYLE_SPACING_LEFT]       = '4';
  style[mxConstants.STYLE_IMAGE_WIDTH]        = '48';
  style[mxConstants.STYLE_IMAGE_HEIGHT]       = '48';
  this.graph.getStylesheet().putDefaultVertexStyle(style);
  
  // Class
  style = new Object();
  style[mxConstants.STYLE_SHAPE]              = mxConstants.SHAPE_SWIMLANE;
  style[mxConstants.STYLE_PERIMETER]          = mxPerimeter.RectanglePerimeter;
  style[mxConstants.STYLE_ALIGN]              = mxConstants.ALIGN_CENTER;
  style[mxConstants.STYLE_GRADIENTCOLOR]      = '#41B9F5';
  style[mxConstants.STYLE_FILLCOLOR]          = '#8CCDF5';
  style[mxConstants.STYLE_SWIMLANE_FILLCOLOR] = '#ffffff';
  style[mxConstants.STYLE_STROKECOLOR]        = '#1B78C8';
  style[mxConstants.STYLE_FONTCOLOR]          = '#000000';
  style[mxConstants.STYLE_STROKEWIDTH]        = '2';
  style[mxConstants.STYLE_STARTSIZE]          = '28';
  style[mxConstants.STYLE_VERTICAL_ALIGN]     = mxConstants.ALIGN_MIDDLE;
  style[mxConstants.STYLE_FONTSIZE]           = '12';
  style[mxConstants.STYLE_FONTSTYLE]          = 1;
  style[mxConstants.STYLE_SHADOW]             = 1;
  this.graph.getStylesheet().putCellStyle('Class', style);
};

/**
 * Override default layout cell to include child nested swimlanes.
 * 
 * @author Gabriel Leonardo Diaz, 17.10.2013.
 */
mxLayoutManager.prototype.layoutCells = function(cells) {
  if (cells.length > 0) {
    // Invokes the layouts while removing duplicates
    var model = this.getGraph().getModel();
    
    model.beginUpdate();
    try {
      var last = null;
      
      for (var i = 0; i < cells.length; i++) {
        if (cells[i] != model.getRoot() && cells[i] != last) {
          last = cells[i];
          this.executeLayout(this.getLayout(last), last);
          
          if (last.children != null) {
            this.layoutCells(last.children);
          }
        }
      }
      
      this.fireEvent(new mxEventObject(mxEvent.LAYOUT_CELLS, 'cells', cells));
    }
    finally {
      model.endUpdate();
    }
  }
};
