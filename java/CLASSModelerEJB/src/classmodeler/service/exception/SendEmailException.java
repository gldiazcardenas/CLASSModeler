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
 * Exception thrown when the system tries to send an email but it is not able to
 * complete the operation by any reason.
 * 
 * @author Gabriel Leonardo Diaz, 18.05.2013.
 */
@ApplicationException (rollback = true)
public class SendEmailException extends ServiceException {

  private static final long serialVersionUID = 1L;
  
  public SendEmailException() {
    super();
  }
  
  public SendEmailException (String message) {
    super(message);
  }
  
  public SendEmailException (String message, Throwable cause) {
    super(message, cause);
  }
  
}
