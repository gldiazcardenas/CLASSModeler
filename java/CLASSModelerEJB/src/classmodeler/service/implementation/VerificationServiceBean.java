/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.implementation;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.codec.digest.DigestUtils;

import classmodeler.domain.user.Diagrammer;
import classmodeler.domain.verification.EVerificationType;
import classmodeler.domain.verification.Verification;
import classmodeler.service.EmailService;
import classmodeler.service.VerificationService;
import classmodeler.service.util.CollectionUtils;

/**
 * Session bean implementation of verification service.
 *
 * @author Gabriel Leonardo Diaz, 18.05.2013.
 */
@Stateless
public class VerificationServiceBean implements VerificationService {

  @PersistenceContext(unitName="CLASSModelerPU")
  private EntityManager em;
  
  @EJB
  private EmailService emailService;
  
  @Override
  public Verification insertVerification(EVerificationType type, Diagrammer user) {
    Verification verification = new Verification();
    verification.setDiagrammer(user);
    verification.setType(type);
    verification.setExpirationDate(generateExpirationDate());
    verification.setCode(generateHashCodeMD5(user.getEmail()));
    verification.setValid(true);
    em.persist(verification);
    return verification;
  }
  
  @Override
  public Verification getVerificationCode(Diagrammer diagrammer, String code, EVerificationType type) {
    TypedQuery<Verification> query = em.createQuery("SELECT v FROM Verification v WHERE v.diagrammer = :diagrammer AND v.type = :verificationType AND v.code = :code", Verification.class);
    query.setParameter("diagrammer", diagrammer);
    query.setParameter("code", code);
    query.setParameter("verificationType", type);
    
    List<Verification> list = query.getResultList();
    
    if (CollectionUtils.isEmptyCollection(list)) {
      return null;
    }
    
    return list.get(0);
  }

  @Override
  public String generateHashCodeMD5(String email) {
    return DigestUtils.md5Hex(email + "+%+" + Calendar.getInstance().getTimeInMillis());
  }

  @Override
  public Date generateExpirationDate() {
    Date expirationDate = Calendar.getInstance().getTime();
    expirationDate.setTime(expirationDate.getTime() + 172800000L); // Adds 2 days
    return expirationDate;
  }
  
}
