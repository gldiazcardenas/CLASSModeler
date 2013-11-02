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
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.testng.annotations.Test;

/**
 * Unit test to verify the code generation using UML2 Framework.
 * 
 * @author Gabriel Leonardo Diaz, 03.10.2013.
 */
public class UMLCodeGeneration {
  
  protected Model createModel(String name) {
    Model model = UMLFactory.eINSTANCE.createModel();
    model.setName(name);
    System.out.println("Model '" + model.getQualifiedName() + "' created.");
    return model;
  }
  
  protected org.eclipse.uml2.uml.Package createPackage(Package nestingPackage, String name) {
    Package package_ = nestingPackage.createNestedPackage(name);
    System.out.println("Package '" + package_.getQualifiedName() + "' created.");
    return package_;
  }
  
  protected static void save(org.eclipse.uml2.uml.Package package_, URI uri) {
    Resource resource = new ResourceSetImpl().createResource(uri);
    resource.getContents().add(package_);
    
    try {
      resource.save(null);
      System.out.println("Done.");
    }
    catch (IOException ioe) {
      System.out.println("Error : " + ioe.getMessage());
    }
  }
  
  @Test
  public void testGenerateCode () {
    Model model = createModel("MyFistModel");
    Package myPackage = createPackage(model, "MyFirstPackage");
    myPackage.createOwnedClass("MyFirstClass", false);
    
    URI uri = URI.createFileURI("C:/").appendSegment("MyFirstModel").appendFileExtension(UMLResource.FILE_EXTENSION);
    
    save(model, uri);
  }
  
}
