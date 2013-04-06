package classmodeler.domain.uml;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:27 p.m.
 */
public class ValueSpecification extends NamedElement implements IPackageableElement, ITypedElement {
  
  public MultiplicityElement owningUpper;
  public MultiplicityElement owningLower;
  
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