package com.pharmasoft.vista;

import com.pharmasoft.dao.ProductoDAO;
import com.pharmasoft.modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class PharmasoftApp extends JFrame {

    private final ProductoDAO productoDAO = new ProductoDAO();

    private final JTextField txtId = new JTextField();
    private final JTextField txtCodigo = new JTextField();
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtCategoria = new JTextField();
    private final JTextField txtPrecio = new JTextField();
    private final JTextField txtCantidad = new JTextField();
    private final JTextField txtFechaVencimiento = new JTextField();

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Código", "Nombre", "Categoría", "Precio", "Cantidad", "Vencimiento"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable tabla = new JTable(modeloTabla);

    public PharmasoftApp() {
        setTitle("PharmaSoft - Gestión de productos");
        setSize(1000, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        construirInterfaz();
        cargarProductos();
    }

    private void construirInterfaz() {
        JPanel formulario = new JPanel(new GridLayout(4, 4, 8, 8));
        formulario.setBorder(BorderFactory.createTitledBorder("Datos del producto"));

        txtId.setEditable(false);

        formulario.add(new JLabel("ID"));
        formulario.add(txtId);
        formulario.add(new JLabel("Código"));
        formulario.add(txtCodigo);

        formulario.add(new JLabel("Nombre"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("Categoría"));
        formulario.add(txtCategoria);

        formulario.add(new JLabel("Precio"));
        formulario.add(txtPrecio);
        formulario.add(new JLabel("Cantidad"));
        formulario.add(txtCantidad);

        formulario.add(new JLabel("Vencimiento (AAAA-MM-DD)"));
        formulario.add(txtFechaVencimiento);
        formulario.add(new JLabel(""));
        formulario.add(new JLabel(""));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnBuscar = new JButton("Buscar");
        JButton btnLimpiar = new JButton("Limpiar");

        btnRegistrar.addActionListener(e -> registrarProducto());
        btnActualizar.addActionListener(e -> actualizarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnBuscar.addActionListener(e -> buscarProducto());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        botones.add(btnRegistrar);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        botones.add(btnBuscar);
        botones.add(btnLimpiar);

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(e -> cargarSeleccion());

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Inventario"));

        JPanel principal = new JPanel(new BorderLayout(10, 10));
        principal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        principal.add(formulario, BorderLayout.NORTH);
        principal.add(scroll, BorderLayout.CENTER);
        principal.add(botones, BorderLayout.SOUTH);

        setContentPane(principal);
    }

    private Producto leerFormulario() {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String categoria = txtCategoria.getText().trim();

        if (codigo.isEmpty() || nombre.isEmpty() || categoria.isEmpty()) {
            throw new IllegalArgumentException("Código, nombre y categoría son obligatorios.");
        }

        BigDecimal precio = new BigDecimal(txtPrecio.getText().trim());
        int cantidad = Integer.parseInt(txtCantidad.getText().trim());

        if (precio.compareTo(BigDecimal.ZERO) < 0 || cantidad < 0) {
            throw new IllegalArgumentException("Precio y cantidad no pueden ser negativos.");
        }

        LocalDate fecha = null;
        String textoFecha = txtFechaVencimiento.getText().trim();
        if (!textoFecha.isEmpty()) {
            fecha = LocalDate.parse(textoFecha);
        }

        int id = txtId.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtId.getText().trim());

        return new Producto(id, codigo, nombre, categoria, precio, cantidad, fecha);
    }

    private void registrarProducto() {
        try {
            Producto producto = leerFormulario();
            productoDAO.insertar(producto);
            mostrarMensaje("Producto registrado correctamente.");
            limpiarFormulario();
            cargarProductos();
        } catch (NumberFormatException ex) {
            mostrarError("Precio, cantidad o ID no tienen un formato válido.");
        } catch (DateTimeParseException ex) {
            mostrarError("La fecha debe tener el formato AAAA-MM-DD.");
        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        } catch (Exception ex) {
            mostrarError("No se pudo registrar el producto: " + ex.getMessage());
        }
    }

    private void actualizarProducto() {
        try {
            if (txtId.getText().trim().isEmpty()) {
                mostrarError("Selecciona un producto de la tabla para actualizarlo.");
                return;
            }

            Producto producto = leerFormulario();
            productoDAO.actualizar(producto);
            mostrarMensaje("Producto actualizado correctamente.");
            limpiarFormulario();
            cargarProductos();
        } catch (NumberFormatException ex) {
            mostrarError("Precio, cantidad o ID no tienen un formato válido.");
        } catch (DateTimeParseException ex) {
            mostrarError("La fecha debe tener el formato AAAA-MM-DD.");
        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        } catch (Exception ex) {
            mostrarError("No se pudo actualizar el producto: " + ex.getMessage());
        }
    }

    private void eliminarProducto() {
        try {
            if (txtId.getText().trim().isEmpty()) {
                mostrarError("Selecciona un producto de la tabla para eliminarlo.");
                return;
            }

            int respuesta = JOptionPane.showConfirmDialog(
                    this,
                    "¿Deseas eliminar el producto seleccionado?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION
            );

            if (respuesta == JOptionPane.YES_OPTION) {
                int id = Integer.parseInt(txtId.getText().trim());
                productoDAO.eliminar(id);
                mostrarMensaje("Producto eliminado correctamente.");
                limpiarFormulario();
                cargarProductos();
            }
        } catch (Exception ex) {
            mostrarError("No se pudo eliminar el producto: " + ex.getMessage());
        }
    }

    private void buscarProducto() {
        String codigo = JOptionPane.showInputDialog(this, "Ingresa el código del producto:");

        if (codigo == null || codigo.trim().isEmpty()) {
            return;
        }

        try {
            Producto producto = productoDAO.buscarPorCodigo(codigo.trim());

            if (producto == null) {
                mostrarMensaje("No se encontró un producto con ese código.");
                return;
            }

            mostrarProductoEnFormulario(producto);
        } catch (Exception ex) {
            mostrarError("No se pudo realizar la búsqueda: " + ex.getMessage());
        }
    }

    private void cargarProductos() {
        modeloTabla.setRowCount(0);

        try {
            for (Producto producto : productoDAO.consultarTodos()) {
                modeloTabla.addRow(new Object[]{
                        producto.getId(),
                        producto.getCodigo(),
                        producto.getNombre(),
                        producto.getCategoria(),
                        producto.getPrecio(),
                        producto.getCantidad(),
                        producto.getFechaVencimiento()
                });
            }
        } catch (Exception ex) {
            mostrarError("No se pudieron cargar los productos. Verifica la conexión con MySQL.");
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();

        if (fila >= 0) {
            txtId.setText(String.valueOf(modeloTabla.getValueAt(fila, 0)));
            txtCodigo.setText(String.valueOf(modeloTabla.getValueAt(fila, 1)));
            txtNombre.setText(String.valueOf(modeloTabla.getValueAt(fila, 2)));
            txtCategoria.setText(String.valueOf(modeloTabla.getValueAt(fila, 3)));
            txtPrecio.setText(String.valueOf(modeloTabla.getValueAt(fila, 4)));
            txtCantidad.setText(String.valueOf(modeloTabla.getValueAt(fila, 5)));

            Object fecha = modeloTabla.getValueAt(fila, 6);
            txtFechaVencimiento.setText(fecha == null ? "" : fecha.toString());
        }
    }

    private void mostrarProductoEnFormulario(Producto producto) {
        txtId.setText(String.valueOf(producto.getId()));
        txtCodigo.setText(producto.getCodigo());
        txtNombre.setText(producto.getNombre());
        txtCategoria.setText(producto.getCategoria());
        txtPrecio.setText(producto.getPrecio().toString());
        txtCantidad.setText(String.valueOf(producto.getCantidad()));
        txtFechaVencimiento.setText(
                producto.getFechaVencimiento() == null ? "" : producto.getFechaVencimiento().toString()
        );
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtCodigo.setText("");
        txtNombre.setText("");
        txtCategoria.setText("");
        txtPrecio.setText("");
        txtCantidad.setText("");
        txtFechaVencimiento.setText("");
        tabla.clearSelection();
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "PharmaSoft", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "PharmaSoft", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PharmasoftApp().setVisible(true));
    }
}
