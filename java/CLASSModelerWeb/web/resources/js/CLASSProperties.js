/*******************************************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS 
 * C�cuta, Colombia 
 * (c) 2014 by UFPS. All rights reserved.
 * 
 * @author: Gabriel Leonardo Diaz, 14.01.2014.
 * 
 ******************************************************************************/

CLASSProperties = function (editor) {
  this.editor = editor;
  this.graph  = editor.graph;
};

/**
 * Instance of the editor component (CLASSEditor).
 */
CLASSProperties.prototype.editor;

/**
 * Instance of the mxGraph cell being edited.
 */
CLASSProperties.prototype.cell;

/**
 * Initializes the property grid component.
 * 
 * @author Gabriel Leonardo Diaz, 14.01.2014.
 */
CLASSProperties.prototype.init = function () {
  var self = this;
  
  this.graph.getSelectionModel().addListener(mxEvent.CHANGE, function(sender, evt) {
    self.selectionChanged(sender, evt);
  });
  
  this.clearGrid();
};

/**
 * Process the selection changed event of the Graph Model.
 * 
 * @param sender
 *          The object sender.
 * @param evt
 *          The event.
 * @author Gabriel Leonardo Diaz, 14.01.2014.
 */
CLASSProperties.prototype.selectionChanged = function (sender, evt) {
  var cells = this.graph.getSelectionCells();
  
  if (cells.length == 1) {
    this.configureGrid(cells[0]);
  }
  else {
    this.clearGrid();
  }
};

/**
 * Clears the property grid and removes the edited element.
 * 
 * @author Gabriel Leonardo Diaz, 14.01.2014.
 */
CLASSProperties.prototype.clearGrid = function () {
  // TODO GD disable table by using styles.
  this.configureGrid(null);
};

/**
 * Clears the Property Grid by setting empty values and removing the editor.
 * 
 * @param cell
 *          The cell to be edited. If the cell is null the grid is cleared.
 * @author Gabriel Leonardo Diaz, 14.01.2014.
 */
CLASSProperties.prototype.configureGrid = function (cell) {
  var self             = this;
  
  var nameValue        = null;
  var visibilityValue  = null;
  var stereotypeValue  = null;
  var attributesValue  = null;
  var operationsValue  = null;
  var abstractValue    = null;
  var rootValue        = null;
  var leafValue        = null;
  var specValue        = null;
  var widthValue       = null;
  var heightValue      = null;
  var xValue           = null;
  var yValue           = null;
  
  var textEditor       = null;
  var booleanEditor    = null;
  var spinnerEditor    = null;
  var visibilityEditor = null;
  var stereotypeEditor = null;
  var attributesEditor = null;
  var operationsEditor = null;
  
  if (this.isCellEditable(cell)) {
    this.cell = cell;
    var node  =  this.cell.value;
    
    // Values
    nameValue       = node.getAttribute("name");
    visibilityValue = node.getAttribute("visibility");
    abstractValue   = node.getAttribute("isAbstract");
    rootValue       = node.getAttribute("isRoot");
    leafValue       = node.getAttribute("isLeaf");
    specValue       = node.getAttribute("isSpec");
    
    // Editors
    textEditor = "text";
    booleanEditor = {"type":"checkbox", "options": {"on":true, "off":false}};
    visibilityEditor = {"type":"combobox", "options": {
      "valueField":"id",
      "textField":"text",
      "panelHeight":"90",
      "data":[
          {"id":"public","text":"public"},
          {"id":"protected","text":"protected"},
          {"id":"package","text":"package"},
          {"id":"private","text":"private"}
      ]
    }};
    
    stereotypeEditor = {"type":"combobox", "options": {
      "valueField":"id",
      "textField":"text",
      "panelHeight":"90"
    }};
    
    attributesEditor     = "button";
    operationsEditor = "button";
  }
  else {
    this.cell = null;
  }
  
  var jSonData = [
      // GENERAL
      {"name":"Nombre", "value":nameValue, "group":"General", "editor":textEditor},
      {"name":"Visibilidad", "value":visibilityValue, "group":"General", "editor":visibilityEditor},
      {"name":"Estereotipo", "value":stereotypeValue, "group":"General", "editor":stereotypeEditor},
      {"name":"Atributos", "value":attributesValue, "group":"General", "editor":attributesEditor},
      {"name":"Metodos", "value":operationsValue, "group":"General", "editor":operationsEditor},
      
      // ADVANCED
      {"name":"Es Abstracto", "value":abstractValue, "group":"Avanzado", "editor":booleanEditor},
      {"name":"Es Raiz", "value":rootValue, "group":"Avanzado", "editor":booleanEditor},
      {"name":"Es Hoja", "value":leafValue, "group":"Avanzado", "editor":booleanEditor},
      {"name":"Es Especificacion", "value":specValue, "group":"Avanzado", "editor":booleanEditor},
      
      // GEOMETRY
      {"name":"Ancho", "value":widthValue, "group":"Geometria", "editor":spinnerEditor},
      {"name":"Alto", "value":heightValue, "group":"Geometria", "editor":spinnerEditor},
      {"name":"X", "value":xValue, "group":"Geometria", "editor":spinnerEditor},
      {"name":"Y", "value":yValue, "group":"Geometria", "editor":spinnerEditor}
  ];
  
  $('#propertyTable').propertygrid({
      data: jSonData,
      showGroup: true,
      showHeader: false,
      scrollbarSize: 0,
      onAfterEdit: function (rowIndex, rowData, changes) {
        self.processChanges(rowIndex, rowData, changes);
      }
  });
};

/**
 * Checks if the given cell is editable.
 * @param cell
 * @returns {Boolean}
 */
CLASSProperties.prototype.isCellEditable = function (cell) {
  return cell != null && cell.value != null;
};

/**
 * Processes the changes after editing a property in the table.
 * 
 * @param rowIndex
 *          The index of the row edited.
 * @param rowData
 *          The initial data of the row.
 * @param changes
 *          The changes made.
 * @author Gabriel Leonardo Diaz, 18.01.2014.
 */
CLASSProperties.prototype.processChanges = function (rowIndex, rowData, changes) {
  if (changes.value) {
    switch (rowIndex) {
    case 0: // Name
      this.graph.cellEditProperty(this.cell, 'name', changes.value, true);
      break;
      
    case 1: // Visibility
      this.graph.cellEditProperty(this.cell, 'visibility', changes.value, true);
      break;
      
    case 2: // Stereotype
      this.graph.cellEditProperty(this.cell, 'stereotype', changes.value, true);
      break;
    
    case 5: // Abstract
      this.graph.cellEditProperty(this.cell, 'isAbstract', changes.value, true);
      break;
      
    case 6: // Root
      this.graph.cellEditProperty(this.cell, 'isRoot', changes.value, true);
      break;
      
    case 7: // Leaf
      this.graph.cellEditProperty(this.cell, 'isLeaf', changes.value, true);
      break;
      
    case 8: // Specification
      this.graph.cellEditProperty(this.cell, 'isSpec', changes.value, true);
      break;
    }
  }
};

/**
 * Defining a button editor for the property sheet.
 * 
 * @author Gabriel Leonardo Diaz, 18.01.2014.
 */
$.extend($.fn.datagrid.defaults.editors, {
  button: {
      init: function (container, options) {
        var input = $('<a href="#" class="easyui-linkbutton">Text Button</a>').appendTo(container);
        return input;
      },
      
      destroy: function (target) {
        $(target).remove();
      },
      
      getValue: function (target) {
        return $(target).val();
      },
      
      setValue: function (target, value) {
        $(target).val(value);
      },
      
      resize: function (target, width) {
        $(target)._outerWidth(width);
      }
  }
});
