/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.diagram;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import classmodeler.domain.user.Diagrammer;

/**
 * Diagram are containers of UML elements created by the user, these organize the
 * elements in a logical and customizable structure for the user.
 * 
 * @author Gabriel Leonardo Diaz, 17.03.2013.
 */
@Entity
@Table(name="diagram")
public class Diagram implements Serializable {
  
  private static final long serialVersionUID = 1L;
  
  /**
   * The key of the diagram, this is used as the object identifier. This is an
   * auto-incremental value.
   */
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="diagram_key", unique=true, nullable=false)
  private int key;
  
  /**
   * The name of the diagram, this is a custom name the user assigns to its
   * diagram.
   */
  @Column(name="diagram_name", nullable=false, length=255)
  private String name;
  
  /**
   * A description of the purpose of this diagram or a general comment the user
   * makes about it.
   */
  @Lob
  @Column(name="diagram_description")
  private String description;
  
  /**
   * UNI-Directional many-to-one association to User. Basically this is the
   * owner of the diagram.
   */
  @ManyToOne
  @JoinColumn(name = "diagram_created_by", nullable = false)
  private Diagrammer createdBy;
  
  /**
   * The creation date of this diagram.
   */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name="diagram_created_date", nullable=false)
  private Date createdDate;
  
  /**
   * UNI-Directional many-to-one association to Diagrammer. This is the reference to
   * the last user who modified this diagram, if the project was shared this is
   * used to know who modified the diagram instead of the owner.
   */
  @ManyToOne
  @JoinColumn(name="diagram_modified_by", nullable=false)
  private Diagrammer modifiedBy;
  
  /**
   * Last modification date of this diagram.
   */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name="diagram_modified_date", nullable=false)
  private Date modifiedDate;
  
  /**
   * XMI representation of the diagram structure, this is a well formed XML document.
   */
  @Lob
  @Column(name="diagram_xml", nullable=false)
  private String XML;

  public Diagram() {
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
  
  public Diagrammer getCreatedBy() {
    return createdBy;
  }
  
  public void setCreatedBy(Diagrammer createdBy) {
    this.createdBy = createdBy;
  }
  
  public Date getCreatedDate() {
    return createdDate;
  }
  
  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }
  
  public Diagrammer getModifiedBy() {
    return modifiedBy;
  }
  
  public void setModifiedBy(Diagrammer modifiedBy) {
    this.modifiedBy = modifiedBy;
  }
  
  public Date getModifiedDate() {
    return modifiedDate;
  }
  
  public void setModifiedDate(Date modifiedDate) {
    this.modifiedDate = modifiedDate;
  }
  
  public String getXML() {
    return XML;
  }
  
  public void setXML(String XML) {
    this.XML = XML;
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
    
    Diagram other = (Diagram) obj;
    if (key != other.key) {
      return false;
    }
    
    return true;
  }
  
  public boolean isOwner (Diagrammer diagrammer) {
    return this.createdBy.equals(diagrammer);
  }

}