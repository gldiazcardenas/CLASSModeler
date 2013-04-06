/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml;

/**
 * A MultiplicityElement is an abstract MetaClass that includes optional
 * attributes for defining the bounds of a multiplicity. A MultiplicityElement
 * also includes specifications of whether the values in an instantiation of
 * this element must be unique or ordered.
 * 
 * @author Gabriel Leonardo Diaz, 24.03.2013.
 */
public abstract class MultiplicityElement extends Element {

  /**
   * For a multivalued multiplicity, this attribute specifies whether the values
   * in an instantiation of this element are sequentially ordered. Default is
   * false.
   */
  private boolean ordered;
  
  /**
   * For a multivalued multiplicity, this attributes specifies whether the
   * values in an instantiation of this element are unique. Default is true.
   */
  private boolean unique = true;
  
  /**
   * The specification of the lower bound for this multiplicity.
   */
  private ValueSpecification lowerValue;
  
  /**
   * The specification of the upper bound for this multiplicity.
   */
  private ValueSpecification upperValue;
  
  public MultiplicityElement() {
    super();
  }
  
  public boolean isOrdered() {
    return ordered;
  }
  
  public void setOrdered(boolean ordered) {
    this.ordered = ordered;
  }
  
  public boolean isUnique() {
    return unique;
  }
  
  public void setUnique(boolean unique) {
    this.unique = unique;
  }
  
  public ValueSpecification getLowerValue() {
    return lowerValue;
  }
  
  public void setLowerValue(ValueSpecification lowerValue) {
    this.lowerValue = lowerValue;
  }
  
  public ValueSpecification getUpperValue() {
    return upperValue;
  }
  
  public void setUpperValue(ValueSpecification upperValue) {
    this.upperValue = upperValue;
  }
  
  /**
   * Checks whether this multiplicity has an upper bound greater than one.
   * pre: upperBound()->notEmpty()
   * isMultivalued = (upperBound() > 1)
   * 
   * @return
   */
  public boolean isMultivalued () {
    return false;
  }
  
  /**
   * Checks whether the specified cardinality is valid for this multiplicity.
   * pre: upperBound()->notEmpty() and lowerBound()->notEmpty()
   * includesCardinality = (lowerBound() <= C) and (upperBound() >= C)
   * 
   * @return
   */
  public boolean includesCardinality () {
    return false;
  }
  
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
  public boolean includesMultiplicity (MultiplicityElement multiplicityElement) {
    return false;
  }
  
  /**
   * Returns the lower bound of the multiplicity as an integer.
   * lowerBound = if lowerValue->isEmpty() then 1 else lowerValue.integerValue() endif.
   * 
   * @return
   */
  public int lowerBound () {
    return 0;
  }
  
  /**
   * Returns the upper bound of the multiplicity for a bounded multiplicity as
   * an unlimited natural.
   * upperBound = if upperValue->isEmpty() then 1 else upperValue.unlimitedValue() endif.
   * 
   * @return
   */
  public long upperBound () {
    return Long.MAX_VALUE;
  }
  
}