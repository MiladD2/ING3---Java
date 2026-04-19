package model;

import java.io.*;

/**
 * Gère la sauvegarde et le chargement des données du système.
 *
 * <p>Cette classe permet de persister un objet {@link JavazikSystem}
 * dans un fichier par sérialisation.</p>
 */
public class PersistanceService {

    private final String cheminFichier;

    /**
     * Construit un service de persistance utilisant un chemin de fichier donné.
     *
     * @param cheminFichier le chemin du fichier de sauvegarde
     * @throws IllegalArgumentException si le chemin est vide
     */
    public PersistanceService(String cheminFichier) {
        if (cheminFichier == null || cheminFichier.trim().isEmpty()) {
            throw new IllegalArgumentException("Le chemin du fichier ne peut pas être vide.");
        }
        this.cheminFichier = cheminFichier.trim();
    }

    /**
     * Retourne le chemin du fichier de sauvegarde.
     *
     * @return le chemin du fichier
     */
    public String getCheminFichier() {
        return cheminFichier;
    }

    /**
     * Indique si le fichier de sauvegarde existe.
     *
     * @return {@code true} si le fichier existe et est un fichier normal
     */
    public boolean fichierExiste() {
        File fichier = new File(cheminFichier);
        return fichier.exists() && fichier.isFile();
    }

    /**
     * Sauvegarde l'état du système dans le fichier configuré.
     *
     * @param systeme le système à sauvegarder
     * @throws IllegalArgumentException si le système est nul
     * @throws IOException en cas d'erreur d'écriture
     */
    public void sauvegarder(JavazikSystem systeme) throws IOException {
        if (systeme == null) {
            throw new IllegalArgumentException("Le système ne peut pas être nul.");
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(cheminFichier))) {
            oos.writeObject(systeme);
        }
    }

    /**
     * Charge un système depuis le fichier configuré.
     *
     * @return le système chargé
     * @throws IOException en cas d'erreur de lecture ou de fichier invalide
     * @throws ClassNotFoundException si une classe sérialisée est introuvable
     */
    public JavazikSystem charger() throws IOException, ClassNotFoundException {
        if (!fichierExiste()) {
            throw new FileNotFoundException("Aucun fichier de sauvegarde trouvé.");
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(cheminFichier))) {
            Object objet = ois.readObject();

            if (!(objet instanceof JavazikSystem)) {
                throw new IOException("Le fichier ne contient pas un système Javazik valide.");
            }

            return (JavazikSystem) objet;
        }
    }

    /**
     * Charge un système existant, ou crée un nouveau système si aucun fichier n'existe.
     *
     * @return le système chargé ou un nouveau système vide
     * @throws IOException en cas d'erreur de lecture
     * @throws ClassNotFoundException si une classe sérialisée est introuvable
     */
    public JavazikSystem chargerOuCreerNouveau() throws IOException, ClassNotFoundException {
        if (!fichierExiste()) {
            return new JavazikSystem();
        }
        return charger();
    }
}