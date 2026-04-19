package controller;

import model.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Main Controller for the Javazik application.
 * Manages the interactions between the model and the JavaFX View.
 */
public class MainController {
    private JavazikSystem system;
    private static final String SAVE_FILE = "javazik_data.ser";

    public MainController() {
        this.system = new JavazikSystem();
        chargerDonnees();
    }

    public JavazikSystem getSystem() {
        return system;
    }

    // --- Authentication & Account Management ---

    public boolean loginAbonne(String id, String pwd) {
        try {
            system.connecterAbonne(id, pwd);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean loginAdministrateur(String id, String pwd) {
        try {
            system.connecterAdministrateur(id, pwd);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void registerAbonne(String id, String pwd) {
        system.creerCompteAbonne(id, pwd);
    }

    public void continuerCommeVisiteur() {
        system.continuerCommeVisiteur();
    }

    public void deconnecter() {
        system.deconnecter();
    }

    // --- Catalogue & Search ---

    public List<Morceau> rechercherMorceaux(String query) {
        return system.getCatalogue().rechercherMorceauxParTitre(query);
    }

    public List<Album> rechercherAlbums(String query) {
        return system.getCatalogue().rechercherAlbumsParTitre(query);
    }

    public List<Artiste> rechercherArtistes(String query) {
        return system.getCatalogue().rechercherArtistesParNom(query);
    }

    public List<Morceau> getTousLesMorceaux() {
        return system.getCatalogue().getMorceaux();
    }

    public List<Album> getTousLesAlbums() {
        return system.getCatalogue().getAlbums();
    }

    public List<Artiste> getTousLesArtistes() {
        return system.getCatalogue().getArtistes();
    }

    public List<Groupe> getTousLesGroupes() {
        return system.getCatalogue().getGroupes();
    }

    public List<Groupe> rechercherGroupes(String query) {
        return system.getCatalogue().rechercherGroupesParNom(query);
    }

    // --- ID Generation ---
    public int getNextArtisteId() {
        int max = 0;
        for (Artiste a : system.getCatalogue().getArtistes()) if (a.getId() > max) max = a.getId();
        return max + 1;
    }

    public int getNextGroupeId() {
        int max = 0;
        for (Groupe g : system.getCatalogue().getGroupes()) if (g.getId() > max) max = g.getId();
        return max + 1;
    }

    public int getNextAlbumId() {
        int max = 0;
        for (Album a : system.getCatalogue().getAlbums()) if (a.getId() > max) max = a.getId();
        return max + 1;
    }

    public int getNextMorceauId() {
        int max = 0;
        for (Morceau m : system.getCatalogue().getMorceaux()) if (m.getId() > max) max = m.getId();
        return max + 1;
    }

    // --- User Management ---
    public void suspendreUtilisateur(String id) {
        system.suspendreAbonne(id);
    }

    public void reactiverUtilisateur(String id) {
        system.reactiverAbonne(id);
    }

    public void supprimerUtilisateur(String id) {
        system.supprimerAbonne(id);
    }

    // --- Catalog Management (Addition) ---
    public void ajouterArtiste(String nom, String bio) {
        system.getCatalogue().ajouterArtiste(new Artiste(getNextArtisteId(), nom, bio));
    }

    public void ajouterGroupe(String nom, String desc) {
        system.getCatalogue().ajouterGroupe(new Groupe(getNextGroupeId(), nom, desc));
    }

    public void ajouterAlbum(String titre, int annee, Object interprete) {
        Album a = new Album(getNextAlbumId(), titre, annee);
        if (interprete instanceof Artiste) a.definirArtiste((Artiste)interprete);
        else if (interprete instanceof Groupe) a.definirGroupe((Groupe)interprete);
        system.getCatalogue().ajouterAlbum(a);
    }

    public void ajouterMorceau(String titre, int duree, String genre, Object interprete) {
        Morceau m = new Morceau(getNextMorceauId(), titre, duree, genre);
        if (interprete instanceof Artiste) m.definirArtiste((Artiste)interprete);
        else if (interprete instanceof Groupe) m.definirGroupe((Groupe)interprete);
        system.getCatalogue().ajouterMorceau(m);
    }

    // --- Catalog Management (Deletion) ---
    public void supprimerMorceau(Morceau m) {
        if (m == null) return;
        // 1. Retirer des playlists de TOUS les abonnés
        for (Abonne a : system.getAbonnes()) {
            for (PlayList p : a.getPlayLists()) {
                if (p.contient(m)) {
                    p.retirerMorceau(m);
                }
            }
        }
        // 2. Retirer de tous les albums
        for (Album al : system.getCatalogue().getAlbums()) {
            if (al.getMorceaux().contains(m)) {
                al.retirerMorceau(m);
            }
        }
        // 3. Retirer du catalogue
        system.getCatalogue().supprimerMorceau(m);
    }

    public void supprimerAlbum(Album a) {
        if (a == null) return;
        system.getCatalogue().supprimerAlbum(a);
    }

    public void supprimerArtiste(Artiste art) {
        if (art == null) return;
        // 1. Supprimer tous ses morceaux en cascade
        List<Morceau> morceaux = new ArrayList<>(system.getCatalogue().getMorceauxDeArtiste(art));
        for (Morceau m : morceaux) supprimerMorceau(m);
        
        // 2. Le retirer de son groupe s'il en a un
        if (art.getGroupe() != null) {
            Groupe g = art.getGroupe();
            g.getMembres().remove(art);
            if (g.getMembres().isEmpty()) supprimerGroupe(g);
        }
        // 3. Retirer du catalogue
        system.getCatalogue().supprimerArtiste(art);
    }

    public void supprimerGroupe(Groupe g) {
        if (g == null) return;
        // 1. Supprimer ses morceaux
        List<Morceau> morceaux = new ArrayList<>(system.getCatalogue().getMorceauxDeGroupe(g));
        for (Morceau m : morceaux) supprimerMorceau(m);
        
        // 2. Dissoudre le lien avec les membres
        // 3. Retirer du catalogue
        system.getCatalogue().supprimerGroupe(g);
    }

    // --- Listening ---

    public void ecouterMorceau(Morceau morceau) {
        if (system.estUnAbonneConnecte()) {
            system.getAbonneConnecte().enregistrerEcoute(morceau);
        } else if (system.estEnModeVisiteur()) {
            system.getVisiteurSession().ecouterMorceau(morceau);
        }
    }

    // --- Playlist Management ---

    public void creerPlaylist(String nom) {
        if (system.estUnAbonneConnecte()) {
            system.getAbonneConnecte().creerPlaylist(nom);
        }
    }

    public void supprimerPlaylist(String nom) {
        if (system.estUnAbonneConnecte()) {
            system.getAbonneConnecte().supprimerPlaylist(nom);
        }
    }

    public void renommerPlaylist(String ancienNom, String nouveauNom) {
        if (system.estUnAbonneConnecte()) {
            system.getAbonneConnecte().renommerPlaylist(ancienNom, nouveauNom);
        }
    }

    public void ajouterMorceauAPlaylist(String nomPlaylist, Morceau morceau) {
        if (system.estUnAbonneConnecte()) {
            system.getAbonneConnecte().ajouterMorceauAPlaylist(nomPlaylist, morceau);
        }
    }

    // --- Persistence ---

    public void sauvegarderDonnees() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(system);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void chargerDonnees() {
        File file = new File(SAVE_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                this.system = (JavazikSystem) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        
        if (system.getNombreAdministrateurs() == 0) {
            system.ajouterAdministrateur(new Administrateur("admin", "admin"));
        }

        if (system.getCatalogue().getNombreMorceaux() == 0) {
            Catalogue cat = system.getCatalogue();
            
            Groupe beatles = new Groupe(1, "The Beatles", "Groupe de rock britannique légendaire.");
            Artiste john = new Artiste(101, "John Lennon", "Chanteur et guitariste.");
            Artiste paul = new Artiste(102, "Paul McCartney", "Chanteur et bassiste.");
            Artiste george = new Artiste(103, "George Harrison", "Guitariste.");
            Artiste ringo = new Artiste(104, "Ringo Starr", "Batteur.");
            beatles.ajouterMembre(john); beatles.ajouterMembre(paul); 
            beatles.ajouterMembre(george); beatles.ajouterMembre(ringo);
            cat.ajouterGroupe(beatles);
            cat.ajouterArtiste(john); cat.ajouterArtiste(paul); 
            cat.ajouterArtiste(george); cat.ajouterArtiste(ringo);

            Album abbey = new Album(1, "Abbey Road", 1969);
            abbey.definirGroupe(beatles);
            String[] abbeyTracks = {"Come Together", "Something", "Maxwell's Silver Hammer", "Oh! Darling", "Octopus's Garden", "I Want You (She's So Heavy)", "Here Comes the Sun", "Because", "You Never Give Me Your Money", "Sun King", "Mean Mr. Mustard", "Polythene Pam", "She Came In Through the Bathroom Window", "Golden Slumbers", "Carry That Weight", "The End", "Her Majesty"};
            for(int i=0; i<abbeyTracks.length; i++) {
                Morceau m = new Morceau(100+i, abbeyTracks[i], 180 + (i*10%60), "Rock");
                m.definirGroupe(beatles);
                abbey.ajouterMorceau(m);
                cat.ajouterMorceau(m);
            }
            cat.ajouterAlbum(abbey);

            Groupe queen = new Groupe(2, "Queen", "Groupe de rock britannique iconique.");
            Artiste freddie = new Artiste(201, "Freddie Mercury", "Chanteur légendaire.");
            Artiste brian = new Artiste(202, "Brian May", "Guitariste virtuose.");
            queen.ajouterMembre(freddie); queen.ajouterMembre(brian);
            cat.ajouterGroupe(queen); cat.ajouterArtiste(freddie); cat.ajouterArtiste(brian);
            
            Album night = new Album(2, "A Night at the Opera", 1975);
            night.definirGroupe(queen);
            String[] queenTracks = {"Death on Two Legs", "Lazing on a Sunday Afternoon", "I'm in Love with My Car", "You're My Best Friend", "39", "Bohemian Rhapsody"};
            for(int i=0; i<queenTracks.length; i++) {
                Morceau m = new Morceau(200+i, queenTracks[i], 200 + (i*20), "Rock");
                m.definirGroupe(queen);
                night.ajouterMorceau(m);
                cat.ajouterMorceau(m);
            }
            cat.ajouterAlbum(night);

            Groupe daft = new Groupe(3, "Daft Punk", "Duo de musique électronique français.");
            Artiste thomas = new Artiste(301, "Thomas Bangalter", "Musicien.");
            Artiste guy = new Artiste(302, "Guy-Manuel de Homem-Christo", "Musicien.");
            daft.ajouterMembre(thomas); daft.ajouterMembre(guy);
            cat.ajouterGroupe(daft); cat.ajouterArtiste(thomas); cat.ajouterArtiste(guy);
            
            Morceau getLucky = new Morceau(3, "Get Lucky", 248, "Disco");
            getLucky.definirGroupe(daft);
            cat.ajouterMorceau(getLucky);

            Artiste mj = new Artiste(4, "Michael Jackson", "Le Roi de la Pop.");
            cat.ajouterArtiste(mj);
            Album thriller = new Album(3, "Thriller", 1982);
            thriller.definirArtiste(mj);
            String[] thrillerTracks = {"Wanna Be Startin' Somethin'", "Baby Be Mine", "The Girl Is Mine", "Thriller", "Beat It", "Billie Jean", "Human Nature", "P.Y.T.", "The Lady in My Life"};
            for(int i=0; i<thrillerTracks.length; i++) {
                Morceau m = new Morceau(400+i, thrillerTracks[i], 250, "Pop");
                m.definirArtiste(mj);
                thriller.ajouterMorceau(m);
                cat.ajouterMorceau(m);
            }
            cat.ajouterAlbum(thriller);

            Artiste adele = new Artiste(5, "Adele", "Chanteuse soul britannique.");
            cat.ajouterArtiste(adele);
            Morceau hello = new Morceau(5, "Hello", 295, "Soul");
            hello.definirArtiste(adele);
            cat.ajouterMorceau(hello);
            Morceau skyfall = new Morceau(6, "Skyfall", 286, "Soundtrack");
            skyfall.definirArtiste(adele);
            cat.ajouterMorceau(skyfall);
        }
    }
}
