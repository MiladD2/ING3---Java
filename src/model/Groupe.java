package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Groupe {
    private final int id;
    private String nom;
    private String description;
    private final List<Artiste> membres;

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

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom du groupe est obligatoire.");
        }
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? "" : description;
    }

    public List<Artiste> getMembres() {
        return Collections.unmodifiableList(membres);
    }

    public void ajouterMembre(Artiste artiste) {
        if (artiste == null) {
            throw new IllegalArgumentException("L'artiste ne peut pas etre null.");
        }
        if (!membres.contains(artiste)) {
            membres.add(artiste);
            artiste.setGroupe(this);
        }
    }

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