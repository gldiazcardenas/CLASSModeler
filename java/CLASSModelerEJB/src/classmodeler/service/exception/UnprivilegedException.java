/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.exception;

/**
 * Used when the user does not have privileges for any operation.
 * 
 * @author Gabriel Leonardo Diaz, 06.10.2013.
 */
public class UnprivilegedException extends ServiceException {
  
  private static final long serialVersionUID = 1L;

  public UnprivilegedException() {
    // Empty Constructor
  }
  
  public UnprivilegedException (String message) {
    super(message);
  }
  
  public UnprivilegedException (String message, Throwable cause) {
    super (message, cause);
  }
  
}
