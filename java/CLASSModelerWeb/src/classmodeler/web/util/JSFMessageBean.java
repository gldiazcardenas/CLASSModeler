/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.util;


/**
 * Interface that defines generic methods to add messages in the JSF context in
 * order to be shown in the graphical user interface.
 * 
 * @author Gabriel Leonardo Diaz, 23.02.2013.
 */
public interface JSFMessageBean {
  
  /**
   * Adds an error message to the JSF context.
   * 
   * @param clientId
   *          The ID of the component in which the message will be displayed.
   * @param message
   *          The text of the message, this parameter should not be null.
   * @param details
   *          The details of the message, null value is allowed.
   */
  public void addErrorMessage (String clientId, String message, String details);
  
  /**
   * Adds a warning message to the JSF context.
   * 
   * @param clientId
   *          The ID of the component in which the message will be displayed.
   * @param message
   *          The text of the message, this parameter should not be null.
   * @param details
   *          The details of the message, null value is allowed.
   */
  public void addWarningMessage (String clientId, String message, String details);
  
  /**
   * Adds an information message to the JSF context.
   * 
   * @param clientId
   *          The ID of the component in which the message will be displayed.
   * @param message
   *          The text of the message, this parameter should not be null.
   * @param details
   *          The details of the message, null value is allowed.
   */
  public void addInformationMessage (String clientId, String message, String details);
  
  /**
   * Adds a fatal error message to the JSF context.
   * 
   * @param clientId
   *          The ID of the component in which the message will be displayed.
   * @param message
   *          The text of the message, this parameter should not be null.
   * @param details
   *          The details of the message, null value is allowed.
   */
  public void addFatalMessage (String clientId, String message, String details);

}
