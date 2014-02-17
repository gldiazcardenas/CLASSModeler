/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.beans;

import classmodeler.domain.diagram.Diagram;
import classmodeler.service.util.CollectionUtils;

import com.mxgraph.sharing.mxSharedState;

/**
 * Bean that represents the shared state of the diagram.
 * 
 * @author Gabriel Leonardo Diaz, 16.02.2014.
 */
public class SharedDiagram extends mxSharedState {
  
  private int key;
  private String name;
  private Diagram diagram;
  
  public SharedDiagram (Diagram diagram) {
    super(diagram.getXML());
    
    this.key = diagram.getKey();
    this.name = diagram.getName();
    this.diagram = diagram;
  }
  
  public int getKey() {
    return key;
  }
  
  public String getName() {
    return name;
  }
  
  public Diagram getDiagram() {
    return diagram;
  }
  
  public void applyChanges () {
    this.diagram.setXML(getState());
  }
  
  public boolean hasMoreListeners () {
    return !CollectionUtils.isEmptyCollection(this.diagramChangeListeners);
  }
  
}
