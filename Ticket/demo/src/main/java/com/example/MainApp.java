package com.example;

import com.example.entity.Match;
import com.example.entity.Stadium;
import com.example.entity.Ticket;
import com.example.entity.User;
import com.example.service.MatchService;
import com.example.service.StadiumService;
import com.example.service.TicketService;
import com.example.service.UserService;
import com.example.util.HibernateUtil;
import com.example.view.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.List;

public class MainApp extends Application {

    private static Stage primaryStage;
    private static User currentUser;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("World Cup 2026 - Ticket Management System");
        
        // Initialize database and create default admin if needed
        initializeDatabase();
        
        // Show login view
        showLoginView();
    }

    private void initializeDatabase() {
        try {
            // Initialize Hibernate
            HibernateUtil.getSessionFactory();
            
            UserService userService = new UserService();
            StadiumService stadiumService = new StadiumService();
            MatchService matchService = new MatchService();
            TicketService ticketService = new TicketService();
            
            // Create default admin if no users exist
            if (userService.getAllUsers().isEmpty()) {
                userService.createUser("Admin", "admin@worldcup.com", "admin123", User.Role.ADMIN);
                userService.createUser("Client Test", "client@test.com", "client123", User.Role.CLIENT);
                System.out.println("Default users created:");
                System.out.println("  Admin: admin@worldcup.com / admin123");
                System.out.println("  Client: client@test.com / client123");
            }
            
            // Initialize sample data if no stadiums exist
            if (stadiumService.getAllStadiums().isEmpty()) {
                initializeSampleData(stadiumService, matchService, ticketService);
            }
            
        } catch (Exception e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void initializeSampleData(StadiumService stadiumService, MatchService matchService, TicketService ticketService) {
        System.out.println("Creating World Cup 2026 sample data...");
        
        // Create World Cup 2026 Stadiums (USA, Mexico, Canada)
        stadiumService.createStadium("MetLife Stadium", "New Jersey, USA", 82500);
        stadiumService.createStadium("AT&T Stadium", "Dallas, USA", 80000);
        stadiumService.createStadium("SoFi Stadium", "Los Angeles, USA", 70000);
        stadiumService.createStadium("Estadio Azteca", "Mexico City, Mexico", 87000);
        stadiumService.createStadium("Hard Rock Stadium", "Miami, USA", 65000);
        stadiumService.createStadium("Lumen Field", "Seattle, USA", 69000);
        stadiumService.createStadium("Gillette Stadium", "Boston, USA", 65000);
        stadiumService.createStadium("BMO Field", "Toronto, Canada", 45000);
        stadiumService.createStadium("BC Place", "Vancouver, Canada", 54000);
        stadiumService.createStadium("Estadio BBVA", "Monterrey, Mexico", 53500);
        
        List<Stadium> stadiums = stadiumService.getAllStadiums();
        
        // Create World Cup 2026 Group Stage Matches - Starting June 11, 2026
        LocalDateTime baseDate = LocalDateTime.of(2026, 6, 11, 18, 0);
        
        // Group A - USA, Mexico, Brazil, Croatia
        matchService.createMatch("USA", "Croatia", baseDate, stadiums.get(0)); // MetLife
        matchService.createMatch("Mexico", "Brazil", baseDate.plusHours(3), stadiums.get(3)); // Azteca
        matchService.createMatch("USA", "Mexico", baseDate.plusDays(4), stadiums.get(2)); // SoFi
        matchService.createMatch("Brazil", "Croatia", baseDate.plusDays(4).plusHours(3), stadiums.get(1)); // AT&T
        matchService.createMatch("USA", "Brazil", baseDate.plusDays(8), stadiums.get(4)); // Hard Rock
        matchService.createMatch("Croatia", "Mexico", baseDate.plusDays(8).plusHours(3), stadiums.get(9)); // BBVA
        
        // Group B - Germany, Spain, Japan, Argentina
        matchService.createMatch("Germany", "Japan", baseDate.plusDays(1), stadiums.get(1)); // AT&T
        matchService.createMatch("Spain", "Argentina", baseDate.plusDays(1).plusHours(3), stadiums.get(0)); // MetLife
        matchService.createMatch("Germany", "Argentina", baseDate.plusDays(5), stadiums.get(3)); // Azteca
        matchService.createMatch("Spain", "Japan", baseDate.plusDays(5).plusHours(3), stadiums.get(5)); // Lumen
        matchService.createMatch("Germany", "Spain", baseDate.plusDays(9), stadiums.get(2)); // SoFi
        matchService.createMatch("Japan", "Argentina", baseDate.plusDays(9).plusHours(3), stadiums.get(6)); // Gillette
        
        // Group C - France, England, Portugal, Netherlands
        matchService.createMatch("France", "Netherlands", baseDate.plusDays(2), stadiums.get(2)); // SoFi
        matchService.createMatch("England", "Portugal", baseDate.plusDays(2).plusHours(3), stadiums.get(7)); // BMO
        matchService.createMatch("France", "Portugal", baseDate.plusDays(6), stadiums.get(3)); // Azteca
        matchService.createMatch("England", "Netherlands", baseDate.plusDays(6).plusHours(3), stadiums.get(8)); // BC Place
        matchService.createMatch("France", "England", baseDate.plusDays(10), stadiums.get(0)); // MetLife
        matchService.createMatch("Portugal", "Netherlands", baseDate.plusDays(10).plusHours(3), stadiums.get(4)); // Hard Rock
        
        // Group D - Italy, Belgium, Morocco, Canada
        matchService.createMatch("Italy", "Morocco", baseDate.plusDays(3), stadiums.get(6)); // Gillette
        matchService.createMatch("Belgium", "Canada", baseDate.plusDays(3).plusHours(3), stadiums.get(7)); // BMO
        matchService.createMatch("Italy", "Canada", baseDate.plusDays(7), stadiums.get(8)); // BC Place
        matchService.createMatch("Belgium", "Morocco", baseDate.plusDays(7).plusHours(3), stadiums.get(5)); // Lumen
        matchService.createMatch("Italy", "Belgium", baseDate.plusDays(11), stadiums.get(1)); // AT&T
        matchService.createMatch("Morocco", "Canada", baseDate.plusDays(11).plusHours(3), stadiums.get(9)); // BBVA
        
        // Round of 16
        matchService.createMatch("1A vs 2B", "Round of 16", baseDate.plusDays(15), stadiums.get(0)); // MetLife
        matchService.createMatch("1B vs 2A", "Round of 16", baseDate.plusDays(15).plusHours(4), stadiums.get(3)); // Azteca
        matchService.createMatch("1C vs 2D", "Round of 16", baseDate.plusDays(16), stadiums.get(1)); // AT&T
        matchService.createMatch("1D vs 2C", "Round of 16", baseDate.plusDays(16).plusHours(4), stadiums.get(2)); // SoFi
        
        // Quarter Finals
        matchService.createMatch("QF1", "Quarter Final", baseDate.plusDays(20), stadiums.get(0)); // MetLife
        matchService.createMatch("QF2", "Quarter Final", baseDate.plusDays(20).plusHours(4), stadiums.get(3)); // Azteca
        matchService.createMatch("QF3", "Quarter Final", baseDate.plusDays(21), stadiums.get(1)); // AT&T
        matchService.createMatch("QF4", "Quarter Final", baseDate.plusDays(21).plusHours(4), stadiums.get(2)); // SoFi
        
        // Semi Finals
        matchService.createMatch("SF1", "Semi Final", baseDate.plusDays(25), stadiums.get(0)); // MetLife
        matchService.createMatch("SF2", "Semi Final", baseDate.plusDays(26), stadiums.get(3)); // Azteca
        
        // Third Place & Final
        matchService.createMatch("3rd Place", "Third Place Match", baseDate.plusDays(29), stadiums.get(4)); // Hard Rock
        matchService.createMatch("FINAL", "World Cup Final", baseDate.plusDays(30), stadiums.get(0)); // MetLife
        
        // Generate tickets for all matches
        List<Match> matches = matchService.getAllMatches();
        for (Match match : matches) {
            String team2 = match.getTeam2();
            boolean isFinal = team2.contains("Final");
            boolean isSemiFinal = team2.contains("Semi");
            boolean isQuarter = team2.contains("Quarter");
            boolean isRound16 = team2.contains("Round of 16");
            
            double vipPrice, standardPrice, economyPrice;
            if (isFinal) {
                vipPrice = 3000.0; standardPrice = 1500.0; economyPrice = 600.0;
            } else if (isSemiFinal) {
                vipPrice = 2000.0; standardPrice = 1000.0; economyPrice = 400.0;
            } else if (isQuarter) {
                vipPrice = 1500.0; standardPrice = 750.0; economyPrice = 300.0;
            } else if (isRound16) {
                vipPrice = 1000.0; standardPrice = 500.0; economyPrice = 200.0;
            } else {
                vipPrice = 800.0; standardPrice = 350.0; economyPrice = 150.0;
            }
            
            ticketService.generateTickets(match, Ticket.Category.VIP, 100, vipPrice);
            ticketService.generateTickets(match, Ticket.Category.STANDARD, 300, standardPrice);
            ticketService.generateTickets(match, Ticket.Category.ECONOMY, 600, economyPrice);
        }
        
        System.out.println("✅ World Cup 2026 data created:");
        System.out.println("   📍 " + stadiums.size() + " stadiums");
        System.out.println("   ⚽ " + matches.size() + " matches");
        System.out.println("   🎫 " + (matches.size() * 1000) + " tickets");
    }

    public static void showLoginView() {
        currentUser = null;
        LoginView loginView = new LoginView();
        Scene scene = new Scene(loginView.getView(), 450, 600);
        scene.getStylesheets().add(MainApp.class.getResource("/styles/main.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void stop() {
        HibernateUtil.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
