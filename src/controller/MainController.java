package controller;

import model.*;
import java.io.*;
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

    // --- Listening ---

    public void ecouterMorceau(Morceau morceau) {
        if (system.estUnAbonneConnecte()) {
            system.getAbonneConnecte().enregistrerEcoute(morceau);
        } else if (system.estEnModeVisiteur()) {
            system.getVisiteurSession().ecouterMorceau(morceau);
        }
        morceau.incrementerNbEcoutes();
    }

    // --- Playlist Management ---

    public void creerPlaylist(String nom) {
        if (system.estUnAbonneConnecte()) {
            system.getAbonneConnecte().creerPlaylist(nom);
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
        
        // Ensure at least one admin exists for testing
        if (system.getNombreAdministrateurs() == 0) {
            system.ajouterAdministrateur(new Administrateur("admin", "admin"));
        }

        // Add some demo data if catalogue is empty
        if (system.getCatalogue().getNombreMorceaux() == 0) {
            Catalogue cat = system.getCatalogue();
            Artiste beatles = new Artiste(1, "The Beatles", "Groupe légendaire");
            cat.ajouterArtiste(beatles);
            
            Morceau m1 = new Morceau(1, "Let It Be", 243, "Rock");
            m1.definirArtiste(beatles);
            cat.ajouterMorceau(m1);
            
            Morceau m2 = new Morceau(2, "Yesterday", 125, "Pop");
            m2.definirArtiste(beatles);
            cat.ajouterMorceau(m2);

            Artiste daft = new Artiste(2, "Daft Punk", "Electro");
            cat.ajouterArtiste(daft);
            Morceau m3 = new Morceau(3, "Get Lucky", 248, "Disco");
            m3.definirArtiste(daft);
            cat.ajouterMorceau(m3);
        }
    }
}
