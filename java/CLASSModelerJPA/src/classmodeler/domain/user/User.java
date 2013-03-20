/****************************************************

  Universidad Francisco de Paula Santander UFPS
  Cúcuta, Colombia
  (c) 2013 by UFPS. All rights reserved.

 ****************************************************/

package classmodeler.domain.user;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * Domain bean that represents a person who is able to login in the system, who
 * has a nickname and a password to enter successfully in the system.
 * 
 * @author Gabriel Leonardo Diaz, 25.12.2012.
 */
@Entity(name="user")
public class User implements Serializable {
  
  private static final long serialVersionUID = 1L;

  /**
   * The key of the user, this is an auto-numeric value given by a database sequence.
   */
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="user_key", insertable=false, nullable=false, updatable=true)
  private int key;

  /**
   * The status account of this user.
   */
  @Column(name="user_account_status")
  @Enumerated(EnumType.ORDINAL)
  private EUserAccountStatus accountStatus;

  /**
   * The date of birth of this user.
   */
  @Temporal(TemporalType.DATE)
  @Column(name="user_birthdate")
  private Date birthdate;

  /**
   * The date in which was created this user.
   */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name="user_created_date")
  private Date createdDate;

  /**
   * The email of the person owner of the user account, this is necessary to
   * contact with the person when he/she forget the password or any other
   * advice.
   */
  @Column(name="user_email")
  private String email;

  /**
   * The first name of the person owner of the user account.
   */
  @Column(name="user_first_name")
  private String firstName;

  /**
   * The gender of the person owner of the user account.
   */
  @Column(name="user_gender")
  private EGender gender;

  /**
   * The last name of the person owner of the user account.
   */
  @Column(name="user_last_name")
  private String lastName;

  /**
   * The password of the user, necessary to log in the system.
   */
  @Column(name="user_password")
  private String password;

  /**
   * The photo of the person owner of the user account.
   */
  @Column(name="user_photo")
  private String photo;

  public User() {
    super();
  }

  public int getKey() {
    return this.key;
  }

  public void setKey(int userKey) {
    this.key = userKey;
  }
  
  public String getFullName() {
    return getFirstName() + " " + getLastName();
  }

  public EUserAccountStatus getAccountStatus() {
    return this.accountStatus;
  }

  public void setAccountStatus(EUserAccountStatus accountStatus) {
    this.accountStatus = accountStatus;
  }

  public Date getBirthdate() {
    return this.birthdate;
  }

  public void setBirthdate(Date birthdate) {
    this.birthdate = birthdate;
  }

  public Date getCreatedDate() {
    return this.createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
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

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPhoto() {
    return this.photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
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
    
    User other = (User) obj;
    if (key != other.key) {
      return false;
    }
    
    return true;
  }

}