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
 * @author Gabriel Leonardo Diaz, 19.10.2013.
 */
CLASSMetamodel.prototype.createClass = function (name) {
  var classXML = this.documentXML.createElement('Class');
  classXML.setAttribute('name', name);
  classXML.setAttribute('isAbstract', 'false');
  classXML.setAttribute('isStatic', 'false');
  classXML.setAttribute('visibility', VisibilityKind.PUBLIC);
  return classXML;
};

/**
 * Factory method for an interface element.
 * 
 * @param name
 *          The name of the interface.
 * @author Gabriel Leonardo Diaz, 20.10.2013.
 */
CLASSMetamodel.prototype.createInterface = function (name) {
  var interfaceXML = this.documentXML.createElement('Interface');
  interfaceXML.setAttribute('name', name);
  interfaceXML.setAttribute('visibility', VisibilityKind.PUBLIC);
  return interfaceXML;
};

/**
 * Factory method for an UML package element.
 * 
 * @param name
 *          The name of the package.
 * @returns The package user object.
 * @author Gabriel Leonardo Diaz, 20.10.2013.
 */
CLASSMetamodel.prototype.createPackage = function (name) {
  var packageXML = this.documentXML.createElement('Package');
  packageXML.setAttribute('name', name);
  return packageXML;
};

/**
 * Factory method for a UML comment element.
 * 
 * @param body
 *          The text of the comment body.
 * @returns The comment element.
 * @author Gabriel Leonardo Diaz, 20.10.2013.
 */
CLASSMetamodel.prototype.createComment = function (body) {
  var commentXML = this.documentXML.createElement('Comment');
  commentXML.setAttribute('body', body);
  return commentXML;
};

/**
 * Factory method for an enumeration element.
 * 
 * @param name
 *          The name of the enumeration.
 * @returns The new enumeration.
 * @author Gabriel Leonardo Diaz, 19.10.2013.
 */
CLASSMetamodel.prototype.createEnumeration = function (name) {
  var enumerationXML = this.documentXML.createElement('Enumeration');
  enumerationXML.setAttribute('name', name);
  enumerationXML.setAttribute('isStatic', 'false');
  enumerationXML.setAttribute('visibility', VisibilityKind.PUBLIC);
  return enumerationXML;
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
 * Factory method for an enumeration literal element.
 * 
 * @param name
 *          The name of the enumeration literal.
 * @author Gabriel Leonardo Diaz, 19.10.2013.
 */
CLASSMetamodel.prototype.createEnumerationLiteral = function (name) {
  var literalXML = this.documentXML.createElement('EnumerationLiteral');
  literalXML.setAttribute('name', name);
  return literalXML;
};

/**
 * Factory method for an association element.
 * 
 * @param name
 *          The name of the association.
 * @author Gabriel Leonardo Diaz, 21.10.2013.
 */
CLASSMetamodel.prototype.createAssociation = function (name) {
  var associationXML = this.documentXML.createElement('Association');
  associationXML.setAttribute('name', name);
  return associationXML;
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

/**
 * Constants that represent the aggregation kinds for a property.
 * 
 * @author Gabriel Leonardo Diaz, 21.10.2013.
 */
var AggregationKind = {
  SHARED    : 'Shared',
  NONE      : 'None',
  COMPOSITE : 'Composite'
};
