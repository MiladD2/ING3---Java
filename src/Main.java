import view.JavaFXApp;
import view.ConsoleView;
import controller.MainController;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n\n\n\n=== JAVAZIK - Système de Streaming ===\n");
        System.out.println("1. Mode Graphique (JavaFX)");
        System.out.println("2. Mode Console");
        System.out.print("Choisissez votre interface (1-2) : ");

        String choix = scanner.nextLine();

        if ("2".equals(choix)) {
            MainController controller = new MainController();
            ConsoleView consoleView = new ConsoleView(controller);
            consoleView.demarrer();
        } else {
            // Par défaut, ou si choix 1, on lance JavaFX
            JavaFXApp.main(args);
        }
    }
}
