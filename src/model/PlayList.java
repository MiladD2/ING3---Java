package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayList implements Serializable {
    private String titre;
    private Abonne proprietaire;
    private List<Morceau> morceaux;

    public PlayList(String titre, Abonne proprietaire) {
        this.titre = titre;
        this.proprietaire = proprietaire;
        this.morceaux = new ArrayList<>();
    }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public Abonne getProprietaire() { return proprietaire; }
    public void setProprietaire(Abonne proprietaire) { this.proprietaire = proprietaire; }
    public List<Morceau> getMorceaux() { return morceaux; }
    public void setMorceaux(List<Morceau> morceaux) { this.morceaux = morceaux; }
}
