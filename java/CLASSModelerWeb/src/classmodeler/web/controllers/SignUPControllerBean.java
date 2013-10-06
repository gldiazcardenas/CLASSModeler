/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS 
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.controllers;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import classmodeler.domain.user.EGender;
import classmodeler.domain.user.Diagrammer;
import classmodeler.service.UserService;
import classmodeler.service.exception.InvalidDiagrammerAccountException;
import classmodeler.service.exception.SendEmailException;
import classmodeler.service.util.GenericUtils;
import classmodeler.web.util.JSFFormControllerBean;
import classmodeler.web.util.JSFGenericBean;


/**
 * JSF bean controller for the signing up process, this manages all the fields
 * in the form and allows to register the user by using the service.
 * 
 * @author Gabriel Leonardo Diaz, 23.02.2013.
 */
@ManagedBean (name="signUPController")
@ViewScoped
public class SignUPControllerBean extends JSFGenericBean implements JSFFormControllerBean {

  private static final long serialVersionUID = 1L;
  
  // Fields
  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private EGender gender;
  
  // Services
  @EJB
  private UserService userService;

  public SignUPControllerBean() {
    super();
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public EGender getGender() {
    return gender;
  }

  public void setGender(EGender gender) {
    this.gender = gender;
  }
  
  /**
   * Gets the items for the gender select radio GUI component.
   * @return An array with the items of the select radio
   */
  public SelectItem[] getGendersForSelectOneRadio() {
    SelectItem[] items = new SelectItem[EGender.values().length];

    int i = 0;
    for (EGender g : EGender.values()) {
      items[i++] = new SelectItem(g, GenericUtils.getLocalizedMessage(g.getName()));
    }
    
    return items;
  }

  @Override
  public String process() {
    return null;
  }
  
  @Override
  public void processAJAX() {
    if (!isAllValid()) {
      return;
    }
    
    Diagrammer newUser = new Diagrammer();
    newUser.setFirstName(firstName);
    newUser.setLastName(lastName);
    newUser.setGender(gender);
    newUser.setEmail(email);
    newUser.setPassword(password);
    
    if (gender == EGender.MALE) {
      newUser.setAvatar(GenericUtils.DEFAULT_MALE_IMAGE_URL);
    }
    else {
      newUser.setAvatar(GenericUtils.DEFAULT_FEMALE_IMAGE_URL);
    }
    
    try {
      userService.insertDiagrammer(newUser);
      addInformationMessage(JSFGenericBean.GENERAL_MESSAGE_ID, GenericUtils.getLocalizedMessage("SIGN_UP_CONFIRMATION_MESSAGE"), null);
      
      // Clears all previous information
      firstName = null;
      lastName  = null;
      gender    = null;
      email     = null;
      password  = null;
    }
    catch (InvalidDiagrammerAccountException e) {
      addErrorMessage(JSFGenericBean.GENERAL_MESSAGE_ID, GenericUtils.getLocalizedMessage("INVALID_ACCOUNT_DUPLICATED_MESSAGE"), null);
    }
    catch (SendEmailException e) {
      addErrorMessage(JSFGenericBean.GENERAL_MESSAGE_ID, GenericUtils.getLocalizedMessage("SEND_ACTIVATION_EMAIL_MESSAGE"), e.getMessage());
    }
  }

  @Override
  public boolean isAllValid() {
    if (gender == null) {
      return false;
    }
    
    // Checks basic required information
    if (GenericUtils.isEmptyString(firstName) ||
        GenericUtils.isEmptyString(lastName) ||
        GenericUtils.isEmptyString(email)) {
      return false;
    }
    
    // Checks the email and password validity
    if (!GenericUtils.isValidEmail(email) ||
        !GenericUtils.isValidPassword(password)) {
      return false;
    }
    
    // All is valid now, so we can create the user
    return true;
  }
  
}
