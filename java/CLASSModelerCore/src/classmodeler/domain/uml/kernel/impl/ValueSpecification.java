package classmodeler.domain.uml.kernel.impl;

import classmodeler.domain.uml.kernel.EVisibilityKind;
import classmodeler.domain.uml.kernel.IMultiplicityElement;
import classmodeler.domain.uml.kernel.IType;
import classmodeler.domain.uml.kernel.ITypedElement;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:27 p.m.
 */
public abstract class ValueSpecification extends PackageableElement implements ITypedElement {
  
  public IMultiplicityElement owningUpper;
  public IMultiplicityElement owningLower;
  
  public ValueSpecification() {
    
  }
  
  public boolean isComputable() {
    return false;
  }
  
  public int integerValue() {
    return 0;
  }
  
  public double realValue() {
    return 0;
  }
  
  public boolean booleanValue() {
    return false;
  }
  
  public String stringValue() {
    return "";
  }
  
  public boolean isNull() {
    return false;
  }
  
  public IType getType() {
    return null;
  }
  
  public EVisibilityKind getVisibility() {
    return null;
  }
  
}