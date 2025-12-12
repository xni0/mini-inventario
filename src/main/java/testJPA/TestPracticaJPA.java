package testJPA;

import java.math.BigDecimal;
import java.util.function.Consumer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import modeloJPA.CategoriaJPA;
import modeloJPA.ProductoJPA;
import utils.JPAUtil;

public class TestPracticaJPA {

	public static void main(String[] args) {
		
		// ejercicio1_ProvocarError();
		// ejercicio2_CorregirSinCascade();
		ejercicio3_CorregirConCascade();
		
	}
	
	/* 
	 * EJERCICIO 5 (LO RESPONDO AQUI PORQUE EN EL EDUCAMOS SOLO DEJABA MANDAR UN MAXIMO DE 5 ARCHIVOS)
	 * 1. ¿Por qué Hibernate exige definir el lado propietario? 
		Porque Hibernate ignora el Lado Inverso (el que tiene mappedBy) a la hora de generar el SQL. 
		Cuando Hibernate se sincroniza, solo mira los cambios en el Lado Propietario para escribir las 
		Claves Foráneas. Si no asignamos la categoría en el producto, para Hibernate la relación no existe, 
		aunque se haya añadido a la lista en memoria.

		2. ¿Por qué la categoría debe existir antes? 
		Porque Hibernate no permite enlazar un objeto Persistent (el producto que vas a guardar) con un objeto 
		Transient (la categoría nueva sin guardar). Hibernate comprueba el estado de las entidades antes de lanzar 
		el SQL. Si detecta que la entidad relacionada es Transient (no tiene ID), bloquea la operación 
		(lanzando TransientObjectException) porque sabe que la base de datos rechazaría un INSERT con una Clave Foránea 
		apuntando a "la nada".

	 */


	// EJERCICIO 1: PROVOCAR EL ERROR
	// Intentar guardar un Producto asociado a una Categoría que no existe en BD
	private static void ejercicio1_ProvocarError() {
		System.out.println("EJERCICIO 1: Provocando el error TransientObjectException");
		
		runInTransaction(em -> {
			// Crear categoría NO persistida 
			CategoriaJPA c = new CategoriaJPA();
			c.setNombre("Categoria Fantasma");
			
			// Crear producto
			ProductoJPA p = new ProductoJPA();
			p.setNombre("Producto Huerfano");
			p.setPrecio(new BigDecimal("10.00"));
			p.setStock(10);
			p.setAgotado(false);
			
			// Asociar el producto a la categoría
			p.setCategoria(c); // Establecemos la relación
			
			// Intentar persistir SOLO el producto
			System.out.println("Intentando hacer em.persist(producto)...");
			em.persist(p); 
			
			// Aquí fallará al hacer commit porque la categoría no tiene ID (no existe en BD)
		});
	}


	// EJERCICIO 2: CORREGIRLO SIN CASCADE
	// Guardar manualmente en el orden correcto: 1º Categoría, 2º Producto
	private static void ejercicio2_CorregirSinCascade() {
	    System.out.println("EJERCICIO 2: Guardando en orden correcto (Sin Cascade)");

	    runInTransaction(em -> {
	        CategoriaJPA c = new CategoriaJPA();
	        c.setNombre("Categoria Manual");

	        ProductoJPA p = new ProductoJPA();
	        p.setNombre("Producto Manual");
	        p.setPrecio(new BigDecimal("20.00"));
	        p.setStock(5);
	        p.setAgotado(false);

	        p.setCategoria(c);        // Rellena la relación (CATEGORIA_ID)
	        p.setCategoria("Manual"); // Rellena el texto antiguo (CATEGORIA) 

	        // Guardamos en orden: Primero Padre, luego Hijo
	        System.out.println("Persistiendo Categoría...");
	        em.persist(c);

	        System.out.println("Persistiendo Producto...");
	        em.persist(p);

	        System.out.println("Todo guardado correctamente.");
	    });
	}


	// EJERCICIO 3: CORREGIRLO CON CASCADE
	// Guardar solo la Categoría y dejar que Hibernate guarde el Producto
	private static void ejercicio3_CorregirConCascade() {
		System.out.println("EJERCICIO 3: Usando CascadeType.ALL");
		
		runInTransaction(em -> {
			CategoriaJPA c = new CategoriaJPA();
			c.setNombre("Categoria Cascada");
			
			ProductoJPA p = new ProductoJPA();
			p.setNombre("Producto Cascada");
			p.setPrecio(new BigDecimal("30.00"));
			p.setStock(8);
			p.setAgotado(false);
			
			// CORRECCIÓN BASE DE DATOS 
			p.setCategoria("Cascada"); 
			
			// VINCULACIÓN 
			// Esto añade el producto a la lista Y establece la relación (p.setCategoria(c))
			c.addProducto(p);
			
			// PERSISTENCIA EN CASCADA
			// Solo guardamos el Padre. Hibernate ve que tiene un hijo nuevo en la lista 
			// y lo guarda automáticamente gracias a CascadeType.ALL
			System.out.println("Persistiendo solo la Categoría...");
			em.persist(c);
			
			System.out.println("Categoría y Producto guardados automáticamente por cascada.");
		});
	}
	

	// Helper para gestionar la transacción 
	private static void runInTransaction(Consumer<EntityManager> work) {
		EntityManager em = JPAUtil.getEM();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			work.accept(em);
			tx.commit();
		} catch (RuntimeException e) {
			if (tx.isActive()) tx.rollback();
			// Imprimimos el error para el ejercicio 1
			System.err.println("ERROR CAPTURADO: " + e.getClass().getSimpleName());
			System.err.println("MENSAJE: " + e.getMessage());
			e.printStackTrace();
		} finally {
			em.close();
		}
	}
}