/*******************************************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS 
 * Cúcuta, Colombia 
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
  
  this.clearProperties();
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
    this.configureProperties(cells[0]);
  }
  else {
    this.clearProperties();
  }
};

/**
 * Clears the property grid and removes the edited element.
 * 
 * @author Gabriel Leonardo Diaz, 14.01.2014.
 */
CLASSProperties.prototype.clearProperties = function () {
  this.configureProperties(null);
};

/**
 * Clears the Property Grid by setting empty values and removing the editor.
 * 
 * @param cell
 *          The cell to be edited. If the cell is null the grid is cleared.
 * @author Gabriel Leonardo Diaz, 14.01.2014.
 */
CLASSProperties.prototype.configureProperties = function (cell) {
  var self               = this;
  var selfEditor         = this.editor;
  
  var nameValue          = null;
  var visibilityValue    = null;
  var abstractValue      = null;
  var staticValue        = null;
  var finalValue         = null;
  var specValue          = null;
  var widthValue         = null;
  var heightValue        = null;
  var xValue             = null;
  var yValue             = null;
  
  var nameEditor         = null;
  var abstractEditor     = null;
  var staticEditor       = null;
  var finalEditor        = null;
  var specEditor         = null;
  var spinnerEditor      = null;
  var visibilityEditor   = null;
  var attributesEditor   = null;
  var operationsEditor   = null;
  var relationshipEditor = null;
  
  if (this.isCellEditable(cell)) {
    this.cell       = cell;
    var node        = cell.value;
    
    // Values
    nameValue       = node.getAttribute("name");
    visibilityValue = node.getAttribute("visibility");
    abstractValue   = node.getAttribute("isAbstract");
    staticValue     = node.getAttribute("isStatic");
    finalValue      = node.getAttribute("isFinal");
    specValue       = node.getAttribute("isSpec");
    
    if (this.graph.isNamedElement(node)) {
      nameEditor      = "text";
    }
    
    if (this.graph.isClassifier(node) || this.graph.isFeature(node)) {
      staticEditor     = {"type":"checkbox", "options": {"on":true, "off":false}};
      finalEditor      = {"type":"checkbox", "options": {"on":true, "off":false}};
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
      
      if (this.graph.isClassifier(node)) {
        abstractEditor = {"type":"checkbox", "options": {"on":true, "off":false}};
        specEditor = {"type":"checkbox", "options": {"on":true, "off":false}};
        
        attributesEditor = {"type": "button", "options": {"onclick": function () {
          selfEditor.showAttributes(cell);
        }}};
        
        operationsEditor = {"type":"button", "options": {"onclick": function () {
          selfEditor.showOperations(cell);
        }}};
      }
    }
    
    if (this.graph.isRelationship(node)) {
      relationshipEditor = {"type":"button", "options": {"onclick": function () {
        selfEditor.showRelationship(cell);
      }}};
    }
  }
  else {
    this.cell = null;
  }
  
  var jSonData = [
      // GENERAL
      {"name":"Nombre", "value":nameValue, "group":"General", "editor":nameEditor},
      {"name":"Visibilidad", "value":visibilityValue, "group":"General", "editor":visibilityEditor},
      {"name":"Paquete", "value":visibilityValue, "group":"General", "editor":null},
      
      // ADVANCED
      {"name":"Es Abstracto", "value":abstractValue, "group":"Avanzado", "editor":abstractEditor},
      {"name":"Es Estatico", "value":staticValue, "group":"Avanzado", "editor":staticEditor},
      {"name":"Es Final", "value":finalValue, "group":"Avanzado", "editor":finalEditor},
      {"name":"Es Especificacion", "value":specValue, "group":"Avanzado", "editor":specEditor},
      
      // SPECIFIC
      {"name":"Atributos", "value":null, "group":"Especifico", "editor":attributesEditor},
      {"name":"Operaciones", "value":null, "group":"Especifico", "editor":operationsEditor},
      {"name":"Editar Relacion", "value":null, "group":"Especifico", "editor":relationshipEditor},
      
      // GEOMETRY
      {"name":"Ancho", "value":widthValue, "group":"Geometria", "editor":spinnerEditor},
      {"name":"Alto", "value":heightValue, "group":"Geometria", "editor":spinnerEditor},
      {"name":"X", "value":xValue, "group":"Geometria", "editor":spinnerEditor},
      {"name":"Y", "value":yValue, "group":"Geometria", "editor":spinnerEditor}
  ];
  
  $("#propertyTable").propertygrid({
      data: jSonData,
      showGroup: true,
      showHeader: false,
      scrollbarSize: 0,
      onAfterEdit: function (rowIndex, rowData, changes) {
        self.processChanges(rowIndex, rowData, changes);
      },
      rowStyler: function (idx, row) {
        if (row.editor == null) {
          return "color: #CCCCCC;";
        }
        return "color: #000000;";
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
      this.graph.cellEditProperty(this.cell, "name", changes.value, true);
      break;
      
    case 1: // Visibility
      this.graph.cellEditProperty(this.cell, "visibility", changes.value, true);
      break;
      
    case 2: // Stereotype
      this.graph.cellEditProperty(this.cell, "stereotype", changes.value, true);
      break;
    
    case 5: // Abstract
      this.graph.cellEditProperty(this.cell, "isAbstract", changes.value, true);
      break;
      
    case 6: // Static
      this.graph.cellEditProperty(this.cell, "isStatic", changes.value, true);
      break;
      
    case 7: // Final
      this.graph.cellEditProperty(this.cell, "isFinal", changes.value, true);
      break;
      
    case 8: // Specification
      this.graph.cellEditProperty(this.cell, "isSpec", changes.value, true);
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
        var input = $("<button type='button'>...</button>").appendTo(container);
        input[0].onclick = options.onclick;
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

