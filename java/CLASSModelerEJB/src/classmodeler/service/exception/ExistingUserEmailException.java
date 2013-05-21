/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.exception;


/**
 * This is thrown when the user tries to register itself twice. The system looks
 * for the email and throws an exception if any is found.
 * 
 * @author Gabriel Leonardo Diaz, 17.05.2013.
 */
public class ExistingUserEmailException extends ServiceException {
  
  private static final long serialVersionUID = 1L;
  
  private String email;
  
  public ExistingUserEmailException() {
    super();
  }
  
  public ExistingUserEmailException (String message) {
    super(message);
  }
  
  public ExistingUserEmailException (String message, String email) {
    super(message);
    
    this.email = email;
  }
  
  public ExistingUserEmailException (String message, Throwable cause) {
    super(message, cause);
  }
  
  public String getEmail() {
    return email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
}
