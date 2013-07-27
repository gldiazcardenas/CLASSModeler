/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS Cúcuta, Colombia (c) 2013 by
 * UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.web.converters;

import javax.faces.convert.EnumConverter;
import javax.faces.convert.FacesConverter;

import classmodeler.domain.diagram.EDiagramPrivilege;

/**
 * JSF Converter for Diagram Privilege enumeration.
 * 
 * @author Gabriel Leonardo Diaz, 27.07.2013.
 */
@FacesConverter("privilegeConverter")
public class EDiagramPrivilegeJSFConverter extends EnumConverter {
  
  public EDiagramPrivilegeJSFConverter() {
    super(EDiagramPrivilege.class);
  }
  
}
