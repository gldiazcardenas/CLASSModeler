/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2014 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.beans;

import classmodeler.domain.diagram.Diagram;
import classmodeler.domain.user.User;

import com.mxgraph.sharing.mxSession;

/**
 * Session for handling a shared diagram.
 * 
 * @author Gabriel Leonardo Diaz, 23.03.2014.
 */
public class SharedDiagramSession extends mxSession {
  
  public static final String SHARED_DIAGRAM_SESSION_ID = "SharedSessionID";
  
  private User user;
  
  public SharedDiagramSession(SharedDiagram diagram, User user) {
    super(SHARED_DIAGRAM_SESSION_ID, diagram);
    this.user = user;
  }
  
  public User getUser() {
    return user;
  }
  
  public void setUser(User user) {
    this.user = user;
  }
  
  public SharedDiagram getSharedDiagram () {
    return (SharedDiagram) diagram;
  }
  
  public Diagram getDiagram () {
    return getSharedDiagram().getWrappedDiagram();
  }
  
}
