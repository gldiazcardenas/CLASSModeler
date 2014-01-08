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
  this.addTemplate(this.createPackageTemplate(), '../../resources/images/uml_package.png', 'Paquete', 'Arrastrar para agregar un paquete');
  
  // Class
  this.addTemplate(this.createClassTemplate(), '../../resources/images/uml_class.png', 'Clase', 'Arrastrar para agregar una clase');
  
  // Interface
  this.addTemplate(this.createInterfaceTemplate(), '../../resources/images/uml_interface.png', 'Interfaz', 'Arrastrar para agregar una interfaz');
  
  // Enumeration
  this.addTemplate(this.createEnumerationTemplate(), '../../resources/images/uml_enumeration.png', 'Enumeracion', 'Arrastra para agregar una enumeracion');
  
  // Comment
  this.addTemplate(this.createCommentTemplate(), '../../resources/images/uml_comment.png', 'Comentario', 'Arrastra para agregar un comentario');
  
  // Composition
  this.addTemplate(this.createCompositionTemplate(), '../../resources/images/uml_composition.png', 'Composicion', 'Arrastra para agregar una composicion');
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

/*******************************************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS 
 * Cúcuta, Colombia 
 * (c) 2013 by UFPS. All rights reserved.
 * 
 * @author: Gabriel Leonardo Diaz, 01.05.2013.
 * 
 ******************************************************************************/

/**
 * Defines the shape for UML package element.
 * 
 * @author Gabriel Leonardo Diaz, 01.05.2013.
 */
ShapePackage = function () {};
ShapePackage.prototype = new mxCylinder();
ShapePackage.prototype.constructor = ShapePackage;
ShapePackage.prototype.name        = 'ShapePackage';
ShapePackage.prototype.tabPosition = 'left';
ShapePackage.prototype.redrawPath  = function (path, x, y, w, h, isForeground) {
  
  var tp = mxUtils.getValue(this.style, 'tabPosition', this.tabPosition);
  var tw = w * 0.50;
  var th = h * 0.20;
  
  if (th > 20) {
    th = 20; // Not more than 20px.
  }
  
  if (tw > 80) {
    tw = 80; // Not more than 80px.
  }
  
  var dx = Math.min(w, tw);
  var dy = Math.min(h, th);

  if (isForeground) {
    if (tp == 'left') {
      path.moveTo(0, dy);
      path.lineTo(dx, dy);
    }
    // Right is default
    else {
      path.moveTo(w - dx, dy);
      path.lineTo(w, dy);
    }
    
    path.end();
  }
  else {
    if (tp == 'left') {
      path.moveTo(0, 0);
      path.lineTo(dx, 0);
      path.lineTo(dx, dy);
      path.lineTo(w, dy);
    }
    // Right is default
    else {
      path.moveTo(0, dy);
      path.lineTo(w - dx, dy);
      path.lineTo(w - dx, 0);
      path.lineTo(w, 0);
    }
    
    path.lineTo(w, h);
    path.lineTo(0, h);
    path.lineTo(0, dy);
    path.close();
    path.end();
  }
};
mxCellRenderer.prototype.defaultShapes['ShapePackage'] = ShapePackage;

/**
 * Defines the shape for a common UML comment appended to any element.
 * 
 * @returns {ShapeNote} The ShapeNote created.
 * @author Gabriel Leonardo Diaz, 16.06.2013.
 */
ShapeComment = function () {};
ShapeComment.prototype = new mxCylinder();
ShapeComment.prototype.constructor = ShapeComment;
ShapeComment.prototype.name        = 'ShapeComment';
ShapeComment.prototype.redrawPath  = function (path, x, y, w, h, isForeground) {
  
  var s = w * 0.20;
  if (s > 15) {
    s = 15; // No more than 15px.
  }
  
  this.size = s;
  
  if (isForeground) {
    path.moveTo(w - s, 0);
    path.lineTo(w - s, s);
    path.lineTo(w, s);
    path.end();
  }
  else {
    path.moveTo(0, 0);
    path.lineTo(w - s, 0);
    path.lineTo(w, s);
    path.lineTo(w, h);
    path.lineTo(0, h);
    path.lineTo(0, 0);
    path.close();
    path.end();
  }
};
mxCellRenderer.prototype.defaultShapes['ShapeComment'] = ShapeComment;
