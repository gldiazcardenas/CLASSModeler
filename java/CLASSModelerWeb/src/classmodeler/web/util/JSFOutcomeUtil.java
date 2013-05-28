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
  
  public static final String PAGES_PATH     = "/pages/*";
  public static final String DESIGNER_PATH  = "/designer";
  public static final String DASHBOARD_PATH = "/dashboard";
  public static final String PORTAL_PATH    = "/portal";
  
  public static final String INDEX          = "/index.xhtml";
  public static final String DASHBOARD      = "/pages/dashboard/dashboard.xhtml";
  public static final String DESIGNER       = "/pages/designer/designer.xhtml";
  
}
