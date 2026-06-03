package commandLine;

import java.util.Scanner;
import personnel.*;

public class PersonnelConsole {


	private GestionPersonnel gestion;
    private Scanner scanner;
    private Employe userConnecte;

    public PersonnelConsole(GestionPersonnel gestion) {
        this.gestion = gestion;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        login();
        menuPrincipal();
    }

    private void login() {
        System.out.println("=== CONNEXION ===");
        System.out.print("Login: ");
        String login = scanner.nextLine();

        System.out.print("Mot de passe: ");
        String password = scanner.nextLine();

        Employe root = gestion.getRoot();

        // 🔥 LOGIN FLEXIBLE (nom OU mail)
        if (root != null &&
            (root.getNom().equals(login) || root.getMail().equals(login)) &&
            root.getPassword().equals(password)) {

            userConnecte = root;
            System.out.println("Connexion réussie !");
        } else {
            System.out.println("Erreur de connexion !");
            System.exit(0);
        }
    }

    private void menuPrincipal() {
        while (true) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Gérer les ligues");
            System.out.println("2. Gérer les employés");
            System.out.println("3. Définir administrateur");
            System.out.println("4. Se déconnecter");
            System.out.print("Choix: ");
            String choix = scanner.nextLine();

            switch (choix) {
                case "1":
                    gererLigues();
                    break;
                case "2":
                    gererEmployes();
                    break;
                case "3":
                    definirAdministrateur();
                    break;

                case "4":
                    System.out.println("Déconnexion...");
                    System.exit(0);
            }
        }
    }
    private void definirAdministrateur()
    {
        System.out.println("\n=== DEFINIR ADMINISTRATEUR ===");

        System.out.println("Ligues :");

        for(Ligue l : gestion.getLigues())
        {
            System.out.println(
                l.getId() + " - " + l.getNom()
            );
        }

        System.out.print("choix ID Ligue : ");
        int ligueId = Integer.parseInt(scanner.nextLine());

        Ligue ligue = gestion.getLigueById(ligueId);

        if(ligue == null)
        {
            System.out.println("Ligue introuvable");
            return;
        }

        System.out.println("\nEmployés :");

        for(Employe e : ligue.getEmployes())
        {
            System.out.println(
                e.getId() + " - " +
                e.getNom() + " " +
                e.getPrenom()
            );
        }

        System.out.print("choix ID Employé : ");

        int employeId =
                Integer.parseInt(scanner.nextLine());

        Employe employe =
                gestion.getEmployeById(employeId);

        if(employe == null)
        {
            System.out.println("Employé introuvable");
            return;
        }

        ligue.setAdministrateur(employe);

        System.out.println("Administrateur enregistré.");
    }


    private void gererLigues() {
        System.out.println("\n=== LIGUES ===");

        for (Ligue l : gestion.getLigues()) {
            System.out.println(l.getId() + " - " + l.getNom());
        }

        System.out.println("1. Ajouter");
        System.out.println("2. Supprimer");
        System.out.println("3. Modifier");
        System.out.println("4. Retour");
        System.out.print("Choix : ");
        String choix = scanner.nextLine();

        switch (choix)
        {
            case "1":
                System.out.print("Nom: ");
                String nom = scanner.nextLine();
                gestion.addLigue(nom);
                break;

            case "2":
                System.out.print("ID: ");
                int id = Integer.parseInt(scanner.nextLine());

                Ligue l = gestion.getLigueById(id);

                if(l != null)
                    gestion.remove(l);

                break;

            case "3":
                modifierLigue();
                break;
        }
    }
    private void modifierLigue()
    {
        System.out.println("\n=== MODIFIER LIGUE ===");

        System.out.print("ID Ligue : ");

        int id =
                Integer.parseInt(scanner.nextLine());

        Ligue ligue =
                gestion.getLigueById(id);

        if(ligue == null)
        {
            System.out.println("Ligue introuvable");
            return;
        }

        System.out.print("Nouveau nom : ");

        ligue.setNom(
                scanner.nextLine());

        System.out.println(
                "Ligue modifiée.");
    }

    private void gererEmployes() {
    
        System.out.println("\n=== EMPLOYES ===");

        for (Ligue l : gestion.getLigues()) {
            for (Employe e : l.getEmployes()) {
                System.out.println(e.getId() + " - " + e);
            }
        }

        System.out.println("1. Ajouter");
        System.out.println("2. Supprimer");
        System.out.println("3. Modifier");
        System.out.println("4. Départ employé");
        System.out.println("5. Retour");
        System.out.print("choix: ");
        String choix = scanner.nextLine();

        switch (choix) {
            case "1":
                System.out.print("Nom: ");
                String nom = scanner.nextLine();

                System.out.print("Prénom: ");
                String prenom = scanner.nextLine();

                System.out.print("Mail: ");
                String mail = scanner.nextLine();

                System.out.print("Password: ");
                String password = scanner.nextLine();

                System.out.print("ID ligue: ");
                int ligueId = Integer.parseInt(scanner.nextLine());

                Ligue ligue = gestion.getLigueById(ligueId);

                gestion.addEmploye(nom, prenom, mail, password, ligue);
                break;

            case "2":
                System.out.print("ID employé: ");
                int empId = Integer.parseInt(scanner.nextLine());

                Employe e = gestion.getEmployeById(empId);

                if (e != null && !e.estRoot()) {
                    e.remove();
                }
                break;
                
            case "3":
                modifierEmploye();
                break;
                
            case "4":
                saisirDateDepart();
                break;
        }
        
    }
    private void saisirDateDepart()
    {
        System.out.println(
            "\n=== DATE DE DEPART ==="
        );

        System.out.print(
            "ID Employé : "
        );

        int id =
            Integer.parseInt(
                scanner.nextLine()
            );

        Employe e =
            gestion.getEmployeById(id);

        if(e == null)
        {
            System.out.println(
                "Employé introuvable"
            );
            return;
        }

        System.out.print(
            "Date départ (AAAA-MM-JJ) : "
        );

        String date =
            scanner.nextLine();

        e.setDateDepart(
            java.sql.Date.valueOf(date)
        );

        System.out.println(
            "Date enregistrée."
        );
    }
    private void modifierEmploye()
    {
        System.out.println("\n=== MODIFIER EMPLOYE ===");

        System.out.print("ID Employé : ");

        int id =
                Integer.parseInt(scanner.nextLine());

        Employe e =
                gestion.getEmployeById(id);

        if(e == null)
        {
            System.out.println("Employé introuvable");
            return;
        }

        System.out.print("Nouveau nom : ");
        e.setNom(scanner.nextLine());

        System.out.print("Nouveau prénom : ");
        e.setPrenom(scanner.nextLine());

        System.out.print("Nouveau mail : ");
        e.setMail(scanner.nextLine());

        System.out.print("Nouveau mot de passe : ");
        e.setPassword(scanner.nextLine());

        System.out.println("Employé modifié.");
    }
    
    public static void main(String[] args)
    {
        jdbc.JDBC jdbc = new jdbc.JDBC();

        GestionPersonnel gestion =
                jdbc.getGestionPersonnel();

        PersonnelConsole console =
                new PersonnelConsole(gestion);

        console.start();
    }
}