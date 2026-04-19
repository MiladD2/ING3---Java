package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Représente une playlist créée par un abonné.
 *
 * <p>Une playlist possède un identifiant, un nom
 * et une liste de morceaux sans doublons.</p>
 */
public class PlayList implements Serializable {
    private final int id;
    private String nom;
    private final List<Morceau> morceaux;

    /**
     * Construit une playlist.
     *
     * @param id l'identifiant de la playlist
     * @param nom le nom de la playlist
     * @throws IllegalArgumentException si l'identifiant est négatif
     *         ou si le nom est vide
     */
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

    /**
     * Retourne l'identifiant de la playlist.
     *
     * @return l'identifiant de la playlist
     */
    public int getId() {
        return id;
    }

    /**
     * Retourne le nom de la playlist.
     *
     * @return le nom de la playlist
     */
    public String getNom() {
        return nom;
    }

    /**
     * Modifie le nom de la playlist.
     *
     * @param nom le nouveau nom
     * @throws IllegalArgumentException si le nom est vide
     */
    public void setNom(String nom) {
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom de la playlist est obligatoire.");
        }
        this.nom = nom;
    }

    /**
     * Retourne les morceaux de la playlist.
     *
     * @return une vue non modifiable des morceaux
     */
    public List<Morceau> getMorceaux() {
        return Collections.unmodifiableList(morceaux);
    }

    /**
     * Ajoute un morceau à la playlist.
     *
     * @param morceau le morceau à ajouter
     * @throws IllegalArgumentException si le morceau est nul
     *         ou déjà présent dans la playlist
     */
    public void ajouterMorceau(Morceau morceau) {
        if (morceau == null) {
            throw new IllegalArgumentException("Le morceau ne peut pas etre null.");
        }
        if (morceaux.contains(morceau)) {
            throw new IllegalArgumentException("Le morceau est deja present dans la playlist.");
        }
        morceaux.add(morceau);
    }

    /**
     * Retire un morceau de la playlist.
     *
     * @param morceau le morceau à retirer
     * @throws IllegalArgumentException si le morceau est nul
     *         ou absent de la playlist
     */
    public void retirerMorceau(Morceau morceau) {
        if (morceau == null) {
            throw new IllegalArgumentException("Le morceau ne peut pas etre null.");
        }
        if (!morceaux.remove(morceau)) {
            throw new IllegalArgumentException("Le morceau n'existe pas dans la playlist.");
        }
    }

    /**
     * Indique si la playlist contient un morceau donné.
     *
     * @param morceau le morceau recherché
     * @return {@code true} si le morceau est présent, sinon {@code false}
     */
    public boolean contient(Morceau morceau) {
        if (morceau == null) {
            return false;
        }
        return morceaux.contains(morceau);
    }

    /**
     * Retourne la durée totale de la playlist en secondes.
     *
     * @return la durée totale de la playlist
     */
    public int getDureeTotale() {
        int total = 0;
        for (Morceau morceau : morceaux) {
            total += morceau.getDureeSecondes();
        }
        return total;
    }

    /**
     * Retourne la durée totale de la playlist au format minutes:secondes.
     *
     * @return la durée totale formatée
     */
    public String getDureeTotaleFormatee() {
        int total = getDureeTotale();
        int minutes = total / 60;
        int secondes = total % 60;
        return String.format("%d:%02d", minutes, secondes);
    }

    /**
     * Retourne le nombre de morceaux de la playlist.
     *
     * @return le nombre de morceaux
     */
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