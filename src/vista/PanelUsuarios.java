package vista;

import dao.UsuarioDAO;
import modelo.Usuario;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelUsuarios extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtBuscar;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public PanelUsuarios() {
        initComponents();
        cargarUsuarios();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(245, 245, 245));

        JLabel titulo = new JLabel("Gestión de Usuarios", SwingConstants.CENTER);
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
                new Object[]{"ID", "Nombre", "Apellido", "DNI", "Correo", "Rol"}, 0
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

        btnAgregar.addActionListener(e -> agregarUsuario());
        btnModificar.addActionListener(e -> modificarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);

        add(panelBotones, BorderLayout.SOUTH);

        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                buscarUsuarios();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                buscarUsuarios();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                buscarUsuarios();
            }
        });
    }

    private void cargarUsuarios() {
        modelo.setRowCount(0);
        List<Usuario> lista = usuarioDAO.listarUsuarios();

        for (Usuario u : lista) {
            modelo.addRow(new Object[]{
                    u.getIdUsuario(),
                    u.getNombre(),
                    u.getApellido(),
                    u.getDni(),
                    u.getCorreo(),
                    u.getRol()
            });
        }
    }

    private void buscarUsuarios() {
        String texto = txtBuscar.getText().trim();

        if (texto.isEmpty()) {
            cargarUsuarios();
            return;
        }

        modelo.setRowCount(0);
        List<Usuario> lista = usuarioDAO.buscarUsuarios(texto);

        for (Usuario u : lista) {
            modelo.addRow(new Object[]{
                    u.getIdUsuario(),
                    u.getNombre(),
                    u.getApellido(),
                    u.getDni(),
                    u.getCorreo(),
                    u.getRol()
            });
        }
    }

    private void agregarUsuario() {
        JTextField txtNombre = new JTextField();
        JTextField txtApellido = new JTextField();
        JTextField txtDni = new JTextField();
        JTextField txtCorreo = new JTextField();
        JTextField txtPassword = new JTextField();
        JComboBox<String> cbRol = new JComboBox<>(new String[]{"admin", "trabajador"});

        Object[] campos = {
                "Nombre:", txtNombre,
                "Apellido:", txtApellido,
                "DNI:", txtDni,
                "Correo:", txtCorreo,
                "Contraseña:", txtPassword,
                "Rol:", cbRol
        };

        int op = JOptionPane.showConfirmDialog(this, campos, "Agregar Usuario", JOptionPane.OK_CANCEL_OPTION);
        if (op == JOptionPane.OK_OPTION) {
            Usuario u = new Usuario();
            u.setNombre(txtNombre.getText().trim());
            u.setApellido(txtApellido.getText().trim());
            u.setDni(txtDni.getText().trim());
            u.setCorreo(txtCorreo.getText().trim());
            u.setPassword(txtPassword.getText().trim());
            u.setRol(cbRol.getSelectedItem().toString());

            if (usuarioDAO.insertarUsuario(u)) {
                JOptionPane.showMessageDialog(this, "Usuario agregado correctamente.");
                buscarUsuarios();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo agregar el usuario.");
            }
        }
    }

    private void modificarUsuario() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario.");
            return;
        }

        int idUsuario = (int) modelo.getValueAt(fila, 0);
        Usuario u = usuarioDAO.buscarPorId(idUsuario);

        if (u == null) {
            JOptionPane.showMessageDialog(this, "No se encontró el usuario.");
            return;
        }

        JTextField txtNombre = new JTextField(u.getNombre());
        JTextField txtApellido = new JTextField(u.getApellido());
        JTextField txtDni = new JTextField(u.getDni());
        JTextField txtCorreo = new JTextField(u.getCorreo());
        JTextField txtPassword = new JTextField(u.getPassword());
        JComboBox<String> cbRol = new JComboBox<>(new String[]{"admin", "trabajador"});
        cbRol.setSelectedItem(u.getRol());

        Object[] campos = {
                "Nombre:", txtNombre,
                "Apellido:", txtApellido,
                "DNI:", txtDni,
                "Correo:", txtCorreo,
                "Contraseña:", txtPassword,
                "Rol:", cbRol
        };

        int op = JOptionPane.showConfirmDialog(this, campos, "Modificar Usuario", JOptionPane.OK_CANCEL_OPTION);
        if (op == JOptionPane.OK_OPTION) {
            u.setNombre(txtNombre.getText().trim());
            u.setApellido(txtApellido.getText().trim());
            u.setDni(txtDni.getText().trim());
            u.setCorreo(txtCorreo.getText().trim());
            u.setPassword(txtPassword.getText().trim());
            u.setRol(cbRol.getSelectedItem().toString());

            if (usuarioDAO.actualizarUsuario(u)) {
                JOptionPane.showMessageDialog(this, "Usuario actualizado correctamente.");
                buscarUsuarios();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar el usuario.");
            }
        }
    }

    private void eliminarUsuario() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario.");
            return;
        }

        int idUsuario = (int) modelo.getValueAt(fila, 0);

        int op = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el usuario seleccionado?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (op == JOptionPane.YES_OPTION) {
            if (usuarioDAO.eliminarUsuario(idUsuario)) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado.");
                buscarUsuarios();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el usuario.");
            }
        }
    }
}