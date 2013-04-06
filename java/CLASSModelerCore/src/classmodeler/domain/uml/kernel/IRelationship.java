/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * 
 ****************************************************/

package classmodeler.domain.uml.kernel;

import java.util.Set;

/**
 * A relationship references one or more related elements. Relationship is an
 * abstract metaclass.
 * 
 * @author Gabriel Leonardo Diaz, 24.03.2013.
 */
public interface IRelationship extends IElement {

  /**
   * Specifies the elements related by the Relationship.
   * 
   * @return The elements related.
   */
  public Set<IElement> getRelatedElements();

}