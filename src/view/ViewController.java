package view;

import controller.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.Morceau;
import model.Utilisateur;
import model.Abonne;

import java.io.IOException;
import java.util.List;

public class ViewController {

    private static MainController mainController;
    private static Label staticTrackTitle;
    private static Label staticTrackArtist;

    @FXML private StackPane contentArea;
    @FXML private VBox abonneMenu;
    @FXML private VBox adminMenu;
    @FXML private Label currentTrackTitle;
    @FXML private Label currentTrackArtist;
    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;

    // Champs pour les sous-vues
    @FXML private FlowPane recentItemsPane;
    @FXML private ListView<Morceau> searchResults;
    @FXML private TextField searchBar;
    @FXML private ListView<Morceau> adminCatalogueList;
    @FXML private ListView<Utilisateur> adminUserList;

    public static void setMainController(MainController controller) {
        mainController = controller;
    }

    @FXML
    public void initialize() {
        // Sauvegarde des références de la barre de lecture
        if (currentTrackTitle != null) {
            staticTrackTitle = currentTrackTitle;
            staticTrackArtist = currentTrackArtist;
        }

        if (roleComboBox != null) {
            roleComboBox.getItems().addAll("Abonné", "Administrateur");
            roleComboBox.setValue("Abonné");
        }
        updateMenuVisibility();

        // Configuration des ListViews
        configurerListViewMorceaux(searchResults);
        configurerListViewMorceaux(adminCatalogueList);

        // Si on est dans la vue Accueil (HomeView)
        if (recentItemsPane != null) {
            chargerMorceauxAccueil();
        }

        // Si on est dans la vue Recherche
        if (searchResults != null) {
            searchResults.getItems().setAll(mainController.getTousLesMorceaux());
        }

        // Si on est dans la vue Admin
        if (adminCatalogueList != null) {
            adminCatalogueList.getItems().setAll(mainController.getTousLesMorceaux());
            adminUserList.getItems().setAll(mainController.getSystem().getAbonnes());
        }
    }

    private void configurerListViewMorceaux(ListView<Morceau> listView) {
        if (listView == null) return;

        listView.setCellFactory(param -> new ListCell<Morceau>() {
            private final HBox root = new HBox(10);
            private final Label label = new Label();
            private final Region spacer = new Region();
            private final Button btn = new Button("▶");

            {
                HBox.setHgrow(spacer, Priority.ALWAYS);
                btn.getStyleClass().add("button-primary");
                btn.setStyle("-fx-padding: 5 10; -fx-font-size: 10px;");
                root.setAlignment(Pos.CENTER_LEFT);
                root.getChildren().addAll(label, spacer, btn);
            }

            @Override
            protected void updateItem(Morceau item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    label.setText(item.getTitre() + " - " + item.getInterprete() + " (" + item.getDureeFormatee() + ")");
                    label.setStyle("-fx-text-fill: white;");
                    btn.setOnAction(e -> playMorceau(item));
                    setGraphic(root);
                }
            }
        });

        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Morceau selected = listView.getSelectionModel().getSelectedItem();
                if (selected != null) playMorceau(selected);
            }
        });
    }

    private void chargerMorceauxAccueil() {
        recentItemsPane.getChildren().clear();
        for (Morceau m : mainController.getTousLesMorceaux()) {
            VBox card = new VBox(10);
            card.getStyleClass().add("morceau-card");
            card.setPrefWidth(150);
            
            Label title = new Label(m.getTitre());
            title.setStyle("-fx-font-weight: bold;");
            Label artist = new Label(m.getInterprete());
            artist.setStyle("-fx-text-fill: #b3b3b3;");
            
            Button playBtn = new Button("▶");
            playBtn.getStyleClass().add("button-primary");
            playBtn.setOnAction(e -> playMorceau(m));
            
            card.getChildren().addAll(title, artist, playBtn);
            recentItemsPane.getChildren().add(card);
        }
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        if (searchResults != null && searchBar != null) {
            String query = searchBar.getText();
            System.out.println("Recherche de : " + query);
            searchResults.getItems().setAll(mainController.rechercherMorceaux(query));
        }
    }

    private void updateMenuVisibility() {
        if (abonneMenu != null && adminMenu != null) {
            boolean isAbonne = mainController.getSystem().estUnAbonneConnecte();
            boolean isAdmin = mainController.getSystem().estUnAdministrateurConnecte();
            abonneMenu.setVisible(isAbonne);
            abonneMenu.setManaged(isAbonne);
            adminMenu.setVisible(isAdmin);
            adminMenu.setManaged(isAdmin);
        }
    }

    // --- Navigation ---

    @FXML
    private void showHome(ActionEvent event) {
        loadView("HomeView.fxml");
    }

    @FXML
    private void showSearch(ActionEvent event) {
        loadView("SearchView.fxml");
    }

    @FXML
    private void showPlaylists(ActionEvent event) {
        loadView("PlaylistView.fxml");
    }

    @FXML
    private void showHistory(ActionEvent event) {
        // history view
    }

    @FXML
    private void showAdminDashboard(ActionEvent event) {
        loadView("AdminView.fxml");
    }

    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            loader.setController(this);
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- Authentication ---

    @FXML
    private void handleLogin(ActionEvent event) {
        String id = loginField.getText();
        String pwd = passwordField.getText();
        String role = roleComboBox.getValue();

        boolean success = false;
        if ("Abonné".equals(role)) {
            success = mainController.loginAbonne(id, pwd);
        } else {
            success = mainController.loginAdministrateur(id, pwd);
        }

        if (success) {
            switchToMainView();
        } else {
            showAlert("Erreur", "Identifiant ou mot de passe incorrect.");
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String id = loginField.getText();
        String pwd = passwordField.getText();
        try {
            mainController.registerAbonne(id, pwd);
            showAlert("Succès", "Compte créé ! Vous pouvez vous connecter.");
        } catch (Exception e) {
            showAlert("Erreur", e.getMessage());
        }
    }

    @FXML
    private void handleVisitor(ActionEvent event) {
        mainController.continuerCommeVisiteur();
        switchToMainView();
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        mainController.deconnecter();
        switchToLoginView();
    }

    private void switchToMainView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
            Parent root = loader.load();
            ViewController controller = loader.getController();
            controller.showHome(null);
            loginField.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchToLoginView() {
        try {
            Parent loginView = FXMLLoader.load(getClass().getResource("LoginView.fxml"));
            contentArea.getScene().setRoot(loginView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- Player ---

    @FXML
    private void handlePlayPause(ActionEvent event) {
        System.out.println("Clic sur le bouton Play central");
        if (staticTrackTitle != null && "Aucun morceau".equals(staticTrackTitle.getText())) {
            List<Morceau> tous = mainController.getTousLesMorceaux();
            if (!tous.isEmpty()) playMorceau(tous.get(0));
        }
    }

    public void playMorceau(Morceau morceau) {
        if (morceau == null) return;
        System.out.println("Lecture demandée : " + morceau.getTitre());
        try {
            mainController.ecouterMorceau(morceau);
            if (staticTrackTitle != null) {
                staticTrackTitle.setText(morceau.getTitre());
                staticTrackArtist.setText(morceau.getInterprete());
            } else {
                System.err.println("ERREUR : Barre de lecture non trouvée !");
            }
        } catch (Exception e) {
            showAlert("Information", e.getMessage());
        }
    }

    // --- Helpers ---

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
