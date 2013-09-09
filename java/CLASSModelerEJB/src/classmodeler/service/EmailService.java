/****************************************************

  Universidad Francisco de Paula Santander UFPS
  Cúcuta, Colombia
  (c) 2013 by UFPS. All rights reserved.

 ****************************************************/

package classmodeler.service;

import java.io.IOException;

import javax.ejb.Local;
import javax.mail.MessagingException;

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

}
