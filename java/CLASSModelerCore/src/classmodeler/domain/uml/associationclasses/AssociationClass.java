package classmodeler.domain.uml.associationclasses;

import java.util.Set;

import classmodeler.domain.uml.kernel.IAssociation;
import classmodeler.domain.uml.kernel.IElement;
import classmodeler.domain.uml.kernel.impl.Class;

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
