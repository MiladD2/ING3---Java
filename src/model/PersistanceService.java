package model;

import java.io.*;

public class PersistanceService {

    private final String cheminFichier;

    public PersistanceService(String cheminFichier) {
        if (cheminFichier == null || cheminFichier.trim().isEmpty()) {
            throw new IllegalArgumentException("Le chemin du fichier ne peut pas être vide.");
        }
        this.cheminFichier = cheminFichier.trim();
    }

    public String getCheminFichier() {
        return cheminFichier;
    }

    public boolean fichierExiste() {
        File fichier = new File(cheminFichier);
        return fichier.exists() && fichier.isFile();
    }

    public void sauvegarder(JavazikSystem systeme) throws IOException {
        if (systeme == null) {
            throw new IllegalArgumentException("Le système ne peut pas être nul.");
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(cheminFichier))) {
            oos.writeObject(systeme);
        }
    }

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

    public JavazikSystem chargerOuCreerNouveau() throws IOException, ClassNotFoundException {
        if (!fichierExiste()) {
            return new JavazikSystem();
        }
        return charger();
    }
}