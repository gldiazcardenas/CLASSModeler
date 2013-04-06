/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml;

import java.util.List;

/**
 * A named element represents elements that may have a name. The name is used
 * for identification of the named element within the namespace in which it is
 * defined. A named element also has a qualified name that allows it to be
 * unambiguously identified within a hierarchy of nested namespaces.
 * NamedElement is an abstract metaclass.
 * 
 * @author Gabriel Leonardo Diaz, 24.03.2013.
 * @version 1.0
 */
public abstract class NamedElement extends Element implements INamedElement {

  private String name;
  private String qualifiedName;
  private Namespace namespace;
  private EVisibilityKind visibilityKind;

  public NamedElement() {
    super();
  }

  @Override
  public String getName() {
    return name;
  }
  
  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getQualifiedName() {
    return qualifiedName;
  }

  @Override
  public String getSeparator() {
    return "::";
  }
  
  public EVisibilityKind getVisibility() {
    return visibilityKind;
  }

  public List<Namespace> getAllNamespaces() {
    return null;
  }
  
  public Namespace getNamespace() {
    return namespace;
  }

  @Override
  public boolean isDistinguishableFrom(INamedElement ne, Namespace ns) {
    return false;
  }

}