package model;

import java.io.Serializable;
import java.util.Objects;

public class Artiste implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int id;
    private String nom;
    private String biographie;
    private Groupe groupe; // null si artiste solo

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

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom de l'artiste est obligatoire.");
        }
        this.nom = nom;
    }

    public String getBiographie() {
        return biographie;
    }

    public void setBiographie(String biographie) {
        this.biographie = biographie == null ? "" : biographie;
    }

    public Groupe getGroupe() {
        return groupe;
    }

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