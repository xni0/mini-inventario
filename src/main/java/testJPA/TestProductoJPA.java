package testJPA;

import java.math.BigDecimal;
import java.util.List;

import daoJPA.DaoProductoJPA;
import jakarta.persistence.EntityManager;
import modeloJPA.CategoriaJPA;
import modeloJPA.ProductoJPA;
import utils.JPAUtil;

public class TestProductoJPA {

	private static DaoProductoJPA daoP = new DaoProductoJPA();
	
	public static void main(String[] args) {
		
		System.out.println("PROBANDO BUSQUEDA POR PRECIO MÍNIMO");

		BigDecimal precioFiltro = new BigDecimal("20.00");
		List<ProductoJPA> caros = daoP.findByPrecioMinimo(precioFiltro);

		System.out.println("Productos encontrados con precio mayor o igual a " + precioFiltro + ": " + caros.size());

		for (ProductoJPA p : caros) {
		    System.out.println(" - " + p.getNombre() + " (" + p.getPrecio() + " €)");
		}

		System.out.println("TEST FIND BY CATEGORIA");
		for (ProductoJPA p : findByCategoria((long)1)) {
			System.out.println(p.toString());
		}
		
		System.out.println("TEST FIND BY NAME");
		List<ProductoJPA> productosPorNombre = findByName("Monitor");
		mostrarListadoProductos(productosPorNombre);
		
		
		//Test remove by id
//		daoP.delete((long)6);

		//Test update
//		CategoriaJPA c= new CategoriaJPA();
//		c.setId(1);
//		ProductoJPA p=findById((long)7);
//		p.setCategoria(c);
//		update(p);
		
		//Test Create
//		ProductoJPA p= new ProductoJPA("Camisa", "Ropa", new BigDecimal(45L), 23, "N", c);
//		create(p);
		
		//Test findAll
//		mostrarListadoProductos(findAll());
	}
	
	private static List<ProductoJPA> findByCategoria(Long id){
		return daoP.findByCategoria(id);	
	}
	
	private static void update(ProductoJPA p) {
		daoP.update(p);
		System.out.println("Producto actualizado correctamente.");
	}
	
	private static void create(ProductoJPA p) {
		daoP.create(p);
	}
	
	private static ProductoJPA findById(Long id) {
		return daoP.findById(id);	
	}
	
	private static List<ProductoJPA> findAll(){
		return daoP.findAll();
	}
	
	private static List<ProductoJPA> findByName(String name){
		return daoP.findByName(name);
	}
	
	private static void mostrarListadoProductos(List<ProductoJPA> listado) {
		for (ProductoJPA producto : listado) {
			System.out.println(producto.toString());
		}
	}

}