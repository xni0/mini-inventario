package daoJPA;

import java.math.BigDecimal;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import modeloJPA.ProductoJPA;
import utils.JPAUtil;

public class DaoProductoJPA {
	
	public ProductoJPA findById(Long id) {
		EntityManager em= JPAUtil.getEM();
		ProductoJPA producto=new ProductoJPA();
		try {
			producto = em.find(ProductoJPA.class, id);
					
		}finally {
			em.close();	
		}
		return producto;
	}
	
	public List<ProductoJPA> findByCategoria(Long idCategoria){
		EntityManager em=JPAUtil.getEM();
		
		try {
			return em.createQuery("SELECT p FROM ProductoJPA p WHERE p.categoria.id=:idCat", ProductoJPA.class)
			.setParameter("idCat", idCategoria)
			.getResultList();
		}catch(RuntimeException e) {
			throw e;
		}finally {
			em.close();
		}
	}
	
	public List<ProductoJPA> findAll(){
		EntityManager em=JPAUtil.getEM();
		List<ProductoJPA> productos;
		try {
			productos = em.createQuery("SELECT p FROM ProductoJPA p ORDER BY p.id", ProductoJPA.class).getResultList();
		}finally {
			em.close();
		}
		return productos;
	}
	
	public void create(ProductoJPA p) {
		EntityManager em= JPAUtil.getEM();
		EntityTransaction tx= em.getTransaction();
		
		try {
			tx.begin();
			em.persist(p);
			tx.commit();
		} catch (RuntimeException e) {
			if(tx.isActive())
				tx.rollback();
			throw e;
		} finally {
			em.close();
		}
	}
	
	public void update(ProductoJPA p) {
		EntityManager em= JPAUtil.getEM();
		EntityTransaction tx= em.getTransaction();
		
		try {
			tx.begin();
			em.merge(p);
			tx.commit();
		} catch (RuntimeException e) {
			if(tx.isActive())
				tx.rollback();
		} finally {
			em.close();
		}
	}
	
	public void delete(Long id) {
		EntityManager em=JPAUtil.getEM();
		EntityTransaction tx= em.getTransaction();
		
		try {
			tx.begin();
			ProductoJPA p= em.find(ProductoJPA.class, id);
			if(p!=null) {
				System.out.println("Producto localizado");
				em.remove(p);
				System.out.println("Producto borrado");
			}
			tx.commit();
		} catch (RuntimeException e) {
			if(tx.isActive())
				tx.rollback();
			throw e;
		} finally {
			em.close();
		}
	}
	
	public List<ProductoJPA> findByPrecioMinimo(BigDecimal precioMinimo) {
	    
	    EntityManager em = JPAUtil.getEM(); 
	    
	    try {
	        String query = "SELECT p FROM ProductoJPA p WHERE p.precio >= :min";
	        
	        return em.createQuery(query, ProductoJPA.class)
	                .setParameter("min", precioMinimo)
	                .getResultList();
	        
	    } catch (RuntimeException e) {
	        throw e;
	        
	    } finally {
	        if (em != null) {
	            em.close();
	        }
	    }
	}
	
	public List<ProductoJPA> findByName(String nombre){

	    EntityManager em = JPAUtil.getEM(); 
	    
	    try {
	        String query = "SELECT p FROM ProductoJPA p WHERE p.nombre LIKE :nombre";
	        
	        return em.createQuery(query, ProductoJPA.class)
	                .setParameter("nombre", "%"+nombre+"%")
	                .getResultList();
	        
	    } catch (RuntimeException e) {
	        throw e;
	        
	    } finally {
	        if (em != null) {
	            em.close();
	        }
	    }
	}
	
	
	
	
}