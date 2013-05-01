/*******************************************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS 
 * Cúcuta, Colombia 
 * (c) 2013 by UFPS. All rights reserved.
 * 
 * @author: Gabriel Leonardo Diaz, 21.04.2013.
 * 
 ******************************************************************************/

/**
 * JavaScript Class for the editor, this inherits from mxEditor.
 */
CLASSEditor = function (initialXML) {
  this.urlInit   = null; //"/CLASSModeler/Designer?init";
  this.urlImage  = "/CLASSModeler/Designer?image";
  this.urlPoll   = "/CLASSModeler/Designer?poll";
  this.urlNotify = "/CLASSModeler/Designer?notify";
  
  // Call super
  mxEditor.call(this);
  
  // Gets the containers
  this.graphContainer   = document.getElementById("graph");
  this.toolboxContainer = document.getElementById("toolbox");
  this.outlineContainer = document.getElementById("outline");
};

// CLASSEditor inherits from mxEditor
mxUtils.extend(CLASSEditor, mxEditor);

/**
 * Initializer method for the CLASSEditor.
 */
CLASSEditor.prototype.init = function() {
  // Creates the Graph
  this.graph = new CLASSGraph();
  this.graph.init(this.graphContainer);
  this.graph.refresh();
  
  // Creates the outline (zoom panel)
  this.outline = new mxOutline(this.graph);
  this.outline.init(this.outlineContainer);
  this.outline.updateOnPan = true;
  
  // Creates the toolBox component.
  this.toolbox = new CLASSToolBox(this);
  this.toolbox.init(this.toolboxContainer);
  
  // Sets the image base path to the popUp handler
  this.popupHandler.imageBasePath = mxClient.imageBasePath;
};