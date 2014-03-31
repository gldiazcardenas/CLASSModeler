/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.test.service;

import javax.naming.NamingException;

import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.VisibilityKind;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import classmodeler.domain.uml.types.java.JavaTypes;
import classmodeler.domain.user.Guest;
import classmodeler.service.CodeGenerationService;
import classmodeler.service.implementation.CodeGenerationServiceBean;

/**
 * Test class to verify the correct operation of code generation service.
 * 
 * @author Gabriel Leonardo Diaz, 24.03.2014.
 */
@Test
public class CodeGenerationServiceTest extends ServiceTest {
  
  private CodeGenerationService generateCodeService;
  
  @Override
  @BeforeClass
  public void configure() throws NamingException {
    generateCodeService = (CodeGenerationService) context.lookup(getServiceObjectName(CodeGenerationServiceBean.class.getSimpleName()));
    generateCodeService.configure(new Guest());
  }
  
  @Override
  @AfterClass
  public void destroy() {
    // Do nothing
  }
  
  /**
   * 
   */
  public void generateClassTest () {
    
  }
  
  /**
   * Unit test that verifies the code generated for an enumeration UML.
   * 
   * @author Gabriel Leonardo Diaz, 24.03.2014.
   */
  public void generateEnumerationTest () {
    Enumeration aEnumeration = UMLFactory.eINSTANCE.createEnumeration();
    aEnumeration.setName("Numeros");
    aEnumeration.setVisibility(VisibilityKind.PUBLIC_LITERAL);
    aEnumeration.createOwnedLiteral("UNO");
    aEnumeration.createOwnedLiteral("DOS");
    aEnumeration.createOwnedLiteral("TRES");
    
    String file = generateCodeService.generateEnumeration(aEnumeration);
    
    assert (file.contains("public enum Numeros")) : "The enum declaration is invalid.";
    assert (file.contains("@author")) : "The file does not contain an author.";
    assert (file.contains("UNO,")) : "The literal 'UNO' was not generated.";
    assert (file.contains("DOS,")) : "The literal 'DOS' was not generated.";
    assert (file.contains("TRES")) : "The literal 'TRES' was not generated.";
    assert (!file.contains("package")) : "The file contains a 'package' sentence, which should not be present.";
    
    Package aPackage = UMLFactory.eINSTANCE.createPackage();
    aPackage.setName("test");
    aEnumeration.setPackage(aPackage);
    
    file = generateCodeService.generateEnumeration(aEnumeration);
    
    assert (file.contains("package test;")) : "The file does not contain a 'package' sentence, it must exist.";
  }
  
  /**
   * Unit test that verifies the code generated for an Interface UML.
   * 
   * @author Gabriel Leonardo Diaz, 24.03.2014.
   */
  public void generateInterfaceTest () {
    Interface aInterface = UMLFactory.eINSTANCE.createInterface();
    aInterface.setName("ReadableAndWriteable");
    aInterface.setVisibility(VisibilityKind.PUBLIC_LITERAL);
    
    Property aProperty = aInterface.createOwnedAttribute("MAX_COUNT", JavaTypes.PRIMITIVE_INT);
    aProperty.setIsStatic(true);
    aProperty.setIsLeaf(true);
    aProperty.setVisibility(VisibilityKind.PUBLIC_LITERAL);
    aProperty.setDefault("DefaultValue");
    
    String file = generateCodeService.generateInterface(aInterface);
    
    assert (file.contains("public interface ReadableAndWriteable")) : "The interface declaration is invalid.";
    assert (file.contains("@author")) : "The file does not contain an author.";
    
    Package aPackage = UMLFactory.eINSTANCE.createPackage();
    aPackage.setName("test");
    aInterface.setPackage(aPackage);
    
    file = generateCodeService.generateInterface(aInterface);
    
    assert (file.contains("package test;")) : "The file does not contain a 'package' sentence, it must exist.";
  }
  
}
