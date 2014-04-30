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
  
  var editor        = null;
  var outline       = null;
  var toolbox       = null;
  var properties    = null;
  
  function stopLoading () {
    // Fades-out the splash screen
    var loading = document.getElementById('loading');
    
    if (loading != null) {
      try {
        mxEvent.release(loading);
        mxEffects.fadeOut(loading, 100, true);
      }
      catch (e) {
        loading.parentNode.removeChild(loading);
      }
    }
  }
  
  return {
    init : function (enabled) {
      if (!mxClient.isBrowserSupported()) {
        mxUtils.error('Browser is not supported for mxGraph!', 200, false);
        return;
      }
      
      // Disables built-in context menu
      mxEvent.disableContextMenu(document.body);
      
      // Vertexes selection color
      mxConstants.HANDLE_FILLCOLOR = "#FC8D98";
      mxConstants.HANDLE_STROKECOLOR = "#E1061A";
      mxConstants.VERTEX_SELECTION_COLOR = "#E1061A";
      mxConstants.EDGE_SELECTION_COLOR = "#E1061A";
      
      editor = new CLASSEditor(mxUtils.load(mxBasePath + "/config/editor.xml").getDocumentElement(), enabled);
      editor.addListener(mxEvent.ROOT, stopLoading);
      
      toolbox = new CLASSToolbox(editor);
      toolbox.init(document.getElementById("toolbox"));
      
      outline = new mxOutline(editor.graph);
      outline.init(document.getElementById("outline"));
      outline.updateOnPan = true;
      
      properties = new CLASSPropertyGrid(editor);
      properties.init();
    },
    
    execute : function (actionName) {
      editor.execute(actionName);
    },
  };
  
})();

// GD, 13.01.2014 Workaround for IE.
if (!document.getElementsByClassName) {
  document.getElementsByClassName = function(className) {
      return this.querySelectorAll("." + className);
  };
  Element.prototype.getElementsByClassName = document.getElementsByClassName;
}

