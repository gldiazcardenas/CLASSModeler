/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml.kernel;

/**
 * Enumeration that represents all different kinds of aggregation.
 * 
 * @author Gabriel Leonardo Diaz, 24.03.2013.
 * @version 1.0
 */
public enum EAggregationKind {
  
  /**
   * Indicates that the property has no aggregation.
   */
  NONE,
  
  /**
   * Indicates that the property has a shared aggregation.
   */
  SHARED,
  
  /**
   * Indicates that the property is aggregated compositely, i.e., the composite
   * object has responsibility for the existence and storage of the composed
   * objects (parts).
   */
  COMPOSITE
}