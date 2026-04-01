package personnel;

public class Ligue implements Comparable<Ligue> {
    private int id;
    private String nom;
    private GestionPersonnel gp;

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