/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.resources;

import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

/**
 * Utility class to get handle localized messages.
 * 
 * @author Gabriel Leonardo Diaz, 19.04.2013.
 */
public final class JSFResourceBundle {
  
  public static final String DEFAULT_MALE_IMAGE_URL = "/resources/uploads/male_avatar.png";
  public static final String DEFAULT_FEMALE_IMAGE_URL = "/resources/uploads/female_avatar.png";
  
  private JSFResourceBundle () {
    // This class CANNOT be instanced
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
  
}
