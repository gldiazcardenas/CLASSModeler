/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.implementation;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import classmodeler.domain.user.Diagrammer;
import classmodeler.domain.user.EDiagrammerAccountStatus;
import classmodeler.domain.user.Guest;
import classmodeler.domain.user.User;
import classmodeler.service.SessionService;
import classmodeler.service.UserService;
import classmodeler.service.exception.InvalidDiagrammerAccountException;
import classmodeler.service.exception.InvalidDiagrammerAccountException.EInvalidAccountErrorType;

/**
 * The implementation of the session service.
 * 
 * @author Gabriel Leonardo Diaz, 01.10.2013.
 */
public @Stateless class SessionServiceBean implements SessionService {
  
  @EJB
  private UserService userService;
  
  @Override
  public User logIn(String email, String password) throws InvalidDiagrammerAccountException {
    if (Guest.GUEST_EMAIL.equals(email) && Guest.GUEST_PASSWORD.equals(password)) {
      return new Guest();
    }
    
    Diagrammer user = userService.getDiagrammerByEmail(email);
    
    if (user == null || !user.getPassword().equals(password)) {
      throw new InvalidDiagrammerAccountException("The user account doesn't exist", EInvalidAccountErrorType.NON_EXISTING_ACCOUNT);
    }
    
    if (user.getAccountStatus() != EDiagrammerAccountStatus.ACTIVATED) {
      throw new InvalidDiagrammerAccountException("The user account is not activated.", EInvalidAccountErrorType.NON_ACTIVATED_ACCOUNT);
    }
    
    return user;
  }
  
  @Override
  public void logOut(User user) {
    // TODO Auto-generated method stub
  }
  
}
