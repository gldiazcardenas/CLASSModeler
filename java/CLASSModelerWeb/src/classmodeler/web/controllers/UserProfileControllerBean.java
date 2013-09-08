/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.primefaces.event.FileUploadEvent;

import classmodeler.domain.user.EGender;
import classmodeler.domain.user.Diagrammer;
import classmodeler.service.UserService;
import classmodeler.service.util.GenericUtils;
import classmodeler.web.util.JSFFormControllerBean;
import classmodeler.web.util.JSFGenericBean;

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
  private Diagrammer loggedUser;
  
  @EJB
  private UserService userService;
  
  private String firstName;
  private String lastName;
  private EGender gender;
  private String avatar;
  
  public UserProfileControllerBean() {
    super();
  }
  
  public void setLoggedUser(Diagrammer loggedUser) {
    this.loggedUser = loggedUser;
    
    if (loggedUser != null) {
      firstName = loggedUser.getFirstName();
      lastName  = loggedUser.getLastName();
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
      items[i++] = new SelectItem(g, GenericUtils.getLocalizedMessage(g.getName()));
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
      avatar = GenericUtils.DEFAULT_MALE_IMAGE_URL;
    }
    else {
      avatar = GenericUtils.DEFAULT_FEMALE_IMAGE_URL;
    }
  }
  
  /**
   * Method that controls the process of uploading a new image file for the user
   * avatar.
   * 
   * @param evt
   *          The PrimeFaces file upload event.
   * @author Gabriel Leonardo Diaz, 04.06.2013.
   */
  public void handleFileUpload (FileUploadEvent evt) {
    try {
      SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
      String path          = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
      String extension     = evt.getFile().getFileName().substring(evt.getFile().getFileName().lastIndexOf('.'));
      String fileName      = GenericUtils.UPLOADS_URL + fmt.format(Calendar.getInstance().getTime()) + extension;
      File file            = new File(path + fileName);
      
      InputStream is       = evt.getFile().getInputstream();
      OutputStream out     = new FileOutputStream(file);
      byte buf[]           = new byte[1024];
      
      int len;
      while ((len = is.read(buf)) > 0) {
        out.write(buf, 0, len);
      }
      
      is.close();
      out.close();
      
      avatar = fileName;
    }
    catch (IOException e) {
      addErrorMessage("profileMessage", GenericUtils.getLocalizedMessage("UNEXPECTED_EXCEPTION_MESSAGE"), e.getMessage());
    }
  }

  @Override
  public void processAJAX() {
    if (!isAllValid()) {
      return;
    }
    
    loggedUser.setFirstName(firstName);
    loggedUser.setLastName(lastName);
    loggedUser.setGender(gender);
    loggedUser.setAvatar(avatar);
    
    userService.updateDiagrammer(loggedUser);
    
    addInformationMessage(JSFGenericBean.GENERAL_MESSAGE_ID, GenericUtils.getLocalizedMessage("SAVED_SUCCESSFULLY_MESSAGE"), null);
  }

  @Override
  public String process() {
    return null; // Not used
  }
  
}
