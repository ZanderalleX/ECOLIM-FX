package vista;

import dao.EstadisticaDAO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelEstadisticas extends JPanel {

    private final EstadisticaDAO estadisticaDAO = new EstadisticaDAO();

    public PanelEstadisticas() {
        initComponents();
    }

    private void initComponents() {

        setLayout(new BorderLayout());
        setBackground(new Color(245,245,245));

        JLabel titulo = new JLabel("Estadísticas", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
        add(titulo, BorderLayout.NORTH);

        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        contenedor.setBackground(new Color(245,245,245));
        contenedor.setBorder(BorderFactory.createEmptyBorder(10,15,15,15));

        // TITULO GENERAL
        JLabel lblGeneral = new JLabel("Estadísticas Generales");
        lblGeneral.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblGeneral.setAlignmentX(Component.CENTER_ALIGNMENT);

        // PANEL GENERAL (VERTICAL)
        JPanel panelGeneral = new JPanel();
        panelGeneral.setLayout(new BoxLayout(panelGeneral, BoxLayout.Y_AXIS));
        panelGeneral.setBackground(new Color(245,245,245));

        panelGeneral.add(crearGraficaTrabajadoresGeneral());
        panelGeneral.add(Box.createVerticalStrut(20));
        panelGeneral.add(crearGraficaResiduosGeneral());

        String periodo = estadisticaDAO.obtenerEtiquetaPeriodoMensual();

        // TITULO MENSUAL
        JLabel lblMensual = new JLabel("Estadísticas Mensuales - " + periodo);
        lblMensual.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblMensual.setAlignmentX(Component.CENTER_ALIGNMENT);

        // PANEL MENSUAL (VERTICAL)
        JPanel panelMensual = new JPanel();
        panelMensual.setLayout(new BoxLayout(panelMensual, BoxLayout.Y_AXIS));
        panelMensual.setBackground(new Color(245,245,245));

        panelMensual.add(crearGraficaTrabajadoresMensual());
        panelMensual.add(Box.createVerticalStrut(20));
        panelMensual.add(crearGraficaResiduosMensual());

        contenedor.add(lblGeneral);
        contenedor.add(Box.createVerticalStrut(15));
        contenedor.add(panelGeneral);
        contenedor.add(Box.createVerticalStrut(25));
        contenedor.add(lblMensual);
        contenedor.add(Box.createVerticalStrut(15));
        contenedor.add(panelMensual);

        JScrollPane scroll = new JScrollPane(contenedor);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(scroll, BorderLayout.CENTER);
    }

    private JPanel crearGraficaTrabajadoresGeneral() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<Object[]> datos = estadisticaDAO.totalPorUsuarioGeneral();

        for (Object[] fila : datos) {
            String trabajador = fila[0].toString();
            double total = Double.parseDouble(fila[1].toString());
            dataset.addValue(total, "Kg recolectados", trabajador);
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Trabajador que más recolectó - General",
                "Trabajador",
                "Kg",
                dataset
        );

        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(900,300));
        return panel;
    }

    private JPanel crearGraficaResiduosGeneral() {

        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        List<Object[]> datos = estadisticaDAO.totalPorResiduoGeneral();

        for (Object[] fila : datos) {
            String residuo = fila[0].toString();
            double total = Double.parseDouble(fila[1].toString());
            dataset.setValue(residuo, total);
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Residuo más recolectado - General",
                dataset,
                true,
                true,
                false
        );

        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(900,300));
        return panel;
    }

    private JPanel crearGraficaTrabajadoresMensual() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<Object[]> datos = estadisticaDAO.totalPorUsuarioMensual();

        for (Object[] fila : datos) {
            String trabajador = fila[0].toString();
            double total = Double.parseDouble(fila[1].toString());
            dataset.addValue(total, "Kg recolectados", trabajador);
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Trabajador que más recolectó - Mensual",
                "Trabajador",
                "Kg",
                dataset
        );

        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(900,300));
        return panel;
    }

    private JPanel crearGraficaResiduosMensual() {

        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        List<Object[]> datos = estadisticaDAO.totalPorResiduoMensual();

        for (Object[] fila : datos) {
            String residuo = fila[0].toString();
            double total = Double.parseDouble(fila[1].toString());
            dataset.setValue(residuo, total);
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Residuo más recolectado - Mensual",
                dataset,
                true,
                true,
                false
        );

        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(900,300));
        return panel;
    }
}