/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.util;

/**
 * Interfaces that contains all OUTCOME names used by the application.
 * 
 * @author Gabriel Leonardo Diaz, 11.05.2013.
 */
public interface JSFOutcomeUtil {
  
  public static final String REDIRECT_SUFIX = "?faces-redirect=true";
  
  public static final String INDEX          = "/index.xhtml" + REDIRECT_SUFIX;
  public static final String DASHBOARD      = "/pages/dashboard/dashboard.xhtml" + REDIRECT_SUFIX;
  public static final String DESIGNER       = "pages/designer/designer.xhtml" + REDIRECT_SUFIX;
  
}
