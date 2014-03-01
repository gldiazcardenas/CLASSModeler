/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.share;

import com.mxgraph.sharing.mxSession;

/**
 * Custom session to handle shared diagrams. This inherits the default behavior
 * from {@link mxSession}.
 * 
 * @author Gabriel Leonardo Diaz, 16.02.2014.
 */
public class SharedDiagramSession extends mxSession {
  
  public static final String SHARED_DIAGRAM_SESSION_ID = "SharedSessionID";
  
  public SharedDiagramSession (SharedDiagram diagram) {
    super(SHARED_DIAGRAM_SESSION_ID, diagram);
  }
  
  @Override
  public void destroy() {
    super.destroy();
    SharedDiagramsCache.getInstance().clearCache();
  }
  
}
