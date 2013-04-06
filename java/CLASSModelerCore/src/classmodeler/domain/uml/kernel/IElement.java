/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml.kernel;

import java.util.Set;

/**
 * Interface definition of an UML element. This defines the default behavior of
 * any of the available elements in the UML metamodel.
 * 
 * @author Gabriel Leonardo Diaz, 24.03.2013.
 */
public interface IElement {

  /**
   * Gets the element that owns this element.
   * 
   * @return An <code>IElement</code> object, or null if this element doesn't
   *         have owner.
   */
  public IElement getOwner();

  /**
   * Replaces the owner of this element object.
   * 
   * @param owner
   *          The new owner for the element.
   */
  public void setOwner (IElement owner);
  
  /**
   * Gets the Comments owned by this element.
   * @return A set of <code>Comment</code> objects.
   */
  public Set<IComment> getOwnedComments();
  
  /**
   * Gets the elements directly owned by this object.
   * 
   * @return A set of element objects, If there are not owned elements this
   *         returns an empty set (never null). This returns a new set each time
   *         this method is called.
   */
  public Set<IElement> getOwnedElements ();

  /**
   * <p>
   * The query allOwnedElements() gives all of the direct and indirect owned
   * elements of an element.
   * </p>
   * 
   * <p>
   * allOwnedElements = ownedElements->union(ownedElements-> collect(e |
   * e.allOwnedElements()))
   * </p>
   * 
   * @return The owned elements of this element object. If there are not owned
   *         elements this returns an empty set (never null). This returns a new
   *         set each time this method is called.
   */
  public Set<IElement> getAllOwnedElements();
  
  /**
   * The query indicates whether elements of this type must have an owner.
   * Subclasses of Element that do not require an owner must override this
   * operation.
   * 
   * @return A <code>boolean</code> value that indicates the condition for this
   *         element. By default is <code>true</code>, Elements that do not
   *         require an owner must override this method.
   */
  public boolean mustBeOwned();

}