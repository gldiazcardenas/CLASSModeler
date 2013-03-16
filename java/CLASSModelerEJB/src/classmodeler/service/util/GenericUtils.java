/****************************************************

  Universidad Francisco de Paula Santander UFPS
  Cúcuta, Colombia
  (c) 2013 by UFPS. All rights reserved.

 ****************************************************/

package classmodeler.service.util;

/**
 * Utility class that contains generic method to handle common operations like
 * validations, conversions and so on.
 * 
 * @author Gabriel Leonardo Diaz, 07.01.2013.
 */
public final class GenericUtils {
  
  private GenericUtils() {
    // This class CANNOT BE INSTANCED.
  }
  
  /**
   * Generic method that checks if the given <code>String</code> parameter is
   * empty or not. It is empty either it's null or its empty method returns
   * true.
   * 
   * @param string
   *          The string to check.
   * @return A boolean value indicating if the string is empty or not.
   * @author Gabriel Leonardo Diaz, 07.01.2013.
   */
  public static boolean isEmptyString (String string) {
    return string == null || string.isEmpty();
  }
  
  /**
   * Generic method to compare to objects by their equals method. This checks if
   * the objects are null, in that case they will be equals only if both are
   * null.
   * 
   * @param obj1
   *          The first object to compare.
   * @param obj2
   *          The second object to compare.
   * @return A <code>boolean</code> indicating if both objects are equals, if
   *         both objects are null this method will return <code>true</code>.
   * @author Gabriel Leonardo Diaz, 02.03.2013.
   */
  public static boolean equals (Object obj1, Object obj2) {
    if (obj1 == null) {
      return obj2 == null;
    }
    
    if (obj2 == null) {
      return false;
    }
    
    return obj1.equals(obj2);
  }
  
  /**
   * Compares two <code>String</code> values and determines if they are equals,
   * this method calls to {@link String#trim()} before calling
   * {@link String#equals(Object)}.
   * 
   * @param str1
   *          The first string to compare.
   * @param str2
   *          The second string to compare.
   * @return A <code>boolean</code> indicating if the strings are equals, if
   *         both objects are null this method will return <code>true</code>.
   * @author Gabriel Leonardo Diaz, 02.03.2013.
   */
  public static boolean equals (String str1, String str2) {
    if (str1 == null) {
      return str2 == null;
    }
    
    if (str2 == null) {
      return false;
    }
    
    return str1.trim().equals(str2.trim());
  }
}
