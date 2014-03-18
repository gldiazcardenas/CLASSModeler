/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service;

import java.util.Comparator;
import java.util.List;

import javax.ejb.Local;

import org.stringtemplate.v4.STGroupFile;

import classmodeler.domain.code.SourceCodeFile;
import classmodeler.domain.share.SharedDiagram;
import classmodeler.domain.user.User;

/**
 * Specification of the services provided to handle files of source code.
 *
 * @author Gabriel Leonardo Diaz, 05.03.2014.
 */
@Local
public interface SourceCodeService {
  
  /**
   * Points to the templates of Java Source code, by the moment this is enough
   * because only support Java Language.
   */
  public static final STGroupFile TEMPLATES = new STGroupFile("classmodeler/domain/code/sourcecode.java.stg");
  
  /**
   * Default source code files comparator.
   */
  public static final SourceCodeFileComparator SOURCE_FILES_COMPARATOR = new SourceCodeFileComparator();
  
  /**
   * Generates the source code of the given diagram.
   * 
   * @param user The user for who is the source code being generated.
   * @param diagram The diagram to generate the source code.
   * @return
   * @author Gabriel Leonardo Diaz, 05.03.2014.
   */
  public List<SourceCodeFile> generateCode (User user, SharedDiagram diagram);
  
  /**
   * Comparator for source code file objects, this allows to sort the objects
   * alphabetically.
   * 
   * @author Gabriel Leonardo Diaz, 05.03.2014.
   */
  public static class SourceCodeFileComparator implements Comparator<SourceCodeFile> {
    @Override
    public int compare(SourceCodeFile o1, SourceCodeFile o2) {
      return o1.getName().compareTo(o2.getName());
    }
  }
  
}
