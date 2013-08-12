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
   * The user can only open in read only mode.
   */
  READ (1),
  
  /**
   * The user can read the diagram and also make changes on it.
   */
  EDIT (2),
  
  /**
   * The user is able to re-share the diagram with other users.
   */
  SHARE (3),
  
  /**
   * The user 'owner' of the diagram. This privilege cannot be given to another user, just used to determine the user owner.
   */
  OWNER (4);
  
  /**
   * This field allows to know the order of the privileges, starting with the
   * most restrictive.
   */
  private int value;
  
  private EDiagramPrivilege (int value) {
    this.value = value;
  }
  
  /**
   * Determines if this privilege is greater than the given one.
   * 
   * @param privilege
   *          The privilege to compare.
   * @return True if the privilege given has less value than this.
   * @author Gabriel Leonardo Diaz, 10.08.2013.
   */
  public boolean isGreaterThan (EDiagramPrivilege privilege) {
    if (privilege == null) {
      return false;
    }
    
    return value > privilege.value;
  }
  
}