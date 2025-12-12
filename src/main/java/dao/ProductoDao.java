package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import conexion.Conexion;
import model.Producto;

public class ProductoDao {

    private static Producto map(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getInt("id"));
        p.setNombre(rs.getString("nombre"));
        p.setCategoria(rs.getString("categoria"));
        p.setPrecio(rs.getBigDecimal("precio"));
        p.setStock(rs.getInt("stock"));
        p.setAgotado("S".equals(rs.getString("agotado")));
        return p;
    }

    public List<String> findCategorias() throws Exception {
        String sql = "SELECT DISTINCT categoria FROM producto ORDER BY categoria";
        try (Connection c = Conexion.getConexion();
             PreparedStatement pst = c.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            List<String> categorias = new ArrayList<>();
            while (rs.next()) {
                categorias.add(rs.getString(1));
            }
            return categorias;
        }
    }

    public List<Producto> findAll() throws Exception {
        return findAll(null, false);
    }

    public List<Producto> findAll(String categoria, Boolean soloAgotados) throws Exception {
        StringBuilder sb = new StringBuilder("SELECT * FROM producto WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (categoria != null && !categoria.isBlank()) {
            sb.append(" AND LOWER(categoria) = ?");
            params.add(categoria.trim().toLowerCase());
        }
        if (Boolean.TRUE.equals(soloAgotados)) {
            sb.append(" AND agotado = 'S'");
        }
        sb.append(" ORDER BY id DESC");

        try (Connection c = Conexion.getConexion();
             PreparedStatement pst = c.prepareStatement(sb.toString())) {

            for (int i = 0; i < params.size(); i++) {
                pst.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = pst.executeQuery()) {
                List<Producto> productos = new ArrayList<>();
                while (rs.next()) {
                    productos.add(map(rs));
                }
                return productos;
            }
        }
    }

    public void insert(Producto p) throws Exception {
    	String sql = "INSERT INTO PRODUCTO (ID, NOMBRE, CATEGORIA, PRECIO, STOCK, AGOTADO) VALUES (?, ?, ?, ?, ?, ?)";
    	try (Connection c = Conexion.getConexion();
    		PreparedStatement ps = c.prepareStatement(sql)) {
    		ps.setInt(1, p.getId());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getCategoria());
            ps.setBigDecimal(4, p.getPrecio());
            ps.setInt(5, p.getStock());
            ps.setBoolean(6, p.isAgotado());
            ps.executeUpdate();
    	}
    }

    public void incrementarStock(int id, int cantidad) throws Exception {
        String sql = "UPDATE producto SET stock = stock + ? WHERE id = ?";
        int k = Math.max(1, cantidad);
        try (Connection c = Conexion.getConexion();
             PreparedStatement pst = c.prepareStatement(sql)) {

            pst.setInt(1, k);
            pst.setInt(2, id);
            pst.executeUpdate();
        }
    }

    public boolean decrementarStock(int id, int cantidad) throws Exception {
        // Evitar stock negativo directamente en la sentencia SQL
        String sql = "UPDATE producto SET stock = stock - ? WHERE id = ? AND stock >= ?";
        int k = Math.max(1, cantidad);
        try (Connection c = Conexion.getConexion();
             PreparedStatement pst = c.prepareStatement(sql)) {

            pst.setInt(1, k);
            pst.setInt(2, id);
            pst.setInt(3, k);
            int updated = pst.executeUpdate();
            return updated == 1;
        }
    }

    public void toggleAgotado(int id) throws Exception {
        String sql = "UPDATE producto SET agotado = CASE WHEN agotado='S' THEN 'N' ELSE 'S' END WHERE id = ?";
        try (Connection c = Conexion.getConexion();
             PreparedStatement pst = c.prepareStatement(sql)) {

            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    public boolean deleteById(int id) throws Exception {
        String sql = "DELETE FROM producto WHERE id = ? AND stock = 0";
        try (Connection c = Conexion.getConexion();
             PreparedStatement pst = c.prepareStatement(sql)) {

            pst.setInt(1, id);
            int deleted = pst.executeUpdate();
            return deleted == 1;
        }
    }

	
}
