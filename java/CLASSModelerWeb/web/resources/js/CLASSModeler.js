/*******************************************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS 
 * Cúcuta, Colombia 
 * (c) 2013 by UFPS. All rights reserved.
 * 
 * @author: Gabriel Leonardo Diaz, 01.05.2013.
 * 
 ******************************************************************************/

var CLASSModeler = {
  
  /**
   * The reference to the editor component.
   */
  EDITOR : new CLASSEditor("/CLASSModeler/Designer?init", "/CLASSModeler/Designer?image", "/CLASSModeler/Designer?poll", "/CLASSModeler/Designer?notify"),
  
    /**
     * Main Function, this starts the mxGraph client side application.
     * 
     * @param initialXML
     *          The initial components painted in the graph represented as an
     *          XML.
     * @author Gabriel Leonardo Diaz, 01.05.2013.
     */
  main : function (initialXML) {
    if (!mxClient.isBrowserSupported()) {
      mxUtils.error('Browser is not supported for mxGraph!', 200, false);
      return;
    }
    
    var graphContainer   = document.getElementById("graph");
    var paletteContainer = document.getElementById("toolbox");
    var outlineContainer = document.getElementById("outline");
    
    CLASSModeler.EDITOR.init(initialXML, graphContainer, paletteContainer, outlineContainer);
  }
};

/**
 * The reference to the editor component.
 */
var EDITOR;

/**
 * Main Function, this starts the mxGraph client side application.
 * 
 * @param initialXML
 *          The initial components painted in the graph represented as an
 *          XML.
 * @author Gabriel Leonardo Diaz, 01.05.2013.
 */
function main (initialXML) {
  if (!mxClient.isBrowserSupported()) {
    mxUtils.error('Browser is not supported for mxGraph!', 200, false);
    return;
  }
  
  var urlInit          = "/CLASSModeler/Designer?init";
  var urlImage         = "/CLASSModeler/Designer?image";
  var urlPool          = "/CLASSModeler/Designer?poll";
  var urlNotify        = "/CLASSModeler/Designer?notify";
  
  var graphContainer   = document.getElementById("graph");
  var paletteContainer = document.getElementById("toolbox");
  var outlineContainer = document.getElementById("outline");
  
  EDITOR = new CLASSEditor(urlInit, urlImage, urlPool, urlNotify);
  EDITOR.init(initialXML, graphContainer, paletteContainer, outlineContainer);
}