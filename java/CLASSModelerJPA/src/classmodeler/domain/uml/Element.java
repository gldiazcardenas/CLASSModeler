/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml;

import java.util.HashSet;
import java.util.Set;

/**
 * Elements represent any object in the UML MetaModel. This abstract class is
 * the most generic object of the UML hierarchy tree and all other objects
 * extend from this.
 * 
 * @author Gabriel Leonardo Diaz, 17.03.2013.
 * @version 1.0
 */
public abstract class Element implements IElement {

  private int key;
  private IElement owner;
  private Set<IElement> ownedElements;
  private Set<Comment> ownedComments;

  public Element() {
    // Empty constructor.
  }
  
  public int getKey() {
    return key;
  }
  
  public void setKey(int key) {
    this.key = key;
  }
  
  public IElement getOwner() {
    return owner;
  }
  
  public void setOwner(IElement owner) {
    this.owner = owner;
  }

  public boolean mustBeOwned() {
    return true;
  }

  public Set<Comment> getOwnedComments() {
    return ownedComments;
  }
  
  /**
   * Adds the given element to the owned elements of this object. It will be
   * added only if the object is not null and it is not equal to this object (An
   * Element cannot be owned itself).
   * 
   * @param element
   *          The element that will be owned by this.
   */
  public void addOwnedElememnt (IElement element) {
    if (element != null && !this.equals(element)) {
      if (ownedElements == null) {
        ownedElements = new HashSet<>();
      }
      
      ownedElements.add(element);
      element.setOwner(this);
    }
  }
  
  @Override
  public Set<IElement> getOwnedElements() {
    Set<IElement> directlyOwnedElements = new HashSet<IElement>();
    if (ownedElements != null) {
      directlyOwnedElements.addAll(ownedElements);
    }
    return directlyOwnedElements;
  }
  
  @Override
  public Set<IElement> getAllOwnedElements() {
    Set<IElement> allOwnedElements = new HashSet<IElement>();
    if (this.ownedElements != null) {
      allOwnedElements.addAll(ownedElements);
      
      for (IElement ownedElement : ownedElements) {
        allOwnedElements.addAll(ownedElement.getAllOwnedElements());
      }
    }
    return allOwnedElements;
  }
}