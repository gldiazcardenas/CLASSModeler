/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/
package classmodeler.service;

import javax.ejb.Local;

import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion.User;

/**
 * Service definition that contains all the operations to handle users in the
 * application.
 * 
 * @author Gabriel Leonardo Diaz, 02.03.2013.
 */
@Local
public interface UserServiceBean {
  
  /**
   * Service method that checks if the given user email already exists.
   * 
   * @param email
   *          The user email to check.
   * @return A <code>boolean</code> value indicating if the user email exists or
   *         not.
   * @author Gabriel Leonardo Diaz, 02.03.2013.
   */
  public boolean existsUser (String email);
  
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
   * @author Gabriel Leonardo Diaz, 14.03.2013
   */
  public User insertUser (User user);
  
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
