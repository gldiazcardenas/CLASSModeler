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
 * Map that stores the quantity of instances created by object kind. Allows to adjust the 
 */
CLASSToolbox.prototype.instanceQuantity = {};

/**
 * Initializer method for the CLASS Modeler toolBox.
 */
CLASSToolbox.prototype.init = function (container) {
  this.container = container;
  
  // Configure drag object
  var draggableItems = document.getElementsByClassName(this.draggableItemStyle);
  for (var i = 0; i < draggableItems.length; i++) {
    this.configureDnD(draggableItems[i], this.editor.getTemplate(draggableItems[i].id));
  }
  
  // Configure selection
  var toolboxItems = document.getElementsByClassName(this.toolboxItemStyle);
  for (var i = 0; i < toolboxItems.length; i++) {
    this.configureSelection(toolboxItems[i]);
  }
  
  // Initialize the instances quantity
  this.instanceQuantity["class"] = 0;
  this.instanceQuantity["interface"] = 0;
  this.instanceQuantity["enumeration"] = 0;
  this.instanceQuantity["package"] = 0;
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
CLASSToolbox.prototype.configureDnD = function (draggableItem, element) {
  var self = this;
  
  // Drop handler
  var doDrop = function (graph, evt, overCell) {
    if (!graph.isEnabled()) {
      return;
    }
    
    graph.stopEditing(true);
    
    var model = graph.getModel();
    model.beginUpdate();
    
    try {
      var point   = graph.getPointForEvent(evt);
      var parent  = graph.getDefaultParent();
      
      var newCell = model.cloneCell(element);
      
      newCell.geometry.x = point.x;
      newCell.geometry.y = point.y;
      
      // CLASS sections
      if (graph.isClass(newCell.value)) {
        var attrSection = model.cloneCell(self.editor.getTemplate("section"));
        attrSection.setAttribute("attribute", "1");
        attrSection.setVertex(true);
        
        var operSection = model.cloneCell(self.editor.getTemplate("section"));
        operSection.setAttribute("attribute", "0");
        operSection.setVertex(true);
        
        newCell.insert(attrSection);
        newCell.insert(operSection);
        
        // Adjust the name
        var nameNumber = self.instanceQuantity["class"] + 1;
        var uniqueName = graph.getUniqueName(newCell.getAttribute("name") + nameNumber);
        newCell.setAttribute("name", uniqueName);
        self.instanceQuantity["class"] = nameNumber;
      }
      
      // INTERFACE sections
      else if (graph.isInterface(newCell.value)) {
        var attrSection = model.cloneCell(self.editor.getTemplate("section"));
        attrSection.setAttribute("attribute", "0");
        attrSection.setVertex(true);
        
        newCell.insert(attrSection);
        
        // Adjust the name
        var nameNumber = self.instanceQuantity["interface"] + 1;
        var uniqueName = graph.getUniqueName(newCell.getAttribute("name") + nameNumber);
        newCell.setAttribute("name", uniqueName);
        self.instanceQuantity["interface"] = nameNumber;
      }
      
      // ENUMERATION sections
      else if (graph.isEnumeration(newCell.value)) {
        var attrSection = model.cloneCell(self.editor.getTemplate("section"));
        attrSection.setAttribute("attribute", "1");
        attrSection.setVertex(true);
        
        newCell.insert(attrSection);
        
        // Adjust the name
        var nameNumber = self.instanceQuantity["enumeration"] + 1;
        var uniqueName = graph.getUniqueName(newCell.getAttribute("name") + nameNumber);
        newCell.setAttribute("name", uniqueName);
        self.instanceQuantity["enumeration"] = nameNumber;
      }
      else if (graph.isPackage(newCell.value)) {
        // Adjust the name
        var nameNumber = self.instanceQuantity["package"] + 1;
        newCell.setAttribute("name", newCell.getAttribute("name") + nameNumber);
        self.instanceQuantity["package"] = nameNumber;
      }
      
      graph.addCell(newCell, parent);
      graph.cellSizeUpdated(newCell, false);
      graph.adjustStyleCell(newCell);
      graph.setSelectionCell(newCell);
    }
    finally {
      model.endUpdate();
    }
  };
  
  // Drag handler
  var dragSource = mxUtils.makeDraggable(draggableItem, this.graph, doDrop, draggableItem.cloneNode(true));
  dragSource.highlightDropTargets = true;
  dragSource.getDropTarget = function (graph, x, y) {
    if (graph.isSwimlane(element)) {
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
  var cellTemplate = this.editor.getTemplate(toolboxItem.id);
  if (cellTemplate && cellTemplate.isEdge()) {
    this.editor.defaultEdge = cellTemplate;
  }
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
  this.editor.defaultEdge = this.editor.getTemplate("association");
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
