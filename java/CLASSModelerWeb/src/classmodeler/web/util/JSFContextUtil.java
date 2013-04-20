/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.util;

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
   * Gets the current session from the faces external context. If the session
   * has not been created this method will return <code>null</code>.
   * 
   * @return The current client session.
   */
  public static HttpSession getCurrentSession () {
    return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
  }

}
