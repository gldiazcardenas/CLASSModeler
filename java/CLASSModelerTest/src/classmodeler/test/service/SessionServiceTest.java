/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.test.service;

import javax.naming.NamingException;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import classmodeler.domain.user.Diagrammer;
import classmodeler.domain.user.EDiagrammerAccountStatus;
import classmodeler.domain.user.EGender;
import classmodeler.domain.user.User;
import classmodeler.service.SessionService;
import classmodeler.service.UserService;
import classmodeler.service.bean.InsertDiagrammerResult;
import classmodeler.service.exception.ExpiredSecurityCodeException;
import classmodeler.service.exception.InvalidDiagrammerAccountException;
import classmodeler.service.exception.InvalidSecurityCodeException;
import classmodeler.service.exception.SendEmailException;
import classmodeler.service.implementation.SessionServiceBean;
import classmodeler.service.implementation.UserServiceBean;

/**
 * Unit tests for the service {@link SessionService}
 * 
 * @author Gabriel Leonardo Diaz, 06.10.2013.
 */
@Test
public class SessionServiceTest extends ServiceTest {

  private UserService userService;
  private SessionService sessionService;
  private Diagrammer diagrammerBasic;
  
  @Override
  @BeforeClass
  public void configure() throws NamingException {
    userService    = (UserService) context.lookup(getServiceObjectName(UserServiceBean.class.getSimpleName()));
    sessionService = (SessionService) context.lookup(getServiceObjectName(SessionServiceBean.class.getSimpleName()));
    
    diagrammerBasic = new Diagrammer();
    diagrammerBasic.setFirstName("Basic Diagrammer");
    diagrammerBasic.setLastName("Basic Diagrammer");
    diagrammerBasic.setEmail("email.basic@gmail.com");
    diagrammerBasic.setPassword("12345");
    diagrammerBasic.setGender(EGender.MALE);
    
    try {
      InsertDiagrammerResult result = userService.insertDiagrammer(diagrammerBasic);
      diagrammerBasic               = result.getDiagrammer();
    }
    catch (InvalidDiagrammerAccountException e) {
      // Do nothing
    }
    catch (SendEmailException e) {
      // Do nothing
    }
  }

  @Override
  @AfterClass
  public void destroy() {
    userService.deleteDiagrammer(diagrammerBasic.getKey());
  }
  
  /**
   * Unit test to verify the correct behavior of the service method
   * {@link SessionService#logIn(String, String)}.
   * 
   * @author Gabriel Leonardo Diaz, 06.10.2013.
   * @throws SendEmailException 
   * @throws InvalidDiagrammerAccountException 
   * @throws ExpiredSecurityCodeException 
   * @throws InvalidSecurityCodeException 
   */
  public void testLogIn () throws InvalidDiagrammerAccountException, SendEmailException, InvalidSecurityCodeException, ExpiredSecurityCodeException {
    String email = "gabriel.test.12345@gmail.com";
    
    Diagrammer diagrammer = new Diagrammer();
    diagrammer.setFirstName("Nombre Diagramador");
    diagrammer.setLastName("Apellido Diagramador");
    diagrammer.setEmail(email);
    diagrammer.setPassword("12345");
    diagrammer.setGender(EGender.MALE);
    
    InsertDiagrammerResult result = null;
    
    try {
      result = userService.insertDiagrammer(diagrammer);
      
      assert (result != null) : "Error: The result of inserting a diagrammer is null";
      assert (result.getDiagrammer() != null) : "Error: The diagrammer inside the result is null";
      assert (result.getSecurityCode() != null) : "Error: The security code inside the result is null";
      
      Diagrammer diagrammerActivated = userService.activateDiagrammerAccount(result.getDiagrammer().getEmail(), result.getSecurityCode().getCode());
      
      assert (diagrammerActivated != null) : "Error: The activation process has failed.";
      assert (diagrammerActivated.getAccountStatus() == EDiagrammerAccountStatus.ACTIVATED) : "Error: The account of the diagrammer is not activated.";
      
      User loggedIn = sessionService.logIn(result.getDiagrammer().getEmail(), result.getDiagrammer().getPassword());
      
      assert (loggedIn != null) : "Error: The diagrammer has not been logged in" ;
    }
    finally {
      if (result != null) {
        userService.deleteDiagrammer(result.getDiagrammer().getKey());
      }
    }
  }
  
  /**
   * Unit test to check an alternate flow of the service method
   * {@link SessionService#logIn(String, String)} by login in with an invalid
   * non existing account.
   * 
   * @author Gabriel Leonardo Diaz, 06.10.2013.
   * @throws InvalidDiagrammerAccountException 
   */
  @Test (expectedExceptions = InvalidDiagrammerAccountException.class)
  public void testLogIn_failNonExistingAccount () throws InvalidDiagrammerAccountException {
    sessionService.logIn("non_existing_account@email.com", "12345");
  }
  
  /**
   * Unit test to check an alternate flow of the service method
   * {@link SessionService#logIn(String, String)} by login in with a non
   * activated account.
   * 
   * @author Gabriel Leonardo Diaz, 06.10.2013.
   * @throws InvalidDiagrammerAccountException 
   */
  @Test (expectedExceptions = InvalidDiagrammerAccountException.class)
  public void testLogIn_failNonActivatedAccount () throws InvalidDiagrammerAccountException {
    sessionService.logIn(diagrammerBasic.getEmail(), diagrammerBasic.getPassword());
  }
  
}
