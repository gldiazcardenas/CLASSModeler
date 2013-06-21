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

import classmodeler.domain.diagram.Diagram;
import classmodeler.domain.user.User;
import classmodeler.service.DiagramService;
import classmodeler.service.util.CollectionUtils;

/**
 * Session bean implementation for Diagram service.
 * 
 * @author Gabriel Leonardo Diaz, 08.05.2013.
 */
public @Stateless class DiagramServiceBean implements DiagramService {
  
  @PersistenceContext(unitName="CLASSModelerPU")
  private EntityManager em;
  
  @Override
  public Diagram insertDiagram(Diagram diagram) {
    if (diagram == null) {
      return diagram;
    }
    
    diagram.setCreatedDate(Calendar.getInstance().getTime());
    diagram.setModifiedDate(diagram.getCreatedDate());
    
    em.persist(diagram);
    
    return diagram;
  }
  
  @Override
  public void updateDiagram(Diagram diagram) {
    if (diagram == null) {
      return;
    }
    
    em.merge(diagram);
  }
  
  @Override
  public void deleteDiagram(int diagramKey) {
    Diagram diagram = em.find(Diagram.class, Integer.valueOf(diagramKey));
    if (diagram != null) {
      em.remove(diagram);
    }
  }
  
  @Override
  public List<Diagram> getAllDiagramsByUser(User user) {
    List<Diagram> ownedDiagrams = em.createQuery("SELECT d FROM Diagram d WHERE d.createdBy = :ownerUser", Diagram.class)
                                    .setParameter("ownerUser", user)
                                    .getResultList();
    
    List<Diagram> sharedDiagrams = em.createQuery("SELECT s.diagram FROM Shared s WHERE s.toUser = :sharedUser", Diagram.class)
                                     .setParameter("sharedUser", user)
                                     .getResultList();
    
    return CollectionUtils.mergeLists(ownedDiagrams, sharedDiagrams);
  }
  
}
