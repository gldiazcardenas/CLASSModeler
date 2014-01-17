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
CLASSAttributesDialog = function (graph) {
  this.graph = graph;
};

/**
 * Instance of the graph being edited.
 */
CLASSAttributesDialog.prototype.graph;

/**
 * Initializes the dialog with the attributes of the given cell containing a
 * classifier XML node.
 * 
 * @param cell
 *          The cell containing the classifier to be edited.
 * @author Gabriel Leonardo Diaz, 16.01.2014.
 */
CLASSAttributesDialog.prototype.init = function (cell) {
  // TODO GD
  $('#attributesTable').datagrid({
      toolbar: [
          { iconCls: 'icon-add', handler: function() { alert('add'); }},
          { iconCls: 'icon-save', handler: function() { alert('edit'); }},
          { iconCls: 'icon-remove', handler: function() { alert('remove'); }}
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
CLASSAttributesDialog.prototype.show = function () {
  dlgAttributes.show();
};
