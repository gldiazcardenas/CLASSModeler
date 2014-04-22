/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.controllers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import classmodeler.domain.diagram.Diagram;
import classmodeler.web.beans.SharedDiagram;

/**
 * Cache that stores diagrams being edited.
 * 
 * @author Gabriel Leonardo Diaz, 16.02.2014.
 */
@ApplicationScoped
@ManagedBean(name="sharedDiagramController")
public class SharedDiagramController {
  
  /**
   * A map that contains the shared diagrams:
   * Key -> Diagram Id.
   * Value -> Shared Diagram object.
   */
  private Map<Integer, SharedDiagram> sharedDiagrams = new HashMap<Integer, SharedDiagram>();
  
  public SharedDiagramController () {
    super();
  }
  
  /**
   * Checks if the given diagram is currently being shared.
   * 
   * @param diagram
   * @return
   * @author Gabriel Leonardo Diaz, 16.02.2014.
   */
  public synchronized boolean existsDiagram (Diagram diagram) {
    if (diagram == null) {
      return false;
    }
    
    return this.sharedDiagrams.containsKey(diagram.getKey());
  }
  
  /**
   * Adds the diagram to the shared map.
   * 
   * @param diagram
   *          The diagram to be added. If the diagram is already shared this
   *          method just returns the local instance.
   * @return
   * @author Gabriel Leonardo Diaz, 16.02.2014.
   */
  public synchronized SharedDiagram putDiagram (Diagram diagram) {
    if (diagram == null) {
      throw new IllegalArgumentException("The diagram cannot be null.");
    }
    
    SharedDiagram shared = this.sharedDiagrams.get(diagram.getKey());
    if (shared == null) {
      shared = new SharedDiagram(diagram);
      
      // Do not put fake diagrams
      if (shared.getKey() > 0) {
        this.sharedDiagrams.put(shared.getKey(), shared);
      }
    }
    return shared;
  }
  
  /**
   * Purges the shared diagrams map by removing those diagram that is not being
   * referenced by any session.
   * 
   * @author Gabriel Leonardo Diaz, 16.02.2014.
   */
  public synchronized void purgeDiagrams () {
    Entry<Integer, SharedDiagram> entry;
    for (Iterator<Entry<Integer, SharedDiagram>> iterator = this.sharedDiagrams.entrySet().iterator(); iterator.hasNext();) {
      entry = iterator.next();
      if (!entry.getValue().hasMoreListeners()) {
        iterator.remove();
      }
    }
  }
  
}
