/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS 
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.controllers;

import java.util.Date;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import classmodeler.domain.user.EGender;
import classmodeler.domain.user.User;
import classmodeler.service.UserService;
import classmodeler.service.exception.ExistingUserEmailException;
import classmodeler.service.exception.SendEmailException;
import classmodeler.service.util.GenericUtils;
import classmodeler.web.resources.JSFResourceBundle;
import classmodeler.web.util.JSFFormControllerBean;
import classmodeler.web.util.JSFGenericBean;
import classmodeler.web.util.JSFOutcomeUtil;


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
  private Date birthdate;
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

  public Date getBirthdate() {
    return birthdate;
  }

  public void setBirthdate(Date birthdate) {
    this.birthdate = birthdate;
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
      items[i++] = new SelectItem(g, JSFResourceBundle.getLocalizedMessage(g.getName()));
    }
    
    return items;
  }

  @Override
  public String actionPerformed() {
    String resultOutCome = null;
    
    if (isAllValid()) {
      User newUser = new User();
      newUser.setFirstName(firstName);
      newUser.setLastName(lastName);
      newUser.setBirthdate(birthdate);
      newUser.setGender(gender);
      newUser.setEmail(email);
      newUser.setPassword(password);
      newUser.setAvatar(JSFResourceBundle.GUEST_DEFAULT_IMAGE_URL);
      
      try {
        userService.insertUser(newUser);
        resultOutCome = JSFOutcomeUtil.SIGN_UP_CONFIRMATION;
      }
      catch (ExistingUserEmailException e) {
        addErrorMessage("customMessage", JSFResourceBundle.getLocalizedMessage("SIGN_UP_FORM_DUPLICATED_EMAIL_MESSAGE"), null);
      }
      catch (SendEmailException e) {
        addErrorMessage("customMessage", JSFResourceBundle.getLocalizedMessage("SIGN_UP_FORM_ACTIVATION_EMAIL_MESSAGE"), e.getMessage());
      }
      catch (RuntimeException e) { // For unexpected exceptions
        addErrorMessage("customMessage", JSFResourceBundle.getLocalizedMessage("UNEXPECTED_EXCEPTION_MESSAGE"), e.getMessage());
      }
    }
    
    return resultOutCome;
  }

  @Override
  public boolean isAllValid() {
    // Checks basic required information
    if (GenericUtils.isEmptyString(firstName) ||
        GenericUtils.isEmptyString(lastName) ||
        GenericUtils.isEmptyString(email) ||
        gender == null) {
      
      return false;
    }
    
    // Checks the email validity
    if (!GenericUtils.isValidEmail(email)) {
      return false;
    }
    
    // All is valid now, so we can create the user
    return true;
  }
  
}
