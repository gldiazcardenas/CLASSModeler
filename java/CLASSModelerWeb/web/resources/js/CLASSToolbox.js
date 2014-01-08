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
};

/**
 * The instance of mxGraph component.
 */
CLASSToolbox.prototype.graph;


/**
 * The CSS style for selected toolBox items.
 */
CLASSToolbox.prototype.selectedStyle = "selected";

/**
 * The CSS style for all toolBox items.
 */
CLASSToolbox.prototype.toolboxItemStyle = "toolbox_item";

/**
 * The CSS style for all draggable items in the toolBox.
 */
CLASSToolbox.prototype.draggableItemStyle = "draggable";

/**
 * Initializer method for the CLASS Modeler toolBox.
 */
CLASSToolbox.prototype.init = function (container) {
  this.container = container;
  
  // Configure drag object
  var draggableItems = document.getElementsByClassName("draggable");
  for (var i = 0; i < draggableItems.length; i++) {
    this.configureDnD(draggableItems[i], this.editor.getTemplate(draggableItems[i].id));
  }
  
  // Configure selection
  var toolboxItems = document.getElementsByClassName(this.toolboxItemStyle);
  for (var i = 0; i < toolboxItems.length; i++) {
    this.configureSelection(toolboxItems[i]);
  }
};

/**
 * Add a cell template to the ToolBox container element.
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
CLASSToolbox.prototype.configureDnD = function (draggableItem, componentDrop) {
  // Drop handler
  var doDrop = function (graph, evt, overCell) {
    graph.stopEditing(false);
    
    var point   = graph.getPointForEvent(evt);
    var parent  = graph.getDefaultParent();
    var model   = graph.getModel();
    var newCell = model.cloneCell(componentDrop);
    
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
  
  // Drag handler
  var dragSource = mxUtils.makeDraggable(draggableItem, this.graph, doDrop, draggableItem.cloneNode(true));
  dragSource.highlightDropTargets = true;
  dragSource.getDropTarget = function (graph, x, y) {
    if (graph.isSwimlane(componentDrop)) {
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
};

/**
 * Configures the selection listener of the toolBox items.
 * 
 * @param toolboxItem
 *          The toolBox item to configure.
 * @author Gabriel Leonardo Diaz, 30.11.2013.
 */
CLASSToolbox.prototype.configureSelection = function (toolboxItem) {
  var self= this;
  toolboxItem.onclick = function () {
    if (self.isSelected(toolboxItem)) {
      self.removeSelection(toolboxItem);
    }
    else {
      self.addSelection(toolboxItem);
    }
  };
};

/**
 * Allows to mark the given toolbox item as selected. This removes the selection from all other
 * items.
 * 
 * @param toolboxItem
 *          The item to be selected.
 * @author Gabriel Leonardo Diaz, 01.12.2013.
 */
CLASSToolbox.prototype.addSelection = function (toolboxItem) {
  var toolboxItems = document.getElementsByClassName(this.toolboxItemStyle);
  for (var i = 0; i < toolboxItems.length; i++) {
    if (toolboxItem != toolboxItems[i]) {
      this.removeSelection (toolboxItems[i]);
    }
  }
  
  toolboxItem.className = toolboxItem.className + " " + this.selectedStyle;
};

/**
 * Allows to remove the selection on the given item
 * 
 * @param toolboxItem
 *          The item to be unselected.
 * @author Gabriel Leonardo Diaz, 01.12.2013.
 */
CLASSToolbox.prototype.removeSelection = function (toolboxItem) {
  toolboxItem.className = toolboxItem.className.replace(" " + this.selectedStyle, "");
};

/**
 * Allows to know whether the given item is selected or not.
 * 
 * @param toolboxItem
 *          The item to be evaluated.
 * @author Gabriel Leonardo Diaz, 01.12.2013.
 */
CLASSToolbox.prototype.isSelected = function (toolboxItem) {
  return toolboxItem.className != null && toolboxItem.className.indexOf(this.selectedStyle) != -1;
};
