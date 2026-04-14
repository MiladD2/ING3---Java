import model.Album;
import model.Artiste;
import model.Groupe;
import model.Morceau;
import model.Historique;
import model.Ecoute;
import model.PlayList;
import model.Abonne;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== TESTS CLASSES DE BASE ===");

        testerArtiste();
        testerGroupe();
        testerMorceau();
        testerAlbum();
        testerExceptions();
        testerPlaylistEtHistorique();
        testerAbonne();

        System.out.println("\n=== FIN DES TESTS ===");
    }

    private static void testerArtiste() {
        System.out.println("\n--- Test Artiste ---");
        Artiste artiste = new Artiste(1, "Daft Punk Solo", "Artiste electro");
        System.out.println(artiste);

        artiste.setNom("Thomas Bangalter");
        artiste.setBiographie("Membre fondateur de Daft Punk");
        System.out.println("Nom modifie : " + artiste.getNom());
        System.out.println("Biographie modifiee : " + artiste.getBiographie());
    }

    private static void testerGroupe() {
        System.out.println("\n--- Test Groupe ---");
        Groupe groupe = new Groupe(10, "Daft Punk", "Groupe electro francais");
        Artiste artiste1 = new Artiste(2, "Thomas Bangalter", "Musicien");
        Artiste artiste2 = new Artiste(3, "Guy-Manuel de Homem-Christo", "Musicien");

        groupe.ajouterMembre(artiste1);
        groupe.ajouterMembre(artiste2);

        System.out.println(groupe);
        System.out.println("Membres du groupe :");
        for (Artiste artiste : groupe.getMembres()) {
            System.out.println("- " + artiste.getNom());
        }

        groupe.retirerMembre(artiste1);
        System.out.println("Apres retrait d'un membre : " + groupe);
    }

    private static void testerMorceau() {
        System.out.println("\n--- Test Morceau ---");
        Artiste artiste = new Artiste(4, "Stromae", "Artiste belge");
        Groupe groupe = new Groupe(11, "Coldplay", "Groupe pop rock");

        Morceau morceau1 = new Morceau(100, "Alors on danse", 217, "Electro");
        morceau1.definirArtiste(artiste);
        morceau1.incrementerNbEcoutes();
        morceau1.incrementerNbEcoutes();

        System.out.println(morceau1);
        System.out.println("Interprete : " + morceau1.getInterprete());
        System.out.println("Duree formatee : " + morceau1.getDureeFormatee());

        Morceau morceau2 = new Morceau(101, "Yellow", 269, "Rock");
        morceau2.definirGroupe(groupe);

        System.out.println(morceau2);
        System.out.println("Interprete : " + morceau2.getInterprete());
    }

    private static void testerAlbum() {
        System.out.println("\n--- Test Album ---");
        Groupe groupe = new Groupe(12, "Imagine Dragons", "Groupe pop rock");
        Album album = new Album(200, "Night Visions", 2012);
        album.definirGroupe(groupe);

        Morceau m1 = new Morceau(102, "Radioactive", 186, "Rock");
        Morceau m2 = new Morceau(103, "Demons", 177, "Rock");

        album.ajouterMorceau(m1);
        album.ajouterMorceau(m2);

        System.out.println(album);
        System.out.println("Interprete album : " + album.getInterprete());
        System.out.println("Nombre de morceaux : " + album.getMorceaux().size());
        System.out.println("Duree totale : " + album.getDureeTotaleFormatee());

        album.retirerMorceau(m2);
        System.out.println("Apres retrait d'un morceau : " + album);
    }

    private static void testerExceptions() {
        System.out.println("\n--- Test Exceptions ---");

        try {
            new Artiste(-1, "Test", "Bio");
            System.out.println("ERREUR : exception attendue non levee pour Artiste");
        } catch (IllegalArgumentException e) {
            System.out.println("OK Artiste id invalide : " + e.getMessage());
        }

        try {
            new Morceau(1, "", 200, "Pop");
            System.out.println("ERREUR : exception attendue non levee pour Morceau");
        } catch (IllegalArgumentException e) {
            System.out.println("OK Morceau titre invalide : " + e.getMessage());
        }

        try {
            Album album = new Album(1, "Test Album", 2024);
            Morceau morceau = new Morceau(1, "Titre", 180, "Pop");
            album.ajouterMorceau(morceau);
            album.ajouterMorceau(morceau);
            System.out.println("ERREUR : exception attendue non levee pour doublon album");
        } catch (IllegalArgumentException e) {
            System.out.println("OK doublon album : " + e.getMessage());
        }

        try {
            Album album = new Album(2, "Album 2", 2024);
            Morceau morceau = new Morceau(2, "Titre 2", 190, "Pop");
            album.retirerMorceau(morceau);
            System.out.println("ERREUR : exception attendue non levee pour retrait absent");
        } catch (IllegalArgumentException e) {
            System.out.println("OK retrait morceau absent : " + e.getMessage());
        }
    }

    private static void testerPlaylistEtHistorique() {
        System.out.println("\n--- Test Playlist et Historique ---");

        Morceau m1 = new Morceau(201, "Numb", 185, "Rock");
        Morceau m2 = new Morceau(202, "In The End", 216, "Rock");

        PlayList playlist = new PlayList(1, "Mes favoris");
        playlist.ajouterMorceau(m1);
        playlist.ajouterMorceau(m2);

        System.out.println(playlist);
        System.out.println("Contient Numb ? " + playlist.contient(m1));
        System.out.println("Nombre de morceaux : " + playlist.getNombreMorceaux());

        Historique historique = new Historique();
        historique.ajouterEcoute(m1);
        historique.ajouterEcoute(m2);

        System.out.println(historique);
        for (Ecoute ecoute : historique.getEcoutes()) {
            System.out.println(ecoute);
        }

        System.out.println("Ecoutes du morceau 1 : " + m1.getNombreEcoutes());
        System.out.println("Ecoutes du morceau 2 : " + m2.getNombreEcoutes());
    }

    private static void testerAbonne() {
        System.out.println("\n--- Test Abonne ---");

        Abonne abonne = new Abonne("alice", "1234");
        System.out.println("Identifiant : " + abonne.getIdentifiant());
        System.out.println("Role : " + abonne.getRole());
        System.out.println("Authentification correcte ? " + abonne.authentifier("1234"));
        System.out.println("Authentification incorrecte ? " + abonne.authentifier("xxxx"));
        System.out.println("Compte suspendu ? " + abonne.estSuspendu());

        abonne.changerMotDePasse("abcd");
        System.out.println("Authentification apres changement mdp ? " + abonne.authentifier("abcd"));

        abonne.creerPlaylist("Road Trip");
        abonne.creerPlaylist("Sport");

        System.out.println("Nombre de playlists : " + abonne.getPlayLists().size());
        System.out.println("Playlist 'Road Trip' trouvee ? " + (abonne.trouverPlaylistParNom("Road Trip") != null));
        System.out.println("Playlist 'Inconnue' trouvee ? " + (abonne.trouverPlaylistParNom("Inconnue") != null));

        Morceau m1 = new Morceau(301, "Believer", 204, "Rock");
        Morceau m2 = new Morceau(302, "Lose Yourself", 326, "Rap");

        abonne.ajouterMorceauAPlaylist("Road Trip", m1);
        abonne.ajouterMorceauAPlaylist("Road Trip", m2);

        System.out.println("Nombre de morceaux dans Road Trip : "
                + abonne.trouverPlaylistParNom("Road Trip").getNombreMorceaux());

        abonne.retirerMorceauDePlaylist("Road Trip", m2);
        System.out.println("Nombre de morceaux apres retrait : "
                + abonne.trouverPlaylistParNom("Road Trip").getNombreMorceaux());

        abonne.renommerPlaylist("Road Trip", "Voyage");
        System.out.println("Playlist renommee trouvee ? " + (abonne.trouverPlaylistParNom("Voyage") != null));

        abonne.enregistrerEcoute(m1);
        System.out.println("Nombre d'ecoutes du morceau : " + m1.getNombreEcoutes());
        System.out.println("Nombre d'ecoutes dans l'historique : " + abonne.getHistorique().getEcoutes().size());

        abonne.supprimerPlaylist("Sport");
        System.out.println("Nombre de playlists apres suppression : " + abonne.getPlayLists().size());

        abonne.suspendre();
        System.out.println("Compte suspendu apres suspension ? " + abonne.estSuspendu());

        try {
            abonne.creerPlaylist("Interdite");
            System.out.println("ERREUR : exception attendue non levee pour compte suspendu");
        } catch (IllegalStateException e) {
            System.out.println("OK creation impossible si compte suspendu : " + e.getMessage());
        }

        abonne.reactiver();
        System.out.println("Compte suspendu apres reactivation ? " + abonne.estSuspendu());

        abonne.creerPlaylist("Nouvelle Playlist");
        System.out.println("Nombre de playlists apres reactivation : " + abonne.getPlayLists().size());

        try {
            new Abonne("", "1234");
            System.out.println("ERREUR : exception attendue non levee pour identifiant vide");
        } catch (IllegalArgumentException e) {
            System.out.println("OK identifiant invalide : " + e.getMessage());
        }

        try {
            new Abonne("bob", "");
            System.out.println("ERREUR : exception attendue non levee pour mot de passe vide");
        } catch (IllegalArgumentException e) {
            System.out.println("OK mot de passe invalide : " + e.getMessage());
        }

        try {
            abonne.creerPlaylist("Voyage");
            System.out.println("ERREUR : exception attendue non levee pour doublon playlist");
        } catch (IllegalArgumentException e) {
            System.out.println("OK doublon playlist : " + e.getMessage());
        }

        try {
            abonne.renommerPlaylist("Voyage", "Nouvelle Playlist");
            System.out.println("ERREUR : exception attendue non levee pour renommage en doublon");
        } catch (IllegalArgumentException e) {
            System.out.println("OK renommage doublon : " + e.getMessage());
        }

        try {
            abonne.supprimerPlaylist("Introuvable");
            System.out.println("ERREUR : exception attendue non levee pour suppression playlist absente");
        } catch (IllegalArgumentException e) {
            System.out.println("OK suppression playlist absente : " + e.getMessage());
        }
    }
}