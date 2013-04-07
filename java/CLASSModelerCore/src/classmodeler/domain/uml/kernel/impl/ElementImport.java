package classmodeler.domain.uml.kernel.impl;

import classmodeler.domain.uml.kernel.EVisibilityKind;
import classmodeler.domain.uml.kernel.INamespace;
import classmodeler.domain.uml.kernel.IPackageableElement;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:22 p.m.
 */
public class ElementImport extends DirectedRelationship {
  
  public EVisibilityKind visibility;
  public String alias;
  public IPackageableElement importedElement;
  public INamespace importingNamespace;
  
  public ElementImport() {
    
  }
  
}