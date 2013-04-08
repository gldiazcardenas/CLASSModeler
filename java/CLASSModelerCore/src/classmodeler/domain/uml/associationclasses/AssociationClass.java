/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml.associationclasses;

import java.util.Set;

import classmodeler.domain.uml.kernel.IAssociation;
import classmodeler.domain.uml.kernel.IElement;
import classmodeler.domain.uml.kernel.impl.Class;

/**
 * An AssociationClass is a declaration of a semantic relationship between
 * Classifiers, which has a set of features of its own. AssociationClass is both
 * an Association and a Class. An AssociationClass describes a set of objects
 * that each share the same specifications of features, constraints, and
 * semantics entailed by the AssociationClass as a kind of Class, and correspond
 * to a unique link instantiating the AssociationClass as a kind of Association.
 * An AssociationClass specifies a Class whose instances are in 1-1
 * correspondence with a semantic relationship that can occur between typed
 * instances. An AssociationClass preserves the static and dynamic semantics of
 * both an Association and of a Class.
 * 
 * @author Gabriel Leonardo Diaz, 07.04.2013.
 */
public class AssociationClass extends Class implements IAssociation {
  
  public AssociationClass() {
    // TODO Auto-generated constructor stub
  }

  @Override
  public Set<IElement> getRelatedElements() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isDerived() {
    // TODO Auto-generated method stub
    return false;
  }
}
