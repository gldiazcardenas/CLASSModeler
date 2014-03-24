/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.code;

import java.util.Comparator;

/**
 * Comparator for source code file objects, this allows to sort the objects
 * alphabetically.
 * 
 * @author Gabriel Leonardo Diaz, 05.03.2014.
 */
public class SourceCodeFileComparator implements Comparator<SourceCodeFile> {
  
  public static final SourceCodeFileComparator CASE_INSENSITIVE_COMPARATOR = new SourceCodeFileComparator();
  
  @Override
  public int compare(SourceCodeFile o1, SourceCodeFile o2) {
    return o1.getName().compareTo(o2.getName());
  }
}