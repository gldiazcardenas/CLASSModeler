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
import classmodeler.domain.user.EUserAccountStatus;
import classmodeler.domain.user.Guest;
import classmodeler.domain.user.IUser;
import classmodeler.domain.user.User;
import classmodeler.domain.verification.EVerificationType;
import classmodeler.domain.verification.Verification;
import classmodeler.service.UserService;
import classmodeler.service.VerificationService;
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
  private VerificationService vsb;
  
  @Override
  public boolean existsUser(String email) {
    return getUserByEmail(email) != null;
  }
  
  @Override
  public IUser logIn(String email, String password) throws InvalidUserAccountException {
    if (Guest.GUEST_EMAIL.equals(email) && Guest.GUEST_PASSWORD.equals(password)) {
      return new Guest();
    }
    
    User user = getUserByEmail(email);
    
    if (user == null || !user.getPassword().equals(password)) {
      throw new InvalidUserAccountException("The user account doesn't exist", EInvalidAccountErrorType.NON_EXISTING_ACCOUNT);
    }
    
    if (user.getAccountStatus() != EUserAccountStatus.ACTIVATED) {
      throw new InvalidUserAccountException("The user account is not activated.", EInvalidAccountErrorType.NON_ACTIVATED_ACCOUNT);
    }
    
    return user;
  }

  @Override
  public User activateUserAccount(String email, String verificationCode) throws InvalidUserAccountException,
                                                                                InvalidVerificationCodeException,
                                                                                ExpiredVerificationCodeException,
                                                                                SendEmailException {
    User user = getUserByEmail(email);
    
    if (user == null) {
      throw new InvalidUserAccountException("The user doesn't exist.", EInvalidAccountErrorType.NON_EXISTING_ACCOUNT);
    }
    
    if (user.getAccountStatus() == EUserAccountStatus.ACTIVATED) {
      throw new InvalidUserAccountException("The user account is already activated.", EInvalidAccountErrorType.ACTIVATED_ACCOUNT);
    }
    
    if (user.getAccountStatus() == EUserAccountStatus.DEACTIVATED) {
      throw new InvalidUserAccountException("The user account is deactivated.", EInvalidAccountErrorType.DEACTIVATED_ACCOUNT);
    }
    
    Verification verification = vsb.getVerificationCode(user, verificationCode, EVerificationType.ACTIVATE_ACCOUNT);
    
    if (verification == null) {
      throw new InvalidVerificationCodeException("The verification code was not found for the user.");
    }
    
    // The code won't be valid any more.
    verification.setValid(false);
    em.merge(verification);
    
    if (verification.getExpirationDate().before(Calendar.getInstance().getTime())) {
      // The verification code has expired, the system should generate a new one.
      verification = vsb.insertVerification(EVerificationType.ACTIVATE_ACCOUNT, user);
      
      // Sends the email with the new activation code.
      vsb.sendAccountActivationEmail(user, verification);
      
      // This doesn't trigger a RollBack operation.
      throw new ExpiredVerificationCodeException("The activation code has expired.");
    }
    
    // Activates the user account.
    user.setAccountStatus(EUserAccountStatus.ACTIVATED);
    em.merge(user);
    
    return user;
  }
  
  @Override
  public boolean isValidToResetPassword(String email, String code) throws InvalidUserAccountException,
                                                                          InvalidVerificationCodeException,
                                                                          ExpiredVerificationCodeException,
                                                                          SendEmailException {
    User user = getUserByEmail(email);
    
    if (user == null) {
      throw new InvalidUserAccountException("The user account doesn't exist", EInvalidAccountErrorType.NON_EXISTING_ACCOUNT);
    }
    
    if (user.getAccountStatus() == EUserAccountStatus.DEACTIVATED) {
      throw new InvalidUserAccountException("The use account is deactivated.", EInvalidAccountErrorType.DEACTIVATED_ACCOUNT);
    }
    
    // Gets the verification code.
    Verification verification = vsb.getVerificationCode(user, code, EVerificationType.PASSWORD_RESET);
    
    if (verification == null) {
      throw new InvalidVerificationCodeException("The verification code was not found for the user.");
    }
    
    // Verifies if the code has expired or was already used.
    if (verification.isValid() && verification.getExpirationDate().before(Calendar.getInstance().getTime())) {
      // Invalidates the current code.
      verification.setValid(false);
      em.merge(verification);
      
      // Generates a new code.
      verification = vsb.insertVerification(EVerificationType.PASSWORD_RESET, user);
      
      // Sends the email.
      vsb.sendResetPasswordEmail(user, verification);
      
      throw new ExpiredVerificationCodeException("The verification code has expired.");
    }
    
    return verification.isValid();
  }
  
  @Override
  public void sendResetPasswordEmail(String email) throws InvalidUserAccountException, SendEmailException {
    User user = getUserByEmail(email);
    
    if (user == null) {
      throw new InvalidUserAccountException("The user account doesn't exist.", EInvalidAccountErrorType.NON_EXISTING_ACCOUNT);
    }
    
    if (user.getAccountStatus() == EUserAccountStatus.DEACTIVATED) {
      throw new InvalidUserAccountException("The user account is deactivated", EInvalidAccountErrorType.DEACTIVATED_ACCOUNT);
    }
    
    // Creates the verification code.
    Verification verification = vsb.insertVerification(EVerificationType.PASSWORD_RESET, user);
    
    // Sends the link to reset the password.
    vsb.sendResetPasswordEmail(user, verification);
  }
  
  @Override
  public User resetPassword(String email, String newPassword) throws InvalidUserAccountException {
    User user = getUserByEmail(email);
    
    if (user == null) {
      throw new InvalidUserAccountException("The user account doesn't exist.", EInvalidAccountErrorType.NON_EXISTING_ACCOUNT);
    }
    
    if (user.getAccountStatus() == EUserAccountStatus.DEACTIVATED) {
      throw new InvalidUserAccountException("The user account is deactivated.", EInvalidAccountErrorType.DEACTIVATED_ACCOUNT);
    }
    
    user.setPassword(newPassword);
    em.merge(user);
    
    return user;
  }

  @Override
  public User insertUser(User user) throws InvalidUserAccountException, SendEmailException {
    if (existsUser(user.getEmail())) {
      throw new InvalidUserAccountException("The user email already exists.", EInvalidAccountErrorType.DUPLICATED_ACCOUNT);
    }
    
    // Inserts the user
    user.setAccountStatus(EUserAccountStatus.INACTIVATED);
    user.setCreatedDate(Calendar.getInstance().getTime());
    em.persist(user);
    
    // Inserts the verification
    Verification verification = vsb.insertVerification(EVerificationType.ACTIVATE_ACCOUNT, user);
    
    // Sends the activation email
    vsb.sendAccountActivationEmail(user, verification);
    
    return user;
  }

  @Override
  public User updateUser(User user) {
    User existingUser = em.find(User.class, Integer.valueOf(user.getKey()));
    if (existingUser != null) {
      em.merge(user);
    }
    return user;
  }

  @Override
  public void deleteUser(int userKey) {
    User user = em.find(User.class, Integer.valueOf(userKey));
    if (user != null) {
      em.remove(user);
    }
  }

  @Override
  public User getUserByKey(int userKey) {
    return em.find(User.class, Integer.valueOf(userKey));
  }
  
  @Override
  public User getUserByEmail(String email) {
    User user = null;
    if (!GenericUtils.isEmptyString(email)) {
      TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE LOWER(u.email) = :userEmail", User.class);
      query.setParameter("userEmail", email.toLowerCase());
      
      List<User> userList = query.getResultList();
      
      if (!CollectionUtils.isEmptyCollection(userList)) {
        user = userList.get(0);
      }
    }
    
    return user;
  }
  
  @Override
  @SuppressWarnings("unchecked")
  public List<User> getUsersToShareDiagram(Diagram diagram) {
    List<User> users = new ArrayList<User>();
    
    if (diagram != null) {
      users = em.createNativeQuery("SELECT * FROM user WHERE user_key <> ?ownerKey AND user_key NOT IN (SELECT shared_to_user FROM shared WHERE shared_diagram_key = ?diagramKey)", User.class)
                .setParameter("ownerKey", Integer.valueOf(diagram.getCreatedBy().getKey()))
                .setParameter("diagramKey", Integer.valueOf(diagram.getKey()))
                .getResultList();
    }
    
    return users;
  }

}
