/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.service.implementation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import classmodeler.domain.diagram.Diagram;
import classmodeler.domain.security.ESecurityCodeType;
import classmodeler.domain.security.SecurityCode;
import classmodeler.domain.user.Diagrammer;
import classmodeler.domain.user.EDiagrammerAccountStatus;
import classmodeler.service.EmailService;
import classmodeler.service.UserService;
import classmodeler.service.SecurityService;
import classmodeler.service.bean.InsertDiagrammerResult;
import classmodeler.service.exception.ExpiredSecurityCodeException;
import classmodeler.service.exception.InvalidDiagrammerAccountException;
import classmodeler.service.exception.InvalidDiagrammerAccountException.EInvalidAccountErrorType;
import classmodeler.service.exception.InvalidSecurityCodeException;
import classmodeler.service.exception.SendEmailException;
import classmodeler.service.util.CollectionUtils;
import classmodeler.service.util.GenericUtils;

/**
 * Class that implements the operations (CRUD) to handle users.
 * 
 * @author Gabriel Leonardo Diaz, 02.03.2013.
 */
public @Stateless class UserServiceBean implements UserService {
  
  @PersistenceContext(unitName="CLASSModelerPU")
  private EntityManager em;
  
  @EJB
  private SecurityService securityService;
  
  @EJB
  private EmailService emailService;
  
  @Override
  public boolean existsUser(String email) {
    return getDiagrammerByEmail(email) != null;
  }

  @Override
  public Diagrammer activateDiagrammerAccount(String email, String verificationCode) throws InvalidDiagrammerAccountException,
                                                                                            InvalidSecurityCodeException,
                                                                                            ExpiredSecurityCodeException,
                                                                                            SendEmailException {
    Diagrammer diagrammer = getDiagrammerByEmail(email);
    
    if (diagrammer == null) {
      throw new InvalidDiagrammerAccountException("The user doesn't exist.", EInvalidAccountErrorType.NON_EXISTING_ACCOUNT);
    }
    
    if (diagrammer.getAccountStatus() == EDiagrammerAccountStatus.ACTIVATED) {
      throw new InvalidDiagrammerAccountException("The user account is already activated.", EInvalidAccountErrorType.ACTIVATED_ACCOUNT);
    }
    
    if (diagrammer.getAccountStatus() == EDiagrammerAccountStatus.DEACTIVATED) {
      throw new InvalidDiagrammerAccountException("The user account is deactivated.", EInvalidAccountErrorType.DEACTIVATED_ACCOUNT);
    }
    
    SecurityCode securityCode = securityService.getSecurityCode(diagrammer, verificationCode, ESecurityCodeType.ACTIVATE_ACCOUNT);
    
    if (securityCode == null) {
      throw new InvalidSecurityCodeException("The verification code was not found for the user.");
    }
    
    // The code won't be valid any more.
    securityCode.setValid(false);
    em.merge(securityCode);
    
    if (securityCode.getExpirationDate().before(Calendar.getInstance().getTime())) {
      // The verification code has expired, the system should generate a new one.
      securityCode = securityService.insertSecurityCode(ESecurityCodeType.ACTIVATE_ACCOUNT, diagrammer);
      
      // Sends the email with the new activation code.
      emailService.sendAccountActivationEmail(diagrammer, securityCode);
      
      // This doesn't trigger a RollBack operation.
      throw new ExpiredSecurityCodeException("The activation code has expired.");
    }
    
    // Activates the user account.
    diagrammer.setAccountStatus(EDiagrammerAccountStatus.ACTIVATED);
    em.merge(diagrammer);
    
    return diagrammer;
  }
  
  @Override
  public boolean isValidToResetPassword(String email, String code) throws InvalidDiagrammerAccountException,
                                                                          InvalidSecurityCodeException,
                                                                          ExpiredSecurityCodeException,
                                                                          SendEmailException {
    Diagrammer diagrammer = getDiagrammerByEmail(email);
    
    if (diagrammer == null) {
      throw new InvalidDiagrammerAccountException("The user account doesn't exist", EInvalidAccountErrorType.NON_EXISTING_ACCOUNT);
    }
    
    if (diagrammer.getAccountStatus() == EDiagrammerAccountStatus.DEACTIVATED) {
      throw new InvalidDiagrammerAccountException("The use account is deactivated.", EInvalidAccountErrorType.DEACTIVATED_ACCOUNT);
    }
    
    // Gets the verification code.
    SecurityCode securityCode = securityService.getSecurityCode(diagrammer, code, ESecurityCodeType.PASSWORD_RESET);
    
    if (securityCode == null) {
      throw new InvalidSecurityCodeException("The verification code was not found for the user.");
    }
    
    // Verifies if the code has expired or was already used.
    if (securityCode.isValid() && securityCode.getExpirationDate().before(Calendar.getInstance().getTime())) {
      // Invalidates the current code.
      securityCode.setValid(false);
      em.merge(securityCode);
      
      // Generates a new code.
      securityCode = securityService.insertSecurityCode(ESecurityCodeType.PASSWORD_RESET, diagrammer);
      
      // Sends the email.
      emailService.sendResetPasswordEmail(diagrammer, securityCode);
      
      throw new ExpiredSecurityCodeException("The verification code has expired.");
    }
    
    return securityCode.isValid();
  }
  
  @Override
  public void requestResetPassword (String email) throws InvalidDiagrammerAccountException, SendEmailException {
    Diagrammer user = getDiagrammerByEmail(email);
    
    if (user == null) {
      throw new InvalidDiagrammerAccountException("The user account doesn't exist.", EInvalidAccountErrorType.NON_EXISTING_ACCOUNT);
    }
    
    if (user.getAccountStatus() == EDiagrammerAccountStatus.DEACTIVATED) {
      throw new InvalidDiagrammerAccountException("The user account is deactivated", EInvalidAccountErrorType.DEACTIVATED_ACCOUNT);
    }
    
    // Creates the verification code.
    SecurityCode securityCode = securityService.insertSecurityCode(ESecurityCodeType.PASSWORD_RESET, user);
    
    // Sends the link to reset the password.
    emailService.sendResetPasswordEmail(user, securityCode);
  }
  
  @Override
  public Diagrammer resetPassword(String email, String newPassword) throws InvalidDiagrammerAccountException {
    Diagrammer user = getDiagrammerByEmail(email);
    
    if (user == null) {
      throw new InvalidDiagrammerAccountException("The user account doesn't exist.", EInvalidAccountErrorType.NON_EXISTING_ACCOUNT);
    }
    
    if (user.getAccountStatus() == EDiagrammerAccountStatus.DEACTIVATED) {
      throw new InvalidDiagrammerAccountException("The user account is deactivated.", EInvalidAccountErrorType.DEACTIVATED_ACCOUNT);
    }
    
    user.setPassword(newPassword);
    em.merge(user);
    
    return user;
  }

  @Override
  public InsertDiagrammerResult insertDiagrammer(Diagrammer diagrammer) throws InvalidDiagrammerAccountException, SendEmailException {
    if (existsUser(diagrammer.getEmail())) {
      throw new InvalidDiagrammerAccountException("The email already exists.", EInvalidAccountErrorType.DUPLICATED_ACCOUNT);
    }
    
    if (!GenericUtils.isValidEmail(diagrammer.getEmail())) {
      throw new InvalidDiagrammerAccountException("The email is not valid.");
    }
    
    if (!GenericUtils.isValidPassword(diagrammer.getPassword())) {
      throw new InvalidDiagrammerAccountException("The password is not valid.");
    }
    
    // Inserts the user
    diagrammer.setAccountStatus(EDiagrammerAccountStatus.INACTIVATED);
    diagrammer.setRegistrationDate(Calendar.getInstance().getTime());
    em.persist(diagrammer);
    
    // Inserts the verification
    SecurityCode securityCode = securityService.insertSecurityCode(ESecurityCodeType.ACTIVATE_ACCOUNT, diagrammer);
    
    // Sends the activation email
    emailService.sendAccountActivationEmail(diagrammer, securityCode);
    
    return new InsertDiagrammerResult(diagrammer, securityCode);
  }

  @Override
  public Diagrammer updateDiagrammer(Diagrammer user) {
    Diagrammer existingUser = em.find(Diagrammer.class, Integer.valueOf(user.getKey()));
    if (existingUser != null) {
      em.merge(user);
    }
    return user;
  }

  @Override
  public void deleteDiagrammer(int userKey) {
    Diagrammer user = em.find(Diagrammer.class, Integer.valueOf(userKey));
    if (user != null) {
      em.remove(user);
    }
  }

  @Override
  public Diagrammer getDiagrammerByKey(int userKey) {
    return em.find(Diagrammer.class, Integer.valueOf(userKey));
  }
  
  @Override
  public Diagrammer getDiagrammerByEmail(String email) {
    Diagrammer diagrammer = null;
    if (!GenericUtils.isEmptyString(email)) {
      TypedQuery<Diagrammer> query = em.createQuery("SELECT d FROM Diagrammer d WHERE LOWER(d.email) = :diagrammerEmail", Diagrammer.class);
      query.setParameter("diagrammerEmail", email.toLowerCase());
      
      List<Diagrammer> diagrammerList = query.getResultList();
      
      if (!CollectionUtils.isEmptyCollection(diagrammerList)) {
        diagrammer = diagrammerList.get(0);
      }
    }
    
    return diagrammer;
  }
  
  @Override
  @SuppressWarnings("unchecked")
  public List<Diagrammer> getDiagrammersAllowedToShareDiagram(Diagram diagram) {
    List<Diagrammer> diagrammers = new ArrayList<Diagrammer>();
    
    if (diagram != null) {
      diagrammers = em.createNativeQuery("SELECT * FROM diagrammer WHERE diagrammer_key <> ?ownerKey AND diagrammer_key NOT IN (SELECT shared_to_diagrammer FROM shared WHERE shared_diagram_key = ?diagramKey)", Diagrammer.class)
                .setParameter("ownerKey", Integer.valueOf(diagram.getCreatedBy().getKey()))
                .setParameter("diagramKey", Integer.valueOf(diagram.getKey()))
                .getResultList();
    }
    
    return diagrammers;
  }

}
