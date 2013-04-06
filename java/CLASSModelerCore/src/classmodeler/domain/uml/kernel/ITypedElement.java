/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml.kernel;

/**
 * A typed element is an element that has a type that serves as a constraint on
 * the range of values the element can represent.
 * 
 * @author Gabriel Leonardo Diaz, 24.03.2013.
 */
public interface ITypedElement extends INamedElement {
  
  /**
   * Getter for the type of the TypedElement
   * 
   * @return The type of the TypedElement.
   */
  public IType getType();
  
}