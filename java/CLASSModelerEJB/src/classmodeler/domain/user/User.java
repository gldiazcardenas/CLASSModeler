/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.domain.user;

import java.io.Serializable;

/**
 * Defines the common behavior of an user that interacts with the system.
 * 
 * @author Gabriel Leonardo Diaz, 07.04.2013.
 */
public interface User extends Serializable {
  
  /**
   * Gets the full name of the user that is using the system.
   * 
   * @return The full name.
   */
  public String getName();
  
  /**
   * Gets the password used to login in the system.
   * 
   * @return The user password.
   */
  public String getPassword();
  
  /**
   * Gets the email of the user, this is used as nickname to login in the
   * system.
   * 
   * @return The email of the user.
   */
  public String getEmail();
  
  /**
   * Gets the URL of the avatar of the user.
   * 
   * @return The avatar of the user.
   */
  public String getAvatar();
  
  /**
   * The query determines if the user is registered in the system, or he is a
   * guest.
   * 
   * @return A boolean value depending on the user implementation.
   */
  public boolean isRegisteredUser();
  
  /**
   * Documentación en formato JavaDoc de la case NombreClase.
   * 
   * @author Gabriel Leonardo Diaz, 27.09.2013.
   */
  package examples;
  
  public class NombreClase {
    
    // Atributos
    private int atributoTipoEntero;
    private String atributoTipoCadena;
    private float atributoTipoFlotante;
   
    // Constructor
    public NombreClase () {
    }
    
    // Operaciones o métodos
    public void metodo () {
    }
    
    public void metodoConParametro (int parametro) {
    }
    
    public String metodoConRetorno () {
      return "una cadena";
    }
  }
}
