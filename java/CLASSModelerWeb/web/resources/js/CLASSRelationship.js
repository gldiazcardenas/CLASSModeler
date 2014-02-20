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
CLASSRelationship = function (editor) {
  this.editor = editor;
  this.graph  = editor.graph;
  this.dialog = dlgRelationship; // Defined by a PrimeFaces dialog.
  this.title  = dlgRelationship.titlebar.children("span.ui-dialog-title").html();
  
//  var self    = this;
//  this.dialog.content[0].onclick = function () {
//    self.stopEditingParameter();
//  };
  
//  this.configureOperationsTable();
//  this.configureVisibilityCombo();
//  this.configureParametersTable();
//  this.configureReturnTypeComboBox();
//  this.configureConcurrencyComboBox();
};

/**
 * The instance of the relationship being edited.
 */
CLASSRelationship.prototype.relationshipCell;

/**
 * Initializes the dialog to edit relationship contained by the given cell.
 * 
 * @param cell
 * @author Gabriel Leonardo Diaz, 19.02.2014.
 */
CLASSRelationship.prototype.init = function (cell) {
  this.relationshipCell = cell;
  this.setTitle();
};

/**
 * Sets the title of the dialog by appending the name of the relationship being
 * edited.
 * 
 * @author Gabriel Leonardo Diaz, 10.02.2014.
 */
CLASSRelationship.prototype.setTitle = function () {
  this.dialog.titlebar.children("span.ui-dialog-title").html(this.title.replace("{0}", this.relationshipCell.getAttribute("name")));
};

/**
 * Shows up the dialog to the user.
 * 
 * @author Gabriel Leonardo Diaz, 26.01.2014.
 */
CLASSRelationship.prototype.show = function () {
  this.dialog.show();
};