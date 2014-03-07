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
  var packageValue       = null;
  var abstractValue      = null;
  var staticValue        = null;
  var finalValue         = null;
  
  var nameEditor         = null;
  var abstractEditor     = null;
  var staticEditor       = null;
  var finalEditor        = null;
  var visibilityEditor   = null;
  var attributesEditor   = null;
  var operationsEditor   = null;
  var relationshipEditor = null;
  var packageEditor      = null;
  var attributesName     = "Atributos";
  
  if (this.isCellEditable(cell)) {
    this.cell       = cell;
    var node        = cell.value;
    
    if (this.graph.isNamedElement(node)) {
      nameValue  = node.getAttribute("name");
      nameEditor = "text";
    }
    
    if (this.graph.isClass(node) || this.graph.isFeature(node)) {
      staticValue  = this.convertBooleanToString(node.getAttribute("static"));
      finalValue  = this.convertBooleanToString(node.getAttribute("final"));
      
      if (cell.parent == null || cell.parent.parent == null || !this.graph.isInterface(cell.parent.parent.value)) {
        staticEditor = {"type":"checkbox", "options": {"on":"Si", "off":"No"}};
        finalEditor = {"type":"checkbox", "options": {"on":"Si", "off":"No"}};
      }
    }
    
    if (this.graph.isClassifier(node) || this.graph.isFeature(node)) {
      visibilityValue  = node.getAttribute("visibility");
      visibilityEditor = {"type":"combobox", "options": {
        "valueField":"id",
        "textField":"text",
        "panelHeight":"90",
        "data": this.graph.getVisibilityJSon()
      }};
      
      if (this.graph.isClassifier(node)) {
        attributesEditor = {"type": "button", "options": {"onclick": function () {
          selfEditor.showAttributes(cell);
        }}};
        
        if (this.graph.isClass(node) || this.graph.isInterface(node)) {
          operationsEditor = {"type":"button", "options": {"onclick": function () {
            selfEditor.showOperations(cell);
          }}};
        }
        
        if (this.graph.isEnumeration(node)) {
          attributesName = "Literales";
        }
        
        packageValue  = node.getAttribute("package");
        packageEditor = {"type":"combobox", "options": {
          "valueField":"id",
          "textField":"text",
          "panelHeight":"90",
          "data":this.graph.getPackagesJSon()
        }};
      }
    }
    
    if (this.graph.isClass(node) || this.graph.isOperation(node)) {
      abstractValue = this.convertBooleanToString(node.getAttribute("abstract"));
      abstractEditor = {"type":"checkbox", "options": {"on":"Si", "off":"No"}};
    }
    
    if (this.graph.isAssociation(node)) {
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
      {"name":"Paquete", "value":packageValue, "group":"General", "editor":packageEditor},
      
      // ADVANCED
      {"name":"Es Abstracto", "value":abstractValue, "group":"Avanzado", "editor":abstractEditor},
      {"name":"Es Estatico", "value":staticValue, "group":"Avanzado", "editor":staticEditor},
      {"name":"Es Final", "value":finalValue, "group":"Avanzado", "editor":finalEditor},
      
      // SHOW
      {"name":attributesName, "value":null, "group":"Mostrar", "editor":attributesEditor},
      {"name":"Operaciones", "value":null, "group":"Mostrar", "editor":operationsEditor},
      {"name":"Relacion", "value":null, "group":"Mostrar", "editor":relationshipEditor}
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
 * Converts the boolean to string.
 * @param booleanValue
 * @returns
 */
CLASSProperties.prototype.convertBooleanToString = function (booleanValue) {
  if (booleanValue != null) {
    return booleanValue == "1" ? "Si" : "No";
  }
  return "";
};

/**
 * Convert the string to boolean.
 * @param stringValue
 * @returns
 */
CLASSProperties.prototype.convertStringToBoolean = function (stringValue) {
  return stringValue == "Si" ? "1" : "0";
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
  if (changes.value != null) {
    switch (rowIndex) {
    case 0: // Name
      this.graph.cellEditProperty(this.cell, "name", changes.value, true);
      break;
      
    case 1: // Visibility
      this.graph.cellEditProperty(this.cell, "visibility", changes.value, true);
      break;
    
    case 2: // Package
      this.graph.cellEditProperty(this.cell, "package", changes.value, true);
      break;
    
    case 3: // Abstract
      this.graph.cellEditProperty(this.cell, "abstract", this.convertStringToBoolean(changes.value), true);
      break;
      
    case 4: // Static
      this.graph.cellEditProperty(this.cell, "static", this.convertStringToBoolean(changes.value), true);
      break;
      
    case 5: // Final
      this.graph.cellEditProperty(this.cell, "final", this.convertStringToBoolean(changes.value), true);
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

