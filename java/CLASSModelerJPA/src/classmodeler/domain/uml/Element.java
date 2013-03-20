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
 * Elements represent any object in the UML MetaModel. This abstract class is
 * the most generic object of the UML hierarchy tree and all other objects
 * extend from this.
 * 
 * @author Gabriel Leonardo Diaz, 17.03.2013.
 */
public abstract class Element {

  private Element owner;
  private List<Element> subElements;

  public Element() {
    // Empty constructor.
  }
  
  public Element getOwner() {
    return owner;
  }
  
  public void setOwner(Element owner) {
    this.owner = owner;
  }
  
  public List<Element> getSubElements() {
    return subElements;
  }
  
  public void setSubElements(List<Element> subElements) {
    this.subElements = subElements;
  }

  public boolean mustBeOwned() {
    return false;
  }
}