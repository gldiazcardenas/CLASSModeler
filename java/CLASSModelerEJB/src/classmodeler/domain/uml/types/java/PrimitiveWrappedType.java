/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml.types.java;

import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.internal.impl.DataTypeImpl;

/**
 * Metamodel class that represents a Java primitive type and allows access the
 * Wrapper type.
 * 
 * @author Gabriel Leonardo Diaz, 27.03.2014.
 */
public class PrimitiveWrappedType extends DataTypeImpl implements PrimitiveType {
  
  private Type wrapper;
  
  public PrimitiveWrappedType (String name, Type wrapper) {
    super();
    setName(name);
    this.wrapper = wrapper;
  }
  
  public Type getWrapper() {
    return wrapper;
  }
  
  public void setWrapper(Type wrapper) {
    this.wrapper = wrapper;
  }
  
}
