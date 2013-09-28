/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * C�cuta, Colombia
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
  
  
  package examples;
  
  /**
   * Documentacion en Javadoc para ClaseA.
   *
   * @author Gabriel Leonardo Diaz, 27.09.2013.
   */
  public class ClaseA {
    
    public int atributoEnA;
    
    public void metodoEnA() {
      
    }
  }
  
  package examples;
  /**
   * Documentaci�n en JavaDoc para ClaseB.
   *
   * @author Gabriel Leonardo Diaz, 27.09.2013.
   */
  public class ClaseB {
    
    private int atributoEnB;
    
    
  }
  
  /**
   * Documentacion en formato JavaDoc de la interfaz NombreInterfaz
   *
   * @author Gabriel Leonardo Diaz, 27.09.2013.
   */
  public interface NombreInterfaz {
    
    public static final int ATRIBUTO_CONSTANTE = 5;
    
    public void metodoDeInterfaz();
  }
  
  /**
   * Documentacion en formato JavaDoc de la enumeracion NombreEnumeracion.
   *
   * @author Gabriel Leonardo Diaz, 27.09.2013.
   */
  public enum NombreEnumeracion {
    
    LITERAL_UNO,
    LITERAL_DOS,
    LITERAL_TRES
    
  }
  
  /**
   * Documentaci�n en formato JavaDoc de la case NombreClase.
   * 
   * @author Gabriel Leonardo Diaz, 27.09.2013.
   */
  public class NombreClase {
    
    // Atributos
    private int atributoTipoEntero;
    private String atributoTipoCadena;
    private float atributoTipoFlotante;
   
    // Constructor
    public NombreClase () {
    }
    
    // Operaciones o m�todos
    public void metodo () {
      // Vacio
    }
    
    public void metodoConParametro (int parametro) {
      // Vacio
    }
    
    public String metodoConRetorno () {
      return "una cadena";
    }
  }
}
