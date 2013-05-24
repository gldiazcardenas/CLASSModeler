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
   * Use this method to validate the values given in the form before to process
   * them.
   * 
   * @return A <code>boolean</code> value indicating if the values are correct
   *         or not.
   * @author Gabriel Leonardo Diaz, 02.03.2013.
   */
  public boolean isAllValid ();
  
  /**
   * Use this method to implement the logic that handles the values provided by
   * the form. This should be used when the it is an asynchronous request AJAX.
   * @author Gabriel Leonardo Diaz, 23.05.2013.
   */
  public void processAJAX ();
  
  /**
   * Use this method to implement the logic that handles the values provided by
   * the form.
   * 
   * @return The OUTCOME redirecting to another page, or <code>null</code> to
   *         stay in the current page.
   * @author Gabriel Leonardo Diaz, 02.03.2013.
   */
  public String process ();

}
