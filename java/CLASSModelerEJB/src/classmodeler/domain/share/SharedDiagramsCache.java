/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.share;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import classmodeler.domain.diagram.Diagram;

/**
 * Cache that stores diagrams being edited.
 * 
 * @author Gabriel Leonardo Diaz, 16.02.2014.
 */
public class SharedDiagramsCache {
  
  private static SharedDiagramsCache instance;
  
  /**
   * A map that contains the shared diagrams:
   * Key -> Diagram Id.
   * Value -> Shared Diagram object.
   */
  private Map<Integer, SharedDiagram> sharedDiagrams = new HashMap<Integer, SharedDiagram>();
  
  private SharedDiagramsCache () {
    super();
  }
  
  /**
   * Gets the reference to the cache object.
   * 
   * @return The cache reference, this instances the object if this has not been
   *         created.
   * @author Gabriel Leonardo Diaz, 16.02.2014.
   */
  public static SharedDiagramsCache getInstance () {
    if (instance == null) {
      instance = new SharedDiagramsCache();
    }
    return instance;
  }
  
  /**
   * Checks if the given diagram is currently being shared.
   * 
   * @param diagram
   * @return
   * @author Gabriel Leonardo Diaz, 16.02.2014.
   */
  public synchronized boolean existsSharedDiagram (Diagram diagram) {
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
  public synchronized void clearCache () {
    Entry<Integer, SharedDiagram> entry;
    for (Iterator<Entry<Integer, SharedDiagram>> iterator = this.sharedDiagrams.entrySet().iterator(); iterator.hasNext();) {
      entry = iterator.next();
      if (!entry.getValue().hasMoreListeners()) {
        iterator.remove();
      }
    }
  }
}
