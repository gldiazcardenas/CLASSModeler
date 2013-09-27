/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.implementation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import classmodeler.domain.diagram.Diagram;
import classmodeler.domain.user.Diagrammer;
import classmodeler.domain.user.EDiagrammerAccountStatus;
import classmodeler.domain.user.Guest;
import classmodeler.domain.user.User;
import classmodeler.domain.verification.EVerificationType;
import classmodeler.domain.verification.Verification;
import classmodeler.service.EmailService;
import classmodeler.service.UserService;
import classmodeler.service.VerificationService;
import classmodeler.service.beans.InsertDiagrammerResult;
import classmodeler.service.exception.ExpiredVerificationCodeException;
import classmodeler.service.exception.InvalidUserAccountException;
import classmodeler.service.exception.InvalidUserAccountException.EInvalidAccountErrorType;
import classmodeler.service.exception.InvalidVerificationCodeException;
import classmodeler.service.exception.SendEmailException;
import classmodeler.service.util.CollectionUtils;
import classmodeler.service.util.GenericUtils;

/**
 * Class that implements the operations (CRUD) to handle users.
 * 
 * @author Gabriel Leonardo Diaz, 02.03.2013.
 */
public @Stateless class UserServiceBean implements UserService {
  
  @PersistenceContext(unitName="CLASSModelerPU")
  private EntityManager em;
  
  @EJB
  private VerificationService verificationService;
  
  @EJB
  private EmailService emailService;
  
  @Override
  public boolean existsUser(String email) {
    return getDiagrammerByEmail(email) != null;
  }
  
  @Override
  public User logIn(String email, String password) throws InvalidUserAccountException {
    if (Guest.GUEST_EMAIL.equals(email) && Guest.GUEST_PASSWORD.equals(password)) {
      return new Guest();
    }
    
    Diagrammer user = getDiagrammerByEmail(email);
    
    if (user == null || !user.getPassword().equals(password)) {
      throw new InvalidUserAccountException("The user account doesn't exist", EInvalidAccountErrorType.NON_EXISTING_ACCOUNT);
    }
    
    if (user.getAccountStatus() != EDiagrammerAccountStatus.ACTIVATED) {
      throw new InvalidUserAccountException("The user account is not activated.", EInvalidAccountErrorType.NON_ACTIVATED_ACCOUNT);
    }
    
    return user;
  }

  @Override
  public Diagrammer activateDiagrammerAccount(String email, String verificationCode) throws InvalidUserAccountException,
                                                                                            InvalidVerificationCodeException,
                                                                                            ExpiredVerificationCodeException,
                                                                                            SendEmailException {
    Diagrammer user = getDiagrammerByEmail(email);
    
    if (user == null) {
      throw new InvalidUserAccountException("The user doesn't exist.", EInvalidAccountErrorType.NON_EXISTING_ACCOUNT);
    }
    
    if (user.getAccountStatus() == EDiagrammerAccountStatus.ACTIVATED) {
      throw new InvalidUserAccountException("The user account is already activated.", EInvalidAccountErrorType.ACTIVATED_ACCOUNT);
    }
    
    if (user.getAccountStatus() == EDiagrammerAccountStatus.DEACTIVATED) {
      throw new InvalidUserAccountException("The user account is deactivated.", EInvalidAccountErrorType.DEACTIVATED_ACCOUNT);
    }
    
    Verification verification = verificationService.getVerificationCode(user, verificationCode, EVerificationType.ACTIVATE_ACCOUNT);
    
    if (verification == null) {
      throw new InvalidVerificationCodeException("The verification code was not found for the user.");
    }
    
    // The code won't be valid any more.
    verification.setValid(false);
    em.merge(verification);
    
    if (verification.getExpirationDate().before(Calendar.getInstance().getTime())) {
      // The verification code has expired, the system should generate a new one.
      verification = verificationService.insertVerification(EVerificationType.ACTIVATE_ACCOUNT, user);
      
      // Sends the email with the new activation code.
      emailService.sendAccountActivationEmail(user, verification);
      
      // This doesn't trigger a RollBack operation.
      throw new ExpiredVerificationCodeException("The activation code has expired.");
    }
    
    // Activates the user account.
    user.setAccountStatus(EDiagrammerAccountStatus.ACTIVATED);
    em.merge(user);
    
    return user;
  }
  
  @Override
  public boolean isValidToResetPassword(String email, String code) throws InvalidUserAccountException,
                                                                          InvalidVerificationCodeException,
                                                                          ExpiredVerificationCodeException,
                                                                          SendEmailException {
    Diagrammer user = getDiagrammerByEmail(email);
    
    if (user == null) {
      throw new InvalidUserAccountException("The user account doesn't exist", EInvalidAccountErrorType.NON_EXISTING_ACCOUNT);
    }
    
    if (user.getAccountStatus() == EDiagrammerAccountStatus.DEACTIVATED) {
      throw new InvalidUserAccountException("The use account is deactivated.", EInvalidAccountErrorType.DEACTIVATED_ACCOUNT);
    }
    
    // Gets the verification code.
    Verification verification = verificationService.getVerificationCode(user, code, EVerificationType.PASSWORD_RESET);
    
    if (verification == null) {
      throw new InvalidVerificationCodeException("The verification code was not found for the user.");
    }
    
    // Verifies if the code has expired or was already used.
    if (verification.isValid() && verification.getExpirationDate().before(Calendar.getInstance().getTime())) {
      // Invalidates the current code.
      verification.setValid(false);
      em.merge(verification);
      
      // Generates a new code.
      verification = verificationService.insertVerification(EVerificationType.PASSWORD_RESET, user);
      
      // Sends the email.
      emailService.sendResetPasswordEmail(user, verification);
      
      throw new ExpiredVerificationCodeException("The verification code has expired.");
    }
    
    return verification.isValid();
  }
  
  @Override
  public void requestResetPassword (String email) throws InvalidUserAccountException, SendEmailException {
    Diagrammer user = getDiagrammerByEmail(email);
    
    if (user == null) {
      throw new InvalidUserAccountException("The user account doesn't exist.", EInvalidAccountErrorType.NON_EXISTING_ACCOUNT);
    }
    
    if (user.getAccountStatus() == EDiagrammerAccountStatus.DEACTIVATED) {
      throw new InvalidUserAccountException("The user account is deactivated", EInvalidAccountErrorType.DEACTIVATED_ACCOUNT);
    }
    
    // Creates the verification code.
    Verification verification = verificationService.insertVerification(EVerificationType.PASSWORD_RESET, user);
    
    // Sends the link to reset the password.
    emailService.sendResetPasswordEmail(user, verification);
  }
  
  @Override
  public Diagrammer resetPassword(String email, String newPassword) throws InvalidUserAccountException {
    Diagrammer user = getDiagrammerByEmail(email);
    
    if (user == null) {
      throw new InvalidUserAccountException("The user account doesn't exist.", EInvalidAccountErrorType.NON_EXISTING_ACCOUNT);
    }
    
    if (user.getAccountStatus() == EDiagrammerAccountStatus.DEACTIVATED) {
      throw new InvalidUserAccountException("The user account is deactivated.", EInvalidAccountErrorType.DEACTIVATED_ACCOUNT);
    }
    
    user.setPassword(newPassword);
    em.merge(user);
    
    return user;
  }

  @Override
  public InsertDiagrammerResult insertDiagrammer(Diagrammer diagrammer) throws InvalidUserAccountException, SendEmailException {
    if (existsUser(diagrammer.getEmail())) {
      throw new InvalidUserAccountException("The user email already exists.", EInvalidAccountErrorType.DUPLICATED_ACCOUNT);
    }
    
    // Inserts the user
    diagrammer.setAccountStatus(EDiagrammerAccountStatus.INACTIVATED);
    diagrammer.setCreatedDate(Calendar.getInstance().getTime());
    em.persist(diagrammer);
    
    // Inserts the verification
    Verification verification = verificationService.insertVerification(EVerificationType.ACTIVATE_ACCOUNT, diagrammer);
    
    // Sends the activation email
    emailService.sendAccountActivationEmail(diagrammer, verification);
    
    return new InsertDiagrammerResult(diagrammer, verification);
  }

  @Override
  public Diagrammer updateDiagrammer(Diagrammer user) {
    Diagrammer existingUser = em.find(Diagrammer.class, Integer.valueOf(user.getKey()));
    if (existingUser != null) {
      em.merge(user);
    }
    return user;
  }

  @Override
  public void deleteDiagrammer(int userKey) {
    Diagrammer user = em.find(Diagrammer.class, Integer.valueOf(userKey));
    if (user != null) {
      em.remove(user);
    }
  }

  @Override
  public Diagrammer getDiagrammerByKey(int userKey) {
    return em.find(Diagrammer.class, Integer.valueOf(userKey));
  }
  
  @Override
  public Diagrammer getDiagrammerByEmail(String email) {
    Diagrammer user = null;
    if (!GenericUtils.isEmptyString(email)) {
      TypedQuery<Diagrammer> query = em.createQuery("SELECT d FROM Diagrammer d WHERE LOWER(d.email) = :diagrammerEmail", Diagrammer.class);
      query.setParameter("diagrammerEmail", email.toLowerCase());
      
      List<Diagrammer> userList = query.getResultList();
      
      if (!CollectionUtils.isEmptyCollection(userList)) {
        user = userList.get(0);
      }
    }
    
    return user;
  }
  
  @Override
  @SuppressWarnings("unchecked")
  public List<Diagrammer> getDiagrammersAllowedToShareDiagram(Diagram diagram) {
    List<Diagrammer> users = new ArrayList<Diagrammer>();
    
    if (diagram != null) {
      users = em.createNativeQuery("SELECT * FROM diagrammer WHERE diagrammer_key <> ?ownerKey AND diagrammer_key NOT IN (SELECT shared_to_diagrammer FROM shared WHERE shared_diagram_key = ?diagramKey)", Diagrammer.class)
                .setParameter("ownerKey", Integer.valueOf(diagram.getCreatedBy().getKey()))
                .setParameter("diagramKey", Integer.valueOf(diagram.getKey()))
                .getResultList();
    }
    
    return users;
  }

}
