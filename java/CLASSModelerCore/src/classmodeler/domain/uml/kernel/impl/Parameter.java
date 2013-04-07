package classmodeler.domain.uml.kernel.impl;

import classmodeler.domain.uml.kernel.EParameterDirectionKind;
import classmodeler.domain.uml.kernel.IMultiplicityElement;
import classmodeler.domain.uml.kernel.IType;
import classmodeler.domain.uml.kernel.ITypedElement;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:25 p.m.
 */
public class Parameter extends NamedElement implements IMultiplicityElement, ITypedElement {
  
  public EParameterDirectionKind direction;
  public ValueSpecification defaultValue;
  
  public Parameter() {
    
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
}