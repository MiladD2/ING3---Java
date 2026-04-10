package controller;

import model.Catalogue;
import view.ConsoleView;

public class MainController {
    private Catalogue catalogue;
    private ConsoleView view;

    public MainController(Catalogue catalogue, ConsoleView view) {
        this.catalogue = catalogue;
        this.view = view;
    }

    public void demarrer() {
        view.afficherMenuPrincipal();
        // Logique de lecture de l'entrée utilisateur et actions associées
    }

    public void chargerDonnees() {
        // Logique de chargement (Persistance)
    }

    public void sauvegarderDonnees() {
        // Logique de sauvegarde (Persistance)
    }
}
