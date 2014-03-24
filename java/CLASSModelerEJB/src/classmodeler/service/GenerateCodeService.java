/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service;

import java.util.List;

import javax.ejb.Local;

import org.eclipse.uml2.uml.Model;

import classmodeler.domain.sourcecode.SourceCodeFile;
import classmodeler.domain.user.User;

/**
 * Specification of the services provided to handle files of source code.
 *
 * @author Gabriel Leonardo Diaz, 05.03.2014.
 */
@Local
public interface GenerateCodeService {
  
  /**
   * Generates the source code of the given diagram.
   * 
   * @param user The user for who is the source code being generated.
   * @param model The model to generate the source files.
   * @return
   * @author Gabriel Leonardo Diaz, 05.03.2014.
   */
  public List<SourceCodeFile> generateCode (User user, Model model);
  
}
