/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.exception;

/**
 * Thrown when the user account doesn't exist or is invalid by any reason.
 * 
 * @author Gabriel Leonardo Diaz, 20.05.2013.
 */
public class InvalidUserAccountException extends ServiceException {

  private static final long serialVersionUID = 1L;
  
  private EInvalidAccountErrorType type;
  
  public InvalidUserAccountException () {
    super();
  }
  
  public InvalidUserAccountException (String message) {
    super(message);
  }
  
  public InvalidUserAccountException (String message, Throwable cause) {
    super (message, cause);
  }
  
  public InvalidUserAccountException (String message, EInvalidAccountErrorType type) {
    super (message);
    this.type = type;
  }
  
  public EInvalidAccountErrorType getType() {
    return type;
  }
  
  public void setType(EInvalidAccountErrorType type) {
    this.type = type;
  }
  
  /**
   * The type of this exception, this encapsulates the error type and avoids
   * creating several exceptions with the same reason.
   * 
   * @author Gabriel Leonardo Diaz, 25.05.2013.
   */
  public enum EInvalidAccountErrorType {
    NON_EXISTING_ACCOUNT,  // Used to validate the user account through the email
    NON_ACTIVATED_ACCOUNT, // Used in the login
    DUPLICATED_ACCOUNT,    // Used in signing up process
    
    DEACTIVATED_ACCOUNT,   // Used in account activation process.
    ACTIVATED_ACCOUNT      // Used in account activation process.
  }
  
}
