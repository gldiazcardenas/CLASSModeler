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
import java.util.Map.Entry;

import org.eclipse.uml2.uml.CallConcurrencyKind;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.VisibilityKind;
import org.w3c.dom.Element;

import classmodeler.domain.code.SourceCodeFile;
import classmodeler.service.util.GenericUtils;
import classmodeler.web.beans.SharedDiagram;

import com.mxgraph.model.mxCell;

/**
 * Converter class that allows to parse a class diagram from XML representation
 * to UML Metamodel.
 * 
 * @author Gabriel Leonardo Diaz, 23.03.2014.
 */
public final class UMLConverter {
  
  private Map<String, Package> packages         = new HashMap<String, Package>();
  private Map<String, Classifier> classifiers   = new HashMap<String, Classifier>();
  
  private Model umlModel;
  private SharedDiagram diagram;
  
  /**
   * Constructs the UML metamodel converter by using the given XML model
   * representation.
   * 
   * @param mxModel
   * @author Gabriel Leonardo Diaz, 25.03.2014.
   */
  public UMLConverter (SharedDiagram diagram) {
    super();
    this.diagram = diagram;
  }
  
  /**
   * Gets the diagram which is being transformed to UML metamodel.
   * 
   * @return
   * @author Gabriel Leonardo Diaz, 26.03.2014.
   */
  public SharedDiagram getDiagram() {
    return diagram;
  }
  
  /**
   * Clears the converter and prepares it for a new execution.
   * 
   * @author Gabriel Leonardo Diaz, 25.03.2014.
   */
  private void initialize () {
    this.packages.clear();
    this.classifiers.clear();
    this.umlModel = UMLFactory.eINSTANCE.createModel();
    this.umlModel.setName(diagram.getName());
  }
  
  /**
   * Generates the UML model from the diagram created.
   * 
   * @param diagram
   * @return
   * @author Gabriel Leonardo Diaz, 26.03.2014.
   */
  public Model execute () {
    initialize();
    
    mxCell mxCell;
    
    // 1. Generate empty types
    for (Object cell : diagram.getModel().getCells().values()) {
      mxCell  = (mxCell) cell;
      if (mxCell.equals(diagram.getModel().getRoot())) {
        continue;
      }
      
      if (!mxCell.isVertex()) {
        continue;
      }
      
      if (isPackage(mxCell)) {
        generatePackage(mxCell);
      }
      else if (isClass(mxCell)) {
        generateClass(mxCell);
      }
      else if (isInterface(mxCell)) {
        generateInteface(mxCell);
      }
      else if (isEnumeration(mxCell)) {
        generateEnumeration(mxCell);
      }
    }
    
    
    mxCell edge;
    Classifier classifier;
    
    // 2. Generate other objects
    for (Entry<String, Classifier> entry : this.classifiers.entrySet()) {
      mxCell = (mxCell) diagram.getModel().getCell(entry.getKey());
      classifier = entry.getValue();
      
      // 2.1 Properties
      for (mxCell property : getProperties(mxCell)) {
        if (classifier instanceof Enumeration) {
          generateLiteral((Enumeration) classifier, property);
        }
        else {
          generateProperty(classifier, property);
        }
      }
      
      // 2.2 Operations
      for (mxCell operation : getOperations(mxCell)) {
        generateOperation(classifier, operation);
      }
      
      // 2.3 Relationships
      if (mxCell.getEdgeCount() > 0) {
        for (int i = 0; i < mxCell.getEdgeCount(); i++) {
          edge = (com.mxgraph.model.mxCell) mxCell.getEdgeAt(i);
          
          if (isGeneralization(edge)) {
            // TODO
          }
          else if (isRealization(mxCell)) {
            // TODO
          }
        }
      }
    }
    
    return this.umlModel;
  }
  
  /**
   * Generates the UML Class Metamodel from the given XML class representation.
   * 
   * @param classCell
   * @return
   * @author Gabriel Leonardo Diaz, 05.03.2014.
   */
  private Class generateClass (mxCell classCell) {
    String name               = classCell.getAttribute("name");
    Package aPackage          = getPackage(classCell.getAttribute("package"));
    VisibilityKind visibility = parseVisibility(classCell.getAttribute("visibility"));
    boolean isStatic          = parseBoolean(classCell.getAttribute("static"));
    boolean isAbstract        = parseBoolean(classCell.getAttribute("abstract"));
    boolean isFinal           = parseBoolean(classCell.getAttribute("final"));
    
    Class aClass = UMLFactory.eINSTANCE.createClass();
    aClass.setName(name);
    aClass.setIsAbstract(isAbstract);
    aClass.setVisibility(visibility);
    aClass.setIsLeaf(isStatic);
    aClass.setIsFinalSpecialization(isFinal);
    aClass.setPackage(aPackage);
    
    this.classifiers.put(classCell.getId(), aClass);
    
    return aClass;
  }
  
  /**
   * Generates the UML Inteface metamodel from the given XML interface
   * representation.
   * 
   * @param interfaceCell
   * @return
   * @author Gabriel Leonardo Diaz, 05.03.2014.
   */
  private Interface generateInteface (mxCell interfaceCell) {
    String name               = interfaceCell.getAttribute("name");
    Package aPackage          = getPackage(interfaceCell.getAttribute("package"));
    VisibilityKind visibility = parseVisibility(interfaceCell.getAttribute("visibility"));
    
    Interface aInterface = UMLFactory.eINSTANCE.createInterface();
    aInterface.setName(name);
    aInterface.setVisibility(visibility);
    aInterface.setPackage(aPackage);
    
    this.classifiers.put(interfaceCell.getId(), aInterface);
    
    return aInterface;
  }
  
  /**
   * Generates the UML Enumeration metamodel from the given XML enumeration.
   * 
   * @param enumerationCell
   * @return
   * @author Gabriel Leonardo Diaz, 05.03.2014.
   */
  private Enumeration generateEnumeration(mxCell enumerationCell) {
    String name               = enumerationCell.getAttribute("name");
    Package aPackage          = getPackage(enumerationCell.getAttribute("package"));
    VisibilityKind visibility = parseVisibility(enumerationCell.getAttribute("visibility"));
    
    Enumeration aEnumeration = UMLFactory.eINSTANCE.createEnumeration();
    aEnumeration.setName(name);
    aEnumeration.setVisibility(visibility);
    aEnumeration.setPackage(aPackage);
    
    this.classifiers.put(enumerationCell.getId(), aEnumeration);
    
    return aEnumeration;
  }
  
  /**
   * Generates a UML package element with the given cell
   * 
   * @param packageCell
   *          The cell representing a package.
   * @return The new package element.
   * @author Gabriel Leonardo Diaz, 25.03.2014.
   */
  private Package generatePackage (mxCell packageCell) {
    Package aPackage = umlModel.createNestedPackage(packageCell.getAttribute("name"));
    this.packages.put(packageCell.getId(), aPackage);
    return aPackage;
  }
  
  /**
   * Generates a UML Enumeration Literal element with the given cell.
   * 
   * @param enumeration
   * @param literalCell
   * @return The new enumeration literal, this was already added as owned
   *         element of the given enumeration parameter.
   * @author Gabriel Leonardo Diaz, 26.03.2014.
   */
  private EnumerationLiteral generateLiteral (Enumeration enumeration, mxCell literalCell) {
    return enumeration.createOwnedLiteral(literalCell.getAttribute("name"));
  }
  
  /**
   * 
   * @param classifier
   * @param propertyCell
   * @return
   */
  private Property generateProperty (Classifier classifier, mxCell propertyCell) {
    Property property = null;
    
    String name               = propertyCell.getAttribute("name");
    String defaultValue       = propertyCell.getAttribute("default");
    Type type                 = getType(propertyCell.getAttribute("type"), propertyCell.getAttribute("collection"));
    VisibilityKind visibility = parseVisibility(propertyCell.getAttribute("visibility"));
    boolean isStatic          = parseBoolean(propertyCell.getAttribute("static"));
    boolean isFinal           = parseBoolean(propertyCell.getAttribute("final"));
    
    if (classifier instanceof Class) {
      property = ((Class) classifier).createOwnedAttribute(name, type);
    }
    else if (classifier instanceof Interface) {
      property = ((Interface) classifier).createOwnedAttribute(name, type);
    }
    
    property.setVisibility(visibility);
    property.setIsStatic(isStatic);
    property.setIsLeaf(isFinal);
    property.setDefault(defaultValue);
    property.setUpper(UMLPackage.LITERAL_UNLIMITED_NATURAL___UNLIMITED_VALUE);
    
    return property;
  }
  
  /**
   * 
   * @param classifier
   * @param operationCell
   * @return
   */
  private Operation generateOperation (Classifier classifier, mxCell operationCell) {
    Operation operation = null;
    
    String name               = operationCell.getAttribute("name");
    Type type                 = getType(operationCell.getAttribute("type"), operationCell.getAttribute("collection"));
    VisibilityKind visibility = parseVisibility(operationCell.getAttribute("visibility"));
    boolean isStatic          = parseBoolean(operationCell.getAttribute("static"));
    boolean isFinal           = parseBoolean(operationCell.getAttribute("final"));
    boolean isSynchronized    = parseBoolean(operationCell.getAttribute("synchronized"));
    
    if (classifier instanceof Class) {
      operation = ((Class) classifier).createOwnedOperation(name, null, null);
    }
    else if (classifier instanceof Interface) {
      operation = ((Interface) classifier).createOwnedOperation(name, null, null);
    }
    
    operation.setType(type);
    operation.setVisibility(visibility);
    operation.setIsStatic(isStatic);
    operation.setIsLeaf(isFinal);
    
    if (isSynchronized) {
      operation.setConcurrency(CallConcurrencyKind.GUARDED_LITERAL);
    }
    
    return operation;
  }
  
  /**
   * Convenience method that creates the package if it does not exist.
   * 
   * @param packageId
   * @return
   * @author Gabriel Leonardo Diaz, 25.03.2014.
   */
  private Package getPackage (String packageId) {
    Package aPackage = this.packages.get(packageId);
    if (aPackage == null && !GenericUtils.isEmptyString(packageId)) {
      mxCell packageCell = (mxCell) diagram.getModel().getCell(packageId);
      if (packageCell != null) {
        aPackage = generatePackage(packageCell);
      }
    }
    return aPackage;
  }
  
  /**
   * Generates the type for the given id. This can be a primitive type, a
   * classifier, a generic collection or an anonymus type.
   * 
   * @param typeId
   * @return
   * @author Gabriel Leonardo Diaz, 26.03.2014.
   */
  private Type getType (String typeId, String collectionType) {
    return null;
  }
  
  /**
   * Creates a source code file with the given UML element.
   * 
   * @param element
   * @return
   * @author Gabriel Leonardo Diaz, 24.03.2014.
   */
  public static SourceCodeFile createSourceCodeFile (NamedElement element) {
    SourceCodeFile file = new SourceCodeFile();
    file.setName(element.getName());
    file.setElement(element);
    file.setFormat(SourceCodeFile.JAVA_FORMAT);
    return file;
  }
  
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
    if (cell == null || cell.isEdge() || !(cell.getValue() instanceof Element)) {
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
    if (cell == null || cell.isEdge() || !(cell.getValue() instanceof Element)) {
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
    if (cell == null || cell.isEdge() || !(cell.getValue() instanceof Element)) {
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
    if (cell == null || cell.isEdge() || !(cell.getValue() instanceof Element)) {
      return false;
    }
    return ((Element) cell.getValue()).getTagName().toLowerCase().equalsIgnoreCase("interface");
  }
  
  /**
   * Checks if the given cell contains a Comment.
   * 
   * @param cell
   * @return
   * @author Gabriel Leonardo Diaz, 25.03.2014.
   */
  public static boolean isComment (mxCell cell) {
    if (cell == null || cell.isEdge() || !(cell.getValue() instanceof Element)) {
      return false;
    }
    return ((Element) cell.getValue()).getTagName().toLowerCase().equalsIgnoreCase("comment");
  }
  
  /**
   * Checks if the given cell contains a Link Association.
   * 
   * @param cell
   * @return
   * @author Gabriel Leonardo Diaz, 17.03.2014.
   */
  public static boolean isLink (mxCell cell) {
    if (cell == null || cell.isVertex() || !(cell.getValue() instanceof Element)) {
      return false;
    }
    return ((Element) cell.getValue()).getTagName().toLowerCase().equals("link");
  }
  
  /**
   * Checks if the given cell contains a UML Association.
   * 
   * @param cell
   * @return
   * @author Gabriel Leonardo Diaz, 17.03.2014.
   */
  public static boolean isAssociation (mxCell cell) {
    if (cell == null || cell.isVertex() || !(cell.getValue() instanceof Element)) {
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
