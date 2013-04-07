/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml.kernel.impl;

import java.util.List;

import classmodeler.domain.uml.kernel.IFeature;
import classmodeler.domain.uml.kernel.IRedefinableElement;
import classmodeler.domain.uml.kernel.IType;

/**
 * A behavioral feature specifies that an instance of a classifier will respond
 * to a designated request by invoking a behavior. BehavioralFeature is an
 * abstract metaclass specializing Feature and Namespace. Kinds of behavioral
 * aspects are modeled by subclasses of BehavioralFeature.
 * 
 * @author Gabriel Leonardo Diaz, 24.03.2013.
 */
public class BehavioralFeature extends Namespace implements IFeature {
  
  /**
   * References the Types representing exceptions that may be raised during an
   * invocation of this operation.
   */
  public List<IType> raisedExceptions;
  
  /**
   * Specifies the ordered set of formal parameters owned by this
   * BehavioralFeature. The parameter direction can be ‘in,’ ‘inout,’ ‘out,’ or
   * ‘return’ to specify input, output, or return parameters.
   */
  public List<Parameter> ownedParameters;
  
  public BehavioralFeature() {
    super();
  }

  @Override
  public boolean isLeaf() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isConsistentWith(IRedefinableElement redefinableElement) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isRedefinitionContextValid(IRedefinableElement redefinableElement) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isStatic() {
    // TODO Auto-generated method stub
    return false;
  }
  
}