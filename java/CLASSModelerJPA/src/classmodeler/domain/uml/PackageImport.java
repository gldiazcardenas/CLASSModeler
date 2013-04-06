package classmodeler.domain.uml;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:25 p.m.
 */
public class PackageImport extends DirectedRelationship {
  
  public EVisibilityKind visibility;
  public Package importedPackage;
  public Namespace importingNamespace;
  
  public PackageImport() {
    
  }
}