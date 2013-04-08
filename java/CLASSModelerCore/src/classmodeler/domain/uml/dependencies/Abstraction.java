/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia (c) 2013 by
 * UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml.dependencies;

import classmodeler.domain.uml.kernel.impl.Expression;

/**
 * An abstraction is a relationship that relates two elements or sets of
 * elements that represent the same concept at different levels of abstraction
 * or from different viewpoints. In the metamodel, an Abstraction is a
 * Dependency in which there is a mapping between the supplier and the client.
 * 
 * @author Gabriel Leonardo Diaz, 24.03.2013.
 */
public class Abstraction extends Dependency {
  
  /**
   * A composition of an Expression that states the abstraction relationship
   * between the supplier and the client. In some cases, such as Derivation, it
   * is usually formal and unidirectional. In other cases, such as Trace, it is
   * usually informal and bidirectional. The mapping expression is optional and
   * may be omitted if the precise relationship between the elements is not
   * specified.
   */
  private Expression mapping;
  
  public Abstraction() {
    super();
  }
  
  public Expression getMapping() {
    return mapping;
  }
  
  public void setMapping(Expression mapping) {
    this.mapping = mapping;
  }
}