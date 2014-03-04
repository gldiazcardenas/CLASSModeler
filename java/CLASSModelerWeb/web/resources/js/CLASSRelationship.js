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
  this.editor      = editor;
  this.graph       = editor.graph;
  this.dialog      = dlgRelationship; // Defined by a PrimeFaces dialog.
  this.title       = dlgRelationship.titlebar.children("span.ui-dialog-title").html();
  this.sourceTitle = $("#sourceFields").children("legend.ui-fieldset-legend").html();
  this.targetTitle = $("#targetFields").children("legend.ui-fieldset-legend").html();
  
  this.configureDirectionCombo();
  this.configureMultiplicityCombo("sourceMultiplicity");
  this.configureMultiplicityCombo("targetMultiplicity");
  this.configureVisibilityCombo("sourceVisibility");
  this.configureVisibilityCombo("targetVisibility");
  this.configureCollectionsCombo("sourceCollectionType");
  this.configureCollectionsCombo("targetCollectionType");
  this.configureAttributesCombo("sourceAttribute");
  this.configureAttributesCombo("targetAttribute");
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
 * The localized text for the source property title.
 */
CLASSRelationship.prototype.sourceTitle;

/**
 * The localized text for the target property title.
 */
CLASSRelationship.prototype.targetTitle;

/**
 * Initializes the dialog to edit relationship contained by the given cell.
 * 
 * @param cell
 * @author Gabriel Leonardo Diaz, 19.02.2014.
 */
CLASSRelationship.prototype.init = function (cell) {
  this.relationshipCell = cell;
  
  this.loadNameTextFieldData();
  this.loadDirectionComboData();
  
  this.loadAttributesComboData("sourceAttribute", cell.source);
  this.loadAttributesComboData("targetAttribute", cell.target);
  
  this.setTitle();
  this.setSourceTargetTitle();
};

/**
 * Loads the name of the relationship in the text field.
 * 
 * @author Gabriel Leonardo Diaz, 22.02.2014.
 */
CLASSRelationship.prototype.loadNameTextFieldData = function () {
  $("#relName").val(this.relationshipCell.getAttribute("name"));
};

/**
 * Loads the direction combo box value.
 * 
 * @author Gabriel Leonardo Diaz, 22.02.2014.
 */
CLASSRelationship.prototype.loadDirectionComboData = function () {
  if (this.graph.isGeneralization(this.relationshipCell.value) || this.graph.isRealization(this.relationshipCell.value)) {
    $("#relDirection").combobox("setValue", "");
    $("#relDirection").combobox("disable");
    return;
  }
  
  // TODO GD get the styles of the edge and check the startArrow and endArrow values.
};

/**
 * Loads the data for the attributes combo box component with the attributes of
 * the given classifier.
 * 
 * @param comboId
 * @param classifier
 * @author Gabriel Leonardo Diaz, 20.02.2014.
 */
CLASSRelationship.prototype.loadAttributesComboData = function (comboId, classifier) {
  comboId = "#" + comboId;
  
  var jSonData = [];
  
  var attributes = this.graph.getAttributes(classifier);
  if (attributes) {
    var attribute;
    for (var i = 0; i < attributes.length; i++) {
      attribute = attributes[i];
      jSonData.push({id: attribute.getAttribute("name"), text: attribute.getAttribute("name")});
    }
  }
  
  $(comboId).combobox({"data" : jSonData});
};

CLASSRelationship.prototype.loadVisibilityComboData = function (comboId, classifier) {
  
};

/**
 * Configures the combo box for direction property.
 * 
 * @author Gabriel Leonardo Diaz, 20.02.2014.
 */
CLASSRelationship.prototype.configureDirectionCombo = function () {
  $("#relDirection").combobox({
      valueField:"id",
      textField:"text",
      panelHeight: 90,
      width: 300,
      data: [
          {id:"unspecified",     text:"Indeterminado"},
          {id:"bidirectional",   text:"Bidireccional"},
          {id:"sourcetarget",    text:"Origen -> Final"},
          {id:"targetsource",    text:"Final -> Origen"}
      ]
  });
  
  // Workaround: The panel is shown behind of the PrimeFaces modal dialog.
  var comboPanel = $("#relDirection").combobox("panel");
  comboPanel.panel("panel").css("z-index", "2000");
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
      data: [
          {id:"array",      text:"[ ]"},
          {id:"list",       text:"List"},
          {id:"listarray",  text:"ArrayList"},
          {id:"listlinked", text:"LinkedList"},
          {id:"set",        text:"Set"},
          {id:"sethash",    text:"HashSet"},
          {id:"vector",     text:"Vector"}
      ]
  });
  
  // Workaround: The panel is shown behind of the PrimeFaces modal dialog.
  var comboPanel = $(elementId).combobox("panel");
  comboPanel.panel("panel").css("z-index", "2000");
};

/**
 * Configures the attributes combo box for the element identified by the given
 * ID.
 * 
 * @param elementId
 * @author Gabriel Leonardo Diaz, 20.02.2014.
 */
CLASSRelationship.prototype.configureAttributesCombo = function (elementId) {
  elementId = "#" + elementId;
  
  $(elementId).combobox({
      valueField:"id",
      textField:"text",
      panelHeight: 90,
      width: 300,
      data: []
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
    relationshipName = "Agregacion";
  }
  else if (this.graph.isComposition(this.relationshipCell.value)) {
    relationshipName = "Composicion";
  }
  else {
    relationshipName = "Asociacion";
  }
  
  this.dialog.titlebar.children("span.ui-dialog-title").html(this.title.replace("{0}", relationshipName));
};

/**
 * Sets the title of the source and target fieldsets.
 * 
 * @author Gabriel Leonardo Diaz, 20.02.2014.
 */
CLASSRelationship.prototype.setSourceTargetTitle = function () {
  var sourceCell = this.relationshipCell.source;
  var targetCell = this.relationshipCell.target;
  $("#sourceFields").children("legend.ui-fieldset-legend").html(this.sourceTitle.replace("{0}", "<b>" + sourceCell.getAttribute("name") + "</b>"));
  $("#targetFields").children("legend.ui-fieldset-legend").html(this.targetTitle.replace("{0}", "<b>" + targetCell.getAttribute("name") + "</b>"));
};

/**
 * Shows up the dialog to the user.
 * 
 * @author Gabriel Leonardo Diaz, 26.01.2014.
 */
CLASSRelationship.prototype.show = function () {
  this.dialog.show();
};