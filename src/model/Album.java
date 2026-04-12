package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Album {
    private final int id;
    private String titre;
    private int anneeSortie;
    private final List<Morceau> morceaux;
    private Artiste artiste;
    private Groupe groupe;

    public Album(int id, String titre, int anneeSortie) {
        if (id < 0) {
            throw new IllegalArgumentException("L'id ne peut pas etre negatif.");
        }
        if (titre == null || titre.isBlank()) {
            throw new IllegalArgumentException("Le titre de l'album est obligatoire.");
        }
        if (anneeSortie < 0) {
            throw new IllegalArgumentException("L'annee de sortie est invalide.");
        }

        this.id = id;
        this.titre = titre;
        this.anneeSortie = anneeSortie;
        this.morceaux = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        if (titre == null || titre.isBlank()) {
            throw new IllegalArgumentException("Le titre de l'album est obligatoire.");
        }
        this.titre = titre;
    }

    public int getAnneeSortie() {
        return anneeSortie;
    }

    public void setAnneeSortie(int anneeSortie) {
        if (anneeSortie < 0) {
            throw new IllegalArgumentException("L'annee de sortie est invalide.");
        }
        this.anneeSortie = anneeSortie;
    }

    public List<Morceau> getMorceaux() {
        return Collections.unmodifiableList(morceaux);
    }

    public Artiste getArtiste() {
        return artiste;
    }

    public Groupe getGroupe() {
        return groupe;
    }

    public void definirArtiste(Artiste artiste) {
        this.artiste = artiste;
        this.groupe = null;
    }

    public void definirGroupe(Groupe groupe) {
        this.groupe = groupe;
        this.artiste = null;
    }

    public void ajouterMorceau(Morceau morceau) {
        if (morceau == null) {
            throw new IllegalArgumentException("Le morceau ne peut pas etre null.");
        }
        if (morceaux.contains(morceau)) {
            throw new IllegalArgumentException("Le morceau est deja present dans l'album.");
        }
        morceaux.add(morceau);
    }

    public void retirerMorceau(Morceau morceau) {
        if (morceau == null) {
            throw new IllegalArgumentException("Le morceau ne peut pas etre null.");
        }
        if (!morceaux.remove(morceau)) {
            throw new IllegalArgumentException("Le morceau n'existe pas dans l'album.");
        }
    }

    public int getDureeTotale() {
        int total = 0;
        for (Morceau morceau : morceaux) {
            total += morceau.getDureeSecondes();
        }
        return total;
    }

    public String getDureeTotaleFormatee() {
        int total = getDureeTotale();
        int minutes = total / 60;
        int secondes = total % 60;
        return String.format("%d:%02d", minutes, secondes);
    }

    public String getInterprete() {
        if (artiste != null) {
            return artiste.getNom();
        }
        if (groupe != null) {
            return groupe.getNom();
        }
        return "Inconnu";
    }

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", anneeSortie=" + anneeSortie +
                ", interprete='" + getInterprete() + '\'' +
                ", nbMorceaux=" + morceaux.size() +
                ", dureeTotale=" + getDureeTotaleFormatee() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Album album)) return false;
        return id == album.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}