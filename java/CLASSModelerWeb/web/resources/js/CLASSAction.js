/*******************************************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS 
 * Cúcuta, Colombia 
 * (c) 2013 by UFPS. All rights reserved.
 * 
 * @author: Gabriel Leonardo Diaz, 01.05.2013.
 * 
 ******************************************************************************/

/**
 * Constructs a new action for the given parameters.
 */
CLASSAction = function (label, funct, enabled) {
  mxEventSource.call(this);
  
  this.label    = label;
  this.funct    = funct;
  this.enabled  = enabled;
};

// CLASSAction inherits from mxEventSource
mxUtils.extend(CLASSAction, mxEventSource);

/**
 * Sets the enabled state of the action and fires a stateChanged event.
 */
CLASSAction.prototype.setEnabled = function(value) {
  if (this.enabled != value) {
    this.enabled = value;
    this.fireEvent(new mxEventObject('stateChanged'));
  }
};

/**
 * Sets the enabled state of the action and fires a stateChanged event.
 */
CLASSAction.prototype.setToggleAction = function(value) {
  this.toggleAction = value;
};

/**
 * Sets the enabled state of the action and fires a stateChanged event.
 */
CLASSAction.prototype.setSelectedCallback = function(funct) {
  this.selectedCallback = funct;
};

/**
 * Sets the enabled state of the action and fires a stateChanged event.
 */
CLASSAction.prototype.isSelected = function() {
  return this.selectedCallback();
};