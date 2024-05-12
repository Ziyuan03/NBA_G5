package nba;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NBA_SQL_2 {
    Connection con;    
    
    //Testing
    public void createConnection() {
        try {            
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nba", "root", "root123NBA.");
            
            System.out.println("Connected");
            
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(NBA_SQL_2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    
    // All players 
    public void createAllPlayersTable() {
        createConnection();
        
        try {      
            Statement stmt = con.createStatement();
            
            stmt.execute("CREATE TABLE ALLPLAYERS ("
                    + "ID INT, "
                    + "First_Name VARCHAR(50), "
                    + "Last_Name VARCHAR(50), "
                    + "Age INT, "
                    + "Position VARCHAR(50), "
                    + "Height VARCHAR(50), "
                    + "Weight VARCHAR(50), "
                    + "Jersey_Number VARCHAR(50), "
                    + "College VARCHAR(50), "
                    + "Country VARCHAR(50), "
                    + "Draft_Year INT, "
                    + "Draft_Round INT, "
                    + "Draft_Number INT, "
                    + "Team_ID INT, "
                    + "Team_Conference VARCHAR(50), "
                    + "Team_Division VARCHAR(50), "
                    + "Team_City VARCHAR(50), "
                    + "Team_Name VARCHAR(50), "
                    + "Team_Full_Name VARCHAR(50), "
                    + "Team_Abbreviation VARCHAR(50));");                        
                            
            stmt.close();
            System.out.println("All Players Table created");
            
        } catch (SQLException ex) {
            Logger.getLogger(NBA_SQL_2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Average stats of all players
    public void createAverageStatsTable() {
        createConnection();
        
        try {      
            Statement stmt = con.createStatement();
            
            stmt.execute("CREATE TABLE AVERAGESTATS ("
                    + "Player_ID INT, "
                    + "Season INT, "
                    + "Salary INT, "
                    + "PTS DECIMAL(5, 3), "
                    + "AST DECIMAL(5, 3), "
                    + "STL DECIMAL(5, 3), "
                    + "BLK DECIMAL(5, 3), "
                    + "REB DECIMAL(5, 3), "
                    + "Games_Played INT, "
                    + "TurnOver DECIMAL(5, 3), "
                    + "PF DECIMAL(5, 3), "
                    + "FGA DECIMAL(5, 3), "
                    + "FGM DECIMAL(5, 3), "
                    + "FTA DECIMAL(5, 3), "
                    + "FTM DECIMAL(5, 3), "
                    + "FG3A DECIMAL(5, 3), "
                    + "FG3M INT, "
                    + "OREB DECIMAL(5, 3), "
                    + "DREB INT, "
                    + "FG_PCT DECIMAL(5, 3), "
                    + "FG3_PCT DECIMAL(5, 2), "
                    + "FT_PCT DECIMAL(5, 3), "
                    + "MIN VARCHAR(50)); ");                                    
                            
            stmt.close();
            System.out.println("Average Stats Table created");
            
        } catch (SQLException ex) {
            Logger.getLogger(NBA_SQL_2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Combination table of ALLPLAYERS and AVERAGESTATS
    public void createView() {
        createConnection();
        
        try {
            Statement stmt = con.createStatement();
            stmt.execute("CREATE VIEW CANDIDATE_LIST AS "
                    + "SELECT ID AS Player_ID, CONCAT(First_Name, ' ', Last_Name) AS 'Player_Name',Age, Height, Weight, Position, Salary, PTS AS Points, REB AS Rebounds, AST AS Assists, STL AS Steals, BLK AS Blocks "
                    + "FROM ALLPLAYERS INNER JOIN AVERAGESTATS "
                    + "ON ALLPLAYERS.ID = AVERAGESTATS.PLAYER_ID;");
            
            stmt.close();
            System.out.println("Candidate List View Added");
            
        } catch (SQLException ex) {
            Logger.getLogger(NBA_SQL_2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
    //San_Antonio
    public void createSanAntonioTable() {
        createConnection();
        
        try {
            Statement stmt = con.createStatement();
            
            stmt.execute("CREATE TABLE San_Antonio ("
                    + "Player_ID INT, "
                    + "Player_Name VARCHAR(50), "
                    + "Age INT, "
                    + "Height VARCHAR(50), "
                    + "Weight INT, "
                    + "Position VARCHAR(50), "
                    + "Salary INT);");
            
            stmt.close();
            System.out.println("San Antonio Table Added");
            
        } catch (SQLException ex) {
            Logger.getLogger(NBA_SQL_2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Trigger to calculate draft_year, age and salary
    public void createTriggger() {
        createConnection();
        
        try {
            Statement stmt = con.createStatement();
            stmt.execute("CREATE TRIGGER Update_DraftYear "
                    + "BEFORE INSERT ON ALLPLAYERS "
                    + "FOR EACH ROW "
                    + "BEGIN IF NEW.Draft_Year IS NULL THEN "
                    + "SET NEW.Draft_Year = (SELECT AVG(Draft_Year) FROM ALLPLAYERS); "
                    + "END IF; END;");
            
            stmt.execute("CREATE TRIGGER Update_Age "
                    + "BEFORE INSERT ON ALLPLAYERS "
                    + "FOR EACH ROW "
                    + "SET NEW.Age = 2024 - NEW.DRAFT_YEAR + 20;");
            
            stmt.execute("CREATE TRIGGER Update_Salary "
                    + "BEFORE INSERT ON AVERAGESTATS "
                    + "FOR EACH ROW "
                    + "BEGIN IF NEW.PTS > 20 THEN SET NEW.SALARY = 3000; "
                    + "ELSE SET NEW.SALARY = 1000; "
                    + "END IF; END;");
            
            stmt.close();
            System.out.println("Triggers Added");
            
        } catch (SQLException ex) {
            Logger.getLogger(NBA_SQL_2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }              
}
