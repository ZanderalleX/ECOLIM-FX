package vista;

import dao.UsuarioDAO;
import modelo.Usuario;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;

public class LoginAdmin extends JFrame {

    private JTextField txtCorreo;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnSalir;

    public LoginAdmin() {

        initComponents();

        setTitle("ECOLIM");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        URL iconUrl = getClass().getResource("/imagenes/logoprincipal.png");

        if (iconUrl != null) {
            setIconImage(new ImageIcon(iconUrl).getImage());
        }
    }

    private void initComponents() {

        setLayout(new GridLayout(1, 2));

        PanelImagen panelIzquierdo = new PanelImagen();
        panelIzquierdo.setLayout(null);

        JPanel panelDerecho = new JPanel(null);
        panelDerecho.setBackground(Color.WHITE);

        add(panelIzquierdo);
        add(panelDerecho);

        JLabel lblBienvenido = new JLabel("BIENVENIDO");
        lblBienvenido.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblBienvenido.setForeground(Color.WHITE);
        lblBienvenido.setBounds(60, 180, 300, 50);
        panelIzquierdo.add(lblBienvenido);

        JLabel lblTexto = new JLabel("Sistema de Gestión de Residuos");
        lblTexto.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblTexto.setForeground(Color.WHITE);
        lblTexto.setBounds(60, 230, 300, 30);
        panelIzquierdo.add(lblTexto);

        URL logoUrl = getClass().getResource("/imagenes/logoprincipal.png");

        if (logoUrl != null) {

            ImageIcon icon = new ImageIcon(logoUrl);

            Image img = icon.getImage().getScaledInstance(
                    80,
                    80,
                    Image.SCALE_SMOOTH
            );

            JLabel lblLogo = new JLabel(new ImageIcon(img));
            lblLogo.setBounds(180, 50, 80, 80);
            panelDerecho.add(lblLogo);
        }

        JLabel lblLogin = new JLabel("Administrador");
        lblLogin.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblLogin.setBounds(100, 150, 250, 40);
        panelDerecho.add(lblLogin);

        JLabel lblCorreo = new JLabel("Correo");
        lblCorreo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblCorreo.setForeground(Color.GRAY);
        lblCorreo.setBounds(80, 220, 250, 20);
        panelDerecho.add(lblCorreo);

        txtCorreo = new JTextField();
        txtCorreo.setBounds(80, 245, 250, 35);
        txtCorreo.setBorder(new MatteBorder(
                0,
                0,
                1,
                0,
                new Color(180, 180, 180)
        ));
        txtCorreo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        panelDerecho.add(txtCorreo);

        JLabel lblPassword = new JLabel("Contraseña");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPassword.setForeground(Color.GRAY);
        lblPassword.setBounds(80, 300, 250, 20);
        panelDerecho.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(80, 325, 250, 35);
        txtPassword.setBorder(new MatteBorder(
                0,
                0,
                1,
                0,
                new Color(180, 180, 180)
        ));
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        panelDerecho.add(txtPassword);

        btnLogin = new JButton("Ingresar");

        btnLogin.setBounds(80, 395, 250, 45);
        btnLogin.setBackground(new Color(18, 73, 40));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLogin.addActionListener(this::loginAdmin);

        panelDerecho.add(btnLogin);

        btnSalir = new JButton("Salir");

        btnSalir.setBounds(80, 450, 250, 35);
        btnSalir.setBackground(Color.WHITE);
        btnSalir.setForeground(Color.GRAY);
        btnSalir.setBorder(BorderFactory.createEmptyBorder());
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnSalir.addActionListener(e -> System.exit(0));

        panelDerecho.add(btnSalir);
    }

    private void loginAdmin(ActionEvent evt) {

        String correo = txtCorreo.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (correo.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese correo y contraseña."
            );

            return;
        }

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        Usuario admin = usuarioDAO.loginAdmin(
                correo,
                password
        );

        if (admin != null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Bienvenido "
                    + admin.getNombre()
                    + " "
                    + admin.getApellido()
            );

            new MenuPrincipal(admin).setVisible(true);
            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Credenciales incorrectas o no tiene rol admin."
            );
        }
    }

    private class PanelImagen extends JPanel {

        private Image imagen;

        public PanelImagen() {

            URL url = getClass().getResource(
                    "/imagenes/fondo_login.jpg"
            );

            if (url != null) {
                imagen = new ImageIcon(url).getImage();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            if (imagen != null) {

                g.drawImage(
                        imagen,
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        this
                );

                g.setColor(new Color(0, 0, 0, 70));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}
