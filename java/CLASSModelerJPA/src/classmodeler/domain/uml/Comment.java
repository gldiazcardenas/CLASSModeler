/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml;

/**
 * This class represents a core UML object, 'Comment' is an annotation made over
 * a specific element of the UML objects.
 * 
 * @author Gabriel Leonardo Diaz, 17.03.2013.
 */
public class Comment extends Element {

  private String body;

  public Comment() {
    super();
  }
  
  public String getBody() {
    return body;
  }
  
  public void setBody(String body) {
    this.body = body;
  }

}