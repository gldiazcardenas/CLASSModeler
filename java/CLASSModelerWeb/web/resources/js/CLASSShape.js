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
 * Defines the shape for UML package element.
 * 
 * @author Gabriel Leonardo Diaz, 01.05.2013.
 */
UMLPackage = function () { };
UMLPackage.prototype = new mxCylinder();
UMLPackage.prototype.constructor = UMLPackage;
UMLPackage.prototype.tabWidth    = 60;
UMLPackage.prototype.tabHeight   = 20;
UMLPackage.prototype.tabPosition = 'left';
UMLPackage.prototype.redrawPath  = function (path, x, y, w, h, isForeground) {
  
  var tw = mxUtils.getValue(this.style, 'tabWidth', this.tabWidth);
  var th = mxUtils.getValue(this.style, 'tabHeight', this.tabHeight);
  var tp = mxUtils.getValue(this.style, 'tabPosition', this.tabPosition);
  var dx = Math.min(w, tw * this.scale);
  var dy = Math.min(h, th * this.scale);

  if (isForeground) {
    if (tp == 'left') {
      path.moveTo(0, dy);
      path.lineTo(dx, dy);
    }
    // Right is default
    else {
      path.moveTo(w - dx, dy);
      path.lineTo(w, dy);
    }
    
    path.end();
  }
  else {
    if (tp == 'left') {
      path.moveTo(0, 0);
      path.lineTo(dx, 0);
      path.lineTo(dx, dy);
      path.lineTo(w, dy);
    }
    // Right is default
    else {
      path.moveTo(0, dy);
      path.lineTo(w - dx, dy);
      path.lineTo(w - dx, 0);
      path.lineTo(w, 0);
    }
    
    path.lineTo(w, h);
    path.lineTo(0, h);
    path.lineTo(0, dy);
    path.close();
    path.end();
  }
};
mxCellRenderer.prototype.defaultShapes['UMLPackage'] = UMLPackage;

/**
 * Defines the shape for a common UML comment appended to any element.
 * 
 * @returns {UMLNote} The UMLNote share created.
 * @author Gabriel Leonardo Diaz, 16.06.2013.
 */
UMLNote = function () { };
UMLNote.prototype = new mxCylinder();
UMLNote.prototype.constructor = UMLNote;
UMLNote.prototype.size = 30;
UMLNote.prototype.redrawPath = function (path, x, y, w, h, isForeground) {
  
  var s = Math.min(w, Math.min(h, mxUtils.getValue(this.style, 'size', this.size)));
  
  if (isForeground) {
    path.moveTo(w - s, 0);
    path.lineTo(w - s, s);
    path.lineTo(w, s);
    path.end();
  }
  else {
    path.moveTo(0, 0);
    path.lineTo(w - s, 0);
    path.lineTo(w, s);
    path.lineTo(w, h);
    path.lineTo(0, h);
    path.lineTo(0, 0);
    path.close();
    path.end();
  }
};
mxCellRenderer.prototype.defaultShapes['UMLNote'] = UMLNote;
