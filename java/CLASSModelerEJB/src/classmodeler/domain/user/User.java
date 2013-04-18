/****************************************************

  Universidad Francisco de Paula Santander UFPS
  Cúcuta, Colombia
  (c) 2013 by UFPS. All rights reserved.

 ****************************************************/

package classmodeler.domain.user;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * Domain bean that represents a person who is able to login in the system, who
 * has a nickname and a password to enter successfully in the system.
 * 
 * @author Gabriel Leonardo Diaz, 25.12.2012.
 */
@Entity
@Table(name = "user")
public class User implements Serializable {
  
  private static final long serialVersionUID = 1L;
  
  /**
   * The key of the user, this is an auto-numeric value given by a database sequence.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_key", unique = true, nullable = false)
  private int key;
  
  /**
   * The status account of this user.
   */
  @Enumerated(EnumType.STRING)
  @Column(name = "user_account_status", nullable = false, length = 15)
  private EUserAccountStatus accountStatus;
  
  /**
   * The date of birth of this user.
   */
  @Temporal(TemporalType.DATE)
  @Column(name = "user_birthdate")
  private Date birthdate;
  
  /**
   * The date in which was created this user.
   */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "user_created_date", nullable = false)
  private Date createdDate;
  
  /**
   * The email of the person owner of the user account, this is necessary to
   * contact with the person when he/she forget the password or any other
   * advice.
   */
  @Column(name = "user_email", nullable = false, length = 255)
  private String email;
  
  /**
   * The first name of the person owner of the user account.
   */
  @Column(name = "user_first_name", nullable = false, length = 50)
  private String firstName;
  
  /**
   * The gender of the person owner of the user account.
   */
  @Enumerated(EnumType.STRING)
  @Column(name = "user_gender", nullable = false, length = 10)
  private EGender gender;
  
  /**
   * The last name of the person owner of the user account.
   */
  @Column(name = "user_last_name", nullable = false, length = 50)
  private String lastName;
  
  /**
   * The password of the user, necessary to log in the system.
   */
  @Column(name = "user_password", nullable = false, length = 20)
  private String password;
  
  /**
   * The photo of the person owner of the user account.
   */
  @Column(name = "user_avatar", length = 255)
  private String avatar;
  
  public User() {
    super();
  }
  
  public int getKey() {
    return this.key;
  }
  
  public void setKey(int key) {
    this.key = key;
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
  
  public String getAvatar() {
    return this.avatar;
  }
  
  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }
  /*
  @Override
  public String getName() {
    return "";
  }
  
  @Override
  public String getNickname() {
    return "";
  }
  
  @Override
  public boolean isRegisteredUser() {
    return true;
  }*/
  
}