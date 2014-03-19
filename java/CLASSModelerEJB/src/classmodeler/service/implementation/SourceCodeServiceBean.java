/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.implementation;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.stringtemplate.v4.ST;
import org.w3c.dom.Element;

import classmodeler.domain.code.SourceCodeFile;
import classmodeler.domain.share.SharedDiagram;
import classmodeler.domain.user.User;
import classmodeler.service.SourceCodeService;
import classmodeler.service.util.GenericUtils;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;

/**
 * Implementation of the file service.
 * 
 * @author Gabriel Leonardo Diaz, 05.03.2014.
 */
public @Stateless class SourceCodeServiceBean implements SourceCodeService {
  
  private mxGraphModel model;
  
  @Override
  public List<SourceCodeFile> generateCode(User user, SharedDiagram diagram) {
    List<SourceCodeFile> sourceCodeFiles = new ArrayList<SourceCodeFile>();
    
    model = diagram.getModel();
    
    mxCell mxCell;
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
  
  /**
   * Generates the source code of the given class element.
   * 
   * @param classUML
   * @return
   * @author Gabriel Leonardo Diaz, 05.03.2014.
   */
  private SourceCodeFile generateClass (mxCell classUML) {
    ST tempClass     = TEMPLATES.getInstanceOf("class");
    
    tempClass.add("name", classUML.getAttribute("name"));
    tempClass.add("visibility", classUML.getAttribute("visibility"));
    tempClass.add("package", getPackage(classUML.getAttribute("package")));
    
    List<String> attributes = new ArrayList<String>();
    for (mxCell property : getProperties(classUML)) {
      
      attributes.add(generateProperty(property));
    }
    
    List<String> operations = new ArrayList<String>();
    for (mxCell operation : getOperations(classUML)) {
      operations.add(generateOperation(operation));
    }
    
    tempClass.add("attributes", attributes);
    tempClass.add("operations", operations);
    
    SourceCodeFile source = new SourceCodeFile();
    source.setName(classUML.getAttribute("name"));
    source.setFormat("java");
    source.setCode(tempClass.render());
    
    return source;
  }
  
  /**
   * Generates the source code of the given interface element.
   * 
   * @param interfaceUML
   * @return
   * @author Gabriel Leonardo Diaz, 05.03.2014.
   */
  private SourceCodeFile generateInteface (mxCell interfaceUML) {
    ST tempInterface = TEMPLATES.getInstanceOf("interface");
    tempInterface.add("name", interfaceUML.getAttribute("name"));
    tempInterface.add("visibility", interfaceUML.getAttribute("visibility"));
    tempInterface.add("package", getPackage(interfaceUML.getAttribute("package")));
    
    
    
    SourceCodeFile source = new SourceCodeFile();
    source.setName(interfaceUML.getAttribute("name"));
    source.setFormat("java");
    source.setCode(tempInterface.render());
    
    return source;
  }
  
  /**
   * Generates the source code of the given enumeration element.
   * 
   * @param enumerationCell
   * @return
   * @author Gabriel Leonardo Diaz, 05.03.2014.
   */
  private SourceCodeFile generateEnumeration (mxCell enumerationCell) {
    ST tempEnum = TEMPLATES.getInstanceOf("enumeration");
    tempEnum.add("name", enumerationCell.getAttribute("name"));
    tempEnum.add("visibility", enumerationCell.getAttribute("visibility"));
    tempEnum.add("package", getPackage(enumerationCell.getAttribute("package")));
    
    List<String> literals = new ArrayList<String>();
    mxCell literalCell;
    for (Object child : getLiterals(enumerationCell)) {
      literalCell = (mxCell) child;
      literals.add(literalCell.getAttribute("name"));
    }
    
    tempEnum.add("literals", literals);
    
    SourceCodeFile source = new SourceCodeFile();
    source.setName(enumerationCell.getAttribute("name"));
    source.setFormat("java");
    source.setCode(tempEnum.render());
    
    return source;
  }
  
  /**
   * 
   * @param property
   * @return
   */
  private String generateProperty (mxCell property) {
    ST tempProperty = TEMPLATES.getInstanceOf("property");
    tempProperty.add("name", property.getAttribute("name"));
    tempProperty.add("visibility", property.getAttribute("visibility"));
    tempProperty.add("type", getType(property));
    return tempProperty.render();
  }
  
  /**
   * 
   * @param operation
   * @return
   */
  private String generateOperation (mxCell operation) {
    ST tempOperation = TEMPLATES.getInstanceOf("operation");
    tempOperation.add("name", operation.getAttribute("name"));
    tempOperation.add("visibility", operation.getAttribute("visibility"));
    tempOperation.add("type", getType(operation));
    return tempOperation.render();
  }
  
  /**
   * 
   * @param feature
   * @return
   */
  private String getType (mxCell feature) {
    String type          = feature.getAttribute("type");
    String collection    = feature.getAttribute("collection");
    
    StringBuilder result = new StringBuilder();
    
    mxCell typeCell      = (mxCell) model.getCell(type);
    if (typeCell != null) {
      type = typeCell.getAttribute("name"); 
    }
    
    if (GenericUtils.isEmptyString(collection)) {
      collection = getCollection(collection);
      
    }
    else {
      result.append(type);
    }
    
    return result.toString();
  }
  
  private String getCollection (String collection) {
    return null;
  }
  
  /**
   * 
   * @param packageId
   * @return
   */
  private String getPackage (String packageId) {
    mxCell packageCell = (mxCell) model.getCell(packageId);
    if (packageCell == null) {
      return "";
    }
    return "package " + packageCell.getAttribute("name") + ";";
  }
  
  /**
   * 
   * @param classifier
   * @return
   */
  private List<mxCell> getProperties (mxCell classifier) {
    List<mxCell> properties = new ArrayList<mxCell>();
    
    for (int i = 0; i < classifier.getChildCount(); i++) {
      mxCell child = (mxCell) classifier.getChildAt(i);
      if (GenericUtils.equals(child.getAttribute("attribute"), "1")) {
        for (int j = 0; j < child.getChildCount(); j++) {
          properties.add((mxCell) child.getChildAt(j));
        }
        break;
      }
    }
    
    if (isClass(classifier)) {
      mxCell edge;
      mxCell property;
      
      for (int i = 0; i < classifier.getEdgeCount(); i++) {
        edge = (mxCell) classifier.getEdgeAt(i);
        
        if (!isAssociation(edge)) {
          continue;
        }
        
        for (int j = 0; j < edge.getChildCount(); j++) {
          property = (mxCell) edge.getChildAt(j);
          if (!GenericUtils.equals(property.getAttribute("type"), classifier.getId())) {
            properties.add(property);
          }
        }
      }
    }
    
    return properties;
  }
  
  /**
   * 
   * @param classifier
   * @return
   */
  private List<mxCell> getLiterals (mxCell enumerationCell) {
    List<mxCell> attributes = new ArrayList<mxCell>();
    
    for (int i = 0; i < enumerationCell.getChildCount(); i++) {
      mxCell child = (mxCell) enumerationCell.getChildAt(i);
      if (GenericUtils.equals(child.getAttribute("attribute"), "1")) {
        for (int j = 0; j < child.getChildCount(); j++) {
          attributes.add((mxCell) child.getChildAt(j));
        }
        break;
      }
    }
    
    return attributes;
  }
  
  /**
   * 
   * @param classifier
   * @return
   */
  private List<mxCell> getOperations (mxCell classifier) {
    List<mxCell> operations = new ArrayList<mxCell>();
    
    for (int i = 0; i < classifier.getChildCount(); i++) {
      mxCell child = (mxCell) classifier.getChildAt(i);
      if (GenericUtils.equals(child.getAttribute("attribute"), "0")) {
        for (int j = 0; j < child.getChildCount(); j++) {
          operations.add((mxCell) child.getChildAt(j));
        }
        break;
      }
    }
    
    return operations;
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
  
  /**
   * Checks if the given cell contains a UML Association.
   * 
   * @param cell
   * @return
   * @author Gabriel Leonardo Diaz, 17.03.2014.
   */
  private boolean isAssociation (mxCell cell) {
    if (cell == null || !(cell.getValue() instanceof Element)) {
      return false;
    }
    return ((Element) cell.getValue()).getTagName().equalsIgnoreCase("association");
  }
  
}
