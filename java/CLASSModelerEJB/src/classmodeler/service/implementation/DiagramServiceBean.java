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
import classmodeler.domain.diagram.SharedItem;
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
    
    diagram.setXMI("");
    diagram.setCreatedDate(Calendar.getInstance().getTime());
    diagram.setModifiedDate(diagram.getCreatedDate());
    
    em.persist(diagram);
    
    return diagram;
  }
  
  @Override
  public Diagram updateDiagram(Diagram diagram) {
    if (diagram == null) {
      return null;
    }
    
    diagram.setModifiedDate(Calendar.getInstance().getTime());
    
    return em.merge(diagram);
  }
  
  @Override
  public void deleteDiagram(int diagramKey) {
    Diagram diagram = em.find(Diagram.class, Integer.valueOf(diagramKey));
    if (diagram != null) {
      em.remove(diagram);
    }
  }
  
  @Override
  public List<Diagram> getDiagramsByDiagrammer (Diagrammer diagrammer) {
    List<Diagram> ownedDiagrams = em.createQuery("SELECT d FROM Diagram d WHERE d.createdBy = :owner", Diagram.class)
                                    .setParameter("owner", diagrammer)
                                    .getResultList();
    
    List<Diagram> sharedDiagrams = em.createQuery("SELECT si.diagram FROM SharedItem si WHERE si.diagrammer = :diagrammer", Diagram.class)
                                     .setParameter("diagrammer", diagrammer)
                                     .getResultList();
    
    return CollectionUtils.union(ownedDiagrams, sharedDiagrams);
  }
  
  @Override
  public List<SharedItem> getSharedItemsByDiagram(Diagram diagram) {
    List<SharedItem> items = new ArrayList<SharedItem>();
    
    if (diagram != null) {
      items.addAll(em.createQuery("SELECT si FROM SharedItem si WHERE si.diagram = :diagram", SharedItem.class)
                     .setParameter("diagram", diagram)
                     .getResultList());
    }
    
    return items;
  }
  
  @Override
  public void shareDiagram(Diagram diagram, List<Diagrammer> toDiagrammers, EDiagramPrivilege privilege) {
    // TODO Auto-generated method stub
  }
  
}
