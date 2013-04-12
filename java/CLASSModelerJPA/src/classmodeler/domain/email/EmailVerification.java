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
import javax.persistence.FetchType;
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
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "email_ver_key", unique = true, nullable = false)
  private int key;
  
  @Column(name = "email_ver_code", nullable = false, length = 255)
  private String verificationCode;
  
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "email_ver_expiration", nullable = false)
  private Date expirationDate;
  
  // UNI-Directional many-to-one association to IUser
  @ManyToOne(fetch = FetchType.LAZY)
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
  
}