/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.implementation;

import java.util.Calendar;
import java.util.Date;

import javax.ejb.Stateless;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Interface;
import org.stringtemplate.v4.DateRenderer;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import classmodeler.domain.code.SourceCodeFile;
import classmodeler.domain.user.User;
import classmodeler.service.CodeGenerationService;

/**
 * Implementation of the file service.
 * 
 * @author Gabriel Leonardo Diaz, 05.03.2014.
 */
public @Stateless class CodeGenerationServiceBean implements CodeGenerationService {
  
  public static final STGroupFile TEMPLATES = new STGroupFile("classmodeler/domain/code/templates/java.stg");
  
  static {
    DateRenderer dateRender = new DateRenderer();
    TEMPLATES.registerRenderer(Date.class, dateRender);
    TEMPLATES.registerRenderer(Calendar.class, dateRender);
  }
  
  private User user;
  private Date now;
  
  public CodeGenerationServiceBean() {
    super();
  }
  
  @Override
  public void configure(User user) {
    this.user = user;
    this.now  = Calendar.getInstance().getTime();
  }
  
  @Override
  public String generateSourceCode (SourceCodeFile file) {
    if (file.getElement() instanceof Class) {
      return generateClass((Class) file.getElement());
    }
    else if (file.getElement() instanceof Interface) {
      return generateInterface((Interface) file.getElement());
    }
    else if (file.getElement() instanceof Enumeration) {
      return generateEnumeration((Enumeration) file.getElement());
    }
    
    return null;
  }
  
  @Override
  public String generateClass(Class aClass) {
    ST template = TEMPLATES.getInstanceOf("class_template");
    template.add("class", aClass);
    template.add("author", user.getName());
    template.add("date", now);
    return template.render();
  }
  
  @Override
  public String generateEnumeration(Enumeration aEnumeration) {
    ST template = TEMPLATES.getInstanceOf("enumeration_template");
    template.add("enumeration", aEnumeration);
    template.add("author", user.getName());
    template.add("date", now);
    return template.render();
  }
  
  @Override
  public String generateInterface(Interface aInterface) {
    ST template = TEMPLATES.getInstanceOf("interface_template");
    template.add("interface", aInterface);
    template.add("author", user.getName());
    template.add("date", now);
    return template.render();
  }
  
}
