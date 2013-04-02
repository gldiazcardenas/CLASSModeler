/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml;

import java.util.Set;

/**
 * This class represents a core UML object, 'Comment' is an annotation made over
 * a specific element of the UML objects.
 * 
 * @author Gabriel Leonardo Diaz, 17.03.2013.
 * @version 1.0
 * @updated 24.03.2013 04:59:22 p.m.
 */
public class Comment extends Element {

  private String body;
  private Set<IElement> annotatedElement;

  public Comment() {
    super();
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }
  
  public Set<IElement> getAnnotatedElement() {
    return annotatedElement;
  }
  
  public void setAnnotatedElement(Set<IElement> annotatedElement) {
    this.annotatedElement = annotatedElement;
  }

}