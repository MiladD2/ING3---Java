package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Groupe implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int id;
    private String nom;
    private String description;
    private final List<Artiste> membres;

    /**
     * Construit un groupe avec un identifiant, un nom et une description.
     *
     * @param id l'identifiant du groupe
     * @param nom le nom du groupe
     * @param description la description du groupe
     * @throws IllegalArgumentException si l'identifiant est négatif
     *         ou si le nom est vide
     */
    public Groupe(int id, String nom, String description) {
        if (id < 0) {
            throw new IllegalArgumentException("L'id ne peut pas être negatif.");
        }
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom du groupe est obligatoire.");
        }

        this.id = id;
        this.nom = nom;
        this.description = description == null ? "" : description;
        this.membres = new ArrayList<>();
    }

    /**
     * Construit un groupe à partir de son nom uniquement.
     *
     * @param nom le nom du groupe
     * @throws IllegalArgumentException si le nom est vide
     */
    public Groupe(String nom) {
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom du groupe est obligatoire.");
        }

        this.id = 0;
        this.nom = nom;
        this.description = "zebi";
        this.membres = new ArrayList<>();
    }

    /**
     * Retourne l'identifiant du groupe.
     *
     * @return l'identifiant du groupe
     */
    public int getId() {
        return id;
    }

    /**
     * Retourne le nom du groupe.
     *
     * @return le nom du groupe
     */
    public String getNom() {
        return nom;
    }

    /**
     * Modifie le nom du groupe.
     *
     * @param nom le nouveau nom
     * @throws IllegalArgumentException si le nom est vide
     */
    public void setNom(String nom) {
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom du groupe est obligatoire.");
        }
        this.nom = nom;
    }

    /**
     * Retourne la description du groupe.
     *
     * @return la description du groupe
     */
    public String getDescription() {
        return description;
    }

    /**
     * Modifie la description du groupe.
     *
     * @param description la nouvelle description
     */
    public void setDescription(String description) {
        this.description = description == null ? "" : description;
    }

    /**
     * Retourne les membres du groupe.
     *
     * @return une vue non modifiable des membres
     */
    public List<Artiste> getMembres() {
        return Collections.unmodifiableList(membres);
    }

    /**
     * Ajoute un artiste au groupe.
     *
     * @param artiste l'artiste à ajouter
     * @throws IllegalArgumentException si l'artiste est nul
     */
    public void ajouterMembre(Artiste artiste) {
        if (artiste == null) {
            throw new IllegalArgumentException("L'artiste ne peut pas etre null.");
        }
        if (!membres.contains(artiste)) {
            membres.add(artiste);
            artiste.setGroupe(this);
        }
    }

    /**
     * Retire un artiste du groupe.
     *
     * @param artiste l'artiste à retirer
     * @throws IllegalArgumentException si l'artiste est nul
     */
    public void retirerMembre(Artiste artiste) {
        if (artiste == null) {
            throw new IllegalArgumentException("L'artiste ne peut pas etre null.");
        }
        if (membres.remove(artiste) && artiste.getGroupe() == this) {
            artiste.setGroupe(null);
        }
    }

    @Override
    public String toString() {
        return "Groupe{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", nbMembres=" + membres.size() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Groupe groupe)) return false;
        return id == groupe.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}