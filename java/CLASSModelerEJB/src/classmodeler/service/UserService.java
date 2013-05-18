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
import classmodeler.service.exception.DuplicatedUserEmailException;
import classmodeler.service.exception.InactivatedUserAccountException;

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
   * @param nickname
   *          The nickname of the user to check.
   * @return A <code>boolean</code> value indicating if the user email exists or
   *         not.
   * @author Gabriel Leonardo Diaz, 02.03.2013.
   */
  public boolean existsUser (String nickname);
  
  /**
   * Logs in the user represented by the given credentials in the system. This
   * also allows to log in as a invited user ({@link Guest}), in that case the
   * nickname should be equal to {@link Guest#GUEST_NICK_NAME} and the password
   * equal to {@link Guest#GUEST_PASSWORD}.
   * 
   * @param nickname
   *          The user nickname.
   * @param password
   *          The use password.
   * @throws InactivatedUserAccountException
   *           When the user is found but the account has not been activated.
   * @return A user bean or null if no one user is found.
   */
  public IUser logIn (String nickname, String password) throws InactivatedUserAccountException;
  
  /**
   * Activates the user account of the given user.
   * 
   * @param user
   *          The user to activate its account.
   * @return The user after setting the account status.
   * @author Gabriel Leonardo Diaz, 14.03.2013
   */
  public User activateUserAccount (User user);
  
  /**
   * Inserts the given user into the database.
   * 
   * @param user
   *          The new user to save.
   * @return The user bean after inserting in the database.
   * @throws DuplicatedUserEmailException
   *           When the user email already exists in database.
   * @throws Exception
   *           For any problem inserting the user or sending the activation
   *           email.
   * @author Gabriel Leonardo Diaz, 14.03.2013
   */
  public User insertUser (User user) throws Exception;
  
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
  
  
}
