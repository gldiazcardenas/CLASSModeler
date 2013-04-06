package classmodeler.domain.uml;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:23 p.m.
 */
public class Generalization extends DirectedRelationship {
  
  public boolean sustituable;
  public Classifier m_Classifier;
  
  public Generalization() {
    
  }
}