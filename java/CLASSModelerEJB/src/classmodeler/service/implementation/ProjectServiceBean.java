/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.implementation;

import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import classmodeler.domain.project.Project;
import classmodeler.domain.user.User;
import classmodeler.service.ProjectService;
import classmodeler.service.util.CollectionUtils;

/**
 * Session bean implementation for Project service.
 * 
 * @author Gabriel Leonardo Diaz, 08.05.2013.
 */
public @Stateless class ProjectServiceBean implements ProjectService {
  
  @PersistenceContext(unitName="CLASSModelerPU")
  private EntityManager em;
  
  @Override
  public Project insertProject(Project project) {
    if (project == null) {
      return project;
    }
    
    project.setCreatedDate(Calendar.getInstance().getTime());
    project.setModifiedDate(project.getCreatedDate());
    
    em.persist(project);
    
    return project;
  }
  
  @Override
  public void updateProject(Project project) {
    if (project == null) {
      return;
    }
    
    em.merge(project);
  }
  
  @Override
  public void deleteProject(int projectKey) {
    Project project = em.find(Project.class, Integer.valueOf(projectKey));
    if (project != null) {
      em.remove(project);
    }
  }
  
  @Override
  public List<Project> getAllProjectsByUser(User user) {
    List<Project> ownedProjects = em.createQuery("SELECT p FROM Project p WHERE p.createdBy = :ownerUser", Project.class)
                                    .setParameter("ownerUser", user)
                                    .getResultList();
    
    List<Project> sharedProjects = em.createQuery("SELECT s.project FROM Shared s WHERE s.toUser = :sharedUser", Project.class)
                                     .setParameter("sharedUser", user)
                                     .getResultList();
    
    return CollectionUtils.mergeLists(ownedProjects, sharedProjects);
  }
  
}
