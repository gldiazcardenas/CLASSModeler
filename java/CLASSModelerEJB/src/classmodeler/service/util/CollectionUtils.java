/****************************************************

  Universidad Francisco de Paula Santander UFPS
  Cúcuta, Colombia
  (c) 2013 by UFPS. All rights reserved.

 ****************************************************/

package classmodeler.service.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
  
  /**
   * Merges two lists in a single one, this method just adds the second list to
   * the end of the first list and returns a new instance. This is null safe.
   * 
   * @param list1
   *          The list one.
   * @param list2
   *          The list two.
   * @return The concatenation of the lists.
   * @author Gabriel Leonardo Diaz, 01.06.2013.
   */
  public static <T extends Object> List<T> mergeLists (List<T> list1, List<T> list2) {
    if (list1 == null) {
      return list2;
    }
    
    if (list2 == null) {
      return list1;
    }
    
    List<T> newList = new ArrayList<T>();
    newList.addAll(list1);
    newList.addAll(list2);
    
    return newList;
  }

}
