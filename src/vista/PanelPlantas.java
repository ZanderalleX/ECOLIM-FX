/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import dao.PlantaDAO;
import modelo.Planta;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class PanelPlantas extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtBuscar;

    private final PlantaDAO plantaDAO = new PlantaDAO();

    public PanelPlantas() {
        initComponents();
        cargarPlantas();
    }

    private void initComponents() {

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(245, 245, 245));

        JLabel titulo = new JLabel("Gestión de Plantas", SwingConstants.CENTER);
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
                        "Código",
                        "Nombre",
                        "Dirección",
                        "Teléfono",
                        "Latitud",
                        "Longitud",
                        "Estado"
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

        btnAgregar.addActionListener(e -> agregarPlanta());
        btnModificar.addActionListener(e -> modificarPlanta());
        btnEliminar.addActionListener(e -> eliminarPlanta());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);

        add(panelBotones, BorderLayout.SOUTH);

        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                buscarPlantas();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                buscarPlantas();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                buscarPlantas();
            }
        });
    }

    private void cargarPlantas() {

        modelo.setRowCount(0);

        List<Planta> lista = plantaDAO.listarPlantas();

        for (Planta p : lista) {

            modelo.addRow(new Object[]{
                    p.getId(),
                    p.getCode(),
                    p.getName(),
                    p.getAddress(),
                    p.getPhone(),
                    p.getLatitude(),
                    p.getLongitude(),
                    p.isStatus() ? "Activo" : "Inactivo"
            });
        }
    }

    private void buscarPlantas() {

        String texto = txtBuscar.getText().trim().toLowerCase();

        modelo.setRowCount(0);

        List<Planta> lista = plantaDAO.listarPlantas();

        for (Planta p : lista) {

            if (
                    String.valueOf(p.getId()).contains(texto)
                            || (p.getCode() != null && p.getCode().toLowerCase().contains(texto))
                            || (p.getName() != null && p.getName().toLowerCase().contains(texto))
                            || (p.getAddress() != null && p.getAddress().toLowerCase().contains(texto))
                            || (p.getPhone() != null && p.getPhone().toLowerCase().contains(texto))
            ) {

                modelo.addRow(new Object[]{
                        p.getId(),
                        p.getCode(),
                        p.getName(),
                        p.getAddress(),
                        p.getPhone(),
                        p.getLatitude(),
                        p.getLongitude(),
                        p.isStatus() ? "Activo" : "Inactivo"
                });
            }
        }
    }

    private void agregarPlanta() {

        JTextField txtCode = new JTextField();
        JTextField txtName = new JTextField();
        JTextField txtAddress = new JTextField();
        JTextField txtPhone = new JTextField();
        JTextField txtLatitude = new JTextField();
        JTextField txtLongitude = new JTextField();

        JCheckBox chkStatus = new JCheckBox("Activo");

        Object[] campos = {
                "Código:", txtCode,
                "Nombre:", txtName,
                "Dirección:", txtAddress,
                "Teléfono:", txtPhone,
                "Latitud:", txtLatitude,
                "Longitud:", txtLongitude,
                "Estado:", chkStatus
        };

        int op = JOptionPane.showConfirmDialog(
                this,
                campos,
                "Agregar Planta",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (op == JOptionPane.OK_OPTION) {

            try {

                Planta p = new Planta();

                p.setCode(txtCode.getText().trim());
                p.setName(txtName.getText().trim());
                p.setAddress(txtAddress.getText().trim());
                p.setPhone(txtPhone.getText().trim());

                if (!txtLatitude.getText().trim().isEmpty()) {
                    p.setLatitude(new BigDecimal(txtLatitude.getText().trim()));
                }

                if (!txtLongitude.getText().trim().isEmpty()) {
                    p.setLongitude(new BigDecimal(txtLongitude.getText().trim()));
                }

                p.setStatus(chkStatus.isSelected());

                if (plantaDAO.insertarPlanta(p)) {

                    JOptionPane.showMessageDialog(this,
                            "Planta agregada correctamente.");

                    cargarPlantas();

                } else {

                    JOptionPane.showMessageDialog(this,
                            "No se pudo agregar la planta.");
                }

            } catch (Exception e) {

                JOptionPane.showMessageDialog(this,
                        "Datos inválidos.");
            }
        }
    }

    private void modificarPlanta() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(this,
                    "Seleccione una planta.");

            return;
        }

        long id = Long.parseLong(
                modelo.getValueAt(fila, 0).toString()
        );

        Planta p = plantaDAO.buscarPorId(id);

        if (p == null) {

            JOptionPane.showMessageDialog(this,
                    "No se encontró la planta.");

            return;
        }

        JTextField txtCode = new JTextField(p.getCode());
        JTextField txtName = new JTextField(p.getName());
        JTextField txtAddress = new JTextField(p.getAddress());
        JTextField txtPhone = new JTextField(p.getPhone());

        JTextField txtLatitude = new JTextField(
                p.getLatitude() != null ? p.getLatitude().toString() : ""
        );

        JTextField txtLongitude = new JTextField(
                p.getLongitude() != null ? p.getLongitude().toString() : ""
        );

        JCheckBox chkStatus = new JCheckBox("Activo", p.isStatus());

        Object[] campos = {
                "Código:", txtCode,
                "Nombre:", txtName,
                "Dirección:", txtAddress,
                "Teléfono:", txtPhone,
                "Latitud:", txtLatitude,
                "Longitud:", txtLongitude,
                "Estado:", chkStatus
        };

        int op = JOptionPane.showConfirmDialog(
                this,
                campos,
                "Modificar Planta",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (op == JOptionPane.OK_OPTION) {

            try {

                p.setCode(txtCode.getText().trim());
                p.setName(txtName.getText().trim());
                p.setAddress(txtAddress.getText().trim());
                p.setPhone(txtPhone.getText().trim());

                if (!txtLatitude.getText().trim().isEmpty()) {
                    p.setLatitude(new BigDecimal(txtLatitude.getText().trim()));
                }

                if (!txtLongitude.getText().trim().isEmpty()) {
                    p.setLongitude(new BigDecimal(txtLongitude.getText().trim()));
                }

                p.setStatus(chkStatus.isSelected());

                if (plantaDAO.actualizarPlanta(p)) {

                    JOptionPane.showMessageDialog(this,
                            "Planta actualizada correctamente.");

                    cargarPlantas();

                } else {

                    JOptionPane.showMessageDialog(this,
                            "No se pudo actualizar la planta.");
                }

            } catch (Exception e) {

                JOptionPane.showMessageDialog(this,
                        "Datos inválidos.");
            }
        }
    }

    private void eliminarPlanta() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(this,
                    "Seleccione una planta.");

            return;
        }

        long id = Long.parseLong(
                modelo.getValueAt(fila, 0).toString()
        );

        int op = JOptionPane.showConfirmDialog(
                this,
                "¿Eliminar la planta seleccionada?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (op == JOptionPane.YES_OPTION) {

            if (plantaDAO.eliminarPlanta(id)) {

                JOptionPane.showMessageDialog(this,
                        "Planta eliminada.");

                cargarPlantas();

            } else {

                JOptionPane.showMessageDialog(this,
                        "No se pudo eliminar la planta.");
            }
        }
    }
}