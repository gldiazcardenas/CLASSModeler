/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml.kernel.impl;

import classmodeler.domain.uml.kernel.EVisibilityKind;
import classmodeler.domain.uml.kernel.INamespace;

/**
 * A package import is defined as a directed relationship that identifies a
 * package whose members are to be imported by a namespace.
 * 
 * @author Gabriel Leonardo Diaz, 24.03.2013.
 */
public class PackageImport extends DirectedRelationship {
  
  /**
   * Specifies the visibility of the imported PackageableElements within the
   * importing Namespace, i.e., whether imported elements will in turn be
   * visible to other packages that use that importingPackage as an
   * importedPackage. If the PackageImport is public, the imported elements will
   * be visible outside the package, while if it is private they will not. By
   * default, the value of visibility is public.
   */
  private EVisibilityKind visibility = EVisibilityKind.PUBLIC;
  
  /**
   * Specifies the Package whose members are imported into a Namespace. Subsets
   * DirectedRelationship::target.
   */
  private Package importedPackage;
  
  /**
   * Specifies the Namespace that imports the members from a Package. Subsets
   * DirectedRelationship::source and Element::owner.
   */
  private INamespace importingNamespace;
  
  public PackageImport() {
    super();
  }
  
  public EVisibilityKind getVisibility() {
    return visibility;
  }
  
  public void setVisibility(EVisibilityKind visibility) {
    this.visibility = visibility;
  }
  
  public Package getImportedPackage() {
    return importedPackage;
  }
  
  public void setImportedPackage(Package importedPackage) {
    this.importedPackage = importedPackage;
  }
  
  public INamespace getImportingNamespace() {
    return importingNamespace;
  }
  
  public void setImportingNamespace(INamespace importingNamespace) {
    this.importingNamespace = importingNamespace;
  }
  
}