/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.test;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

/**
 * Abstract class is the base for all EJB Service classes.
 * 
 * @author Gabriel Leonardo Diaz, 09.09.2013.
 */
public abstract class ServiceTest {
  
  public static final String APP_TEST_NAME = "CLASSModelerTest";
  
  /**
   * The embedded Glassfish EJB Container.
   */
  protected static EJBContainer ejbContainer;
  
  /**
   * The naming context of the Container.
   */
  protected static Context context;
  
  /**
   * @throws NamingException In case the 
   */
  @BeforeSuite
  public void setUp () {
    Map<String, String> properties = new HashMap<String, String>();
    properties.put(EJBContainer.APP_NAME, APP_TEST_NAME);
    properties.put ("org.glassfish.ejb.embedded.glassfish.installation.root", "/path/to/glassfish");
    
    ejbContainer = EJBContainer.createEJBContainer(properties);
    context      = ejbContainer.getContext();
  }
  
  @AfterSuite
  public void tearDown () {
    ejbContainer.close();
  }
  
  /**
   * Constructs the service object name with the received service simple class
   * name.
   * 
   * @param simpleClassName
   *          The simple name of the service class.
   * @return The object name to be looked up by the context.
   * @author Gabriel Leonardo Diaz, 11.09.2013.
   */
  public String getServiceObjectName (String simpleClassName) {
    return "java:global/" + APP_TEST_NAME + "/" + simpleClassName;
  }
  
  /**
   * This method must be implemented by concrete service test classes in order
   * to get the instance of the service object used in the test methods.
   * 
   * @author Gabriel Leonardo Diaz, 11.09.2013.
   */
  public abstract void configureService () throws NamingException;
  
  /**
   * Implement this method to remove the data created by the test.
   * 
   * @author Gabriel Leonardo Diaz, 11.09.2013.
   */
  public abstract void removeCreatedData ();
  
}
