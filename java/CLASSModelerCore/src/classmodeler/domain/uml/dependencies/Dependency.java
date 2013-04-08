/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml.dependencies;

import java.util.List;

import classmodeler.domain.uml.kernel.EVisibilityKind;
import classmodeler.domain.uml.kernel.INamedElement;
import classmodeler.domain.uml.kernel.INamespace;
import classmodeler.domain.uml.kernel.IPackageableElement;
import classmodeler.domain.uml.kernel.impl.DirectedRelationship;

/**
 * A dependency is a relationship that signifies that a single or a set of model
 * elements requires other model elements for their specification or
 * implementation. This means that the complete semantics of the depending
 * elements is either semantically or structurally dependent on the definition
 * of the supplier element(s).
 * 
 * @author Gabriel Leonardo Diaz, 24.03.2013.
 */
public class Dependency extends DirectedRelationship implements IPackageableElement {
  
  private String name;
  private EVisibilityKind visibilityKind = EVisibilityKind.PUBLIC;

  public Dependency() {
    super();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
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
  public List<INamespace> getAllNamespaces() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isDistinguishableFrom(INamedElement ne, INamespace ns) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public EVisibilityKind getVisibility() {
    return visibilityKind;
  }
  
  public void setVisibilityKind(EVisibilityKind visibilityKind) {
    this.visibilityKind = visibilityKind;
  }
}