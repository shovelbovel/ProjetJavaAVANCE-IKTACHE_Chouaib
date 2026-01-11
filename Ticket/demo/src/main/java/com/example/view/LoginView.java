package com.example.view;

import com.example.MainApp;
import com.example.entity.User;
import com.example.service.UserService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LoginView {

    private final UserService userService = new UserService();
    private VBox view;
    private TextField emailField;
    private PasswordField passwordField;
    private Label messageLabel;

    public LoginView() {
        createView();
    }

    private void createView() {
        view = new VBox(20);
        view.setAlignment(Pos.CENTER);
        view.setPadding(new Insets(40));
        view.getStyleClass().add("login-container");

        // Logo/Title
        Label titleLabel = new Label("⚽ World Cup 2026");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.WHITE);

        Label subtitleLabel = new Label("Ticket Management System");
        subtitleLabel.setFont(Font.font("Arial", 14));
        subtitleLabel.setTextFill(Color.web("#cccccc"));

        // Form container
        VBox formBox = new VBox(15);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(30));
        formBox.setMaxWidth(350);
        formBox.getStyleClass().add("form-container");

        Label loginTitle = new Label("Sign In");
        loginTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        loginTitle.setTextFill(Color.web("#333"));

        // Email field
        emailField = new TextField();
        emailField.setPromptText("📧 Email Address");
        emailField.getStyleClass().add("text-field-styled");
        emailField.setPrefHeight(45);

        // Password field
        passwordField = new PasswordField();
        passwordField.setPromptText("🔒 Password");
        passwordField.getStyleClass().add("text-field-styled");
        passwordField.setPrefHeight(45);

        // Login button
        Button loginBtn = new Button("Sign In");
        loginBtn.getStyleClass().add("btn-primary");
        loginBtn.setPrefWidth(200);
        loginBtn.setPrefHeight(45);
        loginBtn.setOnAction(e -> handleLogin());

        // Register link
        Hyperlink registerLink = new Hyperlink("Don't have an account? Register here");
        registerLink.setOnAction(e -> showRegisterView());

        // Message label
        messageLabel = new Label();
        messageLabel.setTextFill(Color.RED);

        // Demo credentials
        VBox demoBox = new VBox(5);
        demoBox.setAlignment(Pos.CENTER);
        demoBox.setPadding(new Insets(15));
        demoBox.setStyle("-fx-background-color: #fff3cd; -fx-background-radius: 8;");
        
        Label demoTitle = new Label("ℹ️ Demo Credentials");
        demoTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        Label demoText = new Label("Admin: admin@worldcup.com / admin123");
        demoText.setFont(Font.font("Arial", 11));
        demoBox.getChildren().addAll(demoTitle, demoText);

        formBox.getChildren().addAll(loginTitle, emailField, passwordField, loginBtn, messageLabel, registerLink, demoBox);

        view.getChildren().addAll(titleLabel, subtitleLabel, formBox);
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please fill in all fields");
            return;
        }

        try {
            var userOpt = userService.authenticate(email, password);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                MainApp.setCurrentUser(user);
                
                if (user.getRole() == User.Role.ADMIN) {
                    showAdminView();
                } else {
                    showClientView();
                }
            } else {
                messageLabel.setText("Invalid email or password");
            }
        } catch (Exception e) {
            messageLabel.setText("Login error: " + e.getMessage());
        }
    }

    private void showAdminView() {
        AdminView adminView = new AdminView();
        Scene scene = new Scene(adminView.getView(), 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
        MainApp.getPrimaryStage().setScene(scene);
    }

    private void showClientView() {
        ClientView clientView = new ClientView();
        Scene scene = new Scene(clientView.getView(), 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
        MainApp.getPrimaryStage().setScene(scene);
    }

    private void showRegisterView() {
        RegisterView registerView = new RegisterView();
        Scene scene = new Scene(registerView.getView(), 450, 650);
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
        MainApp.getPrimaryStage().setScene(scene);
    }

    public VBox getView() {
        return view;
    }
}
