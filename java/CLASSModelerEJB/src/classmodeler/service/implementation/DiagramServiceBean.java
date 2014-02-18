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

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import classmodeler.domain.diagram.Diagram;
import classmodeler.domain.diagram.EDiagramPrivilege;
import classmodeler.domain.diagram.SharedItem;
import classmodeler.domain.user.Diagrammer;
import classmodeler.domain.user.EDiagrammerAccountStatus;
import classmodeler.domain.user.User;
import classmodeler.service.DiagramService;
import classmodeler.service.UserService;
import classmodeler.service.exception.InvalidDiagrammerAccountException;
import classmodeler.service.exception.InvalidDiagrammerAccountException.EInvalidAccountErrorType;
import classmodeler.service.util.CollectionUtils;

/**
 * Session bean implementation for Diagram service.
 * 
 * @author Gabriel Leonardo Diaz, 08.05.2013.
 */
public @Stateless class DiagramServiceBean implements DiagramService {
  
  @PersistenceContext(unitName="CLASSModelerPU")
  private EntityManager em;
  
  @EJB
  private UserService userService;
  
  @Override
  public Diagram insertDiagram(Diagram diagram) throws InvalidDiagrammerAccountException {
    if (diagram == null) {
      return null;
    }
    
    if (diagram.getCreatedBy() == null) {
      throw new InvalidDiagrammerAccountException("The owner is invalid.", EInvalidAccountErrorType.NON_EXISTING_ACCOUNT);
    }
    
    Diagrammer owner = userService.getDiagrammerByEmail(diagram.getCreatedBy().getEmail());
    if (owner == null) {
      throw new InvalidDiagrammerAccountException("The owner does not exist.", EInvalidAccountErrorType.NON_EXISTING_ACCOUNT);
    }
    
    if (owner.getAccountStatus() != EDiagrammerAccountStatus.ACTIVATED) {
      throw new InvalidDiagrammerAccountException("The owner account has not been activated.", EInvalidAccountErrorType.NON_ACTIVATED_ACCOUNT);
    }
    
    diagram.setCreatedDate(Calendar.getInstance().getTime());
    diagram.setModifiedBy(diagram.getCreatedBy());
    diagram.setModifiedDate(diagram.getCreatedDate());
    
    em.persist(diagram);
    
    return diagram;
  }
  
  @Override
  public Diagram updateDiagram(Diagram diagram) {
    if (diagram == null) {
      return null;
    }
    
    // TODO validate the diagrammer account the modify the diagram has privileges.
    
    diagram.setModifiedDate(Calendar.getInstance().getTime());
    
    return em.merge(diagram);
  }
  
  @Override
  public void deleteDiagram(int diagramKey) {
    
    // TODO validate the diagrammer account modifying the diagram has privileges.
    
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
  public void shareDiagram (Diagram diagram, List<Diagrammer> toDiagrammers, EDiagramPrivilege privilege) {
    // TODO GD
  }
  
  @Override
  public EDiagramPrivilege checkDiagramPrivilege(Diagram diagram, User user) {
    if (diagram.getKey() < 0) {
      // Non existing diagram, everybody is able to edit.
      return EDiagramPrivilege.OWNER;
    }
    
    if (!user.isRegisteredUser()) {
      // Guest user does not have privileges over existing diagrams.
      return null;
    }
    
    Diagrammer diagrammer = (Diagrammer) user;
    
    if (diagram.isOwner(diagrammer)) {
      return EDiagramPrivilege.OWNER;
    }
    
    // TODO GD
    return EDiagramPrivilege.OWNER;
  }
  
}
