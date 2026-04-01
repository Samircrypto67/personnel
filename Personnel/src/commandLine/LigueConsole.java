package commandLine;

import personnel.*;
import commandLineMenus.*;

import static commandLineMenus.rendering.examples.util.InOut.*;

public class LigueConsole {

    private GestionPersonnel gestion;
    private EmployeConsole employeConsole;

    public LigueConsole(GestionPersonnel gestion, EmployeConsole employeConsole) {
        this.gestion = gestion;
        this.employeConsole = employeConsole;
    }

    public Menu menuLigues() {
        Menu menu = new Menu("Gestion des ligues", "l");
        for (Ligue l : gestion.getLigues()) {
            menu.add(new Option(l.getNom(), "" + l.getId(), () -> menuLigue(l).start()));
        }
        menu.add(new Option("Ajouter une ligue", "a", () -> ajouterLigue()));
        menu.addBack("r");
        return menu;
    }

    private Menu menuLigue(Ligue ligue) {
        Menu menu = new Menu("Ligue : " + ligue.getNom());
        menu.add(new Option("Modifier ligue", "1", () -> modifierLigue(ligue)));
        menu.add(new Option("Supprimer ligue", "2", () -> supprimerLigue(ligue)));
        return menu;
    }

    private void ajouterLigue() {
        String nom = getString("Nom de la ligue : ");
        try {
            gestion.addLigue(nom);
        } catch (SauvegardeImpossible e) {
            System.out.println("Impossible d'ajouter la ligue.");
        }
    }

    private void modifierLigue(Ligue ligue) {
        String nom = getString("Nouveau nom : ");
        ligue.setNom(nom);
        try { gestion.updateLigue(ligue); } catch (SauvegardeImpossible e) { System.out.println("Impossible de modifier."); }
    }

    private void supprimerLigue(Ligue ligue) {
        gestion.removeLigue(ligue);
        System.out.println("Ligue supprimée.");
    }
}