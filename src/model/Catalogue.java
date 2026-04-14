package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Catalogue {

    private final List<Morceau> morceaux;
    private final List<Album> albums;
    private final List<Artiste> artistes;
    private final List<Groupe> groupes;

    public Catalogue() {
        this.morceaux = new ArrayList<>();
        this.albums = new ArrayList<>();
        this.artistes = new ArrayList<>();
        this.groupes = new ArrayList<>();
    }

    public List<Morceau> getMorceaux() {
        return Collections.unmodifiableList(morceaux);
    }

    public List<Album> getAlbums() {
        return Collections.unmodifiableList(albums);
    }

    public List<Artiste> getArtistes() {
        return Collections.unmodifiableList(artistes);
    }

    public List<Groupe> getGroupes() {
        return Collections.unmodifiableList(groupes);
    }

    public int getNombreMorceaux() {
        return morceaux.size();
    }

    public int getNombreAlbums() {
        return albums.size();
    }

    public int getNombreArtistes() {
        return artistes.size();
    }

    public int getNombreGroupes() {
        return groupes.size();
    }

    public boolean ajouterMorceau(Morceau morceau) {
        validerObjet(morceau, "Le morceau ne peut pas être nul.");
        if (morceaux.contains(morceau)) {
            return false;
        }
        return morceaux.add(morceau);
    }

    public boolean supprimerMorceau(Morceau morceau) {
        validerObjet(morceau, "Le morceau ne peut pas être nul.");
        return morceaux.remove(morceau);
    }

    public boolean ajouterAlbum(Album album) {
        validerObjet(album, "L'album ne peut pas être nul.");
        if (albums.contains(album)) {
            return false;
        }
        return albums.add(album);
    }

    public boolean supprimerAlbum(Album album) {
        validerObjet(album, "L'album ne peut pas être nul.");
        return albums.remove(album);
    }

    public boolean ajouterArtiste(Artiste artiste) {
        validerObjet(artiste, "L'artiste ne peut pas être nul.");
        if (artistes.contains(artiste)) {
            return false;
        }
        return artistes.add(artiste);
    }

    public boolean supprimerArtiste(Artiste artiste) {
        validerObjet(artiste, "L'artiste ne peut pas être nul.");
        return artistes.remove(artiste);
    }

    public boolean ajouterGroupe(Groupe groupe) {
        validerObjet(groupe, "Le groupe ne peut pas être nul.");
        if (groupes.contains(groupe)) {
            return false;
        }
        return groupes.add(groupe);
    }

    public boolean supprimerGroupe(Groupe groupe) {
        validerObjet(groupe, "Le groupe ne peut pas être nul.");
        return groupes.remove(groupe);
    }

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

    public List<Morceau> getMorceauxDeAlbum(Album album) {
        validerObjet(album, "L'album ne peut pas être nul.");
        return new ArrayList<>(album.getMorceaux());
    }

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