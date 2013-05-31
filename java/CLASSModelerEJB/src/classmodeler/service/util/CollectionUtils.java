/****************************************************

  Universidad Francisco de Paula Santander UFPS
  C�cuta, Colombia
  (c) 2013 by UFPS. All rights reserved.

 ****************************************************/

package classmodeler.service.util;

import java.util.Collection;
import java.util.Map;

/**
 * Utility class that contains generic methods to handle collections (lists,
 * arrays, maps and sets) and their elements.
 * 
 * @author Gabriel Leonardo Diaz, 07.01.2013.
 */
public final class CollectionUtils {
  
  private CollectionUtils () {
    // This class CANNOT BE INSTANCED.
  }
  
  /**
   * Generic method that checks if the given collection is empty. If the
   * parameter is null then this method will return <code>true</code>.
   * 
   * @param collection
   *          The collection to check if it's empty.
   * @return A boolean value that indicates if the given collection is empty or
   *         not.
   * @author Gabriel Leonardo Diaz, 07.01.2013
   */
  public static boolean isEmptyCollection (Collection<?> collection) {
    return collection == null || collection.isEmpty();
  }
  
  /**
   * Generic method that checks if the given map is empty. If the parameter is
   * null then this method will return <code>true</code>.
   * 
   * @param map
   *          The map to check.
   * @return A boolean value that indicates if the given map is empty or not.
   * @author Gabriel Leonardo Diaz, 07.01.2013
   */
  public static boolean isEmptyMap (Map<?, ?> map) {
    return map == null || map.isEmpty();
  }
  
  /**
   * Generic method to get the size of a collection, if the collection is null
   * this method will return zero(0).
   * 
   * @param collection
   *          The collection to evaluate.
   * @return The size of the collection.
   * @author Gabriel Leonardo Diaz, 30.05.2013.
   */
  public static int size (Collection<?> collection) {
    return collection == null ? 0 : collection.size();
  }
  
  /**
   * Generic method to get the size of an array, if the array is null this
   * method will return zero(0).
   * 
   * @param array
   *          The array to evaluate.
   * @return The size of the array.
   * @author Gabriel Leonardo Diaz, 30.05.2013.
   */
  public static <T extends Object> int size (T[] array) {
    return array == null ? 0 : array.length;
  }

}
