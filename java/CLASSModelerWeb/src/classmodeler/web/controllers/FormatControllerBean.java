/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS Cúcuta, Colombia (c) 2013 by
 * UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import classmodeler.domain.diagram.EDiagramPrivilege;
import classmodeler.domain.user.EUserAccountStatus;
import classmodeler.domain.user.IUser;
import classmodeler.domain.user.User;
import classmodeler.service.util.GenericUtils;
import classmodeler.web.util.JSFGenericBean;

/**
 * JSF Bean used to format some data in order to get user friendly output.
 * 
 * @author Gabriel Leonardo Diaz, 26.07.2013.
 */
@ManagedBean(name="formatController")
@ApplicationScoped
public class FormatControllerBean extends JSFGenericBean {

  private static final long serialVersionUID = 1L;
  
  public FormatControllerBean() {
    super();
  }
  
  /**
   * Gets the URL to the image that represents the avatar of the given user.
   * 
   * @return The URL to the image.
   * @author Gabriel Leonardo Diaz, 26.07.2013.
   */
  public String getUserAvatar (IUser user) {
    if (user == null) {
      return null;
    }
    
    if (!user.isRegisteredUser()) {
      return GenericUtils.DEFAULT_MALE_IMAGE_URL;
    }
    
    return user.getAvatar();
  }
  
  /**
   * Gets the image URL for the given privilege.
   * 
   * @param privilege
   *          The privilege.
   * @return The URL to the image.
   * @author Gabriel Leonardo Diaz, 26.07.2013.
   */
  public String getDiagramPrivilegeImage (EDiagramPrivilege privilege) {
    if (privilege == null) {
      return null;
    }
    
    switch (privilege) {
    case READ:
      return "/resources/images/zoom_in.png";
      
    case EDIT:
      return "/resources/images/edit.png";
      
    case SHARE:
      return "/resources/images/user.png";
    
    default:
      return "/resources/images/favorite.png";
    }
  }
  
  /**
   * Retrieves the localized name of the privilege.
   * 
   * @param privilege
   *          The privilege.
   * @return A string representation with the privilege name.
   * @author Gabriel Leonardo Diaz, 26.07.2013.
   */
  public String getDiagramPrivilegeName (EDiagramPrivilege privilege) {
    if (privilege == null) {
      return null;
    }
    
    switch (privilege) {
    case READ:
      return GenericUtils.getLocalizedMessage("PRIVILEGE_NAME_READ");
      
    case EDIT:
      return GenericUtils.getLocalizedMessage("PRIVILEGE_NAME_EDIT");
      
    case SHARE:
      return GenericUtils.getLocalizedMessage("PRIVILEGE_NAME_SHARE");
    
    default:
      return GenericUtils.getLocalizedMessage("PRIVILEGE_NAME_OWNER");
    }
  }
  
  /**
   * Retrieves a localized name of the given user account status.
   * 
   * @param status
   *          The status.
   * @return A string representation of the status.
   * @author Gabriel Leonardo Diaz, 27.07.2013.
   */
  public String getUserAccountStatusName (EUserAccountStatus status) {
    if (status == null) {
      return null;
    }
    
    switch (status) {
    case ACTIVATED:
      return GenericUtils.getLocalizedMessage("USER_ACCOUNT_STATUS_ACTIVATED_NAME");
      
    case DEACTIVATED:
      return GenericUtils.getLocalizedMessage("USER_ACCOUNT_STATUS_DEACTIVATED_NAME");
      
    default:
      return GenericUtils.getLocalizedMessage("USER_ACCOUNT_STATUS_INACTIVATED_NAME");
    }
  }
  
  /**
   * Gets the image URL for the user account status.
   * 
   * @param status
   *          The status.
   * @return The relative URL to the image for the user account status.
   * @author Gabriel Leonardo Diaz, 27.07.2013.
   */
  public String getUserAccountStatusImage (EUserAccountStatus status) {
    if (status == null) {
      return null;
    }
    
    switch (status) {
    case ACTIVATED:
      return "/resources/images/accept.png";
      
    case DEACTIVATED:
      return "/resources/images/block.png";
      
    default:
      return "/resources/images/warning.png";
    }
  }
  
  /**
   * Determines whether the given user account status corresponds the ACTIVATED
   * status.
   * 
   * @param status
   *          The status to compare.
   * @return True or False based on the given status.
   * @author Gabriel Leonardo Diaz, 12.08.2013.
   */
  public boolean isActivatedAccount (EUserAccountStatus status) {
    return status == EUserAccountStatus.ACTIVATED;
  }
  
  /**
   * Formats the given date in the pattern: dd/MM/yyyy. If the date is today
   * this method returns a localized label as "Today".
   * 
   * @param date
   *          The date to format.
   * @return A string representation of the date.
   * @author Gabriel Leonardo Diaz, 26.07.2013.
   */
  public String getFormattedDate (Date date) {
    if (date == null) {
      return null;
    }
    
    if (GenericUtils.isToday(date)) {
      return GenericUtils.getLocalizedMessage("TODAY_LABEL");
    }
    
    return new SimpleDateFormat("dd/MM/yyyy").format(date);
  }
  
  /**
   * Formats the name of the given user. For registered users this method returns the first name plus the first letter of the last name.
   * @param user The user to format the name.
   * @return The name formatted.
   * @author Gabriel Leonardo Diaz, 26.07.2013.
   */
  public String getFormattedUserName (IUser user) {
    if (user == null) {
      return null;
    }
    
    if (!user.isRegisteredUser()) {
      return GenericUtils.getLocalizedMessage(user.getName());
    }
    
    User registeredUser = (User) user;
    return registeredUser.getFirstName();
  }
  
  
}
