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
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import classmodeler.service.EmailService;
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

}
