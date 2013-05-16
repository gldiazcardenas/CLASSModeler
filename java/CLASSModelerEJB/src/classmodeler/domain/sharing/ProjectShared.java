/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.sharing;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.Element;

import classmodeler.domain.project.Project;

/**
 * Class that represents a shared project by the user, this extends the basic
 * definition of the {@link Project} class by adding the UML MetaModel root
 * element and the listeners that processes the changes made by other users.
 * 
 * @author Gabriel Leonardo Diaz, 08.05.2013.
 */
public class ProjectShared extends Project {

  private static final long serialVersionUID = 1L;
  
  private Element rootElement;
  private List<IProjectChangeListener> listeners;
  
  public ProjectShared() {
    super();
    
    this.listeners = new ArrayList<IProjectChangeListener>();
  }
  
  public ProjectShared (Project project) {
    super();
    
    setKey(project.getKey());
    setProjectXMI(project.getProjectXMI());
  }
  
  public Element getRootElement() {
    return rootElement;
  }
  
  public void setRootElement(Element rootElement) {
    this.rootElement = rootElement;
  }
  
  public boolean addProjectChangeListener (IProjectChangeListener listener) {
    return this.listeners.add(listener);
  }
  
  public boolean removeProjectChangeListener (IProjectChangeListener listener) {
    return this.listeners.remove(listener);
  }
  
}
