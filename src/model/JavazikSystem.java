package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JavazikSystem implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Catalogue catalogue;
    private final Map<String, Utilisateur> utilisateurs;

    private Utilisateur utilisateurConnecte;
    private VisiteurSession visiteurSession;

    public JavazikSystem() {
        this(new Catalogue());
    }

    public JavazikSystem(Catalogue catalogue) {
        if (catalogue == null) {
            throw new IllegalArgumentException("Le catalogue ne peut pas être nul.");
        }

        this.catalogue = catalogue;
        this.utilisateurs = new LinkedHashMap<>();
        this.utilisateurConnecte = null;
        this.visiteurSession = null;
    }

    public Catalogue getCatalogue() {
        return catalogue;
    }

    public Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    public VisiteurSession getVisiteurSession() {
        return visiteurSession;
    }

    public boolean estUtilisateurConnecte() {
        return utilisateurConnecte != null;
    }

    public boolean estEnModeVisiteur() {
        return visiteurSession != null;
    }

    public boolean estUnAbonneConnecte() {
        return utilisateurConnecte instanceof Abonne;
    }

    public boolean estUnAdministrateurConnecte() {
        return utilisateurConnecte instanceof Administrateur;
    }

    public Abonne getAbonneConnecte() {
        if (!estUnAbonneConnecte()) {
            return null;
        }
        return (Abonne) utilisateurConnecte;
    }

    public Administrateur getAdministrateurConnecte() {
        if (!estUnAdministrateurConnecte()) {
            return null;
        }
        return (Administrateur) utilisateurConnecte;
    }

    public boolean ajouterAbonne(Abonne abonne) {
        validerUtilisateur(abonne);

        if (identifiantExiste(abonne.getIdentifiant())) {
            return false;
        }

        utilisateurs.put(abonne.getIdentifiant(), abonne);
        return true;
    }

    public boolean ajouterAdministrateur(Administrateur administrateur) {
        validerUtilisateur(administrateur);

        if (identifiantExiste(administrateur.getIdentifiant())) {
            return false;
        }

        utilisateurs.put(administrateur.getIdentifiant(), administrateur);
        return true;
    }

    public Abonne creerCompteAbonne(String identifiant, String motDePasse) {
        if (identifiantExiste(identifiant)) {
            throw new IllegalArgumentException("Un utilisateur avec cet identifiant existe déjà.");
        }

        Abonne abonne = new Abonne(identifiant, motDePasse);
        utilisateurs.put(abonne.getIdentifiant(), abonne);
        return abonne;
    }

    public Administrateur connecterAdministrateur(String identifiant, String motDePasse) {
        Utilisateur utilisateur = authentifierUtilisateur(identifiant, motDePasse);

        if (!(utilisateur instanceof Administrateur)) {
            throw new IllegalArgumentException("Ce compte n'est pas un administrateur.");
        }

        utilisateurConnecte = utilisateur;
        visiteurSession = null;
        return (Administrateur) utilisateur;
    }

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

    public VisiteurSession continuerCommeVisiteur() {
        utilisateurConnecte = null;
        visiteurSession = new VisiteurSession();
        return visiteurSession;
    }

    public void deconnecter() {
        utilisateurConnecte = null;
        visiteurSession = null;
    }

    public Abonne rechercherAbonneParIdentifiant(String identifiant) {
        validerIdentifiant(identifiant);

        Utilisateur utilisateur = utilisateurs.get(identifiant.trim());

        if (utilisateur instanceof Abonne) {
            return (Abonne) utilisateur;
        }

        return null;
    }

    public boolean suspendreAbonne(String identifiant) {
        Abonne abonne = rechercherAbonneExistant(identifiant);
        abonne.suspendre();

        if (abonne.equals(utilisateurConnecte)) {
            deconnecter();
        }

        return true;
    }

    public boolean reactiverAbonne(String identifiant) {
        Abonne abonne = rechercherAbonneExistant(identifiant);
        abonne.reactiver();
        return true;
    }

    public boolean supprimerAbonne(String identifiant) {
        Abonne abonne = rechercherAbonneExistant(identifiant);

        if (abonne.equals(utilisateurConnecte)) {
            deconnecter();
        }

        utilisateurs.remove(abonne.getIdentifiant());
        return true;
    }

    public List<Abonne> getAbonnes() {
        List<Abonne> abonnes = new ArrayList<>();

        for (Utilisateur utilisateur : utilisateurs.values()) {
            if (utilisateur instanceof Abonne) {
                abonnes.add((Abonne) utilisateur);
            }
        }

        return Collections.unmodifiableList(abonnes);
    }

    public List<Administrateur> getAdministrateurs() {
        List<Administrateur> administrateurs = new ArrayList<>();

        for (Utilisateur utilisateur : utilisateurs.values()) {
            if (utilisateur instanceof Administrateur) {
                administrateurs.add((Administrateur) utilisateur);
            }
        }

        return Collections.unmodifiableList(administrateurs);
    }

    public int getNombreUtilisateurs() {
        return utilisateurs.size();
    }

    public int getNombreAbonnes() {
        return getAbonnes().size();
    }

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