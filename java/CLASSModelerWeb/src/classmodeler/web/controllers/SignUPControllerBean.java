/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS 
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.controllers;

import java.util.Date;

import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import classmodeler.domain.user.EGender;
import classmodeler.service.util.GenericUtils;
import classmodeler.web.util.JSFContextUtil;
import classmodeler.web.util.JSFFormControllerBean;
import classmodeler.web.util.JSFGenericBean;


/**
 * JSF bean controller for the signing up process, this manages all the fields
 * in the form and allows to register the user by using the service.
 * 
 * @author Gabriel Leonardo Diaz, 23.02.2013.
 */
@Named("signUPController")
@SessionScoped
public class SignUPControllerBean extends JSFGenericBean implements JSFFormControllerBean {

  private static final long serialVersionUID = 1L;
  
  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private Date birthdate;
  private EGender gender;

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
  
  public SelectItem[] getGendersForSelectOneRadio() {
    SelectItem[] items = new SelectItem[EGender.values().length];

    int i = 0;
    for (EGender g : EGender.values()) {
      items[i++] = new SelectItem(g, g.toString());
    }
    
    return items;
  }

  @Override
  public void actionPerformed() {
    if (isAllValid()) {
      return;
    }
  }

  @Override
  public boolean isAllValid() {
    boolean valid = true;
    
    if (GenericUtils.isEmptyString(firstName)) {
      addErrorMessage(JSFContextUtil.getLocalizedMessage("SIGN_UP_FORM_MANDATORY_FST_NAME_MESSAGE"), null);
      valid = false;
    }
    
    if (GenericUtils.isEmptyString(lastName)) {
      addErrorMessage(JSFContextUtil.getLocalizedMessage("SIGN_UP_FORM_MANDATORY_LST_NAME_MESSAGE"), null);
      valid = false;
    }
    
    if (GenericUtils.isEmptyString(email)) {
      addErrorMessage(JSFContextUtil.getLocalizedMessage("SIGN_UP_FORM_MANDATORY_EMAIL_MESSAGE"), null);
      valid = false;
    }
    
    if (gender == null) {
      addErrorMessage(JSFContextUtil.getLocalizedMessage("SIGN_UP_FORM_MANDATORY_GENDER_MESSAGE"), null);
      valid = false;
    }
    
    return valid;
  }
  
}
