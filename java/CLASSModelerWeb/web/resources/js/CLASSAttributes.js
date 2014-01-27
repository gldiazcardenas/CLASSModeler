/*******************************************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ******************************************************************************/

/**
 * Dialog that encapsulates the edition of the classifier's attributes.
 * 
 * @author Gabriel Leonardo Diaz, 16.01.2014.
 */
CLASSAttributes = function (editor) {
  this.editor = editor;
  this.graph  = editor.graph;
  this.dialog = dlgAttributes; // Defined by a PrimeFaces dialog.
  
  this.configureVisibilityCombo();
  this.configureTypeCombo();
  this.configureMultiplicityCombo();
  this.configureAttributesTable();
};

/**
 * Instance of the editor component.
 */
CLASSAttributes.prototype.editor;

/**
 * Instance of the graph component.
 */
CLASSAttributes.prototype.graph;

/**
 * Instance of the dialog to handle.
 */
CLASSAttributes.prototype.dialog;

/**
 * Instance of the classifier edited.
 */
CLASSAttributes.prototype.classifierCell;

/**
 * Instance of the attribute edited.
 */
CLASSAttributes.prototype.attributeCell;

/**
 * Index on the table of the attribute being edited.
 */
CLASSAttributes.prototype.attributeIndex;

/**
 * Initializes the dialog with the attributes of the given cell containing a
 * classifier XML node.
 * 
 * @param cell
 *          The cell containing the classifier to be edited.
 * @author Gabriel Leonardo Diaz, 16.01.2014.
 */
CLASSAttributes.prototype.init = function (cell) {
  this.classifierCell = cell;
  this.attributeCell = null;
  this.loadTableData();
  this.clearSelection();
  this.clearFields();
};

/**
 * Populates and sets up the combo box component for type edition.
 * 
 * @author Gabriel Leonardo Diaz, 17.01.2014.
 */
CLASSAttributes.prototype.configureTypeCombo = function () {
  var jSonData   = [];
  
  // Fixed types
  jSonData.push({id:"boolean", text:"boolean"});
  jSonData.push({id:"byte",    text:"byte"});
  jSonData.push({id:"char",    text:"char"});
  jSonData.push({id:"double",  text:"double"});
  jSonData.push({id:"float",   text:"float"});
  jSonData.push({id:"int",     text:"int"});
  jSonData.push({id:"long",    text:"long"});
  jSonData.push({id:"short",   text:"short"});
  jSonData.push({id:"String",  text:"String"});
  
  var cell;
  
  // Dynamic types
  for (var key in this.graph.model.cells) {
    cell = this.graph.model.getCell(key);
    
    if (this.graph.isClassifier(cell.value)) {
      jSonData.push({id:cell.value.getAttribute("name"), text: cell.value.getAttribute("name")});
    }
  }
  
  $("#attrType").combobox({
      valueField:"id",
      textField:"text",
      panelHeight: 200,
      data: jSonData
  });
  
  $("#attrType").combobox("setValue", "int"); // default value
  
  // Workaround: The panel is shown behind of the PrimeFaces modal dialog.
  var comboPanel = $("#attrType").combobox('panel');
  comboPanel.panel('panel').css('z-index', '2000');
};

/**
 * Populates and sets up the combo box component for visibility edition.
 * 
 * @author Gabriel Leonardo Diaz, 17.01.2014.
 */
CLASSAttributes.prototype.configureVisibilityCombo = function () {
  $("#attrVisibility").combobox({
      valueField:"id",
      textField:"text",
      panelHeight: 90,
      data: [
          {id:"public",    text:"public"},
          {id:"protected", text:"protected"},
          {id:"package",   text:"package"},
          {id:"private",   text:"private"}
      ]
  });
  
  $("#attrVisibility").combobox("setValue", "private"); // default value
  
  // Workaround: The panel is shown behind of the PrimeFaces modal dialog.
  var comboPanel = $("#attrVisibility").combobox('panel');
  comboPanel.panel('panel').css('z-index', '2000');
};

/**
 * Populates and sets up the combo box component for multiplicity edition.
 * 
 * @author Gabriel Leonardo Diaz, 17.01.2014.
 */
CLASSAttributes.prototype.configureMultiplicityCombo = function () {
  return;
  
  $("#attrMultiplicity").combobox({
      valueField:"id",
      textField:"text",
      panelHeight: 130,
      data: [
          {id:"*",       text:"*"},
          {id:"0",       text:"0"},
          {id:"0...*",   text:"0...*"},
          {id:"0...1",   text:"0...1"},
          {id:"1",       text:"1"},
          {id:"1...*",   text:"1...*"}
      ]
  });
  
  // Workaround: The panel is shown behind of the PrimeFaces modal dialog.
  var comboPanel = $("#attrMultiplicity").combobox('panel');
  comboPanel.panel('panel').css('z-index', '2000');
};

/**
 * Populates the attributes table component.
 * 
 * @author Gabriel Leonardo Diaz, 17.01.2014.
 */
CLASSAttributes.prototype.configureAttributesTable = function () {
  var self = this;
  
  $("#attributesTable").datagrid({
      toolbar: "#attrTbToolbar",
      singleSelect: true,
      
      onSelect: function (rowIndex, rowData) {
        self.selectionChanged(rowIndex, true);
      },
      
      onUnSelect: function (rowIndex, rowData) {
        self.selectionChanged(rowIndex, false);
      },
      
      columns:[[
          {field:"name",  title:"Nombre",        width:150},
          {field:"type",  title:"Tipo",          width:150},
          {field:"value", title:"Valor Inicial", width:150}
      ]]
  });
  
  $(function() {
      $("#newAttributeBtn").bind("click", function() {
          self.newAttribute();
      });
  });
  
  $(function() {
      $("#delAttributeBtn").bind("click", function() {
          self.deleteAttribute();
      });
  });
  
  $(function() {
      $("#saveAttributeBtn").bind("click", function() {
          self.saveAttribute();
      });
  });
};

/**
 * Loads the attributes of the edited classifier and fills the table data.
 * @author Gabriel Leonardo Diaz, 25.01.2014.
 */
CLASSAttributes.prototype.loadTableData = function () {
  var jSonData = [];
  
  var attributes = this.graph.getAttributes(this.classifierCell);
  
  if (attributes) {
    for (var i = 0; i < attributes.length; i++) {
      attribute = attributes[i];
      var visibility   = attributes[i].getAttribute("visibility");
      var nameValue    = attributes[i].getAttribute("name");
      var typeValue    = attributes[i].getAttribute("type");
      var initialValue = attributes[i].getAttribute("initialValue");
      
      jSonData.push({name: this.graph.getVisibilityChar(visibility) + " " + nameValue, type: typeValue, value: initialValue});
    }
  }
  
  $("#attributesTable").datagrid({ data : jSonData });
  $("#attributesTable").datagrid("reload");
};

/**
 * Processes the selection changed event.
 * 
 * @param rowIndex
 *          The row selected or unselected.
 * @param selected
 *          A flag indicating the event type (selection or unselection).
 * @author Gabriel Leonardo Diaz, 24.01.2014.
 */
CLASSAttributes.prototype.selectionChanged = function (rowIndex, selected) {
  this.clearFields();
  
  if (selected) {
    this.attributeCell  = this.graph.getAttributes(this.classifierCell)[rowIndex];
    this.attributeIndex = rowIndex;
    
    $("#attrName").val(this.attributeCell.getAttribute("name"));
    $("#attrType").combobox("setValue", this.attributeCell.getAttribute("type"));
    $("#attrVisibility").combobox("setValue", this.attributeCell.getAttribute("visibility"));
    $("#attrInitValue").val(this.attributeCell.getAttribute("initialValue"));
    $("#staticCheck").prop("checked", this.attributeCell.getAttribute("isStatic") == "true");
    $("#finalCheck").prop("checked", this.attributeCell.getAttribute("isFinal") == "true");
  }
};

/**
 * Adds a new attribute to the edited classifier.
 * 
 * @author Gabriel Leonardo Diaz, 18.01.2014.
 */
CLASSAttributes.prototype.newAttribute = function () {
  this.clearSelection();
  this.clearFields();
};

/**
 * Deletes the selected attribute over the table.
 * 
 * @author Gabriel Leonardo Diaz, 18.01.2014.
 */
CLASSAttributes.prototype.deleteAttribute = function () {
  if (this.attributeCell) {
    this.graph.removeCells([this.attributeCell]);
    $("#attributesTable").datagrid("deleteRow", this.attributeIndex);
  }
};

/**
 * Saves the changes over the edited attribute.
 * 
 * @author Gabriel Leonardo Diaz, 18.01.2014.
 */
CLASSAttributes.prototype.saveAttribute = function () {
  var nameValue       = $("#attrName").val();
  var typeValue       = $("#attrType").combobox("getValue");
  var visibilityValue = $("#attrVisibility").combobox("getValue");
  var initialValue    = $("#attrInitValue").val();
  var staticValue     = $("#staticCheck").is(":checked");
  var finalValue      = $("#finalCheck").is(":checked");
  
  if (nameValue == null || nameValue.length == 0) {
    // Invalid Name
    return;
  }
  
  if (!this.graph.isEnumeration(this.classifierCell.value)) {
    if (typeValue == null || typeValue.length == 0) {
      // Invalid Type
      return;
    }
    
    if (visibilityValue == null || visibilityValue.length == 0) {
      // Invalid Visibility
      return;
    }
  }
  
  // Prepare attribute
  var attribute;
  if (this.attributeCell) {
    attribute = this.attributeCell.clone(true);
  }
  else {
    if (typeValue && typeValue.length > 0) {
      attribute = this.graph.model.cloneCell(this.editor.getTemplate("property"));
    }
    else {
      attribute = this.graph.model.cloneCell(this.editor.getTemplate("literal"));
    }
  }
  
  // Set values
  attribute.setAttribute("name", nameValue);
  
  if (this.graph.isProperty(attribute.cell)) {
    attribute.setAttribute("type", typeValue);
    attribute.setAttribute("visibility", visibilityValue);
    attribute.setAttribute("initialValue", initialValue);
    attribute.setAttribute("isStatic", staticValue);
    attribute.setAttribute("isFinal", finalValue);
  }
  
  var attrName = attribute.getAttribute("name");
  var attrVis  = attribute.getAttribute("visibility");
  var attrType = attribute.getAttribute("type");
  var attrVal  = attribute.getAttribute("initialValue");
  var visChar  = this.graph.getVisibilityChar(attrVis);
  
  // Apply changes
  if (this.attributeCell) {
    this.graph.replaceNode(this.attributeCell, attribute.value);
    $("#attributesTable").datagrid("updateRow", {index: this.attributeIndex, row: { name: visChar + " " + attrName, type: attrType, value: attrVal }});
    $("#attributesTable").datagrid("reload");
  }
  else {
    this.graph.addAttribute (this.classifierCell, attribute, this.editor.getTemplate("section"));
    this.attributeCell = attribute;
    this.attributeIndex = $("#attributesTable").datagrid("getRows").length;
    $("#attributesTable").datagrid("insertRow", {row: { name: visChar + " " + attrName, type: attrType, value: attrVal }});
    $("#attributesTable").datagrid("reload");
  }
};

/**
 * Clears the fields to create/edit attributes.
 * 
 * @author Gabriel Leonardo Diaz, 25.01.2014.
 */
CLASSAttributes.prototype.clearFields = function () {
  $("#attrName").val("");
  
  if (this.graph.isEnumeration(this.classifierCell.value)) {
    $("#attrType").combobox("clear");
    $("#attrVisibility").combobox("clear");
    
  }
  else {
    $("#attrType").combobox("setValue", "int");
    $("#attrVisibility").combobox("setValue", "private");
  }
 
  $("#attrInitValue").val("");
  $("#staticCheck").attr("checked", false);
  $("#finalCheck").attr("checked", false);
};

/**
 * Clears the selection on the attributes table.
 * 
 * @author Gabriel Leonardo Diaz, 25.01.2014.
 */
CLASSAttributes.prototype.clearSelection = function () {
  $("#attributesTable").datagrid("unselectAll");
  this.attributeIndex = null;
  this.attributeCell  = null;
};

/**
 * Shows up the dialog to the user.
 * 
 * @author Gabriel Leonardo Diaz, 16.01.2014.
 */
CLASSAttributes.prototype.show = function () {
  this.dialog.show();
};
