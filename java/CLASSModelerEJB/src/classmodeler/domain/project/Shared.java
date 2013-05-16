/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.project;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import classmodeler.domain.user.User;

/**
 * Bean that represents a project shared between two users.
 * 
 * @author Gabriel Leonardo Diaz, 17.03.2013.
 */
@Entity
@Table(name="shared")
public class Shared implements Serializable {
  
  private static final long serialVersionUID = 1L;
  
  /**
   * The identifier of the sharing process of a project. This is an
   * auto-incremental value.
   */
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="shared_key", unique=true, nullable=false)
  private int key;
  
  /**
   * The date in which the project was shared.
   */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name="shared_date", nullable=false)
  private Date date;
  
  /**
   * A general comment made by the owner of the project.
   */
  @Lob
  @Column(name="shared_comment")
  private String comment;
  
  /**
   * The privileged given to the user who receives the project.
   */
  @Enumerated(EnumType.STRING)
  @Column(name="shared_privilege", nullable=false)
  private EProjectPrivilege privilege;
  
  /**
   * UNI-Directional many-to-one association to Project. This is the reference
   * to the shared project.
   */
  @ManyToOne
  @JoinColumn(name="shared_project_key", nullable=false)
  private Project project;
  
  /**
   * UNI-Directional many-to-one association to User. This is the reference of
   * the owner of the project.
   */
  @ManyToOne
  @JoinColumn(name="shared_from_user", nullable=false)
  private User fromUser;
  
  /**
   * UNI-Directional many-to-one association to User. This is the reference to
   * the user who receives the project.
   */
  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="shared_to_user", nullable=false)
  private User toUser;

  public Shared() {
    super();
  }
  
  public int getKey() {
    return key;
  }
  
  public void setKey(int key) {
    this.key = key;
  }
  
  public Date getDate() {
    return date;
  }
  
  public void setDate(Date date) {
    this.date = date;
  }
  
  public String getComment() {
    return comment;
  }
  
  public void setComment(String comment) {
    this.comment = comment;
  }
  
  public EProjectPrivilege getPrivilege() {
    return privilege;
  }
  
  public void setPrivilege(EProjectPrivilege privilege) {
    this.privilege = privilege;
  }
  
  public Project getProject() {
    return project;
  }
  
  public void setProject(Project project) {
    this.project = project;
  }
  
  public User getFromUser() {
    return fromUser;
  }
  
  public void setFromUser(User fromUser) {
    this.fromUser = fromUser;
  }
  
  public User getToUser() {
    return toUser;
  }
  
  public void setToUser(User toUser) {
    this.toUser = toUser;
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
      
    Shared other = (Shared) obj;
    if (key != other.key) {
      return false;
    }
    
    return true;
  }
  
}