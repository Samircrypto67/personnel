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
    //  Ajout de la méthode setNom pour permettre la modification du nom d'un employé
    public void setNom(String nom) {
    this.nom = nom;
    try {
        gestion.update(this);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
public String getPrenom() { return prenom; }
//  Ajout de la méthode setPrenom pour permettre le déclenchement de la modification du prénom d'un employé
   public void setPrenom(String prenom) {
    this.prenom = prenom;
    try {
        gestion.update(this);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getMail() { return mail; }
    //  Ajout de la méthode setMail pour permettre déclenchement de la modification du mail d'un employé
   public void setMail(String mail) {
    this.mail = mail;
    try {
        gestion.update(this);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    public String getPassword() { return password; }
    //  Ajout de la méthode setPassword pour permettre le déclenchement de la modification du mot de passe d'un employé
  public void setPassword(String password) {
    this.password = password;
    try {
        gestion.update(this);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
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
//  Ajout de la méthode remove pour permettre la suppression d'un employé
public void remove() {
    try {
        gestion.remove(this);
    } catch (Exception e) {
        e.printStackTrace();
    }
}