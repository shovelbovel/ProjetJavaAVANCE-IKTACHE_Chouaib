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

public class ClientView {

    private final MatchService matchService = new MatchService();
    private final TicketService ticketService = new TicketService();
    private final ReservationService reservationService = new ReservationService();
    private final UserService userService = new UserService();

    private BorderPane view;
    private VBox contentArea;
    private TableView<Match> matchTable;
    private TableView<Reservation> reservationTable;
    private TextField searchField;

    public ClientView() {
        createView();
        showMatches();
    }

    private void createView() {
        view = new BorderPane();
        view.getStyleClass().add("client-view");

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
        navbar.setStyle("-fx-background-color: linear-gradient(to right, #1a1a2e, #16213e);");

        Label logo = new Label("⚽ World Cup 2026");
        logo.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        logo.setTextFill(Color.WHITE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label userLabel = new Label("👤 " + MainApp.getCurrentUser().getName());
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
        sidebar.setPrefWidth(200);
        sidebar.setStyle("-fx-background-color: #2c3e50;");

        Label menuTitle = new Label("MENU");
        menuTitle.setTextFill(Color.LIGHTGRAY);
        menuTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        menuTitle.setPadding(new Insets(0, 0, 10, 0));

        Button matchesBtn = createSidebarButton("⚽ Browse Matches");
        matchesBtn.setOnAction(e -> showMatches());

        Button reservationsBtn = createSidebarButton("📋 My Reservations");
        reservationsBtn.setOnAction(e -> showReservations());

        Button ticketsBtn = createSidebarButton("🎫 My Tickets");
        ticketsBtn.setOnAction(e -> showMyTickets());

        Button profileBtn = createSidebarButton("👤 My Profile");
        profileBtn.setOnAction(e -> showProfile());

        Button passwordBtn = createSidebarButton("🔒 Change Password");
        passwordBtn.setOnAction(e -> showChangePassword());

        sidebar.getChildren().addAll(menuTitle, matchesBtn, reservationsBtn, ticketsBtn, profileBtn, passwordBtn);
        return sidebar;
    }

    private Button createSidebarButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(170);
        btn.setPrefHeight(40);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.getStyleClass().add("sidebar-btn");
        return btn;
    }

    @SuppressWarnings("unchecked")
    private void showMatches() {
        contentArea.getChildren().clear();

        Label title = new Label("Available Matches");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // Search bar
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchField = new TextField();
        searchField.setPromptText("🔍 Search by team name...");
        searchField.setPrefWidth(300);
        Button searchBtn = new Button("Search");
        searchBtn.getStyleClass().add("btn-primary");
        Button clearBtn = new Button("Clear");
        clearBtn.getStyleClass().add("btn-secondary");
        
        searchBtn.setOnAction(e -> filterMatches());
        clearBtn.setOnAction(e -> {
            searchField.clear();
            refreshMatchTable();
        });
        searchField.setOnAction(e -> filterMatches());
        
        searchBox.getChildren().addAll(searchField, searchBtn, clearBtn);

        matchTable = new TableView<>();
        matchTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        TableColumn<Match, String> team1Col = new TableColumn<>("Team 1");
        team1Col.setCellValueFactory(new PropertyValueFactory<>("team1"));

        TableColumn<Match, String> vsCol = new TableColumn<>("");
        vsCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty("VS"));
        vsCol.setPrefWidth(50);
        vsCol.setStyle("-fx-alignment: CENTER;");

        TableColumn<Match, String> team2Col = new TableColumn<>("Team 2");
        team2Col.setCellValueFactory(new PropertyValueFactory<>("team2"));

        TableColumn<Match, LocalDateTime> dateCol = new TableColumn<>("Date & Time");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("matchDate"));
        dateCol.setCellFactory(col -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatter.format(item));
            }
        });

        TableColumn<Match, String> stadiumCol = new TableColumn<>("Stadium");
        stadiumCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getStadium().getName()));

        TableColumn<Match, String> availableCol = new TableColumn<>("Available Tickets");
        availableCol.setCellValueFactory(data -> {
            List<Ticket> available = ticketService.getAvailableTicketsForMatch(data.getValue().getId());
            return new javafx.beans.property.SimpleStringProperty(String.valueOf(available.size()));
        });

        TableColumn<Match, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button reserveBtn = new Button("🎫 Reserve");
            private final Button buyBtn = new Button("💳 Buy");
            private final HBox buttons = new HBox(5, reserveBtn, buyBtn);
            {
                reserveBtn.getStyleClass().add("btn-warning-small");
                buyBtn.getStyleClass().add("btn-success-small");
                reserveBtn.setOnAction(e -> {
                    Match match = getTableView().getItems().get(getIndex());
                    showReserveDialog(match);
                });
                buyBtn.setOnAction(e -> {
                    Match match = getTableView().getItems().get(getIndex());
                    showBuyDialog(match);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });

        matchTable.getColumns().addAll(team1Col, vsCol, team2Col, dateCol, stadiumCol, availableCol, actionsCol);
        refreshMatchTable();

        contentArea.getChildren().addAll(title, searchBox, matchTable);
    }

    private void filterMatches() {
        String searchText = searchField.getText().toLowerCase().trim();
        if (searchText.isEmpty()) {
            refreshMatchTable();
            return;
        }
        
        List<Match> matches = matchService.getAllMatches();
        List<Match> filtered = matches.stream()
            .filter(m -> m.getTeam1().toLowerCase().contains(searchText) ||
                        m.getTeam2().toLowerCase().contains(searchText) ||
                        m.getStadium().getName().toLowerCase().contains(searchText) ||
                        m.getStadium().getCity().toLowerCase().contains(searchText))
            .toList();
        
        matchTable.setItems(FXCollections.observableArrayList(filtered));
    }

    private void refreshMatchTable() {
        List<Match> matches = matchService.getAllMatches();
        ObservableList<Match> data = FXCollections.observableArrayList(matches);
        matchTable.setItems(data);
    }

    private void showReserveDialog(Match match) {
        List<Ticket> available = ticketService.getAvailableTicketsForMatch(match.getId());
        if (available.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Tickets", "No tickets available for this match.");
            return;
        }

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Reserve Ticket");
        dialog.setHeaderText("Reserve a ticket for " + match.getTeam1() + " vs " + match.getTeam2());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        ComboBox<Ticket.Category> categoryBox = new ComboBox<>();
        for (Ticket ticket : available) {
            if (!categoryBox.getItems().contains(ticket.getCategory())) {
                categoryBox.getItems().add(ticket.getCategory());
            }
        }
        categoryBox.setPromptText("Select Category");

        Label priceLabel = new Label("Price: -");
        categoryBox.setOnAction(e -> {
            Ticket.Category selected = categoryBox.getValue();
            available.stream()
                    .filter(t -> t.getCategory() == selected)
                    .findFirst()
                    .ifPresent(t -> priceLabel.setText("Price: $" + t.getPrice()));
        });

        grid.add(new Label("Category:"), 0, 0);
        grid.add(categoryBox, 1, 0);
        grid.add(priceLabel, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK && categoryBox.getValue() != null) {
                try {
                    Ticket.Category category = categoryBox.getValue();
                    Ticket ticket = available.stream()
                            .filter(t -> t.getCategory() == category)
                            .findFirst()
                            .orElse(null);
                    
                    if (ticket != null) {
                        reservationService.createReservation(MainApp.getCurrentUser(), ticket);
                        showAlert(Alert.AlertType.INFORMATION, "Success", 
                            "Ticket reserved! Complete payment to finalize.");
                        refreshMatchTable();
                    }
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Reservation failed: " + e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showBuyDialog(Match match) {
        List<Ticket> available = ticketService.getAvailableTicketsForMatch(match.getId());
        if (available.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Tickets", "No tickets available for this match.");
            return;
        }

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Buy Ticket");
        dialog.setHeaderText("Purchase a ticket for " + match.getTeam1() + " vs " + match.getTeam2());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        ComboBox<Ticket.Category> categoryBox = new ComboBox<>();
        for (Ticket ticket : available) {
            if (!categoryBox.getItems().contains(ticket.getCategory())) {
                categoryBox.getItems().add(ticket.getCategory());
            }
        }
        categoryBox.setPromptText("Select Category");

        Label priceLabel = new Label("Price: -");
        categoryBox.setOnAction(e -> {
            Ticket.Category selected = categoryBox.getValue();
            available.stream()
                    .filter(t -> t.getCategory() == selected)
                    .findFirst()
                    .ifPresent(t -> priceLabel.setText("Price: $" + t.getPrice()));
        });

        grid.add(new Label("Category:"), 0, 0);
        grid.add(categoryBox, 1, 0);
        grid.add(priceLabel, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK && categoryBox.getValue() != null) {
                try {
                    Ticket.Category category = categoryBox.getValue();
                    Ticket ticket = available.stream()
                            .filter(t -> t.getCategory() == category)
                            .findFirst()
                            .orElse(null);

                    if (ticket != null) {
                        ticketService.purchaseTicket(ticket.getId(), MainApp.getCurrentUser());
                        showAlert(Alert.AlertType.INFORMATION, "Success", 
                            "🎉 Ticket purchased successfully!");
                        refreshMatchTable();
                    }
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Purchase failed: " + e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    @SuppressWarnings("unchecked")
    private void showReservations() {
        contentArea.getChildren().clear();

        Label title = new Label("My Reservations");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        reservationTable = new TableView<>();
        reservationTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

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

        TableColumn<Reservation, String> dateCol = new TableColumn<>("Reserved At");
        dateCol.setCellValueFactory(data -> {
            LocalDateTime date = data.getValue().getReservationDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM HH:mm");
            return new javafx.beans.property.SimpleStringProperty(formatter.format(date));
        });

        TableColumn<Reservation, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button confirmBtn = new Button("✅ Pay");
            private final Button cancelBtn = new Button("❌");
            private final HBox buttons = new HBox(5, confirmBtn, cancelBtn);
            {
                confirmBtn.getStyleClass().add("btn-success-small");
                cancelBtn.getStyleClass().add("btn-danger-small");
                confirmBtn.setOnAction(e -> {
                    Reservation res = getTableView().getItems().get(getIndex());
                    confirmReservation(res);
                });
                cancelBtn.setOnAction(e -> {
                    Reservation res = getTableView().getItems().get(getIndex());
                    cancelReservation(res);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Reservation res = getTableView().getItems().get(getIndex());
                    // Show buttons only if ticket is still reserved
                    setGraphic(res.getTicket().getStatus() == Ticket.Status.RESERVED ? buttons : null);
                }
            }
        });

        reservationTable.getColumns().addAll(matchCol, categoryCol, priceCol, statusCol, dateCol, actionsCol);
        refreshReservationTable();

        contentArea.getChildren().addAll(title, reservationTable);
    }

    private void refreshReservationTable() {
        List<Reservation> reservations = reservationService.getUserReservations(MainApp.getCurrentUser().getId());
        ObservableList<Reservation> data = FXCollections.observableArrayList(reservations);
        reservationTable.setItems(data);
    }

    private void confirmReservation(Reservation reservation) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Payment");
        confirm.setHeaderText("Complete Purchase");
        confirm.setContentText("Pay $" + reservation.getTicket().getPrice() + " for this ticket?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    reservationService.confirmReservation(reservation.getId());
                    showAlert(Alert.AlertType.INFORMATION, "Success", "🎉 Payment successful! Ticket purchased.");
                    refreshReservationTable();
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Payment failed: " + e.getMessage());
                }
            }
        });
    }

    private void cancelReservation(Reservation reservation) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Cancel Reservation");
        confirm.setHeaderText("Cancel this reservation?");
        confirm.setContentText("The ticket will become available again.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    reservationService.cancelReservation(reservation);
                    showAlert(Alert.AlertType.INFORMATION, "Cancelled", "Reservation cancelled.");
                    refreshReservationTable();
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Cancellation failed: " + e.getMessage());
                }
            }
        });
    }

    private void showMyTickets() {
        contentArea.getChildren().clear();

        Label title = new Label("🎫 My Purchased Tickets");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        List<Ticket> myTickets = ticketService.getTicketsByUser(MainApp.getCurrentUser().getId());

        if (myTickets.isEmpty()) {
            Label emptyLabel = new Label("You haven't purchased any tickets yet.");
            emptyLabel.setFont(Font.font("Arial", 16));
            emptyLabel.setTextFill(Color.GRAY);
            contentArea.getChildren().addAll(title, emptyLabel);
            return;
        }

        // Summary card
        VBox summaryCard = new VBox(10);
        summaryCard.setPadding(new Insets(15));
        summaryCard.setStyle("-fx-background-color: linear-gradient(to right, #667eea, #764ba2); -fx-background-radius: 10;");
        
        double totalSpent = myTickets.stream().map(Ticket::getPrice)
                .mapToDouble(java.math.BigDecimal::doubleValue).sum();
        Label summaryTitle = new Label("📊 Summary");
        summaryTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        summaryTitle.setTextFill(Color.WHITE);
        Label countLabel = new Label("Total Tickets: " + myTickets.size());
        countLabel.setTextFill(Color.WHITE);
        Label spentLabel = new Label(String.format("Total Spent: $%.2f", totalSpent));
        spentLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        spentLabel.setTextFill(Color.WHITE);
        summaryCard.getChildren().addAll(summaryTitle, countLabel, spentLabel);

        VBox ticketCards = new VBox(15);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy - HH:mm");

        for (Ticket ticket : myTickets) {
            Match match = ticket.getMatch();
            
            VBox card = new VBox(10);
            card.setPadding(new Insets(20));
            card.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                         "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

            Label matchLabel = new Label("⚽ " + match.getTeam1() + " vs " + match.getTeam2());
            matchLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

            Label dateLabel = new Label("📅 " + formatter.format(match.getMatchDate()));
            Label stadiumLabel = new Label("🏟️ " + match.getStadium().getName() + " - " + match.getStadium().getCity());
            Label categoryLabel = new Label("🎫 Category: " + ticket.getCategory());
            Label priceLabel = new Label("💰 Price: $" + ticket.getPrice());
            priceLabel.setTextFill(Color.web("#27ae60"));

            card.getChildren().addAll(matchLabel, dateLabel, stadiumLabel, categoryLabel, priceLabel);
            ticketCards.getChildren().add(card);
        }

        contentArea.getChildren().addAll(title, summaryCard, ticketCards);
    }

    // ==================== PROFILE ====================
    private void showProfile() {
        contentArea.getChildren().clear();

        User currentUser = MainApp.getCurrentUser();

        Label title = new Label("👤 My Profile");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        VBox profileCard = new VBox(20);
        profileCard.setPadding(new Insets(30));
        profileCard.setMaxWidth(500);
        profileCard.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        // Avatar
        Label avatar = new Label("👤");
        avatar.setFont(Font.font("Arial", 60));
        avatar.setStyle("-fx-background-color: #3498db; -fx-background-radius: 50; " +
                       "-fx-padding: 20; -fx-text-fill: white;");

        // Info fields
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        TextField nameField = new TextField(currentUser.getName());
        nameField.setPrefWidth(300);
        TextField emailField = new TextField(currentUser.getEmail());
        emailField.setPrefWidth(300);

        Label roleLabel = new Label(currentUser.getRole().toString());
        roleLabel.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                          "-fx-padding: 5 15; -fx-background-radius: 15;");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Role:"), 0, 2);
        grid.add(roleLabel, 1, 2);

        Button saveBtn = new Button("💾 Save Changes");
        saveBtn.getStyleClass().add("btn-success");
        saveBtn.setOnAction(e -> {
            try {
                userService.updateProfile(currentUser.getId(), nameField.getText(), emailField.getText());
                currentUser.setName(nameField.getText());
                currentUser.setEmail(emailField.getText());
                showAlert(Alert.AlertType.INFORMATION, "Success", "Profile updated successfully!");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update profile: " + ex.getMessage());
            }
        });

        // Stats
        VBox statsBox = new VBox(10);
        statsBox.setPadding(new Insets(15));
        statsBox.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 5;");

        List<Ticket> myTickets = ticketService.getTicketsByUser(currentUser.getId());
        List<Reservation> myReservations = reservationService.getUserReservations(currentUser.getId());
        double totalSpent = myTickets.stream().map(Ticket::getPrice)
                .mapToDouble(java.math.BigDecimal::doubleValue).sum();

        Label statsTitle = new Label("📊 Your Statistics");
        statsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Label ticketsStat = new Label("🎫 Tickets Purchased: " + myTickets.size());
        Label reservationsStat = new Label("📋 Active Reservations: " + 
            myReservations.stream().filter(r -> r.getTicket().getStatus() == Ticket.Status.RESERVED).count());
        Label spentStat = new Label(String.format("💰 Total Spent: $%.2f", totalSpent));
        spentStat.setStyle("-fx-font-weight: bold; -fx-text-fill: #27ae60;");

        statsBox.getChildren().addAll(statsTitle, ticketsStat, reservationsStat, spentStat);

        profileCard.getChildren().addAll(avatar, grid, saveBtn, statsBox);

        contentArea.getChildren().addAll(title, profileCard);
    }

    // ==================== CHANGE PASSWORD ====================
    private void showChangePassword() {
        contentArea.getChildren().clear();

        Label title = new Label("🔒 Change Password");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        VBox formCard = new VBox(20);
        formCard.setPadding(new Insets(30));
        formCard.setMaxWidth(400);
        formCard.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                         "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        Label infoLabel = new Label("🔐 Secure your account with a strong password");
        infoLabel.setWrapText(true);
        infoLabel.setStyle("-fx-text-fill: #7f8c8d;");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        PasswordField currentPassField = new PasswordField();
        currentPassField.setPromptText("Enter current password");
        currentPassField.setPrefWidth(250);

        PasswordField newPassField = new PasswordField();
        newPassField.setPromptText("Enter new password");
        newPassField.setPrefWidth(250);

        PasswordField confirmPassField = new PasswordField();
        confirmPassField.setPromptText("Confirm new password");
        confirmPassField.setPrefWidth(250);

        grid.add(new Label("Current Password:"), 0, 0);
        grid.add(currentPassField, 1, 0);
        grid.add(new Label("New Password:"), 0, 1);
        grid.add(newPassField, 1, 1);
        grid.add(new Label("Confirm Password:"), 0, 2);
        grid.add(confirmPassField, 1, 2);

        Label strengthLabel = new Label("");
        newPassField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.length() < 6) {
                strengthLabel.setText("⚠️ Weak - At least 6 characters");
                strengthLabel.setStyle("-fx-text-fill: #e74c3c;");
            } else if (newVal.length() < 10) {
                strengthLabel.setText("⚡ Medium");
                strengthLabel.setStyle("-fx-text-fill: #f39c12;");
            } else {
                strengthLabel.setText("✅ Strong");
                strengthLabel.setStyle("-fx-text-fill: #27ae60;");
            }
        });

        Button changeBtn = new Button("🔄 Change Password");
        changeBtn.getStyleClass().add("btn-primary");
        changeBtn.setOnAction(e -> {
            String currentPass = currentPassField.getText();
            String newPass = newPassField.getText();
            String confirmPass = confirmPassField.getText();

            if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Please fill in all fields.");
                return;
            }

            if (!newPass.equals(confirmPass)) {
                showAlert(Alert.AlertType.ERROR, "Error", "New passwords do not match!");
                return;
            }

            if (newPass.length() < 6) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Password must be at least 6 characters.");
                return;
            }

            try {
                boolean success = userService.changePassword(
                    MainApp.getCurrentUser().getId(), 
                    currentPass, 
                    newPass
                );
                
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Password changed successfully!");
                    currentPassField.clear();
                    newPassField.clear();
                    confirmPassField.clear();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Current password is incorrect.");
                }
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to change password: " + ex.getMessage());
            }
        });

        formCard.getChildren().addAll(infoLabel, grid, strengthLabel, changeBtn);

        contentArea.getChildren().addAll(title, formCard);
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
