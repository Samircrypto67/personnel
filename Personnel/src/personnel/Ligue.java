package personnel;

import java.util.HashSet;
import java.util.Set;

public class Ligue implements Comparable<Ligue> 
{
    private int id;
    public void setId(int id) {
        this.id = id;
    }
    private String nom;
    private GestionPersonnel gp;

    private Set<Employe> employes = new HashSet<>();
    private Employe administrateur;

    public Ligue(GestionPersonnel gp, int id, String nom) 
    {
        this.gp = gp;
        this.id = id;
        this.nom = nom;
    }
   
    public int getId() { return id; }
    public String getNom() { return nom; }

    // 🔥 déclenchement update
    public void setNom(String nom) 
    {
        this.nom = nom;
        try 
        {
            gp.update(this);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    // ===============================
    // EMPLOYES
    // ===============================
    public void addEmploye(Employe e)
    {
        employes.add(e);
    }
    public void removeEmploye(Employe e) {
        employes.remove(e);
    }
    public Set<Employe> getEmployes()
    {
        return employes;
    }

    // ===============================
    // ADMIN
    // ===============================
    public Employe getAdministrateur()
    {
        return administrateur;
    }

    public void setAdministrateur(Employe admin)
    {
        this.administrateur = admin;

        try
        {
            gp.updateAdministrateur(this);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int compareTo(Ligue o) 
    {
        return Integer.compare(this.id, o.id);
    }

    @Override
    public String toString() 
    {
        return nom;
    }
}
