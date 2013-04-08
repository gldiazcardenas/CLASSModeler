package classmodeler.domain.uml.kernel.impl;

import java.util.Collection;
import java.util.Set;

import classmodeler.domain.uml.kernel.INamedElement;
import classmodeler.domain.uml.kernel.INamespace;
import classmodeler.domain.uml.kernel.IPackageableElement;

/**
 * <p>
 * A namespace is a named element that can own other named elements. Each named
 * element may be owned by at most one namespace. A namespace provides a means
 * for identifying named elements by name. Named elements can be identified by
 * name in a namespace either by being directly owned by the namespace or by
 * being introduced into the namespace by other means (e.g., importing or
 * inheriting). Namespace is an abstract metaclass.
 * </p>
 * 
 * <p>
 * A namespace can own constraints. A constraint associated with a namespace may
 * either apply to the namespace itself, or it may apply to elements in the
 * namespace.
 * </p>
 * 
 * <p>
 * A namespace has the ability to import either individual members or all
 * members of a package, thereby making it possible to refer to those named
 * elements without qualification in the importing namespace. In the case of
 * conflicts, it is necessary to use qualified names or aliases to disambiguate
 * the referenced elements.
 * </p>
 * 
 * @author Gabriel Leonardo Diaz, 24.03.2013.
 */
public abstract class Namespace extends NamedElement implements INamespace {
  
  private Set<ElementImport> elementImports;
  private Set<PackageImport> packageImports;
  private Set<Constraint> ownedRules;
  private Collection<INamedElement> members;
  private Collection<INamedElement> ownedMembers;
  
  public Namespace() {
    super();
  }
  
  public Set<ElementImport> getElementImports() {
    return elementImports;
  }
  
  public void setElementImports(Set<ElementImport> elementImports) {
    this.elementImports = elementImports;
  }
  
  public Set<PackageImport> getPackageImports() {
    return packageImports;
  }
  
  public void setPackageImports(Set<PackageImport> packageImports) {
    this.packageImports = packageImports;
  }
  
  public Set<Constraint> getOwnedRules() {
    return ownedRules;
  }
  
  public void setOwnedRules(Set<Constraint> ownedRules) {
    this.ownedRules = ownedRules;
  }
  
  @Override
  public Set<String> getNamesOfMember (INamedElement namedElement) {
    if (namedElement != null) {
      return null;
    }
    return null;
  }
  
  @Override
  public boolean membersAreDistinguishable () {
    return false;
  }
  
  @Override
  public Set<IPackageableElement> importedMembers(Set<IPackageableElement> imps) {
    // TODO Auto-generated method stub
    return null;
  }
  
  /**
   * The importedMember property is derived from the ElementImports and the
   * PackageImports. importedMember = self.elementImport.importedElement.asSet()->
   *                                  union(self.packageImport.importedPackage->collect(p | p.visibleMembers()))
   * 
   * @return
   */
  public Set<IPackageableElement> getImportedMembers () {
    return null;
  }
  
  @Override
  public Set<IPackageableElement> excludeCollisions(Set<IPackageableElement> imps) {
    // TODO Auto-generated method stub
    return null;
  }
  
  public Collection<INamedElement> getMembers() {
    return members;
  }
  
  public Collection<INamedElement> getOwnedMembers() {
    return ownedMembers;
  }
}