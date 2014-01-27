/*******************************************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * C�cuta, Colombia
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
  
  this.configureOperationsTable();
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
 * Initializes the dialog to edit the operations of the given classifier cell.
 * @param cell
 */
CLASSOperations.prototype.init = function (cell) {
  this.classifierCell = cell;
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
      
      onUnSelect: function (rowIndex, rowData) {
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
      jSonData.push({id:cell.value.getAttribute("name"), text: cell.value.getAttribute("name")});
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
  var comboPanel = $("#operReturnType").combobox('panel');
  comboPanel.panel('panel').css('z-index', '2000');
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
      panelHeight: 80,
      data: [
          {id:"secuencial",    text:"secuencial"},
          {id:"protegido",     text:"protegido"},
          {id:"concurrente",   text:"concurrente"}
      ]
  });
  
  $("#operConcurrency").combobox("setValue", "secuencial"); // default value
  
  // Workaround: The panel is shown behind of the PrimeFaces modal dialog.
  var comboPanel = $("#operConcurrency").combobox('panel');
  comboPanel.panel('panel').css('z-index', '2000');
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
  // TODO GD
};

/**
 * Clears the fields and prepares the dialog to create a new operation.
 * 
 * @author Gabriel Leonardo Diaz, 26.01.2014.
 */
CLASSOperations.prototype.newOperation = function () {
  // TODO GD
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
  }
};

/**
 * Saves the changes made over an operation, or creates a new one.
 * 
 * @author Gabriel Leonardo Diaz, 26.01.2014.
 */
CLASSOperations.prototype.saveOperation = function () {
  // TODO GD
};

/**
 * Shows up the dialog to the user.
 * 
 * @author Gabriel Leonardo Diaz, 26.01.2014.
 */
CLASSOperations.prototype.show = function () {
  this.dialog.show();
};