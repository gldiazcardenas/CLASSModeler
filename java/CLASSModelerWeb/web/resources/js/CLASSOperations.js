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
CLASSOperations = function (editor) {
  this.editor = editor;
  this.graph  = editor.graph;
  this.dialog = dlgOperations; // Defined by a PrimeFaces dialog.
  
  var self    = this;
  this.dialog.content[0].onclick = function () {
    self.stopEditingParameter();
  };
  
  this.configureOperationsTable();
  this.configureVisibilityCombo();
  this.configureParametersTable();
  this.configureReturnTypeComboBox();
  this.configureConcurrencyComboBox();
};

/**
 * Instance of the editor component.
 */
CLASSOperations.prototype.editor;

/**
 * Instance of the graph component.
 */
CLASSOperations.prototype.graph;

/**
 * The dialog to show.
 */
CLASSOperations.prototype.dialog;

/**
 * The instance of the classifier being edited.
 */
CLASSOperations.prototype.classifierCell;

/**
 * The instance of the operation being edited.
 */
CLASSOperations.prototype.operationCell;

/**
 * The index on the table of the operation being edited.
 */
CLASSOperations.prototype.operationIndex;

/**
 * The index on the table of the parameter being edited.
 */
CLASSOperations.prototype.parameterIndex;

/**
 * Initializes the dialog to edit the operations of the given classifier cell.
 * @param cell
 */
CLASSOperations.prototype.init = function (cell) {
  this.classifierCell = cell;
  this.operationCell  = null;
  this.operationIndex = null;
  this.loadOperationTableData();
  this.clearSelection();
  this.clearFields();
};

/**
 * Creates the operations table component.
 * 
 * @author Gabriel Leonardo Diaz, 17.01.2014.
 */
CLASSOperations.prototype.configureOperationsTable = function () {
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
CLASSOperations.prototype.loadOperationTableData = function () {
  var jSonData = [];
  
  var operations = this.graph.getOperations(this.classifierCell);
  
  if (operations) {
    for (var i = 0; i < operations.length; i++) {
      var visibility   = operations[i].getAttribute("visibility");
      var nameValue    = operations[i].getAttribute("name");
      var retTypeValue = operations[i].getAttribute("returnType");
      var paramValue   = this.graph.convertParametersToString(operations[i]);
      
      jSonData.push({name: this.graph.getVisibilityChar(visibility) + " " + nameValue, type: retTypeValue, parameters: paramValue});
    }
  }
  
  $("#operationsTable").datagrid({ data : jSonData });
  $("#operationsTable").datagrid("reload");
};

/**
 * Populates and sets up the combo box component for visibility edition.
 * 
 * @author Gabriel Leonardo Diaz, 17.01.2014.
 */
CLASSOperations.prototype.configureVisibilityCombo = function () {
  $("#operVisibility").combobox({
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
CLASSOperations.prototype.configureParametersTable = function () {
  var self      = this;
  var jSonTypes = this.graph.getTypes();
  var jSonDirs  = [];
  
  jSonDirs.push({id:"in",     text:"in"});
  jSonDirs.push({id:"inout",  text:"inout"});
  jSonDirs.push({id:"out",    text:"out"});
  jSonDirs.push({id:"return", text:"return"});
  
  $("#parametersTable").datagrid({
      toolbar: "#paramsTbToolbar",
      singleSelect: true,
      
      onClickRow: function (index) {
        self.onClickParameter(index);
      },
      
      columns:[[
          {field:"name", title:"Nombre",    width:150, editor:"text"},
          {field:"type", title:"Tipo",      width:100, editor: {type:"combobox", options: {valueField:"id",  textField:"text", panelHeight: 200, data:jSonTypes, required: true}}},
          {field:"dir",  title:"Direccion", width:100, editor: {type:"combobox", options: {valueField:"id", textField:"text", panelHeight: 90, data:jSonDirs, required: true}}}
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
CLASSOperations.prototype.configureReturnTypeComboBox = function () {
  var jSonData   = [];
  
  // Fixed types
  jSonData.push({id:"void",    text:"void"});
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
      jSonData.push({id:cell.getAttribute("name"), text: cell.getAttribute("name")});
    }
  }
  
  $("#operReturnType").combobox({
      valueField:"id",
      textField:"text",
      panelHeight: 200,
      data: jSonData
  });
  
  $("#operReturnType").combobox("setValue", "void"); // default value
  
  // Workaround: The panel is shown behind of the PrimeFaces modal dialog.
  var comboPanel = $("#operReturnType").combobox("panel");
  comboPanel.panel("panel").css("z-index", "2000");
};

/**
 * Constructs the combo box for concurrency field.
 * 
 * @author Gabriel Leonardo Diaz, 26.01.2014.
 */
CLASSOperations.prototype.configureConcurrencyComboBox = function () {
  $("#operConcurrency").combobox({
      valueField:"id",
      textField:"text",
      panelHeight: 70,
      data: [
          {id:"secuencial",    text:"secuencial"},
          {id:"protegido",     text:"protegido"},
          {id:"concurrente",   text:"concurrente"}
      ]
  });
  
  $("#operConcurrency").combobox("setValue", "secuencial"); // default value
  
  // Workaround: The panel is shown behind of the PrimeFaces modal dialog.
  var comboPanel = $("#operConcurrency").combobox("panel");
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
CLASSOperations.prototype.selectionChanged = function (rowIndex, selected) {
  this.clearFields();
  
  if (selected) {
    this.operationCell  = this.graph.getOperations(this.classifierCell)[rowIndex];
    this.operationIndex = rowIndex;
    
    $("#operName").val(this.operationCell.getAttribute("name"));
    $("#operStaticCheck").prop("checked", this.operationCell.getAttribute("isStatic") == "true");
    $("#operFinalCheck").prop("checked", this.operationCell.getAttribute("isFinal") == "true");
    $("#operAbstractCheck").prop("checked", this.operationCell.getAttribute("isAbstract") == "true");
    $("#operSyncCheck").attr("checked", this.operationCell.getAttribute("isSync") == "true");
    $("#operReturnType").combobox("setValue", this.operationCell.getAttribute("returnType"));
    $("#operConcurrency").combobox("setValue", this.operationCell.getAttribute("concurrency"));
    
    var jSonData = [];
    
    if (this.operationCell.value.childNodes) {
      var param;
      for (var i = 0; i < this.operationCell.value.childNodes.length; i++) {
        param = this.operationCell.value.childNodes[i];
        
        var nameValue = param.getAttribute("name");
        var typeValue = param.getAttribute("type");
        var dirValue  = param.getAttribute("direction");
        
        jSonData.push({name: nameValue, type: typeValue, dir: dirValue});
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
CLASSOperations.prototype.clearSelection = function () {
  $("#operationsTable").datagrid("unselectAll");
  this.operationIndex = null;
  this.operationCell  = null;
};

/**
 * Clears the fields used to edit or create operations.
 * 
 * @author Gabriel Leonardo Diaz, 27.01.2014.
 */
CLASSOperations.prototype.clearFields = function () {
  $("#operName").val("");
  $("#operStaticCheck").prop("checked", false);
  $("#operFinalCheck").prop("checked", false);
  $("#operAbstractCheck").prop("checked", false);
  $("#operSyncCheck").prop("checked", false);
  $("#parametersTable").datagrid({data:[]});
  $("#parametersTable").datagrid("reload");
  $("#operReturnType").combobox("setValue", "void");
  $("#operVisibility").combobox("setValue", "public");
  $("#operConcurrency").combobox("setValue", "secuencial");
};

/**
 * Clears the fields and prepares the dialog to create a new operation.
 * 
 * @author Gabriel Leonardo Diaz, 26.01.2014.
 */
CLASSOperations.prototype.newOperation = function () {
  this.clearSelection();
  this.clearFields();
};

/**
 * Deletes the selected operation.
 * 
 * @author Gabriel Leonardo Diaz, 26.01.2014.
 */
CLASSOperations.prototype.deleteOperation = function () {
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
CLASSOperations.prototype.saveOperation = function () {
  var nameValue       = $("#operName").val();
  var visValue        = $("#operVisibility").combobox("getValue");
  var retTypeValue    = $("#operReturnType").combobox("getValue");
  var concurValue     = $("#operConcurrency").combobox("getValue");
  var staticValue     = $("#operStaticCheck").is(":checked");
  var finalValue      = $("#operFinalCheck").is(":checked");
  var abstractValue   = $("#operAbstractCheck").is(":checked");
  var syncValue       = $("#operSyncCheck").is(":checked");
  
  if (nameValue == null || nameValue.length == 0) {
    // Invalid NAME
    return;
  }
  
  if (visValue == null || visValue.length == 0) {
    // Invalid VISIBILITY
    return;
  }
  
  if (retTypeValue == null || retTypeValue.length == 0) {
    // Invalid Return Type
    return;
  }
  
  if (concurValue == null || concurValue.length == 0) {
    // Invalid Concurrency
    return;
  }
  
  if (!this.stopEditingParameter()) {
    // Invalid Parameter
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
  operation.setAttribute("returnType", retTypeValue);
  operation.setAttribute("concurrency", concurValue);
  operation.setAttribute("isStatic", staticValue);
  operation.setAttribute("isFinal", finalValue);
  operation.setAttribute("isAbstract", abstractValue);
  operation.setAttribute("isSync", syncValue);
  operation.value.innerHTML = ""; // Remove all child nodes
  
  var paramRows = $("#parametersTable").datagrid("getRows");
  if (paramRows) {
    var row;
    var cell;
    var param;
    
    for (var i = 0; i < paramRows.length; i++) {
      row = paramRows[i];
      cell = this.graph.model.cloneCell(this.editor.getTemplate("parameter"));
      
      param = cell.value;
      param.setAttribute("name", row.name);
      param.setAttribute("type", row.type);
      param.setAttribute("direction", row.dir);
      
      operation.value.appendChild(param);
    }
  }
  
  var paramsValue = this.graph.convertParametersToString(operation);
  var visChar     = this.graph.getVisibilityChar(visValue);
  
  // Apply changes
  if (this.operationCell) {
    this.graph.editOperation(this.operationCell, operation);
    $("#operationsTable").datagrid("updateRow", {index: this.operationIndex, row: { name: visChar + " " + nameValue, type: retTypeValue, parameters:  paramsValue}});
  }
  else {
    this.graph.addOperation(this.classifierCell, operation);
    $("#operationsTable").datagrid("insertRow", {row: { name: visChar + " " + nameValue, type: retTypeValue, parameters: paramsValue }});
    $("#operationsTable").datagrid("selectRow", $("#operationsTable").datagrid("getRows").length - 1);
  }
  
  $("#operationsTable").datagrid("reload");
};

/**
 * Creates a new empty parameter row in the table.
 * 
 * @author Gabriel Leonardo Diaz, 27.01.2014.
 */
CLASSOperations.prototype.newParameter = function () {
  if (this.stopEditingParameter()) {
    $("#parametersTable").datagrid("insertRow", {row: { name: "", type: "", dir: "in" }});
    this.startEditingParameter($('#parametersTable').datagrid("getRows").length - 1);
  }
};

/**
 * Deletes the selected parameter on the parameters table.
 * 
 * @author Gabriel Leonardo Diaz, 27.01.2014.
 */
CLASSOperations.prototype.deleteParameter = function () {
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
CLASSOperations.prototype.saveParameter = function () {
  if (this.stopEditingParameter()) {
    $("#parametersTable").datagrid("acceptChanges");
  }
};

/**
 * Starts the edition of the row on the table representing a parameter.
 * 
 * @author Gabriel Leonardo Diaz, 29.01.2014.
 */
CLASSOperations.prototype.onClickParameter = function (index) {
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
CLASSOperations.prototype.startEditingParameter = function (index) {
  $("#parametersTable").datagrid("beginEdit", index);
  $("#parametersTable").datagrid("selectRow", index);
  
  this.parameterIndex = index;
  
  var editor;
  var comboPanel;
  
  // Workaround: The panel is shown behind of the PrimeFaces modal dialog.
  editor = $("#parametersTable").datagrid("getEditor", {index:index, field:"type"});
  comboPanel = $(editor.target).combobox("panel");
  comboPanel.panel("panel").css("z-index", "2000");
  
  editor = $("#parametersTable").datagrid("getEditor", {index:index, field:"dir"});
  comboPanel = $(editor.target).combobox("panel");
  comboPanel.panel("panel").css("z-index", "2000");
};

/**
 * Stops the edition of the parameter, applies the changes on the table.
 * 
 * @author Gabriel Leonardo Diaz, 29.01.2014.
 * @returns {Boolean}
 */
CLASSOperations.prototype.stopEditingParameter = function () {
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
CLASSOperations.prototype.show = function () {
  this.dialog.show();
};