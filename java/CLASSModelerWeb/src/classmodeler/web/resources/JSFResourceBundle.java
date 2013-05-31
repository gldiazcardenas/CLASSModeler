/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.resources;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

import classmodeler.service.util.CollectionUtils;

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
   * @param arguments
   *          The values of the message, where the first element is the
   *          messageKey and the other elements are the arguments of the
   *          message..
   * @return The localized message represented by the given key.
   */
  public static String getLocalizedMessage (String... values) {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, "msgs");
    
    String messageKey = values[0];
    Object [] arguments = null;
    
    if (CollectionUtils.size(values) > 1) {
      arguments = new Object[values.length - 1];
      for (int i = 0; i < arguments.length; i++) {
        arguments[i] = values[i + 1];
      }
    }
    
    return MessageFormat.format(bundle.getString(messageKey), arguments);
  }
  
}
