/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia (c)
 * 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml;

/**
 * A packageable element indicates a named element that may be owned directly by
 * a package.
 * 
 * @author Gabriel Leonardo Diaz, 24.03.2013.
 */
public interface IPackageableElement extends INamedElement {
  
  /**
   * Indicates that packageable elements must always have a visibility (i.e.,
   * visibility is not optional). Redefines NamedElement::visibility. Default
   * value is public.
   * 
   * @return The visibility kind of this packageable element.
   */
  @Override
  public EVisibilityKind getVisibility();
  
}