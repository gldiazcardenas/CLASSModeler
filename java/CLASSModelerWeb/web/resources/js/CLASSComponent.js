/**
 * Client side object for modeling a UML class element.
 * 
 * @param name
 *          The name of the class.
 * @returns A class object.
 * @author Gabriel Leonardo Diaz, 09.10.2013.
 */
function Class (name) {
  this.name       = name;
  this.attributes = new Array();
  this.operations = new Array();
};
Class.prototype.name       = null;
Class.prototype.isAbstract = false;

/**
 * Client side object for modeling a UML attribute element.
 * @param name The name of the attribute.
 * @returns An attribute object.
 * @author Gabriel Leonardo Diaz, 09.10.2013.
 */
function Attribute (name) {
  this.name = name;
};
Attribute.prototype.name        = null;
Attribute.prototype.isStatic    = false;
Attribute.prototype.type        = null;

function Operation (name) {
  this.name = name;
}
Operation.prototype.name        = null;
Operation.prototype.isStatic    = false;
Operation.prototype.returnType  = null;

var VisibilityKind = {
  PUBLIC    : '+',
  PROTECTED : '#',
  PACKAGE   : '~',
  PRIVATE   : '-'
};

