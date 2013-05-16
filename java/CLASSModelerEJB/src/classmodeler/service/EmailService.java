/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service;

import classmodeler.domain.email.EmailVerification;
import classmodeler.domain.user.User;

/**
 * Service used to send e-mails from the system to the user account.
 * 
 * @author Gabriel Leonardo Diaz, 15.05.2013.
 */
public interface EmailService {
  
  /**
   * Generates the hash code for the email verification of the new user account,
   * also sends the confirmation to the email address provided by the user.
   * 
   * @param newUser
   *          The new user created.
   * @return The instance of email verification created in this method.
   * @author Gabriel Leonardo Diaz, 15.05.2013.
   */
  public EmailVerification sendEmailVerification (User newUser);
  
}
