/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml;

/**
 * A feature declares a behavioral or structural characteristic of instances of
 * classifiers.
 * 
 * @author Gabriel Leonardo Diaz, 24.03.2013.
 */
public interface IFeature extends IRedefinableElement {

  /**
   * Specifies whether this feature characterizes individual instances
   * classified by the classifier (false) or the classifier itself (true).
   * 
   * @return A boolean value. Default value is false.
   */
  public boolean isStatic();

}