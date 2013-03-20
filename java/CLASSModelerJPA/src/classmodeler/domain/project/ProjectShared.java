/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.project;

import java.util.Date;

import classmodeler.domain.user.User;

/**
 * Bean that represents sharing a project between two users.
 * 
 * @author Gabriel Leonardo Diaz, 17.03.2013.
 */
public class ProjectShared {

  private int key;
  private Date date;
  private String comment;
  private EProjectPrivilege privilege;
  private Project project;
  private User fromUser;
  private User toUser;

  public ProjectShared() {
    // Empty constructor
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