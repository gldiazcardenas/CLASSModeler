/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.ejb.Local;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import classmodeler.domain.diagram.Diagram;
import classmodeler.domain.share.SharedItem;
import classmodeler.domain.user.Diagrammer;
import classmodeler.domain.user.User;
import classmodeler.service.exception.InvalidDiagrammerAccountException;
import classmodeler.service.exception.UnprivilegedException;

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
   * @throws InvalidDiagrammerAccountException
   *           when the diagrammer owner of the diagram has not activate its
   *           account.
   */
  public Diagram insertDiagram (Diagram diagram) throws InvalidDiagrammerAccountException;
  
  /**
   * Updates the information of the diagram in database. The diagram can be
   * updated only by the owner or a diagrammer who had received the diagram
   * shared.
   * 
   * @param diagram
   *          The diagram to update.
   * @return The diagram after performing the update.
   * @author Gabriel Leonardo Diaz, 28.05.2013.
   * @throws UnprivilegedException
   *           When the diagrammer who is modifying the diagram does not have
   *           privileges for that.
   */
  public Diagram updateDiagram (Diagram diagram) throws UnprivilegedException;
  
  /**
   * Deletes from database the diagram represented by the given key.
   * 
   * @param diagramKey
   *          The key of the diagram to delete.
   * @author Gabriel Leonardo Diaz, 01.06.2013.
   * @throws UnprivilegedException
   *           When the diagrammer does not have privileges for the operation.
   */
  public void deleteDiagram (int diagramKey) throws UnprivilegedException;
  
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
   * @param canWrite
   *          Flag indicating the diagrammers can write the diagram.
   * @author Gabriel Leonardo Diaz, 12.08.2013.
   */
  public void shareDiagram (Diagram diagram, List<Diagrammer> toDiagrammers, boolean canWrite);
  
  /**
   * Checks if the user can write the diagram.
   * 
   * @param diagram
   *          The diagram to be edited.
   * @param user
   *          The user who is going to edit the diagram.
   * @return This returns a boolean value indicating the privilege.
   * @throws If
   *           the user has not privileges for the diagram, it means the diagram
   *           was never shared to him.
   * @author Gabriel Leonardo Diaz, 13.02.2014
   */
  public boolean canWriteDiagram (Diagram diagram, User user) throws UnprivilegedException;
  
  /**
   * Generates the image representation of the diagram using the XML
   * representation, this puts the result in the output parameter.
   * 
   * @param rawXML
   * @param output
   * @author Gabriel Leonardo Diaz, 01.03.2014.
   */
  public void generateImage (String rawXML, OutputStream output) throws ParserConfigurationException, SAXException, IOException ;
  
}
