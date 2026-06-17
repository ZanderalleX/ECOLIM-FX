package vista;

import dao.UbicacionDAO;
import modelo.Ubicacion;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelUbicaciones extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtBuscar;
    private final UbicacionDAO ubicacionDAO = new UbicacionDAO();

    public PanelUbicaciones() {
        initComponents();
        cargarUbicaciones();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(245, 245, 245));

        JLabel titulo = new JLabel("Gestión de Ubicaciones", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        panelSuperior.add(titulo, BorderLayout.NORTH);

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBusqueda.setBackground(new Color(245, 245, 245));

        JLabel lblBuscar = new JLabel("Buscar:");
        txtBuscar = new JTextField(25);

        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(txtBuscar);

        panelSuperior.add(panelBusqueda, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);

        modelo = new DefaultTableModel(
                new Object[]{"ID", "Nombre del Lugar", "Dirección"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        JButton btnAgregar = new JButton("Agregar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");

        btnAgregar.addActionListener(e -> agregarUbicacion());
        btnModificar.addActionListener(e -> modificarUbicacion());
        btnEliminar.addActionListener(e -> eliminarUbicacion());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);

        add(panelBotones, BorderLayout.SOUTH);

        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                buscarUbicaciones();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                buscarUbicaciones();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                buscarUbicaciones();
            }
        });
    }

    private void cargarUbicaciones() {
        modelo.setRowCount(0);
        List<Ubicacion> lista = ubicacionDAO.listarUbicaciones();

        for (Ubicacion u : lista) {
            modelo.addRow(new Object[]{
                    u.getIdUbicacion(),
                    u.getNombreLugar(),
                    u.getDireccion()
            });
        }
    }

    private void buscarUbicaciones() {
        String texto = txtBuscar.getText().trim();

        if (texto.isEmpty()) {
            cargarUbicaciones();
            return;
        }

        modelo.setRowCount(0);
        List<Ubicacion> lista = ubicacionDAO.buscarUbicaciones(texto);

        for (Ubicacion u : lista) {
            modelo.addRow(new Object[]{
                    u.getIdUbicacion(),
                    u.getNombreLugar(),
                    u.getDireccion()
            });
        }
    }

    private void agregarUbicacion() {
        JTextField txtNombre = new JTextField();
        JTextField txtDireccion = new JTextField();

        Object[] campos = {
                "Nombre del lugar:", txtNombre,
                "Dirección:", txtDireccion
        };

        int op = JOptionPane.showConfirmDialog(this, campos, "Agregar Ubicación", JOptionPane.OK_CANCEL_OPTION);
        if (op == JOptionPane.OK_OPTION) {
            Ubicacion u = new Ubicacion();
            u.setNombreLugar(txtNombre.getText().trim());
            u.setDireccion(txtDireccion.getText().trim());

            if (ubicacionDAO.insertarUbicacion(u)) {
                JOptionPane.showMessageDialog(this, "Ubicación agregada correctamente.");
                buscarUbicaciones();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo agregar la ubicación.");
            }
        }
    }

    private void modificarUbicacion() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una ubicación.");
            return;
        }

        int idUbicacion = (int) modelo.getValueAt(fila, 0);
        Ubicacion u = ubicacionDAO.buscarPorId(idUbicacion);

        if (u == null) {
            JOptionPane.showMessageDialog(this, "No se encontró la ubicación.");
            return;
        }

        JTextField txtNombre = new JTextField(u.getNombreLugar());
        JTextField txtDireccion = new JTextField(u.getDireccion());

        Object[] campos = {
                "Nombre del lugar:", txtNombre,
                "Dirección:", txtDireccion
        };

        int op = JOptionPane.showConfirmDialog(this, campos, "Modificar Ubicación", JOptionPane.OK_CANCEL_OPTION);
        if (op == JOptionPane.OK_OPTION) {
            u.setNombreLugar(txtNombre.getText().trim());
            u.setDireccion(txtDireccion.getText().trim());

            if (ubicacionDAO.actualizarUbicacion(u)) {
                JOptionPane.showMessageDialog(this, "Ubicación actualizada correctamente.");
                buscarUbicaciones();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar la ubicación.");
            }
        }
    }

    private void eliminarUbicacion() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una ubicación.");
            return;
        }

        int idUbicacion = (int) modelo.getValueAt(fila, 0);

        int op = JOptionPane.showConfirmDialog(this,
                "¿Eliminar la ubicación seleccionada?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (op == JOptionPane.YES_OPTION) {
            if (ubicacionDAO.eliminarUbicacion(idUbicacion)) {
                JOptionPane.showMessageDialog(this, "Ubicación eliminada.");
                buscarUbicaciones();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar la ubicación.");
            }
        }
    }
}