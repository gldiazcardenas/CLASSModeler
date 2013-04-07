package classmodeler.domain.uml.dependencies;

import java.util.List;

import classmodeler.domain.uml.kernel.EVisibilityKind;
import classmodeler.domain.uml.kernel.INamedElement;
import classmodeler.domain.uml.kernel.INamespace;
import classmodeler.domain.uml.kernel.IPackageableElement;
import classmodeler.domain.uml.kernel.impl.DirectedRelationship;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:22 p.m.
 */
public class Dependency extends DirectedRelationship implements IPackageableElement {

  public Dependency() {
    
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
    // TODO Auto-generated method stub
    return null;
  }
}