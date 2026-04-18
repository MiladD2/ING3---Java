package view;

import model.Catalogue;
import model.Morceau;
import java.util.List;

public class ConsoleView {
    public void afficherMenuPrincipal() {
        System.out.println("=== JAVAZIC ===");
        System.out.println("1. Se connecter en tant qu'administrateur");
        System.out.println("2. Se connecter en tant que client");
        System.out.println("3. Creer un compte client");
        System.out.println("4. Continuer en tant que simple visiteur");
        System.out.println("5. Quitter");
        System.out.print("Votre choix : ");
    }

    public void afficherCatalogue(Catalogue catalogue) {
        System.out.println("--- Catalogue Musical ---");
        for (Morceau m : catalogue.getMorceaux()) {
            System.out.println(m.getTitre() + " - " + m.getInterprete());
        }
    }

    public void afficherMessage(String message) {
        System.out.println(message);
    }
}
