package classmodeler.domain.uml.kernel.impl;

import java.util.Set;

import classmodeler.domain.uml.kernel.INamedElement;
import classmodeler.domain.uml.kernel.INamespace;

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
  
  /*private List<ElementImport> elementImports;
  private List<PackageImport> packageImports;
  private List<Constraint> ownedRules;
  */
  
  //private List<INamedElement> members;
  //private List<NamedElement> ownedMembers;
  
  public Namespace() {
    super();
  }
  
  /*public List<ElementImport> getElementImports() {
    return elementImports;
  }
  
  public void setElementImports(List<ElementImport> elementImports) {
    this.elementImports = elementImports;
  }
  
  public List<PackageImport> getPackageImports() {
    return packageImports;
  }
  
  public void setPackageImports(List<PackageImport> packageImports) {
    this.packageImports = packageImports;
  }
  
  public List<Constraint> getOwnedRules() {
    return ownedRules;
  }
  
  public void setOwnedRules(List<Constraint> ownedRules) {
    this.ownedRules = ownedRules;
  }*/
  
  /**
   * The query getNamesOfMember() gives a set of all of the names that a member
   * would have in a Namespace. In general a member can have multiple names in a
   * Namespace if it is imported more than once with different aliases. The
   * query takes account of importing. It gives back the set of names that an
   * element would have in an importing namespace, either because it is owned;
   * or if not owned, then imported individually; or if not individually, then
   * from a package. Namespace::getNamesOfMember(element: NamedElement):
   * Set(String); getNamesOfMember = if self.ownedMember ->includes(element)
   * then Set{}->include(element.name) else let elementImports: ElementImport =
   * self.elementImport->select(ei | ei.importedElement = element) in if
   * elementImports->notEmpty() then elementImports->collect(el | el.getName())
   * else self.packageImport->select(pi |
   * pi.importedPackage.visibleMembers()->includes(element))-> collect(pi |
   * pi.importedPackage.getNamesOfMember(element)) endif endif
   * 
   * @param namedElement
   * @return
   */
  public Set<String> getNamesOfMember (INamedElement namedElement) {
    if (namedElement != null) {
      return null;
    }
    return null;
  }
  
  /**
   * The Boolean query membersAreDistinguishable() determines whether all of the
   * namespace’s members are distinguishable within it.
   * Namespace::membersAreDistinguishable() : Boolean; membersAreDistinguishable
   * = self.member->forAll( memb | self.member->excluding(memb)->forAll(other |
   * memb.isDistinguishableFrom(other, self)))
   * 
   * @return
   */
  public boolean membersAreDistinguishable () {
    return false;
  }
}