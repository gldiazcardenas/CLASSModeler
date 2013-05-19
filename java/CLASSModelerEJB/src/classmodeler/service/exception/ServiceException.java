/****************************************************

  Universidad Francisco de Paula Santander UFPS
  Cúcuta, Colombia
  (c) 2013 by UFPS. All rights reserved.

 ****************************************************/

package classmodeler.service.exception;


/**
 * Generic exception for EJB services.
 * 
 * @author Gabriel Leonardo Diaz, 14.03.2013.
 */
public abstract class ServiceException extends RuntimeException {

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
