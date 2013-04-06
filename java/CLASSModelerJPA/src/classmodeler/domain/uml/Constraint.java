package classmodeler.domain.uml;

import java.util.List;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:22 p.m.
 */
public class Constraint extends NamedElement implements IPackageableElement {
  
  public List<IElement> constrainedElement;
  public Namespace context;
  public ValueSpecification specification;
  
  public Constraint() {
    super();
  }
}