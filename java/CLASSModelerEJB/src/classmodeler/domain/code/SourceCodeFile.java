/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.code;

/**
 * Bean that encapsulates a source code file generated.
 * 
 * @author Gabriel Leonardo Diaz, 05.03.2014.
 */
public class SourceCodeFile {
  
  private String name;
  private String format;
  private String icon;
  private String code;
  
  public SourceCodeFile() {
    super();
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getFormat() {
    return format;
  }
  
  public void setFormat(String format) {
    this.format = format;
  }
  
  public String getCode() {
    return code;
  }
  
  public void setCode(String code) {
    this.code = code;
  }
  
  public String getIcon() {
    return icon;
  }
  
  public void setIcon(String icon) {
    this.icon = icon;
  }
  
  public String getFullName () {
    return name + "." + format;
  }
  
}
