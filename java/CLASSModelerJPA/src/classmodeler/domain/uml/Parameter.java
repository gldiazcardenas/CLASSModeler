package classmodeler.domain.uml;

import java.util.List;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:25 p.m.
 */
public class Parameter extends MultiplicityElement implements ITypedElement {
  
  public EParameterDirectionKind direction;
  public ValueSpecification defaultValue;
  
  public Parameter() {
    
  }
  
  public String getSeparator() {
    return "";
  }
  
  /**
   * 
   * @param namedElement
   * @param namespace
   */
  public boolean isDistinguishableFrom(INamedElement namedElement, Namespace namespace) {
    return false;
  }
  
  public IType getType() {
    return null;
  }
  
  public String getName() {
    return "";
  }
  
  public EVisibilityKind getVisibility() {
    return null;
  }
  
  public String getQualifiedName() {
    return "";
  }

  @Override
  public void setName(String name) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public List<Namespace> getAllNamespaces() {
    // TODO Auto-generated method stub
    return null;
  }
}// end Parameter