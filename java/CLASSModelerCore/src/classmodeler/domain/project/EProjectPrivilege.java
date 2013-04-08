/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.project;

/**
 * Contains the possible values of the privileges that a user can provide when
 * he/she shares a project.
 * 
 * @author Gabriel Leonardo Diaz, 17.03.2013.
 */
public enum EProjectPrivilege {
  /**
   * The new 'owner' of the project can only open the diagrams to read them,
   * modifications are not allowed.
   */
  READ,
  
  /**
   * The new 'owner' can read the diagrams and also make changes on them.
   */
  EDIT,
  
  /**
   * The user is able to re-share the project with other users.
   */
  SHARE
}