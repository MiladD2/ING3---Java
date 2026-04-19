import model.*;

public class Main {
    public static void main(String[] args) {
        try {
            PersistanceService persistance = new PersistanceService("javazik.ser");

            JavazikSystem systeme = new JavazikSystem();
            systeme.ajouterAdministrateur(new Administrateur("admin", "admin123"));
            systeme.creerCompteAbonne("alice", "1234");

            Catalogue catalogue = systeme.getCatalogue();

            Artiste adele = new Artiste("Adele");
            Morceau hello = new Morceau("Hello", 295, adele);
            Album album = new Album("25");

            album.ajouterMorceau(hello);

            catalogue.ajouterArtiste(adele);
            catalogue.ajouterMorceau(hello);
            catalogue.ajouterAlbum(album);

            persistance.sauvegarder(systeme);
            System.out.println("Sauvegarde OK");

            JavazikSystem systemeCharge = persistance.charger();
            System.out.println("Chargement OK");

            System.out.println("Utilisateurs : " + systemeCharge.getNombreUtilisateurs());
            System.out.println("Abonnés : " + systemeCharge.getNombreAbonnes());
            System.out.println("Administrateurs : " + systemeCharge.getNombreAdministrateurs());
            System.out.println("Morceaux : " + systemeCharge.getCatalogue().getNombreMorceaux());
            System.out.println("Albums : " + systemeCharge.getCatalogue().getNombreAlbums());
            System.out.println("Artistes : " + systemeCharge.getCatalogue().getNombreArtistes());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}