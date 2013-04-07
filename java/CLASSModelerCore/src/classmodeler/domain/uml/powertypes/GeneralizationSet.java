package classmodeler.domain.uml.powertypes;

import classmodeler.domain.uml.kernel.impl.Classifier;
import classmodeler.domain.uml.kernel.impl.Generalization;
import classmodeler.domain.uml.kernel.impl.PackageableElement;

/**
 * @author Gabriel
 * @version 1.0
 * @created 24-mar-2013 04:59:23 p.m.
 */
public class GeneralizationSet extends PackageableElement {
  
  public Classifier m_Classifier;
  public Generalization m_Generalization;
  
  public GeneralizationSet() {
    
  }
}