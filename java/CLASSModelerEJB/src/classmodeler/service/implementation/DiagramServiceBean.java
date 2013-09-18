/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.implementation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import classmodeler.domain.diagram.Diagram;
import classmodeler.domain.diagram.EDiagramPrivilege;
import classmodeler.domain.diagram.Shared;
import classmodeler.domain.user.Diagrammer;
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
  public List<Diagram> getAllDiagramsByUser(Diagrammer diagrammer) {
    List<Diagram> ownedDiagrams = em.createQuery("SELECT d FROM Diagram d WHERE d.createdBy = :owner", Diagram.class)
                                    .setParameter("owner", diagrammer)
                                    .getResultList();
    
    List<Diagram> sharedDiagrams = em.createQuery("SELECT s.diagram FROM Shared s WHERE s.toDiagrammer = :sharedTo", Diagram.class)
                                     .setParameter("sharedTo", diagrammer)
                                     .getResultList();
    
    return CollectionUtils.union(ownedDiagrams, sharedDiagrams);
  }
  
  @Override
  public List<Shared> getSharingsByDiagram(Diagram diagram) {
    List<Shared> sharings = new ArrayList<Shared>();
    
    if (diagram != null) {
      Shared share = new Shared();
      share.setFromDiagrammer(diagram.getCreatedBy());
      share.setToDiagrammer(diagram.getModifiedBy());
      share.setPrivilege(EDiagramPrivilege.OWNER);
      share.setDate(diagram.getCreatedDate());
      share.setKey(Integer.MAX_VALUE);
      share.setDiagram(diagram);
      
      sharings.add(share);
      sharings.addAll(em.createQuery("SELECT s FROM Shared s WHERE s.diagram = :diagram", Shared.class).setParameter("diagram", diagram).getResultList());
    }
    
    return sharings;
  }
  
  @Override
  public void shareDiagram(Diagram diagram, Diagrammer fromUser, List<Diagrammer> toUsers) {
    // TODO Auto-generated method stub
  }
  
}
