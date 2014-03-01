/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.stringtemplate.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import org.testng.annotations.Test;

/**
 * Test class for StringTemplate plugin.
 *
 * @author Gabriel Leonardo Diaz, 26.02.2014.
 */
public class TemplateCodeTest {
  
  @Test
  public void generateCodeTemplate () {
    STGroup g = new STGroupFile("classmodeler/stringtemplate/test/templates.stg");
    
    // Class
    ST aClass = g.getInstanceOf("class");
    aClass.add("visibility", "protected");
    aClass.add("name", "Person");
    System.out.println(aClass.render());
    
    // Interface
    ST aInterface = g.getInstanceOf("interface");
    aInterface.add("visibility", "public");
    aInterface.add("name", "Printable");
    System.out.println(aInterface.render());
    
    // Enumeration
    ST aEnumeration = g.getInstanceOf("enumeration");
    aEnumeration.add("visibility", "public");
    aEnumeration.add("name", "Gender");
    System.out.println(aEnumeration.render());
    
    try {
      // input file 
//      FileInputStream in = new FileInputStream("D:/sometxt.txt");
      
      // out put file 
      ZipOutputStream out = new ZipOutputStream(new FileOutputStream("D:/tmp.zip"));
      
      // name the file inside the zip  file 
      out.putNextEntry(new ZipEntry("myEnumeration.java"));
      out.write(aEnumeration.render().getBytes(Charset.forName("UTF-8")));
      
      out.putNextEntry(new ZipEntry("myClass.java"));
      out.write(aClass.render().getBytes(Charset.forName("UTF-8")));
      
      out.putNextEntry(new ZipEntry("myInterface.java"));
      out.write(aInterface.render().getBytes(Charset.forName("UTF-8")));
      
      out.close();
//      in.close();
    }
    catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    

    

    
  }
  
}
