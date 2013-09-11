package classmodeler.service.test;

import javax.naming.NamingException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import classmodeler.domain.user.Diagrammer;
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
  
  /**
   * Unit test to verify the service method {@link UserServiceBean#insertDiagrammer(Diagrammer)}.
   * 
   * @throws SendEmailException
   * @throws InvalidUserAccountException 
   * @author Gabriel Leonardo Diaz, 11.09.2013.
   */
  @Test
  public void testInsertDiagrammer () throws InvalidUserAccountException, SendEmailException  {
    userService.insertDiagrammer(new Diagrammer());
  }
  
}
