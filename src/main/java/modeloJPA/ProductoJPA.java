package modeloJPA;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
// import jakarta.persistence.Column; // Opcional si quieres personalizar nombres

@Entity
@Table(name="PRODUCTO")
public class ProductoJPA {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="producto_seq")
	@SequenceGenerator(name="producto_seq", sequenceName="PRODUCTO_SEQ", allocationSize=1)
	@Column(name="ID")
	private Integer id;
	
	@Column(name="NOMBRE", nullable=false, length=100)
	private String nombre;
	
	@Column(name="CATEGORIA", length=50)
	private String categoriaTexto;
	
	@Column(name="PRECIO")
	private BigDecimal precio;
	
	@Column(name="STOCK")
	private int stock;
	
	@Column(name="AGOTADO")
	private String agotado;
	
	@ManyToOne
	@JoinColumn(name="CATEGORIA_ID")
	private CategoriaJPA categoria;
	
	public void setCategoria(CategoriaJPA categoria) {
		this.categoria = categoria;
	}
	
	public CategoriaJPA getCategoria() {
		return this.categoria;
	}


	public ProductoJPA() {
		
	}
	
	
	
	public ProductoJPA(String nombre, String categoria, BigDecimal precio, int stock, String agotado, CategoriaJPA c) {
		this.nombre = nombre;
		this.categoriaTexto = categoria;
		this.precio = precio;
		this.stock = stock;
		this.agotado = agotado;
		this.categoria= c;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public void setCategoria(String categoria) {
		this.categoriaTexto = categoria;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public boolean isAgotado() {
		return "S".equalsIgnoreCase(agotado);
	}

	public void setAgotado(boolean agotado) {
		this.agotado = agotado ? "S" : "N";
	}

	public Integer getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Producto [id=" + id + ", nombre=" + nombre + ", categoria=" + categoriaTexto + ", precio=" + precio
				+ ", stock=" + stock + ", agotado=" + agotado + "]";
	}
}