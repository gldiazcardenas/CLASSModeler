/****************************************************

  Universidad Francisco de Paula Santander UFPS
  Cúcuta, Colombia
  (c) 2013 by UFPS. All rights reserved.

 ****************************************************/

package classmodeler.domain.user;

/**
 * This represents the possible values for user account status.
 * 
 * @author Gabriel Leonardo Diaz, 14.03.2013.
 */
public enum EUserAccountStatus {
  
  /*
   * GD, 16.03.2013. WARNING, Don't change the order of the enumeration
   * literals because it affects the ordinal value and it is used to store the
   * object in the database.
   */
  
  /**
   * The user created his/her account but hasn't activated it.
   */
  INACTIVED,
  
  /**
   * The user created his/her account and activated it.
   */
  ACTIVATED,
  
  /**
   * The user had an account but decided to deactivate it by himself/herself.
   */
  DEACTIVATED

}
