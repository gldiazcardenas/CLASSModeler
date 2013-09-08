/****************************************************

  Universidad Francisco de Paula Santander UFPS
  Cúcuta, Colombia
  (c) 2013 by UFPS. All rights reserved.

 ****************************************************/

package classmodeler.domain.user;

/**
 * This represents the possible values for diagrammer account status.
 * 
 * @author Gabriel Leonardo Diaz, 14.03.2013.
 */
public enum EDiagrammerAccountStatus {

  /**
   * The diagrammer created his/her account but hasn't activated it.
   */
  INACTIVATED,
  /**
   * The diagrammer created his/her account and activated it.
   */
  ACTIVATED,
  /**
   * The diagrammer had an account but decided to deactivate it by himself/herself
   */
  DEACTIVATED

}
