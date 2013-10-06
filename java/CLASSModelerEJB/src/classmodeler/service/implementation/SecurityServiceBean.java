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

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.codec.digest.DigestUtils;

import classmodeler.domain.security.ESecurityCodeType;
import classmodeler.domain.security.SecurityCode;
import classmodeler.domain.user.Diagrammer;
import classmodeler.service.SecurityService;
import classmodeler.service.util.CollectionUtils;

/**
 * Session bean implementation for {@link SecurityService}.
 *
 * @author Gabriel Leonardo Diaz, 18.05.2013.
 */
public @Stateless class SecurityServiceBean implements SecurityService {

  @PersistenceContext(unitName="CLASSModelerPU")
  private EntityManager em;
  
  @Override
  public SecurityCode insertSecurityCode(ESecurityCodeType type, Diagrammer diagrammer) {
    SecurityCode securityCode = new SecurityCode();
    securityCode.setDiagrammer(diagrammer);
    securityCode.setType(type);
    securityCode.setExpirationDate(generateExpirationDate());
    securityCode.setCode(generateHashCodeMD5(diagrammer.getEmail()));
    securityCode.setValid(true);
    em.persist(securityCode);
    return securityCode;
  }
  
  @Override
  public SecurityCode getSecurityCode(Diagrammer diagrammer, String code, ESecurityCodeType type) {
    TypedQuery<SecurityCode> query = em.createQuery("SELECT sc FROM SecurityCode sc WHERE sc.diagrammer = :diagrammer AND sc.type = :codeType AND sc.code = :code", SecurityCode.class);
    query.setParameter("diagrammer", diagrammer);
    query.setParameter("code", code);
    query.setParameter("codeType", type);
    
    List<SecurityCode> list = query.getResultList();
    
    if (!CollectionUtils.isEmptyCollection(list)) {
      return list.get(0);
    }
    
    return null;
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
