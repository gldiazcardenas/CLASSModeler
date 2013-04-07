/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.uml.kernel.impl;

import java.util.List;

/**
 * @author Gabriel Leonardo Diaz, 24.03.2013.
 */
public class Operation extends BehavioralFeature {
  
  public boolean ordered;
  public boolean query;
  public boolean unique;
  public int upper;
  public int lower;
  public List<Constraint> preConditions;
  public List<Constraint> bodyConditions;
  public List<Constraint> postCondition;
  public List<Operation> redefinedOperations;
  
  public Operation() {
    super();
  }
  
}