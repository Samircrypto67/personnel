package personnel;

public class Ligue implements Comparable<Ligue> {
    private int id;
    private String nom;
    private GestionPersonnel gp;
    private Employe administrateur;

    public Ligue(GestionPersonnel gp, int id, String nom) {
        this.gp = gp;
        this.id = id;
        this.nom = nom;
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    @Override
    public int compareTo(Ligue o) {
        return Integer.compare(this.id, o.id);
    }

    @Override
    public String toString() {
        return id + " - " + nom;
    }
}
// Ajout de la méthode setNom pour permettre le déclenchement de la modification du nom d'une ligue
public void setNom(String nom) {
    this.nom = nom;
    try {
        gp.update(this);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

// Ajout de la méthode remove pour permettre le déclenchement de la suppression d'une ligue
public void remove() {
    try {
        gp.remove(this);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
// Ajout de la méthode addEmploye pour permettre l'ajout d'un employé à une ligue
private Employe administrateur;

public Employe getAdministrateur() {
    return administrateur;
}

public void setAdministrateur(Employe administrateur)
{
    this.administrateur = administrateur;

    try
    {
        gp.updateAdministrateur(this); // appel BDD
    }
    catch (SauvegardeImpossible e)
    {
        e.printStackTrace();
    }
}


