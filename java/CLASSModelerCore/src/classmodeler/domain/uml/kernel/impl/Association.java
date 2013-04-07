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
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:21 p.m.
 */
public class Association extends Classifier implements IAssociation {
  
  public boolean derived;
  public List<Property> membersEnd;
  public List<Property> ownedEnd;
  public List<Property> navigableOwnedEnd;
  public List<IType> endTypes;
  
  public Association() {
    super();
  }
  
  @Override
  public Set<IElement> getRelatedElements() {
    return null;
  }

  @Override
  public boolean isDerived() {
    // TODO Auto-generated method stub
    return false;
  }
  
}