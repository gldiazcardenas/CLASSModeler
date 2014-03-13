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
CLASSRelationship = function (editor) {
  this.editor      = editor;
  this.graph       = editor.graph;
  this.dialog      = dlgRelationship; // Defined by a PrimeFaces dialog.
  this.title       = dlgRelationship.titlebar.children("span.ui-dialog-title").html();
  
  this.configureMultiplicityCombo("sourceMultiplicity");
  this.configureMultiplicityCombo("targetMultiplicity");
  this.configureVisibilityCombo("sourceVisibility");
  this.configureVisibilityCombo("targetVisibility");
  this.configureCollectionsCombo("sourceCollectionType");
  this.configureCollectionsCombo("targetCollectionType");
};

/**
 * The instance of the relationship being edited.
 */
CLASSRelationship.prototype.relationshipCell;

/**
 * The localized text for the dialog title.
 */
CLASSRelationship.prototype.title;

/**
 * Initializes the dialog to edit relationship contained by the given cell.
 * 
 * @param cell
 * @author Gabriel Leonardo Diaz, 19.02.2014.
 */
CLASSRelationship.prototype.init = function (cell) {
  this.relationshipCell = cell;
  
  $("#relName").val(this.relationshipCell.getAttribute("name"));
  
  this.loadSource();
  this.loadTarget();
  this.setTitle();
};

CLASSRelationship.prototype.loadSource = function () {
  
};

CLASSRelationship.prototype.loadTarget = function () {
  
};

/**
 * Configures the multiplicity combo box for the element identified by the given
 * ID.
 * 
 * @param elementId
 * @author Gabriel Leonardo Diaz, 20.02.2014.
 */
CLASSRelationship.prototype.configureMultiplicityCombo = function (elementId) {
  elementId = "#" + elementId;
  
  $(elementId).combobox({
      valueField:"id",
      textField:"text",
      panelHeight: 90,
      width: 300,
      data: [
          {id:"many",        text:"*"},
          {id:"zero",        text:"0"},
          {id:"zeromany",    text:"0..*"},
          {id:"zeroone",     text:"0..1"},
          {id:"one",         text:"1"},
          {id:"onemany",     text:"1..*"},
      ]
  });
  
  $(elementId).combobox("setValue", "1"); // default value
  
  // Workaround: The panel is shown behind of the PrimeFaces modal dialog.
  var comboPanel = $(elementId).combobox("panel");
  comboPanel.panel("panel").css("z-index", "2000");
};

/**
 * Configures the visibility combo box for the element identified by the given
 * ID.
 * 
 * @param elementId
 * @author Gabriel Leonardo Diaz, 20.02.2014.
 */
CLASSRelationship.prototype.configureVisibilityCombo = function (elementId) {
  elementId = "#" + elementId;
  
  $(elementId).combobox({
      valueField:"id",
      textField:"text",
      panelHeight: 90,
      width: 300,
      data: this.graph.getVisibilityJSon()
  });
  
  $(elementId).combobox("setValue", "private"); // default value
  
  // Workaround: The panel is shown behind of the PrimeFaces modal dialog.
  var comboPanel = $(elementId).combobox("panel");
  comboPanel.panel("panel").css("z-index", "2000");
};

/**
 * Configures the collections combo box for the element identified by the given
 * ID.
 * 
 * @param elementId
 * @author Gabriel Leonardo Diaz, 20.02.2014.
 */
CLASSRelationship.prototype.configureCollectionsCombo = function (elementId) {
  elementId = "#" + elementId;
  
  $(elementId).combobox({
      valueField:"id",
      textField:"text",
      panelHeight: 90,
      width: 300,
      data: this.graph.getCollectionsJSon()
  });
  
  // Workaround: The panel is shown behind of the PrimeFaces modal dialog.
  var comboPanel = $(elementId).combobox("panel");
  comboPanel.panel("panel").css("z-index", "2000");
};

/**
 * Sets the title of the dialog by appending the name of the relationship being
 * edited.
 * 
 * @author Gabriel Leonardo Diaz, 10.02.2014.
 */
CLASSRelationship.prototype.setTitle = function () {
  var relationshipName = "";
  
  if (this.graph.isAggregation(this.relationshipCell.value)) {
    relationshipName = "Agregaci&oacute;n";
  }
  else if (this.graph.isComposition(this.relationshipCell.value)) {
    relationshipName = "Composici&oacute;n";
  }
  else {
    relationshipName = "Asociaci&oacute;n";
  }
  
  this.dialog.titlebar.children("span.ui-dialog-title").html(this.title.replace("{0}", relationshipName));
};

/**
 * Shows up the dialog to the user.
 * 
 * @author Gabriel Leonardo Diaz, 26.01.2014.
 */
CLASSRelationship.prototype.show = function () {
  this.dialog.show();
};