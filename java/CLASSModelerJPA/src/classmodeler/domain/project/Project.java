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
 * Projects are containers of models created by the user, these organize the
 * models in a logical and customizable structure for the user.
 * 
 * @author Gabriel Leonardo Diaz, 17.03.2013.
 */
@Entity
@Table(name="project")
public class Project implements Serializable {
  
  private static final long serialVersionUID = 1L;
  
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="project_key", unique=true, nullable=false)
  private int key;
  
  @Column(name="project_name", nullable=false, length=255)
  private String name;
  
  @Lob
  @Column(name="project_description")
  private String description;
  
  // UNI-Directional many-to-one association to User
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_created_by", nullable = false)
  private User createdBy;
  
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name="project_created_date", nullable=false)
  private Date createdDate;
  
  // UNI-Directional many-to-one association to User
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="project_modified_by", nullable=false)
  private User modifiedBy;
  
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name="project_modified_date", nullable=false)
  private Date modifiedDate;
  
  @Lob
  @Column(name="project_xmi", nullable=false)
  private String projectXMI;

  public Project() {
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
  
  public String getDescription() {
    return description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  public User getCreatedBy() {
    return createdBy;
  }
  
  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }
  
  public Date getCreatedDate() {
    return createdDate;
  }
  
  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }
  
  public User getModifiedBy() {
    return modifiedBy;
  }
  
  public void setModifiedBy(User modifiedBy) {
    this.modifiedBy = modifiedBy;
  }
  
  public Date getModifiedDate() {
    return modifiedDate;
  }
  
  public void setModifiedDate(Date modifiedDate) {
    this.modifiedDate = modifiedDate;
  }
  
  public String getProjectXMI() {
    return projectXMI;
  }
  
  public void setProjectXMI(String projectXMI) {
    this.projectXMI = projectXMI;
  }

}