package testJPA;

import jakarta.persistence.EntityManager;
import utils.JPAUtil;

public class TestEMFactory {

	public static void main(String[] args) {

		System.out.println("Probando EM...");
		EntityManager em= JPAUtil.getEM();
		System.out.println("Todo Ok " + em);
		em.close();
		
	}

}
