/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.util;

import java.util.ResourceBundle;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 * Utility class to handle operation in the JSF context.
 * 
 * @author Gabriel Leonardo Diaz, 23.02.2013.
 */
public final class JSFContextUtil {
  
  private JSFContextUtil () {
    // This class CANNOT BE INSTANCED.
  }
  
  /**
   * Looks for a localized message through the given <code>messageKey</code> in
   * the message resource bundle.
   * 
   * @param messageKey
   *          The key of the message.
   * @return The localized message represented by the given key.
   */
  public static String getLocalizedMessage (String messageKey) {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msgs");
    return bundle.getString(messageKey);
  }
  
  /**
   * Gets the current session from the faces external context. If the session
   * has not been created this method will return <code>null</code>.
   * 
   * @return The current client session.
   */
  public static HttpSession getCurrentSession () {
    return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
  }

}
