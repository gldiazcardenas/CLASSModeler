/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml;

import java.util.List;

/**
 * A structural feature is a typed feature of a classifier that specifies the
 * structure of instances of the classifier. Structural feature is an abstract
 * metaclass. By specializing multiplicity element, it supports a multiplicity
 * that specifies valid cardinalities for the collection of values associated
 * with an instantiation of the structural feature.
 * 
 * @author Gabriel Leonardo Diaz, 24.03.2013.
 */
public class StructuralFeature extends MultiplicityElement implements ITypedElement, IFeature {
  
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
  public String getName() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setName(String name) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public EVisibilityKind getVisibility() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getQualifiedName() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getSeparator() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Namespace> getAllNamespaces() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isDistinguishableFrom(INamedElement ne, Namespace ns) {
    // TODO Auto-generated method stub
    return false;
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
  public boolean isStatic() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public IType getType() {
    // TODO Auto-generated method stub
    return null;
  }
}