package personnel;

import java.io.Serializable;

public class Employe implements Serializable, Comparable<Employe> {

    private static final long serialVersionUID = 4795721718037994734L;

    private int id = -1; // id pour la BDD
    private String nom, prenom, password, mail;
    private java.sql.Date dateArrivee, dateDepart;
    private Ligue ligue;
    private GestionPersonnel gestionPersonnel;

    // Constructeur normal
    public Employe(GestionPersonnel gestionPersonnel, Ligue ligue, String nom, String prenom,
                   String mail, String password, java.sql.Date dateArrivee, java.sql.Date dateDepart) {
        this.gestionPersonnel = gestionPersonnel;
        this.ligue = ligue;
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.password = password;
        this.dateArrivee = dateArrivee;
        this.dateDepart = dateDepart;
    }

    // Constructeur pour charger depuis BDD
    public Employe(GestionPersonnel gestionPersonnel, Ligue ligue, int id, String nom, String prenom,
                   String mail, String password, java.sql.Date dateArrivee, java.sql.Date dateDepart) {
        this(gestionPersonnel, ligue, nom, prenom, mail, password, dateArrivee, dateDepart);
        this.id = id;
    }

    // Ajout de Getters et setters avec update automatique.
    public int getId() { return id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; try { gestionPersonnel.update(this); } catch (Exception e) {} }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; try { gestionPersonnel.update(this); } catch (Exception e) {} }
    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; try { gestionPersonnel.update(this); } catch (Exception e) {} }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; try { gestionPersonnel.update(this); } catch (Exception e) {} }
    public Ligue getLigue() { return ligue; }
    public void setLigue(Ligue ligue) { this.ligue = ligue; try { gestionPersonnel.update(this); } catch (Exception e) {} }
    public java.sql.Date getDateArrivee() { return dateArrivee; }
    public void setDateArrivee(java.sql.Date dateArrivee) { this.dateArrivee = dateArrivee; try { gestionPersonnel.update(this); } catch (Exception e) {} }
    public java.sql.Date getDateDepart() { return dateDepart; }
    public void setDateDepart(java.sql.Date dateDepart) { this.dateDepart = dateDepart; try { gestionPersonnel.update(this); } catch (Exception e) {} }

    public boolean estRoot() { return gestionPersonnel.getRoot() == this; }

    @Override
    public int compareTo(Employe autre) {
        int cmp = getNom().compareTo(autre.getNom());
        if (cmp != 0) return cmp;
        return getPrenom().compareTo(autre.getPrenom());
    }

    @Override
    public String toString() {
        String res = nom + " " + prenom + " " + mail + " (";
        if (estRoot())
            res += "super-utilisateur";
        else
            res += ligue.toString();
        return res + ")";
    }
}