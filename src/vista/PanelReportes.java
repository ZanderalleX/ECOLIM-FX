package vista;

import dao.ReporteDAO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.List;

public class PanelReportes extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JComboBox<ItemCombo> cbUsuarios;
    private JComboBox<ItemCombo> cbUbicaciones;

    private final ReporteDAO reporteDAO = new ReporteDAO();

    public PanelReportes() {
        initComponents();
        cargarCombos();
        cargarReporteGeneral();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 245));

        JLabel titulo = new JLabel("Reportes de Reciclaje", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelSuperior.setBackground(new Color(245, 245, 245));

        cbUsuarios = new JComboBox<>();
        cbUbicaciones = new JComboBox<>();

        JButton btnMostrarTodo = new JButton("Mostrar todo");
        JButton btnFiltrarUsuario = new JButton("Filtrar por usuario");
        JButton btnFiltrarUbicacion = new JButton("Filtrar por ubicación");
        JButton btnExcelUsuario = new JButton("Excel por usuario");
        JButton btnExcelUbicacion = new JButton("Excel por ubicación");

        panelSuperior.add(new JLabel("Usuario:"));
        panelSuperior.add(cbUsuarios);
        panelSuperior.add(btnFiltrarUsuario);
        panelSuperior.add(btnExcelUsuario);

        panelSuperior.add(new JLabel("Ubicación:"));
        panelSuperior.add(cbUbicaciones);
        panelSuperior.add(btnFiltrarUbicacion);
        panelSuperior.add(btnExcelUbicacion);

        panelSuperior.add(btnMostrarTodo);

        add(panelSuperior, BorderLayout.SOUTH);

        modelo = new DefaultTableModel(
                new Object[]{"ID Registro", "ID Usuario", "Trabajador", "ID Ubicación", "Ubicación", "Residuo", "Cantidad", "Unidad", "Fecha", "Observaciones"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(24);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnMostrarTodo.addActionListener(e -> cargarReporteGeneral());
        btnFiltrarUsuario.addActionListener(e -> filtrarPorUsuario());
        btnFiltrarUbicacion.addActionListener(e -> filtrarPorUbicacion());
        btnExcelUsuario.addActionListener(e -> exportarExcelPorUsuario());
        btnExcelUbicacion.addActionListener(e -> exportarExcelPorUbicacion());
    }

    private void cargarCombos() {
        cbUsuarios.removeAllItems();
        cbUbicaciones.removeAllItems();

        cbUsuarios.addItem(new ItemCombo(0, "Seleccione"));
        cbUbicaciones.addItem(new ItemCombo(0, "Seleccione"));

        List<Object[]> usuarios = reporteDAO.listarUsuariosTrabajadores();
        for (Object[] fila : usuarios) {
            cbUsuarios.addItem(new ItemCombo((int) fila[0], fila[1].toString()));
        }

        List<Object[]> ubicaciones = reporteDAO.listarUbicaciones();
        for (Object[] fila : ubicaciones) {
            cbUbicaciones.addItem(new ItemCombo((int) fila[0], fila[1].toString()));
        }
    }

    private void cargarReporteGeneral() {
        llenarTabla(reporteDAO.listarReporteGeneral());
    }

    private void filtrarPorUsuario() {
        ItemCombo item = (ItemCombo) cbUsuarios.getSelectedItem();

        if (item == null || item.getId() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario.");
            return;
        }

        llenarTabla(reporteDAO.listarReportePorUsuario(item.getId()));
    }

    private void filtrarPorUbicacion() {
        ItemCombo item = (ItemCombo) cbUbicaciones.getSelectedItem();

        if (item == null || item.getId() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una ubicación.");
            return;
        }

        llenarTabla(reporteDAO.listarReportePorUbicacion(item.getId()));
    }

    private void llenarTabla(List<Object[]> lista) {
        modelo.setRowCount(0);

        for (Object[] fila : lista) {
            modelo.addRow(fila);
        }
    }

    private void exportarExcelPorUsuario() {
        ItemCombo item = (ItemCombo) cbUsuarios.getSelectedItem();

        if (item == null || item.getId() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para exportar.");
            return;
        }

        List<Object[]> datos = reporteDAO.listarReportePorUsuario(item.getId());

        if (datos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ese usuario no tiene registros.");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new java.io.File("reporte_usuario_" + item.getTexto().replace(" ", "_") + ".xlsx"));

        int opcion = chooser.showSaveDialog(this);

        if (opcion == JFileChooser.APPROVE_OPTION) {
            exportarExcel(datos, chooser.getSelectedFile().getAbsolutePath(), "Reporte por Usuario");
        }
    }

    private void exportarExcelPorUbicacion() {
        ItemCombo item = (ItemCombo) cbUbicaciones.getSelectedItem();

        if (item == null || item.getId() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una ubicación para exportar.");
            return;
        }

        List<Object[]> datos = reporteDAO.listarReportePorUbicacion(item.getId());

        if (datos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Esa ubicación no tiene registros.");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new java.io.File("reporte_ubicacion_" + item.getTexto().replace(" ", "_") + ".xlsx"));

        int opcion = chooser.showSaveDialog(this);

        if (opcion == JFileChooser.APPROVE_OPTION) {
            exportarExcel(datos, chooser.getSelectedFile().getAbsolutePath(), "Reporte por Ubicación");
        }
    }

    private void exportarExcel(List<Object[]> datos, String ruta, String nombreHoja) {
        if (!ruta.toLowerCase().endsWith(".xlsx")) {
            ruta += ".xlsx";
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(nombreHoja);

            String[] columnas = {
                    "ID Registro", "ID Usuario", "Trabajador", "ID Ubicación",
                    "Ubicación", "Residuo", "Cantidad", "Unidad", "Fecha", "Observaciones"
            };

            Row header = sheet.createRow(0);
            for (int i = 0; i < columnas.length; i++) {
                header.createCell(i).setCellValue(columnas[i]);
            }

            int filaNum = 1;
            for (Object[] filaDatos : datos) {
                Row fila = sheet.createRow(filaNum++);

                fila.createCell(0).setCellValue(Integer.parseInt(filaDatos[0].toString()));
                fila.createCell(1).setCellValue(Integer.parseInt(filaDatos[1].toString()));
                fila.createCell(2).setCellValue(filaDatos[2] != null ? filaDatos[2].toString() : "");
                fila.createCell(3).setCellValue(Integer.parseInt(filaDatos[3].toString()));
                fila.createCell(4).setCellValue(filaDatos[4] != null ? filaDatos[4].toString() : "");
                fila.createCell(5).setCellValue(filaDatos[5] != null ? filaDatos[5].toString() : "");
                fila.createCell(6).setCellValue(Double.parseDouble(filaDatos[6].toString()));
                fila.createCell(7).setCellValue(filaDatos[7] != null ? filaDatos[7].toString() : "");

                Object fechaObj = filaDatos[8];
                if (fechaObj instanceof Timestamp timestamp) {
                    fila.createCell(8).setCellValue(timestamp.toString());
                } else {
                    fila.createCell(8).setCellValue(fechaObj != null ? fechaObj.toString() : "");
                }

                fila.createCell(9).setCellValue(filaDatos[9] != null ? filaDatos[9].toString() : "");
            }

            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream out = new FileOutputStream(ruta)) {
                workbook.write(out);
            }

            JOptionPane.showMessageDialog(this, "Excel generado correctamente.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al generar Excel: " + e.getMessage());
            System.out.println("Error exportarExcel: " + e.getMessage());
        }
    }

    private static class ItemCombo {
        private final int id;
        private final String texto;

        public ItemCombo(int id, String texto) {
            this.id = id;
            this.texto = texto;
        }

        public int getId() {
            return id;
        }

        public String getTexto() {
            return texto;
        }

        @Override
        public String toString() {
            return texto;
        }
    }
}