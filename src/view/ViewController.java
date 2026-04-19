package view;

import controller.MainController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
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
    private static Label staticGuestRemainingLabel;

    private static boolean isPlaying = false;
    private static Morceau currentMorceau = null;
    private static List<Morceau> currentQueue = new ArrayList<>();
    private static int queueIndex = 0;
    
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
    
    @FXML private VBox guestCounterBox;
    @FXML private Label guestRemainingLabel;

    @FXML private HBox artistsHomePane;
    @FXML private HBox albumsHomePane;
    @FXML private HBox songsHomePane;

    @FXML private ListView<Object> searchResults;
    @FXML private TextField searchBar;
    @FXML private ToggleButton filterAll;
    @FXML private ToggleButton filterMorceau;
    @FXML private ToggleButton filterAlbum;
    @FXML private ToggleButton filterArtiste;
    @FXML private ToggleButton filterGroupe;

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
                staticGuestRemainingLabel = guestRemainingLabel;

                if (currentMorceau != null) syncPlayerUI();
                updateGuestCounter();

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
            System.err.println("Erreur initialize: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void syncPlayerUI() {
        if (currentMorceau == null || staticTrackTitle == null) return;
        staticTrackTitle.setText(currentMorceau.getTitre());
        staticTrackArtist.setText(currentMorceau.getInterprete());
        staticTotalTimeLabel.setText(currentMorceau.getDureeFormatee());
        if (staticMainPlayButton != null) staticMainPlayButton.setText(isPlaying ? "⏸" : "▶");
        if (staticProgressSlider != null) {
            staticProgressSlider.setMax(currentMorceau.getDureeSecondes());
            staticProgressSlider.setValue(currentSeconds);
        }
    }

    private void updateGuestCounter() {
        if (staticGuestRemainingLabel != null && mainController != null && mainController.getSystem() != null && mainController.getSystem().estEnModeVisiteur()) {
            int max = VisiteurSession.LIMITE_PAR_DEFAUT;
            int current = mainController.getSystem().getVisiteurSession().getNombreEcoutes();
            staticGuestRemainingLabel.setText((max - current) + " écoutes restantes");
        }
    }

    private void updateSliderStyle(Slider slider) {
        if (slider == null) return;
        double percentage = (slider.getValue() - slider.getMin()) / (slider.getMax() - slider.getMin()) * 100;
        String style = String.format("-fx-background-color: linear-gradient(to right, #1db954 %f%%, #3e3e3e %f%%);", percentage, percentage);
        Region track = (Region) slider.lookup(".track");
        if (track != null) track.setStyle(style);
    }

    private void updateMenuVisibility() {
        if (abonneMenu != null && adminMenu != null && mainController != null && mainController.getSystem() != null) {
            boolean isAbonne = mainController.getSystem().estUnAbonneConnecte();
            boolean isAdmin = mainController.getSystem().estUnAdministrateurConnecte();
            boolean isGuest = mainController.getSystem().estEnModeVisiteur();
            abonneMenu.setVisible(isAbonne); abonneMenu.setManaged(isAbonne);
            adminMenu.setVisible(isAdmin); adminMenu.setManaged(isAdmin);
            if (guestCounterBox != null) { guestCounterBox.setVisible(isGuest); guestCounterBox.setManaged(isGuest); }
        }
    }

    @FXML private void showHome(ActionEvent event) { loadView("HomeView.fxml"); chargerAccueilSectons(); }
    @FXML private void showSearch(ActionEvent event) { loadView("SearchView.fxml"); configurerListViewGenerique(searchResults); handleSearch(null); }
    @FXML private void showPlaylists(ActionEvent event) { loadView("PlaylistView.fxml"); }
    @FXML private void showHistory(ActionEvent event) { System.out.println("History clicked"); }
    @FXML private void showAdminDashboard(ActionEvent event) {
        loadView("AdminView.fxml");
        if (adminCatalogueList != null && mainController != null) {
            configurerListViewMorceaux(adminCatalogueList);
            adminCatalogueList.getItems().setAll(mainController.getTousLesMorceaux());
        }
        if (adminUserList != null && mainController != null && mainController.getSystem() != null) {
            List<Utilisateur> abonnes = new ArrayList<>();
            List<Abonne> list = mainController.getSystem().getAbonnes();
            if (list != null) for (Abonne a : list) abonnes.add(a);
            adminUserList.getItems().setAll(abonnes);
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

    private void chargerAccueilSectons() {
        if (artistsHomePane == null || mainController == null) return;
        artistsHomePane.getChildren().clear();
        for (Groupe g : mainController.getTousLesGroupes()) artistsHomePane.getChildren().add(createCard(g));
        for (Artiste a : mainController.getTousLesArtistes()) if (a.getGroupe() == null) artistsHomePane.getChildren().add(createCard(a));
        
        albumsHomePane.getChildren().clear();
        for (Album al : mainController.getTousLesAlbums()) albumsHomePane.getChildren().add(createCard(al));

        songsHomePane.getChildren().clear();
        for (Morceau m : mainController.getTousLesMorceaux()) {
            boolean inAlbum = false;
            for (Album al : mainController.getTousLesAlbums()) if (al.getMorceaux().contains(m)) { inAlbum = true; break; }
            if (!inAlbum) songsHomePane.getChildren().add(createCard(m));
        }
    }

    private VBox createCard(Object item) {
        VBox card = new VBox(10); card.getStyleClass().add("morceau-card");
        card.setPrefWidth(160); card.setMinWidth(160);
        String title = ""; String sub = "";
        if (item instanceof Morceau) { title = ((Morceau)item).getTitre(); sub = ((Morceau)item).getInterprete(); }
        else if (item instanceof Album) { title = ((Album)item).getTitre(); sub = ((Album)item).getInterprete(); }
        else if (item instanceof Artiste) { title = ((Artiste)item).getNom(); sub = "Artiste"; }
        else if (item instanceof Groupe) { title = ((Groupe)item).getNom(); sub = "Groupe"; }

        Hyperlink titleLink = new Hyperlink(title);
        titleLink.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-padding: 0; -fx-underline: false; -fx-font-size: 13px;");
        titleLink.setOnAction(e -> showDetails(item));

        Label subLbl = new Label(sub);
        subLbl.setStyle("-fx-text-fill: #b3b3b3; -fx-font-size: 11px;");
        HBox controls = new HBox(5);
        if (item instanceof Morceau) {
            Button p = new Button((item.equals(currentMorceau) && isPlaying) ? "⏸" : "▶");
            p.getStyleClass().add("button-primary");
            p.setOnAction(e -> handleItemPlayClick((Morceau)item));
            controls.getChildren().add(p);
        } else if (item instanceof Album) {
            Button p = new Button("▶"); p.getStyleClass().add("button-primary");
            p.setOnAction(e -> playAlbum((Album)item));
            controls.getChildren().add(p);
        }
        card.getChildren().addAll(titleLink, subLbl, controls);
        return card;
    }

    private void playAlbum(Album a) {
        if (a == null || a.getMorceaux().isEmpty()) return;
        currentQueue = new ArrayList<>(a.getMorceaux());
        queueIndex = 0;
        playMorceau(currentQueue.get(queueIndex));
    }

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
            @Override protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setGraphic(null);
                else {
                    boolean isCurrent = (item instanceof Morceau && item.equals(currentMorceau));
                    btnPlay.setVisible(item instanceof Morceau); btnPlay.setManaged(item instanceof Morceau);
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

    @FXML
    private void handleSearch(ActionEvent event) {
        if (searchResults == null || mainController == null) return;
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
        if (item instanceof Morceau) {
            Morceau m = (Morceau) item;
            if (detailsSubtitle != null) detailsSubtitle.setText("MORCEAU"); 
            if (detailsTitle != null) detailsTitle.setText(m.getTitre());
            if (detailsLabel1 != null) detailsLabel1.setText(m.getGenre() + " • " + m.getDureeFormatee()); 
            if (detailsLabel2 != null) detailsLabel2.setText(m.getNombreEcoutes() + " écoutes");
            if (sectionArtistes != null) { sectionArtistes.setVisible(true); sectionArtistes.setManaged(true); }
            if (artistsFlowPane != null) {
                artistsFlowPane.getChildren().clear();
                if (m.getArtiste() != null) artistsFlowPane.getChildren().add(createEntityLink(m.getArtiste()));
                if (m.getGroupe() != null) {
                    Label gp = new Label("Groupe : "); gp.setStyle("-fx-text-fill: #b3b3b3;");
                    artistsFlowPane.getChildren().add(new HBox(5, gp, createEntityLink(m.getGroupe())));
                }
            }
            if (detailsPlayButton != null) {
                detailsPlayButton.setVisible(true); detailsPlayButton.setOnAction(e -> handleItemPlayClick(m));
                updatePlayButtonText(m);
            }
        } else if (item instanceof Album) {
            Album a = (Album) item;
            if (detailsSubtitle != null) detailsSubtitle.setText("ALBUM"); 
            if (detailsTitle != null) detailsTitle.setText(a.getTitre());
            if (detailsLabel1 != null) detailsLabel1.setText(a.getInterprete()); 
            if (detailsLabel2 != null) detailsLabel2.setText(a.getAnneeSortie() + " • " + a.getMorceaux().size() + " morceaux");
            if (sectionMorceaux != null) { sectionMorceaux.setVisible(true); sectionMorceaux.setManaged(true); }
            if (morceauxTitleLabel != null) morceauxTitleLabel.setText("Liste des titres");
            populateMorceauxList(a.getMorceaux());
            if (detailsPlayButton != null) {
                detailsPlayButton.setVisible(true); detailsPlayButton.setOnAction(e -> playAlbum(a));
            }
        } else if (item instanceof Artiste) {
            Artiste art = (Artiste) item;
            if (detailsSubtitle != null) detailsSubtitle.setText("ARTISTE"); 
            if (detailsTitle != null) detailsTitle.setText(art.getNom());
            if (detailsLabel1 != null) detailsLabel1.setText(art.getBiographie()); 
            if (detailsLabel2 != null) detailsLabel2.setText("");
            if (art.getGroupe() != null && sectionArtistes != null) {
                sectionArtistes.setVisible(true); sectionArtistes.setManaged(true);
                if (artistsFlowPane != null) {
                    artistsFlowPane.getChildren().clear();
                    Label gp = new Label("Membre de : "); gp.setStyle("-fx-text-fill: #b3b3b3;");
                    artistsFlowPane.getChildren().add(new HBox(5, gp, createEntityLink(art.getGroupe())));
                }
            }
            if (sectionMorceaux != null) { sectionMorceaux.setVisible(true); sectionMorceaux.setManaged(true); }
            if (morceauxTitleLabel != null) morceauxTitleLabel.setText("Morceaux populaires");
            if (mainController != null && mainController.getSystem() != null)
                populateMorceauxList(mainController.getSystem().getCatalogue().getMorceauxDeArtiste(art));
            if (detailsPlayButton != null) detailsPlayButton.setVisible(false);
        } else if (item instanceof Groupe) {
            Groupe g = (Groupe) item;
            if (detailsSubtitle != null) detailsSubtitle.setText("GROUPE"); 
            if (detailsTitle != null) detailsTitle.setText(g.getNom());
            if (detailsLabel1 != null) detailsLabel1.setText(g.getDescription()); 
            if (detailsLabel2 != null) detailsLabel2.setText(g.getMembres().size() + " membres");
            if (sectionArtistes != null) {
                sectionArtistes.setVisible(true); sectionArtistes.setManaged(true);
                if (artistsFlowPane != null) {
                    artistsFlowPane.getChildren().clear();
                    for (Artiste m : g.getMembres()) {
                        Label nameLbl = new Label(m.getNom());
                        nameLbl.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 15; -fx-background-color: #282828; -fx-background-radius: 20;");
                        artistsFlowPane.getChildren().add(nameLbl);
                    }
                }
            }
            if (sectionMorceaux != null) { sectionMorceaux.setVisible(true); sectionMorceaux.setManaged(true); }
            if (morceauxTitleLabel != null) morceauxTitleLabel.setText("Discographie");
            if (mainController != null && mainController.getSystem() != null)
                populateMorceauxList(mainController.getSystem().getCatalogue().getMorceauxDeGroupe(g));
            if (detailsPlayButton != null) detailsPlayButton.setVisible(false);
        }
    }

    private Hyperlink createEntityLink(Object entity) {
        String name = (entity instanceof Artiste) ? ((Artiste)entity).getNom() : ((Groupe)entity).getNom();
        Hyperlink link = new Hyperlink(name);
        link.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-underline: false; -fx-padding: 0;");
        link.setOnAction(e -> showDetails(entity));
        return link;
    }

    private void populateMorceauxList(List<Morceau> list) {
        if (morceauxVBox == null || list == null) return;
        morceauxVBox.getChildren().clear();
        for (Morceau m : list) {
            HBox row = new HBox(15); row.setAlignment(Pos.CENTER_LEFT);
            row.getStyleClass().add("list-cell"); row.setStyle("-fx-padding: 10; -fx-background-radius: 5;");
            Hyperlink titleLink = new Hyperlink(m.getTitre());
            titleLink.setStyle("-fx-text-fill: white; -fx-padding: 0; -fx-underline: false;");
            titleLink.setOnAction(e -> showDetails(m));
            HBox.setHgrow(titleLink, Priority.ALWAYS);
            Label duration = new Label(m.getDureeFormatee()); duration.setStyle("-fx-text-fill: #b3b3b3;");
            Button p = new Button((m.equals(currentMorceau) && isPlaying) ? "⏸" : "▶"); p.getStyleClass().add("button-primary");
            p.setStyle("-fx-padding: 5 10; -fx-font-size: 10px;"); p.setOnAction(e -> handleItemPlayClick(m));
            row.getChildren().addAll(p, titleLink, duration);
            morceauxVBox.getChildren().add(row);
        }
    }

    private void updatePlayButtonText(Morceau m) {
        if (detailsPlayButton != null) detailsPlayButton.setText((m.equals(currentMorceau) && isPlaying) ? "PAUSE" : "LIRE");
    }

    private void handleItemPlayClick(Morceau m) {
        if (m == null) return;
        if (m.equals(currentMorceau)) togglePlayPause();
        else { currentQueue.clear(); playMorceau(m); }
        if (artistsHomePane != null) chargerAccueilSectons();
        if (searchResults != null) searchResults.refresh();
        updatePlayButtonText(m);
    }

    @FXML private void handlePlayPause(ActionEvent event) {
        if (currentMorceau == null) {
            if (mainController != null) {
                List<Morceau> tous = mainController.getTousLesMorceaux();
                if (!tous.isEmpty()) playMorceau(tous.get(0));
            }
        } else togglePlayPause();
    }

    @FXML
    private void handleNext(ActionEvent event) {
        if (!currentQueue.isEmpty() && queueIndex < currentQueue.size() - 1) {
            queueIndex++;
            playMorceau(currentQueue.get(queueIndex));
        }
    }

    @FXML
    private void handlePrevious(ActionEvent event) {
        if (!currentQueue.isEmpty() && queueIndex > 0) {
            queueIndex--;
            playMorceau(currentQueue.get(queueIndex));
        } else {
            // Recommencer le morceau actuel
            playMorceau(currentMorceau);
        }
    }

    private void togglePlayPause() {
        isPlaying = !isPlaying;
        if (staticMainPlayButton != null) staticMainPlayButton.setText(isPlaying ? "⏸" : "▶");
        if (isPlaying) startProgress();
        else if (progressTimeline != null) progressTimeline.pause();
        if (artistsHomePane != null) chargerAccueilSectons();
        if (searchResults != null) searchResults.refresh();
        if (currentMorceau != null) updatePlayButtonText(currentMorceau);
    }

    public void playMorceau(Morceau m) {
        if (m == null || mainController == null) return;
        try {
            mainController.ecouterMorceau(m);
            currentMorceau = m; isPlaying = true; currentSeconds = 0;
            syncPlayerUI(); updateGuestCounter(); startProgress();
        } catch (Exception e) { showAlert("Information", e.getMessage()); }
    }

    private void startProgress() {
        if (progressTimeline != null) progressTimeline.stop();
        progressTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (isPlaying && currentMorceau != null) {
                currentSeconds++;
                if (currentSeconds > currentMorceau.getDureeSecondes()) {
                    currentSeconds = 0;
                    if (!currentQueue.isEmpty() && queueIndex < currentQueue.size() - 1) {
                        queueIndex++;
                        playMorceau(currentQueue.get(queueIndex));
                    } else {
                        isPlaying = false;
                        if (staticMainPlayButton != null) staticMainPlayButton.setText("▶");
                        progressTimeline.stop();
                    }
                } else {
                    if (staticProgressSlider != null) staticProgressSlider.setValue(currentSeconds);
                    if (staticCurrentTimeLabel != null) staticCurrentTimeLabel.setText(formatTime((int)currentSeconds));
                }
            }
        }));
        progressTimeline.setCycleCount(Timeline.INDEFINITE); progressTimeline.play();
    }

    private String formatTime(int seconds) { return String.format("%d:%02d", seconds / 60, seconds % 60); }

    @FXML private void handleLogin(ActionEvent event) {
        if (loginField == null || passwordField == null || mainController == null) return;
        String id = loginField.getText(); String pwd = passwordField.getText();
        String role = (roleComboBox != null) ? roleComboBox.getValue() : "Abonné";
        if ("Abonné".equals(role) ? mainController.loginAbonne(id, pwd) : mainController.loginAdministrateur(id, pwd)) switchToMainView();
        else showAlert("Erreur", "Identifiant ou mot de passe incorrect.");
    }

    @FXML private void handleRegister(ActionEvent event) {
        if (loginField == null || passwordField == null || mainController == null) return;
        String id = loginField.getText(); String pwd = passwordField.getText();
        if (id.isEmpty() || pwd.isEmpty()) { showAlert("Erreur", "Veuillez remplir tous les champs."); return; }
        try { mainController.registerAbonne(id, pwd); showAlert("Succès", "Compte créé !"); }
        catch (Exception e) { showAlert("Erreur", e.getMessage()); }
    }

    @FXML private void handleVisitor(ActionEvent event) { if (mainController != null) { mainController.continuerCommeVisiteur(); switchToMainView(); } }
    @FXML private void handleLogout(ActionEvent event) { if (mainController != null) mainController.deconnecter(); isPlaying = false; currentMorceau = null; switchToLoginView(); }

    private void switchToMainView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
            Parent root = loader.load();
            ViewController c = loader.getController();
            c.showHome(null);
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
