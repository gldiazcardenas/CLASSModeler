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
    var toolboxContainer = document.getElementById("toolbox");
    var outlineContainer = document.getElementById("outline");
    
    CLASSModeler.EDITOR.init(initialXML, graphContainer, toolboxContainer, outlineContainer);
  }
};