package utils;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

public class JPAUtil {
	private static final EntityManagerFactory em = Persistence.createEntityManagerFactory("MiniInventario_Lucilene");
	
	public static EntityManager getEM() {
		return em.createEntityManager();
		
	}
}
