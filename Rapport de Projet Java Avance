<img src="i.jpeg" alt="Moi" width="100"/>

# Système de Gestion des Billets - Coupe du Monde FIFA 2026

**Application Desktop JavaFX**

---

**EMSI - École Marocaine des Sciences de l'Ingénieur**  
Département d'Informatique

**Réalisé par:** Chouaib Iktache  
**Encadré par:** Pr. Abderrahim Larhlimi  
**Année Universitaire:** 2025-2026

---

## Table des Matières

1. [Introduction](#1-introduction)
2. [Technologies Utilisées](#2-technologies-utilisées)
3. [Architecture du Projet](#3-architecture-du-projet)
4. [Fonctionnalités](#4-fonctionnalités)
5. [Utilisation des Collections Java](#5-utilisation-des-collections-java)
6. [Base de Données](#6-base-de-données)
7. [Interface Utilisateur](#7-interface-utilisateur)
8. [Données Initiales](#8-données-initiales)
9. [Guide d'Utilisation](#9-guide-dutilisation)
10. [Conclusion](#10-conclusion)

---

## 1. Introduction

### 1.1 Contexte du Projet

La Coupe du Monde FIFA 2026, organisée conjointement par les États-Unis, le Mexique et le Canada, représente un événement sportif majeur nécessitant une gestion efficace des billets pour les millions de spectateurs attendus.

Ce projet vise à développer une application desktop complète pour la gestion des billets, permettant aux administrateurs de gérer les stades, les matchs et les billets, tout en offrant aux clients une interface intuitive pour acheter et réserver leurs places.

### 1.2 Objectifs

- ✅ Développer une application desktop moderne avec JavaFX
- ✅ Implémenter une architecture MVC robuste
- ✅ Gérer la persistance des données avec Hibernate ORM et SQLite
- ✅ Offrir une interface utilisateur intuitive et responsive
- ✅ Séparer les fonctionnalités Admin et Client
- ✅ Permettre la gestion complète du cycle de vie des billets

### 1.3 Périmètre Fonctionnel

- Authentification des utilisateurs (Admin/Client)
- Gestion des stades (CRUD)
- Gestion des matchs (CRUD)
- Génération et gestion des billets
- Achat et réservation de billets
- Tableau de bord avec statistiques
- Export des données en CSV

---

## 2. Technologies Utilisées

### 2.1 Langage de Programmation

| Technologie | Version | Utilisation |
|-------------|---------|-------------|
| Java | 17 (LTS) | Langage principal |

### 2.2 Frameworks et Bibliothèques

| Framework | Version | Utilisation |
|-----------|---------|-------------|
| JavaFX | 21.0.1 | Interface graphique |
| Hibernate ORM | 6.4.4.Final | Mapping objet-relationnel |
| Hibernate Validator | 8.0.1.Final | Validation des données |
| SQLite JDBC | 3.45.1.0 | Driver base de données |
| Apache Commons CSV | 1.10.0 | Export CSV |
| SLF4J | 2.0.9 | Logging |
| JUnit Jupiter | 5.10.0 | Tests unitaires |

### 2.3 Outils de Développement

| Outil | Utilisation |
|-------|-------------|
| Maven 3.9.12 | Gestion des dépendances et build |
| VS Code | IDE de développement |
| Git | Contrôle de version |
| SQLite | Base de données embarquée |

### 2.4 Justification des Choix Technologiques

#### JavaFX
- Interface utilisateur moderne et stylisable (CSS)
- Composants riches (TableView, Charts, etc.)
- Support du pattern MVC
- Performance native

#### Hibernate ORM
- Mapping automatique objets-tables
- Gestion transparente des transactions
- Requêtes HQL puissantes
- Indépendance vis-à-vis de la base de données

#### SQLite
- Application desktop sans serveur
- Base de données embarquée
- Déploiement simplifié
- Performances excellentes pour volumes modérés

---

## 3. Architecture du Projet

### 3.1 Structure du Projet

```
demo/
├── pom.xml
├── worldcup_tickets.db
└── src/
    └── main/
        ├── java/
        │   └── com/example/
        │       ├── MainApp.java
        │       ├── Launcher.java
        │       ├── entity/
        │       │   ├── User.java
        │       │   ├── Stadium.java
        │       │   ├── Match.java
        │       │   ├── Ticket.java
        │       │   └── Reservation.java
        │       ├── dao/
        │       │   ├── GenericDAO.java
        │       │   ├── GenericDAOImpl.java
        │       │   └── [DAO implementations]
        │       ├── service/
        │       │   ├── UserService.java
        │       │   ├── StadiumService.java
        │       │   ├── MatchService.java
        │       │   ├── TicketService.java
        │       │   └── ReservationService.java
        │       ├── view/
        │       │   ├── LoginView.java
        │       │   ├── AdminView.java
        │       │   └── ClientView.java
        │       └── util/
        │           ├── HibernateUtil.java
        │           └── CsvExporter.java
        └── resources/
            ├── hibernate.cfg.xml
            ├── simplelogger.properties
            └── styles/
                └── main.css
```

### 3.2 Architecture en Couches

```
┌─────────────────────────────────────────┐
│     Couche Présentation (View)          │
│   LoginView, AdminView, ClientView      │
├─────────────────────────────────────────┤
│   Couche Service (Business Logic)       │
│ UserService, MatchService, TicketService│
├─────────────────────────────────────────┤
│      Couche DAO (Data Access)           │
│   GenericDAO, UserDAO, MatchDAO, etc.   │
├─────────────────────────────────────────┤
│        Couche Persistance               │
│       Hibernate ORM + SQLite            │
└─────────────────────────────────────────┘
```

### 3.3 Pattern DAO (Data Access Object)

```java
public interface GenericDAO<T, ID> {
    void save(T entity);
    void update(T entity);
    void delete(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
}
```

### 3.4 Entités (Entities)

```java
@Entity @Table(name = "users")
public class User {
    Long id, String name, String email, 
    String password, Role role (ADMIN/CLIENT)
}

@Entity @Table(name = "stadiums")
public class Stadium {
    Long id, String name, String city, int capacity
}

@Entity @Table(name = "matches")
public class Match {
    Long id, String team1, String team2,
    LocalDateTime matchDate, Stadium stadium,
    List<Ticket> tickets
}

@Entity @Table(name = "tickets")
public class Ticket {
    Long id, Category category (VIP/STANDARD/ECONOMY),
    BigDecimal price, Status status (AVAILABLE/RESERVED/SOLD),
    Match match
}

@Entity @Table(name = "reservations")
public class Reservation {
    Long id, LocalDateTime reservationDate,
    User user, Ticket ticket
}
```

---

## 4. Fonctionnalités

### 4.1 Module d'Authentification

- Connexion sécurisée avec email/mot de passe
- Distinction des rôles (Admin/Client)
- Redirection automatique vers le dashboard approprié
- Possibilité d'inscription pour les nouveaux clients

### 4.2 Module Administration

#### Dashboard
- Vue d'ensemble des statistiques
- Nombre total de stades, matchs, billets
- Billets vendus, réservés, disponibles
- Revenu total généré
- Actions rapides

#### Gestion des Utilisateurs
- Liste de tous les utilisateurs
- Ajout de nouveaux utilisateurs (Admin ou Client)
- Suppression d'utilisateurs
- Visualisation des rôles

#### Gestion des Stades
- CRUD complet des stades
- Informations: nom, ville, capacité
- 10 stades de la Coupe du Monde 2026 pré-configurés

#### Gestion des Matchs
- Création de matchs avec équipes et date
- Association à un stade
- Génération automatique de billets
- Visualisation des billets par match

#### Gestion des Billets
- Génération en masse (VIP, Standard, Economy)
- Tarification différenciée selon catégorie
- Filtrage par statut
- Statistiques de ventes

#### Statistiques
- Revenus par catégorie de billet
- Ventes par match
- Graphiques et tableaux récapitulatifs

### 4.3 Module Client

#### Dashboard
- Bienvenue personnalisée
- Résumé des billets et réservations
- Accès rapide aux fonctionnalités

#### Consultation des Matchs
- Liste des matchs disponibles
- Barre de recherche par équipe
- Informations: équipes, date, stade, billets disponibles
- Achat ou réservation directe

#### Mes Billets
- Historique des achats
- Détails: match, catégorie, prix, statut
- Total dépensé

#### Mes Réservations
- Liste des réservations actives
- Confirmation d'achat
- Annulation possible

#### Profil
- Modification du nom et email
- Changement de mot de passe
- Statistiques personnelles

---

## 5. Utilisation des Collections Java

### 5.1 List<T>

La collection `List` est la plus utilisée dans le projet:

```java
// Dans Match.java - relation OneToMany
private List<Ticket> tickets = new ArrayList<>();

// Dans les services
List<Stadium> stadiums = stadiumService.getAllStadiums();
List<Match> matches = matchService.getAllMatches();
List<Ticket> myTickets = ticketService.getTicketsByUser(userId);
List<Reservation> reservations = reservationService.getUserReservations(userId);
```

### 5.2 Stream API

L'API Stream de Java 8+ est largement utilisée pour le filtrage et les transformations:

```java
// Filtrer les billets vendus
List<Ticket> soldTickets = ticketDAO.findAll().stream()
    .filter(t -> t.getStatus() == Ticket.Status.SOLD)
    .collect(Collectors.toList());

// Calculer le revenu total
double totalRevenue = ticketService.getAllTickets().stream()
    .filter(t -> t.getStatus() == Ticket.Status.SOLD)
    .map(Ticket::getPrice)
    .mapToDouble(BigDecimal::doubleValue)
    .sum();

// Rechercher des matchs par nom d'équipe
List<Match> filtered = matches.stream()
    .filter(m -> m.getTeam1().toLowerCase().contains(searchText) ||
                 m.getTeam2().toLowerCase().contains(searchText))
    .collect(Collectors.toList());

// Compter les réservations actives
long activeReservations = myReservations.stream()
    .filter(r -> r.getTicket().getStatus() == Ticket.Status.RESERVED)
    .count();
```

### 5.3 Opérations sur les Collections

| Opération | Méthode | Exemple |
|-----------|---------|---------|
| Filtrage | `filter()` | Billets par statut |
| Transformation | `map()` | Extraction des prix |
| Réduction | `reduce()` | Somme des revenus |
| Comptage | `count()` | Nombre de réservations |
| Collecte | `collect()` | Conversion en List |
| Recherche | `findFirst()` | Premier billet disponible |

---

## 6. Base de Données

### 6.1 Schéma de la Base de Données

```sql
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL -- 'ADMIN' ou 'CLIENT'
);

CREATE TABLE stadiums (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    capacity INTEGER NOT NULL
);

CREATE TABLE matches (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    team1 VARCHAR(100) NOT NULL,
    team2 VARCHAR(100) NOT NULL,
    match_date DATETIME NOT NULL,
    stadium_id INTEGER REFERENCES stadiums(id)
);

CREATE TABLE tickets (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    category VARCHAR(20) NOT NULL, -- 'VIP', 'STANDARD', 'ECONOMY'
    price DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL, -- 'AVAILABLE', 'RESERVED', 'SOLD'
    match_id INTEGER REFERENCES matches(id)
);

CREATE TABLE reservations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    reservation_date DATETIME NOT NULL,
    user_id INTEGER REFERENCES users(id),
    ticket_id INTEGER REFERENCES tickets(id)
);
```

### 6.2 Relations

- **Stadium - Match**: Un stade peut accueillir plusieurs matchs (1-N)
- **Match - Ticket**: Un match a plusieurs billets (1-N)
- **User - Reservation**: Un utilisateur peut avoir plusieurs réservations (1-N)
- **Ticket - Reservation**: Un billet peut avoir une réservation (1-1)

### 6.3 Configuration Hibernate

```xml
<hibernate-configuration>
    <session-factory>
        <property name="hibernate.connection.driver_class">
            org.sqlite.JDBC
        </property>
        <property name="hibernate.connection.url">
            jdbc:sqlite:worldcup_tickets.db
        </property>
        <property name="hibernate.dialect">
            org.hibernate.community.dialect.SQLiteDialect
        </property>
        <property name="hibernate.hbm2ddl.auto">update</property>
        <property name="hibernate.show_sql">true</property>
        
        <!-- Mapping classes -->
        <mapping class="com.example.entity.User"/>
        <mapping class="com.example.entity.Stadium"/>
        <mapping class="com.example.entity.Match"/>
        <mapping class="com.example.entity.Ticket"/>
        <mapping class="com.example.entity.Reservation"/>
    </session-factory>
</hibernate-configuration>
```

---

## 7. Interface Utilisateur

### 7.1 Design et Ergonomie

L'interface utilise un design moderne avec:
- Palette de couleurs cohérente (bleu, vert, orange)
- Sidebar de navigation
- Cards pour les statistiques
- Tableaux interactifs (TableView)
- Formulaires avec validation
- Dialogs pour les confirmations

### 7.2 Styles CSS

```css
/* Boutons */
.btn-primary {
    -fx-background-color: linear-gradient(to bottom, #667eea, #764ba2);
    -fx-text-fill: white;
    -fx-font-weight: bold;
    -fx-background-radius: 5;
    -fx-cursor: hand;
}

.btn-success {
    -fx-background-color: linear-gradient(to bottom, #11998e, #38ef7d);
    -fx-text-fill: white;
}

/* Sidebar */
.sidebar-btn {
    -fx-background-color: transparent;
    -fx-text-fill: #ecf0f1;
    -fx-font-size: 14px;
}

.sidebar-btn:hover {
    -fx-background-color: rgba(255,255,255,0.1);
}
```

### 7.3 Composants JavaFX Utilisés

| Composant | Utilisation |
|-----------|-------------|
| BorderPane | Layout principal |
| VBox/HBox | Organisation verticale/horizontale |
| TableView | Affichage des données tabulaires |
| Button | Actions utilisateur |
| TextField/PasswordField | Saisie de données |
| ComboBox | Sélection dans une liste |
| Label | Affichage de texte |
| Dialog/Alert | Popups et confirmations |
| DatePicker | Sélection de dates |

---

## 8. Données Initiales

### 8.1 Stades de la Coupe du Monde 2026

| Stade | Ville | Capacité |
|-------|-------|----------|
| MetLife Stadium | New Jersey, USA | 82 500 |
| AT&T Stadium | Dallas, USA | 80 000 |
| SoFi Stadium | Los Angeles, USA | 70 000 |
| Estadio Azteca | Mexico City, Mexico | 87 000 |
| Hard Rock Stadium | Miami, USA | 65 000 |
| Lumen Field | Seattle, USA | 69 000 |
| Gillette Stadium | Boston, USA | 65 000 |
| BMO Field | Toronto, Canada | 45 000 |
| BC Place | Vancouver, Canada | 54 000 |
| Estadio BBVA | Monterrey, Mexico | 53 500 |

### 8.2 Équipes et Groupes

- **Groupe A**: USA, Mexico, Brazil, Croatia
- **Groupe B**: Germany, Spain, Japan, Argentina
- **Groupe C**: France, England, Portugal, Netherlands
- **Groupe D**: Italy, Belgium, Morocco, Canada

### 8.3 Tarification des Billets

| Phase | VIP | Standard | Economy |
|-------|-----|----------|---------|
| Phase de groupes | $800 | $350 | $150 |
| Huitièmes de finale | $1 000 | $500 | $200 |
| Quarts de finale | $1 500 | $750 | $300 |
| Demi-finales | $2 000 | $1 000 | $400 |
| Finale | $3 000 | $1 500 | $600 |

---

## 9. Guide d'Utilisation

### 9.1 Prérequis

- Java JDK 17 ou supérieur
- Maven 3.9+
- 512 MB RAM minimum

### 9.2 Installation

```bash
# Cloner le projet
git clone [repository-url]
cd demo

# Compiler le projet
mvn clean compile

# Lancer l'application
mvn exec:java -Dexec.mainClass=com.example.Launcher
```

### 9.3 Comptes par Défaut

| Rôle | Email | Mot de passe |
|------|-------|--------------|
| Admin | admin@worldcup.com | admin123 |
| Client | client@test.com | client123 |

---

## 10. Conclusion

### 10.1 Bilan

Ce projet a permis de développer une application desktop complète et fonctionnelle pour la gestion des billets de la Coupe du Monde 2026. Les objectifs initiaux ont été atteints:

- ✅ Interface moderne avec JavaFX
- ✅ Architecture en couches (DAO, Service, View)
- ✅ Persistance avec Hibernate ORM et SQLite
- ✅ Gestion complète des billets (génération, vente, réservation)
- ✅ Séparation Admin/Client
- ✅ Utilisation avancée des Collections Java et Stream API

### 10.2 Compétences Acquises

- Développement d'applications desktop avec JavaFX
- Mapping objet-relationnel avec Hibernate
- Pattern DAO et architecture en couches
- Collections Java et programmation fonctionnelle (Stream API)
- Gestion de projet avec Maven

### 10.3 Améliorations Possibles

- Ajout de threads pour les opérations longues
- Intégration d'un système de paiement
- Génération de billets PDF
- Notifications par email
- Application mobile compagnon
- Synchronisation cloud

---

*Rapport généré pour le projet de gestion des billets - Coupe du Monde FIFA 2026*
