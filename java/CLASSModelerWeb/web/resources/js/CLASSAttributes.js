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
  
  $('#attributesTable').datagrid({
      toolbar: [
          { iconCls: 'icon-add', handler: function() { self.addAttribute(); }},
          { iconCls: 'icon-remove', handler: function() { self.deleteAttribute(); }},
          '-',
          { iconCls: 'icon-save', handler: function() { self.saveAttribute(); }}
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
 * Adds a new attribute to the edited classifier.
 * 
 * @author Gabriel Leonardo Diaz, 18.01.2014.
 */
CLASSAttributes.prototype.addAttribute = function () {
  // TODO GD
};

/**
 * Deletes the selected attribute over the table.
 * 
 * @author Gabriel Leonardo Diaz, 18.01.2014.
 */
CLASSAttributes.prototype.deleteAttribute = function () {
  // TODO GD
};

/**
 * Saves the changes over the edited attribute.
 * 
 * @author Gabriel Leonardo Diaz, 18.01.2014.
 */
CLASSAttributes.prototype.saveAttribute = function () {
  // EDIT
  if (this.attributeCell) {
    
    
  }
  
  // CREATE
  else {
    var newAttribute = docXML.createElement("Property");
    newAttribute.setAttribute("name", "");
    newAttribute.setAttribute("type", "");
    newAttribute.setAttribute("visibility", "");
  }
};

/**
 * Shows up the dialog to the user.
 * 
 * @author Gabriel Leonardo Diaz, 16.01.2014.
 */
CLASSAttributes.prototype.show = function () {
  this.dialog.show();
};
