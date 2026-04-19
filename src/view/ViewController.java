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
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;

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

    // --- FXML FIELDS ---
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

    // Stats View
    @FXML private Label statUserCount;
    @FXML private Label statArtistCount;
    @FXML private Label statSongCount;
    @FXML private Label statAlbumCount;
    @FXML private Label statGroupCount;
    @FXML private Label statTotalPlays;
    @FXML private Label statTopInterprete;
    @FXML private Label statTopAlbum;
    @FXML private Label statTopSong;

    // Home Section
    @FXML private VBox recommandationsSection;
    @FXML private HBox recommandationsPane;
    @FXML private HBox artistsHomePane;
    @FXML private HBox albumsHomePane;
    @FXML private HBox songsHomePane;

    // Search Section
    @FXML private ListView<Object> searchResults;
    @FXML private TextField searchBar;
    @FXML private ToggleButton filterAll;
    @FXML private ToggleButton filterMorceau;
    @FXML private ToggleButton filterAlbum;
    @FXML private ToggleButton filterArtiste;
    @FXML private ToggleButton filterGroupe;

    // Details Section
    @FXML private Label detailsTitle;
    @FXML private Label detailsSubtitle;
    @FXML private Label detailsLabel1;
    @FXML private Label detailsLabel2;
    @FXML private Button detailsPlayButton;
    @FXML private Button detailsAddPlaylistButton;
    @FXML private HBox playlistActionsBox;
    @FXML private Button detailsRenameButton;
    @FXML private Button detailsDeleteButton;
    @FXML private VBox sectionArtistes;
    @FXML private FlowPane artistsFlowPane;
    @FXML private VBox sectionMorceaux;
    @FXML private VBox morceauxVBox;

    // Lists Section
    @FXML private ListView<PlayList> playlistList;
    @FXML private ListView<Ecoute> historyList;
    @FXML private ListView<Object> adminCatalogueList;
    @FXML private ListView<Abonne> adminUserList;
    @FXML private ToggleGroup adminCatalogueFilterGroup;
    @FXML private ToggleButton adminFilterAll;
    @FXML private ToggleButton adminFilterGroupes;
    @FXML private ToggleButton adminFilterArtistes;
    @FXML private ToggleButton adminFilterAlbums;
    @FXML private ToggleButton adminFilterSongs;
    
    // Add Entity View
    @FXML private ToggleGroup addEntityGroup;
    @FXML private ToggleButton addTypeArtiste;
    @FXML private ToggleButton addTypeGroupe;
    @FXML private ToggleButton addTypeAlbum;
    @FXML private ToggleButton addTypeMorceau;
    @FXML private ListView<Object> existingEntitiesList;
    @FXML private GridPane addEntityForm;
    
    private Map<String, Control> formFields = new HashMap<>();
    private int currentRowIndex = 0;

    public static void setMainController(MainController controller) {
        mainController = controller;
    }

    @FXML
    public void initialize() {
        if (currentTrackTitle != null) {
            staticTrackTitle = currentTrackTitle;
            staticTrackArtist = currentTrackArtist;
            staticMainPlayButton = mainPlayButton;
            staticProgressSlider = trackProgressSlider;
            staticCurrentTimeLabel = currentTimeLabel;
            staticTotalTimeLabel = totalTimeLabel;
            staticVolumeSlider = volumeSlider;
            staticGuestRemainingLabel = guestRemainingLabel;
            syncPlayerUI();
            updateGuestCounter();
            setupSliderStyles();
        }
        if (roleComboBox != null) {
            roleComboBox.getItems().setAll("Abonné", "Administrateur");
            roleComboBox.setValue("Abonné");
        }
        updateMenuVisibility();
    }

    private void setupSliderStyles() {
        if (volumeSlider != null) {
            volumeSlider.valueProperty().addListener((obs, o, n) -> updateSliderStyle(volumeSlider));
            updateSliderStyle(volumeSlider);
        }
        if (trackProgressSlider != null) {
            trackProgressSlider.valueProperty().addListener((obs, o, n) -> updateSliderStyle(trackProgressSlider));
            updateSliderStyle(trackProgressSlider);
        }
    }

    private void updateSliderStyle(Slider slider) {
        if (slider == null) return;
        double percentage = (slider.getValue() - slider.getMin()) / (slider.getMax() - slider.getMin()) * 100;
        String style = String.format("-fx-background-color: linear-gradient(to right, #1db954 %f%%, #3e3e3e %f%%);", percentage, percentage);
        Region track = (Region) slider.lookup(".track");
        if (track != null) track.setStyle(style);
    }

    private void syncPlayerUI() {
        if (currentMorceau == null || staticTrackTitle == null) return;
        staticTrackTitle.setText(currentMorceau.getTitre());
        staticTrackArtist.setText(currentMorceau.getInterprete());
        if (staticTotalTimeLabel != null) staticTotalTimeLabel.setText(currentMorceau.getDureeFormatee());
        if (staticMainPlayButton != null) staticMainPlayButton.setText(isPlaying ? "⏸" : "▶");
    }

    private void updateGuestCounter() {
        if (staticGuestRemainingLabel != null && mainController != null && mainController.getSystem() != null && mainController.getSystem().estEnModeVisiteur()) {
            int max = VisiteurSession.LIMITE_PAR_DEFAUT;
            int current = mainController.getSystem().getVisiteurSession().getNombreEcoutes();
            staticGuestRemainingLabel.setText((max - current) + " écoutes restantes");
        }
    }

    private void updateMenuVisibility() {
        if (abonneMenu != null && mainController != null && mainController.getSystem() != null) {
            boolean isAbonne = mainController.getSystem().estUnAbonneConnecte();
            boolean isAdmin = mainController.getSystem().estUnAdministrateurConnecte();
            boolean isGuest = mainController.getSystem().estEnModeVisiteur();
            abonneMenu.setVisible(isAbonne); abonneMenu.setManaged(isAbonne);
            adminMenu.setVisible(isAdmin); adminMenu.setManaged(isAdmin);
            if (guestCounterBox != null) { guestCounterBox.setVisible(isGuest); guestCounterBox.setManaged(isGuest); }
        }
    }

    // --- NAVIGATION ---
    @FXML private void showHome(ActionEvent event) { loadView("HomeView.fxml"); chargerAccueilSectons(); }
    @FXML private void showSearch(ActionEvent event) { loadView("SearchView.fxml"); configurerListViewGenerique(searchResults); handleSearch(null); }
    @FXML private void showPlaylists(ActionEvent event) { loadView("PlaylistView.fxml"); configurerListViewPlaylists(); }
    @FXML private void showHistory(ActionEvent event) { loadView("HistoryView.fxml"); configurerListViewHistory(); }
    
    @FXML private void showAdminDashboard(ActionEvent event) {
        loadView("AdminView.fxml");
        if (adminCatalogueFilterGroup != null) {
            adminCatalogueFilterGroup.selectedToggleProperty().addListener((obs, o, n) -> { if (n != null) refreshAdminCatalogueList(); });
        }
        configurerListViewAdminCatalogue();
        refreshAdminCatalogueList();
        if (adminUserList != null) { configurerListViewUtilisateurs(); adminUserList.getItems().setAll(mainController.getSystem().getAbonnes()); }
    }
    
    @FXML private void showStats(ActionEvent event) { loadView("StatsView.fxml"); calculerStatistiques(); }

    @FXML private void showAddEntityView(ActionEvent event) {
        loadView("AddEntityView.fxml");
        if (addEntityGroup != null) {
            addEntityGroup.selectedToggleProperty().addListener((obs, oldT, newT) -> { if (newT != null) refreshAddEntityView(); });
        }
        refreshAddEntityView();
    }

    private void loadView(String fxmlFile) {
        try {
            artistsHomePane = null; albumsHomePane = null; songsHomePane = null;
            recommandationsSection = null; recommandationsPane = null;
            searchResults = null; searchBar = null;
            detailsTitle = null; detailsSubtitle = null; detailsLabel1 = null; detailsLabel2 = null;
            detailsPlayButton = null; detailsAddPlaylistButton = null; 
            playlistActionsBox = null; detailsRenameButton = null; detailsDeleteButton = null;
            sectionArtistes = null; artistsFlowPane = null; sectionMorceaux = null;
            morceauxVBox = null; playlistList = null; historyList = null;
            adminCatalogueList = null; adminUserList = null; adminCatalogueFilterGroup = null;
            addEntityForm = null; existingEntitiesList = null;
            statUserCount = null; statArtistCount = null; statSongCount = null; statAlbumCount = null; statGroupCount = null; 
            statTotalPlays = null; statTopInterprete = null; statTopAlbum = null; statTopSong = null;

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            loader.setController(this);
            Parent view = loader.load();
            if (contentArea != null) contentArea.getChildren().setAll(view);
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void switchToMainView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
            Parent root = loader.load();
            ViewController c = (ViewController) loader.getController();
            c.showHome(null);
            if (loginField != null && loginField.getScene() != null) loginField.getScene().setRoot(root);
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void switchToLoginView() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("LoginView.fxml"));
            if (contentArea != null && contentArea.getScene() != null) contentArea.getScene().setRoot(root);
        } catch (Exception e) { e.printStackTrace(); }
    }

    // --- ADMIN LOGIC ---
    private void calculerStatistiques() {
        if (statUserCount == null || mainController == null) return;
        JavazikSystem sys = mainController.getSystem();
        statUserCount.setText(String.valueOf(sys.getNombreAbonnes()));
        if (statArtistCount != null) statArtistCount.setText(String.valueOf(sys.getCatalogue().getArtistes().size()));
        statSongCount.setText(String.valueOf(sys.getCatalogue().getNombreMorceaux()));
        statAlbumCount.setText(String.valueOf(sys.getCatalogue().getAlbums().size()));
        statGroupCount.setText(String.valueOf(sys.getCatalogue().getGroupes().size()));
        
        int total = 0;
        for (Morceau m : sys.getCatalogue().getMorceaux()) total += m.getNombreEcoutes();
        statTotalPlays.setText(String.valueOf(total));

        if (statTopInterprete != null) {
            Object topInt = mainController.getTopInterprete();
            statTopInterprete.setText(topInt == null ? "N/A" : (topInt instanceof Artiste ? ((Artiste)topInt).getNom() : ((Groupe)topInt).getNom()));
        }
        if (statTopAlbum != null) {
            Album topAlb = mainController.getTopAlbum();
            statTopAlbum.setText(topAlb == null ? "N/A" : topAlb.getTitre());
        }
        if (statTopSong != null) {
            Morceau topSong = mainController.getTopMorceau();
            statTopSong.setText(topSong == null ? "N/A" : topSong.getTitre() + " (" + topSong.getNombreEcoutes() + ")");
        }
    }

    private void configurerListViewUtilisateurs() {
        if (adminUserList == null) return;
        adminUserList.setCellFactory(param -> new ListCell<Abonne>() {
            private final HBox root = new HBox(15);
            private final Label nameLbl = new Label();
            private final CheckBox suspendCb = new CheckBox("Suspendre");
            private final Region spacer = new Region();
            private final Button deleteBtn = new Button("🗑");
            {
                HBox.setHgrow(spacer, Priority.ALWAYS);
                deleteBtn.setStyle("-fx-text-fill: #ff4444; -fx-background-color: transparent; -fx-font-size: 16px;");
                nameLbl.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
                root.setAlignment(Pos.CENTER_LEFT);
                root.getChildren().addAll(nameLbl, spacer, suspendCb, deleteBtn);
            }
            @Override protected void updateItem(Abonne user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) setGraphic(null);
                else {
                    nameLbl.setText(user.getIdentifiant());
                    suspendCb.setSelected(user.estSuspendu());
                    suspendCb.setOnAction(e -> {
                        if (suspendCb.isSelected()) mainController.suspendreUtilisateur(user.getIdentifiant());
                        else mainController.reactiverUtilisateur(user.getIdentifiant());
                    });
                    deleteBtn.setOnAction(e -> {
                        mainController.supprimerUtilisateur(user.getIdentifiant());
                        adminUserList.getItems().remove(user);
                    });
                    setGraphic(root);
                }
            }
        });
    }

    private void refreshAdminCatalogueList() {
        if (adminCatalogueList == null) return;
        List<Object> filtered = new ArrayList<>();
        JavazikSystem sys = mainController.getSystem();
        Catalogue cat = sys.getCatalogue();
        boolean showAll = adminFilterAll.isSelected();
        if (showAll || adminFilterGroupes.isSelected()) filtered.addAll(cat.getGroupes());
        if (showAll || adminFilterArtistes.isSelected()) filtered.addAll(cat.getArtistes());
        if (showAll || adminFilterAlbums.isSelected()) filtered.addAll(cat.getAlbums());
        if (showAll || adminFilterSongs.isSelected()) filtered.addAll(cat.getMorceaux());
        adminCatalogueList.getItems().setAll(filtered);
    }

    private void configurerListViewAdminCatalogue() {
        if (adminCatalogueList == null) return;
        adminCatalogueList.setCellFactory(param -> new ListCell<Object>() {
            private final HBox root = new HBox(15);
            private final Label iconLbl = new Label();
            private final Label nameLbl = new Label();
            private final Region spacer = new Region();
            private final Button deleteBtn = new Button("🗑");
            {
                HBox.setHgrow(spacer, Priority.ALWAYS);
                deleteBtn.setStyle("-fx-text-fill: #ff4444; -fx-background-color: transparent; -fx-font-size: 14px;");
                iconLbl.setStyle("-fx-font-size: 10px; -fx-padding: 2 6; -fx-background-radius: 4; -fx-text-fill: white;");
                nameLbl.setStyle("-fx-text-fill: white;");
                root.setAlignment(Pos.CENTER_LEFT);
                root.getChildren().addAll(iconLbl, nameLbl, spacer, deleteBtn);
            }
            @Override protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setGraphic(null);
                else {
                    if (item instanceof Groupe) { iconLbl.setText("GP"); iconLbl.setStyle(iconLbl.getStyle() + "-fx-background-color: #1db954;"); nameLbl.setText(((Groupe)item).getNom()); }
                    else if (item instanceof Artiste) { iconLbl.setText("AR"); iconLbl.setStyle(iconLbl.getStyle() + "-fx-background-color: #2e77d0;"); nameLbl.setText(((Artiste)item).getNom()); }
                    else if (item instanceof Album) { iconLbl.setText("AL"); iconLbl.setStyle(iconLbl.getStyle() + "-fx-background-color: #8844ee;"); nameLbl.setText(((Album)item).getTitre()); }
                    else if (item instanceof Morceau) { iconLbl.setText("SO"); iconLbl.setStyle(iconLbl.getStyle() + "-fx-background-color: #333333;"); nameLbl.setText(((Morceau)item).getTitre() + " - " + ((Morceau)item).getInterprete()); }
                    deleteBtn.setOnAction(e -> {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer cet élément et ses dépendances ?", ButtonType.YES, ButtonType.NO);
                        alert.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.YES) {
                                if (item instanceof Morceau) mainController.supprimerMorceau((Morceau)item);
                                else if (item instanceof Album) mainController.supprimerAlbum((Album)item);
                                else if (item instanceof Artiste) mainController.supprimerArtiste((Artiste)item);
                                else if (item instanceof Groupe) mainController.supprimerGroupe((Groupe)item);
                                refreshAdminCatalogueList();
                            }
                        });
                    });
                    setGraphic(root);
                }
            }
        });
    }

    private void refreshAddEntityView() {
        if (addEntityForm == null || existingEntitiesList == null) return;
        addEntityForm.getChildren().clear(); formFields.clear(); currentRowIndex = 0;
        existingEntitiesList.getItems().clear(); configurerListViewGenerique(existingEntitiesList);
        if (addTypeArtiste != null && addTypeArtiste.isSelected()) {
            existingEntitiesList.getItems().setAll(new ArrayList<Object>(mainController.getTousLesArtistes()));
            addFormField("Nom", "nom", "Ex: John Lennon"); addFormField("Biographie", "bio", "Courte description...");
        } else if (addTypeGroupe != null && addTypeGroupe.isSelected()) {
            existingEntitiesList.getItems().setAll(new ArrayList<Object>(mainController.getTousLesGroupes()));
            addFormField("Nom", "nom", "Ex: The Beatles"); addFormField("Description", "desc", "Description du groupe...");
        } else if (addTypeAlbum != null && addTypeAlbum.isSelected()) {
            existingEntitiesList.getItems().setAll(new ArrayList<Object>(mainController.getTousLesAlbums()));
            addFormField("Titre", "titre", "Ex: Abbey Road"); addFormField("Année", "annee", "Ex: 1969"); addComboField("Interprète", "interprete", getListInterpretes());
        } else if (addTypeMorceau != null && addTypeMorceau.isSelected()) {
            existingEntitiesList.getItems().setAll(new ArrayList<Object>(mainController.getTousLesMorceaux()));
            addFormField("Titre", "titre", "Ex: Let It Be"); addFormField("Durée (sec)", "duree", "Ex: 240"); addFormField("Genre", "genre", "Ex: Rock"); addComboField("Interprète", "interprete", getListInterpretes());
        }
    }
    private List<Object> getListInterpretes() { List<Object> list = new ArrayList<>(); list.addAll(mainController.getTousLesArtistes()); list.addAll(mainController.getTousLesGroupes()); return list; }
    private void addFormField(String label, String key, String prompt) { addEntityForm.add(new Label(label + " :"), 0, currentRowIndex); TextField tf = new TextField(); tf.setPromptText(prompt); addEntityForm.add(tf, 1, currentRowIndex); formFields.put(key, tf); currentRowIndex++; }
    private void addComboField(String label, String key, List<Object> items) { addEntityForm.add(new Label(label + " :"), 0, currentRowIndex); ComboBox<Object> combo = new ComboBox<>(); combo.getItems().setAll(items); combo.setPrefWidth(300); addEntityForm.add(combo, 1, currentRowIndex); formFields.put(key, combo); currentRowIndex++; }
    @FXML private void handleSaveEntity(ActionEvent event) { try { if (addTypeArtiste.isSelected()) mainController.ajouterArtiste(((TextField)formFields.get("nom")).getText(), ((TextField)formFields.get("bio")).getText()); else if (addTypeGroupe.isSelected()) mainController.ajouterGroupe(((TextField)formFields.get("nom")).getText(), ((TextField)formFields.get("desc")).getText()); else if (addTypeAlbum.isSelected()) mainController.ajouterAlbum(((TextField)formFields.get("titre")).getText(), Integer.parseInt(((TextField)formFields.get("annee")).getText()), ((ComboBox<Object>)formFields.get("interprete")).getValue()); else if (addTypeMorceau.isSelected()) mainController.ajouterMorceau(((TextField)formFields.get("titre")).getText(), Integer.parseInt(((TextField)formFields.get("duree")).getText()), ((TextField)formFields.get("genre")).getText(), ((ComboBox<Object>)formFields.get("interprete")).getValue()); refreshAddEntityView(); showAlert("Succès", "Élément ajouté !"); } catch (Exception e) { showAlert("Erreur", e.getMessage()); } }

    // --- LOGIQUE METIER ---
    private void chargerAccueilSectons() {
        if (artistsHomePane == null || mainController == null) return;
        artistsHomePane.getChildren().clear();
        for (Groupe g : mainController.getTousLesGroupes()) artistsHomePane.getChildren().add(createCard(g));
        for (Artiste a : mainController.getTousLesArtistes()) if (a.getGroupe() == null) artistsHomePane.getChildren().add(createCard(a));
        if (albumsHomePane != null) { albumsHomePane.getChildren().clear(); for (Album al : mainController.getTousLesAlbums()) albumsHomePane.getChildren().add(createCard(al)); }
        if (songsHomePane != null) { songsHomePane.getChildren().clear(); for (Morceau m : mainController.getTousLesMorceaux()) { boolean inAlbum = false; for (Album al : mainController.getTousLesAlbums()) if (al.getMorceaux().contains(m)) { inAlbum = true; break; } if (!inAlbum) songsHomePane.getChildren().add(createCard(m)); } }
        
        // Recommandations (Abonne only)
        if (recommandationsSection != null && recommandationsPane != null) {
            List<Object> recs = mainController.getRecommandations();
            if (!recs.isEmpty()) {
                recommandationsSection.setVisible(true); recommandationsSection.setManaged(true);
                recommandationsPane.getChildren().clear();
                for (Object r : recs) recommandationsPane.getChildren().add(createCard(r));
            } else {
                recommandationsSection.setVisible(false); recommandationsSection.setManaged(false);
            }
        }
    }
    private VBox createCard(Object item) { VBox card = new VBox(10); card.getStyleClass().add("morceau-card"); card.setPrefWidth(160); card.setMinWidth(160); String t = ""; String s = ""; if (item instanceof Morceau) { t = ((Morceau)item).getTitre(); s = ((Morceau)item).getInterprete(); } else if (item instanceof Album) { t = ((Album)item).getTitre(); s = ((Album)item).getInterprete(); } else if (item instanceof Artiste) { t = ((Artiste)item).getNom(); s = "Artiste"; } else if (item instanceof Groupe) { t = ((Groupe)item).getNom(); s = "Groupe"; } Hyperlink tl = new Hyperlink(t); tl.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-padding: 0;"); tl.setOnAction(e -> showDetails(item)); Label sl = new Label(s); sl.setStyle("-fx-text-fill: #b3b3b3; -fx-font-size: 11px;"); HBox c = new HBox(5); if (item instanceof Morceau) { Button p = new Button((item.equals(currentMorceau) && isPlaying) ? "⏸" : "▶"); p.getStyleClass().add("button-primary"); p.setOnAction(e -> handleItemPlayClick((Morceau)item)); c.getChildren().add(p); } else if (item instanceof Album) { Button p = new Button("▶"); p.getStyleClass().add("button-primary"); p.setOnAction(e -> playAlbum((Album)item)); c.getChildren().add(p); } card.getChildren().addAll(tl, sl, c); return card; }
    private void playAlbum(Album a) { if (a == null || a.getMorceaux().isEmpty()) return; currentQueue = new ArrayList<>(a.getMorceaux()); queueIndex = 0; playMorceau(currentQueue.get(queueIndex)); }

    public void showDetails(Object item) {
        loadView("DetailsView.fxml"); boolean isAbonne = mainController != null && mainController.getSystem().estUnAbonneConnecte();
        if (playlistActionsBox != null) { playlistActionsBox.setVisible(false); playlistActionsBox.setManaged(false); }
        if (item instanceof Morceau) {
            Morceau m = (Morceau) item;
            if (detailsSubtitle != null) detailsSubtitle.setText("MORCEAU • " + m.getInterprete().toUpperCase());
            if (detailsTitle != null) detailsTitle.setText(m.getTitre());
            if (detailsLabel1 != null) detailsLabel1.setText("Genre : " + m.getGenre());
            if (detailsLabel2 != null) detailsLabel2.setText(m.getDureeFormatee() + " • " + m.getNombreEcoutes() + " écoutes");
            if (artistsFlowPane != null) { artistsFlowPane.getChildren().clear(); if (m.getArtiste() != null) artistsFlowPane.getChildren().add(createEntityLink(m.getArtiste())); if (m.getGroupe() != null) { Label gp = new Label("Groupe : "); gp.setStyle("-fx-text-fill: #b3b3b3;"); artistsFlowPane.getChildren().add(new HBox(5, gp, createEntityLink(m.getGroupe()))); } }
            if (detailsPlayButton != null) { detailsPlayButton.setOnAction(e -> handleItemPlayClick(m)); updatePlayButtonText(m); }
            if (detailsAddPlaylistButton != null) { detailsAddPlaylistButton.setVisible(isAbonne); detailsAddPlaylistButton.setOnAction(e -> handleAddToPlaylist(m)); }
        } else if (item instanceof Album) {
            Album a = (Album) item; if (detailsSubtitle != null) detailsSubtitle.setText("ALBUM"); if (detailsTitle != null) detailsTitle.setText(a.getTitre()); if (detailsLabel1 != null) detailsLabel1.setText(a.getInterprete()); 
            if (detailsLabel2 != null) detailsLabel2.setText(mainController.getEcoutesTotalesAlbum(a) + " écoutes au total");
            if (sectionMorceaux != null) { sectionMorceaux.setVisible(true); sectionMorceaux.setManaged(true); } populateMorceauxList(a.getMorceaux(), null); if (detailsPlayButton != null) { detailsPlayButton.setVisible(true); detailsPlayButton.setOnAction(e -> playAlbum(a)); }
        } else if (item instanceof Artiste) {
            Artiste art = (Artiste) item; if (detailsTitle != null) detailsTitle.setText(art.getNom()); 
            if (detailsLabel2 != null) detailsLabel2.setText(mainController.getEcoutesTotalesArtiste(art) + " écoutes au total");
            if (sectionMorceaux != null) { sectionMorceaux.setVisible(true); sectionMorceaux.setManaged(true); } populateMorceauxList(mainController.getSystem().getCatalogue().getMorceauxDeArtiste(art), null);
        } else if (item instanceof Groupe) {
            Groupe g = (Groupe) item; if (detailsTitle != null) detailsTitle.setText(g.getNom()); 
            if (detailsLabel2 != null) detailsLabel2.setText(mainController.getEcoutesTotalesGroupe(g) + " écoutes au total");
            if (sectionArtistes != null) { sectionArtistes.setVisible(true); sectionArtistes.setManaged(true); if (artistsFlowPane != null) { artistsFlowPane.getChildren().clear(); for (Artiste m : g.getMembres()) { Label nameLbl = new Label(m.getNom()); nameLbl.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 15; -fx-background-color: #282828; -fx-background-radius: 20;"); artistsFlowPane.getChildren().add(nameLbl); } } }
            if (sectionMorceaux != null) { sectionMorceaux.setVisible(true); sectionMorceaux.setManaged(true); } populateMorceauxList(mainController.getSystem().getCatalogue().getMorceauxDeGroupe(g), null);
        }
    }

    private void showPlaylistDetails(PlayList pl) {
        loadView("DetailsView.fxml"); if (detailsTitle != null) detailsTitle.setText(pl.getNom());
        if (playlistActionsBox != null) { playlistActionsBox.setVisible(true); playlistActionsBox.setManaged(true); }
        if (detailsRenameButton != null) detailsRenameButton.setOnAction(e -> handleRenamePlaylist(pl));
        if (detailsDeleteButton != null) detailsDeleteButton.setOnAction(e -> handleDeletePlaylist(pl));
        if (sectionMorceaux != null) { sectionMorceaux.setVisible(true); sectionMorceaux.setManaged(true); }
        populateMorceauxList(pl.getMorceaux(), pl);
        if (detailsPlayButton != null) { detailsPlayButton.setOnAction(e -> { if (!pl.getMorceaux().isEmpty()) { currentQueue = new ArrayList<>(pl.getMorceaux()); queueIndex = 0; playMorceau(currentQueue.get(queueIndex)); } }); }
    }

    private Hyperlink createEntityLink(Object entity) { String name = (entity instanceof Artiste) ? ((Artiste)entity).getNom() : ((Groupe)entity).getNom(); Hyperlink link = new Hyperlink(name); link.setStyle("-fx-text-fill: white; -fx-padding: 0;"); link.setOnAction(e -> showDetails(entity)); return link; }

    private void populateMorceauxList(List<Morceau> list, PlayList fromPlaylist) {
        if (morceauxVBox == null || list == null) return;
        morceauxVBox.getChildren().clear();
        boolean isAbonne = mainController != null && mainController.getSystem().estUnAbonneConnecte();
        for (Morceau m : list) {
            HBox row = new HBox(15); row.setAlignment(Pos.CENTER_LEFT); row.getStyleClass().add("list-cell"); row.setStyle("-fx-padding: 10; -fx-background-radius: 5;");
            Hyperlink titleLink = new Hyperlink(m.getTitre()); titleLink.setStyle("-fx-text-fill: white; -fx-padding: 0;"); titleLink.setOnAction(e -> showDetails(m));
            HBox.setHgrow(titleLink, Priority.ALWAYS);
            Label duration = new Label(m.getDureeFormatee()); duration.setStyle("-fx-text-fill: #b3b3b3;");
            Button p = new Button((m.equals(currentMorceau) && isPlaying) ? "⏸" : "▶"); p.getStyleClass().add("button-primary"); p.setStyle("-fx-padding: 5 10; -fx-font-size: 10px;"); p.setOnAction(e -> handleItemPlayClick(m));
            row.getChildren().addAll(p, titleLink, duration);
            if (isAbonne) {
                if (fromPlaylist != null) {
                    Button delBtn = new Button("🗑"); delBtn.setStyle("-fx-text-fill: #ff4444; -fx-background-color: transparent;");
                    delBtn.setOnAction(e -> { mainController.retirerMorceauDePlaylist(fromPlaylist.getNom(), m); showPlaylistDetails(fromPlaylist); });
                    row.getChildren().add(delBtn);
                } else {
                    Button addBtn = new Button("+"); addBtn.getStyleClass().add("button-secondary"); addBtn.setStyle("-fx-padding: 2 10; -fx-font-size: 14px;");
                    addBtn.setOnAction(e -> handleAddToPlaylist(m));
                    row.getChildren().add(addBtn);
                }
            }
            morceauxVBox.getChildren().add(row);
        }
    }

    private void handleItemPlayClick(Morceau m) { if (m == null) return; if (m.equals(currentMorceau)) togglePlayPause(); else { currentQueue.clear(); playMorceau(m); } refreshAllVisibleViews(); }
    private void refreshAllVisibleViews() { if (artistsHomePane != null) chargerAccueilSectons(); if (searchResults != null) searchResults.refresh(); if (currentMorceau != null) updatePlayButtonText(currentMorceau); }
    public void playMorceau(Morceau m) { if (m == null || mainController == null) return; try { mainController.ecouterMorceau(m); currentMorceau = m; isPlaying = true; currentSeconds = 0; if (staticTrackTitle != null) { staticTrackTitle.setText(m.getTitre()); staticTrackArtist.setText(m.getInterprete()); if (staticTotalTimeLabel != null) staticTotalTimeLabel.setText(m.getDureeFormatee()); if (staticMainPlayButton != null) staticMainPlayButton.setText("⏸"); if (staticProgressSlider != null) { staticProgressSlider.setMax(m.getDureeSecondes()); staticProgressSlider.setValue(0); } } updateGuestCounter(); startProgress(); } catch (Exception e) { showAlert("Information", e.getMessage()); } }
    private void startProgress() { if (progressTimeline != null) progressTimeline.stop(); progressTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> { if (isPlaying && currentMorceau != null) { currentSeconds++; if (currentSeconds > currentMorceau.getDureeSecondes()) { currentSeconds = 0; if (!currentQueue.isEmpty() && queueIndex < currentQueue.size() - 1) { queueIndex++; playMorceau(currentQueue.get(queueIndex)); } else { isPlaying = false; if (staticMainPlayButton != null) staticMainPlayButton.setText("▶"); progressTimeline.stop(); } } else { if (staticProgressSlider != null) staticProgressSlider.setValue(currentSeconds); if (staticCurrentTimeLabel != null) staticCurrentTimeLabel.setText(formatTime((int)currentSeconds)); } } })); progressTimeline.setCycleCount(Timeline.INDEFINITE); progressTimeline.play(); }
    private void togglePlayPause() { isPlaying = !isPlaying; if (staticMainPlayButton != null) staticMainPlayButton.setText(isPlaying ? "⏸" : "▶"); if (isPlaying) { if (progressTimeline != null) progressTimeline.play(); else startProgress(); } else if (progressTimeline != null) progressTimeline.pause(); refreshAllVisibleViews(); }
    @FXML private void handlePlayPause(ActionEvent event) { if (currentMorceau == null) { if (mainController != null) { List<Morceau> tous = mainController.getTousLesMorceaux(); if (!tous.isEmpty()) playMorceau(tous.get(0)); } } else togglePlayPause(); }
    @FXML private void handleNext(ActionEvent event) { if (!currentQueue.isEmpty() && queueIndex < currentQueue.size()-1) { queueIndex++; playMorceau(currentQueue.get(queueIndex)); } }
    @FXML private void handlePrevious(ActionEvent event) { if (!currentQueue.isEmpty() && queueIndex > 0) { queueIndex--; playMorceau(currentQueue.get(queueIndex)); } else playMorceau(currentMorceau); }

    // --- OTHER HELPERS ---
    private void configurerListViewPlaylists() { if (playlistList == null || mainController == null) return; Abonne user = mainController.getSystem().getAbonneConnecte(); if (user != null) playlistList.getItems().setAll(user.getPlayLists()); playlistList.setCellFactory(param -> new ListCell<PlayList>() { @Override protected void updateItem(PlayList item, boolean empty) { super.updateItem(item, empty); if (empty || item == null) setText(null); else setText(item.getNom() + " (" + item.getNombreMorceaux() + " titres)"); } }); playlistList.setOnMouseClicked(e -> { if (e.getClickCount() == 2) { PlayList s = playlistList.getSelectionModel().getSelectedItem(); if (s != null) showPlaylistDetails(s); } }); }
    private void configurerListViewHistory() { if (historyList == null || mainController == null) return; Abonne user = mainController.getSystem().getAbonneConnecte(); if (user != null) { List<Ecoute> rev = new ArrayList<>(user.getHistorique().getEcoutes()); java.util.Collections.reverse(rev); historyList.getItems().setAll(rev); } historyList.setCellFactory(param -> new ListCell<Ecoute>() { @Override protected void updateItem(Ecoute item, boolean empty) { super.updateItem(item, empty); if (empty || item == null) setText(null); else setText(item.getMorceau().getTitre() + " - " + item.getMorceau().getInterprete()); } }); historyList.setOnMouseClicked(e -> { if (e.getClickCount() == 2) { Ecoute s = historyList.getSelectionModel().getSelectedItem(); if (s != null) showDetails(s.getMorceau()); } }); }
    private void handleAddToPlaylist(Morceau m) { if (mainController == null || !mainController.getSystem().estUnAbonneConnecte()) return; Abonne user = mainController.getSystem().getAbonneConnecte(); List<PlayList> playlists = user.getPlayLists(); if (playlists.isEmpty()) createNewPlaylistAndAdd(m); else { List<String> choices = new ArrayList<>(); for (PlayList p : playlists) choices.add(p.getNom()); choices.add("+ Créer une nouvelle playlist"); ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices); dialog.setTitle("Ajout Playlist"); dialog.setHeaderText(m.getTitre()); dialog.showAndWait().ifPresent(c -> { if (c.equals("+ Créer une nouvelle playlist")) createNewPlaylistAndAdd(m); else { mainController.ajouterMorceauAPlaylist(c, m); showAlert("Succès", "Ajouté !"); } }); } }
    private void createNewPlaylistAndAdd(Morceau m) { TextInputDialog d = new TextInputDialog("Ma Playlist"); d.setTitle("Nouvelle Playlist"); d.showAndWait().ifPresent(name -> { if (!name.trim().isEmpty()) { mainController.creerPlaylist(name); mainController.ajouterMorceauAPlaylist(name, m); showAlert("Succès", "Ajouté !"); } }); }
    @FXML private void handleCreatePlaylist(ActionEvent event) { TextInputDialog d = new TextInputDialog("Ma Playlist"); d.showAndWait().ifPresent(n -> { if (!n.trim().isEmpty()) { mainController.creerPlaylist(n); configurerListViewPlaylists(); } }); }
    @FXML private void handleSearch(ActionEvent event) { if (searchResults == null || mainController == null) return; String q = (searchBar != null) ? searchBar.getText() : ""; if (q.trim().isEmpty()) return; List<Object> r = new ArrayList<>(); boolean all = (filterAll != null && filterAll.isSelected()); if (all || (filterMorceau != null && filterMorceau.isSelected())) for (Morceau m : mainController.rechercherMorceaux(q)) r.add(m); if (all || (filterAlbum != null && filterAlbum.isSelected())) for (Album a : mainController.rechercherAlbums(q)) r.add(a); if (all || (filterArtiste != null && filterArtiste.isSelected())) for (Artiste art : mainController.rechercherArtistes(q)) r.add(art); if (all || (filterGroupe != null && filterGroupe.isSelected())) for (Groupe g : mainController.rechercherGroupes(q)) r.add(g); searchResults.getItems().setAll(r); }
    private void handleRenamePlaylist(PlayList pl) { TextInputDialog d = new TextInputDialog(pl.getNom()); d.setTitle("Renommer"); d.showAndWait().ifPresent(n -> { if (!n.trim().isEmpty()) { mainController.renommerPlaylist(pl.getNom(), n); showPlaylistDetails(pl); } }); }
    private void handleDeletePlaylist(PlayList pl) { Alert a = new Alert(Alert.AlertType.CONFIRMATION); a.setHeaderText("Supprimer " + pl.getNom() + " ?"); a.showAndWait().ifPresent(r -> { if (r == ButtonType.OK) { mainController.supprimerPlaylist(pl.getNom()); showPlaylists(null); } }); }
    private void configurerListViewGenerique(ListView<Object> listView) { if (listView == null) return; listView.setCellFactory(p -> new ListCell<Object>() { private final HBox root = new HBox(10); private final Label lbl = new Label(); private final Region s = new Region(); private final Button btn = new Button("▶"); { HBox.setHgrow(s, Priority.ALWAYS); btn.getStyleClass().add("button-primary"); btn.setStyle("-fx-padding: 2 8; -fx-font-size: 10px;"); root.setAlignment(Pos.CENTER_LEFT); root.getChildren().addAll(lbl, s, btn); } @Override protected void updateItem(Object item, boolean empty) { super.updateItem(item, empty); if (empty || item == null) setGraphic(null); else { boolean isCurr = (item instanceof Morceau && item.equals(currentMorceau)); btn.setVisible(item instanceof Morceau); btn.setManaged(item instanceof Morceau); if (item instanceof Morceau) { lbl.setText(((Morceau)item).getTitre() + " - " + ((Morceau)item).getInterprete()); btn.setText((isCurr && isPlaying) ? "⏸" : "▶"); btn.setOnAction(e -> handleItemPlayClick((Morceau)item)); } else if (item instanceof Album) lbl.setText(((Album)item).getTitre()); else if (item instanceof Artiste) lbl.setText(((Artiste)item).getNom()); else if (item instanceof Groupe) lbl.setText(((Groupe)item).getNom()); lbl.setStyle(isCurr ? "-fx-text-fill: #1db954; -fx-font-weight: bold;" : "-fx-text-fill: white;"); setGraphic(root); } } }); listView.setOnMouseClicked(e -> { if (e.getClickCount() == 2) { Object s = listView.getSelectionModel().getSelectedItem(); if (s != null) showDetails(s); } }); }
    private void configurerListViewMorceaux(ListView<Morceau> listView) { if (listView == null) return; listView.setCellFactory(param -> new ListCell<Morceau>() { @Override protected void updateItem(Morceau item, boolean empty) { super.updateItem(item, empty); if (empty || item == null) setText(null); else setText(item.getTitre() + " - " + item.getInterprete()); } }); }
    private void updatePlayButtonText(Morceau m) { if (detailsPlayButton != null) detailsPlayButton.setText((m != null && m.equals(currentMorceau) && isPlaying) ? "⏸" : "▶"); }
    private String formatTime(int s) { return String.format("%d:%02d", s / 60, s % 60); }
    private void showAlert(String t, String c) { Alert a = new Alert(Alert.AlertType.INFORMATION); a.setTitle(t); a.setHeaderText(null); a.setContentText(c); a.showAndWait(); }

    // --- AUTHENTICATION ---
    @FXML private void handleLogin(ActionEvent event) {
        if (loginField == null || passwordField == null || mainController == null) return;
        String id = loginField.getText().trim(); String pwd = passwordField.getText();
        String role = (roleComboBox != null && roleComboBox.getValue() != null) ? roleComboBox.getValue() : "Abonné";
        
        boolean success = false;
        if ("Abonné".equals(role)) {
            success = mainController.loginAbonne(id, pwd);
        } else {
            success = mainController.loginAdministrateur(id, pwd);
        }

        if (success) {
            switchToMainView();
        } else {
            showAlert("Erreur", "Identifiant ou mot de passe incorrect (Vérifiez également votre rôle).");
        }
    }
    @FXML private void handleRegister(ActionEvent event) { if (loginField == null || passwordField == null || mainController == null) return; String id = loginField.getText(); String pwd = passwordField.getText(); if (id.isEmpty() || pwd.isEmpty()) { showAlert("Erreur", "Veuillez remplir tous les champs."); return; } try { mainController.registerAbonne(id, pwd); showAlert("Succès", "Compte créé !"); } catch (Exception e) { showAlert("Erreur", e.getMessage()); } }
    @FXML private void handleVisitor(ActionEvent event) { if (mainController != null) { mainController.continuerCommeVisiteur(); switchToMainView(); } }
    @FXML private void handleLogout(ActionEvent event) { if (mainController != null) mainController.deconnecter(); isPlaying = false; currentMorceau = null; switchToLoginView(); }
}
