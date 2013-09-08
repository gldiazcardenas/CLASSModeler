/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.user;

/**
 * A Guest is a person who doesn't have an user account but is able to use the
 * system trying a Demo.
 * 
 * @author Gabriel Leonardo Diaz, 07.04.2013.
 */
public class Guest implements User {

  private static final long serialVersionUID = 1L;
  
  public static final String GUEST_NAME      = "GUEST_NAME";
  public static final String GUEST_PASSWORD  = "GUEST_PASSWORD";
  public static final String GUEST_EMAIL     = "GUEST_NICK_NAME";
  public static final String GUEST_AVATAR    = "GUEST_AVATAR";

  @Override
  public String getName() {
    return GUEST_NAME;
  }

  @Override
  public String getPassword() {
    return GUEST_PASSWORD;
  }

  @Override
  public String getEmail() {
    return GUEST_EMAIL;
  }

  @Override
  public String getAvatar() {
    return GUEST_AVATAR;
  }
  
  @Override
  public boolean isRegisteredUser() {
    return false;
  }
}
