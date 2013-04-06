package classmodeler.domain.uml;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:22 p.m.
 */
public class ElementImport extends DirectedRelationship {
  
  public EVisibilityKind visibility;
  public String alias;
  public IPackageableElement importedElement;
  public Namespace importingNamespace;
  
  public ElementImport() {
    
  }
  
}