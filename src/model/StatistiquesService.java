package model;

import java.io.Serializable;

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
}