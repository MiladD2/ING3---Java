package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Représente le système principal de l'application Javazik.
 *
 * <p>Cette classe centralise le catalogue, les utilisateurs,
 * les connexions et la gestion de la session visiteur.</p>
 */
public class JavazikSystem implements Serializable {

    private final Catalogue catalogue;
    private final Map<String, Utilisateur> utilisateurs;

    private Utilisateur utilisateurConnecte;
    private VisiteurSession visiteurSession;

    /**
     * Construit un système Javazik avec un catalogue vide.
     */
    public JavazikSystem() {
        this(new Catalogue());
    }

    /**
     * Construit un système Javazik à partir d'un catalogue donné.
     *
     * @param catalogue le catalogue à utiliser
     * @throws IllegalArgumentException si le catalogue est nul
     */
    public JavazikSystem(Catalogue catalogue) {
        if (catalogue == null) {
            throw new IllegalArgumentException("Le catalogue ne peut pas être nul.");
        }

        this.catalogue = catalogue;
        this.utilisateurs = new LinkedHashMap<>();
        this.utilisateurConnecte = null;
        this.visiteurSession = null;
    }

    /**
     * Retourne le catalogue du système.
     *
     * @return le catalogue musical
     */
    public Catalogue getCatalogue() {
        return catalogue;
    }

    /**
     * Retourne l'utilisateur actuellement connecté.
     *
     * @return l'utilisateur connecté, ou {@code null} si aucun utilisateur n'est connecté
     */
    public Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    /**
     * Retourne la session visiteur en cours.
     *
     * @return la session visiteur, ou {@code null} si le système n'est pas en mode visiteur
     */
    public VisiteurSession getVisiteurSession() {
        return visiteurSession;
    }

    /**
     * Indique si un utilisateur est connecté.
     *
     * @return {@code true} si un utilisateur est connecté, sinon {@code false}
     */
    public boolean estUtilisateurConnecte() {
        return utilisateurConnecte != null;
    }

    /**
     * Indique si le système est en mode visiteur.
     *
     * @return {@code true} si une session visiteur est active, sinon {@code false}
     */
    public boolean estEnModeVisiteur() {
        return visiteurSession != null;
    }

    /**
     * Indique si l'utilisateur connecté est un abonné.
     *
     * @return {@code true} si un abonné est connecté, sinon {@code false}
     */
    public boolean estUnAbonneConnecte() {
        return utilisateurConnecte instanceof Abonne;
    }

    /**
     * Indique si l'utilisateur connecté est un administrateur.
     *
     * @return {@code true} si un administrateur est connecté, sinon {@code false}
     */
    public boolean estUnAdministrateurConnecte() {
        return utilisateurConnecte instanceof Administrateur;
    }

    /**
     * Retourne l'abonné connecté.
     *
     * @return l'abonné connecté, ou {@code null} si aucun abonné n'est connecté
     */
    public Abonne getAbonneConnecte() {
        if (!estUnAbonneConnecte()) {
            return null;
        }
        return (Abonne) utilisateurConnecte;
    }

    /**
     * Retourne l'administrateur connecté.
     *
     * @return l'administrateur connecté, ou {@code null} si aucun administrateur n'est connecté
     */
    public Administrateur getAdministrateurConnecte() {
        if (!estUnAdministrateurConnecte()) {
            return null;
        }
        return (Administrateur) utilisateurConnecte;
    }

    /**
     * Ajoute un abonné au système.
     *
     * @param abonne l'abonné à ajouter
     * @return {@code true} si l'abonné a été ajouté, sinon {@code false}
     * @throws IllegalArgumentException si l'abonné est nul
     */
    public boolean ajouterAbonne(Abonne abonne) {
        validerUtilisateur(abonne);

        if (identifiantExiste(abonne.getIdentifiant())) {
            return false;
        }

        utilisateurs.put(abonne.getIdentifiant(), abonne);
        return true;
    }

    /**
     * Ajoute un administrateur au système.
     *
     * @param administrateur l'administrateur à ajouter
     * @return {@code true} si l'administrateur a été ajouté, sinon {@code false}
     * @throws IllegalArgumentException si l'administrateur est nul
     */
    public boolean ajouterAdministrateur(Administrateur administrateur) {
        validerUtilisateur(administrateur);

        if (identifiantExiste(administrateur.getIdentifiant())) {
            return false;
        }

        utilisateurs.put(administrateur.getIdentifiant(), administrateur);
        return true;
    }

    /**
     * Crée un nouveau compte abonné et l'ajoute au système.
     *
     * @param identifiant l'identifiant du nouvel abonné
     * @param motDePasse le mot de passe du nouvel abonné
     * @return l'abonné créé
     * @throws IllegalArgumentException si l'identifiant existe déjà
     *         ou si les informations sont invalides
     */
    public Abonne creerCompteAbonne(String identifiant, String motDePasse) {
        if (identifiantExiste(identifiant)) {
            throw new IllegalArgumentException("Un utilisateur avec cet identifiant existe déjà.");
        }

        Abonne abonne = new Abonne(identifiant, motDePasse);
        utilisateurs.put(abonne.getIdentifiant(), abonne);
        return abonne;
    }

    /**
     * Authentifie un administrateur.
     *
     * @param identifiant l'identifiant saisi
     * @param motDePasse le mot de passe saisi
     * @return l'administrateur connecté
     * @throws IllegalArgumentException si les identifiants sont incorrects
     *         ou si le compte n'est pas un administrateur
     */
    public Administrateur connecterAdministrateur(String identifiant, String motDePasse) {
        Utilisateur utilisateur = authentifierUtilisateur(identifiant, motDePasse);

        if (!(utilisateur instanceof Administrateur)) {
            throw new IllegalArgumentException("Ce compte n'est pas un administrateur.");
        }

        utilisateurConnecte = utilisateur;
        visiteurSession = null;
        return (Administrateur) utilisateur;
    }

    /**
     * Authentifie un abonné.
     *
     * @param identifiant l'identifiant saisi
     * @param motDePasse le mot de passe saisi
     * @return l'abonné connecté
     * @throws IllegalArgumentException si les identifiants sont incorrects
     *         ou si le compte n'est pas un abonné
     * @throws IllegalStateException si le compte abonné est suspendu
     */
    public Abonne connecterAbonne(String identifiant, String motDePasse) {
        Utilisateur utilisateur = authentifierUtilisateur(identifiant, motDePasse);

        if (!(utilisateur instanceof Abonne)) {
            throw new IllegalArgumentException("Ce compte n'est pas un abonné.");
        }

        Abonne abonne = (Abonne) utilisateur;

        if (abonne.estSuspendu()) {
            throw new IllegalStateException("Ce compte abonné est suspendu.");
        }

        utilisateurConnecte = abonne;
        visiteurSession = null;
        return abonne;
    }

    /**
     * Démarre une nouvelle session visiteur.
     *
     * @return la session visiteur créée
     */
    public VisiteurSession continuerCommeVisiteur() {
        utilisateurConnecte = null;
        visiteurSession = new VisiteurSession();
        return visiteurSession;
    }

    /**
     * Déconnecte l'utilisateur ou ferme la session visiteur en cours.
     */
    public void deconnecter() {
        utilisateurConnecte = null;
        visiteurSession = null;
    }

    /**
     * Recherche un abonné à partir de son identifiant.
     *
     * @param identifiant l'identifiant recherché
     * @return l'abonné trouvé, ou {@code null} si aucun abonné ne correspond
     * @throws IllegalArgumentException si l'identifiant est vide
     */
    public Abonne rechercherAbonneParIdentifiant(String identifiant) {
        validerIdentifiant(identifiant);

        Utilisateur utilisateur = utilisateurs.get(identifiant.trim());

        if (utilisateur instanceof Abonne) {
            return (Abonne) utilisateur;
        }

        return null;
    }

    /**
     * Suspend un abonné.
     *
     * @param identifiant l'identifiant de l'abonné
     * @return {@code true} si l'opération a été effectuée
     * @throws IllegalArgumentException si aucun abonné n'est trouvé
     */
    public boolean suspendreAbonne(String identifiant) {
        Abonne abonne = rechercherAbonneExistant(identifiant);
        abonne.suspendre();

        if (abonne.equals(utilisateurConnecte)) {
            deconnecter();
        }

        return true;
    }

    /**
     * Réactive un abonné suspendu.
     *
     * @param identifiant l'identifiant de l'abonné
     * @return {@code true} si l'opération a été effectuée
     * @throws IllegalArgumentException si aucun abonné n'est trouvé
     */
    public boolean reactiverAbonne(String identifiant) {
        Abonne abonne = rechercherAbonneExistant(identifiant);
        abonne.reactiver();
        return true;
    }

    /**
     * Supprime un abonné du système.
     *
     * @param identifiant l'identifiant de l'abonné
     * @return {@code true} si l'opération a été effectuée
     * @throws IllegalArgumentException si aucun abonné n'est trouvé
     */
    public boolean supprimerAbonne(String identifiant) {
        Abonne abonne = rechercherAbonneExistant(identifiant);

        if (abonne.equals(utilisateurConnecte)) {
            deconnecter();
        }

        utilisateurs.remove(abonne.getIdentifiant());
        return true;
    }

    /**
     * Retourne la liste des abonnés enregistrés.
     *
     * @return une vue non modifiable des abonnés
     */
    public List<Abonne> getAbonnes() {
        List<Abonne> abonnes = new ArrayList<>();

        for (Utilisateur utilisateur : utilisateurs.values()) {
            if (utilisateur instanceof Abonne) {
                abonnes.add((Abonne) utilisateur);
            }
        }

        return Collections.unmodifiableList(abonnes);
    }

    /**
     * Retourne la liste des administrateurs enregistrés.
     *
     * @return une vue non modifiable des administrateurs
     */
    public List<Administrateur> getAdministrateurs() {
        List<Administrateur> administrateurs = new ArrayList<>();

        for (Utilisateur utilisateur : utilisateurs.values()) {
            if (utilisateur instanceof Administrateur) {
                administrateurs.add((Administrateur) utilisateur);
            }
        }

        return Collections.unmodifiableList(administrateurs);
    }

    /**
     * Retourne le nombre total d'utilisateurs du système.
     *
     * @return le nombre total d'utilisateurs
     */
    public int getNombreUtilisateurs() {
        return utilisateurs.size();
    }

    /**
     * Retourne le nombre d'abonnés du système.
     *
     * @return le nombre d'abonnés
     */
    public int getNombreAbonnes() {
        return getAbonnes().size();
    }

    /**
     * Retourne le nombre d'administrateurs du système.
     *
     * @return le nombre d'administrateurs
     */
    public int getNombreAdministrateurs() {
        return getAdministrateurs().size();
    }

    private Utilisateur authentifierUtilisateur(String identifiant, String motDePasse) {
        validerIdentifiant(identifiant);

        Utilisateur utilisateur = utilisateurs.get(identifiant.trim());

        if (utilisateur == null || !utilisateur.authentifier(motDePasse)) {
            throw new IllegalArgumentException("Identifiant ou mot de passe incorrect.");
        }

        return utilisateur;
    }

    private Abonne rechercherAbonneExistant(String identifiant) {
        Abonne abonne = rechercherAbonneParIdentifiant(identifiant);

        if (abonne == null) {
            throw new IllegalArgumentException("Aucun abonné trouvé avec cet identifiant.");
        }

        return abonne;
    }

    private boolean identifiantExiste(String identifiant) {
        validerIdentifiant(identifiant);
        return utilisateurs.containsKey(identifiant.trim());
    }

    private void validerUtilisateur(Utilisateur utilisateur) {
        if (utilisateur == null) {
            throw new IllegalArgumentException("L'utilisateur ne peut pas être nul.");
        }
    }

    private void validerIdentifiant(String identifiant) {
        if (identifiant == null || identifiant.trim().isEmpty()) {
            throw new IllegalArgumentException("L'identifiant ne peut pas être vide.");
        }
    }
}