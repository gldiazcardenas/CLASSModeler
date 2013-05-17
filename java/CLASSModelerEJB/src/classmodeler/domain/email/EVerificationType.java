/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.email;

/**
 * The different kinds of email verification made by the system.
 * 
 * @author Gabriel Leonardo Diaz, 16.05.2013.
 */
public enum EVerificationType {
  
  /**
   * When the user is signed up, the system sends an email to the confirm the
   * account.
   */
  ACCOUNT_ACTIVATION,
  
  /**
   * Used to send a confirmation after the user forgot the password and wants to
   * recover it.
   */
  PASSWORD_RESET
  
}
