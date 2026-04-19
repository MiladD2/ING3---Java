package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Catalogue implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Morceau> morceaux;
    private final List<Album> albums;
    private final List<Artiste> artistes;
    private final List<Groupe> groupes;

    /**
     * Construit un catalogue vide.
     */
    public Catalogue() {
        this.morceaux = new ArrayList<>();
        this.albums = new ArrayList<>();
        this.artistes = new ArrayList<>();
        this.groupes = new ArrayList<>();
    }

    /**
     * Retourne les morceaux du catalogue.
     *
     * @return une vue non modifiable des morceaux
     */
    public List<Morceau> getMorceaux() {
        return Collections.unmodifiableList(morceaux);
    }

    /**
     * Retourne les albums du catalogue.
     *
     * @return une vue non modifiable des albums
     */
    public List<Album> getAlbums() {
        return Collections.unmodifiableList(albums);
    }

    /**
     * Retourne les artistes du catalogue.
     *
     * @return une vue non modifiable des artistes
     */
    public List<Artiste> getArtistes() {
        return Collections.unmodifiableList(artistes);
    }

    /**
     * Retourne les groupes du catalogue.
     *
     * @return une vue non modifiable des groupes
     */
    public List<Groupe> getGroupes() {
        return Collections.unmodifiableList(groupes);
    }

    /**
     * Retourne le nombre de morceaux du catalogue.
     *
     * @return le nombre de morceaux
     */
    public int getNombreMorceaux() {
        return morceaux.size();
    }

    /**
     * Retourne le nombre d'albums du catalogue.
     *
     * @return le nombre d'albums
     */
    public int getNombreAlbums() {
        return albums.size();
    }

    /**
     * Retourne le nombre d'artistes du catalogue.
     *
     * @return le nombre d'artistes
     */
    public int getNombreArtistes() {
        return artistes.size();
    }

    /**
     * Retourne le nombre de groupes du catalogue.
     *
     * @return le nombre de groupes
     */
    public int getNombreGroupes() {
        return groupes.size();
    }

    /**
     * Ajoute un morceau au catalogue.
     *
     * @param morceau le morceau à ajouter
     * @return {@code true} si le morceau a été ajouté, sinon {@code false}
     * @throws IllegalArgumentException si le morceau est nul
     */
    public boolean ajouterMorceau(Morceau morceau) {
        validerObjet(morceau, "Le morceau ne peut pas être nul.");
        if (morceaux.contains(morceau)) {
            return false;
        }
        return morceaux.add(morceau);
    }

    /**
     * Supprime un morceau du catalogue.
     *
     * @param morceau le morceau à supprimer
     * @return {@code true} si le morceau a été supprimé, sinon {@code false}
     * @throws IllegalArgumentException si le morceau est nul
     */
    public boolean supprimerMorceau(Morceau morceau) {
        validerObjet(morceau, "Le morceau ne peut pas être nul.");
        return morceaux.remove(morceau);
    }

    /**
     * Ajoute un album au catalogue.
     *
     * @param album l'album à ajouter
     * @return {@code true} si l'album a été ajouté, sinon {@code false}
     * @throws IllegalArgumentException si l'album est nul
     */
    public boolean ajouterAlbum(Album album) {
        validerObjet(album, "L'album ne peut pas être nul.");
        if (albums.contains(album)) {
            return false;
        }
        return albums.add(album);
    }

    /**
     * Supprime un album du catalogue.
     *
     * @param album l'album à supprimer
     * @return {@code true} si l'album a été supprimé, sinon {@code false}
     * @throws IllegalArgumentException si l'album est nul
     */
    public boolean supprimerAlbum(Album album) {
        validerObjet(album, "L'album ne peut pas être nul.");
        return albums.remove(album);
    }

    /**
     * Ajoute un artiste au catalogue.
     *
     * @param artiste l'artiste à ajouter
     * @return {@code true} si l'artiste a été ajouté, sinon {@code false}
     * @throws IllegalArgumentException si l'artiste est nul
     */
    public boolean ajouterArtiste(Artiste artiste) {
        validerObjet(artiste, "L'artiste ne peut pas être nul.");
        if (artistes.contains(artiste)) {
            return false;
        }
        return artistes.add(artiste);
    }

    /**
     * Supprime un artiste du catalogue.
     *
     * @param artiste l'artiste à supprimer
     * @return {@code true} si l'artiste a été supprimé, sinon {@code false}
     * @throws IllegalArgumentException si l'artiste est nul
     */
    public boolean supprimerArtiste(Artiste artiste) {
        validerObjet(artiste, "L'artiste ne peut pas être nul.");
        return artistes.remove(artiste);
    }

    /**
     * Ajoute un groupe au catalogue.
     *
     * @param groupe le groupe à ajouter
     * @return {@code true} si le groupe a été ajouté, sinon {@code false}
     * @throws IllegalArgumentException si le groupe est nul
     */
    public boolean ajouterGroupe(Groupe groupe) {
        validerObjet(groupe, "Le groupe ne peut pas être nul.");
        if (groupes.contains(groupe)) {
            return false;
        }
        return groupes.add(groupe);
    }

    /**
     * Supprime un groupe du catalogue.
     *
     * @param groupe le groupe à supprimer
     * @return {@code true} si le groupe a été supprimé, sinon {@code false}
     * @throws IllegalArgumentException si le groupe est nul
     */
    public boolean supprimerGroupe(Groupe groupe) {
        validerObjet(groupe, "Le groupe ne peut pas être nul.");
        return groupes.remove(groupe);
    }

    /**
     * Recherche les morceaux dont le titre contient le texte fourni.
     *
     * @param recherche le texte recherché
     * @return la liste des morceaux correspondants
     * @throws IllegalArgumentException si la recherche est vide
     */
    public List<Morceau> rechercherMorceauxParTitre(String recherche) {
        validerRecherche(recherche);
        List<Morceau> resultats = new ArrayList<>();

        for (Morceau morceau : morceaux) {
            if (contientIgnoreCase(morceau.getTitre(), recherche)) {
                resultats.add(morceau);
            }
        }

        return resultats;
    }

    /**
     * Recherche les albums dont le titre contient le texte fourni.
     *
     * @param recherche le texte recherché
     * @return la liste des albums correspondants
     * @throws IllegalArgumentException si la recherche est vide
     */
    public List<Album> rechercherAlbumsParTitre(String recherche) {
        validerRecherche(recherche);
        List<Album> resultats = new ArrayList<>();

        for (Album album : albums) {
            if (contientIgnoreCase(album.getTitre(), recherche)) {
                resultats.add(album);
            }
        }

        return resultats;
    }

    /**
     * Recherche les artistes dont le nom contient le texte fourni.
     *
     * @param recherche le texte recherché
     * @return la liste des artistes correspondants
     * @throws IllegalArgumentException si la recherche est vide
     */
    public List<Artiste> rechercherArtistesParNom(String recherche) {
        validerRecherche(recherche);
        List<Artiste> resultats = new ArrayList<>();

        for (Artiste artiste : artistes) {
            if (contientIgnoreCase(artiste.getNom(), recherche)) {
                resultats.add(artiste);
            }
        }

        return resultats;
    }

    /**
     * Recherche les groupes dont le nom contient le texte fourni.
     *
     * @param recherche le texte recherché
     * @return la liste des groupes correspondants
     * @throws IllegalArgumentException si la recherche est vide
     */
    public List<Groupe> rechercherGroupesParNom(String recherche) {
        validerRecherche(recherche);
        List<Groupe> resultats = new ArrayList<>();

        for (Groupe groupe : groupes) {
            if (contientIgnoreCase(groupe.getNom(), recherche)) {
                resultats.add(groupe);
            }
        }

        return resultats;
    }

    /**
     * Retourne les morceaux d'un album.
     *
     * @param album l'album concerné
     * @return la liste des morceaux de l'album
     * @throws IllegalArgumentException si l'album est nul
     */
    public List<Morceau> getMorceauxDeAlbum(Album album) {
        validerObjet(album, "L'album ne peut pas être nul.");
        return new ArrayList<>(album.getMorceaux());
    }

    /**
     * Retourne les morceaux interprétés par un artiste.
     *
     * @param artiste l'artiste concerné
     * @return la liste des morceaux de l'artiste
     * @throws IllegalArgumentException si l'artiste est nul
     */
    public List<Morceau> getMorceauxDeArtiste(Artiste artiste) {
        validerObjet(artiste, "L'artiste ne peut pas être nul.");
        List<Morceau> resultats = new ArrayList<>();

        for (Morceau morceau : morceaux) {
            if (artiste.equals(morceau.getArtiste())) {
                resultats.add(morceau);
            }
        }

        return resultats;
    }

    /**
     * Retourne les morceaux interprétés par un groupe.
     *
     * @param groupe le groupe concerné
     * @return la liste des morceaux du groupe
     * @throws IllegalArgumentException si le groupe est nul
     */
    public List<Morceau> getMorceauxDeGroupe(Groupe groupe) {
        validerObjet(groupe, "Le groupe ne peut pas être nul.");
        List<Morceau> resultats = new ArrayList<>();

        for (Morceau morceau : morceaux) {
            if (groupe.equals(morceau.getGroupe())) {
                resultats.add(morceau);
            }
        }

        return resultats;
    }

    /**
     * Retourne les albums contenant au moins un morceau d'un artiste.
     *
     * @param artiste l'artiste concerné
     * @return la liste des albums trouvés
     * @throws IllegalArgumentException si l'artiste est nul
     */
    public List<Album> getAlbumsDeArtiste(Artiste artiste) {
        validerObjet(artiste, "L'artiste ne peut pas être nul.");
        List<Album> resultats = new ArrayList<>();

        for (Album album : albums) {
            if (albumContientMorceauArtiste(album, artiste)) {
                resultats.add(album);
            }
        }

        return resultats;
    }

    /**
     * Retourne les albums contenant au moins un morceau d'un groupe.
     *
     * @param groupe le groupe concerné
     * @return la liste des albums trouvés
     * @throws IllegalArgumentException si le groupe est nul
     */
    public List<Album> getAlbumsDeGroupe(Groupe groupe) {
        validerObjet(groupe, "Le groupe ne peut pas être nul.");
        List<Album> resultats = new ArrayList<>();

        for (Album album : albums) {
            if (albumContientMorceauGroupe(album, groupe)) {
                resultats.add(album);
            }
        }

        return resultats;
    }

    private boolean albumContientMorceauArtiste(Album album, Artiste artiste) {
        for (Morceau morceau : album.getMorceaux()) {
            if (artiste.equals(morceau.getArtiste())) {
                return true;
            }
        }
        return false;
    }

    private boolean albumContientMorceauGroupe(Album album, Groupe groupe) {
        for (Morceau morceau : album.getMorceaux()) {
            if (groupe.equals(morceau.getGroupe())) {
                return true;
            }
        }
        return false;
    }

    private boolean contientIgnoreCase(String texte, String recherche) {
        return texte != null && texte.toLowerCase().contains(recherche.toLowerCase().trim());
    }

    private void validerObjet(Object objet, String message) {
        if (objet == null) {
            throw new IllegalArgumentException(message);
        }
    }

    private void validerRecherche(String recherche) {
        if (recherche == null || recherche.trim().isEmpty()) {
            throw new IllegalArgumentException("La recherche ne peut pas être vide.");
        }
    }
}