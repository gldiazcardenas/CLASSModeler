/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/
package classmodeler.service;

import javax.ejb.Local;

import classmodeler.domain.user.Guest;
import classmodeler.domain.user.IUser;
import classmodeler.domain.user.User;
import classmodeler.service.exception.ExpiredVerificationCodeException;
import classmodeler.service.exception.InvalidUserAccountException;
import classmodeler.service.exception.InvalidVerificationCodeException;
import classmodeler.service.exception.SendEmailException;

/**
 * Service definition that contains all the operations to handle users in the
 * application.
 * 
 * @author Gabriel Leonardo Diaz, 02.03.2013.
 */
@Local
public interface UserService {
  
  /**
   * Service method that checks if the given user email already exists.
   * 
   * @param email
   *          The email of the user to check.
   * @return A <code>boolean</code> value indicating if the user email exists or
   *         not.
   * @author Gabriel Leonardo Diaz, 02.03.2013.
   */
  public boolean existsUser (String email);
  
  /**
   * Logs in the user represented by the given credentials in the system. This
   * also allows to log in as a invited user ({@link Guest}), in that case the
   * nickname should be equal to {@link Guest#GUEST_EMAIL} and the password
   * equal to {@link Guest#GUEST_PASSWORD}.
   * 
   * @param email
   *          The user email.
   * @param password
   *          The use password.
   * @throws InvalidUserAccountException
   *           When the user is found but the account has not been activated.
   * @return A user bean or null if no one user is found.
   */
  public IUser logIn (String email, String password) throws InvalidUserAccountException;
  
  /**
   * Sends the link to reset the password of the user account represented by the
   * given email.
   * 
   * @param email
   *          The user email.
   * @throws InvalidUserAccountException
   *           When the user account for the given email is invalid. Doesn't
   *           exist or is deactivated.
   * @throws SendEmailException
   *           When the system is not able to send an email to the user address.
   * @author Gabriel Leonardo Diaz, 21.05.2013.
   */
  public void sendResetPasswordEmail (String email) throws InvalidUserAccountException, SendEmailException;
  
  /**
   * Validates the given email and hash code used to reset the user account
   * password.
   * 
   * @param email
   *          The user email.
   * @param code
   *          The hash code sent to the email address.
   * @return A boolean indicating whether the hash code is valid or not.
   * @throws InvalidUserAccountException
   *           When the user account doesn't exist or it's deactivated.
   * @throws InvalidVerificationCodeException
   *           When the system doesn't found a verification code that matches
   *           with the given one.
   * @throws ExpiredVerificationCodeException
   *           When the verification code has expired, in this case the system
   *           generates a new one and sends it to the user email address.
   * @throws SendEmailException
   *           When the system is not able to re-send the verification code.
   * @author Gabriel Leonardo Diaz, 24.05.2013.
   */
  public boolean isValidToResetPassword (String email, String code) throws InvalidUserAccountException,
                                                                           InvalidVerificationCodeException,
                                                                           ExpiredVerificationCodeException,
                                                                           SendEmailException;
  
  /**
   * Activates the user account of the given user.
   * 
   * @param email
   *          The email of user to activate its account.
   * @return The user after setting the account status.
   * @throws InvalidUserAccountException
   *           When the user account for the given email doesn't exist, was
   *           already ACTIVATED or was DEACTIVATED by the user.
   * @throws InvalidVerificationCodeException
   *           When the system doesn't found a verification code that matches
   *           with the given one.
   * @throws ExpiredVerificationCodeException
   *           When the verification code has expired, in this case the system
   *           generates a new one and sends it to the user email address.
   * @throws SendEmailException
   *           The system is not able to re-send the activation code when the
   *           current has expired.
   * @author Gabriel Leonardo Diaz, 14.03.2013
   */
  public User activateUserAccount (String email, String verificationCode) throws InvalidUserAccountException,
                                                                                 InvalidVerificationCodeException,
                                                                                 ExpiredVerificationCodeException,
                                                                                 SendEmailException;
  
  /**
   * Resets the user password with a new one.
   * 
   * @param email
   *          The user email.
   * @param newPassword
   *          The new password for the user account.
   * @return The user updated.
   * @throws InvalidUserAccountException
   *           When the user email doesn't exist or it is deactivated.
   * @author Gabriel Leonardo Diaz, 25.05.2013.
   */
  public User resetPassword (String email, String newPassword) throws InvalidUserAccountException;
  
  /**
   * Inserts the given user into the database. This method also creates the user
   * account verification code and sends it to the user email address.
   * 
   * @param user
   *          The new user to save.
   * @return The user bean after inserting in the database.
   * @throws InvalidUserAccountException
   *           When the user that is being inserted has an already existing
   *           email in database.
   * @throws SendEmailException
   *           When the system is not able to send the activation email.
   * @author Gabriel Leonardo Diaz, 14.03.2013
   */
  public User insertUser (User user) throws InvalidUserAccountException, SendEmailException;
  
  /**
   * Updates the fields of the given user into the database.
   * 
   * @param user
   *          The user to update its information.
   * @return The user bean after setting the fields.
   * @author Gabriel Leonardo Diaz, 14.03.2013
   */
  public User updateUser (User user);
  
  /**
   * Deletes the information of the given user key from database.
   * 
   * @param userKey
   *          The key of the user to delete.
   * @author Gabriel Leonardo Diaz, 14.03.2013
   */
  public void deleteUser (int userKey);
  
  /**
   * Gets the user bean by its key.
   * 
   * @param userKey
   *          The key of the user to get.
   * @return A user bean.
   * @author Gabriel Leonardo Diaz, 14.03.2013
   */
  public User getUserByKey (int userKey);
  
  /**
   * Gets the user who owns the given email.
   * 
   * @param email
   *          The email of the user.
   * @return The user that has the email associated.
   * @author Gabriel Leonardo Diaz, 18.05.2013.
   */
  public User getUserByEmail (String email);
  
  
}
