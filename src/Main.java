import model.Catalogue;
import view.ConsoleView;
import controller.MainController;

public class Main {
    public static void main(String[] args) {
        // Initialisation du Modèle
        Catalogue catalogue = new Catalogue();
        
        // Initialisation de la Vue
        ConsoleView view = new ConsoleView();
        
        // Initialisation du Contrôleur
        MainController controller = new MainController(catalogue, view);
        
        // Démarrage de l'application
        controller.demarrer();
    }
}
