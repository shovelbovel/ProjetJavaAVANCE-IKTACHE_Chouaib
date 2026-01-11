# 🏆 World Cup 2026 - Ticket Management System

![Java](https://img.shields.io/badge/Java-17-orange)
![JavaFX](https://img.shields.io/badge/JavaFX-21.0.1-blue)
![Hibernate](https://img.shields.io/badge/Hibernate-6.4.4-green)
![SQLite](https://img.shields.io/badge/SQLite-3.45.1-lightgrey)
![Maven](https://img.shields.io/badge/Maven-3.9+-red)

Application desktop JavaFX pour la gestion des billets de la Coupe du Monde FIFA 2026.

---

## 📋 Fiche Projet

| Champ | Information |
|-------|-------------|
| **Nom** | Chouaib IKTACHE |
| **Titre du Projet** | Système de Gestion des Billets - Coupe du Monde FIFA 2026 |
| **Module** | Java Avancé - 4IIR EMSI |
| **Encadrant** | Pr. Abderrahim LARHLIMI |
| **Année** | 2025-2026 |

---

## 📝 Descriptif

Application desktop complète permettant la gestion des billets pour la Coupe du Monde 2026. L'application offre :

- **Interface Admin** : Gestion des stades, matchs, billets, utilisateurs avec dashboard statistiques
- **Interface Client** : Consultation des matchs, achat/réservation de billets, gestion du profil
- **Architecture en couches** : DAO - Service - View (MVC)
- **Persistance** : Hibernate ORM avec base SQLite embarquée

---

## 🛠️ Technologies et Environnement

### Langage
| Technologie | Version | Description |
|-------------|---------|-------------|
| Java | JDK 17 (LTS) | Langage principal |

### Frameworks & Bibliothèques
| Dépendance | Version | Utilisation |
|------------|---------|-------------|
| JavaFX | 21.0.1 | Interface graphique |
| Hibernate ORM | 6.4.4.Final | Mapping objet-relationnel |
| Hibernate Validator | 8.0.1.Final | Validation des entités |
| SQLite JDBC | 3.45.1.0 | Driver base de données |
| Apache Commons CSV | 1.10.0 | Export CSV |
| JUnit Jupiter | 5.10.0 | Tests unitaires |
| SLF4J Simple | 2.0.9 | Logging |

### Outils
| Outil | Utilisation |
|-------|-------------|
| Maven 3.9+ | Gestion des dépendances et build |
| VS Code | IDE de développement |
| Git | Contrôle de version |

---

## 📁 Structure du Projet

```
demo/
├── pom.xml                          # Configuration Maven
├── worldcup_tickets.db              # Base de données SQLite
├── README.md                        # Ce fichier
├── rapport/
│   └── rapport_projet_v2.tex        # Rapport LaTeX
└── src/
    └── main/
        ├── java/com/example/
        │   ├── MainApp.java         # Point d'entrée JavaFX
        │   ├── Launcher.java        # Lanceur (contourne modules JavaFX)
        │   ├── entity/              # Entités JPA
        │   │   ├── User.java
        │   │   ├── Stadium.java
        │   │   ├── Match.java
        │   │   ├── Ticket.java
        │   │   └── Reservation.java
        │   ├── dao/                 # Data Access Objects
        │   │   ├── GenericDAO.java
        │   │   ├── GenericDAOImpl.java
        │   │   ├── UserDAO.java / UserDAOImpl.java
        │   │   ├── MatchDAO.java / MatchDAOImpl.java
        │   │   ├── TicketDAO.java / TicketDAOImpl.java
        │   │   ├── StadiumDAO.java / StadiumDAOImpl.java
        │   │   └── ReservationDAO.java / ReservationDAOImpl.java
        │   ├── service/             # Logique métier
        │   │   ├── UserService.java
        │   │   ├── MatchService.java
        │   │   ├── TicketService.java
        │   │   ├── StadiumService.java
        │   │   └── ReservationService.java
        │   ├── view/                # Interfaces graphiques
        │   │   ├── LoginView.java
        │   │   ├── RegisterView.java
        │   │   ├── AdminView.java
        │   │   └── ClientView.java
        │   └── util/                # Utilitaires
        │       ├── HibernateUtil.java
        │       └── CsvExporter.java
        └── resources/
            ├── hibernate.cfg.xml    # Configuration Hibernate
            ├── application.properties
            ├── simplelogger.properties
            └── styles/
                └── main.css         # Styles JavaFX
```

---

## ⚙️ Configuration

### Prérequis
- **Java JDK 17** ou supérieur
- **Maven 3.9+**
- **512 MB RAM** minimum

### Installation

1. **Cloner le projet**
```bash
git clone https://github.com/votre-username/worldcup-tickets.git
cd worldcup-tickets/demo
```

2. **Compiler le projet**
```bash
mvn clean compile
```

3. **Lancer l'application**
```bash
mvn exec:java -Dexec.mainClass=com.example.Launcher
```

### Alternative : Exécuter avec le plugin JavaFX
```bash
mvn javafx:run
```

---

## 🔧 Configuration Hibernate

Fichier : `src/main/resources/hibernate.cfg.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-configuration PUBLIC
    "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
    "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">

<hibernate-configuration>
    <session-factory>
        <!-- SQLite Connection -->
        <property name="hibernate.connection.driver_class">org.sqlite.JDBC</property>
        <property name="hibernate.connection.url">jdbc:sqlite:worldcup_tickets.db</property>
        
        <!-- SQLite Dialect -->
        <property name="hibernate.dialect">org.hibernate.community.dialect.SQLiteDialect</property>
        
        <!-- Pool -->
        <property name="hibernate.connection.pool_size">1</property>
        
        <!-- Schema Management -->
        <property name="hibernate.hbm2ddl.auto">update</property>
        
        <!-- SQL Logging -->
        <property name="hibernate.show_sql">true</property>
        
        <!-- Entity Mappings -->
        <mapping class="com.example.entity.User"/>
        <mapping class="com.example.entity.Stadium"/>
        <mapping class="com.example.entity.Match"/>
        <mapping class="com.example.entity.Ticket"/>
        <mapping class="com.example.entity.Reservation"/>
    </session-factory>
</hibernate-configuration>
```

---

## 📦 Dépendances Maven (pom.xml)

```xml
<dependencies>
    <!-- JavaFX -->
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>21.0.1</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-fxml</artifactId>
        <version>21.0.1</version>
    </dependency>

    <!-- Hibernate ORM -->
    <dependency>
        <groupId>org.hibernate.orm</groupId>
        <artifactId>hibernate-core</artifactId>
        <version>6.4.4.Final</version>
    </dependency>

    <!-- SQLite JDBC -->
    <dependency>
        <groupId>org.xerial</groupId>
        <artifactId>sqlite-jdbc</artifactId>
        <version>3.45.1.0</version>
    </dependency>

    <!-- Hibernate SQLite Dialect -->
    <dependency>
        <groupId>org.hibernate.orm</groupId>
        <artifactId>hibernate-community-dialects</artifactId>
        <version>6.4.4.Final</version>
    </dependency>

    <!-- Hibernate Validator -->
    <dependency>
        <groupId>org.hibernate.validator</groupId>
        <artifactId>hibernate-validator</artifactId>
        <version>8.0.1.Final</version>
    </dependency>

    <!-- Jakarta Validation API -->
    <dependency>
        <groupId>jakarta.validation</groupId>
        <artifactId>jakarta.validation-api</artifactId>
        <version>3.0.2</version>
    </dependency>

    <!-- Apache Commons CSV -->
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-csv</artifactId>
        <version>1.10.0</version>
    </dependency>

    <!-- JUnit 5 -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.10.0</version>
        <scope>test</scope>
    </dependency>

    <!-- SLF4J Logger -->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-simple</artifactId>
        <version>2.0.9</version>
    </dependency>
</dependencies>
```

---

## 👤 Comptes par Défaut

| Rôle | Email | Mot de passe |
|------|-------|--------------|
| **Admin** | admin@worldcup.com | admin123 |
| **Client** | client@test.com | client123 |

---

## 🎯 Fonctionnalités

### Module Admin
- ✅ Dashboard avec statistiques temps réel
- ✅ Gestion des stades (CRUD)
- ✅ Gestion des matchs (CRUD)
- ✅ Génération de billets (VIP, Standard, Economy)
- ✅ Gestion des utilisateurs
- ✅ Statistiques et revenus
- ✅ Export CSV

### Module Client
- ✅ Consultation des matchs
- ✅ Recherche par équipe/stade
- ✅ Achat de billets
- ✅ Réservation de billets
- ✅ Historique des achats
- ✅ Gestion du profil

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Couche Présentation                       │
│              (LoginView, AdminView, ClientView)              │
└─────────────────────────┬───────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────┐
│                      Couche Service                          │
│    (UserService, MatchService, TicketService, etc.)         │
└─────────────────────────┬───────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────┐
│                       Couche DAO                             │
│         (GenericDAO, UserDAO, MatchDAO, etc.)               │
└─────────────────────────┬───────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────┐
│                  Couche Persistance                          │
│              (Hibernate ORM + SQLite)                        │
└─────────────────────────────────────────────────────────────┘
```

---

## 🎨 Design Patterns Utilisés

| Pattern | Classe | Description |
|---------|--------|-------------|
| **Singleton** | `HibernateUtil` | Instance unique de SessionFactory |
| **DAO** | `GenericDAO<T,ID>` | Abstraction de l'accès aux données |
| **Factory** | `TicketService` | Génération de billets |

---

## 📊 Streams Java - Exemples Clés

| Fichier | Lignes | Opérations |
|---------|--------|------------|
| `TicketService.java` | 58-61 | `stream() → filter() → collect()` |
| `TicketService.java` | 68-71 | `stream() → map() → reduce()` |
| `AdminView.java` | 151-155 | `stream() → filter() → map() → sum()` |
| `ClientView.java` | 223-228 | `stream() → filter() → toList()` |

---

## 🔗 Liens

| Ressource | Lien |
|-----------|------|
| **GitHub** | [À COMPLÉTER] |
| **Vidéo de présentation** | [À COMPLÉTER - Lien Drive] |
| **Rapport PDF** | `rapport/rapport_projet_v2.pdf` |

---

## 📚 Webographie

- [Documentation Oracle Java 17](https://docs.oracle.com/en/java/javase/17/)
- [Documentation JavaFX](https://openjfx.io/javadoc/21/)
- [Hibernate ORM Documentation](https://hibernate.org/orm/documentation/6.4/)
- [SQLite Documentation](https://www.sqlite.org/docs.html)
- [Maven Central Repository](https://mvnrepository.com/)
- [Baeldung - Java Tutorials](https://www.baeldung.com/)

---

## 📄 Licence

Ce projet a été réalisé dans le cadre du module Java Avancé - EMSI 4IIR 2025-2026.

---

## ✍️ Auteur

**Chouaib IKTACHE**  
Filière 4IIR - EMSI  
Encadré par : Pr. Abderrahim LARHLIMI

---

*Année Universitaire 2025-2026*
