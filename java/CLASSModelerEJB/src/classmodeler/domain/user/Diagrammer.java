/****************************************************

  Universidad Francisco de Paula Santander UFPS
  C�cuta, Colombia
  (c) 2013 by UFPS. All rights reserved.

 ****************************************************/

package classmodeler.domain.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Domain bean that represents a person who is able to login in the system, who
 * has a nickname and a password to enter successfully in the system.
 * 
 * @author Gabriel Leonardo Diaz, 25.12.2012.
 */
@Entity
@Table(name = "diagrammer")
public class Diagrammer implements User {
  
  private static final long serialVersionUID = 1L;
  
  /**
   * The key of the user, this is an auto-incremental value.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "diagrammer_key", unique = true, nullable = false)
  private int key;
  
  /**
   * The email of the person owner of the diagrammer account, this is necessary to
   * contact with the person when he/she forget the password or any other
   * advice.
   */
  @Column(name = "diagrammer_email", nullable = false, length = 255)
  private String email;
  
  /**
   * The first name of the person owner of the diagrammer account.
   */
  @Column(name = "diagrammer_first_name", nullable = false, length = 50)
  private String firstName;
  
  /**
   * The last name of the person owner of the user account.
   */
  @Column(name = "diagrammer_last_name", nullable = false, length = 50)
  private String lastName;
  
  /**
   * The password of the diagrammer, necessary to be logged in the system.
   */
  @Column(name = "diagrammer_password", nullable = false, length = 20)
  private String password;
  
  /**
   * The gender of the person owner of the diagrammer account.
   */
  @Enumerated(EnumType.ORDINAL)
  @Column(name = "diagrammer_gender", nullable = false, length = 1)
  private EGender gender;
  
  /**
   * The photo of the person owner of the user account.
   */
  @Column(name = "diagrammer_avatar_path", length = 255)
  private String avatar;
  
  /**
   * The status account of this diagrammer.
   */
  @Enumerated(EnumType.ORDINAL)
  @Column(name = "diagrammer_account_status", nullable = false, length = 1)
  private EDiagrammerAccountStatus accountStatus;
  
  /**
   * The date in which was created this diagrammer.
   */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "diagrammer_registration_date", nullable = false)
  private Date registrationDate;
  
  public Diagrammer() {
    super();
  }
  
  public int getKey() {
    return this.key;
  }
  
  public void setKey(int key) {
    this.key = key;
  }
  
  public EDiagrammerAccountStatus getAccountStatus() {
    return this.accountStatus;
  }
  
  public void setAccountStatus(EDiagrammerAccountStatus accountStatus) {
    this.accountStatus = accountStatus;
  }
  
  public Date getRegistrationDate() {
    return this.registrationDate;
  }
  
  public void setRegistrationDate(Date registrationDate) {
    this.registrationDate = registrationDate;
  }
  
  public String getEmail() {
    return this.email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
  public String getFirstName() {
    return this.firstName;
  }
  
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  
  public EGender getGender() {
    return this.gender;
  }
  
  public void setGender(EGender gender) {
    this.gender = gender;
  }
  
  public String getLastName() {
    return this.lastName;
  }
  
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  
  @Override
  public String getPassword() {
    return this.password;
  }
  
  public void setPassword(String password) {
    this.password = password;
  }
  
  @Override
  public String getAvatar() {
    return this.avatar;
  }
  
  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }
  
  @Override
  public String getName() {
    return firstName + " " + lastName;
  }
  
  @Override
  public boolean isRegisteredUser() {
    return true;
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
      
    Diagrammer other = (Diagrammer) obj;
    if (key != other.key) {
      return false;
    }
    
    return true;
  }
  
}