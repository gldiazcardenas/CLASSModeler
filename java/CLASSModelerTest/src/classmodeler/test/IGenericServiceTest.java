/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.test;

import javax.naming.NamingException;

/**
 * Generic service test definition, this interface defines all common methods
 * that should implement a service unit test class.
 * 
 * @author Gabriel Leonardo Diaz, 16.03.2013.
 */
public interface IGenericServiceTest {
  
  /**
   * Configures the service instance that will be used in the test methods.
   * 
   * @author Gabriel Leonardo Diaz, 16.03.2013.
   */
  public void setupService () throws NamingException;
  
  /**
   * Releases resources obtained in {@link #setupService()} method, after that
   * this nullifies the service instance.
   * 
   * @author Gabriel Leonardo Diaz, 16.03.2013.
   */
  public void tearDownService ();

}
