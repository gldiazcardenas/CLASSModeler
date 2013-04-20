/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.data;

/**
 * Data bean to store the property values for the object in the designer.
 * 
 * @author Gabriel Leonardo Diaz, 19.04.2013.
 */
public class PropertyValue {
  
  private int key;
  private String name;
  private String value;
  
  public PropertyValue() {
    this(0);
  }
  
  public PropertyValue (int key) {
    this(key, null);
  }
  
  public PropertyValue (int key, String name) {
    this (key, name, null);
  }
  
  public PropertyValue (int key, String name, String value) {
    super();
    this.key = key;
    this.name = name;
    this.value = value;
  }
  
  public int getKey() {
    return key;
  }
  
  public void setKey(int key) {
    this.key = key;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getValue() {
    return value;
  }
  
  public void setValue(String value) {
    this.value = value;
  }
  
}
