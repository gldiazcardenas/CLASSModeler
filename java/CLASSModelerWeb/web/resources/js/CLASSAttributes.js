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
  this.title  = dlgAttributes.titlebar.children("span.ui-dialog-title").html();
  
  this.configureVisibilityCombo();
  this.configureCollectionsCombo();
  this.configureTypeCombo();
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
 * The localized title text.
 */
CLASSAttributes.prototype.title;

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
  this.attributeCell  = null;
  this.attributeIndex = null;
  
  this.configureGUI();
  this.loadTableData();
  this.clearSelection();
  this.clearFields();
  this.setTitle();
};

/**
 * Configures the special GUI component behavior depending on the classifier
 * cell being edited.
 * 
 * @author Gabriel Leonardo Diaz, 03.03.2014.
 */
CLASSAttributes.prototype.configureGUI = function () {
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
CLASSAttributes.prototype.setTitle = function () {
  this.dialog.titlebar.children("span.ui-dialog-title").html(this.title.replace("{0}", this.classifierCell.getAttribute("name")));
};

/**
 * Populates and sets up the combo box component for type edition.
 * 
 * @author Gabriel Leonardo Diaz, 17.01.2014.
 */
CLASSAttributes.prototype.configureTypeCombo = function () {
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
CLASSAttributes.prototype.configureVisibilityCombo = function () {
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
CLASSAttributes.prototype.configureCollectionsCombo = function () {
  $("#attrCollection").combobox({
      valueField:"id",
      textField:"text",
      panelHeight: 90,
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
CLASSAttributes.prototype.configureAttributesTable = function () {
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
 * Loads the attributes of the edited classifier and fills the table data.
 * @author Gabriel Leonardo Diaz, 25.01.2014.
 */
CLASSAttributes.prototype.loadTableData = function () {
  var jSonData = [];
  
  var attributes = this.graph.getAttributes(this.classifierCell);
  
  if (attributes) {
    for (var i = 0; i < attributes.length; i++) {
      var visibility   = attributes[i].getAttribute("visibility");
      var nameValue    = attributes[i].getAttribute("name");
      
      var typeValue    = this.graph.convertTypeToString(attributes[i]);
      if (typeValue) {
        typeValue = typeValue.replace("<", "&lt;");
        typeValue = typeValue.replace(">", "&gt;");
      }
      
      var initialValue = attributes[i].getAttribute("initialValue");
      if (initialValue) {
        initialValue = initialValue.replace("<", "&lt;");
        initialValue = initialValue.replace(">", "&gt;");
      }
      
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
CLASSAttributes.prototype.selectionChanged = function (rowIndex, selected) {
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
    this.clearSelection();
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
  var staticValue     = $("#staticCheck").is(":checked") ? "1" : "0";
  var finalValue      = $("#finalCheck").is(":checked") ? "1" : "0";
  var collectionValue = $("#attrCollection").combobox("getValue");
  
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
  
  if (this.graph.isInterface(this.classifierCell.value)) {
    if (staticValue == "0") {
      // Invalid static
      return;
    }
    
    if (finalValue == "0") {
      // Invalid final
      return;
    }
    
    if (initialValue == null || initialValue.length == 0) {
      // Invalid constant for interface
      return;
    }
  }
  
  // Prepare attribute
  var attribute;
  if (this.attributeCell) {
    attribute = this.attributeCell.clone(true);
  }
  else {
    if (typeValue) {
      attribute = this.graph.model.cloneCell(this.editor.getTemplate("property"));
    }
    else {
      attribute = this.graph.model.cloneCell(this.editor.getTemplate("literal"));
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
  
  var attrType = this.graph.convertTypeToString(attribute);
  if (attrType) {
    attrType = attrType.replace("<", "&lt;");
    attrType = attrType.replace(">", "&gt;");
  }
  
  var attrVal  = attribute.getAttribute("initialValue");
  if (attrVal) {
    attrVal = initialValue.replace("<", "&lt;");
    attrVal = initialValue.replace(">", "&gt;");
  }
  
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
 * Clears the fields to create/edit attributes.
 * 
 * @author Gabriel Leonardo Diaz, 25.01.2014.
 */
CLASSAttributes.prototype.clearFields = function () {
  $("#attrName").val("");
  
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
