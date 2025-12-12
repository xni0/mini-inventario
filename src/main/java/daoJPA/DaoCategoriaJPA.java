package daoJPA;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import modeloJPA.CategoriaJPA;
import utils.JPAUtil;

public class DaoCategoriaJPA {

	// Buscar por ID
	public CategoriaJPA findById(Long id) {
		EntityManager em = JPAUtil.getEM();
		CategoriaJPA categoria = null; 
		try {
			categoria = em.find(CategoriaJPA.class, id);
		} finally {
			em.close();
		}
		return categoria;
	}

	// Listar todas las categorías
	public List<CategoriaJPA> findAll() {
		EntityManager em = JPAUtil.getEM();
		List<CategoriaJPA> categorias;
		try {
			categorias = em.createQuery("SELECT c FROM CategoriaJPA c", CategoriaJPA.class).getResultList();
		} finally {
			em.close();
		}
		return categorias;
	}

	// Crear una categoría
	public void create(CategoriaJPA c) {
		EntityManager em = JPAUtil.getEM();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.persist(c);
			tx.commit();
		} catch (RuntimeException e) {
			if (tx.isActive())
				tx.rollback();
			throw e;
		} finally {
			em.close();
		}
	}

	// Actualizar una categoría
	public void update(CategoriaJPA c) {
		EntityManager em = JPAUtil.getEM();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.merge(c);
			tx.commit();
		} catch (RuntimeException e) {
			if (tx.isActive())
				tx.rollback();
		} finally {
			em.close();
		}
	}

	// Borrar una categoría por ID
	public void delete(Long id) {
		EntityManager em = JPAUtil.getEM();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			CategoriaJPA c = em.find(CategoriaJPA.class, id);
			if (c != null) {
				System.out.println("Categoría localizada: " + c.getNombre());
				em.remove(c);
				System.out.println("Categoría borrada");
			}
			tx.commit();
		} catch (RuntimeException e) {
			if (tx.isActive())
				tx.rollback();
			throw e;
		} finally {
			em.close();
		}
	}
}