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

import classmodeler.domain.user.Diagrammer;
import classmodeler.domain.user.EGender;
import classmodeler.domain.verification.EVerificationType;
import classmodeler.domain.verification.Verification;
import classmodeler.service.UserService;
import classmodeler.service.VerificationService;
import classmodeler.service.beans.InsertDiagrammerResult;
import classmodeler.service.exception.InvalidUserAccountException;
import classmodeler.service.exception.SendEmailException;
import classmodeler.service.implementation.UserServiceBean;
import classmodeler.service.implementation.VerificationServiceBean;

/**
 * Unit tests for the service {@link VerificationService}.
 * 
 * @author Gabriel Leonardo Diaz, 26.09.2013.
 */
@Test
public class VerificationServiceTest extends ServiceTest {
  
  private VerificationService verificationService;
  private UserService userService;
  private Diagrammer diagrammerBasic;
  private Verification verificationBasic;

  @Override
  @BeforeClass
  public void configure() throws NamingException {
    verificationService = (VerificationService) context.lookup(getServiceObjectName(VerificationServiceBean.class.getSimpleName()));
    userService = (UserService) context.lookup(getServiceObjectName(UserServiceBean.class.getSimpleName()));
    
    diagrammerBasic = new Diagrammer();
    diagrammerBasic.setFirstName("Basic Diagrammer");
    diagrammerBasic.setLastName("Basic Diagrammer");
    diagrammerBasic.setEmail("email.basic@gmail.com");
    diagrammerBasic.setPassword("12345");
    diagrammerBasic.setGender(EGender.MALE);
    
    try {
      InsertDiagrammerResult result = userService.insertDiagrammer(diagrammerBasic);
      diagrammerBasic = result.getDiagrammer();
      verificationBasic = result.getVerification();
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
  }
  
  /**
   * Unit test to verify the normal flow of the service
   * {@link VerificationService#insertVerification(classmodeler.domain.verification.EVerificationType, classmodeler.domain.user.Diagrammer)}
   * .
   * 
   * @author Gabriel Leonardo Diaz, 26.09.2013
   */
  public void testInsertVerification () {
    Verification verification = verificationService.insertVerification(EVerificationType.ACTIVATE_ACCOUNT, diagrammerBasic);
    assert (verification != null) : "Error: The verification was not inserted.";
  }
  
  /**
   * Unit test to verify the normal flow of the service
   * {@link VerificationService#getVerificationCode(classmodeler.domain.user.Diagrammer, String, classmodeler.domain.verification.EVerificationType)}
   * .
   * 
   * @author Gabriel Leonardo Diaz, 26.09.2013
   */
  public void testGetVerificationCode () {
    Verification verification = verificationService.getVerificationCode(diagrammerBasic, verificationBasic.getCode(), verificationBasic.getType());
    assert (verification != null) : "Error: The verification was not found.";
  }

}
