/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.bean;

import java.io.Serializable;

import classmodeler.domain.security.SecurityCode;
import classmodeler.domain.user.Diagrammer;

/**
 * Java bean used as result of calling the service
 * {@link classmodeler.service.UserService#insertDiagrammer(classmodeler.domain.user.Diagrammer)}
 * . This contains the new diagrammer and the verification code used to activate
 * the account.
 * 
 * @author Gabriel Leonardo Diaz, 26.09.2013.
 */
public class InsertDiagrammerResult implements Serializable {

  private static final long serialVersionUID = 1L;
  
  private Diagrammer diagrammer;
  private SecurityCode securityCode;
  
  public InsertDiagrammerResult() {
    super();
  }
  
  public InsertDiagrammerResult (Diagrammer diagrammer, SecurityCode securityCode) {
    this ();
    this.diagrammer   = diagrammer;
    this.securityCode = securityCode;
  }
  
  public Diagrammer getDiagrammer() {
    return diagrammer;
  }
  
  public void setDiagrammer(Diagrammer diagrammer) {
    this.diagrammer = diagrammer;
  }
  
  public SecurityCode getSecurityCode() {
    return securityCode;
  }
  
  public void setSecurityCode(SecurityCode securityCode) {
    this.securityCode = securityCode;
  }

}
