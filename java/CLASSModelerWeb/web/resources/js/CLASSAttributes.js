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
  this.classifierCell = cell;
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
  
  var jSonData = [];
  
  var cell;
  var node;
  
  for (var i = 0; this.classifierCell.children && i < this.classifierCell.children.length; i++) {
    cell = this.classifierCell.children[i];
    node = cell.value;
    
    if (this.graph.isProperty(node)) {
      var visibility = node.getAttribute("visibility");
      var name = node.getAttribute("name");
      var type = node.getAttribute("type");
      var initialValue = node.getAttribute("initialValue");
      
      jSonData.push({f1: this.graph.getVisibilityChar(visibility) + " " + name, f2:type, f3:initialValue});
    }
  }
  
  $('#attributesTable').datagrid({
      toolbar: [
          { iconCls: 'icon-add', handler: function() { self.newAttribute(); }},
          { iconCls: 'icon-remove', handler: function() { self.deleteAttribute(); }},
          '-',
          { iconCls: 'icon-save', handler: function() { self.saveAttribute(); }}
      ],
      
      data: jSonData,
      
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
CLASSAttributes.prototype.newAttribute = function () {
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
    var template = this.editor.getTemplate("property");
    var newCell = this.graph.model.cloneCell(template);
    newCell.setVertex(true);
    
    var attribute = newCell.value;
    attribute.setAttribute("name", "myAttribute");
    attribute.setAttribute("type", "int");
    attribute.setAttribute("visibility", "private");
    
    this.graph.addClassifierAttribute (this.classifierCell, newCell);
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
