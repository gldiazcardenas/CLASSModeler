package classmodeler.domain.uml.kernel.impl;

import java.util.List;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:06 p.m.
 */
public class Class extends Classifier {
  
  public List<Classifier> nestedClassifiers;
  public List<Property> ownedAttributes;
  public List<Operation> ownedOperations;
  public List<Class> superClasses;
  
  public Class() {
    super();
  }
  
}