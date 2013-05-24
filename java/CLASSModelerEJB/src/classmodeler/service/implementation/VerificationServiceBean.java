/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.implementation;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.ejb.Stateless;
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

import org.apache.commons.codec.digest.DigestUtils;

import classmodeler.domain.user.User;
import classmodeler.domain.verification.EVerificationType;
import classmodeler.domain.verification.Verification;
import classmodeler.service.VerificationService;
import classmodeler.service.exception.SendEmailException;
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
      session.setDebug(true);
      
      // Constructs the HTML message
      String subject = "CLASSModeler - Activación de Cuenta";
      StringBuilder link = new StringBuilder(props.getProperty("classmodeler.verify.code.address"))
         .append("?code=").append(verification.getCode())
         .append("&email=").append(user.getEmail())
         .append("&action=").append(EVerificationType.ACTIVATE_ACCOUNT);
      
      StringBuilder msgHTML = new StringBuilder();
      msgHTML.append("<html>");
      msgHTML.append("<p>Hola <b>").append(user.getName()).append("</b>,");
      msgHTML.append("<br/><br/>");
      msgHTML.append("<div>Tu cuenta ha sido creada satisfactoriamente. El codigo de verificación tiene una vigencia ");
      msgHTML.append("de dos(2) dias, para activar su cuenta ahora por favor pulse sobre el siguiente link: ");
      msgHTML.append("<a href='").append(link.toString()).append("' target='_blank'>").append(link.toString()).append("</a>");
      msgHTML.append("</div>");
      msgHTML.append("</p>");
      msgHTML.append("</html>");
      
      // Constructs the email
      Message msg = new MimeMessage(session);
      msg.setFrom(new InternetAddress(props.getProperty("mail.user")));
      msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail(), false));
      msg.setSubject(subject);
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
      session.setDebug(true);
      
      // Constructs the HTML message
      String subject = "CLASSModeler - Restauración de Contraseña";
      StringBuilder link = new StringBuilder(props.getProperty("classmodeler.reset.address"))
         .append("?code=").append(verification.getCode())
         .append("&email=").append(user.getEmail())
         .append("&action=").append(EVerificationType.PASSWORD_RESET);
      
      StringBuilder msgHTML = new StringBuilder();
      msgHTML.append("<html>");
      msgHTML.append("<p>Hola <b>").append(user.getName()).append("</b>,");
      msgHTML.append("<br/><br/>");
      msgHTML.append("<div>Has solicitado restaurar tu contraseña. El codigo de verificación tiene una vigencia ");
      msgHTML.append("de dos(2) dias, para restaurar tu contraseña ahora por favor pulsa el siguiente link: ");
      msgHTML.append("<a href='").append(link.toString()).append("' target='_blank'>").append(link.toString()).append("</a>");
      msgHTML.append("</div>");
      msgHTML.append("</p>");
      msgHTML.append("</html>");
      
      // Constructs the email
      Message msg = new MimeMessage(session);
      msg.setFrom(new InternetAddress(props.getProperty("mail.user")));
      msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail(), false));
      msg.setSubject(subject);
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
  public String getHashCodeMD5(String email) {
    return DigestUtils.md5Hex(email + "+%+" + Calendar.getInstance().getTimeInMillis());
  }

  @Override
  public Date getExpirationDate() {
    Date expirationDate = Calendar.getInstance().getTime();
    expirationDate.setTime(expirationDate.getTime() + 172800000L); // Adds 2 days
    return expirationDate;
  }
  
}
