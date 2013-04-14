
/*******************************************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS 
 * Cúcuta, Colombia 
 * (c) 2013 by UFPS. All rights reserved.
 * 
 * @author: Gabriel Leonardo Diaz, 31.01.2013.
 * 
 ******************************************************************************/

mxBasePath = '../../resources';
mxImageBasePath = '../../resources/images';

/**
 * Class that defines the editor.
 */
function mxEditorUFPS () {
   // Checking the browser used
  if (!mxClient.isBrowserSupported()) {
    mxUtils.error('Browser is not supported!', 200, false);
    return;
  }
  
  // Creates the editor by loading the specified configuration file
  var node = mxUtils.load('../../resources/config/mxUFPSModeler.xml').getDocumentElement();
  var editor = new mxEditor(node);
  
  // Sets the graph container
  //editor.setGraphContainer(document.getElementById("graph"));

  // Sets the image base path to the popUp handler
  editor.popupHandler.imageBasePath = mxClient.imageBasePath;
  editor.toolbar.imageBasePath = mxClient.imageBasePath;
  
  return editor;
}

/**
 * Method that initializes the editor component.
 */
function onInit () {
  
}