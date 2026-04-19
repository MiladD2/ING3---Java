package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Fournit les statistiques du système Javazik.
 *
 * <p>Cette classe calcule les statistiques simples et évoluées
 * à partir des données du catalogue et des utilisateurs.</p>
 */
public class StatistiquesService implements Serializable {
    private static final long serialVersionUID = 1L;

    private final JavazikSystem systeme;

    /**
     * Construit un service de statistiques associé à un système donné.
     *
     * @param systeme le système utilisé pour les calculs
     * @throws IllegalArgumentException si le système est nul
     */
    public StatistiquesService(JavazikSystem systeme) {
        if (systeme == null) {
            throw new IllegalArgumentException("Le système ne peut pas être nul.");
        }
        this.systeme = systeme;
    }

    /**
     * Retourne le nombre total d'utilisateurs.
     *
     * @return le nombre d'utilisateurs
     */
    public int getNombreUtilisateurs() {
        return systeme.getNombreUtilisateurs();
    }

    /**
     * Retourne le nombre d'abonnés.
     *
     * @return le nombre d'abonnés
     */
    public int getNombreAbonnes() {
        return systeme.getNombreAbonnes();
    }

    /**
     * Retourne le nombre d'administrateurs.
     *
     * @return le nombre d'administrateurs
     */
    public int getNombreAdministrateurs() {
        return systeme.getNombreAdministrateurs();
    }

    /**
     * Retourne le nombre de morceaux du catalogue.
     *
     * @return le nombre de morceaux
     */
    public int getNombreMorceaux() {
        return systeme.getCatalogue().getNombreMorceaux();
    }

    /**
     * Retourne le nombre d'albums du catalogue.
     *
     * @return le nombre d'albums
     */
    public int getNombreAlbums() {
        return systeme.getCatalogue().getNombreAlbums();
    }

    /**
     * Retourne le nombre d'artistes du catalogue.
     *
     * @return le nombre d'artistes
     */
    public int getNombreArtistes() {
        return systeme.getCatalogue().getNombreArtistes();
    }

    /**
     * Retourne le nombre de groupes du catalogue.
     *
     * @return le nombre de groupes
     */
    public int getNombreGroupes() {
        return systeme.getCatalogue().getNombreGroupes();
    }

    /**
     * Calcule le nombre total d'écoutes de tous les morceaux du catalogue.
     *
     * @return le nombre total d'écoutes
     */
    public int getNombreTotalEcoutes() {
        int total = 0;

        for (Morceau morceau : systeme.getCatalogue().getMorceaux()) {
            total += morceau.getNombreEcoutes();
        }

        return total;
    }

    /**
     * Retourne le nombre d'écoutes d'un morceau.
     *
     * @param morceau le morceau concerné
     * @return le nombre d'écoutes du morceau
     * @throws IllegalArgumentException si le morceau est nul
     */
    public int getNombreEcoutesPourMorceau(Morceau morceau) {
        if (morceau == null) {
            throw new IllegalArgumentException("Le morceau ne peut pas être nul.");
        }
        return morceau.getNombreEcoutes();
    }

    /**
     * Retourne le nombre total d'écoutes des morceaux d'un album.
     *
     * @param album l'album concerné
     * @return le nombre d'écoutes de l'album
     * @throws IllegalArgumentException si l'album est nul
     */
    public int getNombreEcoutesPourAlbum(Album album) {
        if (album == null) {
            throw new IllegalArgumentException("L'album ne peut pas être nul.");
        }

        int total = 0;
        for (Morceau morceau : album.getMorceaux()) {
            total += morceau.getNombreEcoutes();
        }
        return total;
    }

    /**
     * Retourne le nombre total d'écoutes des morceaux d'un artiste.
     *
     * @param artiste l'artiste concerné
     * @return le nombre d'écoutes de l'artiste
     * @throws IllegalArgumentException si l'artiste est nul
     */
    public int getNombreEcoutesPourArtiste(Artiste artiste) {
        if (artiste == null) {
            throw new IllegalArgumentException("L'artiste ne peut pas être nul.");
        }

        int total = 0;
        for (Morceau morceau : systeme.getCatalogue().getMorceaux()) {
            if (artiste.equals(morceau.getArtiste())) {
                total += morceau.getNombreEcoutes();
            }
        }
        return total;
    }

    /**
     * Retourne les écoutes classées par morceau.
     *
     * @return une map associant chaque morceau à son nombre d'écoutes,
     *         triée par ordre décroissant
     */
    public Map<Morceau, Integer> getEcoutesParMorceau() {
        Map<Morceau, Integer> resultat = new LinkedHashMap<>();

        for (Morceau morceau : getMorceauxTriesParEcoutes()) {
            resultat.put(morceau, morceau.getNombreEcoutes());
        }

        return resultat;
    }

    /**
     * Retourne les écoutes classées par album.
     *
     * @return une map associant chaque album à son nombre d'écoutes,
     *         triée par ordre décroissant
     */
    public Map<Album, Integer> getEcoutesParAlbum() {
        Map<Album, Integer> resultat = new LinkedHashMap<>();
        List<Album> albums = new ArrayList<>(systeme.getCatalogue().getAlbums());

        albums.sort((a1, a2) -> Integer.compare(
                getNombreEcoutesPourAlbum(a2),
                getNombreEcoutesPourAlbum(a1)
        ));

        for (Album album : albums) {
            resultat.put(album, getNombreEcoutesPourAlbum(album));
        }

        return resultat;
    }

    /**
     * Retourne les écoutes classées par artiste.
     *
     * @return une map associant chaque artiste à son nombre d'écoutes,
     *         triée par ordre décroissant
     */
    public Map<Artiste, Integer> getEcoutesParArtiste() {
        Map<Artiste, Integer> resultat = new LinkedHashMap<>();
        List<Artiste> artistes = new ArrayList<>(systeme.getCatalogue().getArtistes());

        artistes.sort((a1, a2) -> Integer.compare(
                getNombreEcoutesPourArtiste(a2),
                getNombreEcoutesPourArtiste(a1)
        ));

        for (Artiste artiste : artistes) {
            resultat.put(artiste, getNombreEcoutesPourArtiste(artiste));
        }

        return resultat;
    }

    /**
     * Retourne les morceaux les plus écoutés.
     *
     * @param limite le nombre maximal de morceaux à retourner
     * @return la liste des morceaux les plus écoutés
     * @throws IllegalArgumentException si la limite n'est pas strictement positive
     */
    public List<Morceau> getTopMorceauxLesPlusEcoutes(int limite) {
        validerLimite(limite);

        List<Morceau> morceaux = getMorceauxTriesParEcoutes();

        if (limite >= morceaux.size()) {
            return morceaux;
        }

        return new ArrayList<>(morceaux.subList(0, limite));
    }

    /**
     * Retourne les albums les plus écoutés.
     *
     * @param limite le nombre maximal d'albums à retourner
     * @return la liste des albums les plus écoutés
     * @throws IllegalArgumentException si la limite n'est pas strictement positive
     */
    public List<Album> getTopAlbumsLesPlusEcoutes(int limite) {
        validerLimite(limite);

        List<Album> albums = new ArrayList<>(systeme.getCatalogue().getAlbums());

        albums.sort((a1, a2) -> Integer.compare(
                getNombreEcoutesPourAlbum(a2),
                getNombreEcoutesPourAlbum(a1)
        ));

        if (limite >= albums.size()) {
            return albums;
        }

        return new ArrayList<>(albums.subList(0, limite));
    }

    /**
     * Retourne les artistes les plus écoutés.
     *
     * @param limite le nombre maximal d'artistes à retourner
     * @return la liste des artistes les plus écoutés
     * @throws IllegalArgumentException si la limite n'est pas strictement positive
     */
    public List<Artiste> getTopArtistesLesPlusEcoutes(int limite) {
        validerLimite(limite);

        List<Artiste> artistes = new ArrayList<>(systeme.getCatalogue().getArtistes());

        artistes.sort((a1, a2) -> Integer.compare(
                getNombreEcoutesPourArtiste(a2),
                getNombreEcoutesPourArtiste(a1)
        ));

        if (limite >= artistes.size()) {
            return artistes;
        }

        return new ArrayList<>(artistes.subList(0, limite));
    }

    private List<Morceau> getMorceauxTriesParEcoutes() {
        List<Morceau> morceaux = new ArrayList<>(systeme.getCatalogue().getMorceaux());
        morceaux.sort(Comparator.comparingInt(Morceau::getNombreEcoutes).reversed());
        return morceaux;
    }

    private void validerLimite(int limite) {
        if (limite <= 0) {
            throw new IllegalArgumentException("La limite doit être strictement positive.");
        }
    }
}