package classmodeler.domain.uml.kernel.impl;

import java.util.List;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:23 p.m.
 */
public class Expression extends ValueSpecification {
  
  public String symbol;
  public List<ValueSpecification> operand;
  
  public Expression() {
    
  }
}