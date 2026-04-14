package model;

import java.io.Serializable;

public class Artiste implements Interprete {
    private String nom;
    private Groupe groupe; // Facultatif

    public Artiste(String nom) {
        this.nom = nom;
    }

    public Artiste(String nom, Groupe groupe) {
        this.nom = nom;
        this.groupe = groupe;
    }

    @Override
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public Groupe getGroupe() { return groupe; }
    public void setGroupe(Groupe groupe) { this.groupe = groupe; }
}
