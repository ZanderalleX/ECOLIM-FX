package vista;

import modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuPrincipal extends JFrame {

    private JPanel panelMenu;
    private JPanel panelContenido;
    private JPanel topBar;

    private JButton btnUsuarios;
    private JButton btnRoles;
    private JButton btnPlantas;
    private JButton btnUbicaciones;
    private JButton btnReportes;
    private JButton btnEstadisticas;
    private JButton btnRegistros;
    private JButton btnCerrarSesion;

    private final Usuario admin;

    private final Color COLOR_MENU = new Color(18, 33, 61);
    private final Color COLOR_HOVER = new Color(40, 70, 150);

    public MenuPrincipal(Usuario admin) {

        this.admin = admin;

        initComponents();

        setTitle("ECOLIM");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        ImageIcon iconApp = new ImageIcon(
                getClass().getResource("/imagenes/logoprincipal.png")
        );

        setIconImage(iconApp.getImage());

        mostrarPanel(new PanelUsuarios());
    }

    private void initComponents() {

        setLayout(new BorderLayout());

        crearMenuLateral();
        crearAreaPrincipal();
        configurarEventos();
    }

    private void crearMenuLateral() {

        panelMenu = new JPanel(null);
        panelMenu.setPreferredSize(new Dimension(260, 0));
        panelMenu.setBackground(COLOR_MENU);

        add(panelMenu, BorderLayout.WEST);

        ImageIcon iconoLogo = new ImageIcon(
                getClass().getResource("/imagenes/logoprincipal.png")
        );

        Image img = iconoLogo.getImage().getScaledInstance(
                110,
                110,
                Image.SCALE_SMOOTH
        );

        JLabel lblLogo = new JLabel(new ImageIcon(img));
        lblLogo.setBounds(75, 20, 110, 110);

        panelMenu.add(lblLogo);

        JLabel lblTitulo = new JLabel("ECOLIM");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBounds(20, 135, 220, 35);

        panelMenu.add(lblTitulo);

        JLabel lblUsuario = new JLabel(
                "<html><center>"
                + admin.getNombre()
                + "<br>"
                + admin.getApellido()
                + "</center></html>"
        );

        lblUsuario.setForeground(new Color(210, 210, 210));
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblUsuario.setHorizontalAlignment(SwingConstants.CENTER);
        lblUsuario.setBounds(20, 175, 220, 50);

        panelMenu.add(lblUsuario);

        btnUsuarios = crearBoton("Usuarios", 260);
        btnRoles = crearBoton("Roles", 315);
        btnUbicaciones = crearBoton("Ubicaciones", 370);
        btnPlantas = crearBoton("Plantas", 425);
        btnRegistros = crearBoton("Residuos", 480);
        btnReportes = crearBoton("Reportes", 535);
        btnEstadisticas = crearBoton("Estadísticas", 590);
        btnCerrarSesion = crearBoton("Cerrar sesión", 700);

        panelMenu.add(btnUsuarios);
        panelMenu.add(btnRoles);
        panelMenu.add(btnUbicaciones);
        panelMenu.add(btnPlantas);
        panelMenu.add(btnRegistros);
        panelMenu.add(btnReportes);
        panelMenu.add(btnEstadisticas);
        panelMenu.add(btnCerrarSesion);
    }

    private void crearAreaPrincipal() {

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(new Color(245, 247, 250));

        add(contenedor, BorderLayout.CENTER);

        topBar = new JPanel(null);
        topBar.setPreferredSize(new Dimension(0, 75));
        topBar.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Panel Administrativo");

        lblTitulo.setFont(
                new Font("Segoe UI", Font.BOLD, 26)
        );

        lblTitulo.setBounds(
                25,
                18,
                350,
                35
        );

        topBar.add(lblTitulo);

        JLabel lblAdmin = new JLabel(
                admin.getNombre() + " " + admin.getApellido()
        );

        lblAdmin.setFont(
                new Font("Segoe UI", Font.PLAIN, 15)
        );

        lblAdmin.setBounds(
                1100,
                25,
                300,
                25
        );

        topBar.add(lblAdmin);

        contenedor.add(topBar, BorderLayout.NORTH);

        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBackground(new Color(245, 247, 250));

        contenedor.add(panelContenido, BorderLayout.CENTER);
    }

    private JButton crearBoton(String texto, int y) {

        JButton boton = new JButton(texto);

        boton.setBounds(15, y, 230, 45);

        boton.setBackground(COLOR_MENU);
        boton.setForeground(Color.WHITE);

        boton.setFont(
                new Font("Segoe UI", Font.PLAIN, 15)
        );

        boton.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        boton.setFocusPainted(false);
        boton.setBorderPainted(false);

        boton.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        boton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(COLOR_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(COLOR_MENU);
            }
        });

        return boton;
    }

    private void configurarEventos() {

        btnUsuarios.addActionListener(
                e -> mostrarPanel(new PanelUsuarios())
        );

        btnRoles.addActionListener(
                e -> mostrarPanel(new PanelRoles())
        );

        btnUbicaciones.addActionListener(
                e -> mostrarPanel(new PanelUbicaciones())
        );

        btnPlantas.addActionListener(
                e -> mostrarPanel(new PanelPlantas())
        );

        btnReportes.addActionListener(
                e -> mostrarPanel(new PanelReportes())
        );

        btnEstadisticas.addActionListener(
                e -> mostrarPanel(new PanelEstadisticas())
        );

        btnRegistros.addActionListener(
                e -> mostrarPanel(new PanelRegistros(admin))
        );

        btnCerrarSesion.addActionListener(e -> {

            new LoginAdmin().setVisible(true);

            dispose();
        });
    }

    private void mostrarPanel(JPanel panelNuevo) {

        JPanel wrapper = new JPanel(
                new BorderLayout()
        );

        wrapper.setBackground(
                new Color(245, 247, 250)
        );

        wrapper.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        wrapper.add(
                panelNuevo,
                BorderLayout.CENTER
        );

        panelContenido.removeAll();

        panelContenido.add(
                wrapper,
                BorderLayout.CENTER
        );

        panelContenido.revalidate();
        panelContenido.repaint();
    }
}