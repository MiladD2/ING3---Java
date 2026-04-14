package model;

public class VisiteurSession {

    public static final int LIMITE_PAR_DEFAUT = 5;

    private final int limiteEcoutes;
    private int nombreEcoutes;

    public VisiteurSession() {
        this(LIMITE_PAR_DEFAUT);
    }

    public VisiteurSession(int limiteEcoutes) {
        if (limiteEcoutes <= 0) {
            throw new IllegalArgumentException("La limite d'écoutes doit être strictement positive.");
        }
        this.limiteEcoutes = limiteEcoutes;
        this.nombreEcoutes = 0;
    }

    public int getLimiteEcoutes() {
        return limiteEcoutes;
    }

    public int getNombreEcoutes() {
        return nombreEcoutes;
    }

    public int getEcoutesRestantes() {
        return limiteEcoutes - nombreEcoutes;
    }

    public boolean peutEcouter() {
        return nombreEcoutes < limiteEcoutes;
    }

    public boolean limiteAtteinte() {
        return !peutEcouter();
    }

    public void enregistrerEcoute() {
        if (!peutEcouter()) {
            throw new IllegalStateException("La limite d'écoutes du visiteur a été atteinte.");
        }
        nombreEcoutes++;
    }

    public void ecouterMorceau(Morceau morceau) {
        if (morceau == null) {
            throw new IllegalArgumentException("Le morceau ne peut pas être nul.");
        }
        enregistrerEcoute();
    }

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