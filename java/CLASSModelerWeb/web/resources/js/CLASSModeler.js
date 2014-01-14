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
  
  return {
    init : function () {
      if (!mxClient.isBrowserSupported()) {
        mxUtils.error('Browser is not supported for mxGraph!', 200, false);
        return;
      }
      
      editor = new CLASSEditor(mxUtils.load(mxBasePath + '/config/editor.xml').getDocumentElement());
      
      toolbox = new CLASSToolbox(editor);
      toolbox.init(document.getElementById("toolbox"));
      
      outline = new mxOutline(editor.graph);
      outline.init(document.getElementById("outline"));
      outline.updateOnPan = true;
      
      $('#propertyTable').propertygrid({ 
          data: [
                 {"name":"Nombre", "value":"", "group":"General", "editor":"text"},
                 {"name":"Visibilidad", "value":"", "group":"General", "editor": {
                     "type":"combobox", 
                     "options": {
                         "valueField":"id",
                         "textField":"text",
                         "data":[ {"id":"Public", "text":"Public"},
                                  {"id":"Protected", "text":"Protected"},
                                  {"id":"Package", "text":"Package"},
                                  {"id":"Private", "text":"Private"}
                                ]
                     }
                   }
                 },
                 {"name":"Abstracto", "value": "", "group":"Avanzado", "editor": {
                     "type":"checkbox",
                     "options": {
                       "on":true,
                       "off":false
                     }
                   }
                 },
                 {"name":"Es Raiz", "value": "", "group":"Avanzado", "editor": {
                     "type":"checkbox",
                     "options": {
                       "on":true,
                       "off":false
                     }
                   }
                 },
                 {"name":"Es Hoja", "value": "", "group":"Avanzado", "editor": {
                     "type":"checkbox",
                     "options": {
                       "on":true,
                       "off":false
                     }
                   }
                 },
                 {"name":"Es Especificacion", "value": "", "group":"Avanzado", "editor": {
                     "type":"checkbox",
                     "options": {
                       "on":true,
                       "off":false
                     }
                   }
                 }
                ],
          showGroup: true,
          showHeader: false,
          autoSizeColumn: true,
          scrollbarSize: 0
      });
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

