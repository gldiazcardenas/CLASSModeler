/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service;

import javax.ejb.Local;

import classmodeler.domain.user.Diagrammer;
import classmodeler.domain.verification.EVerificationType;
import classmodeler.domain.verification.Verification;
import classmodeler.service.exception.SendEmailException;

/**
 * Session bean that defines basic methods to handle email verifications for the
 * users.
 * 
 * @author Gabriel Leonardo Diaz, 18.05.2013.
 */
@Local
public interface VerificationService {
  
  /**
   * Creates a new verification object for the user with the given type.
   * 
   * @param type
   *          The type of the new verification.
   * @param user
   *          The user that verification is created for.
   * @author Gabriel Leonardo Diaz, 18.05.2013.
   */
  public Verification insertVerification(EVerificationType type, Diagrammer user);
  
  /**
   * Sends the activation account email to the address provided by the user.
   * 
   * @param user
   *          The user owner of the email address.
   * @param verification
   *          The verification object created for the email.
   * @throws SendEmailException
   *           When the system is not able to perform the operation.
   * @author Gabriel Leonardo Diaz, 18.05.2013.
   */
  public void sendAccountActivationEmail(Diagrammer user, Verification verification) throws SendEmailException;
  
  /**
   * Sends the reset password email to the address provided by the user.
   * 
   * @param user
   *          The user owner of the email address.
   * @param verification
   *          The verification object created for the email.
   * @throws SendEmailException
   *           When the system is not able to perform the operation.
   * @author Gabriel Leonardo Diaz, 21.05.2013.
   */
  public void sendResetPasswordEmail (Diagrammer user, Verification verification) throws SendEmailException;
  
  /**
   * Gets a verification code with the provided information.
   * 
   * @param user
   *          The user owner of the verification code.
   * @param code
   *          The hash code MD5 representation.
   * @param type
   *          The verification type.
   * @return An instance of verification or null is nothing is found.
   * @author Gabriel Leonardo Diaz, 25.05.2013.
   */
  public Verification getVerificationCode (Diagrammer user, String code, EVerificationType type);
  
}
