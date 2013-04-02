/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml;

/**
 * Enumeration that represent all possible visibility kinds.
 * 
 * @author Gabriel Leonardo Diaz, 24.03.2013.
 * @version 1.0
 */
public enum EVisibilityKind {
  
  /**
   * A public element is visible to all elements that can access the contents of
   * the {@link Namespace} that owns it.
   */
  PUBLIC,
  
  /**
   * A protected element is visible to elements that have a generalization
   * relationship to the {@link Namespace} that owns it.
   */
  PROTECTED,
  
  /**
   * A package element is owned by a {@link Namespace} that is not a package, and is
   * visible to elements that are in the same package as its owning {@link Namespace}.
   * Only named elements that are not owned by packages can be marked as having
   * package visibility. Any element marked as having package visibility is
   * visible to all elements within the nearest enclosing package (given that
   * other owning elements have proper visibility). Outside the nearest
   * enclosing package, an element marked as having package visibility is not
   * visible.
   */
  PACKAGE,
  
  /**
   * A private element is only visible inside the {@link Namespace} that owns it.
   */
  PRIVATE
}