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
import classmodeler.domain.uml.kernel.IPackageableElement;

/**
 * An element import is defined as a directed relationship between an importing
 * namespace and a packageable element. The name of the packageable element or
 * its alias is to be added to the namespace of the importing namespace. It is
 * also possible to control whether the imported element can be further
 * imported.
 * 
 * @author Gabriel Leonardo Diaz, 24.03.2013.
 */
public class ElementImport extends DirectedRelationship {
  
  /**
   * Specifies the name that should be added to the namespace of the importing
   * Package in lieu of the name of the imported PackagableElement. The aliased
   * name must not clash with any other member name in the importing Package. By
   * default, no alias is used.
   */
  private String alias;
  
  /**
   * Specifies the visibility of the imported PackageableElement within the
   * importing Package. The default visibility is the same as that of the
   * imported element. If the imported element does not have a visibility, it is
   * possible to add visibility to the element import. Default value is public.
   */
  private EVisibilityKind visibility = EVisibilityKind.PUBLIC;
  
  /**
   * Specifies the PackageableElement whose name is to be added to a Namespace.
   * Subsets DirectedRelationship::target.
   */
  private IPackageableElement importedElement;
  
  /**
   * Specifies the Namespace that imports a PackageableElement from another
   * Package. Subsets DirectedRelationship::source and Element::owner.
   */
  private INamespace importingNamespace;
  
  public ElementImport() {
    super();
  }
  
  public String getAlias() {
    return alias;
  }
  
  public void setAlias(String alias) {
    this.alias = alias;
  }
  
  public EVisibilityKind getVisibility() {
    return visibility;
  }
  
  public void setVisibility(EVisibilityKind visibility) {
    this.visibility = visibility;
  }
  
  public IPackageableElement getImportedElement() {
    return importedElement;
  }
  
  public void setImportedElement(IPackageableElement importedElement) {
    this.importedElement = importedElement;
  }
  
  public INamespace getImportingNamespace() {
    return importingNamespace;
  }
  
  public void setImportingNamespace(INamespace importingNamespace) {
    this.importingNamespace = importingNamespace;
  }
  
  /**
   * The query returns the name under which the imported
   * PackageableElement will be known in the importing namespace.
   * 
   * @return
   */
  public String getName() {
    if (alias != null && !alias.isEmpty()) {
      return alias;
    }
    
    return importedElement.getName();
  }
  
}