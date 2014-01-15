/*******************************************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS 
 * Cúcuta, Colombia 
 * (c) 2014 by UFPS. All rights reserved.
 * 
 * @author: Gabriel Leonardo Diaz, 14.01.2014.
 * 
 ******************************************************************************/

CLASSPropertyGrid = function (editor) {
  this.editor = editor;
  this.graph  = editor.graph;
};

/**
 * Instance of the editor component (CLASSEditor).
 */
CLASSPropertyGrid.prototype.editor;

/**
 * Instance of the UML element being edited.
 */
CLASSPropertyGrid.prototype.element;

/**
 * Initializes the property grid component.
 * 
 * @author Gabriel Leonardo Diaz, 14.01.2014.
 */
CLASSPropertyGrid.prototype.init = function () {
  var self = this;
  
  this.graph.getSelectionModel().addListener(mxEvent.CHANGE, function(sender, evt) {
    self.selectionChanged(sender, evt);
  });
  
  this.clearGrid();
};

/**
 * Process the selection changed event of the Graph Model.
 * 
 * @param sender
 *          The object sender.
 * @param evt
 *          The event.
 * @author Gabriel Leonardo Diaz, 14.01.2014.
 */
CLASSPropertyGrid.prototype.selectionChanged = function (sender, evt) {
  var cells = this.graph.selectionModel.cells;
  
  if (cells == null || cells.length != 1) {
    this.clearGrid();
  }
  else {
    this.configureGrid(cells[0]);
  }
};

/**
 * Clears the property grid and removes the edited element.
 * 
 * @author Gabriel Leonardo Diaz, 14.01.2014.
 */
CLASSPropertyGrid.prototype.clearGrid = function () {
  this.configureGrid(null);
};

/**
 * Clears the Property Grid by setting empty values and removing the editor.
 * 
 * @param cell
 *          The cell to be edited. If the cell is null the grid is cleared.
 * @author Gabriel Leonardo Diaz, 14.01.2014.
 */
CLASSPropertyGrid.prototype.configureGrid = function (cell) {
  var nameValue        = null;
  var visibilityValue  = null;
  var stereotypeValue  = null;
  var attributesValue  = null;
  var operationsValue  = null;
  var abstractValue    = null;
  var rootValue        = null;
  var leafValue        = null;
  var specValue        = null;
  var widthValue       = null;
  var heightValue      = null;
  var xValue           = null;
  var yValue           = null;
  var commentValue     = null;
  
  var textEditor       = null;
  var booleanEditor    = null;
  var spinnerEditor    = null;
  var visibilityEditor = null;
  var stereotypeEditor = null;
  var attributesEditor = null;
  var operationsEditor = null;
  var commentEditor    = null;
  
  this.element         = null;
  
  if (cell != null && cell.value != null) {
    this.element = cell.value;
    
    nameValue = this.element.getAttribute("name");
    visibilityValue = this.element.getAttribute("visibility");
    abstractValue = this.element.getAttribute("isAbstract");
    rootValue = this.element.getAttribute("isRoot");
    leafValue = this.element.getAttribute("isLeaf");
    specValue = this.element.getAttribute("isSpec");
    
    textEditor = "text";
    booleanEditor = {"type":"checkbox", "options":{"on":true, "off":false}};
    visibilityEditor = {"type":"combobox", "options": {
      "valueField":"id",
      "textField":"text",
      "data":[{"id":"public","text":"public"},
              {"id":"protected","text":"protected"},
              {"id":"package","text":"package"},
              {"id":"private","text":"private"}]
    }};
  }
  
  var jSonData = [// GENERAL
                  {"name":"Nombre", "value":nameValue, "group":"General", "editor":textEditor},
                  {"name":"Visibilidad", "value":visibilityValue, "group":"General", "editor":visibilityEditor},
                  {"name":"Estereotipo", "value":stereotypeValue, "group":"General", "editor":stereotypeEditor},
                  {"name":"Atributos", "value":attributesValue, "group":"General", "editor":attributesEditor},
                  {"name":"Metodos", "value":operationsValue, "group":"General", "editor":operationsEditor},
                  
                  // ADVANCED
                  {"name":"Es Abstracto", "value":abstractValue, "group":"Avanzado", "editor":booleanEditor},
                  {"name":"Es Raiz", "value":rootValue, "group":"Avanzado", "editor":booleanEditor},
                  {"name":"Es Hoja", "value":leafValue, "group":"Avanzado", "editor":booleanEditor},
                  {"name":"Es Especificacion", "value":specValue, "group":"Avanzado", "editor":booleanEditor},
                  
                  // GEOMETRY
                  {"name":"Ancho", "value":widthValue, "group":"Geometria", "editor":spinnerEditor},
                  {"name":"Alto", "value":heightValue, "group":"Geometria", "editor":spinnerEditor},
                  {"name":"X", "value":xValue, "group":"Geometria", "editor":spinnerEditor},
                  {"name":"Y", "value":yValue, "group":"Geometria", "editor":spinnerEditor},
                  
                  // COMMENT
                  {"name":"Comentario", "value":commentValue, "group":"Detalles", "editor":commentEditor}];
  
  $('#propertyTable').propertygrid({ 
      data: jSonData,
      showGroup: true,
      showHeader: false,
      scrollbarSize: 0
  });
};

