package personnel;

import java.io.Serializable;
import java.sql.Date;

public class Employe implements Serializable, Comparable<Employe> {

    /**
	 * 
	 */
	private static final long serialVersionUID = -350718842227171921L;
	private int id = -1;
    private String nom, prenom, mail, password;
    private Ligue ligue;
    private GestionPersonnel gestion;

    private Date dateArrivee;
    private Date dateDepart;

    // CONSTRUCTEUR NORMAL
    public Employe(GestionPersonnel gestion, Ligue ligue, String nom, String prenom, String mail, String password) {
        this.gestion = gestion;
        this.ligue = ligue;
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.password = password;
    }

    // CONSTRUCTEUR AVEC ID (IMPORTANT JDBC)
    public Employe(GestionPersonnel gestion, Ligue ligue, int id, String nom, String prenom, String mail, String password) {
        this(gestion, ligue, nom, prenom, mail, password);
        this.id = id;
    }

    // GETTERS
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getMail() { return mail; }
    public String getPassword() { return password; }
    public Ligue getLigue() { return ligue; }

    public Date getDateArrivee() { return dateArrivee; }
    public Date getDateDepart() { return dateDepart; }

    // SETTERS
    public void setId(int id) { this.id = id; }

    public void setNom(String nom) {
        this.nom = nom;
        try { gestion.update(this); } catch(Exception e){}
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
        try { gestion.update(this); } catch(Exception e){}
    }

    public void setMail(String mail) {
        this.mail = mail;
        try { gestion.update(this); } catch(Exception e){}
    }

    public void setPassword(String password) {
        this.password = password;
        try { gestion.update(this); } catch(Exception e){}
    }

    public void setDateArrivee(Date d)
    {
        this.dateArrivee = d;

        try
        {
            gestion.update(this);
        }
        catch(Exception e)
        {
        }
    }

    public void setDateDepart(Date d)
    {
        this.dateDepart = d;

        try
        {
            gestion.update(this);
        }
        catch(Exception e)
        {
        }
    }
    public boolean estRoot() {
        return this == gestion.getRoot();
    }
    public boolean checkPassword(String pwd) {
        return this.password.equals(pwd);
    }
    @Override
    public int compareTo(Employe o) {
        int cmp = nom.compareTo(o.nom);
        if (cmp != 0) return cmp;
        return prenom.compareTo(o.prenom);
    }

    @Override
    public String toString()
    {
        return nom + " "
                + prenom
                + " ("
                + mail
                + ") Arrivée="
                + dateArrivee
                + " Départ="
                + dateDepart;
    }
    public void remove() {
        try {
            gestion.remove(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}