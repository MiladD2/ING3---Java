package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Groupe implements Interprete {
    private String nom;
    private List<Artiste> membres;

    public Groupe(String nom) {
        this.nom = nom;
        this.membres = new ArrayList<>();
    }

    public void ajouterMembre(Artiste artiste) {
        membres.add(artiste);
    }

    @Override
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public List<Artiste> getMembres() { return membres; }
    public void setMembres(List<Artiste> membres) { this.membres = membres; }
}
