/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.converters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.VisibilityKind;
import org.w3c.dom.Element;

import classmodeler.domain.code.SourceCodeFile;
import classmodeler.service.util.GenericUtils;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;

/**
 * Converter class that allows to parse a class diagram from XML representation
 * to UML Metamodel.
 * 
 * @author Gabriel Leonardo Diaz, 23.03.2014.
 */
public final class UMLConverter {
  
  protected Map<String, Package> packages         = new HashMap<String, Package>();
  protected Map<String, Class> classes            = new HashMap<String, Class>();
  protected Map<String, Interface> interfaces     = new HashMap<String, Interface>();
  protected Map<String, Enumeration> enumerations = new HashMap<String, Enumeration>();
  
  private Model umlModel;
  private mxGraphModel mxModel;
  private List<SourceCodeFile> sourceCodeFiles;
  
  public UMLConverter (mxGraphModel mxModel) {
    super();
    this.mxModel = mxModel;
    this.sourceCodeFiles = new ArrayList<SourceCodeFile>();
  }
  
  public List<SourceCodeFile> getSourceCodeFiles() {
    return sourceCodeFiles;
  }
  
  public Model getUmlModel() {
    return umlModel;
  }
  
  /**
   * 
   * @param diagram
   * @return
   */
  public void execute () {
    this.umlModel = UMLFactory.eINSTANCE.createModel();
    this.sourceCodeFiles.clear();
    
    mxCell mxCell;
    for (Object cell : mxModel.getCells().values()) {
      mxCell = (com.mxgraph.model.mxCell) cell;
      
      if (isClass(mxCell)) {
        generateClass(mxCell);
      }
      else if (isInterface(mxCell)) {
        generateInteface(mxCell);
      }
      else if (isEnumeration(mxCell)) {
        generateEnumeration(mxCell);
      }
    }
  }
  
  /**
   * Generates the UML Class Metamodel from the given XML class representation.
   * 
   * @param classUML
   * @return
   * @author Gabriel Leonardo Diaz, 05.03.2014.
   */
  private Class generateClass (mxCell classUML) {
    String name               = classUML.getAttribute("name");
    Package aPackage          = generatePackage(classUML.getAttribute("package"));
    VisibilityKind visibility = parseVisibility(classUML.getAttribute("visibility"));
    boolean isStatic          = parseBoolean(classUML.getAttribute("static"));
    boolean isAbstract        = parseBoolean(classUML.getAttribute("abstract"));
    boolean isFinal           = parseBoolean(classUML.getAttribute("final"));
    
    Class aClass = this.umlModel.createOwnedClass(name, isAbstract);
    aClass.setVisibility(visibility);
    aClass.setIsLeaf(isFinal);
    aClass.setIsFinalSpecialization(isStatic);
    aClass.setPackage(aPackage);
    
    for (mxCell property : getProperties(classUML)) {
      generateProperty(aClass, property);
    }
    
    for (mxCell operation : getOperations(classUML)) {
      generateOperation(aClass, operation);
    }
    
    return aClass;
  }
  
  /**
   * Generates the UML Inteface metamodel from the given XML interface
   * representation.
   * 
   * @param interfaceUML
   * @return
   * @author Gabriel Leonardo Diaz, 05.03.2014.
   */
  private Interface generateInteface (mxCell interfaceUML) {
    String name               = interfaceUML.getAttribute("name");
    VisibilityKind visibility = parseVisibility(interfaceUML.getAttribute("visibility"));
    Package aPackage          = generatePackage(interfaceUML.getAttribute("package"));
    
    Interface aInterface = this.umlModel.createOwnedInterface(name);
    aInterface.setVisibility(visibility);
    aInterface.setPackage(aPackage);
    
    for (mxCell property : getProperties(interfaceUML)) {
      generateProperty(aInterface, property);
    }
    
    for (mxCell operation : getOperations(interfaceUML)) {
      generateOperation(aInterface, operation);
    }
    
    return aInterface;
  }
  
  /**
   * Generates the source code of the given enumeration element.
   * 
   * @param enumerationCell
   * @return
   * @author Gabriel Leonardo Diaz, 05.03.2014.
   */
  private Enumeration generateEnumeration(mxCell enumerationCell) {
    String name               = enumerationCell.getAttribute("name");
    VisibilityKind visibility = parseVisibility(enumerationCell.getAttribute("visibility"));
    Package aPackage          = generatePackage(enumerationCell.getAttribute("package"));
    
    Enumeration aEnumeration = this.umlModel.createOwnedEnumeration(name);
    aEnumeration.setVisibility(visibility);
    aEnumeration.setPackage(aPackage);
    
    for (mxCell child : getLiterals(enumerationCell)) {
      generateLiteral(aEnumeration, child);
    }
    
    return aEnumeration;
  }
  
  private Package generatePackage (String name) {
    return null;
  }
  
  private EnumerationLiteral generateLiteral (Enumeration enumeration, mxCell literalCell) {
    return null;
  }
  
  private Property generateProperty (Classifier classifier, mxCell propertyCell) {
//  ST tempProperty = TEMPLATES.getInstanceOf("property");
//  tempProperty.add("name", property.getAttribute("name"));
//  tempProperty.add("visibility", property.getAttribute("visibility"));
//  tempProperty.add("type", getType(property));
//  
//  List<String> modifiers = new ArrayList<String>();
//  if ("1".equals(property.getAttribute("static"))) {
//    modifiers.add("static");
//  }
//  
//  if ("1".equals(property.getAttribute("final"))) {
//    modifiers.add("final");
//  }
//  
//  tempProperty.add("modifiers", parseOptionalList(modifiers));
//  tempProperty.add("value", parseOptionalValue(property.getAttribute("value")));
//  
//  return tempProperty.render();
    return null;
  }
  
  private Property generateOperation (Classifier classifier, mxCell operationCell) {
//  ST tempOperation = TEMPLATES.getInstanceOf("operation");
//  tempOperation.add("name", operation.getAttribute("name"));
//  tempOperation.add("visibility", operation.getAttribute("visibility"));
//  tempOperation.add("type", parseOptionalValue(getType(operation)));
//  
//  List<String> modifiers = new ArrayList<String>();
//  if ("1".equals(operation.getAttribute("synchronized"))) {
//    modifiers.add("synchonized");
//  }
//  
//  if ("1".equals(operation.getAttribute("static"))) {
//    modifiers.add("static");
//  }
//  
//  if ("1".equals(operation.getAttribute("final"))) {
//    modifiers.add("final");
//  }
//  
//  tempOperation.add("modifiers", parseOptionalList(modifiers));
//  
//  return tempOperation.render();
    return null;
  }
  
//  
//  /**
//   * 
//   * @param feature
//   * @return
//   */
//  private String getType (mxCell feature) {
//    String type          = feature.getAttribute("type");
//    String collection    = feature.getAttribute("collection");
//    StringBuilder result = new StringBuilder();
//    
//    if (type == null) {
//      type = "";
//    }
//    
//    mxCell typeCell      = (mxCell) model.getCell(type);
//    if (typeCell != null) {
//      type = typeCell.getAttribute("name"); 
//    }
//    
//    if (!GenericUtils.isEmptyString(collection)) {
//      if (collection.equals("array")) {
//        result.append("[]").append(type);
//      }
//      else {
//        result.append(getCollection(collection)).append("<").append(type).append(">");
//      }
//    }
//    else {
//      result.append(type);
//    }
//    
//    return result.toString();
//  }
  
//  /**
//   * 
//   * @param collection
//   * @return
//   */
//  private String getCollection (String collection) {
//    if ("arraylist".equals(collection)) {
//      return "ArrayList";
//    }
//    else if ("collection".equals(collection)) {
//      return "Collection";
//    }
//    else if ("enumset".equals(collection)) {
//      return "EnumSet";
//    }
//    else if ("hashset".equals(collection)) {
//      return "HashSet";
//    }
//    else if ("list".equals(collection)) {
//      return "List";
//    }
//    else if ("linkedhset".equals(collection)) {
//      return "LinkedHashSet";
//    }
//    else if ("linkedlist".equals(collection)) {
//      return "LinkedList";
//    }
//    else if ("prioriqueue".equals(collection)) {
//      return "PriorityQueue";
//    }
//    else if ("queue".equals(collection)) {
//      return "Queue";
//    }
//    else if ("set".equals(collection)) {
//      return "Set";
//    }
//    else if ("sortedset".equals(collection)) {
//      return "SortedSet";
//    }
//    else if ("stack".equals(collection)) {
//      return "Stack";
//    }
//    else if ("treeset".equals(collection)) {
//      return "TreeSet";
//    }
//    else if ("vector".equals(collection)) {
//      return "Vector";
//    }
//    return collection;
//  }
  
  /**
   * Gets the cells containing the properties of the given classifier.
   * 
   * @param classifier
   * @return
   * @author Gabriel Leonardo Diaz, 24.03.2014.
   */
  public static List<mxCell> getProperties (mxCell classifier) {
    List<mxCell> properties = new ArrayList<mxCell>();
    
    for (int i = 0; i < classifier.getChildCount(); i++) {
      mxCell child = (mxCell) classifier.getChildAt(i);
      if (parseBoolean(child.getAttribute("attribute"))) {
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
   * Gets the literals of the given enumeration.
   * 
   * @param classifier
   * @return
   * @author Gabriel Leonardo Diaz, 24.03.2014.
   */
  public static List<mxCell> getLiterals (mxCell enumerationCell) {
    List<mxCell> literals = new ArrayList<mxCell>();
    
    for (int i = 0; i < enumerationCell.getChildCount(); i++) {
      mxCell child = (mxCell) enumerationCell.getChildAt(i);
      if (parseBoolean(child.getAttribute("attribute"))) {
        for (int j = 0; j < child.getChildCount(); j++) {
          literals.add((mxCell) child.getChildAt(j));
        }
        break;
      }
    }
    
    return literals;
  }
  
  /**
   * Gets the operations of the given classifier.
   * 
   * @param classifier
   * @return
   * @author Gabriel Leonardo Diaz, 24.03.2014.
   */
  public static List<mxCell> getOperations (mxCell classifier) {
    List<mxCell> operations = new ArrayList<mxCell>();
    
    for (int i = 0; i < classifier.getChildCount(); i++) {
      mxCell child = (mxCell) classifier.getChildAt(i);
      if (!parseBoolean(child.getAttribute("attribute"))) {
        for (int j = 0; j < child.getChildCount(); j++) {
          operations.add((mxCell) child.getChildAt(j));
        }
        break;
      }
    }
    
    return operations;
  }
  
  /**
   * Parses the visibility kind from string.
   * 
   * @param visibility
   * @return
   */
  public static VisibilityKind parseVisibility (String visibility) {
    VisibilityKind kind = VisibilityKind.get(visibility);
    if (kind == null) {
      kind = VisibilityKind.PUBLIC_LITERAL;
    }
    return kind;
  }
  
  /**
   * Parses the string to a boolean value.
   * @param booleanValue
   * @return
   */
  public static boolean parseBoolean (String booleanValue) {
    return "1".equals(booleanValue);
  }
  
  /**
   * Checks if the given cell contains a UML Package.
   * 
   * @param cell
   * @return
   * @author Gabriel Leonardo Diaz, 24.03.2014.
   */
  public static boolean isPackage (mxCell cell) {
    if (cell == null || !(cell.getValue() instanceof Element)) {
      return false;
    }
    return ((Element) cell.getValue()).getTagName().toLowerCase().equalsIgnoreCase("package");
  }
  
  /**
   * Checks if the given cell contains a UML Class.
   * 
   * @param cell
   * @return
   * @author Gabriel Leonardo Diaz, 05.03.2014.
   */
  public static boolean isClass (mxCell cell) {
    if (cell == null || !(cell.getValue() instanceof Element)) {
      return false;
    }
    return ((Element) cell.getValue()).getTagName().toLowerCase().equalsIgnoreCase("class");
  }
  
  /**
   * Checks if the given cell contains a UML Enumeration.
   * 
   * @param cell
   * @return
   * @author Gabriel Leonardo Diaz, 05.03.2014.
   */
  public static boolean isEnumeration (mxCell cell) {
    if (cell == null || !(cell.getValue() instanceof Element)) {
      return false;
    }
    return ((Element) cell.getValue()).getTagName().toLowerCase().equalsIgnoreCase("enumeration");
  }
  
  /**
   * Checks if the given cell contains a UML Interface.
   * 
   * @param cell
   * @return
   * @author Gabriel Leonardo Diaz, 05.03.2014.
   */
  public static boolean isInterface (mxCell cell) {
    if (cell == null || !(cell.getValue() instanceof Element)) {
      return false;
    }
    return ((Element) cell.getValue()).getTagName().toLowerCase().equalsIgnoreCase("interface");
  }
  
  /**
   * Checks if the given cell contains a UML Association.
   * 
   * @param cell
   * @return
   * @author Gabriel Leonardo Diaz, 17.03.2014.
   */
  public static boolean isAssociation (mxCell cell) {
    if (cell == null || !(cell.getValue() instanceof Element)) {
      return false;
    }
    return ((Element) cell.getValue()).getTagName().toLowerCase().equals("association");
  }
  
  /**
   * Checks if the given cell contains a UML Generalization.
   * 
   * @param cell
   * @return
   * @author Gabriel Leonardo Diaz, 19.03.2014.
   */
  public static boolean isGeneralization (mxCell cell) {
    if (cell == null || !(cell.getValue() instanceof Element)) {
      return false;
    }
    return ((Element) cell.getValue()).getTagName().toLowerCase().equalsIgnoreCase("generalization");
  }
  
  /**
   * Checks if the given cell contains a UML Realization.
   * 
   * @param cell
   * @return
   * @author Gabriel Leonardo Diaz, 19.03.2014.
   */
  public static boolean isRealization (mxCell cell) {
    if (cell == null || !(cell.getValue() instanceof Element)) {
      return false;
    }
    return ((Element) cell.getValue()).getTagName().toLowerCase().equalsIgnoreCase("realization");
  }
  
}
