/****************************************************

  Universidad Francisco de Paula Santander UFPS
  C�cuta, Colombia
  (c) 2013 by UFPS. All rights reserved.

 ****************************************************/

package classmodeler.service.exception;

import javax.ejb.ApplicationException;


/**
 * Generic exception for EJB services.
 * 
 * @author Gabriel Leonardo Diaz, 14.03.2013.
 */
@ApplicationException (rollback = true, inherited = true)
public abstract class ServiceException extends Exception {

  private static final long serialVersionUID = 1L;
  
  public ServiceException() {
    super();
  }
  
  public ServiceException (String message) {
    super (message);
  }
  
  public ServiceException (String message, Throwable cause) {
    super(message, cause);
  }

}
