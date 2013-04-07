/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml.kernel.impl;

import java.util.List;
import java.util.Set;

import classmodeler.domain.uml.kernel.INamedElement;
import classmodeler.domain.uml.kernel.IPackageableElement;

/**
 * A package is a namespace for its members, and may contain other packages.
 * Only packageable elements can be owned members of a package. By virtue of
 * being a namespace, a package can import either individual members of other
 * packages, or all the members of other packages. In addition a package can be
 * merged with other packages.
 * 
 * @author Gabriel Leonardo Diaz, 24.03.2013.
 */
public class Package extends Namespace implements IPackageableElement {
  
  /**
   * Provides an identifier for the package that can be used for many purposes.
   * A URI is the universally unique identification of the package following the
   * IETF URI specification, RFC 2396 http://www.ietf.org/rfc/rfc2396.txt and it
   * must comply with those syntax rules.
   */
  private String URI;
  //public List<PackageMerge> packagesMerged;
  public List<IPackageableElement> packagedElement;
  //public List<IType> ownedTypes;
  public List<Package> nestedPackages;
  
  public Package() {
    super();
  }
  
  public String getURI() {
    return URI;
  }
  
  /**
   * Defines which members of a Package can be accessed outside it.
   * 
   * @return
   */
  public Set<IPackageableElement> visibleMembers() {
    return null;
  }
  
  public boolean makesVisible(INamedElement el) {
    return el != null;
  }
  
  /**
   * This method redefines the default method in Element class, by default a
   * package must not be owned.
   */
  @Override
  public boolean mustBeOwned() {
    return false;
  }
}