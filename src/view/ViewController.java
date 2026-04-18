package view;

import controller.MainController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;
import model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ViewController {

    private static MainController mainController;
    private static Label staticTrackTitle;
    private static Label staticTrackArtist;
    private static Button staticMainPlayButton;
    private static Slider staticProgressSlider;
    private static Label staticCurrentTimeLabel;
    private static Label staticTotalTimeLabel;
    private static Slider staticVolumeSlider;

    // État de lecture partagé
    private static boolean isPlaying = false;
    private static Morceau currentMorceau = null;
    private static Timeline progressTimeline;
    private static double currentSeconds = 0;

    @FXML private StackPane contentArea;
    @FXML private VBox abonneMenu;
    @FXML private VBox adminMenu;
    @FXML private Label currentTrackTitle;
    @FXML private Label currentTrackArtist;
    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;

    // Player controls
    @FXML private Button mainPlayButton;
    @FXML private Slider trackProgressSlider;
    @FXML private Label currentTimeLabel;
    @FXML private Label totalTimeLabel;
    @FXML private Slider volumeSlider;

    // Sous-vues
    @FXML private FlowPane recentItemsPane;
    @FXML private ListView<Object> searchResults;
    @FXML private TextField searchBar;
    @FXML private ToggleGroup searchFilters;
    @FXML private ToggleButton filterAll;
    @FXML private ToggleButton filterMorceau;
    @FXML private ToggleButton filterAlbum;
    @FXML private ToggleButton filterArtiste;
    @FXML private ToggleButton filterGroupe;

    @FXML private Label detailsTitle;
    @FXML private Label detailsSubtitle;
    @FXML private Label detailsLabel1;
    @FXML private Label detailsLabel2;
    @FXML private ListView<Object> detailsListView;
    @FXML private Button detailsPlayButton;

    @FXML private ListView<Morceau> adminCatalogueList;
    @FXML private ListView<Utilisateur> adminUserList;

    public static void setMainController(MainController controller) {
        mainController = controller;
    }

    @FXML
    public void initialize() {
        try {
            // Sauvegarde des références de la barre de lecture
            if (currentTrackTitle != null) {
                staticTrackTitle = currentTrackTitle;
                staticTrackArtist = currentTrackArtist;
                staticMainPlayButton = mainPlayButton;
                staticProgressSlider = trackProgressSlider;
                staticCurrentTimeLabel = currentTimeLabel;
                staticTotalTimeLabel = totalTimeLabel;
                staticVolumeSlider = volumeSlider;

                // Sync UI state if a song was already playing
                if (currentMorceau != null) {
                    staticTrackTitle.setText(currentMorceau.getTitre());
                    staticTrackArtist.setText(currentMorceau.getInterprete());
                    staticTotalTimeLabel.setText(currentMorceau.getDureeFormatee());
                    staticMainPlayButton.setText(isPlaying ? "⏸" : "▶");
                    staticProgressSlider.setMax(currentMorceau.getDureeSecondes());
                    staticProgressSlider.setValue(currentSeconds);
                }

                // Volume slider listener for green style
                if (volumeSlider != null) {
                    volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> updateSliderStyle(volumeSlider));
                    updateSliderStyle(volumeSlider);
                }
                if (trackProgressSlider != null) {
                    trackProgressSlider.valueProperty().addListener((obs, oldVal, newVal) -> updateSliderStyle(trackProgressSlider));
                }
            }

            if (roleComboBox != null) {
                roleComboBox.getItems().setAll("Abonné", "Administrateur");
                roleComboBox.setValue("Abonné");
            }
            updateMenuVisibility();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateSliderStyle(Slider slider) {
        double percentage = (slider.getValue() - slider.getMin()) / (slider.getMax() - slider.getMin()) * 100;
        String style = String.format("-fx-background-color: linear-gradient(to right, #1db954 %f%%, #3e3e3e %f%%);", percentage, percentage);
        slider.lookup(".track").setStyle(style);
    }

    private void updateMenuVisibility() {
        if (abonneMenu != null && adminMenu != null && mainController != null && mainController.getSystem() != null) {
            boolean isAbonne = mainController.getSystem().estUnAbonneConnecte();
            boolean isAdmin = mainController.getSystem().estUnAdministrateurConnecte();
            abonneMenu.setVisible(isAbonne);
            abonneMenu.setManaged(isAbonne);
            adminMenu.setVisible(isAdmin);
            adminMenu.setManaged(isAdmin);
        }
    }

    // --- Navigation ---

    @FXML private void showHome(ActionEvent event) { loadView("HomeView.fxml"); chargerMorceauxAccueil(); }
    @FXML private void showSearch(ActionEvent event) { loadView("SearchView.fxml"); configurerListViewGenerique(searchResults); handleSearch(null); }
    @FXML private void showPlaylists(ActionEvent event) { loadView("PlaylistView.fxml"); }
    @FXML private void showHistory(ActionEvent event) { System.out.println("History clicked"); }
    @FXML private void showAdminDashboard(ActionEvent event) {
        loadView("AdminView.fxml");
        if (adminCatalogueList != null) {
            configurerListViewMorceaux(adminCatalogueList);
            adminCatalogueList.getItems().setAll(mainController.getTousLesMorceaux());
        }
        if (adminUserList != null) {
            adminUserList.getItems().setAll(new ArrayList<Utilisateur>(mainController.getSystem().getAbonnes()));
        }
    }

    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            loader.setController(this);
            Parent view = loader.load();
            if (contentArea != null) contentArea.getChildren().setAll(view);
        } catch (IOException e) { e.printStackTrace(); }
    }

    // --- Logic ---

    private void configurerListViewGenerique(ListView<Object> listView) {
        if (listView == null) return;
        listView.setCellFactory(param -> new ListCell<Object>() {
            private final HBox root = new HBox(10);
            private final Label label = new Label();
            private final Region spacer = new Region();
            private final Button btnPlay = new Button("▶");
            {
                HBox.setHgrow(spacer, Priority.ALWAYS);
                btnPlay.getStyleClass().add("button-primary");
                btnPlay.setStyle("-fx-padding: 2 8; -fx-font-size: 10px;");
                root.setAlignment(Pos.CENTER_LEFT);
                root.getChildren().addAll(label, spacer, btnPlay);
            }
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setGraphic(null);
                else {
                    boolean isCurrent = (item instanceof Morceau && item.equals(currentMorceau));
                    btnPlay.setVisible(item instanceof Morceau);
                    btnPlay.setManaged(item instanceof Morceau);
                    if (item instanceof Morceau) {
                        Morceau m = (Morceau) item;
                        label.setText("[Son] " + m.getTitre() + " - " + m.getInterprete());
                        btnPlay.setText((isCurrent && isPlaying) ? "⏸" : "▶");
                        btnPlay.setOnAction(e -> handleItemPlayClick(m));
                    } else if (item instanceof Album) label.setText("[Album] " + ((Album)item).getTitre());
                    else if (item instanceof Artiste) label.setText("[Artiste] " + ((Artiste)item).getNom());
                    else if (item instanceof Groupe) label.setText("[Groupe] " + ((Groupe)item).getNom());
                    label.setStyle(isCurrent ? "-fx-text-fill: #1db954; -fx-font-weight: bold;" : "-fx-text-fill: white;");
                    setGraphic(root);
                }
            }
        });
    }

    private void configurerListViewMorceaux(ListView<Morceau> listView) {
        if (listView == null) return;
        listView.setCellFactory(param -> new ListCell<Morceau>() {
            @Override protected void updateItem(Morceau item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else setText(item.getTitre() + " - " + item.getInterprete());
            }
        });
    }

    private void chargerMorceauxAccueil() {
        if (recentItemsPane == null) return;
        recentItemsPane.getChildren().clear();
        for (Morceau m : mainController.getTousLesMorceaux()) {
            boolean isCurrent = m.equals(currentMorceau);
            VBox card = new VBox(8);
            card.getStyleClass().add("morceau-card");
            card.setPrefWidth(160);
            Label title = new Label(m.getTitre());
            title.setStyle(isCurrent ? "-fx-font-weight: bold; -fx-text-fill: #1db954;" : "-fx-font-weight: bold;");
            Label artist = new Label(m.getInterprete());
            artist.setStyle("-fx-text-fill: #b3b3b3; -fx-font-size: 11px;");
            HBox controls = new HBox(5);
            Button p = new Button((isCurrent && isPlaying) ? "⏸" : "▶");
            p.getStyleClass().add("button-primary");
            p.setOnAction(e -> handleItemPlayClick(m));
            Button d = new Button("ℹ"); d.getStyleClass().add("button-secondary"); d.setOnAction(e -> showDetails(m));
            controls.getChildren().addAll(p, d);
            card.getChildren().addAll(title, artist, controls);
            recentItemsPane.getChildren().add(card);
        }
    }

    private void handleItemPlayClick(Morceau m) {
        if (m.equals(currentMorceau)) {
            togglePlayPause();
        } else {
            playMorceau(m);
        }
        // Rafraîchir la vue actuelle pour mettre à jour les boutons
        if (recentItemsPane != null) chargerMorceauxAccueil();
        if (searchResults != null) searchResults.refresh();
        if (detailsListView != null) detailsListView.refresh();
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        if (searchResults == null) return;
        String query = (searchBar != null) ? searchBar.getText() : "";
        List<Object> results = new ArrayList<>();
        boolean all = (filterAll == null || filterAll.isSelected());
        if (all || (filterMorceau != null && filterMorceau.isSelected())) {
            for (Morceau m : mainController.rechercherMorceaux(query)) results.add(m);
        }
        if (all || (filterAlbum != null && filterAlbum.isSelected())) {
            for (Album a : mainController.rechercherAlbums(query)) results.add(a);
        }
        if (all || (filterArtiste != null && filterArtiste.isSelected())) {
            for (Artiste art : mainController.rechercherArtistes(query)) results.add(art);
        }
        if (all || (filterGroupe != null && filterGroupe.isSelected())) {
            for (Groupe g : mainController.rechercherGroupes(query)) results.add(g);
        }
        searchResults.getItems().setAll(results);
    }

    public void showDetails(Object item) {
        loadView("DetailsView.fxml");
        configurerListViewGenerique(detailsListView);
        if (item instanceof Morceau) {
            Morceau m = (Morceau) item;
            if (detailsTitle != null) detailsTitle.setText(m.getTitre());
            if (detailsSubtitle != null) detailsSubtitle.setText("Morceau - " + m.getGenre());
            if (detailsLabel1 != null) detailsLabel1.setText("Artiste: " + m.getInterprete());
            if (detailsLabel2 != null) detailsLabel2.setText("Durée: " + m.getDureeFormatee());
            if (detailsPlayButton != null) {
                detailsPlayButton.setVisible(true);
                boolean isCurrent = m.equals(currentMorceau);
                detailsPlayButton.setText((isCurrent && isPlaying) ? "PAUSE" : "LIRE");
                detailsPlayButton.setOnAction(e -> handleItemPlayClick(m));
            }
        } else if (item instanceof Album) {
            Album a = (Album) item;
            if (detailsTitle != null) detailsTitle.setText(a.getTitre());
            if (detailsSubtitle != null) detailsSubtitle.setText("Album - " + a.getAnneeSortie());
            if (detailsLabel1 != null) detailsLabel1.setText("Interprète: " + a.getInterprete());
            if (detailsListView != null) detailsListView.getItems().setAll(new ArrayList<Object>(a.getMorceaux()));
        } else if (item instanceof Artiste) {
            Artiste art = (Artiste) item;
            if (detailsTitle != null) detailsTitle.setText(art.getNom());
            if (detailsSubtitle != null) detailsSubtitle.setText("Artiste");
            if (detailsLabel1 != null) detailsLabel1.setText(art.getBiographie());
            if (detailsListView != null) detailsListView.getItems().setAll(new ArrayList<Object>(mainController.getSystem().getCatalogue().getMorceauxDeArtiste(art)));
        } else if (item instanceof Groupe) {
            Groupe g = (Groupe) item;
            if (detailsTitle != null) detailsTitle.setText(g.getNom());
            if (detailsSubtitle != null) detailsSubtitle.setText("Groupe");
            if (detailsLabel1 != null) detailsLabel1.setText(g.getDescription());
            if (detailsListView != null) detailsListView.getItems().setAll(new ArrayList<Object>(mainController.getSystem().getCatalogue().getMorceauxDeGroupe(g)));
        }
    }

    // --- Player Logic ---

    @FXML
    private void handlePlayPause(ActionEvent event) {
        if (currentMorceau == null) {
            List<Morceau> tous = mainController.getTousLesMorceaux();
            if (!tous.isEmpty()) playMorceau(tous.get(0));
        } else {
            togglePlayPause();
        }
    }

    private void togglePlayPause() {
        isPlaying = !isPlaying;
        if (staticMainPlayButton != null) staticMainPlayButton.setText(isPlaying ? "⏸" : "▶");
        
        if (isPlaying) {
            startProgress();
        } else {
            if (progressTimeline != null) progressTimeline.pause();
        }
        
        // Refresh visible buttons
        if (recentItemsPane != null) chargerMorceauxAccueil();
        if (searchResults != null) searchResults.refresh();
    }

    public void playMorceau(Morceau morceau) {
        if (morceau == null) return;
        try {
            mainController.ecouterMorceau(morceau);
            currentMorceau = morceau;
            isPlaying = true;
            currentSeconds = 0;

            if (staticTrackTitle != null) {
                staticTrackTitle.setText(morceau.getTitre());
                staticTrackArtist.setText(morceau.getInterprete());
                staticTotalTimeLabel.setText(morceau.getDureeFormatee());
                staticMainPlayButton.setText("⏸");
                staticProgressSlider.setMax(morceau.getDureeSecondes());
                staticProgressSlider.setValue(0);
            }
            
            startProgress();
        } catch (Exception e) {
            showAlert("Information", e.getMessage());
        }
    }

    private void startProgress() {
        if (progressTimeline != null) progressTimeline.stop();
        
        progressTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (isPlaying && currentMorceau != null) {
                currentSeconds++;
                if (currentSeconds > currentMorceau.getDureeSecondes()) {
                    currentSeconds = 0;
                    isPlaying = false;
                    if (staticMainPlayButton != null) staticMainPlayButton.setText("▶");
                    progressTimeline.stop();
                } else {
                    if (staticProgressSlider != null) staticProgressSlider.setValue(currentSeconds);
                    if (staticCurrentTimeLabel != null) staticCurrentTimeLabel.setText(formatTime((int)currentSeconds));
                }
            }
        }));
        progressTimeline.setCycleCount(Timeline.INDEFINITE);
        progressTimeline.play();
    }

    private String formatTime(int seconds) {
        return String.format("%d:%02d", seconds / 60, seconds % 60);
    }

    // --- Authentication ---

    @FXML
    private void handleLogin(ActionEvent event) {
        if (loginField == null || passwordField == null) return;
        String id = loginField.getText();
        String pwd = passwordField.getText();
        String role = (roleComboBox != null) ? roleComboBox.getValue() : "Abonné";
        if ("Abonné".equals(role) ? mainController.loginAbonne(id, pwd) : mainController.loginAdministrateur(id, pwd)) {
            switchToMainView();
        } else {
            showAlert("Erreur", "Identifiant ou mot de passe incorrect.");
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        if (loginField == null || passwordField == null) return;
        String id = loginField.getText();
        String pwd = passwordField.getText();
        if (id.isEmpty() || pwd.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }
        try {
            mainController.registerAbonne(id, pwd);
            showAlert("Succès", "Compte créé !");
        } catch (Exception e) {
            showAlert("Erreur", e.getMessage());
        }
    }

    @FXML private void handleVisitor(ActionEvent event) { mainController.continuerCommeVisiteur(); switchToMainView(); }
    @FXML private void handleLogout(ActionEvent event) { mainController.deconnecter(); isPlaying = false; currentMorceau = null; switchToLoginView(); }

    private void switchToMainView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
            Parent root = loader.load();
            ViewController c = loader.getController();
            c.showHome(null);
            if (loginField != null && loginField.getScene() != null) {
                loginField.getScene().setRoot(root);
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void switchToLoginView() {
        try {
            Parent loginView = FXMLLoader.load(getClass().getResource("LoginView.fxml"));
            if (contentArea != null && contentArea.getScene() != null) {
                contentArea.getScene().setRoot(loginView);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title); alert.setHeaderText(null); alert.setContentText(content);
        alert.showAndWait();
    }
}
