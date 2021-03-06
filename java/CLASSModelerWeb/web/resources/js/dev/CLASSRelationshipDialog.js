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
CLASSRelationshipDialog = function (editor) {
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
CLASSRelationshipDialog.prototype.relationshipCell;

/**
 * Reference to the source property of the relationship.
 */
CLASSRelationshipDialog.prototype.sourceProperty;

/**
 * Reference to the target property of the relationship.
 */
CLASSRelationshipDialog.prototype.targetProperty;

/**
 * The localized text for the dialog title.
 */
CLASSRelationshipDialog.prototype.title;

/**
 * Initializes the dialog to edit relationship contained by the given cell.
 * 
 * @param cell
 * @author Gabriel Leonardo Diaz, 19.02.2014.
 */
CLASSRelationshipDialog.prototype.init = function (cell) {
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
CLASSRelationshipDialog.prototype.loadRelationship = function () {
  $("#relName").val(this.relationshipCell.getAttribute("name"));
};

/**
 * Loads the fields in the source section.
 * 
 * @author Gabriel Leonardo Diaz, 14.03.2014.
 */
CLASSRelationshipDialog.prototype.loadSource = function () {
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
  var sourceLower        = "";
  var sourceUpper        = "";
  var sourceNavigable    = false;
  
  if (this.sourceProperty) {
    sourceName         = this.sourceProperty.getAttribute("name");
    sourceVisibility   = this.sourceProperty.getAttribute("visibility");
    sourceCollection   = this.sourceProperty.getAttribute("collection");
    sourceLower        = this.sourceProperty.getAttribute("lower");
    sourceUpper        = this.sourceProperty.getAttribute("upper");
    sourceNavigable    = this.graph.isNavigable(this.sourceProperty);
  }
  
  $("#sourceName").val(sourceName);
  $("#sourceName").validatebox({ required: false, missingMessage: "" });
  
  $("#sourceVisibility").combobox("setValue", sourceVisibility);
  $("#sourceVisibility").combobox({ required: false, missingMessage: "" });
  
  $("#sourceType").val(this.graph.convertTypeIdToNameString(this.relationshipCell.source.id));
  $("#sourceCollection").combobox("setValue", sourceCollection);
  $("#sourceMultiplicity").combobox("setValue", this.graph.convertMultiplicity(sourceLower, sourceUpper));
  $("#sourceNavigable").prop("checked", sourceNavigable);
  $("#sourceNavigable").attr("disabled", !this.graph.isClass(this.relationshipCell.target.value));
  this.setEnabledSource(sourceNavigable);
};

/**
 * Enables/Disables the source role controls.
 * 
 * @author Gabriel Leonardo Diaz, 15.03.2014.
 */
CLASSRelationshipDialog.prototype.setEnabledSource = function (enabled) {
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
    $("#sourceName").validatebox({ required: false, missingMessage: "" });
    $("#sourceVisibility").combobox("disable");
    $("#sourceCollection").combobox("disable");
  }
};

/**
 * Loads the fields in the target section.
 * 
 * @author Gabriel Leonardo Diaz, 14.03.2014.
 */
CLASSRelationshipDialog.prototype.loadTarget = function () {
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
  var targetLower        = "";
  var targetUpper        = "";
  var targetNavigable    = false;
  
  if (this.targetProperty) {
    targetName         = this.targetProperty.getAttribute("name");
    targetVisibility   = this.targetProperty.getAttribute("visibility");
    targetCollection   = this.targetProperty.getAttribute("collection");
    targetLower        = this.targetProperty.getAttribute("lower");
    targetUpper        = this.targetProperty.getAttribute("upper");
    targetNavigable    = this.graph.isNavigable(this.targetProperty);
  }
  
  $("#targetName").val(targetName);
  $("#targetName").validatebox({ required: false, missingMessage: "" });
  
  $("#targetVisibility").combobox("setValue", targetVisibility);
  $("#targetVisibility").combobox({ required: false, missingMessage: "" });
  
  $("#targetType").val(this.graph.convertTypeIdToNameString(this.relationshipCell.target.id));
  $("#targetCollection").combobox("setValue", targetCollection);
  $("#targetMultiplicity").combobox("setValue", this.graph.convertMultiplicity(targetLower, targetUpper));
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
CLASSRelationshipDialog.prototype.saveRelationship = function () {
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
  if (!this.validProperty(true, sourceName, sourceVisibility, sourceCollection, sourceMultiplicity, sourceNavigable) ||
      !this.validProperty(false, targetName, targetVisibility, targetCollection, targetMultiplicity, targetNavigable)) {
    
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
    
    var multiplicities = this.splitMultiplicity(sourceMultiplicity);
    property.setAttribute("name", sourceName);
    property.setAttribute("visibility", sourceVisibility);
    property.setAttribute("collection", sourceCollection);
    property.setAttribute("lower", multiplicities[0]);
    property.setAttribute("upper", multiplicities[1]);
    property.setAttribute("type", this.relationshipCell.source.id);
    
    if (this.sourceProperty) {
      this.graph.editAttribute(this.sourceProperty, property);
      if (sourceName) {
        this.graph.addAssociationNavigableStyle(true, this.relationshipCell);
      }
      else {
        this.graph.removeAssociationNavigableStyle(true, this.relationshipCell);
      }
    }
    else {
      this.graph.addAssociationAttribute(this.relationshipCell, property, true, false);
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
    
    var multiplicities = this.splitMultiplicity(targetMultiplicity);
    property.setAttribute("name", targetName);
    property.setAttribute("visibility", targetVisibility);
    property.setAttribute("collection", targetCollection);
    property.setAttribute("lower", multiplicities[0]);
    property.setAttribute("upper", multiplicities[1]);
    property.setAttribute("type", this.relationshipCell.target.id);
    
    if (this.targetProperty) {
      this.graph.editAttribute(this.targetProperty, property);
      if (targetName) {
        this.graph.addAssociationNavigableStyle(false, this.relationshipCell);
      }
      else {
        this.graph.removeAssociationNavigableStyle(false, this.relationshipCell);
      }
    }
    else {
      this.graph.addAssociationAttribute(this.relationshipCell, property, false, false);
    }
  }
  else if (this.targetProperty) {
    this.graph.removeAssociationAttribute(this.relationshipCell, this.targetProperty, false);
  }
  
  return true;
};

/**
 * Validates the property fields.
 * @param isSource
 * @param name
 * @param visibility
 * @param collection
 * @param multiplicity
 * @param navigable
 * @returns {Boolean}
 */
CLASSRelationshipDialog.prototype.validProperty = function (isSource, name, visibility, collection, multiplicity, navigable) {
  if (navigable) {
    if (!name) {
      if (isSource) {
        $("#sourceName").validatebox({ required: true, missingMessage: "Campo requerido" });
      }
      else {
        $("#targetName").validatebox({ required: true, missingMessage: "Campo requerido" });
      }
      return false;
    }
    
    if (!visibility) {
      if (isSource) {
        $("#sourceVisibility").combobox({ required: true, missingMessage: "Campo requerido" });
      }
      else {
        $("#targetVisibility").combobox({ required: true, missingMessage: "Campo requerido" });
      }
      return false;
    }
  }
  
  var multiplicityId;
  
  if (isSource) {
    multiplicityId = "sourceMultiplicity";
  }
  else {
    multiplicityId = "targetMultiplicity";
  }
  
  if (!$("#" + multiplicityId).combobox("isValid")) {
    return false;
  }
  
  return true;
};

/**
 * Separates the multiplicity value into two values (lower and upper).
 * 
 * @param multiplicity
 * @author Gabriel Leonardo Diaz, 12.04.2014.
 */
CLASSRelationshipDialog.prototype.splitMultiplicity = function (multiplicity) {
  var values = [];
  
  if (multiplicity) {
    var separated = multiplicity.split("..");
    
    // Lower
    values.push(separated[0]); 
    
    // Upper
    values.push(separated.length > 1 ? separated[1] : "");
  }
  
  return values;
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
CLASSRelationshipDialog.prototype.needsProperty = function (navigable, multiplicity) {
  if (navigable || multiplicity) {
    return true;
  }
  return false;
};

/**
 * 
 * @param enabled
 */
CLASSRelationshipDialog.prototype.setEnabledTarget = function (enabled) {
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
    $("#targetName").validatebox({ required: false, missingMessage: "" });
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
CLASSRelationshipDialog.prototype.setTitle = function () {
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
CLASSRelationshipDialog.prototype.configureVisibilityCombo = function (elementId) {
  elementId = "#" + elementId;
  
  $(elementId).combobox({
      valueField:"id",
      textField:"text",
      panelHeight: 90,
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
CLASSRelationshipDialog.prototype.configureCollectionsCombo = function (elementId) {
  elementId = "#" + elementId;
  
  $(elementId).combobox({
      valueField:"id",
      textField:"text",
      panelHeight: 200,
      data: this.graph.getCollectionsJSon()
  });
  
  // Workaround: The panel is shown behind of the PrimeFaces modal dialog.
  var comboPanel = $(elementId).combobox("panel");
  comboPanel.panel("panel").css("z-index", "2000");
};

/**
 * Configures combo box for multiplicities for the element identified by the
 * given ID.
 * 
 * @param elementId
 *          The element id.
 * @author Gabriel Leonardo Diaz, 10.04.2014.
 */
CLASSRelationshipDialog.prototype.configureMultiplicityCombo = function (elementId) {
  elementId = "#" + elementId;
  
  $(elementId).combobox({
      valueField:"id",
      textField:"text",
      panelHeight: 110,
      validType: "multiplicity",
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
CLASSRelationshipDialog.prototype.configureNavigableCheckBoxes = function () {
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
CLASSRelationshipDialog.prototype.show = function () {
  this.dialog.show();
};

/**
 * Defines the validator for the multiplicity value.
 */
$.extend($.fn.validatebox.defaults.rules, {
  multiplicity: {
      validator: function (multiplicity, param) {
        var isInteger = function (number) {
          return !isNaN(number) && parseFloat(number) % 1 == 0;
        };
        
        if (multiplicity) {
          var values = multiplicity.split("..");
          
          if (values.length > 2) {
            return false;
          }
          
          if ((!isInteger(values[0]) && values[0] != "*") || parseInt(values[0]) < 0) {
            return false;
          }
          
          if (values.length > 1) {
            if ((!isInteger(values[1]) && values[1] != "*") || parseInt(values[1]) < 0) {
              return false;
            }
            
            if (isInteger(values[1]) && parseInt(values[0]) > parseInt(values[1])) {
              return false;
            }
          }
        }
        
        return true;
      },
      message: 'La multiplicidad es invalida.'
  }
});