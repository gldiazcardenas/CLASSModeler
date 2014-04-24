/*******************************************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ******************************************************************************/

/**
 * Dialog that encapsulates the edition of the classifier's operations.
 * 
 * @author Gabriel Leonardo Diaz, 26.01.2014.
 */
CLASSOperationDialog = function (editor) {
  this.editor = editor;
  this.graph  = editor.graph;
  this.dialog = dlgOperations; // Defined by a PrimeFaces dialog.
  this.title  = dlgOperations.titlebar.children("span.ui-dialog-title").html();
  
  var self    = this;
  this.dialog.content[0].onclick = function () {
    self.stopEditingParameter();
  };
  
  this.configureVisibilityComboBox();
  this.configureCollectionsComboBox();
  this.configureReturnTypeComboBox();
  this.configureOperationsTable();
  this.configureParametersTable();
};

/**
 * Instance of the editor component.
 */
CLASSOperationDialog.prototype.editor;

/**
 * Instance of the graph component.
 */
CLASSOperationDialog.prototype.graph;

/**
 * The dialog to show.
 */
CLASSOperationDialog.prototype.dialog;

/**
 * The instance of the classifier being edited.
 */
CLASSOperationDialog.prototype.classifierCell;

/**
 * The instance of the operation being edited.
 */
CLASSOperationDialog.prototype.operationCell;

/**
 * The index on the table of the operation being edited.
 */
CLASSOperationDialog.prototype.operationIndex;

/**
 * The index on the table of the parameter being edited.
 */
CLASSOperationDialog.prototype.parameterIndex;

/**
 * The localized title text.
 */
CLASSOperationDialog.prototype.title;

/**
 * Initializes the dialog to edit the operations of the given classifier cell.
 * @param cell
 */
CLASSOperationDialog.prototype.init = function (cell) {
  this.classifierCell = cell;
  this.operationCell  = null;
  this.operationIndex = null;
  this.parameterIndex = null;
  
  this.loadOperationTableData();
  this.loadTypesData();
  this.clearSelection();
  this.clearFields();
  this.setTitle();
};

/**
 * Sets the title of the dialog by appending the name of the classifier being
 * edited.
 * 
 * @author Gabriel Leonardo Diaz, 10.02.2014.
 */
CLASSOperationDialog.prototype.setTitle = function () {
  this.dialog.titlebar.children("span.ui-dialog-title").html(this.title.replace("{0}", this.classifierCell.getAttribute("name")));
};

/**
 * Creates the operations table component.
 * 
 * @author Gabriel Leonardo Diaz, 17.01.2014.
 */
CLASSOperationDialog.prototype.configureOperationsTable = function () {
  var self = this;
  
  $("#operationsTable").datagrid({
      toolbar: "#operTbToolbar",
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
          {field:"name",       title:"Nombre",          width:150},
          {field:"type",       title:"Tipo de Retorno", width:150},
          {field:"parameters", title:"Parametros",      width:150}
      ]]
  });
  
  $(function() {
      $("#newOperationBtn").bind("click", function() {
          self.newOperation();
      });
  });
  
  $(function() {
      $("#delOperationBtn").bind("click", function() {
          self.deleteOperation();
      });
  });
  
  $(function() {
      $("#saveOperationBtn").bind("click", function() {
          self.saveOperation();
      });
  });
};

/**
 * Loads the data in the operations table.
 * 
 * @author Gabriel Leonardo Diaz, 29.01.2014.
 */
CLASSOperationDialog.prototype.loadOperationTableData = function () {
  var jSonData = [];
  
  var operations = this.graph.getOperations(this.classifierCell);
  
  if (operations) {
    for (var i = 0; i < operations.length; i++) {
      var nameValue    = this.graph.getVisibilityChar(operations[i].getAttribute("visibility")) + " " + operations[i].getAttribute("name");
      var typeValue    = this.graph.escape(this.graph.convertTypeToString(operations[i]));
      var paramValue   = this.graph.escape(this.graph.convertParametersToString(operations[i]));
      
      jSonData.push({name: nameValue, type: typeValue, parameters: paramValue});
    }
  }
  
  $("#operationsTable").datagrid({ data : jSonData });
  $("#operationsTable").datagrid("reload");
};

/**
 * Updates the types in the return type field and the parameters table.
 */
CLASSOperationDialog.prototype.loadTypesData = function () {
  $("#operReturnType").combobox({ data: this.graph.getTypesJSon(true) });
  this.configureParametersTable();
};

/**
 * Populates and sets up the combo box component for visibility edition.
 * 
 * @author Gabriel Leonardo Diaz, 17.01.2014.
 */
CLASSOperationDialog.prototype.configureVisibilityComboBox = function () {
  $("#operVisibility").combobox({
      valueField:"id",
      textField:"text",
      panelHeight: 90,
      data: this.graph.getVisibilityJSon()
  });
  
  $("#operVisibility").combobox("setValue", "public"); // default value
  
  // Workaround: The panel is shown behind of the PrimeFaces modal dialog.
  var comboPanel = $("#operVisibility").combobox("panel");
  comboPanel.panel("panel").css("z-index", "2000");
};

/**
 * Constructs the table component to handle parameters of the operation being
 * edited.
 * 
 * @author Gabriel Leonardo Diaz, 27.01.2014.
 */
CLASSOperationDialog.prototype.configureParametersTable = function () {
  var self      = this;
  
  $("#parametersTable").datagrid({
      toolbar: "#paramsTbToolbar",
      singleSelect: true,
      
      onClickRow: function (index) {
        self.onClickParameter(index);
      },
      
      columns:[[
          { field:"name", title:"Nombre", width:150, editor: { type: "validatebox", options: { required: true, missingMessage: "" }}},
          
          { field:"type", title:"Tipo",   width:120, editor: {
              type:"combobox",
              options: { valueField:"id",  textField:"text", panelHeight: 200, data:this.graph.getTypesJSon(), required: true, missingMessage: "" }
          }},
          
          { field:"collection", title:"Coleccion", width:130, editor: {
              type:"combobox",
              options: { valueField:"id",  textField:"text", panelHeight: 200, data:this.graph.getCollectionsJSon() }
            },
            
            formatter: function (value, row, index) {
              return self.graph.getCollectionName(value);
            }
          },
      ]]
  });
  
  $(function() {
      $("#newParameterBtn").bind("click", function() {
          self.newParameter();
      });
  });
  
  $(function() {
      $("#delParameterBtn").bind("click", function() {
          self.deleteParameter();
      });
  });
  
  $(function() {
      $("#saveParameterBtn").bind("click", function() {
          self.saveParameter();
      });
  });
};

/**
 * Constructs the combo box component for return type field.
 * 
 * @author Gabriel Leonardo Diaz, 26.01.2014.
 */
CLASSOperationDialog.prototype.configureReturnTypeComboBox = function () {
  $("#operReturnType").combobox({
      valueField:"id",
      textField:"text",
      panelHeight: 200,
      data: this.graph.getTypesJSon(true)
  });
  
  $("#operReturnType").combobox("setValue", "void"); // default value
  
  // Workaround: The panel is shown behind of the PrimeFaces modal dialog.
  var comboPanel = $("#operReturnType").combobox("panel");
  comboPanel.panel("panel").css("z-index", "2000");
};

/**
 * Constructs the combo box component for collections field.
 * 
 * @author Gabriel Leonardo Diaz, 03.03.2014.
 */
CLASSOperationDialog.prototype.configureCollectionsComboBox = function () {
  $("#operCollection").combobox({
      valueField:"id",
      textField:"text",
      panelHeight: 200,
      data: this.graph.getCollectionsJSon()
  });
  
  $("#operCollection").combobox("setValue", ""); // default value
  
  // Workaround: The panel is shown behind of the PrimeFaces modal dialog.
  var comboPanel = $("#operCollection").combobox("panel");
  comboPanel.panel("panel").css("z-index", "2000");
};

/**
 * Processes the selection changes on the operations table.
 * 
 * @param rowIndex
 *          The row index selected or unselected.
 * @param selected
 *          A flag indicating the action performed.
 * @author Gabriel Leonardo Diaz, 26.01.2014.
 */
CLASSOperationDialog.prototype.selectionChanged = function (rowIndex, selected) {
  this.clearFields();
  
  if (selected) {
    this.operationCell  = this.graph.getOperations(this.classifierCell)[rowIndex];
    this.operationIndex = rowIndex;
    
    $("#operName").val(this.operationCell.getAttribute("name"));
    $("#operStaticCheck").prop("checked", this.operationCell.getAttribute("static") == "1");
    $("#operFinalCheck").prop("checked", this.operationCell.getAttribute("final") == "1");
    $("#operAbstractCheck").prop("checked", this.operationCell.getAttribute("abstract") == "1");
    $("#operSyncCheck").prop("checked", this.operationCell.getAttribute("synchronized") == "1");
    $("#operReturnType").combobox("setValue", this.operationCell.getAttribute("type"));
    $("#operCollection").combobox("setValue", this.operationCell.getAttribute("collection"));
    
    var jSonData = [];
    
    if (this.operationCell.value.childNodes) {
      var param;
      for (var i = 0; i < this.operationCell.value.childNodes.length; i++) {
        param = this.operationCell.value.childNodes[i];
        
        var nameValue       = param.getAttribute("name");
        var typeValue       = param.getAttribute("type");
        var collectionValue = param.getAttribute("collection");
        
        jSonData.push({name: nameValue, type: typeValue, collection: collectionValue});
      }
    }
    
    $("#parametersTable").datagrid({data: jSonData});
    $("#parametersTable").datagrid("reload");
  }
};

/**
 * Clears the selection on the operations table.
 * 
 * @author Gabriel Leonardo Diaz, 25.01.2014.
 */
CLASSOperationDialog.prototype.clearSelection = function () {
  $("#operationsTable").datagrid("unselectAll");
  this.operationIndex = null;
  this.operationCell  = null;
};

/**
 * Clears the fields used to edit or create operations.
 * 
 * @author Gabriel Leonardo Diaz, 27.01.2014.
 */
CLASSOperationDialog.prototype.clearFields = function () {
  $("#operName").val("");
  $("#operName").validatebox({ required: false, missingMessage: "" });
  $("#operVisibility").combobox({ required: false, missingMessage: "" });
  $("#operReturnType").combobox({ required: false, missingMessage: "" });
  $("#operStaticCheck").prop("checked", false);
  $("#operFinalCheck").prop("checked", false);
  $("#operAbstractCheck").prop("checked", false);
  $("#operSyncCheck").prop("checked", false);
  $("#parametersTable").datagrid({data:[]});
  $("#parametersTable").datagrid("reload");
  $("#operReturnType").combobox("setValue", "void");
  $("#operVisibility").combobox("setValue", "public");
  $("#operConcurrency").combobox("setValue", "secuencial");
  $("#operCollection").combobox("setValue", "");
};

/**
 * Clears the fields and prepares the dialog to create a new operation.
 * 
 * @author Gabriel Leonardo Diaz, 26.01.2014.
 */
CLASSOperationDialog.prototype.newOperation = function () {
  this.clearSelection();
  this.clearFields();
  $("#operName" ).focus();
};

/**
 * Deletes the selected operation.
 * 
 * @author Gabriel Leonardo Diaz, 26.01.2014.
 */
CLASSOperationDialog.prototype.deleteOperation = function () {
  if (this.operationCell) {
    this.graph.removeCells([this.operationCell]);
    $("#operationsTable").datagrid("deleteRow", this.operationIndex);
    this.clearSelection();
  }
};

/**
 * Saves the changes made over an operation, or creates a new one.
 * 
 * @author Gabriel Leonardo Diaz, 26.01.2014.
 */
CLASSOperationDialog.prototype.saveOperation = function () {
  var nameValue       = $("#operName").val();
  var visValue        = $("#operVisibility").combobox("getValue");
  var retTypeValue    = $("#operReturnType").combobox("getValue");
  var staticValue     = $("#operStaticCheck").is(":checked") ? "1" : "0";
  var finalValue      = $("#operFinalCheck").is(":checked") ? "1" : "0";
  var abstractValue   = $("#operAbstractCheck").is(":checked") ? "1" : "0";
  var syncValue       = $("#operSyncCheck").is(":checked") ? "1" : "0";
  var collectionValue = $("#operCollection").combobox("getValue");
  
  // Validate fields
  if (!this.validFields(nameValue, visValue, retTypeValue)) {
    return;
  }
  
  // Prepare the operation
  var operation;
  if (this.operationCell) {
    operation = this.operationCell.clone(true);
  }
  else {
    operation = this.graph.model.cloneCell(this.editor.getTemplate("operation"));
  }
  
  // Set values
  operation.setAttribute("name", nameValue);
  operation.setAttribute("visibility", visValue);
  operation.setAttribute("type", retTypeValue);
  operation.setAttribute("static", staticValue);
  operation.setAttribute("final", finalValue);
  operation.setAttribute("abstract", abstractValue);
  operation.setAttribute("synchronized", syncValue);
  operation.setAttribute("collection", collectionValue);
  operation.value.innerHTML = ""; // Remove all child nodes
  
  var paramRows = $("#parametersTable").datagrid("getRows");
  if (paramRows) {
    for (var i = 0; i < paramRows.length; i++) {
      var row   = paramRows[i];
      var cell  = this.graph.model.cloneCell(this.editor.getTemplate("parameter"));
      
      var param = cell.value;
      param.setAttribute("name", row.name);
      param.setAttribute("type", row.type);
      param.setAttribute("collection", row.collection);
      
      operation.value.appendChild(param);
    }
  }
  
  var operName    = this.graph.getVisibilityChar(visValue) + " " + operation.getAttribute("name");
  var operRetType = this.graph.escape(this.graph.convertTypeToString(operation));
  var operParams  = this.graph.escape(this.graph.convertParametersToString(operation));
  
  // Apply changes
  if (this.operationCell) {
    this.graph.editOperation(this.operationCell, operation);
    $("#operationsTable").datagrid("updateRow", {index: this.operationIndex, row: { name: operName, type: operRetType, parameters:  operParams}});
  }
  else {
    this.graph.addOperation(this.classifierCell, operation);
    $("#operationsTable").datagrid("insertRow", {row: { name: operName, type: operRetType, parameters: operParams }});
    $("#operationsTable").datagrid("selectRow", $("#operationsTable").datagrid("getRows").length - 1);
  }
  
  $("#operationsTable").datagrid("reload");
};

/**
 * Validates the fields on the form.
 */
CLASSOperationDialog.prototype.validFields = function (nameValue, visValue, retTypeValue) {
  var valid = true;
  
  // Invalid NAME
  if (!nameValue) {
    $("#operName").validatebox({ required: true, missingMessage: "Campo requerido" });
    valid = false;
  }
  
  // Invalid VISIBILITY
  if (!visValue) {
    $("#operVisibility").combobox({ required: true, missingMessage: "Campo requerido" });
    valid = false;
  }
  
  // Invalid Return Type
  if (this.graph.isInterface(this.classifierCell.value) || nameValue != this.classifierCell.getAttribute("name")) {
    if (!retTypeValue) {
      $("#operReturnType").combobox({ required: true, missingMessage: "Campo requerido" });
      valid = false;
    }
  }
  
  // Invalid Parameter
  if (!this.stopEditingParameter()) {
    valid = false;
  }
  
  return valid;
};

/**
 * Creates a new empty parameter row in the table.
 * 
 * @author Gabriel Leonardo Diaz, 27.01.2014.
 */
CLASSOperationDialog.prototype.newParameter = function () {
  if (this.stopEditingParameter()) {
    $("#parametersTable").datagrid("insertRow", {row: { name: "", type: "", collection: "" }});
    var newParamIndex = $("#parametersTable").datagrid("getRows").length - 1;
    this.startEditingParameter(newParamIndex);
    var ed = $("#parametersTable").datagrid("getEditor", {index : newParamIndex, field: "name" });
    $(ed.target).focus();
  }
};

/**
 * Deletes the selected parameter on the parameters table.
 * 
 * @author Gabriel Leonardo Diaz, 27.01.2014.
 */
CLASSOperationDialog.prototype.deleteParameter = function () {
  var selected = $("#parametersTable").datagrid("getSelected");
  if (selected == null) {
    return;
  }
  
  var index = $("#parametersTable").datagrid("getRowIndex", selected);
  if (index == null) {
    return;
  }
  
  $("#parametersTable").datagrid("cancelEdit", index);
  $("#parametersTable").datagrid("deleteRow", index);
};

/**
 * Saves the changes on the parameter being edited.
 * 
 * @author Gabriel Leonardo Diaz, 29.01.2014.
 */
CLASSOperationDialog.prototype.saveParameter = function () {
  if (this.stopEditingParameter()) {
    $("#parametersTable").datagrid("acceptChanges");
  }
};

/**
 * Starts the edition of the row on the table representing a parameter.
 * 
 * @author Gabriel Leonardo Diaz, 29.01.2014.
 */
CLASSOperationDialog.prototype.onClickParameter = function (index) {
  if (index == null) {
    return;
  }
  
  if (this.stopEditingParameter()) {
    this.startEditingParameter(index);
  }
};

/**
 * Begins the edition of the parameter represented by the given row. DO NOT CALL
 * THIS DIRECTLY.
 * 
 * @param index
 * @author Gabriel Leonardo Diaz, 29.01.2014.
 */
CLASSOperationDialog.prototype.startEditingParameter = function (index) {
  $("#parametersTable").datagrid("beginEdit", index);
  $("#parametersTable").datagrid("selectRow", index);
  
  this.parameterIndex = index;
  
  var editor;
  var comboPanel;
  
  // Workaround: The panel is shown behind of the PrimeFaces modal dialog.
  editor = $("#parametersTable").datagrid("getEditor", {index:index, field:"type"});
  comboPanel = $(editor.target).combobox("panel");
  comboPanel.panel("panel").css("z-index", "2000");
  
  editor = $("#parametersTable").datagrid("getEditor", {index:index, field:"collection"});
  comboPanel = $(editor.target).combobox("panel");
  comboPanel.panel("panel").css("z-index", "2000");
};

/**
 * Stops the edition of the parameter, applies the changes on the table.
 * 
 * @author Gabriel Leonardo Diaz, 29.01.2014.
 * @returns {Boolean}
 */
CLASSOperationDialog.prototype.stopEditingParameter = function () {
  if (this.parameterIndex == null) {
    return true;
  }
  
  if ($("#parametersTable").datagrid("validateRow", this.parameterIndex)) {
    $("#parametersTable").datagrid("endEdit", this.parameterIndex);
    this.parameterIndex = null;
    return true;
  }
  
  return false;
};

/**
 * Shows up the dialog to the user.
 * 
 * @author Gabriel Leonardo Diaz, 26.01.2014.
 */
CLASSOperationDialog.prototype.show = function () {
  this.dialog.show();
};