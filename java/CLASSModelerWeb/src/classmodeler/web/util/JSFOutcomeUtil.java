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
  
  public static final String PAGES_PREFIX = "/pages";
  public static final String PORTAL_PREFIX = PAGES_PREFIX + "/portal";
  public static final String INDEX     = "/index.xhtml" + REDIRECT_SUFIX;
  public static final String DASHBOARD = PAGES_PREFIX + "/dashboard/dashboard.xhtml" + REDIRECT_SUFIX;
  public static final String SIGN_UP_CONFIRMATION = PORTAL_PREFIX + "/signUpConfirmation.xhtml" + REDIRECT_SUFIX;
  public static final String ACTIVATED_ACCOUNT_PAGE = PORTAL_PREFIX + "/activatedAccount.hxmlt";
  
}
