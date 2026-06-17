package vista;

import dao.UsuarioDAO;
import modelo.Usuario;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginApp extends Application {

    @Override
    public void start(Stage stage) {

        HBox root = new HBox();

        // PANEL IZQUIERDO (BANNER)
        StackPane bannerPane = new StackPane();
        bannerPane.getStyleClass().add("banner-pane");

        Image image = new Image(
                getClass()
                        .getResource("/resources/images/planta.jpg")
                        .toExternalForm()
        );

        BackgroundImage bgImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(
                        100,
                        100,
                        true,
                        true,
                        false, // contain
                        true
                )
        );

        bannerPane.setBackground(new Background(bgImage));

        VBox overlay = new VBox(20);
        overlay.setAlignment(Pos.CENTER_LEFT);
        overlay.setPadding(new Insets(120));

        Label logo = new Label("BIENVENIDO A");
        logo.getStyleClass().add("logo");

        Label title = new Label("ECOLIM");
        title.getStyleClass().add("banner-title");

        Label subtitle = new Label(
                "Sistema de gestión ambiental y\nrecolección inteligente de residuos."
        );

        subtitle.getStyleClass().add("banner-subtitle");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Label footer = new Label("© 2026 ECOLIM. Todos los derechos reservados.");

        footer.getStyleClass().add("footer-text");

        overlay.getChildren().addAll(
                logo,
                title,
                subtitle,
                spacer,
                footer
        );

        bannerPane.getChildren().add(overlay);

        // PANEL DERECHO (LOGIN)
        VBox loginPane = new VBox(20);

        loginPane.getStyleClass().add("login-pane");

        loginPane.setAlignment(Pos.CENTER_LEFT);

        loginPane.setPadding(new Insets(70));

        Label appName = new Label("ADMINISTRADOR");
        appName.getStyleClass().add("app-name");

        Label welcome = new Label("INICIAR SESIÓN");
        welcome.getStyleClass().add("welcome");

        Label descripcion = new Label(
                "Ingrese sus credenciales:"
        );
        descripcion.getStyleClass().add("descripcion");

        TextField txtCorreo = new TextField();
        txtCorreo.setPromptText("Correo electrónico");

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Contraseña");

        //BOTON LOGIN
        Button btnLogin = new Button("Iniciar Sesión");
        btnLogin.getStyleClass().add("login-button");

        btnLogin.setOnAction(e -> {

            String correo = txtCorreo.getText().trim();
            String password = txtPassword.getText().trim();

            if (correo.isEmpty() || password.isEmpty()) {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Campos requeridos");
                alert.setHeaderText(null);
                alert.setContentText("Ingrese correo y contraseña.");
                alert.showAndWait();

                return;
            }

            UsuarioDAO usuarioDAO = new UsuarioDAO();

            Usuario admin = usuarioDAO.loginAdmin(correo, password);

            if (admin != null) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Bienvenido");
                alert.setHeaderText(null);
                alert.setContentText(
                        "Bienvenido "
                        + admin.getNombre()
                        + " "
                        + admin.getApellido()
                );
                alert.showAndWait();

                // Abrir menú principal
                MenuPrincipal menu = new MenuPrincipal(admin);
                menu.show();

                stage.close();

            } else {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Acceso denegado");
                alert.setHeaderText(null);
                alert.setContentText(
                        "Credenciales incorrectas o no tiene rol administrador."
                );
                alert.showAndWait();
            }
        });
        
        //FIN NOTON LOGIN

        Button btnExit = new Button("Salir");
        btnExit.getStyleClass().add("exit-button");
        btnExit.setOnAction(e -> stage.close());
        
        Hyperlink forgot = new Hyperlink("¿Olvidó su contraseña?");

        loginPane.getChildren().addAll(
                appName,
                welcome,
                descripcion,
                txtCorreo,
                txtPassword,
                btnLogin,
                btnExit,
                forgot
        );

        bannerPane.setPrefWidth(850);
        loginPane.setPrefWidth(450);

        root.getChildren().addAll(
                bannerPane,
                loginPane
        );

        Scene scene = new Scene(root, 1300, 800);

        scene.getStylesheets().add(
                getClass()
                        .getResource("login.css")
                        .toExternalForm()
        );

        stage.setTitle("ECOLIM");

        Image icono = new Image(
                getClass()
                        .getResourceAsStream("/resources/images/logoprincipal.png")
        );

        stage.getIcons().add(icono);

        stage.setScene(scene);

        stage.setResizable(false);

        stage.setMinWidth(1300);
        stage.setMaxWidth(1300);

        stage.setMinHeight(800);
        stage.setMaxHeight(800);

        stage.centerOnScreen();

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
