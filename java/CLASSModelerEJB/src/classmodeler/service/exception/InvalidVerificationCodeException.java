/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.exception;

/**
 * Thrown when the verification code is invalid for the user.
 * 
 * @author Gabriel Leonardo Diaz, 23.05.2013.
 */
public class InvalidVerificationCodeException extends ServiceException {

  private static final long serialVersionUID = 1L;
  
  public InvalidVerificationCodeException () {
    super();
  }
  
  public InvalidVerificationCodeException (String message) {
    super(message);
  }
  
  public InvalidVerificationCodeException (String message, Throwable cause) {
    super (message, cause);
  }

}
