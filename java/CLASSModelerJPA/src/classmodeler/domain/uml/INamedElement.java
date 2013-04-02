/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia (c)
 * 2013 by UFPS. All rights reserved.
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
 * @author Gabriel Leonardo diaz, 25.03.2013.
 * @version 1.0
 */
public interface INamedElement extends IElement {

  /**
   * Gets the name of the named element.
   * @return The name in a String object.
   */
  public String getName();

  /**
   * The visibility determines where the NamedElement appears within different
   * Namespaces within the overall model, and its accessibility.
   * 
   * @return
   */
  public EVisibilityKind getVisibility();

  /**
   * 
   * @return
   */
  public String getQualifiedName();

  /**
   * 
   * @return
   */
  public String getSeparator();

  /**
   * The query gives the sequence of namespaces in which the
   * NamedElement is nested, working outwards.
   * 
   * @return
   */
  public List<Namespace> getAllNamespaces();

  /**
   * The query determines whether two NamedElements may logically co-exist
   * within a Namespace. By default, two named elements are distinguishable if
   * (a) they have unrelated types or (b) they have related types but different
   * names.
   * 
   * @param namedElement
   * @param namespace
   */
  public boolean isDistinguishableFrom(INamedElement namedElement, Namespace namespace);

}