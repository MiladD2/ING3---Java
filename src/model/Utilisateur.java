package model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Représente un utilisateur authentifiable du système Javazik.
 * <p>
 * Cette classe abstraite factorise les données communes aux différents
 * types d'utilisateurs persistants du système, notamment les abonnés
 * et les administrateurs.
 */
public abstract class Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String identifiant;
    private String motDePasse;

    /**
     * Construit un utilisateur avec un identifiant et un mot de passe.
     *
     * @param identifiant identifiant de connexion
     * @param motDePasse mot de passe
     * @throws IllegalArgumentException si une donnée est invalide
     */
    public Utilisateur(String identifiant, String motDePasse) {
        this.identifiant = validerIdentifiant(identifiant);
        this.motDePasse = validerMotDePasse(motDePasse);
    }

    public String getIdentifiant() {
        return identifiant;
    }

    /**
     * Vérifie si le mot de passe fourni correspond à celui de l'utilisateur.
     *
     * @param motDePasse mot de passe saisi
     * @return true si le mot de passe correspond, false sinon
     */
    public boolean authentifier(String motDePasse) {
        return this.motDePasse.equals(Objects.requireNonNull(motDePasse));
    }

    /**
     * Modifie le mot de passe de l'utilisateur.
     *
     * @param nouveauMotDePasse nouveau mot de passe
     * @throws IllegalArgumentException si le mot de passe est invalide
     */
    public void changerMotDePasse(String nouveauMotDePasse) {
        this.motDePasse = validerMotDePasse(nouveauMotDePasse);
    }

    /**
     * Indique le rôle métier de l'utilisateur.
     *
     * @return le rôle sous forme textuelle
     */
    public abstract String getRole();

    private String validerIdentifiant(String identifiant) {
        if (identifiant == null || identifiant.isBlank()) {
            throw new IllegalArgumentException("L'identifiant ne peut pas être vide.");
        }

        String identifiantNettoye = identifiant.trim();

        if (identifiantNettoye.length() < 3) {
            throw new IllegalArgumentException("L'identifiant doit contenir au moins 3 caractères.");
        }

        return identifiantNettoye;
    }

    private String validerMotDePasse(String motDePasse) {
        if (motDePasse == null || motDePasse.isBlank()) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas être vide.");
        }

        if (motDePasse.length() < 4) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 4 caractères.");
        }

        return motDePasse;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "identifiant='" + identifiant + '\'' +
                ", role='" + getRole() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilisateur that)) return false;
        return identifiant.equalsIgnoreCase(that.identifiant);
    }

    @Override
    public int hashCode() {
        return identifiant.toLowerCase().hashCode();
    }
}