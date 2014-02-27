/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.stringtemplate.test;

import org.stringtemplate.v4.ST;
import org.testng.annotations.Test;

/**
 * Test class for StringTemplate plugin.
 *
 * @author Gabriel Leonardo Diaz, 26.02.2014.
 */
public class TemplateCodeTest {
  
  @Test
  public void generateCodeTemplate () {
    ST hello = new ST("Hello, <name>");
    hello.add("name", "World");
    System.out.println(hello.render());
  }
  
}
