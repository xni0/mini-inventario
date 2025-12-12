package testJPA;

import java.util.List;
import daoJPA.DaoCategoriaJPA;
import modeloJPA.CategoriaJPA;

public class TestCategoriaJPA {

	private static DaoCategoriaJPA daoC = new DaoCategoriaJPA();

	public static void main(String[] args) {
		
		System.out.println("INICIANDO PRUEBAS JPA");


		// INSERCIÓN (CREATE) 
		/*
		System.out.println("EJECUTANDO INSERCIÓN...");
		CategoriaJPA nuevaCat = new CategoriaJPA("Decoración");
		daoC.create(nuevaCat);
		System.out.println("Categoría insertada correctamente. ID generado: " + nuevaCat.getId());
		*/
		


		// CONSULTA 
		/*
		System.out.println("EJECUTANDO CONSULTAS...");
		List<CategoriaJPA> lista = daoC.findAll();
		for (CategoriaJPA c : lista) {
			System.out.println("ID: " + c.getId() + "Nombre: " + c.getNombre());
		}
		*/
		
		// ACTUALIZACIÓN
		/*
		System.out.println("EJECUTANDO ACTUALIZACIÓN...");
		Long idAEditar = 21L; 
		CategoriaJPA paraEditar = daoC.findById(idAEditar);
		if (paraEditar != null) {
			paraEditar.setNombre("Decoración de Interiores"); 
			daoC.update(paraEditar);
			System.out.println("Update realizado.");
		}
		*/

		
		//ELIMINACIÓN 
		System.out.println("EJECUTANDO ELIMINACIÓN...");
		
		// ID a borrar 
		Long idABorrar = 21L;
		
		System.out.println("Intentando borrar la categoría ID: " + idABorrar);
		
		// Llamamos al método delete del DAO
		daoC.delete(idABorrar);
		
		// Verificamos buscando de nuevo el ID
		CategoriaJPA borrada = daoC.findById(idABorrar);
		
		if (borrada == null) {
			System.out.println("RESULTADO: ÉXITO. La categoría ha sido eliminada (findById devolvió null).");
		} else {
			System.err.println("ERROR: La categoría sigue existiendo en la base de datos.");
		}
		
		
		System.out.println("\n==========================================");
	}
}