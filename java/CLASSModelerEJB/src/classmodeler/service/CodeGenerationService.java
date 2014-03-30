/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service;

import javax.ejb.Local;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Interface;

import classmodeler.domain.code.SourceCodeFile;
import classmodeler.domain.user.User;

/**
 * Specification of the services provided to handle files of source code.
 *
 * @author Gabriel Leonardo Diaz, 05.03.2014.
 */
@Local
public interface CodeGenerationService {
  
  /**
   * 
   * @param user
   */
  public void configure (User user);
  
  /**
   * 
   * @param user
   * @param file
   * @return
   */
  public String generateSourceCode (SourceCodeFile file);
  
  /**
   * 
   * @param user
   * @param aClass
   * @return
   */
  public String generateClass (Class aClass);
  
  /**
   * 
   * @para user
   * @param aInterface
   * @return
   */
  public String generateInterface (Interface aInterface);
  
  /**
   * 
   * @param user
   * @param aEnumeration
   * @return
   */
  public String generateEnumeration (Enumeration aEnumeration);
  
}
