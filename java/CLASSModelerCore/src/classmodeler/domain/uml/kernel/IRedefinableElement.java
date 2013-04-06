/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml.kernel;

/**
 * A redefinable element is a named element that can be redefined in the context
 * of a generalization.
 * 
 * @author Gabriel Leonardo Diaz, 24.03.2013.
 */
public interface IRedefinableElement extends INamedElement {

  /**
   * Indicates whether it is possible to further redefine a RedefinableElement.
   * If the value is true, then it is not possible to further redefine the
   * RedefinableElement. Note that this property is preserved through package
   * merge operations; that is, the capability to redefine a IRedefinableElement
   * (i.e., isLeaf=false) must be preserved in the resulting IRedefinableElement
   * of a package merge operation where a RedefinableElement with isLeaf=false
   * is merged with a matching RedefinableElement with isLeaf=true: the
   * resulting RedefinableElement will have isLeaf=false. Default value is
   * false.
   * 
   * @return
   */
  public boolean isLeaf();

  /**
   * Specifies, for any two IRedefinableElements in a context in which
   * redefinition is possible, whether redefinition would be logically
   * consistent. By default, this is false; this operation must be overridden
   * for subclasses of IRedefinableElement to define the consistency conditions.
   * 
   * @param redefinableElement
   * @return Whether this redefinable element is consitent with the given redefinable element or not.
   */
  public boolean isConsistentWith(IRedefinableElement redefinableElement);

  /**
   * Specifies whether the redefinition
   * contexts of this RedefinableElement are properly related to the
   * redefinition contexts of the specified RedefinableElement to allow this
   * element to redefine the other. By default at least one of the redefinition
   * contexts of this element must be a specialization of at least one of the
   * redefinition contexts of the specified element.
   * 
   * @param redefinableElement
   * @return Whether the redefinition context between this redefinable element
   *         and the given redefinable element or not.
   */
  public boolean isRedefinitionContextValid(IRedefinableElement redefinableElement);

}