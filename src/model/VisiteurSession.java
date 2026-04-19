package model;

import java.io.Serializable;

/**
 * Représente une session de visiteur non authentifié.
 *
 * <p>Cette classe permet de limiter le nombre d'écoutes
 * autorisées pendant une session visiteur.</p>
 */
public class VisiteurSession implements Serializable {

    public static final int LIMITE_PAR_DEFAUT = 5;

    private final int limiteEcoutes;
    private int nombreEcoutes;

    /**
     * Construit une session visiteur avec la limite par défaut.
     */
    public VisiteurSession() {
        this(LIMITE_PAR_DEFAUT);
    }

    /**
     * Construit une session visiteur avec une limite d'écoutes donnée.
     *
     * @param limiteEcoutes le nombre maximal d'écoutes autorisées
     * @throws IllegalArgumentException si la limite n'est pas strictement positive
     */
    public VisiteurSession(int limiteEcoutes) {
        if (limiteEcoutes <= 0) {
            throw new IllegalArgumentException("La limite d'écoutes doit être strictement positive.");
        }
        this.limiteEcoutes = limiteEcoutes;
        this.nombreEcoutes = 0;
    }

    /**
     * Retourne la limite d'écoutes de la session.
     *
     * @return la limite d'écoutes
     */
    public int getLimiteEcoutes() {
        return limiteEcoutes;
    }

    /**
     * Retourne le nombre d'écoutes déjà consommées.
     *
     * @return le nombre d'écoutes consommées
     */
    public int getNombreEcoutes() {
        return nombreEcoutes;
    }

    /**
     * Retourne le nombre d'écoutes restantes.
     *
     * @return le nombre d'écoutes encore autorisées
     */
    public int getEcoutesRestantes() {
        return limiteEcoutes - nombreEcoutes;
    }

    /**
     * Indique si le visiteur peut encore écouter un morceau.
     *
     * @return {@code true} si une écoute supplémentaire est possible
     */
    public boolean peutEcouter() {
        return nombreEcoutes < limiteEcoutes;
    }

    /**
     * Indique si la limite d'écoutes a été atteinte.
     *
     * @return {@code true} si la limite est atteinte, sinon {@code false}
     */
    public boolean limiteAtteinte() {
        return !peutEcouter();
    }

    /**
     * Enregistre une écoute dans la session visiteur.
     *
     * @throws IllegalStateException si la limite d'écoutes est atteinte
     */
    public void enregistrerEcoute() {
        if (!peutEcouter()) {
            throw new IllegalStateException("La limite d'écoutes du visiteur a été atteinte.");
        }
        nombreEcoutes++;
    }

    /**
     * Simule l'écoute d'un morceau par un visiteur.
     *
     * @param morceau le morceau écouté
     * @throws IllegalArgumentException si le morceau est nul
     * @throws IllegalStateException si la limite d'écoutes est atteinte
     */
    public void ecouterMorceau(Morceau morceau) {
        if (morceau == null) {
            throw new IllegalArgumentException("Le morceau ne peut pas être nul.");
        }
        enregistrerEcoute();
    }

    /**
     * Réinitialise complètement la session visiteur.
     */
    public void reinitialiserSession() {
        nombreEcoutes = 0;
    }

    @Override
    public String toString() {
        return "VisiteurSession{" +
                "limiteEcoutes=" + limiteEcoutes +
                ", nombreEcoutes=" + nombreEcoutes +
                '}';
    }
}