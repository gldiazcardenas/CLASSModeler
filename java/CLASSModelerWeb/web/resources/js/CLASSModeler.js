/*******************************************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS 
 * Cúcuta, Colombia 
 * (c) 2013 by UFPS. All rights reserved.
 * 
 * @author: Gabriel Leonardo Diaz, 01.05.2013.
 * 
 ******************************************************************************/

var editorComponent = null;
var actionHandler   = null;

/**
 * Main Function, this starts the mxGraph client side application.
 * 
 * @param initialXML
 *          The initial components painted in the graph represented as an XML.
 * @author Gabriel Leonardo Diaz, 01.05.2013.
 */
function main (initialXML) {
  // Checking the browser used
  if (!mxClient.isBrowserSupported()) {
    mxUtils.error('Browser is not supported for mxGraph!', 200, false);
    return;
  }
  
  var graphContainer   = document.getElementById("graph");
  var toolboxContainer = document.getElementById("toolbox");
  var outlineContainer = document.getElementById("outline");
  
  var urlInit          = "/CLASSModeler/Designer?init";
  var urlImage         = "/CLASSModeler/Designer?image";
  var urlPoll          = "/CLASSModeler/Designer?poll";
  var urlNotify        = "/CLASSModeler/Designer?notify";
  
  editorComponent = new CLASSEditor(urlInit, urlImage, urlPoll, urlNotify);
  editorComponent.init(initialXML, graphContainer, toolboxContainer, outlineContainer);
  
  actionHandler = new CLASSActionHandler(editorComponent);
  actionHandler.init();
}