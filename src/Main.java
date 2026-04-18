import model.*;

import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        JavazikSystem systeme = new JavazikSystem();
        Catalogue catalogue = systeme.getCatalogue();
        StatistiquesService stats = new StatistiquesService(systeme);

        System.out.println("==================================================");
        System.out.println("TEST COMPLET DES STATISTIQUES EVOLUEES");
        System.out.println("==================================================");

        Administrateur admin = new Administrateur("admin", "admin123");
        Abonne alice = new Abonne("alice", "1234");
        Abonne bob = new Abonne("bob", "1234");

        systeme.ajouterAdministrateur(admin);
        systeme.ajouterAbonne(alice);
        systeme.ajouterAbonne(bob);

        Artiste adele = new Artiste("Adele");
        Artiste stromae = new Artiste("Stromae");
        Artiste sia = new Artiste("Sia");

        Groupe coldplay = new Groupe("Coldplay");
        Groupe daftPunk = new Groupe("Daft Punk");

        catalogue.ajouterArtiste(adele);
        catalogue.ajouterArtiste(stromae);
        catalogue.ajouterArtiste(sia);

        catalogue.ajouterGroupe(coldplay);
        catalogue.ajouterGroupe(daftPunk);

        Morceau hello = new Morceau("Hello", 295, adele);
        Morceau easyOnMe = new Morceau("Easy On Me", 224, adele);

        Morceau papaoutai = new Morceau("Papaoutai", 238, stromae);
        Morceau formidable = new Morceau("Formidable", 214, stromae);

        Morceau chandelier = new Morceau("Chandelier", 216, sia);
        Morceau elasticHeart = new Morceau("Elastic Heart", 257, sia);

        catalogue.ajouterMorceau(hello);
        catalogue.ajouterMorceau(easyOnMe);
        catalogue.ajouterMorceau(papaoutai);
        catalogue.ajouterMorceau(formidable);
        catalogue.ajouterMorceau(chandelier);
        catalogue.ajouterMorceau(elasticHeart);

        Album albumAdele = new Album("Best of Adele");
        albumAdele.ajouterMorceau(hello);
        albumAdele.ajouterMorceau(easyOnMe);

        Album albumStromae = new Album("Racine Carree");
        albumStromae.ajouterMorceau(papaoutai);
        albumStromae.ajouterMorceau(formidable);

        Album albumSia = new Album("1000 Forms of Fear");
        albumSia.ajouterMorceau(chandelier);
        albumSia.ajouterMorceau(elasticHeart);

        catalogue.ajouterAlbum(albumAdele);
        catalogue.ajouterAlbum(albumStromae);
        catalogue.ajouterAlbum(albumSia);

        incrementerEcoutes(hello, 12);
        incrementerEcoutes(easyOnMe, 7);

        incrementerEcoutes(papaoutai, 20);
        incrementerEcoutes(formidable, 5);

        incrementerEcoutes(chandelier, 15);
        incrementerEcoutes(elasticHeart, 9);

        System.out.println();
        System.out.println("--------------------------------------------------");
        System.out.println("1) TESTS UNITAIRES DES COMPTEURS");
        System.out.println("--------------------------------------------------");

        System.out.println("Ecoutes de Hello : " + stats.getNombreEcoutesPourMorceau(hello) + " (attendu : 12)");
        System.out.println("Ecoutes de Easy On Me : " + stats.getNombreEcoutesPourMorceau(easyOnMe) + " (attendu : 7)");
        System.out.println("Ecoutes de Papaoutai : " + stats.getNombreEcoutesPourMorceau(papaoutai) + " (attendu : 20)");
        System.out.println("Ecoutes de Formidable : " + stats.getNombreEcoutesPourMorceau(formidable) + " (attendu : 5)");
        System.out.println("Ecoutes de Chandelier : " + stats.getNombreEcoutesPourMorceau(chandelier) + " (attendu : 15)");
        System.out.println("Ecoutes de Elastic Heart : " + stats.getNombreEcoutesPourMorceau(elasticHeart) + " (attendu : 9)");

        System.out.println();
        System.out.println("Ecoutes album Adele : " + stats.getNombreEcoutesPourAlbum(albumAdele) + " (attendu : 19)");
        System.out.println("Ecoutes album Stromae : " + stats.getNombreEcoutesPourAlbum(albumStromae) + " (attendu : 25)");
        System.out.println("Ecoutes album Sia : " + stats.getNombreEcoutesPourAlbum(albumSia) + " (attendu : 24)");

        System.out.println();
        System.out.println("Ecoutes artiste Adele : " + stats.getNombreEcoutesPourArtiste(adele) + " (attendu : 19)");
        System.out.println("Ecoutes artiste Stromae : " + stats.getNombreEcoutesPourArtiste(stromae) + " (attendu : 25)");
        System.out.println("Ecoutes artiste Sia : " + stats.getNombreEcoutesPourArtiste(sia) + " (attendu : 24)");

        System.out.println();
        System.out.println("Nombre total d'ecoutes : " + stats.getNombreTotalEcoutes() + " (attendu : 68)");

        System.out.println();
        System.out.println("--------------------------------------------------");
        System.out.println("2) TESTS DES MAPS CLASSEES");
        System.out.println("--------------------------------------------------");

        System.out.println();
        System.out.println("Ecoutes par morceau :");
        afficherEcoutesParMorceau(stats.getEcoutesParMorceau());

        System.out.println();
        System.out.println("Ecoutes par album :");
        afficherEcoutesParAlbum(stats.getEcoutesParAlbum());

        System.out.println();
        System.out.println("Ecoutes par artiste :");
        afficherEcoutesParArtiste(stats.getEcoutesParArtiste());

        System.out.println();
        System.out.println("--------------------------------------------------");
        System.out.println("3) TESTS DES TOPS");
        System.out.println("--------------------------------------------------");

        System.out.println();
        System.out.println("Top 3 morceaux les plus ecoutés :");
        afficherTopMorceaux(stats.getTopMorceauxLesPlusEcoutes(3), stats);

        System.out.println();
        System.out.println("Top 3 albums les plus ecoutés :");
        afficherTopAlbums(stats.getTopAlbumsLesPlusEcoutes(3), stats);

        System.out.println();
        System.out.println("Top 3 artistes les plus ecoutés :");
        afficherTopArtistes(stats.getTopArtistesLesPlusEcoutes(3), stats);

        System.out.println();
        System.out.println("--------------------------------------------------");
        System.out.println("4) TESTS DE LIMITES");
        System.out.println("--------------------------------------------------");

        System.out.println();
        System.out.println("Top 10 morceaux alors qu'il n'y en a que 6 :");
        afficherTopMorceaux(stats.getTopMorceauxLesPlusEcoutes(10), stats);

        System.out.println();
        System.out.println("Top 10 albums alors qu'il n'y en a que 3 :");
        afficherTopAlbums(stats.getTopAlbumsLesPlusEcoutes(10), stats);

        System.out.println();
        System.out.println("Top 10 artistes alors qu'il n'y en a que 3 :");
        afficherTopArtistes(stats.getTopArtistesLesPlusEcoutes(10), stats);

        System.out.println();
        System.out.println("--------------------------------------------------");
        System.out.println("5) TEST DES EXCEPTIONS");
        System.out.println("--------------------------------------------------");

        try {
            stats.getNombreEcoutesPourMorceau(null);
            System.out.println("ERREUR : exception attendue pour morceau null");
        } catch (IllegalArgumentException e) {
            System.out.println("OK getNombreEcoutesPourMorceau(null) -> " + e.getMessage());
        }

        try {
            stats.getNombreEcoutesPourAlbum(null);
            System.out.println("ERREUR : exception attendue pour album null");
        } catch (IllegalArgumentException e) {
            System.out.println("OK getNombreEcoutesPourAlbum(null) -> " + e.getMessage());
        }

        try {
            stats.getNombreEcoutesPourArtiste(null);
            System.out.println("ERREUR : exception attendue pour artiste null");
        } catch (IllegalArgumentException e) {
            System.out.println("OK getNombreEcoutesPourArtiste(null) -> " + e.getMessage());
        }

        try {
            stats.getTopMorceauxLesPlusEcoutes(0);
            System.out.println("ERREUR : exception attendue pour limite 0");
        } catch (IllegalArgumentException e) {
            System.out.println("OK getTopMorceauxLesPlusEcoutes(0) -> " + e.getMessage());
        }

        try {
            stats.getTopAlbumsLesPlusEcoutes(-1);
            System.out.println("ERREUR : exception attendue pour limite negative");
        } catch (IllegalArgumentException e) {
            System.out.println("OK getTopAlbumsLesPlusEcoutes(-1) -> " + e.getMessage());
        }

        try {
            stats.getTopArtistesLesPlusEcoutes(0);
            System.out.println("ERREUR : exception attendue pour limite 0");
        } catch (IllegalArgumentException e) {
            System.out.println("OK getTopArtistesLesPlusEcoutes(0) -> " + e.getMessage());
        }

        System.out.println();
        System.out.println("==================================================");
        System.out.println("FIN DES TESTS DES STATISTIQUES EVOLUEES");
        System.out.println("==================================================");
    }

    private static void incrementerEcoutes(Morceau morceau, int nombreFois) {
        for (int i = 0; i < nombreFois; i++) {
            morceau.incrementerNbEcoutes();
        }
    }

    private static void afficherEcoutesParMorceau(Map<Morceau, Integer> map) {
        for (Map.Entry<Morceau, Integer> entry : map.entrySet()) {
            System.out.println("- " + entry.getKey().getTitre() + " : " + entry.getValue());
        }
    }

    private static void afficherEcoutesParAlbum(Map<Album, Integer> map) {
        for (Map.Entry<Album, Integer> entry : map.entrySet()) {
            System.out.println("- " + entry.getKey().getTitre() + " : " + entry.getValue());
        }
    }

    private static void afficherEcoutesParArtiste(Map<Artiste, Integer> map) {
        for (Map.Entry<Artiste, Integer> entry : map.entrySet()) {
            System.out.println("- " + entry.getKey().getNom() + " : " + entry.getValue());
        }
    }

    private static void afficherTopMorceaux(List<Morceau> morceaux, StatistiquesService stats) {
        int rang = 1;
        for (Morceau morceau : morceaux) {
            System.out.println(rang + ". " + morceau.getTitre() + " -> " + stats.getNombreEcoutesPourMorceau(morceau) + " ecoutes");
            rang++;
        }
    }

    private static void afficherTopAlbums(List<Album> albums, StatistiquesService stats) {
        int rang = 1;
        for (Album album : albums) {
            System.out.println(rang + ". " + album.getTitre() + " -> " + stats.getNombreEcoutesPourAlbum(album) + " ecoutes");
            rang++;
        }
    }

    private static void afficherTopArtistes(List<Artiste> artistes, StatistiquesService stats) {
        int rang = 1;
        for (Artiste artiste : artistes) {
            System.out.println(rang + ". " + artiste.getNom() + " -> " + stats.getNombreEcoutesPourArtiste(artiste) + " ecoutes");
            rang++;
        }
    }
}