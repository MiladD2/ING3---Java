package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PlayList implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int id;
    private String nom;
    private final List<Morceau> morceaux;

    public PlayList(int id, String nom) {
        if (id < 0) {
            throw new IllegalArgumentException("L'id ne peut pas etre negatif.");
        }
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom de la playlist est obligatoire.");
        }

        this.id = id;
        this.nom = nom;
        this.morceaux = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom de la playlist est obligatoire.");
        }
        this.nom = nom;
    }

    public List<Morceau> getMorceaux() {
        return Collections.unmodifiableList(morceaux);
    }

    public void ajouterMorceau(Morceau morceau) {
        if (morceau == null) {
            throw new IllegalArgumentException("Le morceau ne peut pas etre null.");
        }
        if (morceaux.contains(morceau)) {
            throw new IllegalArgumentException("Le morceau est deja present dans la playlist.");
        }
        morceaux.add(morceau);
    }

    public void retirerMorceau(Morceau morceau) {
        if (morceau == null) {
            throw new IllegalArgumentException("Le morceau ne peut pas etre null.");
        }
        if (!morceaux.remove(morceau)) {
            throw new IllegalArgumentException("Le morceau n'existe pas dans la playlist.");
        }
    }

    public boolean contient(Morceau morceau) {
        if (morceau == null) {
            return false;
        }
        return morceaux.contains(morceau);
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

    public int getNombreMorceaux() {
        return morceaux.size();
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", nbMorceaux=" + morceaux.size() +
                ", dureeTotale=" + getDureeTotaleFormatee() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayList playlist)) return false;
        return id == playlist.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}