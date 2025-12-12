package modeloJPA;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name="CATEGORIA")
public class CategoriaJPA {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="categoria_seq")
	@SequenceGenerator(name="categoria_seq", sequenceName="CATEGORIA_SEQ", allocationSize=1)
	@Column(name="ID")
	private long id;
	
	@Column(name="NOMBRE")
	private String nombre;
	
	@OneToMany(mappedBy="categoria",
				cascade=CascadeType.ALL, 
				orphanRemoval=true)
				// fetch= FetchType.EAGER) -->  oneToMany por defecto Lazy
	private List <ProductoJPA> productos = new ArrayList<>();

	public CategoriaJPA() {
		super();
	}
	
	//helper -- IMPORTANTE
	public void addProducto(ProductoJPA p) {
		productos.add(p);
		p.setCategoria(this);
	}
	
	//helper -- IMPORTANTE
	public void removeProducto(ProductoJPA p) {
		productos.remove(p);
		// p.setCategoria(null); --> no puedo usarlo porque lo confunde con el string
		p.setCategoria((CategoriaJPA) null);
	}
	
	
	
	public void setId(long id) {
		this.id = id;
	}



	public CategoriaJPA(String nombre) {
		this.nombre = nombre;

	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<ProductoJPA> getProductos() {
		return productos;
	}

	public void setProductos(List<ProductoJPA> productos) {
		this.productos = productos;
	}

	public long getId() {
		return id;
	}
	

	@Override
	public String toString() {
	    return "CategoriaJPA [id=" + id + ", nombre=" + nombre + "]";
	}
	
	
}
