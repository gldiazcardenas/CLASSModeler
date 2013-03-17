/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.implementation;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnit;

import classmodeler.service.UserService;

import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion.User;

/**
 * Class that implements the operations (CRUD) to handle users.
 * 
 * @author Gabriel Leonardo Diaz, 02.03.2013.
 */
public @Stateless class UserServiceBean implements UserService {
  
  @PersistenceUnit(unitName="CLASSModelerJPA")
  private EntityManager em;
  
  @Override
  public boolean existsUser(String email) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public User activateUserAccount(User user) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public User insertUser(User user) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public User updateUser(User user) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void deleteUser(int userKey) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public User getUserByKey(int userKey) {
    // TODO Auto-generated method stub
    return null;
  }

}
