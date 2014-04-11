/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.code;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Type;

/**
 * Bean that encapsulates a source code file generated.
 * 
 * @author Gabriel Leonardo Diaz, 05.03.2014.
 */
public class SourceCodeFile {
  
  public static final String JAVA_FORMAT = ".java";
  
  private String name;
  private String format;
  private Element element;
  
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
  
  public Element getElement() {
    return element;
  }
  
  public void setElement(Element element) {
    this.element = element;
  }
  
  public String getFileName () {
    return name + format;
  }
  
  public String getFileNameWithFolder () {
    Package aPackage = null;
    
    if (element instanceof Type) {
      aPackage = ((Type) element).getPackage();
    }
    
    String folderName = "";
    if (aPackage != null) {
      folderName = aPackage.getName() + "/";
    }
    
    return folderName + getFileName();
  }
  
}
