/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.implementation;

import javax.ejb.Stateless;

import classmodeler.domain.project.Project;
import classmodeler.service.ProjectService;

/**
 * Session bean implementation for Project service.
 * 
 * @author Gabriel Leonardo Diaz, 08.05.2013.
 */
public @Stateless class ProjectServiceBean implements ProjectService {
  
  @Override
  public void insertProject(Project project) {
    // TODO Auto-generated method stub
  }
  
  @Override
  public void updateProject(Project project) {
    // TODO Auto-generated method stub
    
  }
  
}
