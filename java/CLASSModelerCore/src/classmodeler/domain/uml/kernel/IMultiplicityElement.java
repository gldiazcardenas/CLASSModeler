/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml.kernel;


/**
 * A MultiplicityElement is an abstract MetaClass that includes optional
 * attributes for defining the bounds of a multiplicity. A MultiplicityElement
 * also includes specifications of whether the values in an instantiation of
 * this element must be unique or ordered.
 * 
 * @author Gabriel Leonardo Diaz, 24.03.2013.
 */
public interface IMultiplicityElement extends IElement {
  
  /**
   * For a multivalued multiplicity, this attribute specifies whether the values
   * in an instantiation of this element are sequentially ordered. Default is
   * false.
   * 
   * @return
   */
  public boolean isOrdered();
  
  /**
   * For a multivalued multiplicity, this attributes specifies whether the
   * values in an instantiation of this element are unique. Default is true.
   * 
   * @return
   */
  public boolean isUnique();
  
  /**
   * Checks whether this multiplicity has an upper bound greater than one.
   * pre: upperBound()->notEmpty()
   * isMultivalued = (upperBound() > 1)
   * 
   * @return
   */
  public boolean isMultivalued ();
  
  /**
   * Checks whether the specified cardinality is valid for this multiplicity.
   * pre: upperBound()->notEmpty() and lowerBound()->notEmpty()
   * includesCardinality = (lowerBound() <= C) and (upperBound() >= C)
   * 
   * @return
   */
  public boolean includesCardinality ();
  
  /**
   * Checks whether this multiplicity includes all the cardinalities allowed by
   * the specified multiplicity.
   * pre: self.upperBound()->notEmpty() and
   *      self.lowerBound()->notEmpty() and
   *      M.upperBound()->notEmpty() and
   *      M.lowerBound()->notEmpty()
   * includesMultiplicity = (self.lowerBound() <= M.lowerBound()) and (self.upperBound() >= M.upperBound())
   * 
   * @param multiplicityElement
   * @return
   */
  public boolean includesMultiplicity (IMultiplicityElement multiplicityElement);
  
  /**
   * Returns the lower bound of the multiplicity as an integer.
   * lowerBound = if lowerValue->isEmpty() then 1 else lowerValue.integerValue() endif.
   * 
   * @return
   */
  public int lowerBound ();
  
  /**
   * Returns the upper bound of the multiplicity for a bounded multiplicity as
   * an unlimited natural.
   * upperBound = if upperValue->isEmpty() then 1 else upperValue.unlimitedValue() endif.
   * 
   * @return
   */
  public long upperBound ();
  
}
