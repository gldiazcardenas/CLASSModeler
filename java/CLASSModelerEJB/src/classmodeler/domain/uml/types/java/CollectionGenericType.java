/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml.types.java;

import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.internal.impl.DataTypeImpl;

/**
 * Metamodel class that represents a Java one-dimensional collection. The method
 * {@link #getName()} was overridden to include the generic type supported by
 * this collection, in order to get only the name of the collection use the
 * method {@link #getCollectionName()}.
 * 
 * @author Gabriel Leonardo Diaz, 27.03.2014.
 */
public class CollectionGenericType extends DataTypeImpl {
  
  private Type genericType;
  
  public CollectionGenericType(String name, Type genericType) {
    super();
    setName(name);
    this.genericType = genericType;
  }
  
  public Type getGenericType() {
    return genericType;
  }
  
  public void setGenericType(Type genericType) {
    this.genericType = genericType;
  }
  
  @Override
  public String getName() {
    return super.getName() + "<" + getGenericTypeName() + ">";
  }
  
  /**
   * Convenience method to check the generic type and get the appropriate name.
   * This considers wrapped types.
   * 
   * @return
   * @author Gabriel Leonardo Diaz, 27.03.2014.
   */
  public String getGenericTypeName () {
    if (genericType instanceof PrimitiveWrappedType) {
      return ((PrimitiveWrappedType) genericType).getWrapper().getName();
    }
    
    return genericType.getName();
  }
  
  /**
   * Gets the raw collection name.
   * @return
   */
  public String getCollectionName () {
    return getName();
  }
  
}
