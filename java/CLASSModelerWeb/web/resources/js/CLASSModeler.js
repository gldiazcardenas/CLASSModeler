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
 * Singleton class that encapsulates the client side application for
 * CLASSModeler software.
 * 
 * @author Gabriel Leonardo Diaz, 26.10.2013.
 */
var CLASSModeler = (function () {
  
  var editor  = null;
  var outline = null;
  var toolbox = null;
  
  return {
    init : function () {
      if (!mxClient.isBrowserSupported()) {
        mxUtils.error('Browser is not supported for mxGraph!', 200, false);
        return;
      }
      
      editor = new CLASSEditor("/CLASSModeler/Designer?init", "/CLASSModeler/Designer?image", "/CLASSModeler/Designer?poll", "/CLASSModeler/Designer?notify");
      editor.init(document.getElementById("graph"));
      
      outline = new mxOutline(editor.graph);
      outline.init(document.getElementById("outline"));
      outline.updateOnPan = true;
      
      toolbox = new CLASSToolbox(editor.graph);
      toolbox.init(document.getElementById("toolbox"));
    },
    
    execute : function (actionName) {
      
    },
    
    showXML : function () {
      var encoder = new mxCodec();
      var node = encoder.encode(editor.graph.getModel());
      mxUtils.popup(mxUtils.getPrettyXml(node), true);
    }
  };
  
})();

