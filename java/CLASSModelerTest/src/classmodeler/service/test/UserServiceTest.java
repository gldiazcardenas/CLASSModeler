package classmodeler.service.test;

import javax.naming.NamingException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import classmodeler.domain.user.Diagrammer;
import classmodeler.service.UserService;
import classmodeler.service.exception.InvalidUserAccountException;
import classmodeler.service.exception.SendEmailException;

@Test
public class UserServiceTest extends ServiceTest {
  
  private UserService userService;
  
  @Override
  @BeforeClass
  public void setUp() throws NamingException {
    super.setUp();
    userService = (UserService) ejbContainer.getContext().lookup("java:global/CLASSModeler/CLASSModelerEJB/UserServiceBean");
  }
  
  public void testInsertDiagrammer () {
    try {
      userService.insertDiagrammer(new Diagrammer());
    }
    catch (InvalidUserAccountException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (SendEmailException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
}
