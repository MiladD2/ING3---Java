package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Représente une écoute d'un morceau.
 *
 * <p>Une écoute mémorise le morceau concerné
 * ainsi que la date et l'heure de l'écoute.</p>
 */
public class Ecoute implements Serializable {
    private final LocalDateTime dateHeure;
    private final Morceau morceau;

    /**
     * Construit une écoute avec la date et l'heure courantes.
     *
     * @param morceau le morceau écouté
     * @throws IllegalArgumentException si le morceau est nul
     */
    public Ecoute(Morceau morceau) {
        if (morceau == null) {
            throw new IllegalArgumentException("Le morceau ne peut pas etre null.");
        }
        this.morceau = morceau;
        this.dateHeure = LocalDateTime.now();
    }

    /**
     * Construit une écoute avec une date et une heure données.
     *
     * @param morceau le morceau écouté
     * @param dateHeure la date et l'heure de l'écoute
     * @throws IllegalArgumentException si le morceau ou la date sont nuls
     */
    public Ecoute(Morceau morceau, LocalDateTime dateHeure) {
        if (morceau == null) {
            throw new IllegalArgumentException("Le morceau ne peut pas etre null.");
        }
        if (dateHeure == null) {
            throw new IllegalArgumentException("La date d'ecoute ne peut pas etre null.");
        }
        this.morceau = morceau;
        this.dateHeure = dateHeure;
    }

    /**
     * Retourne la date et l'heure de l'écoute.
     *
     * @return la date et l'heure de l'écoute
     */
    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

    /**
     * Retourne le morceau écouté.
     *
     * @return le morceau écouté
     */
    public Morceau getMorceau() {
        return morceau;
    }

    @Override
    public String toString() {
        return "Ecoute{" +
                "dateHeure=" + dateHeure +
                ", morceau=" + morceau.getTitre() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ecoute ecoute)) return false;
        return Objects.equals(dateHeure, ecoute.dateHeure) &&
                Objects.equals(morceau, ecoute.morceau);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateHeure, morceau);
    }
}