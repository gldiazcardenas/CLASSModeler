/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * C�cuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.diagram;

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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import classmodeler.domain.user.Diagrammer;

/**
 * Bean that represents a diagram shared between two users.
 * 
 * @author Gabriel Leonardo Diaz, 17.03.2013.
 */
@Entity
@Table(name="shared")
public class Shared implements Serializable {
  
  private static final long serialVersionUID = 1L;
  
  /**
   * The identifier of the sharing process of a diagram. This is an
   * auto-incremental value.
   */
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="shared_key", unique=true, nullable=false)
  private int key;
  
  /**
   * The date in which the diagram was shared.
   */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name="shared_date", nullable=false)
  private Date date;
  
  /**
   * The privileged given to the user who receives the diagram.
   */
  @Enumerated(EnumType.STRING)
  @Column(name="shared_privilege", nullable=false)
  private EDiagramPrivilege privilege;
  
  /**
   * UNI-Directional many-to-one association to diagram. This is the reference
   * to the shared diagram.
   */
  @ManyToOne
  @JoinColumn(name="shared_diagram_key", nullable=false)
  private Diagram diagram;
  
  /**
   * UNI-Directional many-to-one association to User. This is the reference of
   * the owner of the diagram.
   */
  @ManyToOne
  @JoinColumn(name="shared_from_diagrammer", nullable=false)
  private Diagrammer fromDiagrammer;
  
  /**
   * UNI-Directional many-to-one association to User. This is the reference to
   * the user who receives the diagram.
   */
  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="shared_to_diagrammer", nullable=false)
  private Diagrammer toDiagrammer;

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
  
  public EDiagramPrivilege getPrivilege() {
    return privilege;
  }
  
  public void setPrivilege(EDiagramPrivilege privilege) {
    this.privilege = privilege;
  }
  
  public Diagram getDiagram() {
    return diagram;
  }
  
  public void setDiagram(Diagram diagram) {
    this.diagram = diagram;
  }
  
  public Diagrammer getFromDiagrammer() {
    return fromDiagrammer;
  }
  
  public void setFromDiagrammer(Diagrammer fromDiagrammer) {
    this.fromDiagrammer = fromDiagrammer;
  }
  
  public Diagrammer getToDiagrammer() {
    return toDiagrammer;
  }
  
  public void setToDiagrammer(Diagrammer toDiagrammer) {
    this.toDiagrammer = toDiagrammer;
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