/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.implementation;

import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import classmodeler.domain.user.EUserAccountStatus;
import classmodeler.domain.user.Guest;
import classmodeler.domain.user.IUser;
import classmodeler.domain.user.User;
import classmodeler.domain.verification.EVerificationType;
import classmodeler.domain.verification.Verification;
import classmodeler.service.UserService;
import classmodeler.service.VerificationService;
import classmodeler.service.exception.ExistingUserEmailException;
import classmodeler.service.exception.ExpiredVerificationCodeException;
import classmodeler.service.exception.InactivatedUserAccountException;
import classmodeler.service.exception.InvalidUserAccountException;
import classmodeler.service.exception.SendEmailException;
import classmodeler.service.exception.ServiceException;
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
  public IUser logIn(String email, String password) throws InactivatedUserAccountException {
    if (Guest.GUEST_EMAIL.equals(email) && Guest.GUEST_PASSWORD.equals(password)) {
      return new Guest();
    }
    
    // TODO Auto-generated method stub
    throw new InactivatedUserAccountException();
  }

  @Override
  public User activateUserAccount(String email, String verificationCode) throws ServiceException {
    User user = getUserByEmail(email);
    
    if (user == null || user.getAccountStatus() != EUserAccountStatus.INACTIVATED) {
      throw new InvalidUserAccountException("The user account is invalid.");
    }
    
    em.refresh(user); // Reloads the changes
    
    TypedQuery<Verification> query = em.createQuery("SELECT v FROM Verification v WHERE v.user = :user AND v.type = :verificationType AND v.code = :code AND v.expirationDate >= :currentTime", Verification.class);
    query.setParameter("user", user);
    query.setParameter("code", verificationCode);
    query.setParameter("verificationType", EVerificationType.ACTIVATE_ACCOUNT);
    query.setParameter("currentTime", Calendar.getInstance().getTime());
    
    if (CollectionUtils.isEmptyCollection(query.getResultList())) {
      // The verification code has expired, the system should generate a new one.
      Verification verification = vsb.insertVerification(EVerificationType.ACTIVATE_ACCOUNT, user);
      
      // Sends the email with the new activation code.
      vsb.sendActivationEmail(user, verification);
      
      throw new ExpiredVerificationCodeException("The activation code has expired."); // This doesn't trigger a RollBack operation.
    }
    
    user.setAccountStatus(EUserAccountStatus.ACTIVATED);
    em.merge(user);
    
    return user;
  }

  @Override
  public User insertUser(User user) throws ExistingUserEmailException, SendEmailException {
    if (existsUser(user.getEmail())) {
      throw new ExistingUserEmailException("The user email already exists.", user.getEmail());
    }
    
    // Inserts the user
    user.setAccountStatus(EUserAccountStatus.INACTIVATED);
    user.setCreatedDate(Calendar.getInstance().getTime());
    em.persist(user);
    
    // Inserts the verification
    Verification verification = vsb.insertVerification(EVerificationType.ACTIVATE_ACCOUNT, user);
    
    // Sends the activation email
    vsb.sendActivationEmail(user, verification);
    
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

}
