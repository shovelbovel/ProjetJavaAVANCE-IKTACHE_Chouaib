# Plan Type : Rapport de Projet Java Avancé
# 4IIR EMSI 2025-2026

## 1. Page de Garde
*   **Logo de l'établissement** (ex: EMSI).
*   **Titre du projet** (ex: "Application de Gestion des Étudiants").
*   **Intitulé du module** (ex: Java Avancé / Programmation Orientée Objet).
*   **Réalisé par :** (Noms et Prénoms des étudiants).
*   **Encadré par :** (Nom du professeur).
*   **Année Universitaire :** (ex: 2023-2024).

## 2. Remerciements (Optionnel)
Un court paragraphe pour remercier l'encadrant, l'administration de l'école et toute personne ayant aidé à la réalisation.

## 3. Table des Matières
Générée automatiquement (Word ou LaTeX) avec les numéros de pages corrects.

---

## 4. Introduction Générale
*   **Contexte du projet :** Dans quel cadre ce projet est-il réalisé ? (ex: Modernisation d'un système de gestion papier).
*   **Problématique :** Quel est le problème à résoudre ? (ex: Lenteur, perte de données, besoin de centralisation).
*   **Objectifs :** Liste des fonctionnalités principales attendues (ex: CRUD, Authentification, Reporting).

---

## 5. Partie I : Analyse et Conception
*Cette partie théorique valide la réflexion avant le codage.*

### 5.1 Spécification des besoins
*   **Besoins Fonctionnels :** Ce que le logiciel doit faire (ex: "Le système doit permettre d'ajouter un étudiant").
*   **Besoins Non-Fonctionnels :** Contraintes techniques (ex: Sécurité des mots de passe, temps de réponse < 1s, ergonomie).

### 5.2 Conception UML (Indispensable pour Java)
*   **Diagramme de Cas d'Utilisation (Use Case) :** Identifie les acteurs (Admin, Utilisateur) et leurs actions.
*   **Diagramme de Classes :** Le schéma le plus important pour Java. Il doit montrer :
    *   Les Entités (Attributs/Méthodes).
    *   Les Relations (Associations, Héritage, Composition).
    *   Les Interfaces.
*   **Diagramme de Séquence (Optionnel) :** Pour détailler un flux complexe (ex: Processus d'authentification ou de réservation).

### 5.3 Conception de la Base de Données
*   **Modèle Logique de Données (MLD) :** Schéma relationnel (Tables, Clés Primaires `PK`, Clés Étrangères `FK`).
*   **Dictionnaire de données :** Tableau décrivant les champs (Type, Taille, Contrainte).

---

## 6. Partie II : Environnement Technique
*Décrivez les outils utilisés avec précision.*

*   **Langage de programmation :** Java (Version, ex: JDK 21).
*   **Environnement de développement (IDE) :** IntelliJ IDEA, Eclipse ou NetBeans.
*   **Gestion de projet/Build :** Maven (montrer un extrait des dépendances clés du `pom.xml` comme le driver JDBC).
*   **Système de Gestion de Base de Données (SGBD) :** MySQL 8.0 / PostgreSQL.
*   **Outils de modélisation :** StarUML, UML Designer, ou MySQL Workbench.
*   **Bibliothèques tierces :** Lombok, iText (PDF), JFreeChart, etc.

---

## 7. Partie III : Architecture et Implémentation
*Le cœur technique du rapport. Ne copiez pas tout le code, seulement l'essentiel.*

### 7.1 Architecture logicielle
Expliquez l'organisation de vos packages (ex: Architecture en couches ou MVC).
*   `ma.emsi.model` (Les objets métiers / POJO).
*   `ma.emsi.dao` (Data Access Object - Gestion du SQL).
*   `ma.emsi.service` (Logique métier / Traitements).
*   `ma.emsi.ui` (Interface Graphique - Swing/JavaFX/Console).

### 7.2 Design Patterns (Patrons de conception)
Justifiez les patterns utilisés (très apprécié des correcteurs).
*   **Singleton :** Souvent utilisé pour l'instance unique de connexion à la BDD.
*   **DAO (Data Access Object) :** Pour isoler le code JDBC du reste de l'application.
*   **Factory ou Builder :** Si utilisés pour la création d'objets.

### 7.3 Extraits de code clés
Présentez des bouts de code intelligents avec des explications.
*   *Exemple :* La méthode générique de connexion.
*   *Exemple :* Une requête `PreparedStatement` complexe avec jointures.
*   *Exemple :* La gestion des exceptions (`try-catch-finally` ou `try-with-resources`).

---

## 8. Partie IV : Interface Utilisateur et Tests
*La preuve visuelle du fonctionnement.*

### 8.1 Présentation des interfaces (Captures d'écran)
Montrez les écrans principaux avec une légende explicative.
*   Page de Login.
*   Tableau de bord (Dashboard).
*   Formulaires d'ajout/modification.

### 8.2 Scénarios de Test
*   **Tests nominaux :** Cas où tout se passe bien (ex: Ajout réussi d'un étudiant).
*   **Tests d'erreurs :** Cas limites (ex: Tentative de connexion avec un mauvais mot de passe -> Affichage d'une `Alert` ou `JOptionPane`).

---

## 9. Conclusion et Perspectives
*   **Bilan technique :** Le cahier des charges est-il respecté ?
*   **Bilan personnel :** Compétences acquises (ex: Maîtrise de JDBC, gestion des conflits Maven, travail en équipe).
*   **Difficultés rencontrées :** Comment les avez-vous surmontées ?
*   **Perspectives / Améliorations futures :** Que feriez-vous avec plus de temps ? (ex: Version Web, Mobile, Sécurité avancée).

## 10. Webographie / Bibliographie
Liste des sources : Documentation Oracle, StackOverflow, Cours, Tutoriels YouTube.

---

### 💡 Conseils pour un bon rapport
1.  **Formatage du Code :** N'écrivez jamais du code comme du texte normal. Utilisez une police à chasse fixe (ex: *Courier New* ou *Consolas*) et la coloration syntaxique si possible.
2.  **Cohérence :** Le diagramme de classe UML doit correspondre exactement aux classes Java présentes dans le code.
3.  **Justification :** Ne dites pas "J'ai utilisé Maven". Dites "J'ai utilisé Maven **pour automatiser la gestion des dépendances et faciliter l'intégration du driver MySQL**".