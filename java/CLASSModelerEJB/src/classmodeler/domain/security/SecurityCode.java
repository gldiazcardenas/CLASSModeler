/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.security;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import classmodeler.domain.user.Diagrammer;

/**
 * The persistent class for the {@code security_code} database table.
 * 
 * @author Gabriel Leonardo Diaz, 11.04.2013.
 */
@Entity
@Table(name = "security_code")
public class SecurityCode implements Serializable {
  
  private static final long serialVersionUID = 1L;
  
  /**
   * The identifier of the security code. This is an auto-incremental value.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "security_code_key", unique = true, nullable = false)
  private int key;
  
  /**
   * The security code hash representation.
   */
  @Column(name = "security_code_hash", nullable = false, length = 255)
  private String code;
  
  /**
   * The security code type.
   */
  @Enumerated(EnumType.ORDINAL)
  @Column (name="security_code_type", nullable = false, length = 1)
  private ESecurityCodeType type;
  
  /**
   * Whether the security code is valid or not.
   */
  @Column (name="security_code_valid", nullable = false)
  private boolean valid;
  
  /**
   * The expiration date of the hash code, when the code has expired the user
   * has to generate a new code.
   */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "security_code_expiration_date", nullable = false)
  private Date expirationDate;
  
  /**
   * UNI-Directional many-to-one association to Diagrammer. The diagrammer which the
   * confirmation code was generated for.
   */
  @ManyToOne
  @JoinColumn(name = "diagrammer_key", nullable = false)
  private Diagrammer diagrammer;
  
  public SecurityCode() {
    super();
  }
  
  public int getKey() {
    return key;
  }
  
  public void setKey(int key) {
    this.key = key;
  }
  
  public String getCode() {
    return code;
  }
  
  public void setCode(String code) {
    this.code = code;
  }
  
  public ESecurityCodeType getType() {
    return type;
  }
  
  public void setType(ESecurityCodeType type) {
    this.type = type;
  }
  
  public Date getExpirationDate() {
    return expirationDate;
  }
  
  public void setExpirationDate(Date expirationDate) {
    this.expirationDate = expirationDate;
  }
  
  public Diagrammer getDiagrammer() {
    return this.diagrammer;
  }
  
  public void setDiagrammer(Diagrammer diagrammer) {
    this.diagrammer = diagrammer;
  }
  
  public boolean isValid() {
    return valid;
  }
  
  public void setValid(boolean valid) {
    this.valid = valid;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + key;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
      
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
      
    SecurityCode other = (SecurityCode) obj;
    if (key != other.key) {
      return false;
    }
    
    return true;
  }
  
}