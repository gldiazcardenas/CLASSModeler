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
 * Bean that represents sharing a project between two users.
 * 
 * @author Gabriel Leonardo Diaz, 17.03.2013.
 */
@Entity
@Table(name="project_sharing")
public class ProjectSharing implements Serializable {
  
  private static final long serialVersionUID = 1L;
  
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="project_sharing_key", unique=true, nullable=false)
  private int key;
  
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name="project_sharing_date", nullable=false)
  private Date date;
  
  @Lob
  @Column(name="project_sharing_comment")
  private String comment;
  
  @Column(name="project_sharing_privilege_key", nullable=false)
  private EProjectPrivilege privilege;
  
  // UNI-Directional many-to-one association to Project
  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="project_sharing_project_key", nullable=false)
  private Project project;
  
  // UNI-Directional many-to-one association to User
  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="project_sharing_from_user", nullable=false)
  private User fromUser;
  
  // UNI-Directional many-to-one association to User
  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="project_sharing_to_user", nullable=false)
  private User toUser;

  public ProjectSharing() {
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
  
}