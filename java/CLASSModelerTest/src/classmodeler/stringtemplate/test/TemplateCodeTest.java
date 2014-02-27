/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.stringtemplate.test;

import java.io.StringReader;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
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
    String templates = "group simple; vardef(type,name) ::= \"<type> <name>;\""; // templates from above
    // Use the constructor that accepts a Reader
    StringTemplateGroup group = new StringTemplateGroup(new StringReader(templates));
    StringTemplate t = group.getInstanceOf("vardef");
    t.setAttribute("type", "int");
    t.setAttribute("name", "foo");
    System.out.println(t);
    
    STGroup g = new STGroupFile("classmodeler/stringtemplate/test/ClassTemplate.stg");
    ST st = g.getInstanceOf("class");
    st.add("visibility", "protected");
    st.add("name", "Person");
    System.out.println(st.render());
  }
  
}
