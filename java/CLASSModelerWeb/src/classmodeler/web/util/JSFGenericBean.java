/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.util;

import java.io.Serializable;

import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

/**
 * Class that defines common methods for JSF CDI beans. This should be the parent
 * class for each JSF bean in the application.
 * 
 * @author Gabriel Leonardo Diaz, 23.02.2013.
 */
public abstract class JSFGenericBean implements Serializable {

  private static final long serialVersionUID = 1L;
  
  /**
   * Use this always to add messages for the user. The component with this ID
   * shows all messages added from the controller.
   */
  public static final String GENERAL_MESSAGE_ID = "generalMessage";

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
  public void addErrorMessage(String clientId, String message, String details) {
    addMessage(FacesMessage.SEVERITY_ERROR, clientId, message, details);
  }

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
  public void addWarningMessage(String clientId, String message, String details) {
    addMessage(FacesMessage.SEVERITY_WARN, clientId, message, details);
  }

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
  public void addInformationMessage(String clientId, String message, String details) {
    addMessage(FacesMessage.SEVERITY_INFO, clientId, message, details);
  }
  
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
  public void addFatalMessage(String clientId, String message, String details) {
    addMessage(FacesMessage.SEVERITY_FATAL, clientId, message, details);
  }
  
  /**
   * Gets a JSF bean by its name.
   * 
   * @param beanName
   *          The name of the bean to look for.
   * @param clazz
   *          The class of the bean.
   * @return A JSF bean object.
   */
  @SuppressWarnings("unchecked")
  public <T> T getJSFBean(final String beanName, final Class<T> clazz) {
    ELContext elContext = FacesContext.getCurrentInstance().getELContext();
    return (T) FacesContext.getCurrentInstance().getApplication()
                                                .getELResolver()
                                                .getValue(elContext, null, beanName);
  } 
  
  /**
   * Generic method that adds messages to the JSF context with the given
   * severity.
   * 
   * @param severity
   *          The severity of the message.
   * @param clientId
   *          The ID of the component in which the message will be displayed.
   * @param message
   *          The message text.
   * @param details
   *          The details of the message.
   * @author Gabriel Leonardo Diaz, 23.02.2013.
   */
  private void addMessage (Severity severity, String clientId, String message, String details) {
    FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(severity, message, details));
  }
  
}
