/****************************************************

  Universidad Francisco de Paula Santander UFPS
  Cúcuta, Colombia
  (c) 2013 by UFPS. All rights reserved.

 ****************************************************/

package classmodeler.service;

import java.io.IOException;

import javax.ejb.Local;
import javax.mail.MessagingException;

import classmodeler.domain.security.SecurityCode;
import classmodeler.domain.user.Diagrammer;
import classmodeler.service.exception.SendEmailException;

/**
 * Definition of the service used to send e-mails.
 * 
 * @author Gabriel Leonardo Diaz, 09.09.2013.
 */
@Local
public interface EmailService {
  
  /**
   * Constructs a HTML email with the given information and sends it to the
   * receivers.
   * 
   * @param subject
   *          The subject of the email
   * @param htmlBody
   *          The body of the email in HTML format.
   * @param addresses
   *          The mail addresses that will receive the e-mail. This is a
   *          comma-separated String.
   * @throws MessagingException
   *           When the email cannot be send because of connection problems.
   * @throws IOException
   *           When the SMTP mail configuration properties file cannot be loaded
   *           correctly.
   * @author Gabriel Leonardo Diaz, 09.09.2013.
   */
  void sendEmail (String subject, String htmlBody, String addresses) throws MessagingException, IOException;
  
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
  public void sendAccountActivationEmail(Diagrammer user, SecurityCode verification) throws SendEmailException;
  
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
  public void sendResetPasswordEmail (Diagrammer user, SecurityCode verification) throws SendEmailException;

}
