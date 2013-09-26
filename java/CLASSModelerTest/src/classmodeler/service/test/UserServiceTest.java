package classmodeler.service.test;

import javax.naming.NamingException;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import classmodeler.domain.user.Diagrammer;
import classmodeler.domain.user.EDiagrammerAccountStatus;
import classmodeler.domain.user.EGender;
import classmodeler.service.UserService;
import classmodeler.service.exception.InvalidUserAccountException;
import classmodeler.service.exception.SendEmailException;
import classmodeler.service.implementation.UserServiceBean;

/**
 * Class that contains all unit tests developed to verify the correct operation
 * of User Service.
 * 
 * @author Gabriel Leonardo Diaz, 11.09.2013.
 */
@Test
public class UserServiceTest extends ServiceTest {
  
  private Diagrammer diagrammerBasic;
  private UserService userService;
  
  @Override
  @BeforeClass
  public void configure() throws NamingException {
    userService = (UserService) context.lookup(getServiceObjectName(UserServiceBean.class.getSimpleName()));
    
    diagrammerBasic = new Diagrammer();
    diagrammerBasic.setFirstName("Basic Diagrammer");
    diagrammerBasic.setLastName("Basic Diagrammer");
    diagrammerBasic.setEmail("email.basic@gmail.com");
    diagrammerBasic.setPassword("12345");
    diagrammerBasic.setGender(EGender.MALE);
    
    try {
      userService.insertDiagrammer(diagrammerBasic);
    }
    catch (InvalidUserAccountException e) {
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
    
    diagrammerBasic = null;
    userService     = null;
  }
  
  /**
   * Unit test to verify the diagrammer is correctly found by the method.
   * 
   * @throws InvalidUserAccountException
   * @throws SendEmailException
   * @author Gabriel Leonardo Diaz, 22.09.2013.
   */
  public void testExistsUser () throws InvalidUserAccountException, SendEmailException {
    assert (userService.existsUser(diagrammerBasic.getEmail())) : "Error: The user was not found";
  }
  
  /**
   * Unit test to verify the service method {@link UserService#insertDiagrammer(Diagrammer)}.
   * 
   * @throws SendEmailException
   * @throws InvalidUserAccountException 
   * @author Gabriel Leonardo Diaz, 11.09.2013.
   */
  public void testInsertDiagrammer () throws InvalidUserAccountException, SendEmailException {
    String email = "leonar24834544646@gmail.com";
    
    Diagrammer diagrammer = new Diagrammer();
    diagrammer.setFirstName("Nombre Diagramador");
    diagrammer.setLastName("Apellido Diagramador");
    diagrammer.setEmail(email);
    diagrammer.setPassword("12345");
    diagrammer.setGender(EGender.MALE);
    
    userService.insertDiagrammer(diagrammer);
    
    Diagrammer afterInsert = userService.getDiagrammerByEmail(email);
    
    try {
      assert (afterInsert != null) : "Error: The diagrammer was not created";
      assert (afterInsert.getAccountStatus() == EDiagrammerAccountStatus.INACTIVATED) : "Error: The account status must be INACTIVATED after inserting";
      assert (email.equals(afterInsert.getEmail())) : "Error: The email does not match with the saved value.";
    }
    finally {
      userService.deleteDiagrammer(diagrammer.getKey());
    }
  }
  
  /**
   * Unit test to verify an alternate flow of
   * {@link UserService#insertDiagrammer(Diagrammer)} service, when the email
   * used to create a diagrammer account is the same than other account.
   * 
   * @author Gabriel Leonardo Diaz, 24.09.2013.
   * @throws SendEmailException 
   * @throws InvalidUserAccountException 
   */
  @Test (expectedExceptions = InvalidUserAccountException.class)
  public void testInsertDiagrammer_failAlreadyExisting () throws InvalidUserAccountException, SendEmailException {
    Diagrammer diagrammer = new Diagrammer();
    diagrammer.setFirstName("Nombre Diagramador");
    diagrammer.setLastName("Apellido Diagramador");
    diagrammer.setEmail(diagrammerBasic.getEmail());
    diagrammer.setPassword("12345");
    diagrammer.setGender(EGender.MALE);
    
    userService.insertDiagrammer(diagrammer);
  }
  
  /**
   * Unit test to verify the normal flow of activating the account of a
   * diagrammer.
   * 
   * @author Gabriel Leonardo Diaz, 24.09.2013.
   */
  public void testActivateDiagrammerAccount () {
    // TODO
  }
  
  /**
   * Unit test to verify an alternative flow of the service
   * {@link UserService#activateDiagrammerAccount(String, String)} by passing a
   * non existing email.
   * 
   * @author Gabriel Leonardo Diaz, 25.09.2013.
   */
  public void testActivateDiagrammerAccount_failNonExisting () {
    // TODO
  }
  
  /**
   * Unit test to verify an alternative flow of the service
   * {@link UserService#activateDiagrammerAccount(String, String)} by passing an
   * account already activated.
   * 
   * @author Gabriel Leonardo Diaz, 25.09.2013.
   */
  public void testActivateDiagrammerAccount_invalidAccountStatus () {
    // TODO
  }
  
  /**
   * Unit test to verify an alternative flow of the service
   * {@link UserService#activateDiagrammerAccount(String, String)} by passing an
   * invalid verification code.
   * 
   * @author Gabriel Leonardo Diaz, 25.09.2013.
   */
  public void testActivateDiagrammerAccount_invalidVerificationCode () {
    // TODO
  }
  
  /**
   * Unit test to verify an alternative flow of the service
   * {@link UserService#activateDiagrammerAccount(String, String)} by forcing
   * expired verification code.
   * 
   * @author Gabriel Leonardo Diaz, 25.09.2013.
   */
  public void testActivateDiagrammerAccount_expiredVerificationCode () {
    // TODO
  }
  
  /**
   * Unit test to verify the normal flow of the service
   * {@link UserService#requestResetPassword(String)}.
   * 
   * @author Gabriel Leonardo Diaz, 25.09.2013.
   */
  public void testRequestPassword () {
    // TODO
  }
  
  /**
   * Unit test to verify the normal flow of the service
   * {@link UserService#resetPassword(String, String)}.
   * 
   * @author Gabriel Leonardo Diaz, 25.09.2013.
   */
  public void testResetPassword () {
    // TODO
  }
  
  /**
   * Unit test to verify the normal flow of the service
   * {@link UserService#getDiagrammerByEmail(String)}.
   * 
   * @author Gabriel Leonardo Diaz, 25.09.2013
   */
  public void testGetDiagrammerByEmail () {
    // TODO
  }
  
}
