/**
 * Client side object for modeling a UML class element.
 * 
 * @param name
 *          The name of the class.
 * @returns A class object.
 * @author Gabriel Leonardo Diaz, 09.10.2013.
 */
Class = function (name) {
  this.name       = name;
  this.attributes = new Array();
  this.operations = new Array();
};
Class.prototype.name       = null;
Class.prototype.isAbstract = false;

/**
 * Client side object for modeling a UML property element.
 * 
 * @param name
 *          The name of the attribute.
 * @returns An attribute object.
 * @author Gabriel Leonardo Diaz, 09.10.2013.
 */
Property = function (name) {
  this.name = name;
};
Property.prototype.name        = null;
Property.prototype.isStatic    = false;
Property.prototype.type        = null;

/**
 * Client side object for modeling a UML operation element.
 * 
 * @param name
 *          The name of the operation.
 * @returns An operation object.
 * @author Gabriel Leonardo Diaz, 16.10.2013.
 */
Operation = function (name) {
  this.name = name;
};
Operation.prototype.name        = null;
Operation.prototype.isStatic    = false;
Operation.prototype.returnType  = null;

/**
 * Constants that represent the visibility kinds in the UML metamodel.
 * 
 * @author Gabriel Leonardo Diaz, 16.10.2013.
 */
var VisibilityKind = {
  PUBLIC    : '+',
  PROTECTED : '#',
  PACKAGE   : '~',
  PRIVATE   : '-'
};

