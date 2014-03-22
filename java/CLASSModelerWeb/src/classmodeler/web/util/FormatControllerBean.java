/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS Cúcuta, Colombia (c) 2013 by
 * UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import classmodeler.domain.share.SharedItem;
import classmodeler.domain.user.Diagrammer;
import classmodeler.domain.user.EDiagrammerAccountStatus;
import classmodeler.domain.user.User;
import classmodeler.service.util.GenericUtils;

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
  public String getUserAvatar (User user) {
    if (user == null) {
      return null;
    }
    
    if (!user.isRegisteredUser()) {
      return GenericUtils.DEFAULT_MALE_IMAGE_URL;
    }
    
    return user.getAvatar();
  }
  
  /**
   * Retrieves a localized name of the given user account status.
   * 
   * @param status
   *          The status.
   * @return A string representation of the status.
   * @author Gabriel Leonardo Diaz, 27.07.2013.
   */
  public String getUserAccountStatusName (EDiagrammerAccountStatus status) {
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
  public String getUserAccountStatusImage (EDiagrammerAccountStatus status) {
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
   * Gets the image URL depending on the privilege of the shared item.
   * 
   * @param item
   * @return
   * @author Gabriel Leonardo Diaz, 22.03.2014.
   */
  public String getPrivilegeImage (SharedItem item) {
    if (item.isWriteable()) {
      return "/resources/images/edit_16x16.png";
    }
    return "/resources/images/eye.png";
  }
  
  /**
   * Gets the privilege name for the given shared item.
   * 
   * @param item
   * @return
   * @author Gabriel Leonardo Diaz, 22.03.2014.
   */
  public String getPrivilegeName (SharedItem item) {
    if (item.isWriteable()) {
      return GenericUtils.getLocalizedMessage("CAN_EDIT_LABEL");
    }
    return GenericUtils.getLocalizedMessage("READ_ONLY_LABEL");
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
  public boolean isActivatedAccount (EDiagrammerAccountStatus status) {
    return status == EDiagrammerAccountStatus.ACTIVATED;
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
  public String getFormattedUserName (User user) {
    if (user == null) {
      return null;
    }
    
    if (!user.isRegisteredUser()) {
      return GenericUtils.getLocalizedMessage(user.getName());
    }
    
    Diagrammer registeredUser = (Diagrammer) user;
    return registeredUser.getFirstName();
  }
  
  
}
