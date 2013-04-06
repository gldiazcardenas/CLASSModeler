/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia (c)
 * 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml.kernel;

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
   * Sets the name of the named element.
   * @param name
   */
  public void setName(String name);

  /**
   * The visibility determines where the NamedElement appears within different
   * Namespaces within the overall model, and its accessibility.
   * 
   * @return
   */
  public EVisibilityKind getVisibility();

  /**
   * A name that allows the NamedElement to be identified within a hierarchy of
   * nested Namespaces. It is constructed from the names of the containing
   * namespaces starting at the root of the hierarchy and ending with the name
   * of the NamedElement itself
   * 
   * @return
   */
  public String getQualifiedName();

  /**
   * The query gives the string that is used to separate names when constructing
   * a qualified name.<br/>
   * 
   * <code>separator = ‘::’</code>
   * @return
   */
  public String getSeparator();

  /**
   * The query gives the sequence of namespaces in which the NamedElement is
   * nested, working outwards.
   * 
   * <code>
   * allNamespaces = if self.namespace->isEmpty() then Sequence{}
                     else self.namespace.allNamespaces()->prepend(self.namespace)
                     endif
   * </code>
   * 
   * @return
   */
  public List<INamespace> getAllNamespaces();

  /**
   * The query determines whether two NamedElements may logically co-exist
   * within a Namespace. By default, two named elements are distinguishable if
   * (a) they have unrelated types or (b) they have related types but different
   * names.
   * 
   * @param ne
   *          The named element to compare.
   * @param ns
   *          the namespace in which will be compared the named elements.
   */
  public boolean isDistinguishableFrom(INamedElement ne, INamespace ns);

}