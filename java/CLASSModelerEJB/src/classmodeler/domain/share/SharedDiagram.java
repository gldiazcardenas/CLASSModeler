/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.share;

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
  
  private Diagram wrappedDiagram;
  
  public SharedDiagram (Diagram diagram) {
    super((mxGraphModel) new mxCodec().decode(mxXmlUtils.parseXml(diagram.getXML()).getDocumentElement()));
    this.wrappedDiagram = diagram;
  }
  
  public int getKey() {
    return wrappedDiagram.getKey();
  }
  
  public String getName() {
    return wrappedDiagram.getName();
  }
  
  public Diagram getWrappedDiagram() {
    return wrappedDiagram;
  }
  
  /**
   * Publishes the changes to the wrapped diagram in form of XML. This copies
   * the XML representation to the local diagram and prepares it to be saved in
   * DB.
   * 
   * @author Gabriel Leonardo Diaz, 01.03.2014.
   */
  public void publishChanges () {
    String value = getState();
    this.wrappedDiagram.setXML(value);
  }
  
  /**
   * Checks if the diagram has listeners of its state, it means other users are
   * also editing/seeing the diagram.
   * 
   * @return
   * @author Gabriel Leonardo Diaz, 01.03.2014.
   */
  public boolean hasMoreListeners () {
    return !CollectionUtils.isEmptyCollection(this.diagramChangeListeners);
  }
  
}
