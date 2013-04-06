package classmodeler.domain.uml;

import java.util.List;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:22 p.m.
 */
public class DataType extends Classifier {
  
  public List<Property> ownedAttribute;
  public List<Operation> ownedOperation;
  
  public DataType() {
    super();
  }
  
}