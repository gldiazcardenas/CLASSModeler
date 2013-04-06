package classmodeler.domain.uml;

import java.util.List;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:26 p.m.
 */
public class Property extends StructuralFeature {
  
  public EAggregationKind aggregationKind;
  public boolean derived;
  public boolean derivedUnion;
  public boolean readOnly;
  public boolean isID;
  public List<Property> redefinedProperties;
  public ValueSpecification defaultValue;
  
  public Property() {
    
  }
  
}