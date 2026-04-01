package personnel;

import java.io.Serializable;

public class Employe implements Serializable, Comparable<Employe> {
    private int id = -1;
    private String nom, prenom, mail, password;
    private Ligue ligue;
    private GestionPersonnel gestion;

    public Employe(GestionPersonnel gestion, Ligue ligue, String nom, String prenom, String mail, String password) {
        this.gestion = gestion;
        this.ligue = ligue;
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.password = password;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Ligue getLigue() { return ligue; }
    public GestionPersonnel getGestionPersonnel() { return gestion; }

    public boolean estRoot() { return this == gestion.getRoot(); }
    public boolean checkPassword(String pwd) { return password.equals(pwd); }

    @Override
    public int compareTo(Employe o) {
        int cmp = nom.compareTo(o.nom);
        if (cmp != 0) return cmp;
        return prenom.compareTo(o.prenom);
    }

    @Override
    public String toString() { return nom + " " + prenom + " (" + mail + ")"; }
}