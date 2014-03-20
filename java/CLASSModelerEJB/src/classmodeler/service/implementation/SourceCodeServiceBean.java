/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.implementation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import org.stringtemplate.v4.DateRenderer;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;
import org.w3c.dom.Element;

import classmodeler.domain.code.SourceCodeFile;
import classmodeler.domain.share.SharedDiagram;
import classmodeler.domain.user.User;
import classmodeler.service.SourceCodeService;
import classmodeler.service.util.CollectionUtils;
import classmodeler.service.util.GenericUtils;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;

/**
 * Implementation of the file service.
 * 
 * @author Gabriel Leonardo Diaz, 05.03.2014.
 */
public @Stateless class SourceCodeServiceBean implements SourceCodeService {
  
  public static final STGroupFile TEMPLATES = new STGroupFile("classmodeler/domain/code/sourcecode.java.stg");
  
  static {
    DateRenderer dateRender = new DateRenderer();
    TEMPLATES.registerRenderer(Date.class, dateRender);
    TEMPLATES.registerRenderer(Calendar.class, dateRender);
  }
  
  private mxGraphModel model;
  private User user;
  private Date now;
  
  @Override
  public List<SourceCodeFile> generateCode(User user, SharedDiagram diagram) {
    List<SourceCodeFile> sourceCodeFiles = new ArrayList<SourceCodeFile>();
    
    this.model = diagram.getModel();
    this.user  = user;
    this.now   = Calendar.getInstance().getTime();
    
    mxCell mxCell;
    SourceCodeFile file;
    
    for (Object cell : this.model.getCells().values()) {
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
    ST tempClass = TEMPLATES.getInstanceOf("class");
    
    tempClass.add("name", classUML.getAttribute("name"));
    tempClass.add("visibility", classUML.getAttribute("visibility"));
    tempClass.add("package", getPackage(classUML.getAttribute("package")));
    tempClass.add("author", getUserName(user));
    tempClass.add("date", now);
    
    List<String> imports    = new ArrayList<String>();
    
    List<String> properties = new ArrayList<String>();
    for (mxCell property : getProperties(classUML)) {
      properties.add(generateProperty(property, imports));
    }
    
    List<String> operations = new ArrayList<String>();
    for (mxCell operation : getOperations(classUML)) {
      operations.add(generateOperation(operation, imports));
    }
    
    List<String> extended = new ArrayList<String>();
    for (mxCell extend : getExtendedClassifiers(classUML)) {
      extended.add(extend.getAttribute("name"));
    }
    
    List<String> implemented = new ArrayList<String>();
    for (mxCell implement : getImplementedInterfaces(classUML)) {
      implemented.add(implement.getAttribute("name"));
    }
    
    tempClass.add("imports", parseOptionalList(imports));
    tempClass.add("extend", parseOptionalList(extended));
    tempClass.add("interfaces", parseOptionalList(implemented));
    tempClass.add("properties", parseOptionalList(properties));
    tempClass.add("operations", parseOptionalList(operations));
    
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
    tempInterface.add("author", getUserName(user));
    tempInterface.add("date", now);
    
    List<String> imports    = new ArrayList<String>();
    
    List<String> properties = new ArrayList<String>();
    for (mxCell property : getProperties(interfaceUML)) {
      properties.add(generateProperty(property, imports));
    }
    
    List<String> operations = new ArrayList<String>();
    for (mxCell operation : getOperations(interfaceUML)) {
      operations.add(generateOperation(operation, imports));
    }
    
    List<String> extended = new ArrayList<String>();
    for (mxCell extend : getExtendedClassifiers(interfaceUML)) {
      extended.add(extend.getAttribute("name"));
    }
    
    tempInterface.add("imports", parseOptionalList(imports));
    tempInterface.add("extends", parseOptionalList(extended));
    tempInterface.add("properties", parseOptionalList(properties));
    tempInterface.add("operations", parseOptionalList(operations));
    
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
    tempEnum.add("author", getUserName(user));
    tempEnum.add("date", now);
    
    List<String> literals = new ArrayList<String>();
    mxCell literalCell;
    for (Object child : getLiterals(enumerationCell)) {
      literalCell = (mxCell) child;
      literals.add(literalCell.getAttribute("name"));
    }
    
    tempEnum.add("literals", parseOptionalList(literals));
    
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
  private String generateProperty (mxCell property, List<String> imports) {
    ST tempProperty = TEMPLATES.getInstanceOf("property");
    tempProperty.add("name", property.getAttribute("name"));
    tempProperty.add("visibility", property.getAttribute("visibility"));
    tempProperty.add("type", getType(property));
    
    List<String> modifiers = new ArrayList<String>();
    if ("1".equals(property.getAttribute("static"))) {
      modifiers.add("static");
    }
    
    if ("1".equals(property.getAttribute("final"))) {
      modifiers.add("final");
    }
    
    tempProperty.add("modifiers", parseOptionalList(modifiers));
    tempProperty.add("value", parseOptionalValue(property.getAttribute("value")));
    
    return tempProperty.render();
  }
  
  /**
   * 
   * @param operation
   * @return
   */
  private String generateOperation (mxCell operation, List<String> imports) {
    ST tempOperation = TEMPLATES.getInstanceOf("operation");
    tempOperation.add("name", operation.getAttribute("name"));
    tempOperation.add("visibility", operation.getAttribute("visibility"));
    tempOperation.add("type", parseOptionalValue(getType(operation)));
    
    List<String> modifiers = new ArrayList<String>();
    if ("1".equals(operation.getAttribute("synchronized"))) {
      modifiers.add("synchonized");
    }
    
    if ("1".equals(operation.getAttribute("static"))) {
      modifiers.add("static");
    }
    
    if ("1".equals(operation.getAttribute("final"))) {
      modifiers.add("final");
    }
    
    tempOperation.add("modifiers", parseOptionalList(modifiers));
    
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
    
    if (type == null) {
      type = "";
    }
    
    mxCell typeCell      = (mxCell) model.getCell(type);
    if (typeCell != null) {
      type = typeCell.getAttribute("name"); 
    }
    
    if (!GenericUtils.isEmptyString(collection)) {
      if (collection.equals("array")) {
        result.append("[]").append(type);
      }
      else {
        result.append(getCollection(collection)).append("<").append(type).append(">");
      }
    }
    else {
      result.append(type);
    }
    
    return result.toString();
  }
  
  /**
   * 
   * @param user
   * @return
   */
  private String getUserName (User user) {
    if (!user.isRegisteredUser()) {
      GenericUtils.getLocalizedMessage(user.getName());
    }
    return user.getName();
  }
  
  /**
   * 
   * @param value
   * @return
   */
  private String parseOptionalValue (String value) {
    if (GenericUtils.isEmptyString(value)) {
      return null;
    }
    return value;
  }
  
  /**
   * 
   * @param values
   * @return
   */
  private List<String> parseOptionalList (List<String> values) {
    if (CollectionUtils.isEmptyCollection(values)) {
      return null;
    }
    return values;
  }
  
  /**
   * 
   * @param collection
   * @return
   */
  private String getCollection (String collection) {
    if ("arraylist".equals(collection)) {
      return "ArrayList";
    }
    else if ("collection".equals(collection)) {
      return "Collection";
    }
    else if ("enumset".equals(collection)) {
      return "EnumSet";
    }
    else if ("hashset".equals(collection)) {
      return "HashSet";
    }
    else if ("list".equals(collection)) {
      return "List";
    }
    else if ("linkedhset".equals(collection)) {
      return "LinkedHashSet";
    }
    else if ("linkedlist".equals(collection)) {
      return "LinkedList";
    }
    else if ("prioriqueue".equals(collection)) {
      return "PriorityQueue";
    }
    else if ("queue".equals(collection)) {
      return "Queue";
    }
    else if ("set".equals(collection)) {
      return "Set";
    }
    else if ("sortedset".equals(collection)) {
      return "SortedSet";
    }
    else if ("stack".equals(collection)) {
      return "Stack";
    }
    else if ("treeset".equals(collection)) {
      return "TreeSet";
    }
    else if ("vector".equals(collection)) {
      return "Vector";
    }
    return collection;
  }
  
  /**
   * 
   * @param packageId
   * @return
   */
  private String getPackage (String packageId) {
    mxCell packageCell = (mxCell) model.getCell(packageId);
    if (packageCell == null) {
      return null;
    }
    return packageCell.getAttribute("name");
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
   * 
   * @param classifier
   * @return
   */
  private List<mxCell> getExtendedClassifiers (mxCell classifier) {
    List<mxCell> extended = new ArrayList<mxCell>();
    
    for (int i = 0; i < classifier.getEdgeCount(); i++) {
      mxCell edge = (mxCell) classifier.getEdgeAt(i);
      if (isGeneralization(edge) && classifier.equals(edge.getSource())) {
        extended.add((mxCell) edge.getTarget());
      }
    }
    
    return extended;
  }
  
  /**
   * 
   * @param classifier
   * @return
   */
  private List<mxCell> getImplementedInterfaces (mxCell classifier) {
    List<mxCell> implemented = new ArrayList<mxCell>();
    
    for (int i = 0; i < classifier.getEdgeCount(); i++) {
      mxCell edge = (mxCell) classifier.getEdgeAt(i);
      if (isRealization(edge) && classifier.equals(edge.getSource())) {
        implemented.add((mxCell) edge.getTarget());
      }
    }
    
    return implemented;
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
  
  /**
   * Checks if the given cell contains a UML Generalization.
   * 
   * @param cell
   * @return
   * @author Gabriel Leonardo Diaz, 19.03.2014.
   */
  private boolean isGeneralization (mxCell cell) {
    if (cell == null || !(cell.getValue() instanceof Element)) {
      return false;
    }
    return ((Element) cell.getValue()).getTagName().equalsIgnoreCase("generalization");
  }
  
  /**
   * Checks if the given cell contains a UML Realization.
   * 
   * @param cell
   * @return
   * @author Gabriel Leonardo Diaz, 19.03.2014.
   */
  private boolean isRealization (mxCell cell) {
    if (cell == null || !(cell.getValue() instanceof Element)) {
      return false;
    }
    return ((Element) cell.getValue()).getTagName().equalsIgnoreCase("realization");
  }
  
}
