/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.project;

import java.util.Date;

import classmodeler.domain.uml.kernel.IElement;
import classmodeler.domain.user.User;

/**
 * Projects are containers of the diagrams created by the user, these organize
 * the diagrams in a logical and customizable structure for the user.
 * 
 * @author Gabriel Leonardo Diaz, 17.03.2013.
 * @version 1.0
 * @updated 24.03.2013 04:59:26 p.m.
 */
public class Project {

  private int key;
  private String name;
  private String description;
  private User createdBy;
  private Date createdDate;
  private User modifiedBy;
  private Date modifiedDate;
  private IElement rootElement;

  public Project() {
    // Empty construct
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
  
  public IElement getRootElement() {
    return rootElement;
  }
  
  public void setRootElement(IElement rootElement) {
    this.rootElement = rootElement;
  }

}