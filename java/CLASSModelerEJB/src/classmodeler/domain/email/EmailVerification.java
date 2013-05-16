/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.email;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import classmodeler.domain.user.User;

/**
 * The persistent class for the email_verification database table.
 * 
 * @author Gabriel Leonardo Diaz, 11.04.2013.
 */
@Entity
@Table(name = "email_verification")
public class EmailVerification implements Serializable {
  
  private static final long serialVersionUID = 1L;
  
  /**
   * The identifier of the email verification. This is an auto-incremental value.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "verification_key", unique = true, nullable = false)
  private int key;
  
  /**
   * The hash code used to confirm the user account.
   */
  @Column(name = "verification_code", nullable = false, length = 255)
  private String verificationCode;
  
  /**
   * The expiration date of the hash code, when the code has expired the user
   * has to generate a new code.
   */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "verification_expiration_date", nullable = false)
  private Date expirationDate;
  
  /**
   * UNI-Directional many-to-one association to User. The user which the
   * confirmation code was generated for.
   */
  @ManyToOne
  @JoinColumn(name = "user_key", nullable = false)
  private User user;
  
  public EmailVerification() {
    super();
  }
  
  public int getKey() {
    return key;
  }
  
  public void setKey(int key) {
    this.key = key;
  }
  
  public String getVerificationCode() {
    return verificationCode;
  }
  
  public void setVerificationCode(String verificationCode) {
    this.verificationCode = verificationCode;
  }
  
  public Date getExpirationDate() {
    return expirationDate;
  }
  
  public void setExpirationDate(Date expirationDate) {
    this.expirationDate = expirationDate;
  }
  
  public User getUser() {
    return this.user;
  }
  
  public void setUser(User user) {
    this.user = user;
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
      
    EmailVerification other = (EmailVerification) obj;
    if (key != other.key) {
      return false;
    }
    
    return true;
  }
  
}