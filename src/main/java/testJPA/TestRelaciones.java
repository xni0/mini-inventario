package testJPA;

import java.math.BigDecimal;
import java.util.function.Consumer;

import org.hibernate.LazyInitializationException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import modeloJPA.CategoriaJPA;
import modeloJPA.ProductoJPA;
import utils.JPAUtil;

public class TestRelaciones {

	public static void main(String[] args) {

		// Ejecuta uno por uno para ver los resultados claros en consola
		// test01_CrearCategoriaYProductos();
		// test02_LadoInversoNoManda();
		// test03_moverProductoDeCategoria();
		// test04_OrphanRemoval();
		test05_LazyVsEager();
		
	}
	
	private static void test05_LazyVsEager() {
		Long idPrueba = 63L; 
		/*
		// PARTE A: Acceso con EM abierto 
		System.out.println("PARTE A: EM ABIERTO");
		runInTransaction(em->{
			CategoriaJPA c=em.find(CategoriaJPA.class, idPrueba);
			if(c==null) {
				System.out.println("No existe la categoría");
				return;
			}
			
			System.out.println("Categoría: "+ c.getNombre());
			System.out.println("Accediendo a los productos con el EM abierto: ");
			for(ProductoJPA p: c.getProductos()) {
				System.out.println("-"+p.getNombre());
			}
		});
		
		*/
		
		
		// PARTE B: Acceso con EM cerrado 
		System.out.println("PARTE B: EM CERRADO");
		
		CategoriaJPA categoriaOffLine = null; 
		EntityManager em1 = JPAUtil.getEM();
		
		try {
			// Solo buscamos la categoría, NO tocamos la lista de productos todavía
			categoriaOffLine = em1.find(CategoriaJPA.class, idPrueba);
			
			
		} catch(Exception e){
			System.out.println("Error al buscar: " + e.getMessage());
		} finally {
			em1.close();
			System.out.println("EntityManager cerrado.");
		}
		
		if(categoriaOffLine == null) {
			System.out.println("No se encontró la categoría con ID " + idPrueba);
			return; // Salimos si no hay categoría
		}
		
		try {
			System.out.println("Intentando leer productos offline...");
			for(ProductoJPA p : categoriaOffLine.getProductos()) 
				System.out.println("-" + p.getNombre());
			
			System.out.println("ÉXITO: Se han leído los productos (EAGER funcionó).");
			
		} catch(org.hibernate.LazyInitializationException le){
			System.out.println("EXCEPCIÓN: LazyInitializationException por estar en modo lazy y EM cerrado.");
		}
	}
	
	
	
	
	private static void test01_CrearCategoriaYProductos() {
		runInTransaction(em-> {
			System.out.println(">>> INICIO TEST 01: CASCADE PERSIST");
			
			CategoriaJPA c = new CategoriaJPA();
			c.setNombre("Alimentacion");
			
			ProductoJPA p1 = new ProductoJPA();
			p1.setNombre("Agua");
			p1.setPrecio(new BigDecimal ("3"));
			p1.setStock(505);
			p1.setCategoria("Bebida");
			p1.setAgotado(false); // Ponemos valor explícito
			
			ProductoJPA p2 = new ProductoJPA();
			p2.setNombre("Café");
			p2.setPrecio(new BigDecimal ("5"));
			p2.setStock(99); // CORREGIDO: antes ponías p1.setStock
			p2.setCategoria("Bebida");
			p2.setAgotado(false);
			
			// Usamos el helper que sincroniza ambos lados
			c.addProducto(p1);
			c.addProducto(p2);
			
			em.persist(c); // Por el CascadeType.ALL se guardan también los productos
			
			System.out.println("Categoría creada ID: " + c.getId());
			System.out.println("Productos guardados en cascada: " + c.getProductos().size());
		});
	}
	
	private static void test02_LadoInversoNoManda() {
		runInTransaction(em ->{
			System.out.println("\n>>> INICIO TEST 02: LADO INVERSO");
			
			// IMPORTANTE: Asegúrate de usar un ID que exista. 
			// Si ejecutaste el test01, usa el ID que te salió en consola.
			Long idCategoria = 1L; 
			CategoriaJPA c = em.find(CategoriaJPA.class, idCategoria);
			
			if(c == null) {
				System.out.println("No existe la categoría con id=" + idCategoria);
				return;
			}
			System.out.println("Categoría recuperada: "+ c.getNombre());
			
			ProductoJPA productoFantasma = new ProductoJPA();
			productoFantasma.setNombre("Monitor fantasma");
			productoFantasma.setPrecio(new BigDecimal("234"));
			productoFantasma.setStock(9);
			productoFantasma.setCategoria("Deprecated");
			productoFantasma.setAgotado(false);
			
			// ERROR INTENCIONADO:
			// Añadimos solo a la lista Java (lado inverso) pero NO establecemos la relación en el Producto.
			// Al hacer commit, el producto se guarda (por cascade), pero su FK (CATEGORIA_ID) será NULL.
			c.getProductos().add(productoFantasma);
			
			System.out.println("Producto añadido a la lista, pero sin asignar lado propietario.");
			System.out.println("Resultado: Se guarda en BD pero con CATEGORIA_ID = NULL");
		});
	}
	
	private static void test03_moverProductoDeCategoria() {
		runInTransaction(em->{
			System.out.println("INICIO TEST 03: MOVER PRODUCTO");
			
			Long idProductoAMover =16L; 
			Long idCategoriaDestino = 65L; // Necesitamos haber creado otra categoría antes
			
			// Si no tenemos otra categoría, creamos una rápida aquí para que el test no falle
			CategoriaJPA catNueva = new CategoriaJPA();
			catNueva.setNombre("Oficina");
			em.persist(catNueva);
			idCategoriaDestino = catNueva.getId();

			ProductoJPA p = em.find(ProductoJPA.class, idProductoAMover);
			CategoriaJPA cFinal = em.find(CategoriaJPA.class, idCategoriaDestino);
			
			if(p == null || cFinal == null) {
				System.out.println("Datos insuficientes (Producto o Categoría no existen).");
				return;
			}
			
			CategoriaJPA cOriginal = p.getCategoria(); // Recuperamos su categoría actual
			
			System.out.println("Moviendo producto: " + p.getNombre());
			if (cOriginal != null) {
				System.out.println("De origen: " + cOriginal.getNombre());
				// Usamos el helper para quitarlo limpiamente de la lista original
				cOriginal.removeProducto(p);
			}
			System.out.println("A destino: " + cFinal.getNombre());
			
			// Usamos el helper para añadirlo a la nueva (esto actualiza la FK)
			cFinal.addProducto(p);
			
			System.out.println("Cambio realizado en memoria.");
			// Al hacer commit, Hibernate detecta el cambio en 'p' y hace el UPDATE
		});
	}
	

	private static void test04_OrphanRemoval() {
		runInTransaction(em -> {
			System.out.println("INICIO TEST 04: ORPHAN REMOVAL");
			
			// Buscamos una categoría que sepamos que tiene productos
			Long idCategoria = 65L; 
			CategoriaJPA c = em.find(CategoriaJPA.class, idCategoria);
			
			if(c == null || c.getProductos().isEmpty()) {
				System.out.println("No se encontró categoría con productos para el test.");
				
				// Creamos datos al vuelo para demostrar que funciona
				System.out.println("Creando datos de prueba...");
				c = new CategoriaJPA(); 
				c.setNombre("Temp para Borrar");
				ProductoJPA pTemp = new ProductoJPA();
				pTemp.setNombre("Producto a Eliminar");
				c.addProducto(pTemp);
				em.persist(c);
			}
			
			System.out.println("Categoría: " + c.getNombre());
			System.out.println("Productos antes: " + c.getProductos().size());
			
			// Obtenemos el primer producto de la lista
			ProductoJPA victima = c.getProductos().get(0);
			System.out.println("Eliminando de la lista a: " + victima.getNombre());
			
			// --Lo quitamos de la lista usando el helper.--
			// Gracias a orphanRemoval=true en la entidad CategoriaJPA,
			// Hibernate borrará este producto de la Base de Datos automáticamente.
			c.removeProducto(victima);
			
			System.out.println("Productos después (en memoria): " + c.getProductos().size());
			System.out.println("Al hacer commit, se ejecuta DELETE del producto huérfano.");
		});
	}
	
	//--helper--//
	private static void runInTransaction(Consumer<EntityManager> work) {
		EntityManager em = JPAUtil.getEM();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			work.accept(em);
			tx.commit();
		} catch(RuntimeException e) {
			if(tx.isActive())
				tx.rollback();
			e.printStackTrace(); // Es bueno imprimir el error para saber qué pasó
		} finally {
			em.close();
		}
	}
}
