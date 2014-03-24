/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.implementation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import org.eclipse.uml2.uml.Model;
import org.stringtemplate.v4.DateRenderer;
import org.stringtemplate.v4.STGroupFile;

import classmodeler.domain.sourcecode.SourceCodeFile;
import classmodeler.domain.user.User;
import classmodeler.service.GenerateCodeService;

/**
 * Implementation of the file service.
 * 
 * @author Gabriel Leonardo Diaz, 05.03.2014.
 */
public @Stateless class GenerateCodeServiceBean implements GenerateCodeService {
  
  public static final STGroupFile TEMPLATES = new STGroupFile("classmodeler/domain/code/templates.java.stg");
  
  static {
    DateRenderer dateRender = new DateRenderer();
    TEMPLATES.registerRenderer(Date.class, dateRender);
    TEMPLATES.registerRenderer(Calendar.class, dateRender);
  }
  
  protected User user;
  protected Date now;
  protected Model model;
  
  @Override
  public List<SourceCodeFile> generateCode(User user, Model model) {
    List<SourceCodeFile> sourceCodeFiles = new ArrayList<SourceCodeFile>();
    
    this.model = model;
    this.user  = user;
    this.now   = Calendar.getInstance().getTime();
    
    return sourceCodeFiles;
  }
  
}
