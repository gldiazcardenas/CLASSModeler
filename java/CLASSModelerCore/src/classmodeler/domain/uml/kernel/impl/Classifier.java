/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml.kernel.impl;

import java.util.List;

import classmodeler.domain.uml.kernel.IFeature;
import classmodeler.domain.uml.kernel.INamedElement;
import classmodeler.domain.uml.kernel.IRedefinableElement;
import classmodeler.domain.uml.kernel.IType;

/**
 * <p>
 * A classifier is a namespace whose members can include features. Classifier is
 * an abstract metaclass.
 * </p>
 * <p>
 * A classifier is a type and can own generalizations, thereby making it
 * possible to define generalization relationships to other classifiers.
 * </p>
 * <p>
 * A classifier can specify a generalization hierarchy by referencing its
 * general classifiers.
 * </p>
 * <p>
 * A classifier is a redefinable element, meaning that it is possible to
 * redefine nested classifiers.
 * </p>
 * 
 * @author Gabriel Leonardo Diaz, 24.03.2013.
 */
public abstract class Classifier extends Namespace implements IType, IRedefinableElement {
  
  public boolean finalSpecification;
  public List<Property> attributes;
  public List<IFeature> features;
  public List<Classifier> generals;
  public List<Generalization> generalizations;
  public List<INamedElement> inheritMember;
  public List<Classifier> redefinedClassifiers;
  
  public Classifier() {
    super();
  }
  
  public boolean isLeaf() {
    return false;
  }

  @Override
  public boolean isConsistentWith(IRedefinableElement redefinableElement) {
    return false;
  }

  @Override
  public boolean isRedefinitionContextValid(IRedefinableElement redefinableElement) {
    return false;
  }

  public boolean conformsTo(IType type) {
    return false;
  }
  
}