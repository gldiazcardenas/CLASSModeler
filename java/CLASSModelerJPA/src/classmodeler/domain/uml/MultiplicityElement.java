/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml;

/**
 * A MultiplicityElement is an abstract metaclass that includes optional
 * attributes for defining the bounds of a multiplicity. A MultiplicityElement
 * also includes specifications of whether the values in an instantiation of
 * this element must be unique or ordered.
 * 
 * @author Gabriel Leonardo Diaz, 24.03.2013.
 * @version 1.0
 */
public abstract class MultiplicityElement extends Element {

  private boolean ordered;
  private boolean unique;
  private int upper;
  private int lower;
  private ValueSpecification upperValue;
  private ValueSpecification lowerValue;

  public MultiplicityElement() {
    // Empty constructor
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
  
  public int getUpper() {
    return upper;
  }
  
  public void setUpper(int upper) {
    this.upper = upper;
  }
  
  public int getLower() {
    return lower;
  }
  
  public void setLower(int lower) {
    this.lower = lower;
  }
  
  public ValueSpecification getUpperValue() {
    return upperValue;
  }
  
  public void setUpperValue(ValueSpecification upperValue) {
    this.upperValue = upperValue;
  }
  
  public ValueSpecification getLowerValue() {
    return lowerValue;
  }
  
  public void setLowerValue(ValueSpecification lowerValue) {
    this.lowerValue = lowerValue;
  }
  
}