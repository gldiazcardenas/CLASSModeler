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
 * Exception thrown when the user tries to activate its account but the
 * activation code has expired.
 * 
 * @author Gabriel Leonardo Diaz, 20.05.2013.
 */
@ApplicationException (rollback = false)
public class ExpiredVerificationCodeException extends ServiceException {

  private static final long serialVersionUID = 1L;
  
  public ExpiredVerificationCodeException () {
    super();
  }
  
  public ExpiredVerificationCodeException (String message) {
    super(message);
  }
  
  public ExpiredVerificationCodeException (String message, Throwable cause) {
    super(message, cause);
  }

}
