/****************************************************

  Universidad Francisco de Paula Santander UFPS
  C�cuta, Colombia
  (c) 2013 by UFPS. All rights reserved.

 ****************************************************/

package classmodeler.domain.user;

/**
 * This represents the possible values for user account status.
 * 
 * @author Gabriel Leonardo Diaz, 14.03.2013.
 * @version 1.0
 * @updated 24.03.2013 04:59:23 p.m.
 */
public enum EUserAccountStatus {

  /**
   * The user created his/her account but hasn't activated it.
   */
  INACTIVED,
  /**
   * The user created his/her account and activated it.
   */
  ACTIVATED,
  /**
   * The user had an account but decided to deactivate it by himself/herself
   */
  DEACTIVATED

}
