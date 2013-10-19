/**
 * This class represents the factory of the metamodel for UML elements.
 * 
 * @author Gabriel Leonardo Diaz, 18.10.2013.
 */
CLASSMetamodel = function () {
  this.documentXML = mxUtils.createXmlDocument();
};

/**
 * The XML document used to create the elements.
 */
CLASSMetamodel.prototype.documentXML = null;

/**
 * Factory method for a named element.
 * 
 * @param name
 *          The name of the element
 * @author Gabriel Leonardo Diaz, 18.10.2013.
 */
CLASSMetamodel.prototype.createNamedElement = function (name) {
  var namedElementXML = this.documentXML.createElement('NamedElement');
  namedElementXML.setAttribute('name', name);
  return namedElementXML;
};

/**
 * Factory method for a class element.
 * 
 * @param name
 *          The name of the class.
 * @author Gabriel Leonardo Diaz.
 */
CLASSMetamodel.prototype.createClass = function (name) {
  var classXML = this.documentXML.createElement('Class');
  classXML.setAttribute('name', name);
  classXML.setAttribute('isAbstract', 'false');
  classXML.setAttribute('visibility', VisibilityKind.PUBLIC);
  return classXML;
};

/**
 * Factory method for a property element.
 * 
 * @param name
 *          The name of the property.
 * @author Gabriel Leonardo Diaz, 18.10.2013.
 */
CLASSMetamodel.prototype.createProperty = function (name) {
  var propertyXML = this.documentXML.createElement('Property');
  propertyXML.setAttribute('name', name);
  propertyXML.setAttribute('isStatic', 'false');
  propertyXML.setAttribute('isFinal', 'false');
  propertyXML.setAttribute('visibility', VisibilityKind.PUBLIC);
  propertyXML.setAttribute('type', 'int');
  return propertyXML;
};

/**
 * Factory method for an operation element.
 * 
 * @param name
 *          The name of the operation.
 * @author Gabriel Leonardo Diaz, 18.10.2013.
 */
CLASSMetamodel.prototype.createOperation = function (name) {
  var operationXML = this.documentXML.createElement('Operation');
  operationXML.setAttribute('name', name);
  operationXML.setAttribute('isStatic', 'false');
  operationXML.setAttribute('isFinal', 'false');
  operationXML.setAttribute('visibility', VisibilityKind.PUBLIC);
  operationXML.setAttribute('returnType', 'void');
  return operationXML;
};

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
