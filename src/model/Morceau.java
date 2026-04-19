package model;

import java.io.Serializable;
import java.util.Objects;

public class Morceau implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int id;
    private String titre;
    private int dureeSecondes;
    private String genre;
    private int nombreEcoutes;
    private Artiste artiste;
    private Groupe groupe;

    /**
     * Construit un morceau avec ses informations principales.
     *
     * @param id l'identifiant du morceau
     * @param titre le titre du morceau
     * @param dureeSecondes la durée du morceau en secondes
     * @param genre le genre musical
     * @throws IllegalArgumentException si l'identifiant est négatif,
     *         si le titre est vide ou si la durée n'est pas strictement positive
     */
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

    /**
     * Construit un morceau avec un titre, un identifiant et un artiste.
     *
     * @param titre le titre du morceau
     * @param id l'identifiant du morceau
     * @param artiste l'artiste du morceau
     */
    public Morceau(String titre, int id, Artiste artiste) {
        this.id = id;
        this.titre = titre;
        this.artiste = artiste;
    }

    /**
     * Retourne l'identifiant du morceau.
     *
     * @return l'identifiant du morceau
     */
    public int getId() {
        return id;
    }

    /**
     * Retourne le titre du morceau.
     *
     * @return le titre du morceau
     */
    public String getTitre() {
        return titre;
    }

    /**
     * Modifie le titre du morceau.
     *
     * @param titre le nouveau titre
     * @throws IllegalArgumentException si le titre est vide
     */
    public void setTitre(String titre) {
        if (titre == null || titre.isBlank()) {
            throw new IllegalArgumentException("Le titre du morceau est obligatoire.");
        }
        this.titre = titre;
    }

    /**
     * Retourne la durée du morceau en secondes.
     *
     * @return la durée du morceau
     */
    public int getDureeSecondes() {
        return dureeSecondes;
    }

    /**
     * Modifie la durée du morceau.
     *
     * @param dureeSecondes la nouvelle durée
     * @throws IllegalArgumentException si la durée n'est pas strictement positive
     */
    public void setDureeSecondes(int dureeSecondes) {
        if (dureeSecondes <= 0) {
            throw new IllegalArgumentException("La duree doit etre strictement positive.");
        }
        this.dureeSecondes = dureeSecondes;
    }

    /**
     * Retourne le genre musical du morceau.
     *
     * @return le genre du morceau
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Modifie le genre musical du morceau.
     *
     * @param genre le nouveau genre
     */
    public void setGenre(String genre) {
        this.genre = genre == null ? "" : genre;
    }

    /**
     * Retourne le nombre d'écoutes du morceau.
     *
     * @return le nombre d'écoutes
     */
    public int getNombreEcoutes() {
        return nombreEcoutes;
    }

    /**
     * Retourne l'artiste associé au morceau.
     *
     * @return l'artiste associé, ou {@code null} si aucun artiste n'est défini
     */
    public Artiste getArtiste() {
        return artiste;
    }

    /**
     * Retourne le groupe associé au morceau.
     *
     * @return le groupe associé, ou {@code null} si aucun groupe n'est défini
     */
    public Groupe getGroupe() {
        return groupe;
    }

    /**
     * Définit l'artiste interprète du morceau.
     *
     * @param artiste l'artiste à associer
     */
    public void definirArtiste(Artiste artiste) {
        this.artiste = artiste;
        this.groupe = null;
    }

    /**
     * Définit le groupe interprète du morceau.
     *
     * @param groupe le groupe à associer
     */
    public void definirGroupe(Groupe groupe) {
        this.groupe = groupe;
        this.artiste = null;
    }

    /**
     * Incrémente le nombre d'écoutes du morceau.
     */
    public void incrementerNbEcoutes() {
        nombreEcoutes++;
    }

    /**
     * Retourne le nom de l'interprète du morceau.
     *
     * @return le nom de l'artiste, du groupe, ou {@code "Inconnu"}
     */
    public String getInterprete() {
        if (artiste != null) {
            return artiste.getNom();
        }
        if (groupe != null) {
            return groupe.getNom();
        }
        return "Inconnu";
    }

    /**
     * Retourne la durée du morceau au format minutes:secondes.
     *
     * @return la durée formatée
     */
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