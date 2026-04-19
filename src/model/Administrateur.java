package model;

import java.io.Serializable;

public class Administrateur extends Utilisateur implements Serializable {

    public Administrateur(String identifiant, String motDePasse) {
        super(identifiant, motDePasse);
    }

    @Override
    public String getRole() {
        return "ADMINISTRATEUR";
    }

    @Override
    public String toString() {
        return "Administrateur{" +
                "identifiant='" + getIdentifiant() + '\'' +
                '}';
    }
}