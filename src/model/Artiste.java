package model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Représente un artiste solo du catalogue musical.
 *
 * <p>Un artiste possède un identifiant, un nom, une biographie
 * et peut éventuellement appartenir à un groupe.</p>
 */
public class Artiste implements Serializable {
    private final int id;
    private String nom;
    private String biographie;
    private Groupe groupe; // null si artiste solo

    /**
     * Construit un artiste avec un identifiant, un nom et une biographie.
     *
     * @param id l'identifiant de l'artiste
     * @param nom le nom de l'artiste
     * @param biographie la biographie de l'artiste
     * @throws IllegalArgumentException si l'identifiant est négatif
     *         ou si le nom est vide
     */
    public Artiste(int id, String nom, String biographie) {
        if (id < 0) {
            throw new IllegalArgumentException("L'id ne peut pas être negatif.");
        }
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom de l'artiste est obligatoire.");
        }

        this.id = id;
        this.nom = nom;
        this.biographie = biographie == null ? "" : biographie;
    }

    /**
     * Construit un artiste à partir de son nom uniquement.
     *
     * @param nom le nom de l'artiste
     * @throws IllegalArgumentException si le nom est vide
     */
    public Artiste(String nom) {
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom de l'artiste est obligatoire.");
        }

        this.id = 0;
        this.nom = nom;
        this.biographie = "zebi";
    }

    /**
     * Retourne l'identifiant de l'artiste.
     *
     * @return l'identifiant de l'artiste
     */
    public int getId() {
        return id;
    }

    /**
     * Retourne le nom de l'artiste.
     *
     * @return le nom de l'artiste
     */
    public String getNom() {
        return nom;
    }

    /**
     * Modifie le nom de l'artiste.
     *
     * @param nom le nouveau nom
     * @throws IllegalArgumentException si le nom est vide
     */
    public void setNom(String nom) {
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom de l'artiste est obligatoire.");
        }
        this.nom = nom;
    }

    /**
     * Retourne la biographie de l'artiste.
     *
     * @return la biographie de l'artiste
     */
    public String getBiographie() {
        return biographie;
    }

    /**
     * Modifie la biographie de l'artiste.
     *
     * @param biographie la nouvelle biographie
     */
    public void setBiographie(String biographie) {
        this.biographie = biographie == null ? "" : biographie;
    }

    /**
     * Retourne le groupe auquel appartient l'artiste.
     *
     * @return le groupe associé, ou {@code null} si aucun groupe n'est défini
     */
    public Groupe getGroupe() {
        return groupe;
    }

    /**
     * Définit le groupe de l'artiste.
     *
     * @param groupe le groupe à associer
     */
    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }

    @Override
    public String toString() {
        return "Artiste{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", groupe=" + (groupe != null ? groupe.getNom() : "aucun") +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Artiste artiste)) return false;
        return id == artiste.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}