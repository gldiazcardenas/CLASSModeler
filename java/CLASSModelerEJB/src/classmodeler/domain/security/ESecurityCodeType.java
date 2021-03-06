/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * C�cuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.security;

/**
 * The different kinds of security code generated by the system.
 * 
 * @author Gabriel Leonardo Diaz, 16.05.2013.
 */
public enum ESecurityCodeType {
  
  /*
   * IMPORTANT: Please don't change the order of the enumeration literals, this
   * order is important because the ordinal of each literal is stored in
   * database.
   */
  
  /**
   * When the user is signed up, the system sends an email to confirm the
   * account.
   */
  ACTIVATE_ACCOUNT,
  
  /**
   * Used to send a confirmation after the user forgot the password and wants to
   * recover it.
   */
  PASSWORD_RESET
  
}
