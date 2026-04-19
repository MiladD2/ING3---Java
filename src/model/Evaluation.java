package model;

import java.io.Serializable;



public class Evaluation implements Serializable {
    private int note; // Par exemple de 1 à 5
    private String commentaire;
    private Abonne auteur;

    public Evaluation(int note, String commentaire, Abonne auteur) {
        this.note = note;
        this.commentaire = commentaire;
        this.auteur = auteur;
    }

    public int getNote() { return note; }
    public void setNote(int note) { this.note = note; }
    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
    public Abonne getAuteur() { return auteur; }
}
