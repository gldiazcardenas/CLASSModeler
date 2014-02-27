/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.uml.test;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;
import org.testng.annotations.Test;

/**
 * Unit test to verify the code generation using UML2 Framework.
 * 
 * @author Gabriel Leonardo Diaz, 03.10.2013.
 */
public class CodeGenerationTest {
  
  public static final Model UML_MODEL    = UMLFactory.eINSTANCE.createModel();
  public static final PrimitiveType INT  = UML_MODEL.createOwnedPrimitiveType("int");
  public static final PrimitiveType LONG = UML_MODEL.createOwnedPrimitiveType("long");
  
  @Test
  public void testGenerateCode () {
    Model model = UMLFactory.eINSTANCE.createModel();
    model.setName("UMLClassDiagram");
    
    Package defaultPackage = model.createNestedPackage("default");
    
    Class personClass = defaultPackage.createOwnedClass("Person", false);
    personClass.createOwnedAttribute("name", INT);
    
    URI uri = URI.createFileURI("C:/").appendSegment("MyFirstModelfdsf").appendFileExtension(UMLResource.FILE_EXTENSION);
    
    ResourceSet resourceSet = new ResourceSetImpl();
    UMLResourcesUtil.init(resourceSet);
    Resource resource = resourceSet.createResource(uri);
    resource.getContents().add(model);
    
    try {
      resource.save(null);
      System.out.println("Done.");
    }
    catch (IOException ioe) {
      System.out.println("Error : " + ioe.getMessage());
    }
  }
  
}
