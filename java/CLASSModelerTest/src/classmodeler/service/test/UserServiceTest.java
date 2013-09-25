package classmodeler.service.test;

import javax.naming.NamingException;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import classmodeler.domain.user.Diagrammer;
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
  
  private UserService userService;
  
  @Override
  @BeforeClass
  public void configureServices() throws NamingException {
    userService = (UserService) context.lookup(getServiceObjectName(UserServiceBean.class.getSimpleName()));
  }
  
  @Override
  @AfterClass
  public void removeCreatedData() {
    // TODO Auto-generated method stub
  }
  
  /**
   * Unit test to verify the diagrammer is correctly found by the method.
   * 
   * @throws InvalidUserAccountException
   * @throws SendEmailException
   * @author Gabriel Leonardo Diaz, 22.09.2013.
   */
  public void testExistsUser () throws InvalidUserAccountException, SendEmailException {
    // TODO
  }
  
  /**
   * Unit test to verify the service method {@link UserServiceBean#insertDiagrammer(Diagrammer)}.
   * 
   * @throws SendEmailException
   * @throws InvalidUserAccountException 
   * @author Gabriel Leonardo Diaz, 11.09.2013.
   */
  public void testInsertDiagrammer () throws InvalidUserAccountException, SendEmailException {
    Diagrammer diagrammer = new Diagrammer();
    diagrammer.setFirstName("Gabrielito");
    diagrammer.setLastName("JOJOJO");
    diagrammer.setEmail("leonar248@hotmail.com");
    diagrammer.setPassword("12345");
    diagrammer.setGender(EGender.MALE);
    
    userService.insertDiagrammer(diagrammer);
  }
  
  /**
   * Unit test to verify an alternate flow of
   * {@link UserService#insertDiagrammer(Diagrammer)} service, when the email
   * used to create a diagrammer account is the same than other account.
   * 
   * @author Gabriel Leonardo Diaz, 24.09.2013.
   */
  public void testInsertDiagrammer_failAlreadyExisting () {
    // TODO
  }
  
  /**
   * Unit test to verify the normal flow of activating the account of a
   * diagrammer.
   * 
   * @author Gabriel Leonardo Diaz, 24.09.2013.
   */
  public void testActivateDiagrammerAccount() {
    // TODO
  }
  
}
