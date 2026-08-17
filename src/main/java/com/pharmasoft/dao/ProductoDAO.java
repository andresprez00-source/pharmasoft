package com.pharmasoft.dao;

import com.pharmasoft.conexion.ConexionBD;
import com.pharmasoft.modelo.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public boolean insertar(Producto producto) throws SQLException {
        String sql = "INSERT INTO productos " +
                "(codigo, nombre, categoria, precio, cantidad, fecha_vencimiento) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            cargarParametros(sentencia, producto);
            return sentencia.executeUpdate() > 0;
        }
    }

    public List<Producto> consultarTodos() throws SQLException {
        String sql = "SELECT id, codigo, nombre, categoria, precio, cantidad, fecha_vencimiento " +
                "FROM productos ORDER BY id";
        List<Producto> productos = new ArrayList<>();

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {
                productos.add(mapearProducto(resultado));
            }
        }
        return productos;
    }

    public Producto buscarPorCodigo(String codigo) throws SQLException {
        String sql = "SELECT id, codigo, nombre, categoria, precio, cantidad, fecha_vencimiento " +
                "FROM productos WHERE codigo = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setString(1, codigo);

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return mapearProducto(resultado);
                }
            }
        }
        return null;
    }

    public boolean actualizar(Producto producto) throws SQLException {
        String sql = "UPDATE productos SET codigo=?, nombre=?, categoria=?, precio=?, " +
                "cantidad=?, fecha_vencimiento=? WHERE id=?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            cargarParametros(sentencia, producto);
            sentencia.setInt(7, producto.getId());
            return sentencia.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM productos WHERE id=?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {

            sentencia.setInt(1, id);
            return sentencia.executeUpdate() > 0;
        }
    }

    private void cargarParametros(PreparedStatement sentencia, Producto producto)
            throws SQLException {
        sentencia.setString(1, producto.getCodigo());
        sentencia.setString(2, producto.getNombre());
        sentencia.setString(3, producto.getCategoria());
        sentencia.setBigDecimal(4, producto.getPrecio());
        sentencia.setInt(5, producto.getCantidad());

        if (producto.getFechaVencimiento() != null) {
            sentencia.setDate(6, Date.valueOf(producto.getFechaVencimiento()));
        } else {
            sentencia.setNull(6, Types.DATE);
        }
    }

    private Producto mapearProducto(ResultSet resultado) throws SQLException {
        Date fecha = resultado.getDate("fecha_vencimiento");

        return new Producto(
                resultado.getInt("id"),
                resultado.getString("codigo"),
                resultado.getString("nombre"),
                resultado.getString("categoria"),
                resultado.getBigDecimal("precio"),
                resultado.getInt("cantidad"),
                fecha != null ? fecha.toLocalDate() : null
        );
    }
}
