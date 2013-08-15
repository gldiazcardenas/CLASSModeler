/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.implementation;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;

import classmodeler.domain.user.User;
import classmodeler.domain.verification.EVerificationType;
import classmodeler.domain.verification.Verification;
import classmodeler.service.VerificationService;
import classmodeler.service.exception.SendEmailException;
import classmodeler.service.util.CollectionUtils;
import classmodeler.service.util.GenericUtils;

/**
 * Session bean implementation of verification service.
 *
 * @author Gabriel Leonardo Diaz, 18.05.2013.
 */
@Stateless
public class VerificationServiceBean implements VerificationService {

  @PersistenceContext(unitName="CLASSModelerPU")
  private EntityManager em;
  
  @Override
  public Verification insertVerification(EVerificationType type, User user) {
    Verification verification = new Verification();
    verification.setUser(user);
    verification.setType(type);
    verification.setExpirationDate(getExpirationDate());
    verification.setCode(getHashCodeMD5(user.getEmail()));
    verification.setValid(true);
    
    em.persist(verification);
    
    return verification;
  }

  @Override
  public void sendActivationEmail(User user, Verification verification) throws SendEmailException {
    try {
      // Properties
      final Properties props = new Properties();
      props.load(GenericUtils.class.getResourceAsStream("smtp_email_config.properties"));
      
      // Get a Session object
      Session session = Session.getInstance(props, new Authenticator() {
        @Override protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(props.getProperty("mail.user"), props.getProperty("mail.password"));
        }
      });
      
      // Constructs the HTML message
      StringBuilder link = new StringBuilder(getApplicationURL()).append("/pages/portal/activateAccount.xhtml?").append("code=").append(verification.getCode()).append("&email=").append(user.getEmail());
      
      StringBuilder msgHTML = new StringBuilder();
      msgHTML.append("<html>")
             .append("<p>Hola <b>").append(user.getName()).append("</b>,")
             .append("<br/><br/>")
             .append("<div>").append(getLocalizedMessage("EMAIL_BODY_ACCOUNT_ACTIVATION_MESSAGE"))
             .append("<a href='").append(link.toString()).append("' target='_blank'>").append(link.toString()).append("</a>")
             .append("</div>")
             .append("</p>")
             .append("</html>");
      
      // Constructs the email
      Message msg = new MimeMessage(session);
      msg.setFrom(new InternetAddress(props.getProperty("mail.user")));
      msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail(), false));
      msg.setSubject(getLocalizedMessage("EMAIL_SUBJECT_ACCOUNT_ACTIVATION_MESSAGE"));
      msg.setContent(msgHTML.toString(), "text/html");
      msg.setSentDate(Calendar.getInstance().getTime());
      
      // Send the thing off
      Transport.send(msg);
    }
    catch (IOException e) {
      throw new SendEmailException(e.getMessage(), e);
    }
    catch (AddressException e) {
      throw new SendEmailException(e.getMessage(), e);
    }
    catch (MessagingException e) {
      throw new SendEmailException(e.getMessage(), e);
    }
  }
  
  @Override
  public void sendResetPasswordEmail(User user, Verification verification) throws SendEmailException {
    try {
      // Properties
      final Properties props = new Properties();
      props.load(GenericUtils.class.getResourceAsStream("smtp_email_config.properties"));
      
      // Get a Session object
      Session session = Session.getInstance(props, new Authenticator() {
        @Override protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(props.getProperty("mail.user"), props.getProperty("mail.password"));
        }
      });
      
      // Constructs the HTML message
      StringBuilder link = new StringBuilder(getApplicationURL()).append("/pages/portal/resetPassword.xhtml?").append("code=").append(verification.getCode()).append("&email=").append(user.getEmail());
      
      StringBuilder msgHTML = new StringBuilder();
      msgHTML.append("<html>")
             .append("<p>Hola <b>").append(user.getName()).append("</b>,")
             .append("<br/><br/>")
             .append("<div>").append(getLocalizedMessage("EMAIL_BODY_RESET_PASSWORD_MESSAGE"))
             .append("<a href='").append(link.toString()).append("' target='_blank'>").append(link.toString()).append("</a>")
             .append("</div>")
             .append("</p>")
             .append("</html>");
      
      // Constructs the email
      Message msg = new MimeMessage(session);
      msg.setFrom(new InternetAddress(props.getProperty("mail.user")));
      msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail(), false));
      msg.setSubject(getLocalizedMessage("EMAIL_SUBJECT_RESET_PASSWORD_MESSAGE"));
      msg.setContent(msgHTML.toString(), "text/html");
      msg.setSentDate(Calendar.getInstance().getTime());
      
      // Send the thing off
      Transport.send(msg);
    }
    catch (IOException e) {
      throw new SendEmailException(e.getMessage(), e);
    }
    catch (AddressException e) {
      throw new SendEmailException(e.getMessage(), e);
    }
    catch (MessagingException e) {
      throw new SendEmailException(e.getMessage(), e);
    }
  }
  
  @Override
  public Verification getVerificationCode(User user, String code, EVerificationType type) {
    TypedQuery<Verification> query = em.createQuery("SELECT v FROM Verification v WHERE v.user = :user AND v.type = :verificationType AND v.code = :code", Verification.class);
    query.setParameter("user", user);
    query.setParameter("code", code);
    query.setParameter("verificationType", type);
    
    List<Verification> list = query.getResultList();
    
    if (CollectionUtils.isEmptyCollection(list)) {
      return null;
    }
    
    return list.get(0);
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
   * @param email
   *          The email of the user.
   * @return The MD5 code representation.
   * @author Gabriel Leonardo Diaz, 16.05.2013.
   */
  private static String getHashCodeMD5(String email) {
    return DigestUtils.md5Hex(email + "+%+" + Calendar.getInstance().getTimeInMillis());
  }

  /**
   * Generates the expiration date of the verification code through the current
   * time. Basically this captures the current time and adds 2 days to it.
   * 
   * @return The expiration date generated.
   * @author Gabriel Leonardo Diaz, 16.05.2013.
   */
  private static Date getExpirationDate() {
    Date expirationDate = Calendar.getInstance().getTime();
    expirationDate.setTime(expirationDate.getTime() + 172800000L); // Adds 2 days
    return expirationDate;
  }
  
  /**
   * Looks for a localized message through the given <code>messageKey</code> in
   * the message resource bundle.
   * 
   * @param arguments
   *          The values of the message, where the first element is the
   *          messageKey and the other elements are the arguments of the
   *          message..
   * @return The localized message represented by the given key.
   */
  private static String getLocalizedMessage (String... values) {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msgs");
    
    String messageKey = values[0];
    Object [] arguments = null;
    
    if (CollectionUtils.size(values) > 1) {
      arguments = new Object[values.length - 1];
      for (int i = 0; i < arguments.length; i++) {
        arguments[i] = values[i + 1];
      }
    }
    
    return MessageFormat.format(bundle.getString(messageKey), arguments);
  }
  
  /**
   * Provides the URL to the application until the application name.
   * 
   * @return The URL to the application used to send emails.
   * @author Gabriel Leonardo Diaz, 14.08.2013.
   */
  private static String getApplicationURL () {
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
  }
  
}
