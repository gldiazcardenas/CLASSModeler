/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.diagram;

/**
 * Contains the possible values of the privileges that a user can provide when
 * he/she shares a diagram.
 * 
 * @author Gabriel Leonardo Diaz, 17.03.2013.
 */
public enum EDiagramPrivilege {
  /**
   * The new 'owner' of the diagram can only open in read only mode.
   */
  READ,
  
  /**
   * The new 'owner' can read the diagram and also make changes on it.
   */
  EDIT,
  
  /**
   * The user is able to re-share the diagram with other users.
   */
  SHARE
}