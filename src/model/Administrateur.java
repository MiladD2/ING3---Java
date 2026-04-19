package model;

import java.io.Serializable;

/**
 * Représente un administrateur du système.
 *
 * <p>Un administrateur est un utilisateur disposant
 * de droits de gestion sur le catalogue et les comptes abonnés.</p>
 */
public class Administrateur extends Utilisateur {

    /**
     * Construit un administrateur.
     *
     * @param identifiant l'identifiant de l'administrateur
     * @param motDePasse le mot de passe de l'administrateur
     */
    public Administrateur(String identifiant, String motDePasse) {
        super(identifiant, motDePasse);
    }

    /**
     * Retourne le rôle de l'utilisateur.
     *
     * @return la chaîne {@code "ADMINISTRATEUR"}
     */
    @Override
    public String getRole() {
        return "ADMINISTRATEUR";
    }

    @Override
    public String toString() {
        return "Administrateur{" +
                "identifiant='" + getIdentifiant() + '\'' +
                '}';
    }
}