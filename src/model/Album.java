package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Représente un album du catalogue musical.
 *
 * <p>Un album possède un identifiant, un titre, une année de sortie,
 * un interprète principal et une liste de morceaux.</p>
 */
public class Album implements Serializable {
    private final int id;
    private String titre;
    private int anneeSortie;
    private final List<Morceau> morceaux;
    private Artiste artiste;
    private Groupe groupe;

    /**
     * Construit un album avec son identifiant, son titre et son année de sortie.
     *
     * @param id l'identifiant de l'album
     * @param titre le titre de l'album
     * @param anneeSortie l'année de sortie de l'album
     * @throws IllegalArgumentException si l'identifiant est négatif,
     *         si le titre est vide ou si l'année de sortie est invalide
     */
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

    /**
     * Construit un album à partir de son titre uniquement.
     *
     * @param titre le titre de l'album
     * @throws IllegalArgumentException si le titre est vide
     */
    public Album(String titre) {
        if (titre == null || titre.isBlank()) {
            throw new IllegalArgumentException("Le titre de l'album est obligatoire.");
        }

        this.id = 0;
        this.titre = titre;
        this.anneeSortie = 0000;
        this.morceaux = new ArrayList<>();
    }

    /**
     * Retourne l'identifiant de l'album.
     *
     * @return l'identifiant de l'album
     */
    public int getId() {
        return id;
    }

    /**
     * Retourne le titre de l'album.
     *
     * @return le titre de l'album
     */
    public String getTitre() {
        return titre;
    }

    /**
     * Modifie le titre de l'album.
     *
     * @param titre le nouveau titre
     * @throws IllegalArgumentException si le titre est vide
     */
    public void setTitre(String titre) {
        if (titre == null || titre.isBlank()) {
            throw new IllegalArgumentException("Le titre de l'album est obligatoire.");
        }
        this.titre = titre;
    }

    /**
     * Retourne l'année de sortie de l'album.
     *
     * @return l'année de sortie
     */
    public int getAnneeSortie() {
        return anneeSortie;
    }

    /**
     * Modifie l'année de sortie de l'album.
     *
     * @param anneeSortie la nouvelle année
     * @throws IllegalArgumentException si l'année est invalide
     */
    public void setAnneeSortie(int anneeSortie) {
        if (anneeSortie < 0) {
            throw new IllegalArgumentException("L'annee de sortie est invalide.");
        }
        this.anneeSortie = anneeSortie;
    }

    /**
     * Retourne les morceaux de l'album.
     *
     * @return une vue non modifiable des morceaux
     */
    public List<Morceau> getMorceaux() {
        return Collections.unmodifiableList(morceaux);
    }

    /**
     * Retourne l'artiste associé à l'album.
     *
     * @return l'artiste associé, ou {@code null} si aucun artiste n'est défini
     */
    public Artiste getArtiste() {
        return artiste;
    }

    /**
     * Retourne le groupe associé à l'album.
     *
     * @return le groupe associé, ou {@code null} si aucun groupe n'est défini
     */
    public Groupe getGroupe() {
        return groupe;
    }

    /**
     * Définit l'artiste principal de l'album.
     *
     * @param artiste l'artiste à associer
     */
    public void definirArtiste(Artiste artiste) {
        this.artiste = artiste;
        this.groupe = null;
    }

    /**
     * Définit le groupe principal de l'album.
     *
     * @param groupe le groupe à associer
     */
    public void definirGroupe(Groupe groupe) {
        this.groupe = groupe;
        this.artiste = null;
    }

    /**
     * Ajoute un morceau à l'album.
     *
     * @param morceau le morceau à ajouter
     * @throws IllegalArgumentException si le morceau est nul
     *         ou déjà présent dans l'album
     */
    public void ajouterMorceau(Morceau morceau) {
        if (morceau == null) {
            throw new IllegalArgumentException("Le morceau ne peut pas etre null.");
        }
        if (morceaux.contains(morceau)) {
            throw new IllegalArgumentException("Le morceau est deja present dans l'album.");
        }
        morceaux.add(morceau);
    }

    /**
     * Retire un morceau de l'album.
     *
     * @param morceau le morceau à retirer
     * @throws IllegalArgumentException si le morceau est nul
     *         ou absent de l'album
     */
    public void retirerMorceau(Morceau morceau) {
        if (morceau == null) {
            throw new IllegalArgumentException("Le morceau ne peut pas etre null.");
        }
        if (!morceaux.remove(morceau)) {
            throw new IllegalArgumentException("Le morceau n'existe pas dans l'album.");
        }
    }

    /**
     * Calcule la durée totale de l'album en secondes.
     *
     * @return la durée totale en secondes
     */
    public int getDureeTotale() {
        int total = 0;
        for (Morceau morceau : morceaux) {
            total += morceau.getDureeSecondes();
        }
        return total;
    }

    /**
     * Retourne la durée totale de l'album au format minutes:secondes.
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
     * Retourne le nom de l'interprète principal de l'album.
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