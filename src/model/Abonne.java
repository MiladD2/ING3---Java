package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Représente un utilisateur abonné du système.
 *
 * <p>Un abonné peut gérer ses playlists, consulter son historique
 * d'écoute et écouter des morceaux, sauf si son compte est suspendu.</p>
 */
public class Abonne extends Utilisateur {

    private final List<PlayList> playlists;
    private final Historique historique;
    private boolean suspendu;

    /**
     * Construit un nouvel abonné actif.
     *
     * @param identifiant l'identifiant de l'abonné
     * @param motDePasse le mot de passe de l'abonné
     */
    public Abonne(String identifiant, String motDePasse) {
        super(identifiant, motDePasse);
        this.playlists = new ArrayList<>();
        this.historique = new Historique();
        this.suspendu = false;
    }

    /**
     * Retourne le rôle de l'utilisateur.
     *
     * @return la chaîne {@code "ABONNE"}
     */
    @Override
    public String getRole() {
        return "ABONNE";
    }

    /**
     * Retourne les playlists de l'abonné.
     *
     * @return une vue non modifiable des playlists
     */
    public List<PlayList> getPlayLists() {
        return Collections.unmodifiableList(playlists);
    }

    /**
     * Retourne l'historique d'écoute de l'abonné.
     *
     * @return l'historique d'écoute
     */
    public Historique getHistorique() {
        return historique;
    }

    /**
     * Indique si le compte abonné est suspendu.
     *
     * @return {@code true} si le compte est suspendu, sinon {@code false}
     */
    public boolean estSuspendu() {
        return suspendu;
    }

    /**
     * Suspend le compte de l'abonné.
     */
    public void suspendre() {
        this.suspendu = true;
    }

    /**
     * Réactive le compte de l'abonné.
     */
    public void reactiver() {
        this.suspendu = false;
    }

    /**
     * Crée une nouvelle playlist pour l'abonné.
     *
     * @param nom le nom de la playlist
     * @return la playlist créée
     * @throws IllegalArgumentException si le nom est invalide ou déjà utilisé
     * @throws IllegalStateException si le compte est suspendu
     */
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

    /**
     * Supprime une playlist de l'abonné.
     *
     * @param nom le nom de la playlist à supprimer
     * @throws IllegalArgumentException si la playlist est introuvable
     * @throws IllegalStateException si le compte est suspendu
     */
    public void supprimerPlaylist(String nom) {
        verifierCompteActif();

        PlayList playlist = trouverPlaylistOuEchouer(nom);
        playlists.remove(playlist);
    }

    /**
     * Renomme une playlist existante.
     *
     * @param ancienNom l'ancien nom de la playlist
     * @param nouveauNom le nouveau nom de la playlist
     * @throws IllegalArgumentException si la playlist est introuvable
     *         ou si le nouveau nom est invalide ou déjà utilisé
     * @throws IllegalStateException si le compte est suspendu
     */
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

    /**
     * Ajoute un morceau à une playlist de l'abonné.
     *
     * @param nomPlaylist le nom de la playlist cible
     * @param morceau le morceau à ajouter
     * @throws IllegalArgumentException si la playlist est introuvable
     *         ou si le morceau est nul
     * @throws IllegalStateException si le compte est suspendu
     */
    public void ajouterMorceauAPlaylist(String nomPlaylist, Morceau morceau) {
        verifierCompteActif();

        if (morceau == null) {
            throw new IllegalArgumentException("Le morceau ne peut pas être null.");
        }

        PlayList playlist = trouverPlaylistOuEchouer(nomPlaylist);
        playlist.ajouterMorceau(morceau);
    }

    /**
     * Retire un morceau d'une playlist de l'abonné.
     *
     * @param nomPlaylist le nom de la playlist cible
     * @param morceau le morceau à retirer
     * @throws IllegalArgumentException si la playlist est introuvable
     *         ou si le morceau est nul
     * @throws IllegalStateException si le compte est suspendu
     */
    public void retirerMorceauDePlaylist(String nomPlaylist, Morceau morceau) {
        verifierCompteActif();

        if (morceau == null) {
            throw new IllegalArgumentException("Le morceau ne peut pas être null.");
        }

        PlayList playlist = trouverPlaylistOuEchouer(nomPlaylist);
        playlist.retirerMorceau(morceau);
    }

    /**
     * Enregistre l'écoute d'un morceau dans l'historique de l'abonné.
     *
     * @param morceau le morceau écouté
     * @throws IllegalArgumentException si le morceau est nul
     * @throws IllegalStateException si le compte est suspendu
     */
    public void enregistrerEcoute(Morceau morceau) {
        verifierCompteActif();

        if (morceau == null) {
            throw new IllegalArgumentException("Le morceau ne peut pas être null.");
        }

        historique.ajouterEcoute(morceau);
    }

    /**
     * Recherche une playlist par son nom.
     *
     * @param nom le nom recherché
     * @return la playlist correspondante, ou {@code null} si elle n'existe pas
     */
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