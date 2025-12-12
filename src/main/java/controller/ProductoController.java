package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Producto;
import dao.ProductoDao;

/**
 * Servlet implementation class Controlador
 */
@WebServlet("/productos")
public class ProductoController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProductoController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println(">>> Entrando en Controlador.doGet, action=" + request.getParameter("action"));

		String action = nullSafe(request.getParameter("action"));

		try {

			switch (action) {

			case "create":
				HttpSession session = request.getSession(false);
				if (session != null) {
					Object form = session.getAttribute("FLASH");
					Object flash = session.getAttribute("FLASH2");
					if (form != null) {
						request.setAttribute("FLASH", form);
						session.removeAttribute("FLASH");
						request.setAttribute("FLASH2", flash);
						session.removeAttribute("FLASH2");
					}
				}
				request.getRequestDispatcher("/WEB-INF/views/formulario.jsp").forward(request, response);
				break;
			default:

				ProductoDao dao = new ProductoDao();
				request.setAttribute("categorias", dao.findCategorias());
				String categoria = nullSafe(request.getParameter("categoria"));
				Boolean check = Boolean.parseBoolean(nullSafe(request.getParameter("soloAgotados")));
				request.setAttribute("productos", dao.findAll(categoria, check));
				request.getRequestDispatcher("/WEB-INF/views/listado.jsp").forward(request, response);
				break;
			}

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	private String nullSafe(String s) {
		return s == null ? "" : s.trim();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		Map<String, Object> form = new HashMap<String, Object>();
		Map<String, Object> flash = new HashMap<String, Object>();

		String action = nullSafe(request.getParameter("action"));

		ArrayList<String> errores = new ArrayList<String>();

		request.setAttribute("errores", errores);

		switch (action) {

		case "save":
			save(request, form, errores);
			break;
		case "delete":
			eliminar(request, flash, errores);
			break;
		case "inc":
			inc(request, flash, errores);
			break;
		case "dec":
			dec(request, flash, errores);
			break;
		case "toggle":
			toggle(request, flash, errores);
			break;
		}

		HttpSession session = request.getSession();

		session.setAttribute("FLASH", form);
		session.setAttribute("FLASH2", flash);
		response.sendRedirect(request.getContextPath() + "/productos");
	}

	private void save(HttpServletRequest request, Map<String, Object> form, ArrayList<String> errores) {

		String nombre = nullSafe(request.getParameter("nombre"));
		String categoria = nullSafe(request.getParameter("categoria"));
		double precioDouble = Double.parseDouble(nullSafe(request.getParameter("precio")));
		BigDecimal precio = BigDecimal.valueOf(precioDouble);
		int stock = Integer.parseInt(nullSafe(request.getParameter("stockInicial")));

		if (nombre == "") {

			errores.add("el nombre no puede ser nulo");
			return;

		}
		if (categoria == "") {

			errores.add("la categoria no puede ser nulo");

		}
		if (precioDouble < 0.0) {

			errores.add("el precio no puede ser negativo");
			return;
		}
		if (stock < 0) {

			errores.add("el nombre no puede ser negativo");
			return;
		}

		try {

			ProductoDao dao = new ProductoDao();

			Producto producto = new Producto();

			producto.setNombre(nombre);

			producto.setCategoria(categoria);

			producto.setPrecio(precio);

			producto.setAgotado(false);

			producto.setStock(stock);

			dao.insert(producto);

			form.put("nombre", nombre);
			form.put("categoria", categoria);
			form.put("precio", precio);
			form.put("stock", stock);

		} catch (Exception e) {

			errores.add(e.getMessage());

		}

	}

	private void eliminar(HttpServletRequest request, Map<String, Object> form, ArrayList<String> errores) {

		int id = Integer.parseInt(nullSafe(request.getParameter("id")));

		try {

			ProductoDao dao = new ProductoDao();

			dao.deleteById(id);

			form.put("type", "ok");
			form.put("message", "accion correcta");

		} catch (Exception e) {

			errores.add(e.getMessage());
		}

	}

	private void inc(HttpServletRequest request, Map<String, Object> form, ArrayList<String> errores) {

		int id = Integer.parseInt(nullSafe(request.getParameter("id")));

		try {

			ProductoDao dao = new ProductoDao();

			dao.incrementarStock(id, 1);

			form.put("type", "ok");
			form.put("message", "accion correcta");

		} catch (Exception e) {

			errores.add(e.getMessage());

		}

	}

	private void dec(HttpServletRequest request, Map<String, Object> form, ArrayList<String> errores) {

		int id = Integer.parseInt(nullSafe(request.getParameter("id")));

		String categoria = nullSafe(request.getParameter("categoriaActual"));

		try {

			ProductoDao dao = new ProductoDao();

			dao.decrementarStock(id, 1);

			form.put("type", "ok");
			form.put("message", "accion correcta");

		} catch (Exception e) {
			errores.add(e.getMessage());
		}

	}

	private void toggle(HttpServletRequest request, Map<String, Object> form, ArrayList<String> errores) {
		int id = Integer.parseInt(nullSafe(request.getParameter("id")));

		String categoria = nullSafe(request.getParameter("categoriaActual"));

		try {

			ProductoDao dao = new ProductoDao();

			dao.toggleAgotado(id);

			form.put("type", "ok");
			form.put("message", "accion correcta");

		} catch (Exception e) {

			errores.add(e.getMessage());
		}

	}

}
