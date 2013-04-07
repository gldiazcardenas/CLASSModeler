package classmodeler.domain.uml.kernel.impl;

import classmodeler.domain.uml.kernel.EVisibilityKind;
import classmodeler.domain.uml.kernel.INamespace;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:25 p.m.
 */
public class PackageImport extends DirectedRelationship {
  
  public EVisibilityKind visibility;
  public Package importedPackage;
  public INamespace importingNamespace;
  
  public PackageImport() {
    
  }
}