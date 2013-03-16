/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.util;

/**
 * Interface that represents the common behavior of a Form controller JSF bean.
 * 
 * @author Gabriel Leonardo Diaz, 02.03.2013.
 */
public interface JSFFormControllerBean {
  
  /**
   * Use this method to implement the logic that catches and processes the
   * values sent through the form.
   * 
   * @author Gabriel Leonardo Diaz, 02.03.2013.
   */
  public void actionPerformed ();
  
  /**
   * Use this method to validate the values given in the form before to process
   * them.
   * 
   * @return A <code>boolean</code> value indicating if the values are correct
   *         or not.
   * @author Gabriel Leonardo Diaz, 02.03.2013.
   */
  public boolean isAllValid ();

}
