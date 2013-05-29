/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service;

import javax.ejb.Local;

import classmodeler.domain.project.Project;

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
   * @author Gabriel Leonardo Diaz, 28.05.2013.
   */
  public void insertProject (Project project);
  
}
