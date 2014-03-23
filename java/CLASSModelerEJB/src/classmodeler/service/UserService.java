/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/
package classmodeler.service;

import java.util.List;

import javax.ejb.Local;

import classmodeler.domain.diagram.Diagram;
import classmodeler.domain.user.Diagrammer;
import classmodeler.domain.user.User;
import classmodeler.service.bean.InsertDiagrammerResult;
import classmodeler.service.exception.ExpiredSecurityCodeException;
import classmodeler.service.exception.InvalidDiagrammerAccountException;
import classmodeler.service.exception.InvalidSecurityCodeException;
import classmodeler.service.exception.SendEmailException;

/**
 * Service definition that contains all the operations to handle users in the
 * application.
 * 
 * @author Gabriel Leonardo Diaz, 02.03.2013.
 */
@Local
public interface UserService {
  
  /**
   * Service method that validates the user credentials and determines if the
   * user is able to get logged in the system.
   * 
   * @param email
   *          The email of the user.
   * @param password
   *          The password of the user account.
   * @return The user logged in.
   * @throws InvalidDiagrammerAccountException
   *           When the user account does not exist or is invalid.
   * @author Gabriel Leonardo Diaz, 01.10.2013.
   */
  User logIn (String email, String password) throws InvalidDiagrammerAccountException;
  
  /**
   * Service method that checks if the given user email already exists.
   * 
   * @param email
   *          The email of the user to check.
   * @return A <code>boolean</code> value indicating if the user email exists or
   *         not.
   * @author Gabriel Leonardo Diaz, 02.03.2013.
   */
  public boolean existsUser (String email);
  
  /**
   * Sends the link to reset the password of the user account represented by the
   * given email.
   * 
   * @param email
   *          The user email.
   * @throws InvalidDiagrammerAccountException
   *           When the user account for the given email is invalid. Doesn't
   *           exist or is deactivated.
   * @throws SendEmailException
   *           When the system is not able to send an email to the user address.
   * @author Gabriel Leonardo Diaz, 21.05.2013.
   */
  public void requestResetPassword (String email) throws InvalidDiagrammerAccountException, SendEmailException;
  
  /**
   * Validates the given email and hash code used to reset the user account
   * password.
   * 
   * @param email
   *          The user email.
   * @param code
   *          The hash code sent to the email address.
   * @return A boolean indicating whether the hash code is valid or not.
   * @throws InvalidDiagrammerAccountException
   *           When the user account doesn't exist or it's deactivated.
   * @throws InvalidSecurityCodeException
   *           When the system doesn't found a verification code that matches
   *           with the given one.
   * @throws ExpiredSecurityCodeException
   *           When the verification code has expired, in this case the system
   *           generates a new one and sends it to the user email address.
   * @throws SendEmailException
   *           When the system is not able to re-send the verification code.
   * @author Gabriel Leonardo Diaz, 24.05.2013.
   */
  public boolean isValidToResetPassword (String email, String code) throws InvalidDiagrammerAccountException,
                                                                           InvalidSecurityCodeException,
                                                                           ExpiredSecurityCodeException,
                                                                           SendEmailException;
  
  /**
   * Activates the diagrammer account of the given email address and
   * verification code..
   * 
   * @param email
   *          The email of diagrammer to activate its account.
   * @param securityCode
   *          The code generated to activate the account.
   * @return The user after setting the account status.
   * @throws InvalidDiagrammerAccountException
   *           When the user account for the given email doesn't exist, was
   *           already ACTIVATED or was DEACTIVATED by the user.
   * @throws InvalidSecurityCodeException
   *           When the system doesn't found a verification code that matches
   *           with the given one.
   * @throws ExpiredSecurityCodeException
   *           When the verification code has expired, in this case the system
   *           generates a new one and sends it to the user email address.
   * @throws SendEmailException
   *           The system is not able to re-send the activation code when the
   *           current has expired.
   * @author Gabriel Leonardo Diaz, 14.03.2013
   */
  public Diagrammer activateDiagrammerAccount (String email, String securityCode) throws InvalidDiagrammerAccountException,
                                                                                         InvalidSecurityCodeException,
                                                                                         ExpiredSecurityCodeException,
                                                                                         SendEmailException;
  
  /**
   * Resets the user password with a new one.
   * 
   * @param email
   *          The user email.
   * @param newPassword
   *          The new password for the user account.
   * @return The user updated.
   * @throws InvalidDiagrammerAccountException
   *           When the user email doesn't exist or it is deactivated.
   * @author Gabriel Leonardo Diaz, 25.05.2013.
   */
  public Diagrammer resetPassword (String email, String newPassword) throws InvalidDiagrammerAccountException;
  
  /**
   * Inserts the given diagrammer into the database. This method also creates the diagrammer
   * account verification code and sends it to the user email address.
   * 
   * @param diagrammer
   *          The new diagrammer to save.
   * @return The bean with the information after inserting in the database.
   * @throws InvalidDiagrammerAccountException
   *           When the user that is being inserted has an already existing
   *           email in database.
   * @throws SendEmailException
   *           When the system is not able to send the activation email.
   * @author Gabriel Leonardo Diaz, 14.03.2013
   */
  public InsertDiagrammerResult insertDiagrammer (Diagrammer diagrammer) throws InvalidDiagrammerAccountException, SendEmailException;
  
  /**
   * Updates the fields of the given diagrammer into the database.
   * 
   * @param diagrammer
   *          The diagrammer to update its information.
   * @return The diagrammer bean after setting the fields.
   * @author Gabriel Leonardo Diaz, 14.03.2013
   */
  public Diagrammer updateDiagrammer (Diagrammer diagrammer);
  
  /**
   * Deletes the information of the given diagrammer key from database.
   * 
   * @param diagrammerKey
   *          The key of the diagrammer to delete.
   * @author Gabriel Leonardo Diaz, 14.03.2013
   */
  public void deleteDiagrammer (int diagrammerKey);
  
  /**
   * Gets the diagrammer bean by its key.
   * 
   * @param diagrammerKey
   *          The key of the user to get.
   * @return A user bean.
   * @author Gabriel Leonardo Diaz, 14.03.2013
   */
  public Diagrammer getDiagrammerByKey (int diagrammerKey);
  
  /**
   * Gets the diagrammer who owns the given email.
   * 
   * @param email
   *          The email of the diagrammer.
   * @return The user that has the email associated.
   * @author Gabriel Leonardo Diaz, 18.05.2013.
   */
  public Diagrammer getDiagrammerByEmail (String email);
  
  /**
   * Retrieves a list of diagrammers that have not the given diagram viewed.
   * 
   * @param diagram
   *          The diagram to share.
   * @return A list of users, never null.
   * @author Gabriel Leonardo Diaz, 27.07.2013.
   */
  public List<Diagrammer> getDiagrammersAllowedToShareDiagram (Diagram diagram);
  
}
