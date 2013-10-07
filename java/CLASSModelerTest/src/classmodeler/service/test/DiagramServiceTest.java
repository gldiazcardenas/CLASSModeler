/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.test;

import javax.naming.NamingException;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import classmodeler.domain.diagram.Diagram;
import classmodeler.domain.user.Diagrammer;
import classmodeler.domain.user.EGender;
import classmodeler.service.DiagramService;
import classmodeler.service.UserService;
import classmodeler.service.bean.InsertDiagrammerResult;
import classmodeler.service.exception.ExpiredSecurityCodeException;
import classmodeler.service.exception.InvalidDiagrammerAccountException;
import classmodeler.service.exception.InvalidSecurityCodeException;
import classmodeler.service.exception.SendEmailException;
import classmodeler.service.exception.UnprivilegedException;
import classmodeler.service.implementation.DiagramServiceBean;
import classmodeler.service.implementation.UserServiceBean;

/**
 * Unit test for the service {@link DiagramService}.
 * 
 * @author Gabriel Leonardo Diaz, 06.10.2013.
 */
@Test
public class DiagramServiceTest extends ServiceTest {
  
  private UserService userService;
  private DiagramService diagramService;
  private Diagrammer diagrammerBasic;

  @Override
  @BeforeClass
  public void configure() throws NamingException {
    userService    = (UserService) context.lookup(getServiceObjectName(UserServiceBean.class.getSimpleName()));
    diagramService = (DiagramService) context.lookup(getServiceObjectName(DiagramServiceBean.class.getSimpleName()));
    
    diagrammerBasic = new Diagrammer();
    diagrammerBasic.setFirstName("Basic Diagrammer");
    diagrammerBasic.setLastName("Basic Diagrammer");
    diagrammerBasic.setEmail("email.basic@gmail.com");
    diagrammerBasic.setPassword("12345");
    diagrammerBasic.setGender(EGender.MALE);
    
    try {
      InsertDiagrammerResult result = userService.insertDiagrammer(diagrammerBasic);
      diagrammerBasic               = result.getDiagrammer();
      
      userService.activateDiagrammerAccount(result.getDiagrammer().getEmail(), result.getSecurityCode().getCode());
    }
    catch (InvalidDiagrammerAccountException e) {
      // Do nothing
    }
    catch (SendEmailException e) {
      // Do nothing
    }
    catch (InvalidSecurityCodeException e) {
      // Do nothing
    }
    catch (ExpiredSecurityCodeException e) {
      // Do nothing
    }
  }

  @Override
  @AfterClass
  public void destroy() {
    userService.deleteDiagrammer(diagrammerBasic.getKey());
  }
  
  /**
   * Unit test for checking the correct operation of the service method
   * {@link DiagramService#insertDiagram(classmodeler.domain.diagram.Diagram)}.
   * 
   * @author Gabriel Leonardo Diaz, 06.10.2013.
   * @throws InvalidDiagrammerAccountException 
   * @throws UnprivilegedException 
   */
  public void testInsertDiagram () throws InvalidDiagrammerAccountException, UnprivilegedException {
    Diagram diagram = new Diagram();
    diagram.setName("DiagramTest");
    diagram.setDescription("DiagramDescription");
    diagram.setCreatedBy(diagrammerBasic);
    
    try {
      diagram = diagramService.insertDiagram(diagram);
    }
    finally {
      diagramService.deleteDiagram(diagram.getKey());
    }
  }
  
  /**
   * Unit test for checking an alternate flow of the service method
   * {@link DiagramService#insertDiagram(classmodeler.domain.diagram.Diagram)}.
   * 
   * @author Gabriel Leonardo Diaz, 06.10.2013.
   * @throws InvalidDiagrammerAccountException 
   */
  @Test (expectedExceptions = InvalidDiagrammerAccountException.class)
  public void testInsertDiagrammer_failNonExistingAccount () throws InvalidDiagrammerAccountException {
    Diagram diagram = new Diagram();
    diagram.setName("DiagramTest");
    diagram.setDescription("DiagramDescription");
    diagram.setCreatedBy(new Diagrammer());
    diagram = diagramService.insertDiagram(diagram);
  }
  
}
