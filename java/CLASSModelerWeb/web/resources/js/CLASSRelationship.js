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
  this.dialog.save = mxUtils.bind(this, function () {
    if (this.saveRelationship()) {
      this.dialog.hide();
    }
  });
  
  this.configureVisibilityCombo("sourceVisibility");
  this.configureVisibilityCombo("targetVisibility");
  this.configureCollectionsCombo("sourceCollection");
  this.configureCollectionsCombo("targetCollection");
  this.configureMultiplicityCombo("sourceMultiplicity");
  this.configureMultiplicityCombo("targetMultiplicity");
  this.configureNavigableCheckBoxes();
};

/**
 * The instance of the relationship being edited.
 */
CLASSRelationship.prototype.relationshipCell;

/**
 * Reference to the source property of the relationship.
 */
CLASSRelationship.prototype.sourceProperty;

/**
 * Reference to the target property of the relationship.
 */
CLASSRelationship.prototype.targetProperty;

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
  this.loadRelationship();
  this.loadSource();
  this.loadTarget();
  this.setTitle();
};

/**
 * Loads the name of the relationship in the text field.
 * 
 * @author Gabriel Leonardo Diaz, 16.03.2014.
 */
CLASSRelationship.prototype.loadRelationship = function () {
  $("#relName").val(this.relationshipCell.getAttribute("name"));
};

/**
 * Loads the fields in the source section.
 * 
 * @author Gabriel Leonardo Diaz, 14.03.2014.
 */
CLASSRelationship.prototype.loadSource = function () {
  this.sourceProperty = null;
  
  if (this.relationshipCell.children) {
    for (var i = 0; i < this.relationshipCell.children.length; i++) {
      var child = this.relationshipCell.children[i];
      if (child.getAttribute("type") == this.relationshipCell.source.id) {
        this.sourceProperty = child;
        break;
      }
    }
  }
  
  var sourceName         = "";
  var sourceVisibility   = "";
  var sourceCollection   = "";
  var sourceMultiplicity = "";
  var sourceNavigable    = false;
  
  if (this.sourceProperty) {
    sourceName         = this.sourceProperty.getAttribute("name");
    sourceVisibility   = this.sourceProperty.getAttribute("visibility");
    sourceCollection   = this.sourceProperty.getAttribute("collection");
    sourceMultiplicity = this.sourceProperty.getAttribute("multiplicity");
    sourceNavigable    = this.graph.isNavigable(this.sourceProperty);
  }
  
  $("#sourceName").val(sourceName);
  $("#sourceVisibility").combobox("setValue", sourceVisibility);
  $("#sourceType").val(this.graph.convertTypeIdToNameString(this.relationshipCell.source.id));
  $("#sourceCollection").combobox("setValue", sourceCollection);
  $("#sourceMultiplicity").combobox("setValue", sourceMultiplicity);
  $("#sourceNavigable").prop("checked", sourceNavigable);
  $("#sourceNavigable").attr("disabled", !this.graph.isClass(this.relationshipCell.target.value));
  this.setEnabledSource(sourceNavigable);
};

/**
 * Enables/Disables the source role controls.
 * 
 * @author Gabriel Leonardo Diaz, 15.03.2014.
 */
CLASSRelationship.prototype.setEnabledSource = function (enabled) {
  if (enabled) {
    $("#sourceName").removeAttr("disabled");
    $("#sourceVisibility").combobox("enable");
    $("#sourceCollection").combobox("enable");
    if (!$("#sourceVisibility").combobox("getValue")) {
      $("#sourceVisibility").combobox("setValue", "private");
    }
  }
  else {
    $("#sourceName").attr("disabled", true);
    $("#sourceVisibility").combobox("disable");
    $("#sourceCollection").combobox("disable");
  }
};

/**
 * Loads the fields in the target section.
 * 
 * @author Gabriel Leonardo Diaz, 14.03.2014.
 */
CLASSRelationship.prototype.loadTarget = function () {
  this.targetProperty = null;
  
  if (this.relationshipCell.children) {
    for (var i = 0; i < this.relationshipCell.children.length; i++) {
      var child = this.relationshipCell.children[i];
      if (child.getAttribute("type") == this.relationshipCell.target.id) {
        this.targetProperty = child;
        break;
      }
    }
  }
  
  var targetName         = "";
  var targetVisibility   = "";
  var targetCollection   = "";
  var targetMultiplicity = "";
  var targetNavigable    = false;
  
  if (this.targetProperty) {
    targetName         = this.targetProperty.getAttribute("name");
    targetVisibility   = this.targetProperty.getAttribute("visibility");
    targetCollection   = this.targetProperty.getAttribute("collection");
    targetMultiplicity = this.targetProperty.getAttribute("multiplicity");
    targetNavigable    = this.graph.isNavigable(this.targetProperty);
  }
  
  $("#targetName").val(targetName);
  $("#targetVisibility").combobox("setValue", targetVisibility);
  $("#targetType").val(this.graph.convertTypeIdToNameString(this.relationshipCell.target.id));
  $("#targetCollection").combobox("setValue", targetCollection);
  $("#targetMultiplicity").combobox("setValue", targetMultiplicity);
  $("#targetNavigable").prop("checked", targetNavigable);
  $("#targetNavigable").attr("disabled", !this.graph.isClass(this.relationshipCell.source.value));
  this.setEnabledTarget(targetNavigable);
};

/**
 * Saves the relationship and its properties.
 * 
 * @returns {Boolean}
 * @author Gabriel Leonardo Diaz, 16.03.2014.
 */
CLASSRelationship.prototype.saveRelationship = function () {
  var sourceName         = $("#sourceName").val();
  var sourceVisibility   = $("#sourceVisibility").combobox("getValue");
  var sourceCollection   = $("#sourceCollection").combobox("getValue");
  var sourceMultiplicity = $("#sourceMultiplicity").combobox("getValue");
  var sourceNavigable    = $("#sourceNavigable").is(":checked");
  
  var targetName         = $("#targetName").val();
  var targetVisibility   = $("#targetVisibility").combobox("getValue");
  var targetCollection   = $("#targetCollection").combobox("getValue");
  var targetMultiplicity = $("#targetMultiplicity").combobox("getValue");
  var targetNavigable    = $("#targetNavigable").is(":checked");
  
  // Validation
  if (!this.validProperty(sourceName, sourceVisibility, sourceCollection, sourceMultiplicity, sourceNavigable) ||
      !this.validProperty(targetName, targetVisibility, targetCollection, targetMultiplicity, targetNavigable)) {
    return false;
  }
  
  // 1. SAVE RELATIONSHIP
  var relationshipName = $("#relName").val();
  if (relationshipName != this.relationshipCell.getAttribute("name")) {
    this.graph.cellEditProperty(this.relationshipCell, "name", relationshipName, true);
  }
  
  
  // 2. SAVE SOURCE
  if (this.needsProperty(sourceNavigable, sourceMultiplicity)) {
    var property;
    if (this.sourceProperty) {
      property = this.sourceProperty.clone(true);
    }
    else {
      property = this.graph.model.cloneCell(this.editor.getTemplate("property"));
    }
    
    if (!sourceNavigable) {
      sourceName = "";
      sourceCollection = "";
    }
    
    property.setAttribute("name", sourceName);
    property.setAttribute("visibility", sourceVisibility);
    property.setAttribute("collection", sourceCollection);
    property.setAttribute("multiplicity", sourceMultiplicity);
    property.setAttribute("type", this.relationshipCell.source.id);
    
    if (this.sourceProperty) {
      this.graph.editAttribute(this.sourceProperty, property);
    }
    else {
      this.graph.addAssociationAttribute(this.relationshipCell, property, true, false);
    }
    
    if (sourceName == null || sourceName.length == 0) {
      this.graph.removeAssociationNavigableStyle(true, this.relationshipCell);
    }
  }
  else if (this.sourceProperty) {
    this.graph.removeAssociationAttribute(this.relationshipCell, this.sourceProperty, true);
  }
  
  // 3. SAVE TARGET
  if (this.needsProperty(targetNavigable, targetMultiplicity)) {
    var property;
    if (this.targetProperty) {
      property = this.targetProperty.clone(true);
    }
    else {
      property = this.graph.model.cloneCell(this.editor.getTemplate("property"));
    }
    
    if (!targetNavigable) {
      targetName = "";
      targetCollection = "";
    }
    
    property.setAttribute("name", targetName);
    property.setAttribute("visibility", targetVisibility);
    property.setAttribute("collection", targetCollection);
    property.setAttribute("multiplicity", targetMultiplicity);
    property.setAttribute("type", this.relationshipCell.target.id);
    
    if (this.targetProperty) {
      this.graph.editAttribute(this.targetProperty, property);
    }
    else {
      this.graph.addAssociationAttribute(this.relationshipCell, property, false, false);
    }
    
    if (targetName == null || targetName.length == 0) {
      this.graph.removeAssociationNavigableStyle(false, this.relationshipCell);
    }
  }
  else if (this.targetProperty) {
    this.graph.removeAssociationAttribute(this.relationshipCell, this.targetProperty, false);
  }
  
  return true;
};

/**
 * Validates the property fields.
 * @param name
 * @param visibility
 * @param collection
 * @param multiplicity
 * @param navigable
 * @returns {Boolean}
 */
CLASSRelationship.prototype.validProperty = function (name, visibility, collection, multiplicity, navigable) {
  if (navigable) {
    if (!name) {
      return false;
    }
    
    if (!visibility) {
      return false;
    }
  }
  
  if (!this.validMultiplicity(multiplicity)) {
    return false;
  }
  
  return true;
};

/**
 * Validates the multiplicity of the property.
 * @param multiplicity
 * @returns {Boolean}
 */
CLASSRelationship.prototype.validMultiplicity = function (multiplicity) {
  if (multiplicity) {
    var isInteger = function (n) {
      return !isNaN(n) && parseInt(n) % 1 == 0;
    };
    
    var values = multiplicity.split("..");
    
    if (values.length > 2) {
      return false;
    }
    
    if (!isInteger(values[0]) || parseInt(values[0]) < 0) {
      return false;
    }
    
    if (values.length > 1) {
      if (!isInteger(values[1]) && values[1] != "*") {
        return false;
      }
      
      if (isInteger(values[1]) && parseInt(values[0]) > parseInt(values[1])) {
        return false;
      }
    }
  }
  
  return true;
};

/**
 * Checks if the relationship end needs a property.
 * 
 * @param navigable
 *          If the end is navigable.
 * @param multiplicity
 *          The multiplicity value selected for the end.
 * @author Gabriel Leonardo Diaz, 30.03.2014.
 */
CLASSRelationship.prototype.needsProperty = function (navigable, multiplicity) {
  if (navigable || multiplicity) {
    return true;
  }
  return false;
};

/**
 * 
 * @param enabled
 */
CLASSRelationship.prototype.setEnabledTarget = function (enabled) {
  if (enabled) {
    $("#targetName").removeAttr("disabled");
    $("#targetVisibility").combobox("enable");
    $("#targetCollection").combobox("enable");
    if (!$("#targetVisibility").combobox("getValue")) {
      $("#targetVisibility").combobox("setValue", "private");
    }
  }
  else {
    $("#targetName").attr("disabled", true);
    $("#targetVisibility").combobox("disable");
    $("#targetCollection").combobox("disable");
  }
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
      width: 380,
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
      width: 220,
      data: this.graph.getCollectionsJSon()
  });
  
  // Workaround: The panel is shown behind of the PrimeFaces modal dialog.
  var comboPanel = $(elementId).combobox("panel");
  comboPanel.panel("panel").css("z-index", "2000");
};

/**
 * Configures the multiplicity combo box for the element identified by the given
 * ID.
 * 
 * @param elementId
 *          The element id.
 * @author Gabriel Leonardo Diaz, 10.04.2014.
 */
CLASSRelationship.prototype.configureMultiplicityCombo = function (elementId) {
  elementId = "#" + elementId;
  
  $(elementId).combobox({
      valueField:"id",
      textField:"text",
      panelHeight: 90,
      width: 150,
      data: this.graph.getMultiplicitiesJSon()
  });
  
  // Workaround: The panel is shown behind of the PrimeFaces modal dialog.
  var comboPanel = $(elementId).combobox("panel");
  comboPanel.panel("panel").css("z-index", "2000");
};

/**
 * Configures the checkboxes to enable/disable the fields when checked.
 * 
 * @author Gabriel Leonardo Diaz, 15.03.2014.
 */
CLASSRelationship.prototype.configureNavigableCheckBoxes = function () {
  var self = this;
  
  $("#sourceNavigable").change(function() {
    self.setEnabledSource($(this).is(":checked"));
  });
  
  $("#targetNavigable").change(function() {
    self.setEnabledTarget($(this).is(":checked"));
  });
};

/**
 * Shows up the dialog to the user.
 * 
 * @author Gabriel Leonardo Diaz, 26.01.2014.
 */
CLASSRelationship.prototype.show = function () {
  this.dialog.show();
};