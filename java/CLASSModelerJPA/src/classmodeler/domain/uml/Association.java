/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml;

import java.util.List;
import java.util.Set;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:21 p.m.
 */
public class Association extends Classifier implements IRelationship {
  
  public boolean derived;
  public List<Property> membersEnd;
  public List<Property> ownedEnd;
  public List<Property> navigableOwnedEnd;
  public List<IType> endTypes;
  
  public Association() {
    super();
  }
  
  public Set<IElement> getRelatedElements() {
    return null;
  }
  
}