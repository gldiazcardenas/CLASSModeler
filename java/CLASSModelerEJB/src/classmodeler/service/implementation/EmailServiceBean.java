package classmodeler.service.implementation;

import java.io.IOException;
import java.util.Calendar;
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

import classmodeler.domain.user.Diagrammer;
import classmodeler.domain.verification.Verification;
import classmodeler.service.EmailService;
import classmodeler.service.exception.SendEmailException;
import classmodeler.service.util.GenericUtils;

public @Stateless class EmailServiceBean implements EmailService {
  
  @Override
  public void sendEmail(String subject, String htmlBody, String addresses) throws MessagingException, IOException {
    final Properties props = new Properties();
    props.load(GenericUtils.class.getResourceAsStream("smtp_email_config.properties"));
    
    // Get a Session object
    Session session = Session.getInstance(props, new Authenticator() {
      @Override protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(props.getProperty("mail.user"), props.getProperty("mail.password"));
      }
    });
    
    // Constructs the email
    Message msg = new MimeMessage(session);
    msg.setFrom(new InternetAddress(props.getProperty("mail.user")));
    msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(addresses, false));
    msg.setSubject(subject);
    msg.setContent(htmlBody, "text/html");
    msg.setSentDate(Calendar.getInstance().getTime());
    
    // Send the thing off
    Transport.send(msg);
  }
  
  @Override
  public void sendAccountActivationEmail(Diagrammer user, Verification verification) throws SendEmailException {
    try {
      // Constructs the HTML message
      String link           = getEmailVerificationURL("/pages/portal/activateAccount.xhtml?", user.getEmail(), verification.getCode());
      String title          = GenericUtils.getLocalizedMessage("EMAIL_SUBJECT_ACCOUNT_ACTIVATION_MESSAGE");
      String greetings      = GenericUtils.getLocalizedMessage("EMAIL_GREETINGS_MESSAGE", user.getName());
      StringBuilder message = new StringBuilder();
      
      message.append(GenericUtils.getLocalizedMessage("EMAIL_BODY_ACCOUNT_ACTIVATION_MESSAGE"));
      message.append(" <a href=\"").append(link).append("\" target=\"_blank\">").append(link).append("</a>");
      
      // Send the email
      sendEmail(GenericUtils.getLocalizedMessage("EMAIL_SUBJECT_TITLE", title), getEmailHTMLCode(title, greetings, message.toString()), user.getEmail());
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
  public void sendResetPasswordEmail(Diagrammer user, Verification verification) throws SendEmailException {
    try {
      // Constructs the HTML message
      String link           = getEmailVerificationURL("/pages/portal/resetPassword.xhtml?", user.getEmail(), verification.getCode());
      String title          = GenericUtils.getLocalizedMessage("EMAIL_SUBJECT_RESET_PASSWORD_MESSAGE");
      String greetings      = GenericUtils.getLocalizedMessage("EMAIL_GREETINGS_MESSAGE", user.getName());
      StringBuilder message = new StringBuilder();
      
      message.append(GenericUtils.getLocalizedMessage("EMAIL_BODY_RESET_PASSWORD_MESSAGE"));
      message.append(" <a href=\"").append(link).append("\" target=\"_blank\">").append(link).append("</a>");
      
      // Send the email.
      sendEmail(GenericUtils.getLocalizedMessage("EMAIL_SUBJECT_TITLE", title), getEmailHTMLCode(title, greetings, message.toString()), user.getEmail());
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
  
  /**
   * Builds the URL for the verification link sent in an e-mail to the user.
   * 
   * @param processorPath
   *          The file processor for the e-mail link.
   * @param email
   *          The e-mail address of the user.
   * @param code
   *          The verification code.
   * @return The link generated.
   * @author Gabriel Leonardo Diaz, 18.08.2013.
   */
  private static String getEmailVerificationURL (String processorPath, String email, String code) {
    return GenericUtils.getApplicationURL() + processorPath + "code=" + code + "&email=" + email;
  }
  
  /**
   * Processes the given parameters and creates the HTML body of the email to be
   * sent to the user(s).
   * 
   * @param title
   *          The title of the e-mail, this is a header of the message.
   * @param greetings
   *          Greetings for the users, can be null.
   * @param message
   *          The HTML message body of the e-mail.
   * @return The HTML code for the e-mail.
   * @author Gabriel Leonardo Diaz, 16.08.2013.
   */
  private static String getEmailHTMLCode (String title, String greetings, String message) {
    StringBuilder sb = new StringBuilder();
    sb.append("<html>");
    sb.append("<body style=\"font-size: 12px; font-style: normal; font-weight: normal; font-family: Arial, Helvetica, sans-serif; border: 0; margin: 0; padding: 0; background-color: #EDEDED;\">");
    sb.append("    <div style=\"margin: 20px auto; width: 650px; background-color: #FFFFFF; -webkit-box-shadow: 4px 0px 3px #CCCCCC, -4px 0px 3px #CCCCCC; -moz-box-shadow: 4px 0px 3px #CCCCCC, -4px 0px 3px #CCCCCC; box-shadow: 4px 0px 3px #CCCCCC, -4px 0px 3px #CCCCCC;\">");
    sb.append("        <div style=\"padding-top: 10px; padding-left: 20px; padding-right: 20px; \"><h2>").append(title).append("</h2></div>");
    sb.append("        <div style=\"padding-left: 20px; padding-right: 20px;\">");
    
    if (!GenericUtils.isEmptyString(greetings)) {
      sb.append("         <div>").append(greetings).append("</div>");
    }
    
    sb.append("           <p style=\"text-align: justify; margin: 30px 0px 80px 0px;\">").append(message).append("</p>");
    sb.append("       </div>");
    sb.append("       <div style=\"border-top: 3px solid #E1061A; background-color: #151618; color: #FFFFFF; text-align: center; padding: 10px 0px;\">");
    sb.append("           <div>").append(GenericUtils.getLocalizedMessage("FOOTER_EMAIL_COPYRIGHT_MESSAGE")).append("</div>");
    sb.append("           <div>").append(GenericUtils.getLocalizedMessage("APPLICATION_NAME")).append("</div>");
    sb.append("       </div>");
    sb.append("    </div>");
    sb.append("</body>");
    sb.append("</html>");
    return sb.toString();
  }

}
