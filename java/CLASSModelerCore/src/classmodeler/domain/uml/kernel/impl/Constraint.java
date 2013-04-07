package classmodeler.domain.uml.kernel.impl;

import java.util.List;

import classmodeler.domain.uml.kernel.IElement;
import classmodeler.domain.uml.kernel.INamespace;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:22 p.m.
 */
public class Constraint extends PackageableElement {
  
  public List<IElement> constrainedElement;
  public INamespace context;
  public ValueSpecification specification;
  
  public Constraint() {
    super();
  }
}