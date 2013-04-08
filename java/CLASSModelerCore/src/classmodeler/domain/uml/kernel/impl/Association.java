/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml.kernel.impl;

import java.util.List;
import java.util.Set;

import classmodeler.domain.uml.kernel.IAssociation;
import classmodeler.domain.uml.kernel.IElement;
import classmodeler.domain.uml.kernel.IType;

/**
 * <p>
 * An association specifies a semantic relationship that can occur between typed
 * instances. It has at least two ends represented by properties, each of which
 * is connected to the type of the end. More than one end of the association may
 * have the same type.
 * </p>
 * <p>
 * An end property of an association that is owned by an end class or that is a
 * navigable owned end of the association indicates that the association is
 * navigable from the opposite ends; otherwise, the association is not navigable
 * from the opposite ends.
 * </p>
 * 
 * @author Gabriel Leonardo Diaz, 24.03.2013.
 */
public class Association extends Classifier implements IAssociation {
  
  /**
   * Specifies whether the association is derived from other model elements such
   * as other associations or constraints. The default value is false.
   */
  public boolean derived;
  
  public List<IType> endTypes;
  
  public List<Property> membersEnd;
  public List<Property> ownedEnd;
  public List<Property> navigableOwnedEnd;
  
  
  public Association() {
    super();
  }
  
  @Override
  public Set<IElement> getRelatedElements() {
    return null;
  }

  @Override
  public boolean isDerived() {
    return derived;
  }
  
  public void setDerived(boolean derived) {
    this.derived = derived;
  }
  
}