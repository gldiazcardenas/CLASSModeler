/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.implementation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.stringtemplate.v4.ST;
import org.w3c.dom.Element;

import classmodeler.domain.code.SourceCodeFile;
import classmodeler.domain.share.SharedDiagram;
import classmodeler.service.SourceCodeService;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;

/**
 * Implementation of the file service.
 * 
 * @author Gabriel Leonardo Diaz, 05.03.2014.
 */
public @Stateless class SourceCodeServiceBean implements SourceCodeService {
  
  
  @Override
  public List<SourceCodeFile> generateCode(SharedDiagram diagram) {
    List<SourceCodeFile> sourceCodeFiles = new ArrayList<SourceCodeFile>();
    
    mxCell mxCell;
    mxGraphModel model = diagram.getModel();
    
    SourceCodeFile file;
    
    for (Object cell : model.getCells().values()) {
      mxCell = (com.mxgraph.model.mxCell) cell;
      file   = null;
      
      if (isClass(mxCell)) {
        file = generateClass(mxCell);
      }
      else if (isInterface(mxCell)) {
        file = generateInteface(mxCell);
      }
      else if (isEnumeration(mxCell)) {
        file = generateEnumeration(mxCell);
      }
      
      if (file != null) {
        sourceCodeFiles.add(file);
      }
    }
    
    return sourceCodeFiles;
  }
  
  @Override
  public File createDiskFile(SourceCodeFile file) {
    return null; // TODO Auto-generated method stub
  }
  
  /**
   * Generates the source code of the given class element.
   * 
   * @param classUML
   * @return
   * @author Gabriel Leonardo Diaz, 05.03.2014.
   */
  private SourceCodeFile generateClass (mxCell classUML) {
    ST classTemplate = TEMPLATES.getInstanceOf("class");
    classTemplate.add("name", classUML.getAttribute("name"));
    classTemplate.add("visibility", classUML.getAttribute("visibility"));
    classTemplate.add("package", classUML.getAttribute("package"));
    
    SourceCodeFile file = new SourceCodeFile();
    file.setName(classUML.getAttribute("name"));
    file.setFormat("java");
    file.setCode(classTemplate.render());
    
    return file;
  }
  
  /**
   * Generates the source code of the given interface element.
   * 
   * @param interfaceUML
   * @return
   * @author Gabriel Leonardo Diaz, 05.03.2014.
   */
  private SourceCodeFile generateInteface (mxCell interfaceUML) {
    ST interfaceTemplate = TEMPLATES.getInstanceOf("interface");
    interfaceTemplate.add("name", interfaceUML.getAttribute("name"));
    interfaceTemplate.add("visibility", interfaceUML.getAttribute("visibility"));
    interfaceTemplate.add("package", interfaceUML.getAttribute("package"));
    
    SourceCodeFile file = new SourceCodeFile();
    file.setName(interfaceUML.getAttribute("name"));
    file.setFormat("java");
    file.setCode(interfaceTemplate.render());
    
    return file;
  }
  
  /**
   * Generates the source code of the given enumeration element.
   * 
   * @param enumerationUML
   * @return
   * @author Gabriel Leonardo Diaz, 05.03.2014.
   */
  private SourceCodeFile generateEnumeration (mxCell enumerationUML) {
    ST enumerationTemplate = TEMPLATES.getInstanceOf("enumeration");
    enumerationTemplate.add("name", enumerationUML.getAttribute("name"));
    enumerationTemplate.add("visibility", enumerationUML.getAttribute("visibility"));
    enumerationTemplate.add("package", enumerationUML.getAttribute("package"));
    
    SourceCodeFile file = new SourceCodeFile();
    file.setName(enumerationUML.getAttribute("name"));
    file.setFormat("java");
    file.setCode(enumerationTemplate.render());
    
    return file;
  }
  
  /**
   * Checks if the given cell contains a UML Class.
   * 
   * @param cell
   * @return
   * @author Gabriel Leonardo Diaz, 05.03.2014.
   */
  private boolean isClass (mxCell cell) {
    if (cell == null || !(cell.getValue() instanceof Element)) {
      return false;
    }
    return ((Element) cell.getValue()).getTagName().equalsIgnoreCase("class");
  }
  
  /**
   * Checks if the given cell contains a UML Enumeration.
   * 
   * @param cell
   * @return
   * @author Gabriel Leonardo Diaz, 05.03.2014.
   */
  private boolean isEnumeration (mxCell cell) {
    if (cell == null || !(cell.getValue() instanceof Element)) {
      return false;
    }
    return ((Element) cell.getValue()).getTagName().equalsIgnoreCase("enumeration");
  }
  
  /**
   * Checks if the given cell contains a UML Interface.
   * 
   * @param cell
   * @return
   * @author Gabriel Leonardo Diaz, 05.03.2014.
   */
  private boolean isInterface (mxCell cell) {
    if (cell == null || !(cell.getValue() instanceof Element)) {
      return false;
    }
    return ((Element) cell.getValue()).getTagName().equalsIgnoreCase("interface");
  }
  
}
