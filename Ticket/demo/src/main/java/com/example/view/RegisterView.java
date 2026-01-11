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

public class RegisterView {

    private final UserService userService = new UserService();
    private VBox view;
    private TextField nameField;
    private TextField emailField;
    private PasswordField passwordField;
    private Label messageLabel;

    public RegisterView() {
        createView();
    }

    private void createView() {
        view = new VBox(20);
        view.setAlignment(Pos.CENTER);
        view.setPadding(new Insets(40));
        view.getStyleClass().add("register-container");

        // Logo/Title
        Label titleLabel = new Label("⚽ World Cup 2026");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.WHITE);

        Label subtitleLabel = new Label("Create Your Account");
        subtitleLabel.setFont(Font.font("Arial", 14));
        subtitleLabel.setTextFill(Color.web("#cccccc"));

        // Form container
        VBox formBox = new VBox(15);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(30));
        formBox.setMaxWidth(350);
        formBox.getStyleClass().add("form-container");

        Label registerTitle = new Label("Register");
        registerTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        registerTitle.setTextFill(Color.web("#333"));

        // Name field
        nameField = new TextField();
        nameField.setPromptText("👤 Full Name");
        nameField.getStyleClass().add("text-field-styled");
        nameField.setPrefHeight(45);

        // Email field
        emailField = new TextField();
        emailField.setPromptText("📧 Email Address");
        emailField.getStyleClass().add("text-field-styled");
        emailField.setPrefHeight(45);

        // Password field
        passwordField = new PasswordField();
        passwordField.setPromptText("🔒 Password (min 6 characters)");
        passwordField.getStyleClass().add("text-field-styled");
        passwordField.setPrefHeight(45);

        // Register button
        Button registerBtn = new Button("Create Account");
        registerBtn.getStyleClass().add("btn-success");
        registerBtn.setPrefWidth(200);
        registerBtn.setPrefHeight(45);
        registerBtn.setOnAction(e -> handleRegister());

        // Login link
        Hyperlink loginLink = new Hyperlink("Already have an account? Sign in");
        loginLink.setOnAction(e -> showLoginView());

        // Message label
        messageLabel = new Label();
        messageLabel.setTextFill(Color.RED);

        // Features
        HBox featuresBox = new HBox(20);
        featuresBox.setAlignment(Pos.CENTER);
        featuresBox.getChildren().addAll(
            createFeatureLabel("✓ Easy Booking"),
            createFeatureLabel("✓ Secure"),
            createFeatureLabel("✓ VIP Access")
        );

        formBox.getChildren().addAll(registerTitle, nameField, emailField, passwordField, 
                                     registerBtn, messageLabel, loginLink, featuresBox);

        view.getChildren().addAll(titleLabel, subtitleLabel, formBox);
    }

    private Label createFeatureLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", 11));
        label.setTextFill(Color.web("#28a745"));
        return label;
    }

    private void handleRegister() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please fill in all fields");
            messageLabel.setTextFill(Color.RED);
            return;
        }

        if (password.length() < 6) {
            messageLabel.setText("Password must be at least 6 characters");
            messageLabel.setTextFill(Color.RED);
            return;
        }

        try {
            userService.createUser(name, email, password, User.Role.CLIENT);
            messageLabel.setText("Registration successful! Redirecting...");
            messageLabel.setTextFill(Color.GREEN);
            
            // Auto-login after registration
            var userOpt = userService.authenticate(email, password);
            if (userOpt.isPresent()) {
                MainApp.setCurrentUser(userOpt.get());
                showClientView();
            }
        } catch (IllegalArgumentException e) {
            messageLabel.setText(e.getMessage());
            messageLabel.setTextFill(Color.RED);
        } catch (Exception e) {
            messageLabel.setText("Registration error: " + e.getMessage());
            messageLabel.setTextFill(Color.RED);
        }
    }

    private void showLoginView() {
        MainApp.showLoginView();
    }

    private void showClientView() {
        ClientView clientView = new ClientView();
        Scene scene = new Scene(clientView.getView(), 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
        MainApp.getPrimaryStage().setScene(scene);
    }

    public VBox getView() {
        return view;
    }
}
