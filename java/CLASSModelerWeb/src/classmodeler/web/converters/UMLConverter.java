/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.converters;

import com.mxgraph.model.mxGraphModel;

/**
 * Core implementation of the diagram XML converter. This allows to parse the
 * XML diagram and convert it to UML meta-model objects.
 * 
 * @author Gabriel Leonardo Diaz, 16.02.2014.
 */
public class UMLConverter {
  
  private mxGraphModel mxModel;
  
  /**
   * Basic constructor for the UML meta model converter.
   * @param mxModel
   */
  public UMLConverter (mxGraphModel mxModel) {
    super();
    this.mxModel  = mxModel;
  }
  
  /**
   * Converts from the XML document object representation to a UML meta-model
   * representation.
   * 
   * @return The UML model object.
   * @author Gabriel Leonardo Diaz, 25.02.2014.
   */
  public void convert () {
    convertPackages();
    convertClassifiers();
    convertAttributes(null);
    convertOperations(null);
    convertComments(null);
  }
  
  private void convertPackages () {
    // TODO GD
  }
  
  private void convertClassifiers () {
    // TODO GD
  }
  
  private void convertAttributes (Object classifier) {
    // TODO GD
  }
  
  private void convertOperations (Object classifier) {
    // TODO GD
  }
  
  private void convertComments (Object element) {
    // TODO GD
  }
  
  public mxGraphModel getMxModel() {
    return mxModel;
  }
  
}
