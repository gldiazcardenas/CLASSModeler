/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:24 p.m.
 */
public interface IRedefinableElement extends INamedElement {

  public boolean isLeaf();

  /**
   * 
   * @param redefinableElement
   */
  public void isConsistentWith(IRedefinableElement redefinableElement);

  /**
   * 
   * @param redefinableElement
   */
  public void isRedefinitionContextValid(IRedefinableElement redefinableElement);

}