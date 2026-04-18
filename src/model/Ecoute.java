package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Ecoute implements Serializable {
    private static final long serialVersionUID = 1L;
    private final LocalDateTime dateHeure;
    private final Morceau morceau;

    public Ecoute(Morceau morceau) {
        if (morceau == null) {
            throw new IllegalArgumentException("Le morceau ne peut pas etre null.");
        }
        this.morceau = morceau;
        this.dateHeure = LocalDateTime.now();
    }

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

    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

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