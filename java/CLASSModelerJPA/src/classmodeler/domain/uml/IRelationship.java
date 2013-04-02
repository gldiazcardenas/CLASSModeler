/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * C�cuta, Colombia
 * 
 ****************************************************/

package classmodeler.domain.uml;

import java.util.Set;

/**
 * Defines the common behavior of any relationship element.
 * @author Gabriel Leonardo Diaz, 24.03.2013.
 * @version 1.0
 */
public interface IRelationship extends IElement {

  public Set<IElement> getRelatedElements();

}