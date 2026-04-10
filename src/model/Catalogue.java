package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Catalogue implements Serializable {
    private List<Morceau> morceaux;
    private List<Album> albums;
    private List<Artiste> artistes;
    private List<Groupe> groupes;
    private List<Abonne> abonnes;

    public Catalogue() {
        this.morceaux = new ArrayList<>();
        this.albums = new ArrayList<>();
        this.artistes = new ArrayList<>();
        this.groupes = new ArrayList<>();
        this.abonnes = new ArrayList<>();
    }

    // Methods to add items
    public void ajouterMorceau(Morceau m) { morceaux.add(m); }
    public void ajouterAlbum(Album a) { albums.add(a); }
    public void ajouterArtiste(Artiste a) { artistes.add(a); }
    public void ajouterGroupe(Groupe g) { groupes.add(g); }
    public void ajouterAbonne(Abonne a) { abonnes.add(a); }

    // Getters
    public List<Morceau> getMorceaux() { return morceaux; }
    public List<Album> getAlbums() { return albums; }
    public List<Artiste> getArtistes() { return artistes; }
    public List<Groupe> getGroupes() { return groupes; }
    public List<Abonne> getAbonnes() { return abonnes; }
}
