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
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import classmodeler.service.UserService;
import classmodeler.service.util.GenericUtils;
import classmodeler.web.resources.JSFResourceBundle;

/**
 * JSF bean validator to check the email entered in the Form.
 * 
 * @author Gabriel Leonardo Diaz, 10.05.2013.
 */
@ManagedBean(name="signUpEmailValidator")
@RequestScoped
public class SignUpEmailJSFValidator implements Validator {
  
  @EJB
  private UserService userService;

  @Override
  public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
    String email = (String) value;
    
    // Validates the email format
    if (!GenericUtils.isValidEmail(email)) {
      FacesMessage message = new FacesMessage(JSFResourceBundle.getLocalizedMessage("INVALID_EMAIL_ADDRESS_MESSAGE"));
      message.setSeverity(FacesMessage.SEVERITY_ERROR);
      context.addMessage("signUpMessage", message);
      
      throw new ValidatorException(message); // Not displayed
    }
    
    // Checks if the entered Email already exists.
    if (userService.existsUser(email)) {
      FacesMessage message = new FacesMessage(JSFResourceBundle.getLocalizedMessage("INVALID_ACCOUNT_DUPLICATED_MESSAGE"));
      message.setSeverity(FacesMessage.SEVERITY_ERROR);
      context.addMessage("signUpMessage", message);
      
      throw new ValidatorException(message); // Not displayed
    }
  }
  
}
