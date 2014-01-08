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
 * The reference to the CLASSModeler component.
 */


//Static variables to point out the resources location.
mxBasePath      = '/CLASSModeler/resources';
mxImageBasePath = '/CLASSModeler/resources/images';
mxLoadResources = false;

// Instance of the modeler
var MODELER     = null;

/**
 * Main Function, this starts the mxGraph client side application.
 * 
 * @author Gabriel Leonardo Diaz, 01.05.2013.
 */
function main () {
  if (!mxClient.isBrowserSupported()) {
    mxUtils.error('Browser is not supported for mxGraph!', 200, false);
    return;
  }
  
  MODELER = new CLASSModeler();
  MODELER.initialize();
}

/**
 * Singleton class that encapsulates the client application for class diagrams
 * design.
 * 
 * @author Gabriel Leonardo Diaz, 24.10.2013.
 */
CLASSModeler = function () {};

/**
 * Represents an instance of CLASSEditor object.
 */
CLASSModeler.prototype.editor = null;

/**
 * Represents an instance of mxOutline object.
 */
CLASSModeler.prototype.outline = null;

/**
 * Represents an instance of CLASSToolbox object.
 */
CLASSModeler.prototype.toolbox = null;

/**
 * Initializes the CLASSModeler component.
 */
CLASSModeler.prototype.initialize = function () {
  var urlInit          = "/CLASSModeler/Designer?init";
  var urlImage         = "/CLASSModeler/Designer?image";
  var urlPoll          = "/CLASSModeler/Designer?poll";
  var urlNotify        = "/CLASSModeler/Designer?notify";
  
  var graphContainer   = document.getElementById("graph");
  var toolboxContainer = document.getElementById("toolbox");
  var outlineContainer = document.getElementById("outline");
  
  // 1. Configures the editor
  this.editor = new CLASSEditor(urlInit, urlImage, urlPoll, urlNotify);
  this.editor.init(graphContainer);
  
  // 2. Configures the ToolBox
  this.toolbox = new CLASSToolbox(this.editor);
  this.toolbox.init(toolboxContainer);
  
  // 3. Configures the outline
  this.outline = new mxOutline(this.graph);
  this.outline.init(outlineContainer);
  this.outline.updateOnPan = true;
};

/**
 * Executes from external event the action represented by the given name on the
 * editor component.
 * 
 * @author Gabriel Leonardo Diaz, 26.10.2013.
 */
CLASSModeler.prototype.execute = function () {
  
};

