/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml.kernel.impl;

import classmodeler.domain.uml.kernel.IFeature;
import classmodeler.domain.uml.kernel.IMultiplicityElement;
import classmodeler.domain.uml.kernel.IRedefinableElement;
import classmodeler.domain.uml.kernel.IType;
import classmodeler.domain.uml.kernel.ITypedElement;

/**
 * A structural feature is a typed feature of a classifier that specifies the
 * structure of instances of the classifier. Structural feature is an abstract
 * metaclass. By specializing multiplicity element, it supports a multiplicity
 * that specifies valid cardinalities for the collection of values associated
 * with an instantiation of the structural feature.
 * 
 * @author Gabriel Leonardo Diaz, 24.03.2013.
 */
public class StructuralFeature extends NamedElement implements IFeature, IMultiplicityElement, ITypedElement {
  
  private boolean readOnly;
  
  public StructuralFeature() {
    super();
  }
  
  public boolean isReadOnly() {
    return readOnly;
  }
  
  public void setReadOnly(boolean readOnly) {
    this.readOnly = readOnly;
  }

  @Override
  public boolean isLeaf() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isConsistentWith(IRedefinableElement redefinableElement) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isRedefinitionContextValid(IRedefinableElement redefinableElement) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public IType getType() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isOrdered() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isUnique() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isMultivalued() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean includesCardinality() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean includesMultiplicity(IMultiplicityElement multiplicityElement) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public int lowerBound() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public long upperBound() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean isStatic() {
    // TODO Auto-generated method stub
    return false;
  }
}