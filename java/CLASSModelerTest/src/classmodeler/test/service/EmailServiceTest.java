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

import classmodeler.service.EmailService;

/**
 * Unit tests for the service {@link EmailService}.
 * 
 * @author Gabriel Leonardo Diaz, 26.09.2013.
 */
@Test
public class EmailServiceTest extends ServiceTest {

  @Override
  @BeforeClass
  public void configure() throws NamingException {
    // TODO implement
  }

  @Override
  @AfterClass
  public void destroy() {
    // TODO implement
  }
  
  /**
   * Unit test to verify the normal flow of the service
   * {@link EmailService#sendEmail(String, String, String)}.
   * 
   * @author Gabriel Leonardo Diaz, 26.09.2013
   */
  public void testSendEmail () {
    // TODO
  }
  
  /**
   * Unit test to verify the normal flow of the service
   * {@link EmailService#sendAccountActivationEmail(classmodeler.domain.user.Diagrammer, classmodeler.domain.verification.Verification)}
   * .
   * 
   * @author Gabriel Leonardo Diaz, 26.09.2013
   */
  public void testSendActivationAccountEmail () {
    // TODO
  }
  
  /**
   * Unit test to verify the normal flow of the service
   * {@link EmailService#sendResetPasswordEmail(classmodeler.domain.user.Diagrammer, classmodeler.domain.verification.Verification)}
   * .
   * 
   * @author Gabriel Leonardo Diaz, 26.09.2013
   */
  public void testSendResetPasswordEmail () {
    // TODO
  }

}
