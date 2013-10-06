/****************************************************

  Universidad Francisco de Paula Santander UFPS
  Cúcuta, Colombia
  (c) 2013 by UFPS. All rights reserved.

 ****************************************************/

package classmodeler.domain.user;


/**
 * Enumeration that represents the common human genders.
 * 
 * @author Gabriel Leonardo Diaz, 09.02.2013.
 */
public enum EGender {
  
  /*
   * IMPORTANT: Please don't change the order of the enumeration literals, this
   * order is important because the ordinal of each literal is stored in
   * database.
   */
  
  MALE ("GENDER_MALE"),
  FEMALE ("GENDER_FEMALE");
  
  private String name;
  
  private EGender (String name) {
    this.name = name;
  }
  
  /**
   * Gets the name of the gender enumerated value. This is a <code>String</code>
   * constant value as 'GENDER_MALE' or 'GENDER_FEMALE'.
   * 
   * @return A <code>String</code> value as the name of the gender.
   */
  public String getName () {
    return name;
  }
}
