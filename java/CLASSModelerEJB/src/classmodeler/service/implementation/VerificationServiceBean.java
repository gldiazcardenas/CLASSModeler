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
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.codec.digest.DigestUtils;

import classmodeler.domain.user.Diagrammer;
import classmodeler.domain.verification.EVerificationType;
import classmodeler.domain.verification.Verification;
import classmodeler.service.EmailService;
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
  
  @EJB
  private EmailService emailService;
  
  @Override
  public Verification insertVerification(EVerificationType type, Diagrammer user) {
    Verification verification = new Verification();
    verification.setDiagrammer(user);
    verification.setType(type);
    verification.setExpirationDate(getExpirationDate());
    verification.setCode(getHashCodeMD5(user.getEmail()));
    verification.setValid(true);
    em.persist(verification);
    return verification;
  }
  
  @Override
  public Verification getVerificationCode(Diagrammer user, String code, EVerificationType type) {
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
      emailService.sendEmail(GenericUtils.getLocalizedMessage("EMAIL_SUBJECT_TITLE", title), getEmailHTMLCode(title, greetings, message.toString()), user.getEmail());
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
      emailService.sendEmail(GenericUtils.getLocalizedMessage("EMAIL_SUBJECT_TITLE", title), getEmailHTMLCode(title, greetings, message.toString()), user.getEmail());
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
   * Creates a hash code using MD5 encrypt algorithm, this uses the email and
   * the current date to build an special string that is parsed in MD5.
   * 
   * For example:
   * - Email: gabriel.leonardo.diaz@gmail.com
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
