/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.UMLFactory;

/**
 * Constants used to generate UML metamodel.
 * 
 * @author Gabriel Leonardo Diaz, 24.03.2014.
 */
public final class UMLConstants {
  
  public static final PrimitiveType PRIMITIVE_INT   = UMLFactory.eINSTANCE.createPrimitiveType();
  public static final PrimitiveType PRIMITIVE_LONG  = UMLFactory.eINSTANCE.createPrimitiveType();
  public static final PrimitiveType PRIMITIVE_SHORT = UMLFactory.eINSTANCE.createPrimitiveType();
  public static final PrimitiveType PRIMITIVE_BYTE  = UMLFactory.eINSTANCE.createPrimitiveType();
  public static final PrimitiveType PRIMITIVE_CHAR  = UMLFactory.eINSTANCE.createPrimitiveType();
  
  public static final Map<String, PrimitiveType> PRIMITIVE_TYPES = new HashMap<String, PrimitiveType>();
  
  static {
    PRIMITIVE_INT.setName("int");
    PRIMITIVE_LONG.setName("long");
    PRIMITIVE_SHORT.setName("short");
    PRIMITIVE_BYTE.setName("byte");
    PRIMITIVE_CHAR.setName("char");
  }
  
  static {
    PRIMITIVE_TYPES.put(PRIMITIVE_INT.getName(), PRIMITIVE_INT);
    PRIMITIVE_TYPES.put(PRIMITIVE_LONG.getName(), PRIMITIVE_LONG);
    PRIMITIVE_TYPES.put(PRIMITIVE_SHORT.getName(), PRIMITIVE_SHORT);
    PRIMITIVE_TYPES.put(PRIMITIVE_BYTE.getName(), PRIMITIVE_BYTE);
    PRIMITIVE_TYPES.put(PRIMITIVE_CHAR.getName(), PRIMITIVE_CHAR);
  }
  
  private UMLConstants() {
    super();
  }
  
  /**
   * Checks if the given type is a primitive one.
   * 
   * @param type
   * @return
   * @author Gabriel Leonardo Diaz, 24.03.2014.
   */
  public static boolean isPrimitiveType (String type) {
    return PRIMITIVE_TYPES.containsKey(type);
  }
  
}
