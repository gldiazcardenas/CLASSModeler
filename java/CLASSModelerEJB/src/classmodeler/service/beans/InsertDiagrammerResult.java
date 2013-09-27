/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.beans;

import java.io.Serializable;

import classmodeler.domain.user.Diagrammer;
import classmodeler.domain.verification.Verification;

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
  private Verification verification;
  
  public InsertDiagrammerResult() {
    super();
  }
  
  public InsertDiagrammerResult (Diagrammer diagrammer, Verification verification) {
    this ();
    this.diagrammer   = diagrammer;
    this.verification = verification;
  }
  
  public Diagrammer getDiagrammer() {
    return diagrammer;
  }
  
  public void setDiagrammer(Diagrammer diagrammer) {
    this.diagrammer = diagrammer;
  }
  
  public Verification getVerification() {
    return verification;
  }
  
  public void setVerification(Verification verification) {
    this.verification = verification;
  }

}
