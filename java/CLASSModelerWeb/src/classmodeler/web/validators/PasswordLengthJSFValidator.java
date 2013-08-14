/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.validators;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import classmodeler.service.util.GenericUtils;
import classmodeler.web.resources.JSFResourceBundle;
import classmodeler.web.util.JSFMessageBean;

/**
 * JSF validator used to control the minimum and maximum length of the password
 * field.
 * 
 * @author Gabriel Leonardo Diaz, 13.08.2013.
 */
@ManagedBean(name="passwordValidator")
@RequestScoped
public class PasswordLengthJSFValidator implements Validator {

  @Override
  public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
    String password = (String) value;
    
    if (GenericUtils.isEmptyString(password)) {
      FacesMessage message = new FacesMessage(JSFResourceBundle.getLocalizedMessage("PASSWORD_INVALID_LENGTH_MESSAGE"));
      message.setSeverity(FacesMessage.SEVERITY_ERROR);
      context.addMessage(JSFMessageBean.GENERAL_MESSAGE_ID, message);
      
      throw new ValidatorException(message); // Not displayed
    }
  }

}
