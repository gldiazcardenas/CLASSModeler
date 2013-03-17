
/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/
package classmodeler.test;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import classmodeler.service.UserService;

/**
 * Unit tests for user service EJB bean.
 *
 * @author Gabriel Leonardo Diaz, 16.03.2013.
 */
public class UserServiceTest implements IGenericServiceTest {
  
  public UserService ejbUserServiceBean;
  
  @Override
  @BeforeClass
  public void setupService() throws NamingException {
    ejbUserServiceBean = (UserService) EJBContainer.createEJBContainer()
                                                   .getContext()
                                                   .lookup("java:global/classes/SeriesBean");
  }
  
  @AfterClass
  public void tearDownService () {
    // TODO Destroy the service
  }

  @Test
  public void activateUserAccount() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void deleteUser() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void existsUser() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void getUserByKey() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void insertUser() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void updateUser() {
    throw new RuntimeException("Test not implemented");
  }
}
