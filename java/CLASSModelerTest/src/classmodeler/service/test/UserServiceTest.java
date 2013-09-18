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
public class UserServiceTest extends ServiceTest {
  
  private UserService userService;
  
  @Override
  @BeforeClass
  public void configureService() throws NamingException {
    userService = (UserService) context.lookup(getServiceObjectName(UserServiceBean.class.getSimpleName()));
  }
  
  @Override
  @AfterClass
  public void removeCreatedData() {
    // TODO Auto-generated method stub
  }
  
  /**
   * Unit test to verify the service method {@link UserServiceBean#insertDiagrammer(Diagrammer)}.
   * 
   * @throws SendEmailException
   * @throws InvalidUserAccountException 
   * @author Gabriel Leonardo Diaz, 11.09.2013.
   */
  @Test
  public void testInsertDiagrammer () throws InvalidUserAccountException, SendEmailException {
    Diagrammer diagrammer = new Diagrammer();
    diagrammer.setFirstName("Gabrielito");
    diagrammer.setLastName("JOJOJO");
    diagrammer.setEmail("leonar248@hotmail.com");
    diagrammer.setPassword("12345");
    diagrammer.setGender(EGender.MALE);
    
    userService.insertDiagrammer(diagrammer);
  }
  
}
