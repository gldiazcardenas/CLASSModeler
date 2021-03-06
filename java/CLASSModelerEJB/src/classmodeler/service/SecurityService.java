/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * C�cuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service;

import java.util.Date;

import javax.ejb.Local;

import classmodeler.domain.security.ESecurityCodeType;
import classmodeler.domain.security.SecurityCode;
import classmodeler.domain.user.Diagrammer;

/**
 * Session bean that defines basic methods to handle email verifications for the
 * users.
 * 
 * @author Gabriel Leonardo Diaz, 18.05.2013.
 */
@Local
public interface SecurityService {
  
  /**
   * Creates a new verification object for the user with the given type.
   * 
   * @param type
   *          The type of the new verification.
   * @param diagrammer
   *          The diagrammer that verification is created for.
   * @author Gabriel Leonardo Diaz, 18.05.2013.
   */
  public SecurityCode insertSecurityCode(ESecurityCodeType type, Diagrammer diagrammer);
  
  /**
   * Gets a verification code with the provided information.
   * 
   * @param diagrammer
   *          The diagrammer owner of the verification code.
   * @param code
   *          The hash code MD5 representation.
   * @param type
   *          The verification type.
   * @return An instance of verification or null is nothing is found.
   * @author Gabriel Leonardo Diaz, 25.05.2013.
   */
  public SecurityCode getSecurityCode (Diagrammer diagrammer, String code, ESecurityCodeType type);
  
  /**
   * Creates a hash code using MD5 encrypt algorithm, this uses the email and
   * the current date to build an special string that is parsed in MD5.
   * 
   * For example:
   * - Email: gabriel.leonardo.diaz@gmail.com
   * 
   * The result string is: "gabriel.leonardo.diaz@gmail.com" + "+%+" + Calendar.getInstance().getTimeInMillis().
   * Later this is parsed to MD5 and converted to the specific format.
   * 
   * @param email
   *          The email of the user.
   * @return The MD5 code representation.
   * @author Gabriel Leonardo Diaz, 16.05.2013.
   */
  public String generateHashCodeMD5(String email);
  
  /**
   * Generates the expiration date of the verification code through the current
   * time. Basically this captures the current time and adds 2 days to it.
   * 
   * @return The expiration date generated.
   * @author Gabriel Leonardo Diaz, 16.05.2013.
   */
  public Date generateExpirationDate();
  
}
