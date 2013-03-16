
/*******************************************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS 
 * Cúcuta, Colombia 
 * (c) 2013 by UFPS. All rights reserved.
 * 
 * @author: Gabriel Leonardo Diaz, 31.01.2013.
 * 
 ******************************************************************************/
/**
 * Base resources path configuration
 */


function mxEditorUFPS (container) {
    try {
      // Checking the browser used
      if (!mxClient.isBrowserSupported()) {
        mxUtils.error('Browser is not supported!', 200, false);
        return;
      }
      
      // Creating the editor and returning it
      var node = mxUtils.load('../../resources/config/mxEditorUFPS.xml').getDocumentElement();
      var editor = new mxEditor(node);
      
      // Configuring the editor
      editor.setGraphContainer(container);
      editor.popupHandler.imageBasePath = mxClient.imageBasePath;
      
      return editor;
    }
    catch (e) {
      mxUtils.alert('Cannot start application: ' + e.message);
      throw e;
    }
}