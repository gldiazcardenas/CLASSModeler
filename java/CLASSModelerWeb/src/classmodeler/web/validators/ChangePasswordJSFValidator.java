/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.validators;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import classmodeler.service.UserService;
import classmodeler.service.util.GenericUtils;
import classmodeler.web.controllers.SessionControllerBean;
import classmodeler.web.resources.JSFResourceBundle;

/**
 * JSF Validator for changing password form.
 * 
 * @author Gabriel Leonardo Diaz, 03.06.2013.
 */
@ManagedBean(name="changePasswordValidator")
@RequestScoped
public class ChangePasswordJSFValidator implements Validator {
  
  @ManagedProperty("#{sessionController}")
  private SessionControllerBean sessionController;
  
  @EJB
  private UserService userService;
  
  public ChangePasswordJSFValidator() {
    super();
  }
  
  public void setSessionController(SessionControllerBean sessionController) {
    this.sessionController = sessionController;
  }

  @Override
  public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
    if (component.getId().equals("oldPassword")) {
      String oldPassword = (String) value;
      if (!sessionController.getLoggedRegisteredUser().getPassword().equals(oldPassword)) {
        FacesMessage message = new FacesMessage(JSFResourceBundle.getLocalizedMessage("CHANGE_PASSWORD_INVALID_OLD_MESSAGE"));
        message.setSeverity(FacesMessage.SEVERITY_ERROR);
        context.addMessage("changePasswordMessage", message);
        
        throw new ValidatorException(message); // Not displayed
      }
    }
    else if (component.getId().equals("newConfirmation")) {
      String newPassword = (String) component.getAttributes().get("attrNewPassword");
      String newConfirmation = (String) value;
      
      if (!GenericUtils.equals(newPassword, newConfirmation)) {
        FacesMessage message = new FacesMessage(JSFResourceBundle.getLocalizedMessage("CHANGE_PASSWORD_INVALID_CONFIRM_MESSAGE"));
        message.setSeverity(FacesMessage.SEVERITY_ERROR);
        context.addMessage("changePasswordMessage", message);
        
        throw new ValidatorException(message); // Not displayed
      }
    }
  }
  
}
