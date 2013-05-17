/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.implementation;

import java.util.HashSet;
import java.util.Set;

import javax.ejb.Singleton;

import classmodeler.domain.sharing.ProjectShared;
import classmodeler.service.SharingService;

/**
 * Session Bean implementation.
 * 
 * @author Gabriel Leonardo Diaz, 06.05.2013.
 */
public @Singleton class SharingServiceBean implements SharingService {
  
  private Set<ProjectShared> sharedProjects;
  
  public SharingServiceBean() {
    super();
    
    this.sharedProjects = new HashSet<ProjectShared>();
  }

  @Override
  public void addSharedProject(ProjectShared projectShared) {
    this.sharedProjects.add(projectShared);
  }
  
}
