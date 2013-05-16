/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.preference;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import classmodeler.domain.user.User;

/**
 * Preferences are commonly values that store some configuration of the
 * application or specific user's configurations.
 * 
 * @author Gabriel Leonardo Diaz, 10.05.2013.
 */
public class Preference implements Serializable {

  private static final long serialVersionUID = 1L;
  
  /**
   * The identifier of the preference. This is an auto-incremental value.
   */
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="preference_key", unique=true, nullable=false)
  private int key;

  /**
   * The name of the preference.
   */
  @Column(name="preference_name", nullable=false, length=255)
  private String name;
  
  /**
   * The value of the preference.
   */
  @Column(name="preference_value", nullable=false, length=4000)
  private String value;
  
  /**
   * UNI-Directional many-to-one association to User. This is the reference to
   * the user that saved this preferences as its personal configurations. If
   * null the preference is a system preference.
   */
  @ManyToOne
  @JoinColumn(name="user_key")
  private User user;
  
  public Preference() {
    super();
  }

  public int getKey() {
    return key;
  }

  public void setKey(int key) {
    this.key = key;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public User getUser() {
    return user;
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
    
    Preference other = (Preference) obj;
    if (key != other.key) {
      return false;
    }
    
    return true;
  }
  
}
