/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.exception;

/**
 * Thrown when the user account is invalid to be activated or doesn't exist.
 * 
 * @author Gabriel Leonardo Diaz, 20.05.2013.
 */
public class InvalidUserAccountException extends ServiceException {

  private static final long serialVersionUID = 1L;
  
  public InvalidUserAccountException () {
    super();
  }
  
  public InvalidUserAccountException (String message) {
    super(message);
  }
  
  public InvalidUserAccountException (String message, Throwable cause) {
    super (message, cause);
  }
  
}
