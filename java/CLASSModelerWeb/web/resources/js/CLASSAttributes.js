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
CLASSAttributes = function (graph) {
  this.graph  = graph;
  this.dialog = dlgAttributes; // Defined by a PrimeFaces dialog.
};

/**
 * Instance of the graph being edited.
 */
CLASSAttributes.prototype.graph;

/**
 * Instance of the dialog to handle.
 */
CLASSAttributes.prototype.dialog;

/**
 * Instance of the cell being edited.
 */
CLASSAttributes.prototype.cell;

/**
 * Initializes the dialog with the attributes of the given cell containing a
 * classifier XML node.
 * 
 * @param cell
 *          The cell containing the classifier to be edited.
 * @author Gabriel Leonardo Diaz, 16.01.2014.
 */
CLASSAttributes.prototype.init = function (cell) {
  this.cell = cell;
  this.configureVisibilityCombo();
  this.configureTypeCombo();
  this.configureMultiplicityCombo();
  this.configureAttributesTable();
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
      data: [
          {id:"boolean", text:"boolean"},
          {id:"byte",    text:"byte"},
          {id:"char",    text:"char"},
          {id:"double",  text:"double"},
          {id:"float",   text:"float"},
          {id:"int",     text:"int"},
          {id:"long",    text:"long"},
          {id:"short",   text:"short"},
          {id:"String",  text:"String"}
      ]
  });
  
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
  $("#attrMultiplicity").combobox({
      valueField:"id",
      textField:"text",
      panelHeight: 130,
      data: [
          {id:"many",       text:"*"},
          {id:"zero",       text:"0"},
          {id:"zeroOrMany", text:"0...*"},
          {id:"zeroOrOne",  text:"0...1"},
          {id:"one",        text:"1"},
          {id:"oneOrMany",  text:"1...*"}
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
  $('#attributesTable').datagrid({
      toolbar: [
          { iconCls: 'icon-add', handler: function() { alert('add'); }},
          { iconCls: 'icon-remove', handler: function() { alert('remove'); }},
          '-',
          { iconCls: 'icon-save', handler: function() { alert('save'); }},
          '-',
          { iconCls: 'icon-up', handler: function() { alert('up'); }},
          { iconCls: 'icon-down', handler: function() { alert('down'); }}
      ],
      
      data: [
          {f1:'', f2:'', f3:''}
      ],
      
      columns:[[
          {field:'name',title:'Nombre',width:150},
          {field:'type',title:'Tipo',width:150},
          {field:'value',title:'Valor Inicial',width:150}
      ]]
  });
};

/**
 * Shows up the dialog to the user.
 * 
 * @author Gabriel Leonardo Diaz, 16.01.2014.
 */
CLASSAttributes.prototype.show = function () {
  this.dialog.show();
};
