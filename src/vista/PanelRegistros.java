package vista;

import dao.RegistroDAO;
import dao.UsuarioDAO;
import dao.UbicacionDAO;

import modelo.RegistroRecoleccion;
import modelo.Usuario;
import modelo.Ubicacion;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class PanelRegistros extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;

    private JTextField txtBuscar;
    private JComboBox<String> cbFiltro;

    private final RegistroDAO registroDAO = new RegistroDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final UbicacionDAO ubicacionDAO = new UbicacionDAO();

    private final Usuario usuarioActivo;

    // CACHE
    private final Map<Integer, String> usuariosMap = new HashMap<>();
    private final Map<Integer, String> ubicacionesMap = new HashMap<>();

    private final Stack<RegistroRecoleccion> historialEliminados
            = new Stack<>();

    // FORMATO FECHA
    private final DateTimeFormatter formatoFecha
            = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PanelRegistros(Usuario usuarioActivo) {

        this.usuarioActivo = usuarioActivo;

        cargarUsuariosCache();
        cargarUbicacionesCache();

        initComponents();
        cargarRegistros();
    }

    private void cargarUsuariosCache() {

        usuariosMap.clear();

        List<Usuario> lista = usuarioDAO.listarUsuarios();

        for (Usuario u : lista) {

            usuariosMap.put(
                    u.getIdUsuario(),
                    u.getNombre() + " " + u.getApellido()
            );
        }
    }

    private void cargarUbicacionesCache() {

        ubicacionesMap.clear();

        List<Ubicacion> lista = ubicacionDAO.listarUbicaciones();

        for (Ubicacion u : lista) {

            ubicacionesMap.put(
                    u.getIdUbicacion(),
                    u.getNombreLugar()
            );
        }
    }

    private void initComponents() {

        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(new Color(245, 247, 250));
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));

        add(panelPrincipal, BorderLayout.CENTER);

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBackground(new Color(245, 247, 250));

        JLabel titulo = new JLabel("Gestión de Residuos");

        titulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titulo.setForeground(new Color(30, 30, 30));

        JLabel subtitulo = new JLabel(
                "Administración de registros de recolección"
        );

        subtitulo.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        subtitulo.setForeground(Color.GRAY);

        panelSuperior.add(titulo);
        panelSuperior.add(Box.createVerticalStrut(5));
        panelSuperior.add(subtitulo);
        panelSuperior.add(Box.createVerticalStrut(20));

        JPanel panelBusqueda = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT,
                        12,
                        0
                )
        );

        panelBusqueda.setBackground(
                new Color(245, 247, 250)
        );

        cbFiltro = new JComboBox<>(
                new String[]{
                    "ID",
                    "Usuario",
                    "Ubicación",
                    "Residuo",
                    "Unidad"
                }
        );

        cbFiltro.setPreferredSize(
                new Dimension(170, 38)
        );

        cbFiltro.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        txtBuscar = new JTextField();

        txtBuscar.setPreferredSize(
                new Dimension(280, 38)
        );

        txtBuscar.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        panelBusqueda.add(
                new JLabel("Buscar:")
        );

        panelBusqueda.add(cbFiltro);
        panelBusqueda.add(txtBuscar);

        panelSuperior.add(panelBusqueda);

        panelPrincipal.add(
                panelSuperior,
                BorderLayout.NORTH
        );

        modelo = new DefaultTableModel(
                new Object[]{
                    "ID",
                    "Usuario",
                    "Ubicación",
                    "Residuo",
                    "Cantidad",
                    "Unidad",
                    "Fecha",
                    "Observaciones"
                },
                0
        ) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {
                return false;
            }
        };

        tabla = new JTable(modelo);

        tabla.setRowHeight(36);

        tabla.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        tabla.setGridColor(
                new Color(235, 235, 235)
        );

        tabla.setShowVerticalLines(false);

        tabla.setSelectionBackground(
                new Color(52, 120, 246)
        );

        tabla.setSelectionForeground(
                Color.WHITE
        );

        tabla.getTableHeader().setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        tabla.getTableHeader().setBackground(
                new Color(18, 33, 61)
        );

        tabla.getTableHeader().setForeground(
                Color.WHITE
        );

        tabla.getTableHeader()
                .setReorderingAllowed(false);

        JScrollPane scroll = new JScrollPane(tabla);

        scroll.setBorder(
                BorderFactory.createEmptyBorder()
        );

        JPanel cardTabla = new JPanel(
                new BorderLayout()
        );

        cardTabla.setBackground(Color.WHITE);

        cardTabla.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                new Color(225, 225, 225),
                                1,
                                true
                        ),
                        new EmptyBorder(
                                15,
                                15,
                                15,
                                15
                        )
                )
        );

        cardTabla.add(
                scroll,
                BorderLayout.CENTER
        );

        panelPrincipal.add(
                cardTabla,
                BorderLayout.CENTER
        );

        JPanel panelBotones = new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT,
                        10,
                        15
                )
        );

        panelBotones.setBackground(
                new Color(245, 247, 250)
        );

        JButton btnAgregar = crearBoton(
                "Agregar",
                new Color(46, 125, 50)
        );

        JButton btnModificar = crearBoton(
                "Modificar",
                new Color(25, 118, 210)
        );

        JButton btnEliminar = crearBoton(
                "Eliminar",
                new Color(198, 40, 40)
        );

        JButton btnDeshacer = crearBoton(
                "Deshacer",
                new Color(255, 143, 0)
        );

        btnAgregar.addActionListener(
                e -> agregarRegistro()
        );

        btnModificar.addActionListener(
                e -> modificarRegistro()
        );

        btnEliminar.addActionListener(
                e -> eliminarRegistro()
        );

        btnDeshacer.addActionListener(
                e -> deshacerEliminacion()
        );

        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnDeshacer);

        panelPrincipal.add(
                panelBotones,
                BorderLayout.SOUTH
        );

        txtBuscar.getDocument()
                .addDocumentListener(
                        new DocumentListener() {

                    @Override
                    public void insertUpdate(
                            DocumentEvent e
                    ) {
                        buscarRegistros();
                    }

                    @Override
                    public void removeUpdate(
                            DocumentEvent e
                    ) {
                        buscarRegistros();
                    }

                    @Override
                    public void changedUpdate(
                            DocumentEvent e
                    ) {
                        buscarRegistros();
                    }
                });

        cbFiltro.addActionListener(
                e -> buscarRegistros()
        );
    }

    private JButton crearBoton(
            String texto,
            Color color
    ) {

        JButton btn = new JButton(texto);

        btn.setPreferredSize(
                new Dimension(130, 40)
        );

        btn.setBackground(color);
        btn.setForeground(Color.WHITE);

        btn.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        btn.setFocusPainted(false);
        btn.setBorderPainted(false);

        btn.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        return btn;
    }

    private void cargarRegistros() {

        modelo.setRowCount(0);

        List<RegistroRecoleccion> lista
                = registroDAO.listarRegistros();

        for (RegistroRecoleccion r : lista) {

            modelo.addRow(new Object[]{
                r.getIdRegistro(),
                obtenerNombreUsuario(r.getIdUsuario()),
                obtenerNombreUbicacion(r.getIdUbicacion()),
                obtenerTipoResiduo(r.getIdResiduo()),
                r.getCantidad(),
                r.getUnidad(),
                formatearFecha(r.getFecha()),
                r.getObservaciones()
            });
        }
    }

    private void buscarRegistros() {

        String texto = txtBuscar.getText()
                .trim()
                .toLowerCase();

        if (texto.isEmpty()) {

            cargarRegistros();
            return;
        }

        modelo.setRowCount(0);

        List<RegistroRecoleccion> lista
                = registroDAO.listarRegistros();

        String filtro
                = cbFiltro.getSelectedItem().toString();

        for (RegistroRecoleccion r : lista) {

            String nombreUsuario
                    = obtenerNombreUsuario(r.getIdUsuario());

            String nombreUbicacion
                    = obtenerNombreUbicacion(r.getIdUbicacion());

            String tipoResiduo
                    = obtenerTipoResiduo(r.getIdResiduo());

            boolean coincide = false;

            switch (filtro) {

                case "ID":

                    coincide = String.valueOf(
                            r.getIdRegistro()
                    ).contains(texto);

                    break;

                case "Usuario":

                    coincide = nombreUsuario
                            .toLowerCase()
                            .contains(texto);

                    break;

                case "Ubicación":

                    coincide = nombreUbicacion
                            .toLowerCase()
                            .contains(texto);

                    break;

                case "Residuo":

                    coincide = tipoResiduo
                            .toLowerCase()
                            .contains(texto);

                    break;

                case "Unidad":

                    coincide = r.getUnidad()
                            .toLowerCase()
                            .contains(texto);

                    break;
            }

            if (coincide) {

                modelo.addRow(new Object[]{
                    r.getIdRegistro(),
                    nombreUsuario,
                    nombreUbicacion,
                    tipoResiduo,
                    r.getCantidad(),
                    r.getUnidad(),
                    formatearFecha(r.getFecha()),
                    r.getObservaciones()
                });
            }
        }
    }

    private String formatearFecha(LocalDateTime fecha) {

        if (fecha == null) {
            return "";
        }

        return fecha.format(formatoFecha);
    }

    private String obtenerTipoResiduo(int idResiduo) {

        switch (idResiduo) {

            case 1:
                return "Sólido";

            case 2:
                return "Líquido";

            case 3:
                return "Gaseoso";

            case 4:
                return "Metálico";

            default:
                return "Desconocido";
        }
    }

    private int obtenerIdResiduo(String tipo) {

        switch (tipo) {

            case "Sólido":
                return 1;

            case "Líquido":
                return 2;

            case "Gaseoso":
                return 3;

            case "Metálico":
                return 4;

            default:
                return 0;
        }
    }

    private String obtenerNombreUsuario(int idUsuario) {

        return usuariosMap.getOrDefault(
                idUsuario,
                "Desconocido"
        );
    }

    private String obtenerNombreUbicacion(int idUbicacion) {

        return ubicacionesMap.getOrDefault(
                idUbicacion,
                "Desconocida"
        );
    }

    private int obtenerIdUbicacion(String nombre) {

        for (Map.Entry<Integer, String> entry
                : ubicacionesMap.entrySet()) {

            if (entry.getValue().equals(nombre)) {

                return entry.getKey();
            }
        }

        return 0;
    }

    private void agregarRegistro() {

        JComboBox<String> cbUbicacion
                = new JComboBox<>();

        for (String nombre : ubicacionesMap.values()) {

            cbUbicacion.addItem(nombre);
        }

        JComboBox<String> cbResiduo
                = new JComboBox<>(new String[]{
            "Sólido",
            "Líquido",
            "Gaseoso",
            "Metálico"
        });

        JTextField txtCantidad = new JTextField();
        
        JTextField txtUnidad = new JTextField();
        
        JTextField txtObservaciones = new JTextField();

        Object[] campos = {
            "Ubicación:", cbUbicacion,
            "Residuo:", cbResiduo,
            "Cantidad:", txtCantidad,
            "Unidad:", txtUnidad,
            "Observaciones:", txtObservaciones
        };

        int op = JOptionPane.showConfirmDialog(
                this,
                campos,
                "Agregar Registro",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (op == JOptionPane.OK_OPTION) {

            try {

                RegistroRecoleccion r
                        = new RegistroRecoleccion();

                r.setIdUsuario(
                        usuarioActivo.getIdUsuario()
                );

                r.setIdUbicacion(
                        obtenerIdUbicacion(
                                cbUbicacion
                                        .getSelectedItem()
                                        .toString()
                        )
                );

                r.setIdResiduo(
                        obtenerIdResiduo(
                                cbResiduo
                                        .getSelectedItem()
                                        .toString()
                        )
                );

                r.setCantidad(
                        Double.parseDouble(
                                txtCantidad.getText().trim()
                        )
                );

                r.setUnidad(
                        txtUnidad.getText().trim()
                );

                r.setFecha(LocalDateTime.now());

                r.setObservaciones(
                        txtObservaciones.getText().trim()
                );

                if (registroDAO.insertarRegistro(r)) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Registro agregado correctamente."
                    );

                    cargarRegistros();

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "No se pudo agregar el registro."
                    );
                }

            } catch (Exception e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Datos inválidos. Qué inesperado."
                );
            }
        }
    }

    private void modificarRegistro() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un registro."
            );

            return;
        }

        int idRegistro
                = (int) modelo.getValueAt(fila, 0);

        RegistroRecoleccion r
                = registroDAO.buscarPorId(idRegistro);

        if (r == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se encontró el registro."
            );

            return;
        }

        JComboBox<String> cbUbicacion
                = new JComboBox<>();

        for (String nombre : ubicacionesMap.values()) {

            cbUbicacion.addItem(nombre);
        }

        cbUbicacion.setSelectedItem(
                obtenerNombreUbicacion(
                        r.getIdUbicacion()
                )
        );

        JComboBox<String> cbResiduo
                = new JComboBox<>(new String[]{
            "Sólido",
            "Líquido",
            "Gaseoso",
            "Metálico"
        });

        cbResiduo.setSelectedItem(
                obtenerTipoResiduo(
                        r.getIdResiduo()
                )
        );

        JTextField txtCantidad
                = new JTextField(
                        String.valueOf(r.getCantidad())
                );

        JTextField txtUnidad
                = new JTextField(r.getUnidad());

        JTextField txtObservaciones
                = new JTextField(r.getObservaciones());

        Object[] campos = {
            "Ubicación:", cbUbicacion,
            "Residuo:", cbResiduo,
            "Cantidad:", txtCantidad,
            "Unidad:", txtUnidad,
            "Observaciones:", txtObservaciones
        };

        int op = JOptionPane.showConfirmDialog(
                this,
                campos,
                "Modificar Registro",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (op == JOptionPane.OK_OPTION) {

            try {

                r.setIdUbicacion(
                        obtenerIdUbicacion(
                                cbUbicacion
                                        .getSelectedItem()
                                        .toString()
                        )
                );

                r.setIdResiduo(
                        obtenerIdResiduo(
                                cbResiduo
                                        .getSelectedItem()
                                        .toString()
                        )
                );

                r.setCantidad(
                        Double.parseDouble(
                                txtCantidad.getText().trim()
                        )
                );

                r.setUnidad(
                        txtUnidad.getText().trim()
                );

                r.setObservaciones(
                        txtObservaciones.getText().trim()
                );

                if (registroDAO.actualizarRegistro(r)) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Registro actualizado correctamente."
                    );

                    cargarRegistros();

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "No se pudo actualizar."
                    );
                }

            } catch (Exception e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Datos inválidos."
                );
            }
        }
    }

    private void eliminarRegistro() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un registro."
            );

            return;
        }

        int idRegistro
                = (int) modelo.getValueAt(fila, 0);

        int op = JOptionPane.showConfirmDialog(
                this,
                "¿Eliminar el registro seleccionado?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (op != JOptionPane.YES_OPTION) {
            return;
        }

        RegistroRecoleccion registro
                = registroDAO.buscarPorId(idRegistro);

        if (registro == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se encontró el registro."
            );

            return;
        }

        if (registroDAO.eliminarRegistro(idRegistro)) {

            historialEliminados.push(registro);

            JOptionPane.showMessageDialog(
                    this,
                    "Registro eliminado."
            );

            cargarRegistros();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo eliminar."
            );
        }
    }

    private void deshacerEliminacion() {

        if (historialEliminados.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No hay registros para restaurar."
            );

            return;
        }

        RegistroRecoleccion registro
                = historialEliminados.pop();

        if (registroDAO.restaurarRegistro(registro)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Registro restaurado correctamente."
            );

            cargarRegistros();

        } else {

            historialEliminados.push(registro);

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo restaurar el registro."
            );
        }
    }

}
