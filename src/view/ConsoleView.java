package view;

import controller.MainController;
import model.*;
import java.util.Scanner;
import java.util.List;

public class ConsoleView {
    private final MainController controller;
    private final Scanner scanner;
    private boolean enCours = true;

    public ConsoleView(MainController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    public void demarrer() {
        while (enCours) {
            System.out.println("\n====================================");
            System.out.println("      JAVAZIK - MODE CONSOLE");
            System.out.println("====================================");
            System.out.println("1. Se connecter (Abonné)");
            System.out.println("2. Se connecter (Administrateur)");
            System.out.println("3. Créer un compte");
            System.out.println("4. Mode Visiteur");
            System.out.println("5. Quitter");
            System.out.print("\nVotre choix : ");

            String choix = scanner.nextLine();
            switch (choix) {
                case "1" -> menuLoginAbonne();
                case "2" -> menuLoginAdmin();
                case "3" -> menuInscription();
                case "4" -> { controller.continuerCommeVisiteur(); menuPrincipalVisiteur(); }
                case "5" -> { enCours = false; System.out.println("Au revoir !"); }
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    // --- AUTHENTICATION ---

    private void menuLoginAbonne() {
        System.out.print("Identifiant : ");
        String id = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String pwd = scanner.nextLine();

        if (controller.loginAbonne(id, pwd)) {
            System.out.println("Connexion réussie !");
            menuPrincipalAbonne();
        } else {
            System.out.println("Échec de connexion.");
        }
    }

    private void menuLoginAdmin() {
        System.out.print("Identifiant Admin : ");
        String id = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String pwd = scanner.nextLine();

        if (controller.loginAdministrateur(id, pwd)) {
            System.out.println("Mode Administrateur activé.");
            menuPrincipalAdmin();
        } else {
            System.out.println("Accès refusé.");
        }
    }

    private void menuInscription() {
        System.out.print("Nouvel identifiant : ");
        String id = scanner.nextLine();
        System.out.print("Nouveau mot de passe : ");
        String pwd = scanner.nextLine();
        try {
            controller.registerAbonne(id, pwd);
            System.out.println("Compte créé avec succès !");
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    // --- MENUS PRINCIPAUX ---

    private void menuPrincipalVisiteur() {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n--- MENU VISITEUR ---");
            System.out.println("1. Rechercher un morceau");
            System.out.println("2. Voir tout le catalogue");
            System.out.println("3. Retour");
            System.out.print("Choix : ");
            String choix = scanner.nextLine();
            switch (choix) {
                case "1" -> rechercherEtEcouter();
                case "2" -> afficherToutLeCatalogue();
                case "3" -> { controller.deconnecter(); retour = true; }
            }
        }
    }

    private void menuPrincipalAbonne() {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n--- MENU ABONNÉ (" + controller.getSystem().getAbonneConnecte().getIdentifiant() + ") ---");
            System.out.println("1. Rechercher / Écouter");
            System.out.println("2. Voir tout le catalogue");
            System.out.println("3. Mes Playlists");
            System.out.println("4. Mon Historique");
            System.out.println("5. Pour vous (Recommandations)");
            System.out.println("6. Déconnexion");
            System.out.print("Choix : ");
            String choix = scanner.nextLine();
            switch (choix) {
                case "1" -> rechercherEtEcouter();
                case "2" -> afficherToutLeCatalogue();
                case "3" -> menuGestionPlaylists();
                case "4" -> afficherHistorique();
                case "5" -> afficherRecommandations();
                case "6" -> { controller.deconnecter(); retour = true; }
            }
        }
    }

    private void menuPrincipalAdmin() {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n--- DASHBOARD ADMINISTRATEUR ---");
            System.out.println("1. Statistiques Globales");
            System.out.println("2. Voir tout le catalogue");
            System.out.println("3. Gérer les utilisateurs");
            System.out.println("4. Supprimer un morceau du catalogue");
            System.out.println("5. Déconnexion");
            System.out.print("Choix : ");
            String choix = scanner.nextLine();
            switch (choix) {
                case "1" -> afficherStatsAdmin();
                case "2" -> afficherToutLeCatalogue();
                case "3" -> menuGestionUtilisateurs();
                case "4" -> menuSuppressionCatalogue();
                case "5" -> { controller.deconnecter(); retour = true; }
            }
        }
    }

    // --- ACTIONS CATALOGUE ---

    private void rechercherEtEcouter() {
        System.out.print("Entrez votre recherche (titre) : ");
        String query = scanner.nextLine();
        List<Morceau> resultats = controller.rechercherMorceaux(query);

        if (resultats.isEmpty()) {
            System.out.println("Aucun résultat.");
        } else {
            for (int i = 0; i < resultats.size(); i++) {
                System.out.println((i + 1) + ". " + resultats.get(i).getTitre() + " [" + resultats.get(i).getInterprete() + "]");
            }
            System.out.print("Numéro du morceau à écouter (0 pour annuler) : ");
            try {
                int index = Integer.parseInt(scanner.nextLine());
                if (index > 0 && index <= resultats.size()) {
                    Morceau m = resultats.get(index - 1);
                    controller.ecouterMorceau(m);
                    simulerLecture(m);
                }
            } catch (Exception e) { System.out.println("Saisie invalide."); }
        }
    }

    private void afficherToutLeCatalogue() {
        List<Morceau> tous = controller.getTousLesMorceaux();
        System.out.println("\n--- TOUT LE CATALOGUE ---");
        if (tous.isEmpty()) {
            System.out.println("Le catalogue est vide.");
        } else {
            for (int i = 0; i < tous.size(); i++) {
                Morceau m = tous.get(i);
                System.out.println((i + 1) + ". " + m.getTitre() + " (" + m.getInterprete() + ")");
            }
            System.out.print("\nEntrez le numéro du morceau pour l'écouter (0 pour retour) : ");
            try {
                int index = Integer.parseInt(scanner.nextLine());
                if (index > 0 && index <= tous.size()) {
                    Morceau m = tous.get(index - 1);
                    controller.ecouterMorceau(m);
                    simulerLecture(m);
                }
            } catch (Exception e) {
                System.out.println("Choix invalide.");
            }
        }
    }

    private void simulerLecture(Morceau m) {
        System.out.println("\nLecture de : " + m.getTitre() + "...");
        int dureeSimulee = 10;
        for (int i = 0; i <= dureeSimulee; i++) {
            int pourcent = (i * 100) / dureeSimulee;
            String barre = "=".repeat(i) + " ".repeat(dureeSimulee - i);
            System.out.print("\r[" + barre + "] " + pourcent + "%");
            try { Thread.sleep(300); } catch (InterruptedException e) {}
        }
        System.out.println("\nLecture terminée !");
    }

    // --- GESTION ABONNÉ ---

    private void menuGestionPlaylists() {
        Abonne user = controller.getSystem().getAbonneConnecte();
        System.out.println("\n--- MES PLAYLISTS ---");
        List<PlayList> lists = user.getPlayLists();
        for (int i = 0; i < lists.size(); i++) {
            System.out.println((i + 1) + ". " + lists.get(i).getNom() + " (" + lists.get(i).getNombreMorceaux() + " titres)");
        }
        System.out.println("N. Créer une nouvelle playlist");
        System.out.println("0. Retour");
        System.out.print("Action : ");
        String choix = scanner.nextLine();
        if ("N".equalsIgnoreCase(choix)) {
            System.out.print("Nom de la playlist : ");
            controller.creerPlaylist(scanner.nextLine());
            System.out.println("Playlist créée !");
        }
    }

    private void afficherHistorique() {
        System.out.println("\n--- MON HISTORIQUE D'ÉCOUTE ---");
        List<Ecoute> history = controller.getSystem().getAbonneConnecte().getHistorique().getEcoutes();
        if (history.isEmpty()) System.out.println("Historique vide.");
        else {
            for (Ecoute e : history) System.out.println("- " + e.getMorceau().getTitre() + " [" + e.getMorceau().getInterprete() + "]");
        }
    }

    private void afficherRecommandations() {
        System.out.println("\n--- POUR VOUS (RECOMMANDATIONS) ---");
        List<Object> recs = controller.getRecommandations();
        if (recs.isEmpty()) System.out.println("Écoutez quelques morceaux pour recevoir des recommandations !");
        else {
            for (Object r : recs) {
                if (r instanceof Morceau) System.out.println("[Son] " + ((Morceau)r).getTitre() + " - " + ((Morceau)r).getInterprete());
            }
        }
    }

    // --- GESTION ADMIN ---

    private void afficherStatsAdmin() {
        System.out.println("\n====================================");
        System.out.println("   STATISTIQUES GLOBALES JAVAZIK");
        System.out.println("====================================");
        JavazikSystem sys = controller.getSystem();
        System.out.println("Utilisateurs : " + sys.getNombreAbonnes());
        System.out.println("Artistes     : " + sys.getCatalogue().getArtistes().size());
        System.out.println("Groupes      : " + sys.getCatalogue().getGroupes().size());
        System.out.println("Albums       : " + sys.getCatalogue().getAlbums().size());
        System.out.println("Morceaux     : " + sys.getCatalogue().getNombreMorceaux());
        
        int totalEcoutes = 0;
        for (Morceau m : sys.getCatalogue().getMorceaux()) totalEcoutes += m.getNombreEcoutes();
        System.out.println("Écoutes Totales : " + totalEcoutes);

        System.out.println("\n--- 🏆 PALMARÈS ---");
        Object topInt = controller.getTopInterprete();
        String nameInt = topInt == null ? "N/A" : (topInt instanceof Artiste ? ((Artiste)topInt).getNom() : ((Groupe)topInt).getNom());
        System.out.println("Top Artiste/Groupe : " + nameInt);
        System.out.println("Top Album          : " + (controller.getTopAlbum() != null ? controller.getTopAlbum().getTitre() : "N/A"));
        System.out.println("Top Morceau        : " + (controller.getTopMorceau() != null ? controller.getTopMorceau().getTitre() : "N/A"));
        System.out.println("====================================");
    }

    private void menuGestionUtilisateurs() {
        List<Abonne> abonnes = controller.getSystem().getAbonnes();
        System.out.println("\n--- LISTE DES UTILISATEURS ---");
        for (int i = 0; i < abonnes.size(); i++) {
            Abonne a = abonnes.get(i);
            System.out.println((i + 1) + ". " + a.getIdentifiant() + (a.estSuspendu() ? " [SUSPENDU]" : " [ACTIF]"));
        }
        System.out.print("Numéro pour suspendre/réactiver (0 pour retour) : ");
        try {
            int index = Integer.parseInt(scanner.nextLine());
            if (index > 0 && index <= abonnes.size()) {
                Abonne a = abonnes.get(index - 1);
                if (a.estSuspendu()) controller.reactiverUtilisateur(a.getIdentifiant());
                else controller.suspendreUtilisateur(a.getIdentifiant());
                System.out.println("Statut mis à jour.");
            }
        } catch (Exception e) {}
    }

    private void menuSuppressionCatalogue() {
        List<Morceau> morceaux = controller.getTousLesMorceaux();
        System.out.println("\n--- SUPPRESSION DE MORCEAU ---");
        for (int i = 0; i < morceaux.size(); i++) {
            System.out.println((i + 1) + ". " + morceaux.get(i).getTitre());
        }
        System.out.print("Numéro à supprimer définitivement (0 pour retour) : ");
        try {
            int index = Integer.parseInt(scanner.nextLine());
            if (index > 0 && index <= morceaux.size()) {
                controller.supprimerMorceau(morceaux.get(index - 1));
                System.out.println("Morceau supprimé du catalogue global.");
            }
        } catch (Exception e) {}
    }
}
