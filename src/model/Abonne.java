package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Abonne implements Serializable {
    private String login;
    private String password;
    private List<PlayList> playlists;
    private List<Morceau> historique;

    public Abonne(String login, String password) {
        this.login = login;
        this.password = password;
        this.playlists = new ArrayList<>();
        this.historique = new ArrayList<>();
    }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public List<PlayList> getPlaylists() { return playlists; }
    public void setPlaylists(List<PlayList> playlists) { this.playlists = playlists; }
    public List<Morceau> getHistorique() { return historique; }
    public void setHistorique(List<Morceau> historique) { this.historique = historique; }
}
