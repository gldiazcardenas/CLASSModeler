/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service;

import java.util.List;

import javax.ejb.Local;

import classmodeler.domain.diagram.Diagram;
import classmodeler.domain.diagram.EDiagramPrivilege;
import classmodeler.domain.diagram.SharedItem;
import classmodeler.domain.user.Diagrammer;

/**
 * Service that handles all basic operations (CRUD) over Diagrams.
 * 
 * @author Gabriel Leonardo Diaz, 08.05.2013.
 */
@Local
public interface DiagramService {
  
  /**
   * Inserts the given diagram in database.
   * 
   * @param diagram
   *          The new diagram to be saved.
   * @return The insert project.
   * @author Gabriel Leonardo Diaz, 28.05.2013.
   */
  public Diagram insertDiagram (Diagram diagram);
  
  /**
   * Updates the information of the diagram in database. The diagram can be
   * updated only by the owner or a diagrammer who had received the diagram
   * shared.
   * 
   * @param diagram
   *          The diagram to update.
   * @return The diagram after performing the update.
   * @author Gabriel Leonardo Diaz, 28.05.2013.
   */
  public Diagram updateDiagram (Diagram diagram);
  
  /**
   * Deletes from database the diagram represented by the given key.
   * 
   * @param diagramKey
   *          The key of the diagram to delete.
   * @author Gabriel Leonardo Diaz, 01.06.2013.
   */
  public void deleteDiagram (int diagramKey);
  
  /**
   * Gets the list of diagram that the diagrammer can see, it means the diagrams owned
   * by the diagrammer and all the diagrams shared by other diagrammers.
   * 
   * @param diagrammer
   *          The diagrammer to get all the diagram it can see.
   * @return A list with all diagrams of the user. Can be null.
   * @author Gabriel Leonardo Diaz, 01.06.2013.
   */
  public List<Diagram> getDiagramsByDiagrammer (Diagrammer diagrammer);
  
  /**
   * Gets all shared items of the given diagram.
   * 
   * @param diagram
   *          The diagram to process.
   * @return A list of Shared objects.
   * @author Gabriel Leonardo Diaz, 03.07.2013.
   */
  public List<SharedItem> getSharedItemsByDiagram (Diagram diagram);
  
  /**
   * Shares the given diagram from the specific user to the users in the list
   * parameter.
   * 
   * @param diagram
   *          The diagram to share.
   * @param toDiagrammers
   *          The diagrammers that will receive the diagram.
   * @param privilege
   *          The privilege granted to the diagrammers.
   * @author Gabriel Leonardo Diaz, 12.08.2013.
   */
  public void shareDiagram (Diagram diagram, List<Diagrammer> toDiagrammers, EDiagramPrivilege privilege);
  
}
