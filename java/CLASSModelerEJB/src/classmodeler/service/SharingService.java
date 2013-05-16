/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service;

import javax.ejb.Local;

import classmodeler.domain.sharing.ProjectShared;

/**
 * Service definition that contains the operations needed to interact with the
 * application, this encapsulates all business logic in a piece of code to
 * handle operations of Projects, Sharing and Code Generation.
 * 
 * @author Gabriel Leonardo Diaz, 06.05.2013.
 */
@Local
public interface SharingService {

  public void addSharedProject (ProjectShared projectShared);
}
