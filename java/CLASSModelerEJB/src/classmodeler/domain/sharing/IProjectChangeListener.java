/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.sharing;

/**
 * Listener class that helps to load the changes made by other users.
 * 
 * @author Gabriel Leonardo Diaz, 08.05.2013.
 */
public interface IProjectChangeListener {
  
  public void projectChanged(Object sender);
}
