package model;

import java.util.Objects;

public class Morceau {
    private final int id;
    private String titre;
    private int dureeSecondes;
    private String genre;
    private int nombreEcoutes;
    private Artiste artiste;
    private Groupe groupe;

    public Morceau(int id, String titre, int dureeSecondes, String genre) {
        if (id < 0) {
            throw new IllegalArgumentException("L'id ne peut pas etre negatif.");
        }
        if (titre == null || titre.isBlank()) {
            throw new IllegalArgumentException("Le titre du morceau est obligatoire.");
        }
        if (dureeSecondes <= 0) {
            throw new IllegalArgumentException("La duree doit etre strictement positive.");
        }

        this.id = id;
        this.titre = titre;
        this.dureeSecondes = dureeSecondes;
        this.genre = genre == null ? "" : genre;
        this.nombreEcoutes = 0;
    }

    public int getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        if (titre == null || titre.isBlank()) {
            throw new IllegalArgumentException("Le titre du morceau est obligatoire.");
        }
        this.titre = titre;
    }

    public int getDureeSecondes() {
        return dureeSecondes;
    }

    public void setDureeSecondes(int dureeSecondes) {
        if (dureeSecondes <= 0) {
            throw new IllegalArgumentException("La duree doit etre strictement positive.");
        }
        this.dureeSecondes = dureeSecondes;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre == null ? "" : genre;
    }

    public int getNombreEcoutes() {
        return nombreEcoutes;
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

    public void incrementerNbEcoutes() {
        nombreEcoutes++;
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

    public String getDureeFormatee() {
        int minutes = dureeSecondes / 60;
        int secondes = dureeSecondes % 60;
        return String.format("%d:%02d", minutes, secondes);
    }

    @Override
    public String toString() {
        return "Morceau{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", duree=" + getDureeFormatee() +
                ", genre='" + genre + '\'' +
                ", interprete='" + getInterprete() + '\'' +
                ", ecoutes=" + nombreEcoutes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Morceau morceau)) return false;
        return id == morceau.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}