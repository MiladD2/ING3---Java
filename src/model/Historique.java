package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Historique implements Serializable {
    private static final long serialVersionUID = 1L;
    private final List<Ecoute> ecoutes;

    /**
     * Construit un historique vide.
     */
    public Historique() {
        this.ecoutes = new ArrayList<>();
    }

    /**
     * Ajoute une écoute à l'historique et incrémente le nombre d'écoutes du morceau.
     *
     * @param morceau le morceau écouté
     * @throws IllegalArgumentException si le morceau est nul
     */
    public void ajouterEcoute(Morceau morceau) {
        if (morceau == null) {
            throw new IllegalArgumentException("Le morceau ne peut pas etre null.");
        }

        morceau.incrementerNbEcoutes();
        ecoutes.add(new Ecoute(morceau));
    }

    /**
     * Ajoute une écoute déjà construite à l'historique.
     *
     * @param ecoute l'écoute à ajouter
     * @throws IllegalArgumentException si l'écoute est nulle
     */
    public void ajouterEcoute(Ecoute ecoute) {
        if (ecoute == null) {
            throw new IllegalArgumentException("L'ecoute ne peut pas etre null.");
        }
        ecoutes.add(ecoute);
    }

    /**
     * Retourne les écoutes enregistrées.
     *
     * @return une vue non modifiable des écoutes
     */
    public List<Ecoute> getEcoutes() {
        return Collections.unmodifiableList(ecoutes);
    }

    /**
     * Retourne le nombre total d'écoutes enregistrées.
     *
     * @return le nombre d'écoutes
     */
    public int getNombreEcoutes() {
        return ecoutes.size();
    }

    /**
     * Indique si l'historique est vide.
     *
     * @return {@code true} si l'historique est vide, sinon {@code false}
     */
    public boolean estVide() {
        return ecoutes.isEmpty();
    }

    /**
     * Vide complètement l'historique.
     */
    public void vider() {
        ecoutes.clear();
    }

    @Override
    public String toString() {
        return "Historique{" +
                "nombreEcoutes=" + ecoutes.size() +
                '}';
    }
}