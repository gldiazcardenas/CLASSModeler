package classmodeler.domain.uml;

import java.util.List;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:23 p.m.
 */
public class Enumeration extends DataType {
  
  public List<EnumerationLiteral> ownedLiterals;
  
  public Enumeration() {
    
  }
}