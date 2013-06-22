/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * C�cuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service;

import java.util.List;

import javax.ejb.Local;

import classmodeler.domain.diagram.Diagram;
import classmodeler.domain.user.User;

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
   * Updates the information of the diagram in database.
   * 
   * @param diagram
   *          The diagram to update.
   * @author Gabriel Leonardo Diaz, 28.05.2013.
   */
  public void updateDiagram (Diagram diagram);
  
  /**
   * Deletes from database the diagram represented by the given key.
   * 
   * @param diagramKey
   *          The key of the diagram to delete.
   * @author Gabriel Leonardo Diaz, 01.06.2013.
   */
  public void deleteDiagram (int diagramKey);
  
  /**
   * Gets the list of diagram that the user can see, it means the diagrams owned
   * by the user and all the diagrams shared by other users.
   * 
   * @param user
   *          The user to get all the diagram it can see.
   * @return A list with all diagrams of the user. Can be null.
   * @author Gabriel Leonardo Diaz, 01.06.2013.
   */
  public List<Diagram> getAllDiagramsByUser (User user);
  
}