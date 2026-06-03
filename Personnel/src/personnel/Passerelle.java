package personnel;

public interface Passerelle {

    GestionPersonnel getGestionPersonnel();

    void sauvegarderGestionPersonnel(GestionPersonnel gestionPersonnel)
            throws SauvegardeImpossible;

    int insert(Ligue ligue)
            throws SauvegardeImpossible;

    int insert(Employe employe)
            throws SauvegardeImpossible;

    void update(Ligue ligue)
            throws SauvegardeImpossible;

    void update(Employe employe)
            throws SauvegardeImpossible;

    void updateAdministrateur(Ligue ligue)
            throws SauvegardeImpossible;

    void delete(Ligue ligue)
            throws SauvegardeImpossible;

    void delete(Employe employe)
            throws SauvegardeImpossible;
}