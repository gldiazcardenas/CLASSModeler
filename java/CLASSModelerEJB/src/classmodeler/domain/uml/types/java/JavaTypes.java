/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml.types.java;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;

/**
 * Constants used to generate UML metamodel.
 * 
 * @author Gabriel Leonardo Diaz, 24.03.2014.
 */
public final class JavaTypes {
  
  public static final Type BOOLEAN_TYPE                       = createDataType("Boolean");
  public static final Type BYTE_TYPE                          = createDataType("Byte");
  public static final Type CHARACTER_TYPE                     = createDataType("Character");
  public static final Type DOUBLE_TYPE                        = createDataType("Double");
  public static final Type FLOAT_TYPE                         = createDataType("Float");
  public static final Type INTEGER_TYPE                       = createDataType("Integer");
  public static final Type LONG_TYPE                          = createDataType("Long");
  public static final Type SHORT_TYPE                         = createDataType("Short");
  public static final Type STRING_TYPE                        = createDataType("String");
  public static final Type VOID_TYPE                          = createDataType("Void");
  public static final Type DATE_TYPE                          = createDataType("Date");
  public static final Type TIME_TYPE                          = createDataType("Time");
  
  public static final PrimitiveWrappedType PRIMITIVE_BOOLEAN  = createPrimitiveType("boolean", BOOLEAN_TYPE);
  public static final PrimitiveWrappedType PRIMITIVE_BYTE     = createPrimitiveType("byte", BYTE_TYPE);
  public static final PrimitiveWrappedType PRIMITIVE_CHAR     = createPrimitiveType("char", CHARACTER_TYPE);
  public static final PrimitiveWrappedType PRIMITIVE_DOUBLE   = createPrimitiveType("double", DOUBLE_TYPE);
  public static final PrimitiveWrappedType PRIMITIVE_FLOAT    = createPrimitiveType("float", FLOAT_TYPE);
  public static final PrimitiveWrappedType PRIMITIVE_INT      = createPrimitiveType("int", INTEGER_TYPE);
  public static final PrimitiveWrappedType PRIMITIVE_LONG     = createPrimitiveType("long", LONG_TYPE);
  public static final PrimitiveWrappedType PRIMITIVE_SHORT    = createPrimitiveType("short", SHORT_TYPE);
  public static final PrimitiveWrappedType PRIMITIVE_VOID     = createPrimitiveType("void", VOID_TYPE);
  
  private static final Map<String, PrimitiveWrappedType> PRIMITIVE_TYPES = new HashMap<String, PrimitiveWrappedType>();
  private static final Map<String, Type> OBJECT_TYPES = new HashMap<String, Type>();
  
  static {
    PRIMITIVE_TYPES.put(PRIMITIVE_BOOLEAN.getName(), PRIMITIVE_BOOLEAN);
    PRIMITIVE_TYPES.put(PRIMITIVE_BYTE.getName(), PRIMITIVE_BYTE);
    PRIMITIVE_TYPES.put(PRIMITIVE_CHAR.getName(), PRIMITIVE_CHAR);
    PRIMITIVE_TYPES.put(PRIMITIVE_DOUBLE.getName(), PRIMITIVE_DOUBLE);
    PRIMITIVE_TYPES.put(PRIMITIVE_FLOAT.getName(), PRIMITIVE_FLOAT);
    PRIMITIVE_TYPES.put(PRIMITIVE_INT.getName(), PRIMITIVE_INT);
    PRIMITIVE_TYPES.put(PRIMITIVE_LONG.getName(), PRIMITIVE_LONG);
    PRIMITIVE_TYPES.put(PRIMITIVE_SHORT.getName(), PRIMITIVE_SHORT);
    PRIMITIVE_TYPES.put(PRIMITIVE_VOID.getName(), PRIMITIVE_VOID);
    
    OBJECT_TYPES.put(STRING_TYPE.getName(), STRING_TYPE);
    OBJECT_TYPES.put(DATE_TYPE.getName(), DATE_TYPE);
    OBJECT_TYPES.put(TIME_TYPE.getName(), TIME_TYPE);
  }
  
  private JavaTypes() {
    super();
  }
  
  /**
   * Creates a data type with the given name.
   * @param name
   * @return
   */
  private static Type createDataType (String name) {
    DataType dataType = UMLFactory.eINSTANCE.createDataType();
    dataType.setName(name);
    return dataType;
  }
  
  /**
   * Creates a primitive type with the given name and its wrapper.
   * 
   * @param name
   * @param wrapperType
   * @return
   */
  private static PrimitiveWrappedType createPrimitiveType (String name, Type wrapperType) {
    return new PrimitiveWrappedType(name, wrapperType);
  }
  
  /**
   * Checks if the given type is a primitive one.
   * 
   * @param type
   * @return
   * @author Gabriel Leonardo Diaz, 24.03.2014.
   */
  public static boolean isPrimitiveType (String typeId) {
    return PRIMITIVE_TYPES.containsKey(typeId);
  }
  
  /**
   * Checks if the given type is an object one.
   * 
   * @param typeId
   * @return
   * @author Gabriel Leonardo Diaz, 18.04.2014.
   */
  public static boolean isObjectType (String typeId) {
    return OBJECT_TYPES.containsKey(typeId);
  }
  
  /**
   * Gets the primitive type associated to the given id.
   * 
   * @param typeId
   * @return
   * @author Gabriel Leonardo Diaz, 27.03.2014.
   */
  public static Type getType (String typeId) {
    Type type = PRIMITIVE_TYPES.get(typeId);
    
    if (type == null) {
      type = OBJECT_TYPES.get(type);
    }
    
    return type;
  }
  
  /**
   * Creates a collection type with the given name, for the given type.
   * 
   * @param collectionName
   * @param genericType
   * @return
   * @author Gabriel Leonardo Diaz, 27.03.2014.
   */
  public static CollectionGenericType createCollectionType (String collectionName, Type genericType) {
    if (ArrayPrimitiveType.ARRAY_TYPE_NAME.equals(collectionName)) {
      return new ArrayPrimitiveType(genericType);
    }
    
    return new CollectionGenericType(collectionName, genericType);
  }
  
}
