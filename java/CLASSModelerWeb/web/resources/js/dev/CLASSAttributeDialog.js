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
CLASSAttributeDialog = function (editor) {
  this.editor = editor;
  this.graph  = editor.graph;
  this.dialog = dlgAttributes; // Defined by a PrimeFaces dialog.
  this.title  = dlgAttributes.titlebar.children("span.ui-dialog-title").html();
  
  this.configureVisibilityCombo();
  this.configureCollectionsCombo();
  this.configureTypeCombo();
  this.configureAttributesTable();
};

/**
 * Instance of the editor component.
 */
CLASSAttributeDialog.prototype.editor;

/**
 * Instance of the graph component.
 */
CLASSAttributeDialog.prototype.graph;

/**
 * Instance of the dialog to handle.
 */
CLASSAttributeDialog.prototype.dialog;

/**
 * Instance of the classifier edited.
 */
CLASSAttributeDialog.prototype.classifierCell;

/**
 * Instance of the attribute edited.
 */
CLASSAttributeDialog.prototype.attributeCell;

/**
 * Index on the table of the attribute being edited.
 */
CLASSAttributeDialog.prototype.attributeIndex;

/**
 * The localized title text.
 */
CLASSAttributeDialog.prototype.title;

/**
 * Initializes the dialog with the attributes of the given cell containing a
 * classifier XML node.
 * 
 * @param cell
 *          The cell containing the classifier to be edited.
 * @author Gabriel Leonardo Diaz, 16.01.2014.
 */
CLASSAttributeDialog.prototype.init = function (cell) {
  this.classifierCell = cell;
  this.attributeCell  = null;
  this.attributeIndex = null;
  
  this.loadTableData();
  this.loadTypesData();
  this.clearSelection();
  this.clearFields();
  this.configureGUI();
  this.setTitle();
};

/**
 * Configures the special GUI component behavior depending on the classifier
 * cell being edited.
 * 
 * @author Gabriel Leonardo Diaz, 03.03.2014.
 */
CLASSAttributeDialog.prototype.configureGUI = function () {
  if (this.graph.isEnumeration(this.classifierCell.value)) {
    $("#staticCheck").attr("disabled", true);
    $("#finalCheck").attr("disabled", true);
    $("#attrType").combobox("disable");
    $("#attrVisibility").combobox("disable");
    $("#attrCollection").combobox("disable");
    $("#attrInitValue").attr("disabled", true);
  }
  else if (this.graph.isInterface(this.classifierCell.value)) {
    $("#staticCheck").attr("disabled", true);
    $("#finalCheck").attr("disabled", true);
    $("#attrType").combobox("enable");
    $("#attrVisibility").combobox("enable");
    $("#attrCollection").combobox("enable");
    $("#attrInitValue").removeAttr("disabled");
  }
  else {
    $("#staticCheck").removeAttr("disabled");
    $("#finalCheck").removeAttr("disabled");
    $("#attrType").combobox("enable");
    $("#attrVisibility").combobox("enable");
    $("#attrCollection").combobox("enable");
    $("#attrInitValue").removeAttr("disabled");
  }
};

/**
 * Sets the title of the dialog by appending the name of the classifier being
 * edited.
 * 
 * @author Gabriel Leonardo Diaz, 10.02.2014.
 */
CLASSAttributeDialog.prototype.setTitle = function () {
  var aTitle = this.title.replace("{0}", this.classifierCell.getAttribute("name"));
  if (this.graph.isEnumeration(this.classifierCell.value)) {
    aTitle = aTitle.replace("Atributos", "Literales");
  }
  this.dialog.titlebar.children("span.ui-dialog-title").html(aTitle);
};

/**
 * Populates and sets up the combo box component for type edition.
 * 
 * @author Gabriel Leonardo Diaz, 17.01.2014.
 */
CLASSAttributeDialog.prototype.configureTypeCombo = function () {
  $("#attrType").combobox({
      valueField:"id",
      textField:"text",
      panelHeight: 200,
      data: this.graph.getTypesJSon()
  });
  
  $("#attrType").combobox("setValue", "int"); // default value
  
  // Workaround: The panel is shown behind of the PrimeFaces modal dialog.
  var comboPanel = $("#attrType").combobox("panel");
  comboPanel.panel("panel").css("z-index", "2000");
};

/**
 * Populates and sets up the combo box component for visibility edition.
 * 
 * @author Gabriel Leonardo Diaz, 17.01.2014.
 */
CLASSAttributeDialog.prototype.configureVisibilityCombo = function () {
  $("#attrVisibility").combobox({
      valueField:"id",
      textField:"text",
      panelHeight: 90,
      data: this.graph.getVisibilityJSon()
  });
  
  $("#attrVisibility").combobox("setValue", "private"); // default value
  
  // Workaround: The panel is shown behind of the PrimeFaces modal dialog.
  var comboPanel = $("#attrVisibility").combobox("panel");
  comboPanel.panel("panel").css("z-index", "2000");
};

/**
 * Populates and sets up the combo box component for Collection selection.
 * 
 * @author Gabriel Leonardo Diaz, 03.03.2014.
 */
CLASSAttributeDialog.prototype.configureCollectionsCombo = function () {
  $("#attrCollection").combobox({
      valueField:"id",
      textField:"text",
      panelHeight: 200,
      data: this.graph.getCollectionsJSon()
  });
  
  $("#attrCollection").combobox("setValue", ""); // default value
  
  // Workaround: The panel is shown behind of the PrimeFaces modal dialog.
  var comboPanel = $("#attrCollection").combobox("panel");
  comboPanel.panel("panel").css("z-index", "2000");
};

/**
 * Populates the attributes table component.
 * 
 * @author Gabriel Leonardo Diaz, 17.01.2014.
 */
CLASSAttributeDialog.prototype.configureAttributesTable = function () {
  var self = this;
  
  $("#attributesTable").datagrid({
      toolbar: "#attrTbToolbar",
      singleSelect: true,
      
      onSelect: function (rowIndex, rowData) {
        self.selectionChanged(rowIndex, true);
      },
      
      onUnselectAll: function (rows) {
        self.selectionChanged(rows[0], false);
      },
      
      onUnselect: function (rowIndex, rowData) {
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
 * Loads the types data once again.
 */
CLASSAttributeDialog.prototype.loadTypesData = function () {
  $("#attrType").combobox({ data: this.graph.getTypesJSon() });
};

/**
 * Loads the attributes of the edited classifier and fills the table data.
 * @author Gabriel Leonardo Diaz, 25.01.2014.
 */
CLASSAttributeDialog.prototype.loadTableData = function () {
  var jSonData = [];
  
  var attributes = this.graph.getAttributes(this.classifierCell);
  
  if (attributes) {
    for (var i = 0; i < attributes.length; i++) {
      var visibility   = attributes[i].getAttribute("visibility");
      var nameValue    = attributes[i].getAttribute("name");
      var typeValue    = this.graph.escape(this.graph.convertTypeToString(attributes[i]));
      var initialValue = this.graph.escape(attributes[i].getAttribute("initialValue"));
      
      jSonData.push({ name: this.graph.getVisibilityChar(visibility) + " " + nameValue, type: typeValue, value: initialValue });
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
CLASSAttributeDialog.prototype.selectionChanged = function (rowIndex, selected) {
  this.clearFields();
  
  if (selected) {
    this.attributeCell  = this.graph.getAttributes(this.classifierCell)[rowIndex];
    this.attributeIndex = rowIndex;
    
    $("#attrName").val(this.attributeCell.getAttribute("name"));
    $("#attrType").combobox("setValue", this.attributeCell.getAttribute("type"));
    $("#attrCollection").combobox("setValue", this.attributeCell.getAttribute("collection"));
    $("#attrVisibility").combobox("setValue", this.attributeCell.getAttribute("visibility"));
    $("#attrInitValue").val(this.attributeCell.getAttribute("initialValue"));
    $("#staticCheck").prop("checked", this.attributeCell.getAttribute("static") == "1");
    $("#finalCheck").prop("checked", this.attributeCell.getAttribute("final") == "1");
  }
};

/**
 * Adds a new attribute to the edited classifier.
 * 
 * @author Gabriel Leonardo Diaz, 18.01.2014.
 */
CLASSAttributeDialog.prototype.newAttribute = function () {
  this.clearSelection();
  this.clearFields();
  $("#attrName" ).focus();
};

/**
 * Deletes the selected attribute over the table.
 * 
 * @author Gabriel Leonardo Diaz, 18.01.2014.
 */
CLASSAttributeDialog.prototype.deleteAttribute = function () {
  if (this.attributeCell) {
    this.graph.removeCells([this.attributeCell]);
    $("#attributesTable").datagrid("deleteRow", this.attributeIndex);
    this.clearSelection();
  }
};

/**
 * Saves the changes over the edited attribute.
 * 
 * @author Gabriel Leonardo Diaz, 18.01.2014.
 */
CLASSAttributeDialog.prototype.saveAttribute = function () {
  var nameValue       = $("#attrName").val();
  var typeValue       = $("#attrType").combobox("getValue");
  var visibilityValue = $("#attrVisibility").combobox("getValue");
  var initialValue    = $("#attrInitValue").val();
  var staticValue     = $("#staticCheck").is(":checked") ? "1" : "0";
  var finalValue      = $("#finalCheck").is(":checked") ? "1" : "0";
  var collectionValue = $("#attrCollection").combobox("getValue");
  var isEnum          = this.graph.isEnumeration(this.classifierCell.value);
  
  // Validate Values
  if (!this.validFields(nameValue, typeValue, visibilityValue, initialValue)) {
    return;
  }
  
  // Prepare attribute
  var attribute;
  if (this.attributeCell) {
    attribute = this.attributeCell.clone(true);
  }
  else {
    if (isEnum) {
      attribute = this.graph.model.cloneCell(this.editor.getTemplate("literal"));
    }
    else {
      attribute = this.graph.model.cloneCell(this.editor.getTemplate("property"));
    }
  }
  
  // Set values
  attribute.setAttribute("name", nameValue);
  
  if (this.graph.isProperty(attribute.value)) {
    attribute.setAttribute("type", typeValue);
    attribute.setAttribute("visibility", visibilityValue);
    attribute.setAttribute("static", staticValue);
    attribute.setAttribute("final", finalValue);
    attribute.setAttribute("initialValue", initialValue);
    attribute.setAttribute("collection", collectionValue);
  }
  
  var attrName = this.graph.getVisibilityChar(attribute.getAttribute("visibility")) + " " + attribute.getAttribute("name");
  var attrType = this.graph.escape(this.graph.convertTypeToString(attribute));
  var attrVal  = this.graph.escape(attribute.getAttribute("initialValue"));
  
  // Apply changes
  if (this.attributeCell) {
    this.graph.editAttribute(this.attributeCell, attribute);
    $("#attributesTable").datagrid("updateRow", {index: this.attributeIndex, row: { name: attrName, type: attrType, value: attrVal }});
  }
  else {
    this.graph.addAttribute (this.classifierCell, attribute);
    $("#attributesTable").datagrid("insertRow", {row: { name: attrName, type: attrType, value: attrVal }});
    $("#attributesTable").datagrid("selectRow", $("#attributesTable").datagrid("getRows").length - 1);
  }
  
  $("#attributesTable").datagrid("reload");
};

/**
 * Validates the fields on the form.
 */
CLASSAttributeDialog.prototype.validFields = function (nameValue, typeValue, visibilityValue, initialValue) {
  var valid = true;
  
  // Invalid Name
  if (!nameValue) {
    $("#attrName").validatebox({ required: true, missingMessage: "Campo requerido" });
    valid = false;
  }
  
  if (!this.graph.isEnumeration(this.classifierCell.value)) {
    // Invalid Type
    if (!typeValue) {
      $("#attrType").combobox({ required: true, missingMessage: "Campo requerido" });
      valid = false;
    }
    
    // Invalid Visibility
    if (!visibilityValue) {
      $("#attrVisibility").combobox({ required: true, missingMessage: "Campo requerido" });
      valid = false;
    }
  }
  
  // Invalid constant for interface
  if (this.graph.isInterface(this.classifierCell.value) && !initialValue) {
    $("#attrInitValue").validatebox({ required: true, missingMessage: "Campo requerido para variable constante" });
    valid = false;
  }
  
  return valid;
};

/**
 * Clears the fields to create/edit attributes.
 * 
 * @author Gabriel Leonardo Diaz, 25.01.2014.
 */
CLASSAttributeDialog.prototype.clearFields = function () {
  $("#attrName").val("");
  $("#attrName").validatebox({ required: false, missingMessage: "" });
  $("#attrType").combobox({ required: false, missingMessage: "" });
  $("#attrVisibility").combobox({ required: false, missingMessage: "" });
  
  if (this.graph.isEnumeration(this.classifierCell.value)) {
    $("#attrType").combobox("clear");
    $("#attrVisibility").combobox("clear");
    $("#attrCollection").combobox("clear");
  }
  else {
    $("#attrType").combobox("setValue", "int");
    $("#attrVisibility").combobox("setValue", "private");
    $("#attrCollection").combobox("setValue", "");
  }
 
  $("#attrInitValue").val("");
  $("#staticCheck").prop("checked", this.graph.isInterface(this.classifierCell.value));
  $("#finalCheck").prop("checked", this.graph.isInterface(this.classifierCell.value));
  this.configureGUI();
};

/**
 * Clears the selection on the attributes table.
 * 
 * @author Gabriel Leonardo Diaz, 25.01.2014.
 */
CLASSAttributeDialog.prototype.clearSelection = function () {
  $("#attributesTable").datagrid("unselectAll");
  this.attributeIndex = null;
  this.attributeCell  = null;
};

/**
 * Shows up the dialog to the user.
 * 
 * @author Gabriel Leonardo Diaz, 16.01.2014.
 */
CLASSAttributeDialog.prototype.show = function () {
  this.dialog.show();
};
