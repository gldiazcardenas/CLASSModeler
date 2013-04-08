/****************************************************

  Universidad Francisco de Paula Santander UFPS
  Cúcuta, Colombia
  (c) 2013 by UFPS. All rights reserved.

 ****************************************************/

package classmodeler.domain.user;

import java.util.Date;


/**
 * Domain bean that represents a person who is able to login in the system, who
 * has a nickname and a password to enter successfully in the system.
 * 
 * @author Gabriel Leonardo Diaz, 25.12.2012.
 */
public class User implements IUser {
  
  /**
   * The key of the user, this is an auto-numeric value given by a database sequence.
   */
  private int key;

  /**
   * The status account of this user.
   */
  private EUserAccountStatus accountStatus;

  /**
   * The date of birth of this user.
   */
  private Date birthdate;

  /**
   * The date in which was created this user.
   */
  private Date createdDate;

  /**
   * The email of the person owner of the user account, this is necessary to
   * contact with the person when he/she forget the password or any other
   * advice.
   */
  private String email;

  /**
   * The first name of the person owner of the user account.
   */
  private String firstName;

  /**
   * The gender of the person owner of the user account.
   */
  private EGender gender;

  /**
   * The last name of the person owner of the user account.
   */
  private String lastName;

  /**
   * The password of the user, necessary to log in the system.
   */
  private String password;

  /**
   * The photo of the person owner of the user account.
   */
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

	public String getName(){
		return "";
	}

}