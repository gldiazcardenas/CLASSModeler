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
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import classmodeler.domain.user.EGender;
import classmodeler.domain.user.User;
import classmodeler.service.UserService;
import classmodeler.service.util.GenericUtils;
import classmodeler.web.resources.JSFResourceBundle;
import classmodeler.web.util.JSFFormControllerBean;
import classmodeler.web.util.JSFGenericBean;
import classmodeler.web.util.JSFMessageBean;

/**
 * JSF Form Bean controller that handles user interactions with the edit user
 * profile page.
 * 
 * @author Gabriel Leonardo Diaz, 03.06.2013.
 */
@ManagedBean (name="userProfileController")
@ViewScoped
public class UserProfileControllerBean extends JSFGenericBean implements JSFFormControllerBean {

  private static final long serialVersionUID = 1L;
  
  @ManagedProperty("#{sessionController.loggedRegisteredUser}")
  private User loggedUser;
  
  @EJB
  private UserService userService;
  
  private String firstName;
  private String lastName;
  private Date birthdate;
  private EGender gender;
  private String avatar;
  
  public UserProfileControllerBean() {
    super();
  }
  
  public void setLoggedUser(User loggedUser) {
    this.loggedUser = loggedUser;
    
    if (loggedUser != null) {
      firstName = loggedUser.getFirstName();
      lastName  = loggedUser.getLastName();
      birthdate = loggedUser.getBirthdate();
      gender    = loggedUser.getGender();
      avatar    = loggedUser.getAvatar();
    }
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
  
  public String getAvatar() {
    return avatar;
  }
  
  public void setAvatar(String avatar) {
    this.avatar = avatar;
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
  public boolean isAllValid() {
    if (loggedUser == null) {
      return false;
    }
    
    if (gender == null) {
      return false;
    }
    
    if (GenericUtils.isEmptyString(firstName) ||
        GenericUtils.isEmptyString(lastName) ||
        GenericUtils.isEmptyString(avatar)) {
      
      return false;
    }
    
    return true;
  }
  
  /**
   * Replaces the current avatar and loads the default image.
   * 
   * @author Gabriel Leonardo Diaz, 04.06.2013.
   */
  public void setDefaultAvatar () {
    if (gender == EGender.MALE) {
      avatar = JSFResourceBundle.DEFAULT_MALE_IMAGE_URL;
    }
    else {
      avatar = JSFResourceBundle.DEFAULT_FEMALE_IMAGE_URL;
    }
  }
  
  /**
   * Method that controls the process of uploading a new image file for the user
   * avatar.
   * 
   * @param evt
   *          The primefaces file upload event.
   * @author Gabriel Leonardo Diaz, 04.06.2013.
   */
  public void handleFileUpload (FileUploadEvent evt) {
    UploadedFile file = evt.getFile();
    if (file != null) {
      System.out.println();
    }
  }

  @Override
  public void processAJAX() {
    if (!isAllValid()) {
      return;
    }
    
    
    loggedUser.setFirstName(firstName);
    loggedUser.setLastName(lastName);
    loggedUser.setBirthdate(birthdate);
    loggedUser.setGender(gender);
    loggedUser.setAvatar(avatar);
    
    userService.updateUser(loggedUser);
    
    addInformationMessage(JSFMessageBean.GENERAL_MESSAGE_ID, JSFResourceBundle.getLocalizedMessage("SAVED_SUCCESSFULLY_MESSAGE"), null);
  }

  @Override
  public String process() {
    return null; // Not used
  }
  
}
