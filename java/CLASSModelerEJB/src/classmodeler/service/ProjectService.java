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

import classmodeler.domain.project.Project;
import classmodeler.domain.user.User;

/**
 * Service that handles all basic operations (CRUD) over Projects.
 * 
 * @author Gabriel Leonardo Diaz, 08.05.2013.
 */
@Local
public interface ProjectService {
  
  /**
   * Inserts the given project in database.
   * 
   * @param project
   *          The new project to be saved.
   * @return The insert project.
   * @author Gabriel Leonardo Diaz, 28.05.2013.
   */
  public Project insertProject (Project project);
  
  /**
   * Updates the information of the project in database.
   * 
   * @param project
   *          The project to update.
   * @author Gabriel Leonardo Diaz, 28.05.2013.
   */
  public void updateProject (Project project);
  
  /**
   * Gets the list of project that the user can see, it means the projects owned
   * by the user and all the projects shared by other users.
   * 
   * @param user
   *          The user to get all the project it can see.
   * @return A list with all projects of the user. Can be null.
   * @author Gabriel Leonardo Diaz, 01.06.2013.
   */
  public List<Project> getAllProjectsByUser (User user);
  
}
