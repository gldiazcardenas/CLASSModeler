/****************************************************

  Universidad Francisco de Paula Santander UFPS
  Cúcuta, Colombia
  (c) 2013 by UFPS. All rights reserved.

 ****************************************************/

package classmodeler.domain.user;


/**
 * Enumeration that represents the common human genders.
 * 
 * @author Gabriel Leonardo Diaz, o9.02.2013.
 */
public enum EGender {
  
  MALE ('M', "GENDER_MALE"),
  FEMALE ('F', "GENDER_FEMALE");
  
  private char id;
  private String name;
  
  private EGender (char id, String name) {
    this.id   = id;
    this.name = name;
  }
  
  /**
   * Gets the id of the gender enumerated value.
   * @return A <code>String</code> value as the id of the gender.
   */
  public char getId() {
    return id;
  }
  
	/**
	 * Gets the name of the gender enumerated value. This is a
	 * <code>String</code> constant value as 'GENDER_MALE'.
	 * 
	 * @return A <code>String</code> value as the name of the gender.
	 */
  public String getName () {
    return name;
  }
  
  @Override
  public String toString() {
    return getName();
  }
}
