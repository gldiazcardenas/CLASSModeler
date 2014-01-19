/*******************************************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS 
 * C�cuta, Colombia 
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
  
  return {
    init : function () {
      if (!mxClient.isBrowserSupported()) {
        mxUtils.error('Browser is not supported for mxGraph!', 200, false);
        return;
      }
      
      // Disables built-in context menu
      mxEvent.disableContextMenu(document.body);
      
      // Vertexes selection color
      mxConstants.HANDLE_FILLCOLOR = '#FC8D98';
      mxConstants.HANDLE_STROKECOLOR = '#E1061A';
      mxConstants.VERTEX_SELECTION_COLOR = '#E1061A';
      
      editor = new CLASSEditor(mxUtils.load(mxBasePath + '/config/editor.xml').getDocumentElement());
      
      toolbox = new CLASSToolbox(editor);
      toolbox.init(document.getElementById("toolbox"));
      
      outline = new mxOutline(editor.graph);
      outline.init(document.getElementById("outline"));
      outline.updateOnPan = true;
      
      properties = new CLASSProperties(editor);
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

