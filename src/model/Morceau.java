package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Morceau implements Serializable {
    private String titre;
    private int duree; // en secondes
    private Interprete interprete;
    private List<Album> albums;
    private List<Evaluation> evaluations;

    public Morceau(String titre, int duree, Interprete interprete) {
        this.titre = titre;
        this.duree = duree;
        this.interprete = interprete;
        this.albums = new ArrayList<>();
        this.evaluations = new ArrayList<>();
    }

    public List<Evaluation> getEvaluations() { return evaluations; }
    public void setEvaluations(List<Evaluation> evaluations) { this.evaluations = evaluations; }
    public void ajouterEvaluation(Evaluation e) { evaluations.add(e); }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public int getDuree() { return duree; }
    public void setDuree(int duree) { this.duree = duree; }
    public Interprete getInterprete() { return interprete; }
    public void setInterprete(Interprete interprete) { this.interprete = interprete; }
    public List<Album> getAlbums() { return albums; }
    public void setAlbums(List<Album> albums) { this.albums = albums; }
}
