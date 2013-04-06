/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml.kernel;

import java.util.Set;

/**
 * A comment gives the ability to attach various remarks to elements. A comment
 * carries no semantic force, but may contain information that is useful to a
 * modeler. A comment can be owned by any element.
 * 
 * @author Gabriel Leonardo Diaz, 05.04.2013.
 */
public interface IComment extends IElement {
  
  /**
   * Specifies a string that is the comment.
   * 
   * @return the body of the comment.
   */
  public String getBody();
  
  /**
   * References the Element(s) being commented.
   * 
   * @return The commented elements.
   */
  public Set<IElement> getAnnotatedElements();
  
}
