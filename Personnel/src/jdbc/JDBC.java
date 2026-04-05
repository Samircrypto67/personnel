package jdbc;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import personnel.*;

public class JDBC implements Passerelle 
{
    Connection connection;

    public JDBC()
    {
        try
        {
            Class.forName(Credentials.getDriverClassName());
            connection = DriverManager.getConnection(Credentials.getUrl(), Credentials.getUser(), Credentials.getPassword());
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Pilote JDBC non installé.");
        }
        catch (SQLException e)
        {
            System.out.println(e);
        }
    }

    @Override
    public GestionPersonnel getGestionPersonnel() 
    {
        GestionPersonnel gestionPersonnel = new GestionPersonnel(this);

        try 
        {
        
            //  Charger toutes les ligues 
            String requeteLigue = "SELECT * FROM ligue";
            Statement stmtLigue = connection.createStatement();
            ResultSet rsLigue = stmtLigue.executeQuery(requeteLigue);
            while (rsLigue.next())
                gestionPersonnel.addLigue(rsLigue.getInt("id"), rsLigue.getString("nom"));

            //  Charger le root
            String requeteRoot = "SELECT * FROM employe WHERE id_ligue IS NULL LIMIT 1";
            Statement stmtRoot = connection.createStatement();
            ResultSet rootRS = stmtRoot.executeQuery(requeteRoot);

            if (rootRS.next())
            {
                gestionPersonnel.addRoot(
                    rootRS.getInt("id"),
                    rootRS.getString("nom"),
                    rootRS.getString("prenom"),
                    rootRS.getString("mail"),
                    rootRS.getString("password")
                );
            };

            //  ETAPE 7 : charger les employés avec jointure
            String requeteJointure = "SELECT l.id AS ligue_id, l.nom AS ligue_nom, " +"e.id AS emp_id, e.nom AS emp_nom, e.prenom, e.mail, e.password " +"FROM ligue l LEFT JOIN employe e ON l.id = e.id_ligue";
            Statement stmtJointure = connection.createStatement();
            ResultSet rsJointure = stmtJointure.executeQuery(requeteJointure);
            Ligue ligueCourante = null;
            int currentLigueId = -1;

            while (rsJointure.next())
                {
                    int ligueId = rsJointure.getInt("ligue_id");

                    // récupérer la ligue
                    if (ligueId != currentLigueId)
                        {
                            ligueCourante = gestionPersonnel.addLigue(
                                ligueId,
                                rsJointure.getString("ligue_nom")
                            );
                            currentLigueId = ligueId;
                        }

                        // créer employé
                        if (rsJointure.getObject("emp_id") != null)
                        {
                            Employe employe = new Employe(
                                gestionPersonnel,
                                ligueCourante,
                                rsJointure.getInt("employé_id"),
                                rsJointure.getString("employé_nom"),
                                rsJointure.getString("prenom"),
                                rsJointure.getString("mail"),
                                rsJointure.getString("password")
                            );

                            ligueCourante.addEmploye(employe);
                        }
                    }
             }
            
        }
        catch (SQLException e)
        {
            System.out.println(e);
        }

        return gestionPersonnel;
    }
    

    @Override
    public void sauvegarderGestionPersonnel(GestionPersonnel gestionPersonnel) throws SauvegardeImpossible 
    {
        close();
    }

    public void close() throws SauvegardeImpossible
    {
        try
        {
            if (connection != null)
                connection.close();
        }
        catch (SQLException e)
        {
            throw new SauvegardeImpossible(e);
        }
    }

    @Override
    public int insert(Ligue ligue) throws SauvegardeImpossible 
    {
        try 
        {
            PreparedStatement instruction;
            instruction = connection.prepareStatement("insert into ligue (nom) values(?)", Statement.RETURN_GENERATED_KEYS);
            instruction.setString(1, ligue.getNom());        
            instruction.executeUpdate();
            ResultSet id = instruction.getGeneratedKeys();
            id.next();
            return id.getInt(1);
        } 
        catch (SQLException exception) 
        {
            exception.printStackTrace();
            throw new SauvegardeImpossible(exception);
        }        
    }

    //  Méthode insert(Employe) 
    @Override
    public int insert(Employe employe) throws SauvegardeImpossible
    {
        try
        {
            PreparedStatement instruction = connection.prepareStatement(
                "INSERT INTO employe (nom, prenom, mail, password, id_ligue) VALUES (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            instruction.setString(1, employe.getNom());
            instruction.setString(2, employe.getPrenom());
            instruction.setString(3, employe.getMail());
            instruction.setString(4, employe.getPassword());
            if (employe.getLigue() != null)
                instruction.setInt(5, employe.getLigue().getId());
            else
                instruction.setNull(5, java.sql.Types.INTEGER); // root n’a pas de ligue

            instruction.executeUpdate();
            ResultSet id = instruction.getGeneratedKeys();
            id.next();
            return id.getInt(1);
        }
        catch (SQLException exception)
        {
            exception.printStackTrace();
            throw new SauvegardeImpossible(exception);
        }
    }

//supprimer des employés
@Override
public void delete(Employe employe) throws SauvegardeImpossible
{
    try
    {
        PreparedStatement instruction = connection.prepareStatement(
            "DELETE FROM employe WHERE id=?"
        );
        instruction.setInt(1, employe.getId());
        instruction.executeUpdate();
    }
    catch(SQLException e)
    {
        e.printStackTrace();
        throw new SauvegardeImpossible(e);
    }
}

//  Implementation de la methode update .
//Implementation de la methode update Ligue
@Override
public void update(Ligue ligue) throws SauvegardeImpossible 
{
    try 
    {
        PreparedStatement instruction;
        instruction = connection.prepareStatement(
            "UPDATE ligue SET nom = ? WHERE id = ?"
        );
        instruction.setString(1, ligue.getNom());
        instruction.setInt(2, ligue.getId());
        instruction.executeUpdate();
    } 
    catch (SQLException exception) 
    {
        exception.printStackTrace();
        throw new SauvegardeImpossible(exception);
    }       
}


// Implementation de la methode update Employes
@Override
public void update(Employe employe) throws SauvegardeImpossible
{
    try
    {
        PreparedStatement instruction = connection.prepareStatement(
            "UPDATE employe SET nom=?, prenom=?, mail=?, password=?, date_arrivee=?, date_depart=?, id_ligue=? WHERE id=?"
        );

        instruction.setString(1, employe.getNom());
        instruction.setString(2, employe.getPrenom());
        instruction.setString(3, employe.getMail());
        instruction.setString(4, employe.getPassword());
        instruction.setDate(5, employe.getDateArrivee());
        instruction.setDate(6, employe.getDateDepart());

        if (employe.getLigue() != null)
            instruction.setInt(7, employe.getLigue().getId());
        else
            instruction.setNull(7, java.sql.Types.INTEGER);

        instruction.setInt(8, employe.getId());

        instruction.executeUpdate();
    }
    catch(SQLException e)
    {
        e.printStackTrace();
        throw new SauvegardeImpossible(e);
    }
}

