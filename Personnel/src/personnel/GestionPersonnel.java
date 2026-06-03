package personnel;

import java.util.ArrayList;
import java.util.List;

public class GestionPersonnel {

    private Passerelle passerelle;
    private List<Ligue> ligues;
    private Employe root;

    
    public GestionPersonnel(Passerelle passerelle) {
        this.passerelle = passerelle;
        this.ligues = new ArrayList<>();
    }
    

    // ROOT
    public void addRoot(int id, String nom, String prenom, String mail, String password) {
        this.root = new Employe(this, null, id, nom, prenom, mail, password);
    }

    public Employe getRoot() {
        // 🔥 SÉCURITÉ : si root absent → créer un root temporaire
        if (root == null) {
            root = new Employe(this, null, -1, "root", "", "root", "toor");
        }
        return root;
    }

    // LIGUES
    public Ligue addLigue(int id, String nom) {
        Ligue ligue = new Ligue(this, id, nom);
        ligues.add(ligue);
        return ligue;
    }

    public Ligue addLigue(String nom) {
        Ligue ligue = new Ligue(this, -1, nom);
        try {
            int id = passerelle.insert(ligue);
            ligue.setId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ligues.add(ligue);
        return ligue;
    }

    public List<Ligue> getLigues() {
        return ligues;
    }

    public void remove(Ligue ligue) {
        try {
            passerelle.delete(ligue);
            ligues.remove(ligue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Ligue getLigueById(int id) {
        return ligues.stream().filter(l -> l.getId() == id).findFirst().orElse(null);
    }

    // EMPLOYES
    public Employe addEmploye(String nom, String prenom, String mail, String password, Ligue ligue) {
        Employe e = new Employe(this, 
        		ligue, 
        		-1, 
        		nom, 
        		prenom, 
        		mail, 
        		password);
        e.setDateArrivee(
        	    new java.sql.Date(
        	        System.currentTimeMillis()
        	    )
        	);
        try {
            int id = passerelle.insert(e);
            e.setId(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (ligue != null) ligue.addEmploye(e);
        return e;
    }

    public Employe getEmployeById(int id) {
        for (Ligue l : ligues) {
            for (Employe e : l.getEmployes()) {
                if (e.getId() == id) return e;
            }
        }
        return null;
    }
    public void remove(Employe employe) {
        try {
            passerelle.delete(employe);

            if (employe.getLigue() != null) {
                employe.getLigue().removeEmploye(employe);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateAdministrateur(Ligue ligue)
    {
        try
        {
            passerelle.updateAdministrateur(ligue);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void update(Employe employe) {
        try {
            passerelle.update(employe);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void update(Ligue ligue) {
        try {
            passerelle.update(ligue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }
    