/****************************************************
 * 
 * Universidad Francisco de Paula Santander UFPS
 * Cúcuta, Colombia
 * (c) 2013 by UFPS. All rights reserved.
 * 
 ****************************************************/

package classmodeler.test;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import javax.persistence.EntityManagerFactory;

import org.testng.annotations.Test;

/**
 * Unit tests to ensure the database connection is correct.
 * 
 * @author Gabriel Leonardo Diaz, 16.03.2013.
 */
public class EntityManagerTest {
  
  @Test
  public void testEntityManager() {
    EntityManagerFactory factory = Persistence.createEntityManagerFactory("CLASSModelerJPA");
    EntityManager em = factory.createEntityManager();
    
    em.clear();
  }
}
