/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.test;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

/**
 * This abstract class is the base for all EJB services methods.
 * 
 * @author Gabriel Leonardo Diaz, 09.09.2013.
 */
public abstract class ServiceTest {
  
  /**
   * Instance of the embeddable container.
   */
  protected EJBContainer ejbContainer;
  
  /**
   * @throws NamingException In case the 
   */
  @BeforeClass
  public void setUp () throws NamingException {
    ejbContainer = EJBContainer.createEJBContainer();
  }
  
  @AfterClass
  public void tearDown () {
    ejbContainer.close();
  }

}
