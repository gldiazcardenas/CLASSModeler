/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.implementation;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import classmodeler.domain.diagram.Diagram;
import classmodeler.domain.share.SharedItem;
import classmodeler.domain.user.Diagrammer;
import classmodeler.domain.user.EDiagrammerAccountStatus;
import classmodeler.domain.user.User;
import classmodeler.service.DiagramService;
import classmodeler.service.SourceCodeService;
import classmodeler.service.UserService;
import classmodeler.service.exception.InvalidDiagrammerAccountException;
import classmodeler.service.exception.InvalidDiagrammerAccountException.EInvalidAccountErrorType;
import classmodeler.service.exception.UnprivilegedException;
import classmodeler.service.util.CollectionUtils;

import com.mxgraph.reader.mxGraphViewImageReader;

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
  
  @EJB
  private SourceCodeService sourceCodeService;
  
  @Override
  public Diagram insertDiagram (Diagram diagram) throws InvalidDiagrammerAccountException {
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
  public Diagram updateDiagram (Diagram diagram) {
    if (diagram == null) {
      return null;
    }
    
    // TODO validate the diagrammer account the modify the diagram has privileges.
    
    diagram.setModifiedDate(Calendar.getInstance().getTime());
    
    return em.merge(diagram);
  }
  
  @Override
  public void deleteDiagram (int diagramKey) {
    
    // TODO validate the diagrammer account modifying the diagram has privileges.
    
    Diagram diagram = em.find(Diagram.class, Integer.valueOf(diagramKey));
    if (diagram != null) {
      em.remove(diagram);
    }
  }
  
  @Override
  public void deleteSharedItem(int diagramKey, int diagrammerKey) {
    // TODO validate the diagrammer account modifying the diagram has privileges.
    
    em.createNativeQuery("DELETE FROM shared_item WHERE shared_item_diagrammer_key = ?diagrammer AND shared_item_diagram_key = ?diagram")
      .setParameter("diagrammer", diagrammerKey)
      .setParameter("diagram", diagramKey)
      .executeUpdate();
  }
  
  @Override
  public void deleteSharedItem(int sharedItemKey) {
    // TODO validate the diagrammer account modifying the diagram has privileges.
    
    SharedItem sharedItem = em.find(SharedItem.class, Integer.valueOf(sharedItemKey));
    if (sharedItem != null) {
      em.remove(sharedItem);
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
  public void shareDiagram (Diagram diagram, List<Diagrammer> toDiagrammers, boolean canWrite) {
    SharedItem item;
    Date now = Calendar.getInstance().getTime();
    
    for (Diagrammer diagrammer : toDiagrammers) {
      item = new SharedItem();
      item.setDate(now);
      item.setDiagram(diagram);
      item.setDiagrammer(diagrammer);
      item.setWriteable(canWrite);
      
      em.persist(item);
    }
  }
  
  @Override
  public boolean canWriteDiagram(Diagram diagram, User user) throws UnprivilegedException {
    if (diagram.getKey() < 0) {
      // This is a fake diagram, so everybody can write on it.
      return true;
    }
    
    if (!user.isRegisteredUser()) {
      throw new UnprivilegedException("The user is not privileged for this diagram.");
    }
    
    Diagrammer diagrammer = (Diagrammer) user;
    if (diagram.isOwner(diagrammer)) {
      return true;
    }
    
    List<SharedItem> item = em.createQuery("SELECT si FROM SharedItem si WHERE si.diagram = :diagram AND si.diagrammer = :diagrammer", SharedItem.class)
                              .setParameter("diagram", diagram)
                              .setParameter("diagrammer", diagrammer)
                              .getResultList();
    
    if (CollectionUtils.isEmptyCollection(item)) {
      throw new UnprivilegedException("The user is not privileged for this diagram.");
    }
    
    // There should be only one item
    return item.get(0).isWriteable();
  }
  
  @Override
  public void generateImage(String rawXML, OutputStream output) throws ParserConfigurationException, SAXException, IOException {
    String xml = URLDecoder.decode(rawXML, "UTF-8");
    mxGraphViewImageReader reader = new mxGraphViewImageReader(Color.WHITE, 4, true, true);
    InputSource inputSource = new InputSource(new StringReader(xml));
    BufferedImage image = mxGraphViewImageReader.convert(inputSource, reader);
    ImageIO.write(image, "png", output);
  }
  
}
