/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.implementation;

import java.io.IOException;

import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import classmodeler.domain.email.EVerificationType;
import classmodeler.domain.email.Verification;
import classmodeler.domain.user.EUserAccountStatus;
import classmodeler.domain.user.Guest;
import classmodeler.domain.user.IUser;
import classmodeler.domain.user.User;
import classmodeler.service.UserService;
import classmodeler.service.exception.InactivatedUserAccountException;
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
  
  @Override
  public boolean existsUser(String nickname) {
    if (GenericUtils.isEmptyString(nickname)) {
      return false;
    }
    
    Query query = em.createQuery("SELECT u FROM User u WHERE LOWER(u.email) = :userEmail");
    query.setParameter("userEmail", nickname.toLowerCase());
    
    return !CollectionUtils.isEmptyCollection(query.getResultList());
  }
  
  @Override
  public IUser logIn(String nickname, String password) throws InactivatedUserAccountException {
    if (Guest.GUEST_NICK_NAME.equals(nickname) && Guest.GUEST_PASSWORD.equals(password)) {
      return new Guest();
    }
    
    // TODO Auto-generated method stub
    throw new InactivatedUserAccountException();
  }

  @Override
  public User activateUserAccount(User user) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public User insertUser(User user) {
    
    try {
      // Inserts the user
      user.setAccountStatus(EUserAccountStatus.INACTIVATED);
      em.persist(user);
      
      // Inserts the email verification
      Verification verificationCode = new Verification();
      verificationCode.setUser(user);
      verificationCode.setType(EVerificationType.ACCOUNT_ACTIVATION);
      verificationCode.setExpirationDate(GenericUtils.generateExpirationDate());
      verificationCode.setCode(GenericUtils.getHashCodeMD5(user.getEmail()));
      em.persist(verificationCode);
      
      // Sends the email to the user address
      GenericUtils.sendActivationAccountEmail(user.getEmail(), verificationCode.getCode());
      
      // Commits the changes.
      em.flush();
    }
    catch (AddressException e) {
      // TODO Auto-generated catch block
    }
    catch (MessagingException e) {
      // TODO Auto-generated catch block
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
    }
    
    return user;
  }

  @Override
  public User updateUser(User user) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void deleteUser(int userKey) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public User getUserByKey(int userKey) {
    // TODO Auto-generated method stub
    return null;
  }

}
