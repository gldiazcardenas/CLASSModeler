/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml.kernel;

import java.util.Set;

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
public interface INamespace extends INamedElement {
  
  /**
   * <p>
   * The query getNamesOfMember() gives a set of all of the names that a member
   * would have in a Namespace. In general a member can have multiple names in a
   * Namespace if it is imported more than once with different aliases. The
   * query takes account of importing. It gives back the set of names that an
   * element would have in an importing namespace, either because it is owned;
   * or if not owned, then imported individually; or if not individually, then
   * from a package.
   * </p>
   * 
   * Namespace::getNamesOfMember(element: NamedElement): Set(String);
   * getNamesOfMember = if self.ownedMember ->includes(element) then
   * Set{}->include(element.name) else let elementImports: ElementImport =
   * self.elementImport->select(ei | ei.importedElement = element) in if
   * elementImports->notEmpty() then elementImports->collect(el | el.getName())
   * else self.packageImport->select(pi |
   * pi.importedPackage.visibleMembers()->includes(element))-> collect(pi |
   * pi.importedPackage.getNamesOfMember(element)) endif endif
   * 
   * @param namedElement
   * @return
   */
  public Set<String> getNamesOfMember (INamedElement namedElement);
  
  /**
   * <p>
   * The Boolean determines whether all of the namespace’s members are
   * distinguishable within it.
   * </p>
   * <p>
   * Namespace::membersAreDistinguishable() : Boolean;<br>
   * membersAreDistinguishable = self.member -> forAll( memb | self.member->excluding(memb)
   *                                         -> forAll(other | memb.isDistinguishableFrom(other, self)))
   * </p>
   * 
   * @return
   */
  public boolean membersAreDistinguishable ();
  
  /**
   * <p>
   * Defines which of a set of PackageableElements are actually imported into
   * the namespace. This excludes hidden ones, i.e., those that have names that
   * conflict with names of owned members, and also excludes elements that would
   * have the same name when imported.
   * </p>
   * Namespace::importMembers(imps: Set(PackageableElement)): Set(PackageableElement);
   * importMembers = self.excludeCollisions(imps)->select(imp | self.ownedMember->forAll(mem | mem.imp.isDistinguishableFrom(mem, self)))
   * 
   * @param imps
   * @return
   */
  public Set<IPackageableElement> importedMembers (Set<IPackageableElement> imps);
  
  /**
   * <p>
   * Excludes from a set of PackageableElements any that would not be
   * distinguishable from each other in this namespace.
   * </p>
   * 
   * Namespace::excludeCollisions(imps: Set(PackageableElements)):
   * Set(PackageableElements); excludeCollisions = imps->reject(imp1 |
   * imps.exists(imp2 | not imp1.isDistinguishableFrom(imp2, self)))
   * 
   * @param imps
   * @return
   */
  public Set<IPackageableElement> excludeCollisions (Set<IPackageableElement> imps);
}
