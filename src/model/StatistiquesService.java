package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StatistiquesService implements Serializable {
    private static final long serialVersionUID = 1L;

    private final JavazikSystem systeme;

    public StatistiquesService(JavazikSystem systeme) {
        if (systeme == null) {
            throw new IllegalArgumentException("Le système ne peut pas être nul.");
        }
        this.systeme = systeme;
    }

    public int getNombreUtilisateurs() {
        return systeme.getNombreUtilisateurs();
    }

    public int getNombreAbonnes() {
        return systeme.getNombreAbonnes();
    }

    public int getNombreAdministrateurs() {
        return systeme.getNombreAdministrateurs();
    }

    public int getNombreMorceaux() {
        return systeme.getCatalogue().getNombreMorceaux();
    }

    public int getNombreAlbums() {
        return systeme.getCatalogue().getNombreAlbums();
    }

    public int getNombreArtistes() {
        return systeme.getCatalogue().getNombreArtistes();
    }

    public int getNombreGroupes() {
        return systeme.getCatalogue().getNombreGroupes();
    }

    public int getNombreTotalEcoutes() {
        int total = 0;

        for (Morceau morceau : systeme.getCatalogue().getMorceaux()) {
            total += morceau.getNombreEcoutes();
        }

        return total;
    }

    public int getNombreEcoutesPourMorceau(Morceau morceau) {
        if (morceau == null) {
            throw new IllegalArgumentException("Le morceau ne peut pas être nul.");
        }
        return morceau.getNombreEcoutes();
    }

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

    public Map<Morceau, Integer> getEcoutesParMorceau() {
        Map<Morceau, Integer> resultat = new LinkedHashMap<>();

        for (Morceau morceau : getMorceauxTriesParEcoutes()) {
            resultat.put(morceau, morceau.getNombreEcoutes());
        }

        return resultat;
    }

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

    public List<Morceau> getTopMorceauxLesPlusEcoutes(int limite) {
        validerLimite(limite);

        List<Morceau> morceaux = getMorceauxTriesParEcoutes();

        if (limite >= morceaux.size()) {
            return morceaux;
        }

        return new ArrayList<>(morceaux.subList(0, limite));
    }

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