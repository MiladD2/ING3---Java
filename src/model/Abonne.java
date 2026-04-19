package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Abonne extends Utilisateur {

    private final List<PlayList> playlists;
    private final Historique historique;
    private boolean suspendu;

    public Abonne(String identifiant, String motDePasse) {
        super(identifiant, motDePasse);
        this.playlists = new ArrayList<>();
        this.historique = new Historique();
        this.suspendu = false;
    }

    @Override
    public String getRole() {
        return "ABONNE";
    }

    public List<PlayList> getPlayLists() {
        return Collections.unmodifiableList(playlists);
    }

    public Historique getHistorique() {
        return historique;
    }

    public boolean estSuspendu() {
        return suspendu;
    }

    public void suspendre() {
        this.suspendu = true;
    }

    public void reactiver() {
        this.suspendu = false;
    }

    public PlayList creerPlaylist(String nom) {
        verifierCompteActif();

        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom de la playlist ne peut pas être vide.");
        }

        if (trouverPlaylistParNom(nom) != null) {
            throw new IllegalArgumentException("Une playlist avec ce nom existe déjà.");
        }

        PlayList playlist = new PlayList(0, nom);
        playlists.add(playlist);
        return playlist;
    }

    public void supprimerPlaylist(String nom) {
        verifierCompteActif();

        PlayList playlist = trouverPlaylistOuEchouer(nom);
        playlists.remove(playlist);
    }

    public void renommerPlaylist(String ancienNom, String nouveauNom) {
        verifierCompteActif();

        if (nouveauNom == null || nouveauNom.isBlank()) {
            throw new IllegalArgumentException("Le nouveau nom de la playlist ne peut pas être vide.");
        }

        PlayList playlist = trouverPlaylistOuEchouer(ancienNom);

        PlayList autre = trouverPlaylistParNom(nouveauNom);
        if (autre != null && autre != playlist) {
            throw new IllegalArgumentException("Une playlist avec ce nom existe déjà.");
        }

        playlist.setNom(nouveauNom);
    }

    public void ajouterMorceauAPlaylist(String nomPlaylist, Morceau morceau) {
        verifierCompteActif();

        if (morceau == null) {
            throw new IllegalArgumentException("Le morceau ne peut pas être null.");
        }

        PlayList playlist = trouverPlaylistOuEchouer(nomPlaylist);
        playlist.ajouterMorceau(morceau);
    }

    public void retirerMorceauDePlaylist(String nomPlaylist, Morceau morceau) {
        verifierCompteActif();

        if (morceau == null) {
            throw new IllegalArgumentException("Le morceau ne peut pas être null.");
        }

        PlayList playlist = trouverPlaylistOuEchouer(nomPlaylist);
        playlist.retirerMorceau(morceau);
    }

    public void enregistrerEcoute(Morceau morceau) {
        verifierCompteActif();

        if (morceau == null) {
            throw new IllegalArgumentException("Le morceau ne peut pas être null.");
        }

        historique.ajouterEcoute(morceau);
    }

    public PlayList trouverPlaylistParNom(String nom) {
        if (nom == null || nom.isBlank()) {
            return null;
        }

        for (PlayList playlist : playlists) {
            if (playlist.getNom().equalsIgnoreCase(nom.trim())) {
                return playlist;
            }
        }

        return null;
    }

    private PlayList trouverPlaylistOuEchouer(String nom) {
        PlayList playlist = trouverPlaylistParNom(nom);
        if (playlist == null) {
            throw new IllegalArgumentException("Playlist introuvable : " + nom);
        }
        return playlist;
    }

    private void verifierCompteActif() {
        if (suspendu) {
            throw new IllegalStateException("Le compte abonné est suspendu.");
        }
    }

    @Override
    public String toString() {
        return "Abonne{" +
                "identifiant='" + getIdentifiant() + '\'' +
                ", suspendu=" + suspendu +
                ", nbPlaylists=" + playlists.size() +
                '}';
    }
}