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
 * The reference to the editor component.
 */
var EDITOR;

mxEditor.prototype.urlInit   = "/CLASSModeler/Designer?init";
mxEditor.prototype.urlImage  = "/CLASSModeler/Designer?image";
mxEditor.prototype.urlPoll   = "/CLASSModeler/Designer?poll";
mxEditor.prototype.urlNotify = "/CLASSModeler/Designer?notify";

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
  
  var urlInit   = "/CLASSModeler/Designer?init";
  var urlImage  = "/CLASSModeler/Designer?image";
  var urlPoll   = "/CLASSModeler/Designer?poll";
  var urlNotify = "/CLASSModeler/Designer?notify";
  
  var graphContainer   = document.getElementById("graph");
  var toolboxContainer = document.getElementById("toolbox");
  var outlineContainer = document.getElementById("outline");
  
  EDITOR = new CLASSEditor(urlInit, urlImage, urlPoll, urlNotify);
  EDITOR.init(graphContainer, outlineContainer, toolboxContainer);
}


