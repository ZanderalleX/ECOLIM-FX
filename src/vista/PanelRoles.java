/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package vista;

import dao.RolDAO;
import modelo.Rol;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelRoles extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtBuscar;

    private final RolDAO rolDAO = new RolDAO();

    public PanelRoles() {
        initComponents();
        cargarRoles();
    }

    private void initComponents() {

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(245, 245, 245));

        JLabel titulo = new JLabel("Gestión de Roles", SwingConstants.CENTER);
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
                new Object[]{
                        "ID",
                        "Nombre"
                }, 0
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

        btnAgregar.addActionListener(e -> agregarRol());
        btnModificar.addActionListener(e -> modificarRol());
        btnEliminar.addActionListener(e -> eliminarRol());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);

        add(panelBotones, BorderLayout.SOUTH);

        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                buscarRoles();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                buscarRoles();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                buscarRoles();
            }
        });
    }

    private void cargarRoles() {

        modelo.setRowCount(0);

        List<Rol> lista = rolDAO.listarRoles();

        for (Rol r : lista) {

            modelo.addRow(new Object[]{
                    r.getId(),
                    r.getName()
            });
        }
    }

    private void buscarRoles() {

        String texto = txtBuscar.getText().trim().toLowerCase();

        modelo.setRowCount(0);

        List<Rol> lista = rolDAO.listarRoles();

        for (Rol r : lista) {

            if (
                    String.valueOf(r.getId()).contains(texto)
                            || (r.getName() != null
                            && r.getName().toLowerCase().contains(texto))
            ) {

                modelo.addRow(new Object[]{
                        r.getId(),
                        r.getName()
                });
            }
        }
    }

    private void agregarRol() {

        JTextField txtNombre = new JTextField();

        Object[] campos = {
                "Nombre:", txtNombre
        };

        int op = JOptionPane.showConfirmDialog(
                this,
                campos,
                "Agregar Rol",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (op == JOptionPane.OK_OPTION) {

            Rol r = new Rol();

            r.setName(txtNombre.getText().trim());

            if (rolDAO.insertarRol(r)) {

                JOptionPane.showMessageDialog(this,
                        "Rol agregado correctamente.");

                cargarRoles();

            } else {

                JOptionPane.showMessageDialog(this,
                        "No se pudo agregar el rol.");
            }
        }
    }

    private void modificarRol() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(this,
                    "Seleccione un rol.");

            return;
        }

        long id = Long.parseLong(
                modelo.getValueAt(fila, 0).toString()
        );

        Rol r = rolDAO.buscarPorId(id);

        if (r == null) {

            JOptionPane.showMessageDialog(this,
                    "No se encontró el rol.");

            return;
        }

        JTextField txtNombre = new JTextField(r.getName());

        Object[] campos = {
                "Nombre:", txtNombre
        };

        int op = JOptionPane.showConfirmDialog(
                this,
                campos,
                "Modificar Rol",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (op == JOptionPane.OK_OPTION) {

            r.setName(txtNombre.getText().trim());

            if (rolDAO.actualizarRol(r)) {

                JOptionPane.showMessageDialog(this,
                        "Rol actualizado correctamente.");

                cargarRoles();

            } else {

                JOptionPane.showMessageDialog(this,
                        "No se pudo actualizar el rol.");
            }
        }
    }

    private void eliminarRol() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(this,
                    "Seleccione un rol.");

            return;
        }

        long id = Long.parseLong(
                modelo.getValueAt(fila, 0).toString()
        );

        int op = JOptionPane.showConfirmDialog(
                this,
                "¿Eliminar el rol seleccionado?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (op == JOptionPane.YES_OPTION) {

            if (rolDAO.eliminarRol(id)) {

                JOptionPane.showMessageDialog(this,
                        "Rol eliminado.");

                cargarRoles();

            } else {

                JOptionPane.showMessageDialog(this,
                        "No se pudo eliminar el rol.");
            }
        }
    }
}