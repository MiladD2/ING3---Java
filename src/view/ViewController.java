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

    @FXML private Button mainPlayButton;
    @FXML private Slider trackProgressSlider;
    @FXML private Label currentTimeLabel;
    @FXML private Label totalTimeLabel;
    @FXML private Slider volumeSlider;

    @FXML private FlowPane recentItemsPane;
    @FXML private ListView<Object> searchResults;
    @FXML private TextField searchBar;
    @FXML private ToggleButton filterAll;
    @FXML private ToggleButton filterMorceau;
    @FXML private ToggleButton filterAlbum;
    @FXML private ToggleButton filterArtiste;
    @FXML private ToggleButton filterGroupe;

    // Nouveau Detail View components
    @FXML private Label detailsTitle;
    @FXML private Label detailsSubtitle;
    @FXML private Label detailsLabel1;
    @FXML private Label detailsLabel2;
    @FXML private Button detailsPlayButton;
    @FXML private VBox sectionArtistes;
    @FXML private FlowPane artistsFlowPane;
    @FXML private VBox sectionMorceaux;
    @FXML private VBox morceauxVBox;
    @FXML private Label morceauxTitleLabel;

    @FXML private ListView<Morceau> adminCatalogueList;
    @FXML private ListView<Utilisateur> adminUserList;

    public static void setMainController(MainController controller) {
        mainController = controller;
    }

    @FXML
    public void initialize() {
        try {
            if (currentTrackTitle != null) {
                staticTrackTitle = currentTrackTitle;
                staticTrackArtist = currentTrackArtist;
                staticMainPlayButton = mainPlayButton;
                staticProgressSlider = trackProgressSlider;
                staticCurrentTimeLabel = currentTimeLabel;
                staticTotalTimeLabel = totalTimeLabel;
                staticVolumeSlider = volumeSlider;

                if (currentMorceau != null) {
                    syncPlayerUI();
                }

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

    private void syncPlayerUI() {
        if (currentMorceau == null || staticTrackTitle == null) return;
        staticTrackTitle.setText(currentMorceau.getTitre());
        staticTrackArtist.setText(currentMorceau.getInterprete());
        staticTotalTimeLabel.setText(currentMorceau.getDureeFormatee());
        staticMainPlayButton.setText(isPlaying ? "⏸" : "▶");
        staticProgressSlider.setMax(currentMorceau.getDureeSecondes());
        staticProgressSlider.setValue(currentSeconds);
    }

    private void updateSliderStyle(Slider slider) {
        double percentage = (slider.getValue() - slider.getMin()) / (slider.getMax() - slider.getMin()) * 100;
        String style = String.format("-fx-background-color: linear-gradient(to right, #1db954 %f%%, #3e3e3e %f%%);", percentage, percentage);
        Region track = (Region) slider.lookup(".track");
        if (track != null) track.setStyle(style);
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
    
    @FXML private void showSearch(ActionEvent event) { 
        loadView("SearchView.fxml"); 
        configurerListViewGenerique(searchResults); 
        handleSearch(null); 
    }
    
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
                        label.setText(m.getTitre() + " - " + m.getInterprete());
                        btnPlay.setText((isCurrent && isPlaying) ? "⏸" : "▶");
                        btnPlay.setOnAction(e -> handleItemPlayClick(m));
                    } else if (item instanceof Album) label.setText(((Album)item).getTitre());
                    else if (item instanceof Artiste) label.setText(((Artiste)item).getNom());
                    else if (item instanceof Groupe) label.setText(((Groupe)item).getNom());
                    
                    label.setStyle(isCurrent ? "-fx-text-fill: #1db954; -fx-font-weight: bold;" : "-fx-text-fill: white;");
                    setGraphic(root);
                }
            }
        });
        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Object selected = listView.getSelectionModel().getSelectedItem();
                if (selected != null) showDetails(selected);
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

    @FXML
    private void handleSearch(ActionEvent event) {
        if (searchResults == null) return;
        String query = (searchBar != null) ? searchBar.getText() : "";
        List<Object> results = new ArrayList<>();
        boolean all = (filterAll != null && filterAll.isSelected());
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
        
        if (item instanceof Morceau m) {
            detailsSubtitle.setText("MORCEAU");
            detailsTitle.setText(m.getTitre());
            detailsLabel1.setText(m.getGenre() + " • " + m.getDureeFormatee());
            detailsLabel2.setText(m.getNombreEcoutes() + " écoutes");
            
            sectionArtistes.setVisible(true); sectionArtistes.setManaged(true);
            artistsFlowPane.getChildren().clear();
            
            // Related Artist
            if (m.getArtiste() != null) {
                artistsFlowPane.getChildren().add(createEntityLink(m.getArtiste()));
            }
            // Related Group
            if (m.getGroupe() != null) {
                Label groupPrefix = new Label("Groupe : ");
                groupPrefix.setStyle("-fx-text-fill: #b3b3b3;");
                HBox groupLink = new HBox(5, groupPrefix, createEntityLink(m.getGroupe()));
                artistsFlowPane.getChildren().add(groupLink);
            }
            
            detailsPlayButton.setVisible(true);
            detailsPlayButton.setOnAction(e -> handleItemPlayClick(m));
            updatePlayButtonText(m);
            
        } else if (item instanceof Album a) {
            detailsSubtitle.setText("ALBUM");
            detailsTitle.setText(a.getTitre());
            detailsLabel1.setText(a.getInterprete());
            detailsLabel2.setText(a.getAnneeSortie() + " • " + a.getMorceaux().size() + " morceaux");
            
            sectionMorceaux.setVisible(true); sectionMorceaux.setManaged(true);
            morceauxTitleLabel.setText("Liste des titres");
            populateMorceauxList(a.getMorceaux());
            
            detailsPlayButton.setVisible(true);
            if (!a.getMorceaux().isEmpty()) detailsPlayButton.setOnAction(e -> handleItemPlayClick(a.getMorceaux().get(0)));
            
        } else if (item instanceof Artiste art) {
            detailsSubtitle.setText("ARTISTE");
            detailsTitle.setText(art.getNom());
            detailsLabel1.setText(art.getBiographie());
            detailsLabel2.setText("");
            
            if (art.getGroupe() != null) {
                sectionArtistes.setVisible(true); sectionArtistes.setManaged(true);
                artistsFlowPane.getChildren().clear();
                Label groupPrefix = new Label("Membre de : "); groupPrefix.setStyle("-fx-text-fill: #b3b3b3;");
                artistsFlowPane.getChildren().add(new HBox(5, groupPrefix, createEntityLink(art.getGroupe())));
            }
            
            sectionMorceaux.setVisible(true); sectionMorceaux.setManaged(true);
            morceauxTitleLabel.setText("Morceaux populaires");
            populateMorceauxList(mainController.getSystem().getCatalogue().getMorceauxDeArtiste(art));
            
            detailsPlayButton.setVisible(false);
            
        } else if (item instanceof Groupe g) {
            detailsSubtitle.setText("GROUPE");
            detailsTitle.setText(g.getNom());
            detailsLabel1.setText(g.getDescription());
            detailsLabel2.setText(g.getMembres().size() + " membres");
            
            // Section Membres
            sectionArtistes.setVisible(true); sectionArtistes.setManaged(true);
            artistsFlowPane.getChildren().clear();
            for (Artiste m : g.getMembres()) {
                artistsFlowPane.getChildren().add(createEntityLink(m));
            }
            
            // Section Morceaux
            sectionMorceaux.setVisible(true); sectionMorceaux.setManaged(true);
            morceauxTitleLabel.setText("Discographie");
            populateMorceauxList(mainController.getSystem().getCatalogue().getMorceauxDeGroupe(g));
            
            detailsPlayButton.setVisible(false);
        }
    }

    private Hyperlink createEntityLink(Object entity) {
        String name = (entity instanceof Artiste) ? ((Artiste)entity).getNom() : ((Groupe)entity).getNom();
        Hyperlink link = new Hyperlink(name);
        link.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-underline: false; -fx-padding: 0;");
        link.setOnAction(e -> showDetails(entity));
        link.setOnMouseEntered(e -> link.setStyle("-fx-text-fill: #1db954; -fx-font-weight: bold; -fx-underline: true;"));
        link.setOnMouseExited(e -> link.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-underline: false;"));
        return link;
    }

    private void populateMorceauxList(List<Morceau> list) {
        morceauxVBox.getChildren().clear();
        for (Morceau m : list) {
            HBox row = new HBox(15);
            row.setAlignment(Pos.CENTER_LEFT);
            row.getStyleClass().add("list-cell"); // reuse hover effect
            row.setStyle("-fx-padding: 10; -fx-background-radius: 5;");
            
            Label title = new Label(m.getTitre());
            title.setStyle("-fx-text-fill: white;");
            HBox.setHgrow(title, Priority.ALWAYS);
            
            Label duration = new Label(m.getDureeFormatee());
            duration.setStyle("-fx-text-fill: #b3b3b3;");
            
            Button p = new Button((m.equals(currentMorceau) && isPlaying) ? "⏸" : "▶");
            p.getStyleClass().add("button-primary");
            p.setStyle("-fx-padding: 5 10; -fx-font-size: 10px;");
            p.setOnAction(e -> handleItemPlayClick(m));
            
            row.getChildren().addAll(p, title, duration);
            morceauxVBox.getChildren().add(row);
        }
    }

    private void updatePlayButtonText(Morceau m) {
        if (detailsPlayButton != null) {
            boolean isCurrent = m.equals(currentMorceau);
            detailsPlayButton.setText((isCurrent && isPlaying) ? "PAUSE" : "LIRE");
        }
    }

    // --- Player Logic ---

    private void handleItemPlayClick(Morceau m) {
        if (m.equals(currentMorceau)) togglePlayPause();
        else playMorceau(m);
        
        if (recentItemsPane != null) chargerMorceauxAccueil();
        if (searchResults != null) searchResults.refresh();
        if (morceauxVBox != null && !morceauxVBox.getChildren().isEmpty()) {
            // refresh manually if we are in details view
            if (currentMorceau != null && currentMorceau.equals(m)) {
               // Reload current details to refresh everything
               // (Not the most efficient but ensures consistent state)
            }
        }
        updatePlayButtonText(m);
    }

    @FXML
    private void handlePlayPause(ActionEvent event) {
        if (currentMorceau == null) {
            List<Morceau> tous = mainController.getTousLesMorceaux();
            if (!tous.isEmpty()) playMorceau(tous.get(0));
        } else togglePlayPause();
    }

    private void togglePlayPause() {
        isPlaying = !isPlaying;
        if (staticMainPlayButton != null) staticMainPlayButton.setText(isPlaying ? "⏸" : "▶");
        if (isPlaying) startProgress();
        else if (progressTimeline != null) progressTimeline.pause();
        
        if (recentItemsPane != null) chargerMorceauxAccueil();
        if (searchResults != null) searchResults.refresh();
        if (currentMorceau != null) updatePlayButtonText(currentMorceau);
    }

    public void playMorceau(Morceau m) {
        if (m == null) return;
        try {
            mainController.ecouterMorceau(m);
            currentMorceau = m; isPlaying = true; currentSeconds = 0;
            syncPlayerUI();
            startProgress();
        } catch (Exception e) { showAlert("Information", e.getMessage()); }
    }

    private void startProgress() {
        if (progressTimeline != null) progressTimeline.stop();
        progressTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (isPlaying && currentMorceau != null) {
                currentSeconds++;
                if (currentSeconds > currentMorceau.getDureeSecondes()) {
                    currentSeconds = 0; isPlaying = false;
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

    private String formatTime(int seconds) { return String.format("%d:%02d", seconds / 60, seconds % 60); }

    // --- Authentication ---

    @FXML
    private void handleLogin(ActionEvent event) {
        if (loginField == null || passwordField == null) return;
        String id = loginField.getText(); String pwd = passwordField.getText();
        String role = (roleComboBox != null) ? roleComboBox.getValue() : "Abonné";
        if ("Abonné".equals(role) ? mainController.loginAbonne(id, pwd) : mainController.loginAdministrateur(id, pwd)) switchToMainView();
        else showAlert("Erreur", "Identifiant ou mot de passe incorrect.");
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        if (loginField == null || passwordField == null) return;
        String id = loginField.getText(); String pwd = passwordField.getText();
        if (id.isEmpty() || pwd.isEmpty()) { showAlert("Erreur", "Veuillez remplir tous les champs."); return; }
        try { mainController.registerAbonne(id, pwd); showAlert("Succès", "Compte créé !"); }
        catch (Exception e) { showAlert("Erreur", e.getMessage()); }
    }

    @FXML private void handleVisitor(ActionEvent event) { mainController.continuerCommeVisiteur(); switchToMainView(); }
    @FXML private void handleLogout(ActionEvent event) { mainController.deconnecter(); isPlaying = false; currentMorceau = null; switchToLoginView(); }

    private void switchToMainView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
            Parent root = loader.load();
            ((ViewController)loader.getController()).showHome(null);
            if (loginField != null && loginField.getScene() != null) loginField.getScene().setRoot(root);
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void switchToLoginView() {
        try {
            Parent loginView = FXMLLoader.load(getClass().getResource("LoginView.fxml"));
            if (contentArea != null && contentArea.getScene() != null) contentArea.getScene().setRoot(loginView);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title); alert.setHeaderText(null); alert.setContentText(content);
        alert.showAndWait();
    }
}
