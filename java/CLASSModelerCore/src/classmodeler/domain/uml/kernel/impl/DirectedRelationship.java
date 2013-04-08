/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * C�cuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml.kernel.impl;

import java.util.HashSet;
import java.util.Set;

import classmodeler.domain.uml.kernel.IElement;
import classmodeler.domain.uml.kernel.IRelationship;

/**
 * A relationship that has a defined direction from the source to the target.
 * 
 * @author Gabriel Leonardo Diaz, 24.03.2013.
 */
public abstract class DirectedRelationship extends Element implements IRelationship {

  private Set<IElement> source;
  private Set<IElement> target;

  public DirectedRelationship() {
    super();
  }

  public Set<IElement> getRelatedElements() {
    Set<IElement> relatedElements = new HashSet<IElement>();
    
    if (source != null) {
      relatedElements.addAll(source);
    }
    
    if (target != null) {
      relatedElements.addAll(target);
    }
    
    return relatedElements;
  }
  
  public Set<IElement> getSource() {
    return source;
  }
  
  public void setSource(Set<IElement> source) {
    this.source = source;
  }
  
  public Set<IElement> getTarget() {
    return target;
  }
  
  public void setTarget(Set<IElement> target) {
    this.target = target;
  }
}