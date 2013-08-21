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
import classmodeler.web.util.JSFGenericBean;

/**
 * JSF validator used to control the minimum and maximum length of the password
 * field.
 * 
 * @author Gabriel Leonardo Diaz, 13.08.2013.
 */
@ManagedBean(name="passwordValidator")
@RequestScoped
public class PasswordJSFValidator implements Validator {

  @Override
  public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
    String password = (String) value;
    
    if (!GenericUtils.isValidPassword(password)) {
      String clientId = (String) component.getAttributes().get("messageOutput");
      if (clientId == null) {
        clientId = JSFGenericBean.GENERAL_MESSAGE_ID;
      }
      
      FacesMessage message = new FacesMessage(GenericUtils.getLocalizedMessage("PASSWORD_INVALID_LENGTH_MESSAGE"));
      message.setSeverity(FacesMessage.SEVERITY_ERROR);
      context.addMessage(clientId, message);
      
      throw new ValidatorException(message); // Not displayed
    }
  }

}
