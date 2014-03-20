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

import classmodeler.domain.security.ESecurityCodeType;
import classmodeler.domain.security.SecurityCode;
import classmodeler.domain.user.Diagrammer;
import classmodeler.domain.user.EGender;
import classmodeler.service.UserService;
import classmodeler.service.SecurityService;
import classmodeler.service.bean.InsertDiagrammerResult;
import classmodeler.service.exception.InvalidDiagrammerAccountException;
import classmodeler.service.exception.SendEmailException;
import classmodeler.service.implementation.UserServiceBean;
import classmodeler.service.implementation.SecurityServiceBean;

/**
 * Unit tests for the service {@link SecurityService}.
 * 
 * @author Gabriel Leonardo Diaz, 26.09.2013.
 */
@Test
public class SecurityServiceTest extends ServiceTest {
  
  private SecurityService securityService;
  private UserService userService;
  private Diagrammer diagrammerBasic;
  private SecurityCode securityCodeBasic;

  @Override
  @BeforeClass
  public void configure() throws NamingException {
    securityService = (SecurityService) context.lookup(getServiceObjectName(SecurityServiceBean.class.getSimpleName()));
    userService = (UserService) context.lookup(getServiceObjectName(UserServiceBean.class.getSimpleName()));
    
    diagrammerBasic = new Diagrammer();
    diagrammerBasic.setFirstName("Basic Diagrammer");
    diagrammerBasic.setLastName("Basic Diagrammer");
    diagrammerBasic.setEmail("email.basic@gmail.com");
    diagrammerBasic.setPassword("12345");
    diagrammerBasic.setGender(EGender.MALE);
    
    try {
      InsertDiagrammerResult result = userService.insertDiagrammer(diagrammerBasic);
      diagrammerBasic               = result.getDiagrammer();
      securityCodeBasic             = result.getSecurityCode();
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
   * Unit test to verify the normal flow of the service
   * {@link SecurityService#insertSecurityCode(classmodeler.domain.security.ESecurityCodeType, classmodeler.domain.user.Diagrammer)}
   * .
   * 
   * @author Gabriel Leonardo Diaz, 26.09.2013
   */
  public void testInsertSecurityCode () {
    SecurityCode verification = securityService.insertSecurityCode(ESecurityCodeType.ACTIVATE_ACCOUNT, diagrammerBasic);
    assert (verification != null) : "Error: The verification was not inserted.";
  }
  
  /**
   * Unit test to verify the normal flow of the service
   * {@link SecurityService#getSecurityCode(classmodeler.domain.user.Diagrammer, String, classmodeler.domain.security.ESecurityCodeType)}
   * .
   * 
   * @author Gabriel Leonardo Diaz, 26.09.2013
   */
  public void testGetSecurityCode () {
    SecurityCode verification = securityService.getSecurityCode(diagrammerBasic, securityCodeBasic.getCode(), securityCodeBasic.getType());
    assert (verification != null) : "Error: The verification was not found.";
  }

}
