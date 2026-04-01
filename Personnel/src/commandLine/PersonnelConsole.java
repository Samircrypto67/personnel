package commandLine;

import personnel.*;
import java.util.Scanner;

public class PersonnelConsole {
    private GestionPersonnel gp;
    private Scanner sc;

    public PersonnelConsole() {
        gp = GestionPersonnel.getGestionPersonnel();
        sc = new Scanner(System.in);
    }

    public void start() {
        if (login()) menuPrincipal();
    }

    private boolean login() {
        System.out.println("=== CONNEXION ===");
        System.out.print("Login (root): ");
        String login = sc.nextLine();
        System.out.print("Mot de passe: ");
        String pwd = sc.nextLine();

        if (gp.getRoot().checkPassword(pwd)) {
            System.out.println("Connexion réussie !");
            return true;
        } else {
            System.out.println("Mot de passe incorrect.");
            return false;
        }
    }

    private void menuPrincipal() {
        while (true) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Gérer les ligues");
            System.out.println("2. Gérer les employés");
            System.out.println("3. Se déconnecter");

            int choix = sc.nextInt();
            sc.nextLine();
            switch (choix) {
                case 1: gererLigues(); break;
                case 2: gererEmployes(); break;
                case 3: return;
            }
        }
    }

    private void gererLigues() {
        while (true) {
            System.out.println("\n--- LISTE DES LIGUES ---");
            for (Ligue l : gp.getLigues()) System.out.println(l);
            System.out.println("a. Ajouter | m. Modifier | s. Supprimer | r. Retour");
            String c = sc.nextLine();
            try {
                switch (c) {
                    case "a":
                        System.out.print("Nom: "); String nom = sc.nextLine();
                        gp.addLigue(nom);
                        break;
                    case "m":
                        System.out.print("ID ligue: "); int id = sc.nextInt(); sc.nextLine();
                        System.out.print("Nouveau nom: "); String nn = sc.nextLine();
                        for (Ligue l : gp.getLigues())
                            if (l.getId() == id) gp.updateLigue(l, nn);
                        break;
                    case "s":
                        System.out.print("ID ligue: "); int del = sc.nextInt(); sc.nextLine();
                        gp.getLigues().stream().filter(l -> l.getId() == del).findFirst().ifPresent(gp::removeLigue);
                        break;
                    case "r": return;
                }
            } catch (Exception e) {
                System.out.println("Erreur: " + e.getMessage());
            }
        }
    }

    private void gererEmployes() {
        while (true) {
            System.out.println("\n--- LISTE DES EMPLOYÉS ---");
            for (Employe e : gp.getEmployes()) System.out.println(e);
            System.out.println("a. Ajouter | m. Modifier | s. Supprimer | r. Retour");
            String c = sc.nextLine();
            try {
                switch (c) {
                    case "a":
                        System.out.print("Nom: "); String n = sc.nextLine();
                        System.out.print("Prénom: "); String p = sc.nextLine();
                        System.out.print("Mail: "); String m = sc.nextLine();
                        System.out.print("Password: "); String pw = sc.nextLine();
                        gp.addEmploye(n, p, m, pw, null);
                        break;
                    case "m":
                        System.out.print("Nom employé: "); String nm = sc.nextLine();
                        for (Employe e : gp.getEmployes()) {
                            if (e.getNom().equals(nm)) {
                                System.out.print("Nouveau prénom: "); String pr = sc.nextLine();
                                System.out.print("Nouveau mail: "); String ml = sc.nextLine();
                                System.out.print("Nouveau password: "); String pw2 = sc.nextLine();
                                e.setPrenom(pr); e.setMail(ml); e.setPassword(pw2);
                            }
                        }
                        break;
                    case "s":
                        System.out.print("Nom employé: "); String nd = sc.nextLine();
                        gp.getEmployes().stream().filter(e -> e.getNom().equals(nd) && !e.estRoot()).findFirst().ifPresent(gp::removeEmploye);
                        break;
                    case "r": return;
                }
            } catch (Exception e) {
                System.out.println("Erreur: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new PersonnelConsole().start();
    }
}