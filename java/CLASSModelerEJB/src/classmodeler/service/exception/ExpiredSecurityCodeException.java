/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.exception;

import javax.ejb.ApplicationException;

/**
 * Thrown when the verification code has expired.
 * 
 * @author Gabriel Leonardo Diaz, 25.05.2013.
 */
@ApplicationException (rollback = false)
public class ExpiredSecurityCodeException extends ServiceException {

  private static final long serialVersionUID = 1L;
  
  public ExpiredSecurityCodeException () {
    super();
  }
  
  public ExpiredSecurityCodeException (String message) {
    super(message);
  }
  
  public ExpiredSecurityCodeException (String message, Throwable cause) {
    super (message, cause);
  }

}
