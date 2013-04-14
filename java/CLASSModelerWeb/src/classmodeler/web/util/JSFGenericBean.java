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
public class JSFGenericBean implements JSFMessageBean, Serializable {

  private static final long serialVersionUID = 1L;

  @Override
  public void addErrorMessage(String message, String details) {
    addMessage(FacesMessage.SEVERITY_ERROR, message, details);
  }

  @Override
  public void addWarningMessage(String message, String details) {
    addMessage(FacesMessage.SEVERITY_WARN, message, details);
  }

  @Override
  public void addInformationMessage(String message, String details) {
    addMessage(FacesMessage.SEVERITY_INFO, message, details);
  }
  
  @Override
  public void addFatalMessage(String message, String details) {
    addMessage(FacesMessage.SEVERITY_FATAL, message, details);
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
   * @param message
   *          The message text.
   * @param details
   *          The details of the message.
   * @author Gabriel Leonardo Diaz, 23.02.2013.
   */
  private void addMessage (Severity severity, String message, String details) {
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, message, details));
  }
  
}
