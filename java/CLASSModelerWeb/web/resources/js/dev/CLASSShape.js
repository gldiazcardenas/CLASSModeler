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
ShapePackage = function () {};
ShapePackage.prototype = new mxCylinder();
ShapePackage.prototype.constructor = ShapePackage;
ShapePackage.prototype.tabPosition = 'left';
ShapePackage.prototype.redrawPath  = function (path, x, y, w, h, isForeground) {
  
  var tp = mxUtils.getValue(this.style, 'tabPosition', this.tabPosition);
  var tw = w * 0.50;
  var th = h * 0.20;
  
  if (th > 20) {
    th = 20; // Not more than 20px.
  }
  
  if (tw > 80) {
    tw = 80; // Not more than 80px.
  }
  
  var dx = Math.min(w, tw);
  var dy = Math.min(h, th);

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
mxCellRenderer.prototype.defaultShapes['package'] = ShapePackage;

/**
 * Defines the shape for a common UML comment appended to any element.
 * 
 * @returns {ShapeNote} The ShapeNote created.
 * @author Gabriel Leonardo Diaz, 16.06.2013.
 */
ShapeComment = function () {};
ShapeComment.prototype = new mxCylinder();
ShapeComment.prototype.constructor = ShapeComment;
ShapeComment.prototype.redrawPath  = function (path, x, y, w, h, isForeground) {
  
  var s = w * 0.20;
  if (s > 10) {
    s = 10; // No more than 10px.
  }
  
  this.size = s;
  
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
mxCellRenderer.prototype.defaultShapes['comment'] = ShapeComment;