/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.user;

import java.io.Serializable;

/**
 * Defines the common behavior of an user that interacts with the system.
 * 
 * @author Gabriel Leonardo Diaz, 07.04.2013.
 */
public interface IUser extends Serializable {
  
  /**
   * Gets the full name of the user that is using the system.
   * 
   * @return The full name.
   */
  public String getName();
  
  /**
   * Gets the password used to login in the system.
   * 
   * @return The user password.
   */
  public String getPassword();
  
  /**
   * Gets the nickname of the user used to login in the system.
   * 
   * @return The nickname of the user.
   */
  public String getNickname();
  
  /**
   * Gets the URL of the avatar of the user.
   * 
   * @return The avatar of the user.
   */
  public String getAvatar();
  
  /**
   * The query determines if the user is registered in the system, or he is a
   * guest.
   * 
   * @return A boolean value depending on the user implementation.
   */
  public boolean isRegisteredUser();
}
