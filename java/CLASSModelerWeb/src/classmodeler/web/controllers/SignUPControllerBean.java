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
import classmodeler.domain.user.EUserAccountStatus;
import classmodeler.domain.user.User;
import classmodeler.service.UserService;
import classmodeler.service.util.GenericUtils;
import classmodeler.web.resources.JSFMessageBundle;
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
      items[i++] = new SelectItem(g, JSFMessageBundle.getLocalizedMessage(g.getName()));
    }
    
    return items;
  }

  @Override
  public void actionPerformed() {
    if (!isAllValid()) {
      return;
    }
    
    User newUser = createUserFromFields();
    
    try {
      userService.insertUser(newUser);
    }
    catch (Exception e) {
      addErrorMessage("Unexpected error: " + e.getMessage(), null);
      return;
    }
  }

  @Override
  public boolean isAllValid() {
    boolean valid = true;
    
    if (GenericUtils.isEmptyString(firstName)) {
      addErrorMessage(JSFMessageBundle.getLocalizedMessage("SIGN_UP_FORM_MANDATORY_FST_NAME_MESSAGE"), null);
      valid = false;
    }
    
    if (GenericUtils.isEmptyString(lastName)) {
      addErrorMessage(JSFMessageBundle.getLocalizedMessage("SIGN_UP_FORM_MANDATORY_LST_NAME_MESSAGE"), null);
      valid = false;
    }
    
    if (GenericUtils.isEmptyString(email)) {
      addErrorMessage(JSFMessageBundle.getLocalizedMessage("SIGN_UP_FORM_MANDATORY_EMAIL_MESSAGE"), null);
      valid = false;
    }
    
    if (gender == null) {
      addErrorMessage(JSFMessageBundle.getLocalizedMessage("SIGN_UP_FORM_MANDATORY_GENDER_MESSAGE"), null);
      valid = false;
    }
    
    return valid;
  }
  
  private User createUserFromFields () {
    User newUser = new User();
    newUser.setFirstName(firstName);
    newUser.setLastName(lastName);
    newUser.setBirthdate(birthdate);
    newUser.setGender(gender);
    newUser.setPassword(password);
    newUser.setAccountStatus(EUserAccountStatus.INACTIVATED);
    return newUser;
  }
  
}
