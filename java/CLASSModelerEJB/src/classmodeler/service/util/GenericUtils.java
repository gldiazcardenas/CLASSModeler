/****************************************************

  Universidad Francisco de Paula Santander UFPS
  C�cuta, Colombia
  (c) 2013 by UFPS. All rights reserved.

 ****************************************************/

package classmodeler.service.util;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.codec.digest.DigestUtils;

import classmodeler.domain.email.EVerificationType;
import classmodeler.domain.user.User;

/**
 * Utility class that contains generic method to handle common operations like
 * validations, conversions and so on.
 * 
 * @author Gabriel Leonardo Diaz, 07.01.2013.
 */
public final class GenericUtils {
  
  private GenericUtils() {
    // This class CANNOT BE INSTANCED.
  }
  
  /**
   * Generic method that checks if the given <code>String</code> parameter is
   * empty or not. It is empty either it's null or its empty method returns
   * true (after calling {@link String#trim()} method).
   * 
   * @param string
   *          The string to check.
   * @return A boolean value indicating if the string is empty or not.
   * @author Gabriel Leonardo Diaz, 07.01.2013.
   */
  public static boolean isEmptyString (String string) {
    return string == null || string.trim().isEmpty();
  }
  
  /**
   * Generic method to compare to objects by their equals method. This checks if
   * the objects are null, in that case they will be equals only if both are
   * null.
   * 
   * @param obj1
   *          The first object to compare.
   * @param obj2
   *          The second object to compare.
   * @return A <code>boolean</code> indicating if both objects are equals, if
   *         both objects are null this method will return <code>true</code>.
   * @author Gabriel Leonardo Diaz, 02.03.2013.
   */
  public static boolean equals (Object obj1, Object obj2) {
    if (obj1 == null) {
      return obj2 == null;
    }
    
    if (obj2 == null) {
      return false;
    }
    
    return obj1.equals(obj2);
  }
  
  /**
   * Compares two <code>String</code> values and determines if they are equals,
   * this method calls to {@link String#trim()} before calling
   * {@link String#equals(Object)}.
   * 
   * @param str1
   *          The first string to compare.
   * @param str2
   *          The second string to compare.
   * @return A <code>boolean</code> indicating if the strings are equals, if
   *         both objects are null this method will return <code>true</code>.
   * @author Gabriel Leonardo Diaz, 02.03.2013.
   */
  public static boolean equals (String str1, String str2) {
    if (str1 == null) {
      return str2 == null;
    }
    
    if (str2 == null) {
      return false;
    }
    
    return str1.trim().equals(str2.trim());
  }
  
  /**
   * Basic method that checks an email address and determines whether it is
   * valid or not. This validation is made based on a regular expression, it is
   * really simple and allows to know the validity of an email without complex
   * calculations.
   * 
   * @param email
   *          The email address to check.
   * @return A <code>boolean</code> value with the result of the validation.
   * @author Gabriel Leonardo Diaz, 10.05.2013.
   */
  public static boolean isValidEmail (String email) {
    if (isEmptyString(email)) {
      return false;
    }
    
    Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
    Matcher m = p.matcher(email);
    return m.matches();
  }
  
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
   * @param userEmail
   *          The email of the user.
   * @return The MD5 code representation.
   * @author Gabriel Leonardo Diaz, 16.05.2013.
   */
  public static String getHashCodeMD5(String userEmail) {
    return DigestUtils.md5Hex(userEmail + "+%+" + Calendar.getInstance().getTimeInMillis());
  }
  
  /**
   * Generates the expiration date of the verification code through the current
   * time. Basically this captures the current time and adds 2 days.
   * 
   * @return The expiration date generated.
   * @author Gabriel Leonardo Diaz, 16.05.2013.
   */
  public static Date generateExpirationDate () {
    Date expirationDate = Calendar.getInstance().getTime();
    expirationDate.setTime(expirationDate.getTime() + 172800000L); // Adds 2 days
    return expirationDate;
  }
  
  /**
   * Sends the activation code to the user email address.
   * 
   * @param user
   *          The user email address.
   * @param hashCode
   *          The hash code generated to confirm the user account.
   * @throws AddressException 
   * @throws SendFailedException 
   * @throws MessagingException 
   * @throws IOException 
   * @author Gabriel Leonardo Diaz, 16.05.2013.
   */
  public static void sendActivationAccountEmail(User user, String hashCode) throws AddressException,
                                                                                   SendFailedException,
                                                                                   MessagingException,
                                                                                   IOException {
    // Properties
    final Properties props = new Properties();
    props.load(GenericUtils.class.getResourceAsStream("smtp_email_config.properties"));
    
    // Get a Session object
    Session session = Session.getInstance(props, new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(props.getProperty("mail.user"), props.getProperty("mail.password"));
      }
    });
    session.setDebug(true);
    
    // Constructs the HTML message
    StringBuilder msgHTML = new StringBuilder();
    msgHTML.append("<html>");
    msgHTML.append("<p>Hola <b>").append(user.getName()).append("</b>,");
    msgHTML.append("<br/><br/>");
    msgHTML.append("<div>Tu cuenta ha sido creada, por favor pulsa en el siguiente link para hacer la activaci�n: ");
    msgHTML.append("<a href='").append(props.getProperty("classmodeler.verification.address"))
           .append("?code=").append(hashCode)
           .append("&address=").append(user.getEmail())
           .append("&type=").append(EVerificationType.ACTIVATE_ACCOUNT)
           .append("'>Activar Cuenta</a>");
    msgHTML.append("</div>");
    msgHTML.append("</p>");
    msgHTML.append("</html>");
    
    // Constructs the email
    Message msg = new MimeMessage(session);
    msg.setFrom(new InternetAddress(props.getProperty("mail.user")));
    msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail(), false));
    msg.setSubject("CLASSModeler - Activaci�n de Cuenta");
    msg.setHeader("X-Mailer", "msgsend");
    msg.setContent(msgHTML.toString(), "text/html");
    msg.setSentDate(Calendar.getInstance().getTime());
    
    // Send the thing off
    Transport.send(msg);
  }
}
