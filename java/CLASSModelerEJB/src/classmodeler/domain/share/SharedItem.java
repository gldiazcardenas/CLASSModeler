/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.share;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import classmodeler.domain.diagram.Diagram;
import classmodeler.domain.user.Diagrammer;

/**
 * Bean that encapsulates the information of a shared diagram.
 * 
 * @author Gabriel Leonardo Diaz, 17.03.2013.
 */
@Entity
@Table(name="shared_item")
public class SharedItem implements Serializable {
  
  private static final long serialVersionUID = 1L;
  
  /**
   * The identifier of the sharing process of a diagram. This is an
   * auto-incremental value.
   */
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="shared_item_key", unique=true, nullable=false)
  private int key;
  
  /**
   * The date in which the diagram was shared.
   */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name="shared_item_date", nullable=false)
  private Date date;
  
  /**
   * The privileged given to the user who receives the diagram.
   */
  @Column(name="shared_item_writeable", nullable=false)
  private boolean writeable;
  
  /**
   * UNI-Directional many-to-one association to diagram. This is the reference
   * to the shared diagram.
   */
  @ManyToOne
  @JoinColumn(name="shared_item_diagram_key", nullable=false)
  private Diagram diagram;
  
  /**
   * UNI-Directional many-to-one association to User. This is the reference of
   * the diagrammer that the diagram was shared to.
   */
  @ManyToOne
  @JoinColumn(name="shared_item_diagrammer_key", nullable=false)
  private Diagrammer diagrammer;

  public SharedItem() {
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
  
  public boolean isWriteable() {
    return writeable;
  }
  
  public void setWriteable(boolean writeable) {
    this.writeable = writeable;
  }
  
  public Diagram getDiagram() {
    return diagram;
  }
  
  public void setDiagram(Diagram diagram) {
    this.diagram = diagram;
  }
  
  public Diagrammer getDiagrammer() {
    return diagrammer;
  }
  
  public void setDiagrammer(Diagrammer diagrammer) {
    this.diagrammer = diagrammer;
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
      
    SharedItem other = (SharedItem) obj;
    if (key != other.key) {
      return false;
    }
    
    return true;
  }
  
}