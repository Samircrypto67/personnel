package jdbc;

import java.sql.*;
import personnel.*;

public class JDBC implements Passerelle 
{
    Connection connection;

    public JDBC()
    {
        try
        {
            Class.forName(Credentials.getDriverClassName());
            connection = DriverManager.getConnection(
                Credentials.getUrl(),
                Credentials.getUser(),
                Credentials.getPassword()
            );
        }
        catch (Exception e)
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
            // =========================
            // 1️⃣ Charger les ligues
            // =========================
            String sqlLigue = "SELECT * FROM ligue";
            Statement stmtLigue = connection.createStatement();
            ResultSet rsLigue = stmtLigue.executeQuery(sqlLigue);

            while (rsLigue.next())
            {
                gestionPersonnel.addLigue(
                    rsLigue.getInt("id"),
                    rsLigue.getString("nom")
                );
            }

            // =========================
            // 2️⃣ Charger le ROOT
            // =========================
            String sqlRoot = "SELECT * FROM employe WHERE mail='root' LIMIT 1";
            Statement stmtRoot = connection.createStatement();
            ResultSet rsRoot = stmtRoot.executeQuery(sqlRoot);

            if (rsRoot.next())
            {
                gestionPersonnel.addRoot(
                    rsRoot.getInt("id"),
                    rsRoot.getString("nom"),
                    rsRoot.getString("prenom"),
                    rsRoot.getString("mail"),
                    rsRoot.getString("password")
                );
            }
            else
            {
            	try
            	{
            	    Employe root = new Employe(
            	        gestionPersonnel,
            	        null,
            	        "root",
            	        "",
            	        "root",
            	        "toor"
            	    );

            	    int id = insert(root);

            	    gestionPersonnel.addRoot(
            	        id,
            	        "root",
            	        "",
            	        "root",
            	        "toor"
            	    );
            	}
            	catch (SauvegardeImpossible e)
            	{
            	    e.printStackTrace();
            	}
            }
            // =========================
            // 3️⃣ Charger employés (ETAPE 7)
            // =========================
            String sql = "SELECT * FROM employe";
            Statement stmtEmp = connection.createStatement();
            ResultSet rsEmp = stmtEmp.executeQuery(sql);

            while (rsEmp.next())
            {
                int ligueId = rsEmp.getInt("ligue_id");

                Ligue ligue = gestionPersonnel.getLigueById(ligueId);

                Employe e = new Employe(
                    gestionPersonnel,
                    ligue,
                    rsEmp.getInt("id"),
                    rsEmp.getString("nom"),
                    rsEmp.getString("prenom"),
                    rsEmp.getString("mail"),
                    rsEmp.getString("password")
                );
                e.setDateArrivee(
                        rsEmp.getDate("date_arrivee"));

                e.setDateDepart(
                        rsEmp.getDate("date_depart"));

                if (ligue != null)
                    ligue.addEmploye(e);
            }

            // =========================
            // 4️⃣ Charger administrateurs (ETAPE 11)
            // =========================
            String sqlAdmin = "SELECT * FROM ligue WHERE administrateur_id IS NOT NULL";
            Statement stmtAdmin = connection.createStatement();
            ResultSet rsAdmin = stmtAdmin.executeQuery(sqlAdmin);

            while (rsAdmin.next())
            {
                Ligue ligue = gestionPersonnel.getLigueById(rsAdmin.getInt("id"));
                Employe admin = gestionPersonnel.getEmployeById(rsAdmin.getInt("administrateur_id"));

                if (ligue != null && admin != null)
                    ligue.setAdministrateur(admin);
            }

     }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return gestionPersonnel;
    }
    @Override
    public void sauvegarderGestionPersonnel(GestionPersonnel gestionPersonnel)
            throws SauvegardeImpossible {
        // plus utilisé avec JDBC
    }
    // =========================
    // INSERT LIGUE
    // =========================
    @Override
    public int insert(Ligue ligue) throws SauvegardeImpossible 
    {
        try 
        {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO ligue (nom) VALUES(?)",
                Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, ligue.getNom());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        } 
        catch (SQLException e) 
        {
            throw new SauvegardeImpossible(e);
        }
    }

    // =========================
    // INSERT EMPLOYE
    // =========================
    @Override
    public int insert(Employe e) throws SauvegardeImpossible
    {
        try
        {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO employe (nom, prenom, mail, password, ligue_id) VALUES (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, e.getNom());
            ps.setString(2, e.getPrenom());
            ps.setString(3, e.getMail());
            ps.setString(4, e.getPassword());

            if (e.getLigue() != null)
                ps.setInt(5, e.getLigue().getId());
            else
                ps.setNull(5, Types.INTEGER);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        }
        catch(SQLException ex)
        {
            throw new SauvegardeImpossible(ex);
        }
    }

    // =========================
    // UPDATE LIGUE
    // =========================
    @Override
    public void update(Ligue ligue) throws SauvegardeImpossible
    {
        try
        {
            PreparedStatement ps = connection.prepareStatement(
                "UPDATE ligue SET nom=? WHERE id=?"
            );

            ps.setString(1, ligue.getNom());
            ps.setInt(2, ligue.getId());
            ps.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new SauvegardeImpossible(e);
        }
    }

    // =========================
    // UPDATE EMPLOYE
    // =========================
    @Override
    public void update(Employe e) throws SauvegardeImpossible
    {
        try
        {
        	PreparedStatement ps = connection.prepareStatement(
        		    "UPDATE employe SET "
        		    + "nom=?, "
        		    + "prenom=?, "
        		    + "mail=?, "
        		    + "password=?, "
        		    + "ligue_id=?, "
        		    + "date_arrivee=?, "
        		    + "date_depart=? "
        		    + "WHERE id=?"
        		);

        		ps.setString(1, e.getNom());
        		ps.setString(2, e.getPrenom());
        		ps.setString(3, e.getMail());
        		ps.setString(4, e.getPassword());

        		if(e.getLigue() != null)
        		    ps.setInt(5, e.getLigue().getId());
        		else
        		    ps.setNull(5, Types.INTEGER);

        		ps.setDate(6, e.getDateArrivee());
        		ps.setDate(7, e.getDateDepart());

        		ps.setInt(8, e.getId());

        		ps.executeUpdate();
            ps.executeUpdate();
        }
        catch(SQLException ex)
        {
            throw new SauvegardeImpossible(ex);
        }
    }

    // =========================
    // DELETE EMPLOYE
    // =========================
 
    @Override
    public void delete(Employe employe)
            throws SauvegardeImpossible
    {
        try {
            PreparedStatement ps =
                    connection.prepareStatement(
                            "DELETE FROM employe WHERE id=?");

            ps.setInt(1, employe.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new SauvegardeImpossible(e);
        }
    }

    // =========================
    // DELETE LIGUE
    // =========================
    @Override
    public void delete(Ligue ligue)
            throws SauvegardeImpossible
    {
        try {

            PreparedStatement ps1 =
                    connection.prepareStatement(
                            "DELETE FROM employe WHERE ligue_id=?");

            ps1.setInt(1, ligue.getId());
            ps1.executeUpdate();

            PreparedStatement ps2 =
                    connection.prepareStatement(
                            "DELETE FROM ligue WHERE id=?");

            ps2.setInt(1, ligue.getId());
            ps2.executeUpdate();

        } catch (SQLException e) {
            throw new SauvegardeImpossible(e);
        }
    }

    // =========================
    // UPDATE ADMIN (ETAPE 12)
    // =========================
    @Override
    public void updateAdministrateur(
            Ligue ligue)
            throws SauvegardeImpossible
    {
        try
        {
            PreparedStatement ps =
                connection.prepareStatement(
                    "UPDATE ligue SET administrateur_id=? WHERE id=?"
                );

            if(ligue.getAdministrateur() != null)
            {
                ps.setInt(
                    1,
                    ligue.getAdministrateur().getId()
                );
            }
            else
            {
                ps.setNull(
                    1,
                    Types.INTEGER
                );
            }

            ps.setInt(
                2,
                ligue.getId()
            );

            ps.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new SauvegardeImpossible(e);
        }
    }
 }