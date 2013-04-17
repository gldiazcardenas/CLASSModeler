/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.exception;

/**
 * Exception class that is thrown when the user tries to log in the system but
 * he/she has not activated its account.
 * 
 * @author Gabriel Leonardo Diaz, 14.04.2013.
 */
public class InactivatedUserAccountException extends ServiceException {

  private static final long serialVersionUID = 1L;
  
  public InactivatedUserAccountException() {
    super();
  }
  
  public InactivatedUserAccountException (String message) {
    super(message);
  }
  
  public InactivatedUserAccountException (String message, Throwable cause) {
    super (message, cause);
  }
  
}
