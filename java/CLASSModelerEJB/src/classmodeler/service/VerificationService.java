/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service;

import java.util.Date;

import javax.ejb.Local;

import classmodeler.domain.email.EVerificationType;
import classmodeler.domain.email.Verification;
import classmodeler.domain.user.User;
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
  public Verification insertVerification(EVerificationType type, User user);
  
  /**
   * Sends the activation account email to the address provided by the user.
   * 
   * @param user
   *          The user which the email will be sent to.
   * @param verification
   *          The verification object created for the email.
   * @throws SendEmailException
   *           When the system is not able to perform the operation.
   * @author Gabriel Leonardo Diaz, 18.05.2013.
   */
  public void sendActivationEmail(User user, Verification verification) throws SendEmailException;
  
  /**
   * Creates a hash code using MD5 encrypt algorithm, this uses the email and
   * the current date to build an special string that is parsed in MD5.
   * 
   * For example:
   * - Email: gabriel.leonardo.diaz@gmail.com
   * - Key  : 25
   * 
   * The result string is: "gabriel.leonardo.diaz@gmail.com" + "+%+" + Calendar.getInstance().getTimeInMillis().
   * Later this is parsed to MD5 and converted to the specific format.
   * 
   * @param email
   *          The email of the user.
   * @return The MD5 code representation.
   * @author Gabriel Leonardo Diaz, 16.05.2013.
   */
  public String getHashCodeMD5 (String email);
  
  /**
   * Generates the expiration date of the verification code through the current
   * time. Basically this captures the current time and adds 2 days to it.
   * 
   * @return The expiration date generated.
   * @author Gabriel Leonardo Diaz, 16.05.2013.
   */
  public Date getExpirationDate ();
  
}
