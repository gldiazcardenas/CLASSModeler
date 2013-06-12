/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.util;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.VisibilityKind;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

/**
 * Class used to implement some test for the Plugin UML2 of Eclipse Tools.
 * 
 * @author Gabriel Leonardo Diaz, 11.06.2013.
 */
public class UML2Tests {
  
  public static void main (String[] args) {
    Model model = UMLFactory.eINSTANCE.createModel();
    model.setName("My Project Name");
    model.setVisibility(VisibilityKind.PUBLIC_LITERAL);
    
    Package subPackage = model.createNestedPackage("My SubPackage");
    subPackage.createEAnnotation("This package is a sub package of the Model");
    model.createOwnedClass("My Class In the model", true);
    
    ResourceSet resourceSet = new ResourceSetImpl();
    UMLResourcesUtil.init(resourceSet);
    Resource resource = resourceSet.createResource(URI.createFileURI("C:/").appendSegment("myFile").appendFileExtension(UMLResource.FILE_EXTENSION));
    resource.getContents().add(model);
    
    try {
        resource.save(null);
    }
    catch (Exception ioe) {
      System.out.println(ioe.getMessage());
    }
  }

}
