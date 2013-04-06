/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml.kernel;

/**
 * The directions of the parameters in an operation element.
 * 
 * @author Gabriel Leonardo Diaz, 24.03.2013.
 * @version 1.0
 */
public enum EParameterDirectionKind {
  
  /**
   * Indicates that parameter values are passed into the behavioral element by
   * the caller.
   */
  IN,
  
  /**
   * Indicates that parameter values are passed into a behavioral element by the
   * caller and then back out to the caller from the behavioral element.
   */
  INOUT,
  
  /**
   * Indicates that parameter values are passed from a behavioral element out to
   * the caller.
   */
  OUT,
  
  /**
   * Indicates that parameter values are passed as return values from a
   * behavioral element back to the caller.
   */
  RETURN
}