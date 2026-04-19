# JAVAZIK - Music Streaming System

Projet d'Informatique POO - Java (ING3) - ECE Paris.
Une application de streaming musical inspirée de Spotify/Deezer, développée en Java avec JavaFX.

## 🚀 Fonctionnalités

### 👤 Visiteur
*   Consultation du catalogue (Artistes, Groupes, Albums, Morceaux).
*   Recherche filtrée.
*   Écoute limitée à 5 morceaux par session.

### 💎 Abonné
*   Écoutes illimitées.
*   Gestion de Playlists personnelles (Créer, Renommer, Ajouter/Retirer des titres, Supprimer).
*   Historique d'écoute complet.

### 🛠 Administrateur
*   **Gestion du Catalogue** : Ajout/Suppression en cascade (Artistes, Groupes, Albums, Morceaux).
*   **Gestion des Utilisateurs** : Liste des abonnés, suspension et suppression de comptes.
*   **Statistiques** : Dashboard automatisé (Nombre d'utilisateurs, artistes, morceaux, total des écoutes).

## 🛠 Installation & Lancement

### Pré-requis
*   **Java JDK 17** ou supérieur.
*   Visual Studio Code avec l'extension **Extension Pack for Java**.

### Lancement via VS Code (Recommandé)
1.  Ouvrir le dossier `ING3---Java` dans VS Code.
2.  Aller dans le fichier `src/Main.java`.
3.  Appuyer sur **F5** ou cliquer sur **Run**.
*La configuration est automatique grâce au fichier `.vscode/settings.json`.*

### Lancement via Ligne de Commande
Se placer à la racine du projet (`ING3---Java`) et exécuter :
```bash
java --module-path lib --add-modules javafx.controls,javafx.fxml,javafx.media -cp "lib/*:out" Main
```

## 🔐 Identifiants par défaut (Demo)
*   **Admin** : `admin` / `admin`
*   **Abonné** : Utilisez le bouton "S'inscrire" dans l'application pour créer votre compte.

## 💻 Compatibilité Système
Le projet est configuré pour être **Cross-Platform** (Mac & Windows).
*   Les bibliothèques `.dylib` sont pour macOS.
*   Les bibliothèques `.dll` sont pour Windows.
*Toutes les dépendances sont regroupées dans le dossier `lib/`.*

---
*Développé dans le cadre du module Programmation Orientée Objet (Java).*
