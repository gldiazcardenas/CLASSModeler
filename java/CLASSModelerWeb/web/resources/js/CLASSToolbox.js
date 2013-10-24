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
  this.editor    = editor;
  this.graph     = editor.graph;
  this.metamodel = new CLASSMetamodel();
  
  // Configures the styles
  this.configureStylesheet();
};

/**
 * The instance of CLASSEditor component.
 */
CLASSToolbox.prototype.editor = null;

/**
 * The instance of mxGraph component.
 */
CLASSToolbox.prototype.graph = null;

/**
 * The instance of CLASSMetamodel component.
 */
CLASSToolbox.prototype.metamodel = null;

/**
 * The default height of an element.
 */
CLASSToolbox.prototype.defaultHeight = 20;

/**
 * The default width of an element.
 */
CLASSToolbox.prototype.defaultWidth = 130;

/**
 * Initializer method for the CLASS Modeler toolBox.
 */
CLASSToolbox.prototype.init = function (container) {
  this.container = container;
  
  // Package
  this.addTemplate(this.createPackageTemplate(), '../../resources/images/package_template.png', 'Paquete', 'Arrastrar para agregar un paquete');
  
  // Class
  this.addTemplate(this.createClassTemplate(), '../../resources/images/class_template.png', 'Clase', 'Arrastrar para agregar una clase');
  
  // Interface
  this.addTemplate(this.createInterfaceTemplate(), '../../resources/images/interface_template.png', 'Interfaz', 'Arrastrar para agregar una interfaz');
  
  // Enumeration
  this.addTemplate(this.createEnumerationTemplate(), '../../resources/images/enumeration_template.png', 'Enumeracion', 'Arrastra para agregar una enumeracion');
  
  // Comment
  this.addTemplate(this.createCommentTemplate(), '../../resources/images/comment_template.png', 'Comentario', 'Arrastra para agregar un comentario');
  
  // Composition
  this.addTemplate(this.createCompositionTemplate(), '../../resources/images/composition_template.png', 'Composicion', 'Arrastra para agregar una composicion');
};

/**
 * Creates the template for a Class element.
 * @author Gabriel Leonardo Diaz, 16.10.2013.
 */
CLASSToolbox.prototype.createClassTemplate = function () {
  var property = new mxCell (this.metamodel.createProperty('property'), new mxGeometry(0, 0, this.defaultWidth, this.defaultHeight), 'Feature');
  property.setVertex(true);
  
  var operation = new mxCell (this.metamodel.createOperation('operation'), new mxGeometry(0, 0, this.defaultWidth, this.defaultHeight), 'Feature');
  operation.setVertex(true);
  
  var sectionProperties = new mxCell(this.metamodel.createNamedElement(''), new mxGeometry(0, 0, this.defaultWidth, this.defaultHeight), 'Section');
  sectionProperties.setVertex(true);
  sectionProperties.insert(property);
  
  var sectionOperations = new mxCell(this.metamodel.createNamedElement(''), new mxGeometry(0, 0, this.defaultWidth, this.defaultHeight), 'Section');
  sectionOperations.setVertex(true);
  sectionOperations.insert(operation);
  
  var clazzCell = new mxCell (this.metamodel.createClass('Clase'), new mxGeometry(0, 0, this.defaultWidth, this.defaultHeight * 2), 'Classifier');
  clazzCell.setVertex(true);
  clazzCell.insert(sectionProperties);
  clazzCell.insert(sectionOperations);
  
  return clazzCell;
};

/**
 * Creates the template for an Enumeration element.
 */
CLASSToolbox.prototype.createEnumerationTemplate = function () {
  var literal1 = new mxCell(this.metamodel.createEnumerationLiteral('LITERAL_ONE'), new mxGeometry(0, 0, this.defaultWidth, this.defaultHeight), 'Feature');
  literal1.setVertex(true);
  
  var literal2 = new mxCell(this.metamodel.createEnumerationLiteral('LITERAL_TWO'), new mxGeometry(0, 0, this.defaultWidth, this.defaultHeight), 'Feature');
  literal2.setVertex(true);
  
  var sectionProperties = new mxCell(this.metamodel.createNamedElement(''), new mxGeometry(0, 0, this.defaultWidth, this.defaultHeight), 'Section');
  sectionProperties.setVertex(true);
  sectionProperties.insert(literal1);
  sectionProperties.insert(literal2);
  
  var enumerationCell = new mxCell (this.metamodel.createEnumeration('Enumeracion'), new mxGeometry(0, 0, this.defaultWidth, this.defaultHeight * 2), 'Stereotype');
  enumerationCell.setVertex(true);
  enumerationCell.insert(sectionProperties);
  
  return enumerationCell;
};

/**
 * Creates the template for an Interface element.
 */
CLASSToolbox.prototype.createInterfaceTemplate = function () {
  var operation1 = new mxCell (this.metamodel.createOperation('operation1'), new mxGeometry(0, 0, this.defaultWidth, this.defaultHeight), 'Feature');
  operation1.setVertex(true);
  
  var operation2 = new mxCell (this.metamodel.createOperation('operation2'), new mxGeometry(0, 0, this.defaultWidth, this.defaultHeight), 'Feature');
  operation2.setVertex(true);
  
  var sectionOperations = new mxCell(this.metamodel.createNamedElement(''), new mxGeometry(0, 0, this.defaultWidth, this.defaultHeight), 'Section');
  sectionOperations.setVertex(true);
  sectionOperations.insert(operation1);
  sectionOperations.insert(operation2);
  
  var interfaceCell = new mxCell (this.metamodel.createInterface('Interface'), new mxGeometry(0, 0, this.defaultWidth, this.defaultHeight * 2), 'Stereotype');
  interfaceCell.setVertex(true);
  interfaceCell.insert(sectionOperations);
  
  return interfaceCell;
};

/**
 * Creates the template for an UML package.
 */
CLASSToolbox.prototype.createPackageTemplate = function () {
  var packageCell = new mxCell(this.metamodel.createPackage('MyPackage'), new mxGeometry(0, 0, this.defaultWidth, this.defaultHeight * 4), 'Package');
  packageCell.setVertex(true);
  return packageCell;
};

/**
 * Creates the template for an UML comment.
 */
CLASSToolbox.prototype.createCommentTemplate = function () {
  var commentCell = new mxCell(this.metamodel.createComment('The comment body'), new mxGeometry(0, 0, this.defaultWidth, this.defaultHeight * 4), 'Comment');
  commentCell.setVertex(true);
  
  return commentCell;
};

CLASSToolbox.prototype.createCompositionTemplate = function () {
  var compositionCell = new mxCell(this.metamodel.createAssociation('Association'), new mxGeometry(0, 0, 0, 0), 'Composition');
  compositionCell.setEdge(true);
  compositionCell.geometry.setTerminalPoint(new mxPoint(0, 0), true);
  compositionCell.geometry.setTerminalPoint(new mxPoint(this.defaultWidth, 0), false);
  compositionCell.geometry.relative = true;
  return compositionCell;
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
  
  var label = document.createElement('span');
  label.innerText = name;
  
  var element = document.createElement('div');
  element.appendChild(image);
  element.appendChild(label);
  element.title = tooltip;
  element.className = 'template';
  
  var doDrop = function (graph, evt, overCell) {
    graph.stopEditing(false);
    
    var point   = graph.getPointForEvent(evt);
    var parent  = graph.getDefaultParent();
    var model   = graph.getModel();
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
  var style = new Object();
  style[mxConstants.STYLE_ALIGN]              = mxConstants.ALIGN_LEFT;
  style[mxConstants.STYLE_VERTICAL_ALIGN]     = mxConstants.ALIGN_MIDDLE;
  this.graph.getStylesheet().putDefaultVertexStyle(style);
  
  // Type
  style = new Object();
  style[mxConstants.STYLE_SHAPE]              = mxConstants.SHAPE_RECTANGLE;
  style[mxConstants.STYLE_PERIMETER]          = mxPerimeter.RectanglePerimeter;
  style[mxConstants.STYLE_ALIGN]              = mxConstants.ALIGN_LEFT;
  style[mxConstants.STYLE_VERTICAL_ALIGN]     = mxConstants.ALIGN_MIDDLE;
  style[mxConstants.STYLE_FONTCOLOR]          = '#000000';
  style[mxConstants.STYLE_FONTSIZE]           = '11';
  style[mxConstants.STYLE_FONTSTYLE]          = 0;
  style[mxConstants.STYLE_SPACING_LEFT]       = '2';
  this.graph.getStylesheet().putCellStyle('Feature', style);
  
  // Classifier
  style = new Object();
  style[mxConstants.STYLE_SHAPE]              = mxConstants.SHAPE_SWIMLANE;
  style[mxConstants.STYLE_PERIMETER]          = mxPerimeter.RectanglePerimeter;
  style[mxConstants.STYLE_ALIGN]              = mxConstants.ALIGN_CENTER;
  style[mxConstants.STYLE_VERTICAL_ALIGN]     = mxConstants.ALIGN_MIDDLE;
  style[mxConstants.STYLE_GRADIENTCOLOR]      = '#DDDDDD';
  style[mxConstants.STYLE_FILLCOLOR]          = '#EEEEEE';
  style[mxConstants.STYLE_SWIMLANE_FILLCOLOR] = '#FFFFFF';
  style[mxConstants.STYLE_STROKECOLOR]        = '#BBBBBB';
  style[mxConstants.STYLE_FONTCOLOR]          = '#000000';
  style[mxConstants.STYLE_STROKEWIDTH]        = '1';
  style[mxConstants.STYLE_STARTSIZE]          = '25';
  style[mxConstants.STYLE_FONTSIZE]           = '12';
  style[mxConstants.STYLE_FONTSTYLE]          = 1;
  style[mxConstants.STYLE_SHADOW]             = 1;
  this.graph.getStylesheet().putCellStyle('Classifier', style);
  
  // Stereotype
  style = mxUtils.clone(style);
  style[mxConstants.STYLE_STARTSIZE]          = '35';
  this.graph.getStylesheet().putCellStyle('Stereotype', style);
  
  // Section
  style = new Object();
  style[mxConstants.STYLE_SHAPE]              = mxConstants.SHAPE_SWIMLANE;
  style[mxConstants.STYLE_PERIMETER]          = mxPerimeter.RectanglePerimeter;
  style[mxConstants.STYLE_ALIGN]              = mxConstants.ALIGN_RIGHT;
  style[mxConstants.STYLE_VERTICAL_ALIGN]     = mxConstants.ALIGN_MIDDLE;
  style[mxConstants.STYLE_STROKECOLOR]        = '#BBBBBB';
  style[mxConstants.STYLE_FILLCOLOR]          = '#FFFFFF';
  style[mxConstants.STYLE_STARTSIZE]          = '0';
  style[mxConstants.STYLE_FONTSIZE]           = '10';
  style[mxConstants.STYLE_FONTSTYLE]          = 0;
  style[mxConstants.STYLE_SHADOW]             = 0;
  style[mxConstants.STYLE_STROKEWIDTH]        = '1';
  style[mxConstants.STYLE_SWIMLANE_LINE]      = 0;
  this.graph.getStylesheet().putCellStyle('Section', style);
  
  // Package
  style = new Object();
  style[mxConstants.STYLE_SHAPE]              = ShapePackage.prototype.name;
  style[mxConstants.STYLE_STROKECOLOR]        = '#BBBBBB';
  style[mxConstants.STYLE_FILLCOLOR]          = '#FFFFFF';
  style[mxConstants.STYLE_ALIGN]              = mxConstants.ALIGN_CENTER;
  this.graph.getStylesheet().putCellStyle('Package', style);
  
  // Comment
  style = new Object();
  style[mxConstants.STYLE_SHAPE]              = ShapeComment.prototype.name;
  style[mxConstants.STYLE_STROKECOLOR]        = '#BBBBBB';
  style[mxConstants.STYLE_FILLCOLOR]          = '#FFFFFF';
  style[mxConstants.STYLE_ALIGN]              = mxConstants.ALIGN_CENTER;
  this.graph.getStylesheet().putCellStyle('Comment', style);
  
  // Composition
  style = new Object();
  style[mxConstants.STYLE_EDGE]               = mxConstants.EDGESTYLE_ORTHOGONAL;
  style[mxConstants.STYLE_STARTARROW]         = mxConstants.ARROW_DIAMOND_THIN;
  style[mxConstants.STYLE_STARTSIZE]          = '14';
  style[mxConstants.STYLE_ENDARROW]           = mxConstants.ARROW_OPEN;
  style[mxConstants.STYLE_ENDSIZE]            = '10';
  this.graph.getStylesheet().putCellStyle('Composition', style);
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
