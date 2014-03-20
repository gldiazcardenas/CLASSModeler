/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.test.stringtemplate;

import static classmodeler.service.implementation.SourceCodeServiceBean.TEMPLATES;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.stringtemplate.v4.ST;
import org.testng.annotations.Test;

/**
 * Class that contains Unit Tests for StringTemplate library, this allows to
 * understand the operation of the library and as training for the CLASSModeler
 * code generation tool.
 * 
 * @author Gabriel Leonardo Diaz, 26.02.2014.
 */
public class TemplateCodeTest {
  
  private static final String AUTHOR = "Gabriel Diaz";
  private static final Date NOW = Calendar.getInstance().getTime();
  
  
  /**
   * Unit test that generates a class file, this tries to check the result by
   * doing some basic text validations.
   * 
   * @author Gabriel Leonardo Diaz, 18.03.2014.
   */
  @Test
  public void generateClass () {
    
    
  }
  
  /**
   * Unit test that generates an enumeration file.
   * 
   * @author Gabriel Leonardo Diaz, 18.03.2014.
   */
  @Test
  public void generateEnumeration () {
    ST enumeration = TEMPLATES.getInstanceOf("enumeration");
    
    List<String> literals = Arrays.asList("UNO", "DOS", "TRES");
    
    enumeration.add("name", "Numeros");
    enumeration.add("visibility", "public");
    enumeration.add("literals", literals);
    enumeration.add("author", AUTHOR);
    enumeration.add("date", NOW);
    
    String file = enumeration.render();
    
    assert (file.contains("public enum Numeros")) : "The enum declaration is invalid.";
    assert (file.contains("@author")) : "The file does not contain an author.";
    assert (file.contains("UNO,")) : "The literal 'UNO' was not generated.";
    assert (file.contains("DOS,")) : "The literal 'DOS' was not generated.";
    assert (file.contains("TRES")) : "The literal 'TRES' was not generated.";
    assert (!file.contains("package")) : "The file contains a 'package' sentence, which should not be present.";
    
    enumeration.add("package", "test");
    file = enumeration.render();
    
    assert (file.contains("package")) : "The file does not contain a 'package' sentence, it must exist.";
  }
  
  /**
   * Unit test that generates an interface file.
   * 
   * @author Gabriel Leonardo Diaz, 18.03.2014.
   */
  @Test
  public void generateInterface () {
    ST aInterface = TEMPLATES.getInstanceOf("interface");
    
    aInterface.add("name", "ReadableAndWritable");
    aInterface.add("visibility", "public");
    aInterface.add("author", AUTHOR);
    aInterface.add("date", NOW);
    
    String file = aInterface.render();
    
    assert (file.contains("public interface ReadableAndWritable")) : "The interface declaration is invalid.";
    assert (file.contains("@author")) : "The file does not contain an author.";
    assert (!file.contains("package")) : "The file contains a 'package' sentence, which should not be present.";
    
    aInterface.add("package", "test");
    file = aInterface.render();
    
    assert (file.contains("package")) : "The file does not contain a 'package' sentence, it must exist.";
    
    List<String> extendedInterfaces = Arrays.asList("Readable", "Writable");
    aInterface.add("extends", extendedInterfaces);
    file = aInterface.render();
    System.out.println(file);
    
    assert (file.contains("extends Readable, Writable")) : "The exnteded interfaces were not added.";
  }
  
}
