/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.converters;

import javax.faces.convert.EnumConverter;
import javax.faces.convert.FacesConverter;

import classmodeler.domain.user.EGender;

/**
 * JSF converter that converts from <code>String</code> to the values of the
 * enumeration {@link EGender} and vice versa.
 * 
 * @author Gabriel Leonardo Diaz, 02.03.2013.
 */
@FacesConverter("genderConverter")
public class EGenderJSFConverter extends EnumConverter {
  
  public EGenderJSFConverter() {
    super (EGender.class);
  }

}
