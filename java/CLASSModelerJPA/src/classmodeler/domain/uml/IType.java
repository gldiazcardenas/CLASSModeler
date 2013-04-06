/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml;

/**
 * A type serves as a constraint on the range of values represented by a typed
 * element. Type is an abstract metaclass.
 * 
 * @author Gabri
 */
public interface IType extends IPackageableElement {
  
  /**
   * The query gives true for a type that conforms to another. By default, two
   * types do not conform to each other. This query is intended to be redefined
   * for specific conformance situations.
   * 
   * @param type
   *          The type to compare.
   */
  public boolean conformsTo(IType type);
  
}