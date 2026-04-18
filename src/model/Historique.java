package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Historique implements Serializable {
    private static final long serialVersionUID = 1L;
    private final List<Ecoute> ecoutes;

    public Historique() {
        this.ecoutes = new ArrayList<>();
    }

    public void ajouterEcoute(Morceau morceau) {
        if (morceau == null) {
            throw new IllegalArgumentException("Le morceau ne peut pas etre null.");
        }

        morceau.incrementerNbEcoutes();
        ecoutes.add(new Ecoute(morceau));
    }

    public void ajouterEcoute(Ecoute ecoute) {
        if (ecoute == null) {
            throw new IllegalArgumentException("L'ecoute ne peut pas etre null.");
        }
        ecoutes.add(ecoute);
    }

    public List<Ecoute> getEcoutes() {
        return Collections.unmodifiableList(ecoutes);
    }

    public int getNombreEcoutes() {
        return ecoutes.size();
    }

    public boolean estVide() {
        return ecoutes.isEmpty();
    }

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