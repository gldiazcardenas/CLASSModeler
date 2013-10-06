/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service;

import javax.ejb.Local;

import classmodeler.domain.user.User;
import classmodeler.service.exception.InvalidDiagrammerAccountException;

/**
 * Service used to log in the system as a registered or invited user.
 * 
 * @author Gabriel Leonardo Diaz, 01.10.2013.
 */
@Local
public interface SessionService {
  
  /**
   * Service method that validates the user credentials and determines if the
   * user is able to get logged in the system.
   * 
   * @param email
   *          The email of the user.
   * @param password
   *          The password of the user account.
   * @return The user logged in.
   * @throws InvalidDiagrammerAccountException
   *           When the user account does not exist or is invalid.
   * @author Gabriel Leonardo Diaz, 01.10.2013.
   */
  User logIn (String email, String password) throws InvalidDiagrammerAccountException;
  
  /**
   * Service method that log out the user from the system, this method deletes
   * the user specific information and cleans up the data.
   * 
   * @param user
   *          The user that was logged in.
   * @author Gabriel Leonardo Diaz, 01.10.2013.
   */
  void logOut (User user);
  
}
