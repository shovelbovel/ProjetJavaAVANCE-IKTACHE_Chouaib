package com.example.view;

import com.example.MainApp;
import com.example.entity.*;
import com.example.service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SuppressWarnings("unchecked")
public class AdminView {

    private final StadiumService stadiumService = new StadiumService();
    private final MatchService matchService = new MatchService();
    private final TicketService ticketService = new TicketService();
    private final UserService userService = new UserService();
    private final ReservationService reservationService = new ReservationService();

    private BorderPane view;
    private VBox contentArea;

    public AdminView() {
        createView();
        showDashboard();
    }

    private void createView() {
        view = new BorderPane();
        view.getStyleClass().add("admin-view");

        HBox navbar = createNavbar();
        view.setTop(navbar);

        VBox sidebar = createSidebar();
        view.setLeft(sidebar);

        contentArea = new VBox(20);
        contentArea.setPadding(new Insets(20));
        contentArea.getStyleClass().add("content-area");

        ScrollPane scrollPane = new ScrollPane(contentArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f8f9fa;");
        view.setCenter(scrollPane);
    }

    private HBox createNavbar() {
        HBox navbar = new HBox();
        navbar.setAlignment(Pos.CENTER_LEFT);
        navbar.setPadding(new Insets(15, 30, 15, 30));
        navbar.setSpacing(20);
        navbar.setStyle("-fx-background-color: linear-gradient(to right, #8b0000, #a52a2a);");

        Label logo = new Label("⚽ World Cup 2026 - Admin Panel");
        logo.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        logo.setTextFill(Color.WHITE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label userLabel = new Label("👤 " + MainApp.getCurrentUser().getName() + " (Admin)");
        userLabel.setTextFill(Color.WHITE);

        Button logoutBtn = new Button("Logout");
        logoutBtn.getStyleClass().add("btn-danger");
        logoutBtn.setOnAction(e -> logout());

        navbar.getChildren().addAll(logo, spacer, userLabel, logoutBtn);
        return navbar;
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(20, 15, 20, 15));
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: #2c3e50;");

        Label menuTitle = new Label("MENU ADMIN");
        menuTitle.setTextFill(Color.LIGHTGRAY);
        menuTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        menuTitle.setPadding(new Insets(0, 0, 10, 0));

        Button dashboardBtn = createSidebarButton("📊 Dashboard");
        dashboardBtn.setOnAction(e -> showDashboard());

        Button usersBtn = createSidebarButton("👥 Users");
        usersBtn.setOnAction(e -> showUsers());

        Button stadiumsBtn = createSidebarButton("🏟️ Stadiums");
        stadiumsBtn.setOnAction(e -> showStadiums());

        Button matchesBtn = createSidebarButton("⚽ Matches");
        matchesBtn.setOnAction(e -> showMatches());

        Button ticketsBtn = createSidebarButton("🎫 Tickets");
        ticketsBtn.setOnAction(e -> showTickets());

        Button reservationsBtn = createSidebarButton("📋 All Reservations");
        reservationsBtn.setOnAction(e -> showAllReservations());

        Button statsBtn = createSidebarButton("📈 Statistics");
        statsBtn.setOnAction(e -> showStatistics());

        sidebar.getChildren().addAll(menuTitle, dashboardBtn, usersBtn, stadiumsBtn, 
            matchesBtn, ticketsBtn, reservationsBtn, statsBtn);
        return sidebar;
    }

    private Button createSidebarButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(190);
        btn.setPrefHeight(40);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.getStyleClass().add("sidebar-btn");
        return btn;
    }

    // ==================== DASHBOARD ====================
    private void showDashboard() {
        contentArea.getChildren().clear();

        Label title = new Label("📊 Dashboard Overview");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));

        // Stats cards
        HBox statsRow = new HBox(20);
        statsRow.setPadding(new Insets(20, 0, 20, 0));

        long totalUsers = userService.countUsers();
        long totalClients = userService.countClients();
        int totalStadiums = stadiumService.getAllStadiums().size();
        int totalMatches = matchService.getAllMatches().size();
        int totalTickets = ticketService.getAllTickets().size();
        long soldTickets = ticketService.getAllTickets().stream()
                .filter(t -> t.getStatus() == Ticket.Status.SOLD).count();
        long reservedTickets = ticketService.getAllTickets().stream()
                .filter(t -> t.getStatus() == Ticket.Status.RESERVED).count();
        long availableTickets = ticketService.getAllTickets().stream()
                .filter(t -> t.getStatus() == Ticket.Status.AVAILABLE).count();
        double totalRevenue = ticketService.getAllTickets().stream()
                .filter(t -> t.getStatus() == Ticket.Status.SOLD)
                .map(Ticket::getPrice)
                .mapToDouble(java.math.BigDecimal::doubleValue).sum();

        statsRow.getChildren().addAll(
            createStatCard("👥 Total Users", String.valueOf(totalUsers), "#3498db"),
            createStatCard("👤 Clients", String.valueOf(totalClients), "#9b59b6"),
            createStatCard("🏟️ Stadiums", String.valueOf(totalStadiums), "#1abc9c"),
            createStatCard("⚽ Matches", String.valueOf(totalMatches), "#e67e22")
        );

        HBox statsRow2 = new HBox(20);
        statsRow2.getChildren().addAll(
            createStatCard("🎫 Total Tickets", String.valueOf(totalTickets), "#34495e"),
            createStatCard("✅ Sold", String.valueOf(soldTickets), "#27ae60"),
            createStatCard("⏳ Reserved", String.valueOf(reservedTickets), "#f39c12"),
            createStatCard("📦 Available", String.valueOf(availableTickets), "#95a5a6")
        );

        VBox revenueCard = createRevenueCard(totalRevenue);

        // Quick actions
        Label actionsTitle = new Label("Quick Actions");
        actionsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        actionsTitle.setPadding(new Insets(20, 0, 10, 0));

        HBox quickActions = new HBox(15);
        Button addStadiumBtn = new Button("➕ Add Stadium");
        addStadiumBtn.getStyleClass().add("btn-primary");
        addStadiumBtn.setOnAction(e -> showAddStadiumDialog());

        Button addMatchBtn = new Button("➕ Add Match");
        addMatchBtn.getStyleClass().add("btn-success");
        addMatchBtn.setOnAction(e -> showAddMatchDialog());

        Button addTicketsBtn = new Button("➕ Generate Tickets");
        addTicketsBtn.getStyleClass().add("btn-warning");
        addTicketsBtn.setOnAction(e -> showGenerateTicketsDialog());

        quickActions.getChildren().addAll(addStadiumBtn, addMatchBtn, addTicketsBtn);

        contentArea.getChildren().addAll(title, statsRow, statsRow2, revenueCard, actionsTitle, quickActions);
    }

    private VBox createStatCard(String label, String value, String color) {
        VBox card = new VBox(5);
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(150);
        card.setPrefHeight(100);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 10;");

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        valueLabel.setTextFill(Color.WHITE);

        Label nameLabel = new Label(label);
        nameLabel.setFont(Font.font("Arial", 12));
        nameLabel.setTextFill(Color.WHITE);

        card.getChildren().addAll(valueLabel, nameLabel);
        return card;
    }

    private VBox createRevenueCard(double revenue) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: linear-gradient(to right, #11998e, #38ef7d); -fx-background-radius: 10;");

        Label titleLabel = new Label("💰 Total Revenue");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        titleLabel.setTextFill(Color.WHITE);

        Label valueLabel = new Label(String.format("$%.2f", revenue));
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        valueLabel.setTextFill(Color.WHITE);

        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }

    // ==================== USERS MANAGEMENT ====================
    private void showUsers() {
        contentArea.getChildren().clear();

        Label title = new Label("👥 User Management");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Button addUserBtn = new Button("➕ Add User");
        addUserBtn.getStyleClass().add("btn-primary");
        addUserBtn.setOnAction(e -> showAddUserDialog());

        TableView<User> userTable = new TableView<>();
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        TableColumn<User, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<User, User.Role> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(User.Role item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.toString());
                    if (item == User.Role.ADMIN) {
                        setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #3498db;");
                    }
                }
            }
        });

        TableColumn<User, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("✏️");
            private final Button deleteBtn = new Button("🗑️");
            private final Button roleBtn = new Button("🔄");
            private final HBox buttons = new HBox(5, editBtn, roleBtn, deleteBtn);
            {
                editBtn.getStyleClass().add("btn-warning-small");
                roleBtn.getStyleClass().add("btn-primary-small");
                deleteBtn.getStyleClass().add("btn-danger-small");
                
                editBtn.setOnAction(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    showEditUserDialog(user);
                });
                roleBtn.setOnAction(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    changeUserRole(user);
                });
                deleteBtn.setOnAction(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    deleteUser(user);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    User user = getTableView().getItems().get(getIndex());
                    // Don't allow deleting current admin
                    if (user.getId().equals(MainApp.getCurrentUser().getId())) {
                        setGraphic(new Label("Current"));
                    } else {
                        setGraphic(buttons);
                    }
                }
            }
        });

        userTable.getColumns().addAll(idCol, nameCol, emailCol, roleCol, actionsCol);

        List<User> users = userService.getAllUsers();
        userTable.setItems(FXCollections.observableArrayList(users));

        contentArea.getChildren().addAll(title, addUserBtn, userTable);
    }

    private void showAddUserDialog() {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Add New User");
        dialog.setHeaderText("Create a new user account");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        ComboBox<User.Role> roleBox = new ComboBox<>();
        roleBox.getItems().addAll(User.Role.values());
        roleBox.setValue(User.Role.CLIENT);

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Password:"), 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(new Label("Role:"), 0, 3);
        grid.add(roleBox, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    userService.createUser(nameField.getText(), emailField.getText(), 
                        passwordField.getText(), roleBox.getValue());
                    showAlert(Alert.AlertType.INFORMATION, "Success", "User created successfully!");
                    showUsers();
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to create user: " + e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showEditUserDialog(User user) {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Edit User");
        dialog.setHeaderText("Edit user: " + user.getName());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField(user.getName());
        TextField emailField = new TextField(user.getEmail());

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    userService.updateProfile(user.getId(), nameField.getText(), emailField.getText());
                    showAlert(Alert.AlertType.INFORMATION, "Success", "User updated successfully!");
                    showUsers();
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update user: " + e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void changeUserRole(User user) {
        User.Role newRole = user.getRole() == User.Role.ADMIN ? User.Role.CLIENT : User.Role.ADMIN;
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Change Role");
        confirm.setHeaderText("Change user role");
        confirm.setContentText("Change " + user.getName() + " from " + user.getRole() + " to " + newRole + "?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    userService.changeUserRole(user.getId(), newRole);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Role changed successfully!");
                    showUsers();
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to change role: " + e.getMessage());
                }
            }
        });
    }

    private void deleteUser(User user) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete User");
        confirm.setHeaderText("Delete user: " + user.getName());
        confirm.setContentText("This action cannot be undone. Continue?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    userService.deleteUserById(user.getId());
                    showAlert(Alert.AlertType.INFORMATION, "Success", "User deleted successfully!");
                    showUsers();
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete user: " + e.getMessage());
                }
            }
        });
    }

    // ==================== STADIUMS ====================
    private void showStadiums() {
        contentArea.getChildren().clear();

        Label title = new Label("🏟️ Stadium Management");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Button addBtn = new Button("➕ Add Stadium");
        addBtn.getStyleClass().add("btn-primary");
        addBtn.setOnAction(e -> showAddStadiumDialog());

        TableView<Stadium> stadiumTable = new TableView<>();
        stadiumTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        TableColumn<Stadium, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Stadium, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Stadium, String> cityCol = new TableColumn<>("City");
        cityCol.setCellValueFactory(new PropertyValueFactory<>("city"));

        TableColumn<Stadium, Integer> capacityCol = new TableColumn<>("Capacity");
        capacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        TableColumn<Stadium, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("✏️ Edit");
            private final Button deleteBtn = new Button("🗑️");
            private final HBox buttons = new HBox(5, editBtn, deleteBtn);
            {
                editBtn.getStyleClass().add("btn-warning-small");
                deleteBtn.getStyleClass().add("btn-danger-small");
                editBtn.setOnAction(e -> {
                    Stadium stadium = getTableView().getItems().get(getIndex());
                    showEditStadiumDialog(stadium);
                });
                deleteBtn.setOnAction(e -> {
                    Stadium stadium = getTableView().getItems().get(getIndex());
                    deleteStadium(stadium);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });

        stadiumTable.getColumns().addAll(idCol, nameCol, cityCol, capacityCol, actionsCol);

        List<Stadium> stadiums = stadiumService.getAllStadiums();
        stadiumTable.setItems(FXCollections.observableArrayList(stadiums));

        contentArea.getChildren().addAll(title, addBtn, stadiumTable);
    }

    private void showAddStadiumDialog() {
        Dialog<Stadium> dialog = new Dialog<>();
        dialog.setTitle("Add Stadium");
        dialog.setHeaderText("Add a new stadium");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField();
        TextField cityField = new TextField();
        TextField capacityField = new TextField();

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("City:"), 0, 1);
        grid.add(cityField, 1, 1);
        grid.add(new Label("Capacity:"), 0, 2);
        grid.add(capacityField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    stadiumService.createStadium(nameField.getText(), cityField.getText(), 
                        Integer.parseInt(capacityField.getText()));
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Stadium added!");
                    showStadiums();
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showEditStadiumDialog(Stadium stadium) {
        Dialog<Stadium> dialog = new Dialog<>();
        dialog.setTitle("Edit Stadium");
        dialog.setHeaderText("Edit: " + stadium.getName());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField(stadium.getName());
        TextField cityField = new TextField(stadium.getCity());
        TextField capacityField = new TextField(String.valueOf(stadium.getCapacity()));

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("City:"), 0, 1);
        grid.add(cityField, 1, 1);
        grid.add(new Label("Capacity:"), 0, 2);
        grid.add(capacityField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    stadium.setName(nameField.getText());
                    stadium.setCity(cityField.getText());
                    stadium.setCapacity(Integer.parseInt(capacityField.getText()));
                    stadiumService.updateStadium(stadium);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Stadium updated!");
                    showStadiums();
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void deleteStadium(Stadium stadium) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Stadium");
        confirm.setContentText("Delete " + stadium.getName() + "?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    stadiumService.deleteStadium(stadium);
                    showAlert(Alert.AlertType.INFORMATION, "Deleted", "Stadium deleted!");
                    showStadiums();
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
                }
            }
        });
    }

    // ==================== MATCHES ====================
    private void showMatches() {
        contentArea.getChildren().clear();

        Label title = new Label("⚽ Match Management");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Button addBtn = new Button("➕ Add Match");
        addBtn.getStyleClass().add("btn-primary");
        addBtn.setOnAction(e -> showAddMatchDialog());

        TableView<Match> matchTable = new TableView<>();
        matchTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        TableColumn<Match, String> teamsCol = new TableColumn<>("Match");
        teamsCol.setCellValueFactory(data -> 
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getTeam1() + " vs " + data.getValue().getTeam2()));

        TableColumn<Match, LocalDateTime> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("matchDate"));
        dateCol.setCellFactory(col -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatter.format(item));
            }
        });

        TableColumn<Match, String> stadiumCol = new TableColumn<>("Stadium");
        stadiumCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getStadium().getName()));

        TableColumn<Match, String> ticketsCol = new TableColumn<>("Tickets");
        ticketsCol.setCellValueFactory(data -> {
            int total = data.getValue().getTickets() != null ? data.getValue().getTickets().size() : 0;
            return new javafx.beans.property.SimpleStringProperty(String.valueOf(total));
        });

        TableColumn<Match, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("✏️");
            private final Button deleteBtn = new Button("🗑️");
            private final Button ticketsBtn = new Button("🎫");
            private final HBox buttons = new HBox(5, editBtn, ticketsBtn, deleteBtn);
            {
                editBtn.getStyleClass().add("btn-warning-small");
                ticketsBtn.getStyleClass().add("btn-primary-small");
                deleteBtn.getStyleClass().add("btn-danger-small");
                
                editBtn.setOnAction(e -> {
                    Match match = getTableView().getItems().get(getIndex());
                    showEditMatchDialog(match);
                });
                ticketsBtn.setOnAction(e -> {
                    Match match = getTableView().getItems().get(getIndex());
                    showMatchTickets(match);
                });
                deleteBtn.setOnAction(e -> {
                    Match match = getTableView().getItems().get(getIndex());
                    deleteMatch(match);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });

        matchTable.getColumns().addAll(teamsCol, dateCol, stadiumCol, ticketsCol, actionsCol);

        List<Match> matches = matchService.getAllMatches();
        matchTable.setItems(FXCollections.observableArrayList(matches));

        contentArea.getChildren().addAll(title, addBtn, matchTable);
    }

    private void showAddMatchDialog() {
        Dialog<Match> dialog = new Dialog<>();
        dialog.setTitle("Add Match");
        dialog.setHeaderText("Create a new match");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField team1Field = new TextField();
        team1Field.setPromptText("Team 1");
        TextField team2Field = new TextField();
        team2Field.setPromptText("Team 2");
        DatePicker datePicker = new DatePicker();
        Spinner<Integer> hourSpinner = new Spinner<>(0, 23, 18);
        Spinner<Integer> minuteSpinner = new Spinner<>(0, 59, 0, 15);
        ComboBox<Stadium> stadiumBox = new ComboBox<>();
        stadiumBox.getItems().addAll(stadiumService.getAllStadiums());
        stadiumBox.setPromptText("Select Stadium");

        grid.add(new Label("Team 1:"), 0, 0);
        grid.add(team1Field, 1, 0);
        grid.add(new Label("Team 2:"), 0, 1);
        grid.add(team2Field, 1, 1);
        grid.add(new Label("Date:"), 0, 2);
        grid.add(datePicker, 1, 2);
        grid.add(new Label("Time:"), 0, 3);
        grid.add(new HBox(5, hourSpinner, new Label(":"), minuteSpinner), 1, 3);
        grid.add(new Label("Stadium:"), 0, 4);
        grid.add(stadiumBox, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    LocalDateTime dateTime = datePicker.getValue()
                        .atTime(hourSpinner.getValue(), minuteSpinner.getValue());
                    matchService.createMatch(team1Field.getText(), team2Field.getText(), 
                        dateTime, stadiumBox.getValue());
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Match created!");
                    showMatches();
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showEditMatchDialog(Match match) {
        Dialog<Match> dialog = new Dialog<>();
        dialog.setTitle("Edit Match");
        dialog.setHeaderText("Edit: " + match.getTeam1() + " vs " + match.getTeam2());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField team1Field = new TextField(match.getTeam1());
        TextField team2Field = new TextField(match.getTeam2());
        DatePicker datePicker = new DatePicker(match.getMatchDate().toLocalDate());
        Spinner<Integer> hourSpinner = new Spinner<>(0, 23, match.getMatchDate().getHour());
        Spinner<Integer> minuteSpinner = new Spinner<>(0, 59, match.getMatchDate().getMinute(), 15);
        ComboBox<Stadium> stadiumBox = new ComboBox<>();
        stadiumBox.getItems().addAll(stadiumService.getAllStadiums());
        stadiumBox.setValue(match.getStadium());

        grid.add(new Label("Team 1:"), 0, 0);
        grid.add(team1Field, 1, 0);
        grid.add(new Label("Team 2:"), 0, 1);
        grid.add(team2Field, 1, 1);
        grid.add(new Label("Date:"), 0, 2);
        grid.add(datePicker, 1, 2);
        grid.add(new Label("Time:"), 0, 3);
        grid.add(new HBox(5, hourSpinner, new Label(":"), minuteSpinner), 1, 3);
        grid.add(new Label("Stadium:"), 0, 4);
        grid.add(stadiumBox, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    match.setTeam1(team1Field.getText());
                    match.setTeam2(team2Field.getText());
                    LocalDateTime dateTime = datePicker.getValue()
                        .atTime(hourSpinner.getValue(), minuteSpinner.getValue());
                    match.setMatchDate(dateTime);
                    match.setStadium(stadiumBox.getValue());
                    matchService.updateMatch(match);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Match updated!");
                    showMatches();
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showMatchTickets(Match match) {
        contentArea.getChildren().clear();

        Button backBtn = new Button("← Back to Matches");
        backBtn.getStyleClass().add("btn-secondary");
        backBtn.setOnAction(e -> showMatches());

        Label title = new Label("🎫 Tickets for " + match.getTeam1() + " vs " + match.getTeam2());
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Button addTicketsBtn = new Button("➕ Generate Tickets");
        addTicketsBtn.getStyleClass().add("btn-primary");
        addTicketsBtn.setOnAction(e -> showGenerateTicketsForMatch(match));

        TableView<Ticket> ticketTable = new TableView<>();
        ticketTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        TableColumn<Ticket, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Ticket, Ticket.Category> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Ticket, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Ticket, Ticket.Status> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Ticket.Status item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.toString());
                    switch (item) {
                        case AVAILABLE -> setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                        case RESERVED -> setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
                        case SOLD -> setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    }
                }
            }
        });

        ticketTable.getColumns().addAll(idCol, categoryCol, priceCol, statusCol);

        List<Ticket> tickets = match.getTickets();
        if (tickets != null) {
            ticketTable.setItems(FXCollections.observableArrayList(tickets));
        }

        contentArea.getChildren().addAll(backBtn, title, addTicketsBtn, ticketTable);
    }

    private void showGenerateTicketsForMatch(Match match) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Generate Tickets");
        dialog.setHeaderText("Generate tickets for " + match.getTeam1() + " vs " + match.getTeam2());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        ComboBox<Ticket.Category> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll(Ticket.Category.values());
        categoryBox.setValue(Ticket.Category.STANDARD);
        
        Spinner<Integer> quantitySpinner = new Spinner<>(1, 1000, 50);
        TextField priceField = new TextField("100");

        grid.add(new Label("Category:"), 0, 0);
        grid.add(categoryBox, 1, 0);
        grid.add(new Label("Quantity:"), 0, 1);
        grid.add(quantitySpinner, 1, 1);
        grid.add(new Label("Price ($):"), 0, 2);
        grid.add(priceField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    int quantity = quantitySpinner.getValue();
                    double price = Double.parseDouble(priceField.getText());
                    Ticket.Category category = categoryBox.getValue();
                    
                    ticketService.generateTickets(match, category, quantity, price);
                    showAlert(Alert.AlertType.INFORMATION, "Success", 
                        quantity + " tickets generated!");
                    showMatchTickets(match);
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void deleteMatch(Match match) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Match");
        confirm.setContentText("Delete " + match.getTeam1() + " vs " + match.getTeam2() + "?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    matchService.deleteMatch(match);
                    showAlert(Alert.AlertType.INFORMATION, "Deleted", "Match deleted!");
                    showMatches();
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
                }
            }
        });
    }

    // ==================== TICKETS ====================
    private void showTickets() {
        contentArea.getChildren().clear();

        Label title = new Label("🎫 All Tickets");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Button generateBtn = new Button("➕ Generate Tickets");
        generateBtn.getStyleClass().add("btn-primary");
        generateBtn.setOnAction(e -> showGenerateTicketsDialog());

        // Filter buttons
        HBox filters = new HBox(10);
        Button allBtn = new Button("All");
        Button availableBtn = new Button("Available");
        Button reservedBtn = new Button("Reserved");
        Button soldBtn = new Button("Sold");
        
        allBtn.getStyleClass().add("btn-secondary");
        availableBtn.getStyleClass().add("btn-success");
        reservedBtn.getStyleClass().add("btn-warning");
        soldBtn.getStyleClass().add("btn-danger");

        filters.getChildren().addAll(new Label("Filter:"), allBtn, availableBtn, reservedBtn, soldBtn);

        TableView<Ticket> ticketTable = new TableView<>();
        ticketTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        TableColumn<Ticket, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Ticket, String> matchCol = new TableColumn<>("Match");
        matchCol.setCellValueFactory(data -> {
            Match m = data.getValue().getMatch();
            return new javafx.beans.property.SimpleStringProperty(m.getTeam1() + " vs " + m.getTeam2());
        });

        TableColumn<Ticket, Ticket.Category> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Ticket, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Ticket, Ticket.Status> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Ticket.Status item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.toString());
                    switch (item) {
                        case AVAILABLE -> setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                        case RESERVED -> setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
                        case SOLD -> setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    }
                }
            }
        });

        ticketTable.getColumns().addAll(idCol, matchCol, categoryCol, priceCol, statusCol);

        List<Ticket> allTickets = ticketService.getAllTickets();
        ticketTable.setItems(FXCollections.observableArrayList(allTickets));

        // Filter actions
        allBtn.setOnAction(e -> ticketTable.setItems(FXCollections.observableArrayList(allTickets)));
        availableBtn.setOnAction(e -> ticketTable.setItems(FXCollections.observableArrayList(
            allTickets.stream().filter(t -> t.getStatus() == Ticket.Status.AVAILABLE).toList())));
        reservedBtn.setOnAction(e -> ticketTable.setItems(FXCollections.observableArrayList(
            allTickets.stream().filter(t -> t.getStatus() == Ticket.Status.RESERVED).toList())));
        soldBtn.setOnAction(e -> ticketTable.setItems(FXCollections.observableArrayList(
            allTickets.stream().filter(t -> t.getStatus() == Ticket.Status.SOLD).toList())));

        contentArea.getChildren().addAll(title, generateBtn, filters, ticketTable);
    }

    private void showGenerateTicketsDialog() {
        List<Match> matches = matchService.getAllMatches();
        if (matches.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Matches", "Create a match first!");
            return;
        }

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Generate Tickets");
        dialog.setHeaderText("Generate tickets for a match");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        ComboBox<Match> matchBox = new ComboBox<>();
        matchBox.getItems().addAll(matches);
        matchBox.setPromptText("Select Match");

        ComboBox<Ticket.Category> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll(Ticket.Category.values());
        categoryBox.setValue(Ticket.Category.STANDARD);
        
        Spinner<Integer> quantitySpinner = new Spinner<>(1, 1000, 50);
        TextField priceField = new TextField("100");

        grid.add(new Label("Match:"), 0, 0);
        grid.add(matchBox, 1, 0);
        grid.add(new Label("Category:"), 0, 1);
        grid.add(categoryBox, 1, 1);
        grid.add(new Label("Quantity:"), 0, 2);
        grid.add(quantitySpinner, 1, 2);
        grid.add(new Label("Price ($):"), 0, 3);
        grid.add(priceField, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK && matchBox.getValue() != null) {
                try {
                    int quantity = quantitySpinner.getValue();
                    double price = Double.parseDouble(priceField.getText());
                    Ticket.Category category = categoryBox.getValue();
                    Match match = matchBox.getValue();
                    
                    ticketService.generateTickets(match, category, quantity, price);
                    showAlert(Alert.AlertType.INFORMATION, "Success", 
                        quantity + " tickets generated for " + match.getTeam1() + " vs " + match.getTeam2() + "!");
                    showTickets();
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    // ==================== ALL RESERVATIONS ====================
    private void showAllReservations() {
        contentArea.getChildren().clear();

        Label title = new Label("📋 All Reservations");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        TableView<Reservation> resTable = new TableView<>();
        resTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        TableColumn<Reservation, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Reservation, String> userCol = new TableColumn<>("User");
        userCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getUser().getName()));

        TableColumn<Reservation, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getUser().getEmail()));

        TableColumn<Reservation, String> matchCol = new TableColumn<>("Match");
        matchCol.setCellValueFactory(data -> {
            Match m = data.getValue().getTicket().getMatch();
            return new javafx.beans.property.SimpleStringProperty(m.getTeam1() + " vs " + m.getTeam2());
        });

        TableColumn<Reservation, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getTicket().getCategory().toString()));

        TableColumn<Reservation, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty("$" + data.getValue().getTicket().getPrice()));

        TableColumn<Reservation, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getTicket().getStatus().toString()));

        TableColumn<Reservation, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(data -> {
            LocalDateTime date = data.getValue().getReservationDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM HH:mm");
            return new javafx.beans.property.SimpleStringProperty(formatter.format(date));
        });

        TableColumn<Reservation, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button confirmBtn = new Button("✅");
            private final Button cancelBtn = new Button("❌");
            private final HBox buttons = new HBox(5, confirmBtn, cancelBtn);
            {
                confirmBtn.getStyleClass().add("btn-success-small");
                cancelBtn.getStyleClass().add("btn-danger-small");
                confirmBtn.setOnAction(e -> {
                    Reservation res = getTableView().getItems().get(getIndex());
                    confirmReservationAdmin(res);
                });
                cancelBtn.setOnAction(e -> {
                    Reservation res = getTableView().getItems().get(getIndex());
                    cancelReservationAdmin(res);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Reservation res = getTableView().getItems().get(getIndex());
                    setGraphic(res.getTicket().getStatus() == Ticket.Status.RESERVED ? buttons : null);
                }
            }
        });

        resTable.getColumns().addAll(idCol, userCol, emailCol, matchCol, categoryCol, priceCol, statusCol, dateCol, actionsCol);

        List<Reservation> reservations = reservationService.getAllReservations();
        resTable.setItems(FXCollections.observableArrayList(reservations));

        contentArea.getChildren().addAll(title, resTable);
    }

    private void confirmReservationAdmin(Reservation reservation) {
        try {
            reservationService.confirmReservation(reservation.getId());
            showAlert(Alert.AlertType.INFORMATION, "Success", "Reservation confirmed!");
            showAllReservations();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    private void cancelReservationAdmin(Reservation reservation) {
        try {
            reservationService.cancelReservation(reservation);
            showAlert(Alert.AlertType.INFORMATION, "Cancelled", "Reservation cancelled!");
            showAllReservations();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    // ==================== STATISTICS ====================
    private void showStatistics() {
        contentArea.getChildren().clear();

        Label title = new Label("📈 Statistics & Reports");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        List<Ticket> allTickets = ticketService.getAllTickets();
        List<Match> allMatches = matchService.getAllMatches();

        // Revenue by category
        VBox categoryStats = new VBox(10);
        categoryStats.setPadding(new Insets(20));
        categoryStats.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        
        Label categoryTitle = new Label("💰 Revenue by Category");
        categoryTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        for (Ticket.Category category : Ticket.Category.values()) {
            double revenue = allTickets.stream()
                .filter(t -> t.getCategory() == category && t.getStatus() == Ticket.Status.SOLD)
                .map(Ticket::getPrice)
                .mapToDouble(java.math.BigDecimal::doubleValue).sum();
            long count = allTickets.stream()
                .filter(t -> t.getCategory() == category && t.getStatus() == Ticket.Status.SOLD)
                .count();
            
            HBox row = new HBox(20);
            row.setAlignment(Pos.CENTER_LEFT);
            Label catLabel = new Label(category.toString());
            catLabel.setPrefWidth(100);
            Label countLabel = new Label(count + " tickets");
            countLabel.setPrefWidth(100);
            Label revLabel = new Label(String.format("$%.2f", revenue));
            revLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #27ae60;");
            row.getChildren().addAll(catLabel, countLabel, revLabel);
            categoryStats.getChildren().add(row);
        }
        categoryStats.getChildren().add(0, categoryTitle);

        // Match statistics
        VBox matchStats = new VBox(10);
        matchStats.setPadding(new Insets(20));
        matchStats.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        
        Label matchTitle = new Label("⚽ Tickets per Match");
        matchTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        matchStats.getChildren().add(matchTitle);

        for (Match match : allMatches) {
            long sold = match.getTickets() != null ? 
                match.getTickets().stream().filter(t -> t.getStatus() == Ticket.Status.SOLD).count() : 0;
            long available = match.getTickets() != null ? 
                match.getTickets().stream().filter(t -> t.getStatus() == Ticket.Status.AVAILABLE).count() : 0;
            
            HBox row = new HBox(20);
            row.setAlignment(Pos.CENTER_LEFT);
            Label matchLabel = new Label(match.getTeam1() + " vs " + match.getTeam2());
            matchLabel.setPrefWidth(200);
            Label soldLabel = new Label("Sold: " + sold);
            soldLabel.setStyle("-fx-text-fill: #e74c3c;");
            Label availLabel = new Label("Available: " + available);
            availLabel.setStyle("-fx-text-fill: #27ae60;");
            row.getChildren().addAll(matchLabel, soldLabel, availLabel);
            matchStats.getChildren().add(row);
        }

        // Summary
        VBox summaryBox = new VBox(10);
        summaryBox.setPadding(new Insets(20));
        summaryBox.setStyle("-fx-background-color: linear-gradient(to right, #667eea, #764ba2); -fx-background-radius: 10;");
        
        double totalRevenue = allTickets.stream()
            .filter(t -> t.getStatus() == Ticket.Status.SOLD)
            .map(Ticket::getPrice)
            .mapToDouble(java.math.BigDecimal::doubleValue).sum();
        long totalSold = allTickets.stream().filter(t -> t.getStatus() == Ticket.Status.SOLD).count();
        
        Label summaryTitle = new Label("📊 Summary");
        summaryTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        summaryTitle.setTextFill(Color.WHITE);
        
        Label revenueLabel = new Label(String.format("Total Revenue: $%.2f", totalRevenue));
        revenueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        revenueLabel.setTextFill(Color.WHITE);
        
        Label soldLabel = new Label("Total Tickets Sold: " + totalSold);
        soldLabel.setTextFill(Color.WHITE);

        summaryBox.getChildren().addAll(summaryTitle, revenueLabel, soldLabel);

        contentArea.getChildren().addAll(title, summaryBox, categoryStats, matchStats);
    }

    private void logout() {
        MainApp.setCurrentUser(null);
        MainApp.showLoginView();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public BorderPane getView() {
        return view;
    }
}
