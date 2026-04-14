package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Album implements Serializable {
    private String titre;
    private Interprete interprete;
    private AlbumType type;
    private List<Morceau> morceaux;

    public Album(String titre, Interprete interprete, AlbumType type) {
        this.titre = titre;
        this.interprete = interprete;
        this.type = type;
        this.morceaux = new ArrayList<>();
    }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public Interprete getInterprete() { return interprete; }
    public void setInterprete(Interprete interprete) { this.interprete = interprete; }
    public AlbumType getType() { return type; }
    public void setType(AlbumType type) { this.type = type; }
    public List<Morceau> getMorceaux() { return morceaux; }
    public void setMorceaux(List<Morceau> morceaux) { this.morceaux = morceaux; }
}
