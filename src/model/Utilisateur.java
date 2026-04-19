package model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Représente un utilisateur du système Javazik.
 *
 * <p>Cette classe abstraite factorise les informations communes
 * aux abonnés et aux administrateurs, comme l'identifiant,
 * le mot de passe et le rôle.</p>
 */
public abstract class Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String identifiant;
    private String motDePasse;

    /**
     * Construit un utilisateur avec un identifiant et un mot de passe.
     *
     * @param identifiant l'identifiant de l'utilisateur
     * @param motDePasse le mot de passe de l'utilisateur
     * @throws IllegalArgumentException si l'identifiant ou le mot de passe est invalide
     */
    public Utilisateur(String identifiant, String motDePasse) {
        this.identifiant = validerIdentifiant(identifiant);
        this.motDePasse = validerMotDePasse(motDePasse);
    }

    /**
     * Retourne l'identifiant de l'utilisateur.
     *
     * @return l'identifiant de l'utilisateur
     */
    public String getIdentifiant() {
        return identifiant;
    }

    /**
     * Vérifie si le mot de passe fourni correspond à celui de l'utilisateur.
     *
     * @param motDePasse le mot de passe à vérifier
     * @return {@code true} si le mot de passe est correct, sinon {@code false}
     * @throws NullPointerException si le mot de passe fourni est nul
     */
    public boolean authentifier(String motDePasse) {
        return this.motDePasse.equals(Objects.requireNonNull(motDePasse));
    }

    /**
     * Remplace le mot de passe actuel par un nouveau mot de passe valide.
     *
     * @param nouveauMotDePasse le nouveau mot de passe
     * @throws IllegalArgumentException si le nouveau mot de passe est invalide
     */
    public void changerMotDePasse(String nouveauMotDePasse) {
        this.motDePasse = validerMotDePasse(nouveauMotDePasse);
    }

    /**
     * Retourne le rôle de l'utilisateur.
     *
     * @return le rôle de l'utilisateur
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