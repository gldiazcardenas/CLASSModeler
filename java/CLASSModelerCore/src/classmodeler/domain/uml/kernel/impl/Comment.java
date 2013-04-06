/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml.kernel.impl;

import java.util.Set;

import classmodeler.domain.uml.kernel.IComment;
import classmodeler.domain.uml.kernel.IElement;

/**
 * A comment gives the ability to attach various remarks to elements. A comment
 * carries no semantic force, but may contain information that is useful to a
 * modeler. A comment can be owned by any element.
 * 
 * @author Gabriel Leonardo Diaz, 17.03.2013.
 */
public class Comment extends Element implements IComment {

  private String body;
  private Set<IElement> annotatedElements;

  public Comment() {
    super();
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }
  
  public Set<IElement> getAnnotatedElements() {
    return annotatedElements;
  }
  
  public void setAnnotatedElements(Set<IElement> annotatedElements) {
    this.annotatedElements = annotatedElements;
  }

}