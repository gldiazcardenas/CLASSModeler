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

import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.sharing.mxSharedGraphModel;
import com.mxgraph.util.mxXmlUtils;

/**
 * Bean that represents the shared state of the diagram.
 * 
 * @author Gabriel Leonardo Diaz, 16.02.2014.
 */
public class SharedDiagram extends mxSharedGraphModel {
  
  private Diagram diagram;
  
  public SharedDiagram (Diagram diagram) {
    super((mxGraphModel) new mxCodec().decode(mxXmlUtils.parseXml(diagram.getXML()).getDocumentElement()));
    this.diagram = diagram;
  }
  
  public int getKey() {
    return diagram.getKey();
  }
  
  public String getName() {
    return diagram.getName();
  }
  
  public Diagram getWrappedDiagram() {
    return diagram;
  }
  
  public void publishChanges () {
    String value = getState();
    this.diagram.setXML(value);
  }
  
  public boolean hasMoreListeners () {
    return !CollectionUtils.isEmptyCollection(this.diagramChangeListeners);
  }
  
}
