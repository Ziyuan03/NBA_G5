/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package nba;

import java.awt.Component;
import java.awt.Image;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author user
 */
public class jPlayerProfile extends javax.swing.JFrame {

    private BtnInjuryOrRoster btnInjuryRoster = new BtnInjuryOrRoster();
    private PlayerTypeButton buttonType;
    private LastPageStored lastPage;
    Connection con;
    ResultSet rs;
    String currentID;
    String currentName;
    int selectedRow;
    int salaryMax = 20000;
    int playerMax = 15;
    int playerMin = 10;
    String[] positionArray = {"G", "F", "C"};
    int[] positionNumber = new int[3];

    public jPlayerProfile() {
        initComponents();
        createConnection();
        setImage();
    }

    public jPlayerProfile(String playerName, LastPageStored lastPage, PlayerTypeButton buttonType) {
        try {
            initComponents();
            createConnection();
            setImage();
            this.lastPage = lastPage;
            this.buttonType = buttonType;
            System.out.println(playerName);
            setImageMethod("src\\icons\\Player_Profile\\" + playerName + ".png", getPlayerProfile());

            String team = "";
            String page = lastPage.getLastPage();

            String btnType = buttonType.getButtonType();

            if (btnType == null) {
                btnType = "All_Players";
            }

            // Display correct button and player profile
            switch (page) {
                case "View_Roster" -> {
                    team += "SAN_ANTONIO WHERE Salary ";
                    btnAddRemove.setVisible(false);
                }
                case "Add_Player" -> {
                    team += "CANDIDATE_LIST WHERE Salary ";
                    btnAddRemove.setText("Add");
                }
                case "Remove_Player" -> {
                    team += "SAN_ANTONIO WHERE Salary ";
                    btnAddRemove.setText("Remove");
                }
                default ->
                    team = "Impossible";
            }

            // Display player profile based on button pressed before (Player Type: Superstar / Non-Superstar / All Players)
            switch (btnType) {
                case "All_Players" ->
                    team += ">= 0";
                case "Superstar_Players" ->
                    team += "= 3000";
                case "Non_Superstar_Players" ->
                    team += "= 1000";
            }

            // Additional condition for display CANDIDATE_LIST players (No display player that exist in SAN_ANTONIO)
            if (page.equals("Add_Player")) {
                team += " AND Player_ID NOT IN (SELECT Player_ID FROM SAN_ANTONIO)";
            }
            
            //Additional condition for display SAN_ANTONIO players by descending (easy to check when remove player, correspond to table)
            if(page.equals("Remove_Player")) {
                team += " ORDER BY Player_ID ASC";
            }

            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery("SELECT * FROM " + team);

            boolean playerFound = false;

            while (rs.next()) {
                if (rs.getString("Player_Name").equals(playerName)) {
                    updatePlayerProfile();
                    currentName = rs.getString("Player_Name");

                    playerFound = true;
                    break;
                }
            }

            if (!playerFound) {
                JOptionPane.showMessageDialog(jPlayerProfile.this, "The player you selected does not exist!", "No Player Found!", JOptionPane.ERROR_MESSAGE);
                btnNext.setVisible(false);
                btnPrevious.setVisible(false);
            } else {
                btnNext.setVisible(!rs.isLast());
                btnPrevious.setVisible(!rs.isFirst());
            }

        } catch (SQLException ex) {
            Logger.getLogger(jPlayerProfile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setImage() {
        setImageMethod("src\\icons\\nbaLogo.png", nbaLogo);
        setImageMethod("src\\icons\\iconPlayerProfile\\iconCandidateList.png", iconCandidateList);
        setImageMethod("src\\icons\\backgroundBasketballCourt.jpg", backgroundBasketballCourt);
        setImageMethod("src\\icons\\iconPlayerProfile\\iconID.png", iconPlayerID);
        setImageMethod("src\\icons\\iconPlayerProfile\\iconAge.png", iconAge);
        setImageMethod("src\\icons\\iconPlayerProfile\\iconHeight.png", iconHeight);
        setImageMethod("src\\icons\\iconPlayerProfile\\iconWeight.png", iconWeight);
        setImageMethod("src\\icons\\iconPlayerProfile\\iconPosition.png", iconPosition);
        setImageMethod("src\\icons\\iconPlayerProfile\\iconSalary.png", iconSalary);
        setImageMethod("src\\icons\\iconPlayerProfile\\iconPoints.png", iconPoints);
        setImageMethod("src\\icons\\iconPlayerProfile\\iconSteals.png", iconSteals);
        setImageMethod("src\\icons\\iconPlayerProfile\\iconBlocks.png", iconBlocks);
        setImageMethod("src\\icons\\iconPlayerProfile\\iconAssists.png", iconAssists);
        setImageMethod("src\\icons\\iconPlayerProfile\\iconRebounds.png", iconRebounds);
        setImageMethod("src\\icons\\iconPlayerProfile\\iconInjury.png", iconInjury);
    }

    public void setImageMethod(String imagePath, JLabel labelName) {
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage();
            Image imgScale = img.getScaledInstance(labelName.getWidth(), labelName.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(imgScale);
            labelName.setIcon(scaledIcon);
        } catch (Exception e) {
            System.out.println("Error loading image: " + imagePath);
            e.printStackTrace();
        }
    }

    //Create connection with database when initiate this program everytime 
    private void createConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nba", "root", "root123NBA.");

            System.out.println("Database is Connected. Program will be started soon...");

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(NBA_SAN_ANTONIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Calculate current player(s) in roster / total salary (Multiuse Method)
    public int getCurrent(String function, String functionName) {
        int current = 0;

        try {
            if (con == null) {
                current = 0;
            } else {
                Statement stmt = con.createStatement();

                ResultSet rs = stmt.executeQuery("SELECT " + function + " AS '" + functionName + "' FROM SAN_ANTONIO");

                if (rs.next()) {
                    current = rs.getInt(functionName);
                } else {
                    current = 0;
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(NBA_SAN_ANTONIO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return current;
    }

    //Set JOptionPane positon and get the selection
    public int showConfirmDialog(Component parentComponent, Object message, String title, int optionType) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.QUESTION_MESSAGE, optionType);
        JDialog dialog = optionPane.createDialog(parentComponent, title);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);

        Object selectedValue = optionPane.getValue();

        if (selectedValue instanceof Integer) {
            return (Integer) selectedValue;
        } else {
            return JOptionPane.CLOSED_OPTION;
        }
    }

    //Set JOptionPane position and swingman selection
    public int showConfirmTwoOptions(Component parentComponent, Object message, String title, int optionType, Object[] options) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.QUESTION_MESSAGE, optionType, null, options, options[0]);
        JDialog dialog = optionPane.createDialog(parentComponent, title);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);

        Object selectedOption = optionPane.getValue();
        if (selectedOption == null) {
            return JOptionPane.CLOSED_OPTION;
        }

        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(selectedOption)) {
                return i;
            }
        }

        return JOptionPane.CLOSED_OPTION;
    }

    //Method check whether player duplicate (Same with method playerExist() in jRemovePlayer, but playerExist() not in here, so use this with add player)
    public boolean checkPlayer(String condition) {
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM " + condition);
            ResultSet rs = pstmt.executeQuery();

            return rs.next();

        } catch (SQLException ex) {
            Logger.getLogger(NBA_SAN_ANTONIO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    //Check if position meet requirement before adding player when reach 6 players
    public void checkPositionBeforeAdd(int id, String name, int age, String height, int weight, String position, int salary, double points, double steals, double blocks, double assists, double rebounds) {
        if (getCurrent("COUNT(Player_ID)", "TotalPlayer") >= 6) {
            if (booleanPlayerPosition()) {
                addPlayer(id, name, age, height, weight, position, salary, points, steals, blocks, assists, rebounds);
            } else {
                for (int i = 0; i < positionArray.length; i++) {
                    if (positionArray[i].equalsIgnoreCase(position)) {
                        if (2 - positionNumber[i] > 0) {
                            addPlayer(id, name, age, height, weight, position, salary, points, steals, blocks, assists, rebounds);
                        } else {
                            JOptionPane.showMessageDialog(jPlayerProfile.this,
                                    "You should not add this player as this position: '" + position + "' already meets the requirement. \nYou can add this player again after you meet the other position requirements. \nYou will be redirected to add player page.", "Player Position Error!", JOptionPane.ERROR_MESSAGE);
                            dispose();
                            new jAddPlayer().setVisible(true);
                        }
                    } else {
                        //Impossible to have error at this part. If error, check the array.
                    }
                }
            }
        } else {
            addPlayer(id, name, age, height, weight, position, salary, points, steals, blocks, assists, rebounds);
        }
    }

    //Boolean for position number
    public boolean booleanPlayerPosition() {
        getPlayerPosition();

        int meet = 0;

        for (int i = 0; i < positionArray.length; i++) {
            if (positionNumber[i] - 2 >= 0) {
                meet++;
            }
        }

        return meet == 3;
    }

    //Get each position number
    public void getPlayerPosition() {
        int playerPosition = 0;

        if (con == null) {
            System.out.println("Connection loss.");
        } else {
            try {
                for (int i = 0; i < positionArray.length; i++) {
                    PreparedStatement pstmt = con.prepareStatement("SELECT COUNT(Position) AS PositionCount FROM SAN_ANTONIO WHERE Position = ?");
                    pstmt.setString(1, positionArray[i]);
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        playerPosition = rs.getInt("PositionCount");
                        positionNumber[i] = playerPosition;
                    } else {
                        positionNumber[i] = 0;
                    }
                }

            } catch (SQLException ex) {
                Logger.getLogger(NBA_SAN_ANTONIO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //Add player into team
    public void addPlayer(int id, String name, int age, String height, int weight, String position, int salary, double points, double steals, double blocks, double assists, double rebounds) {
        try {
            JOptionPane.showMessageDialog(jPlayerProfile.this, "Adding the selected player " + name + " into your roster...", "Adding Player...", JOptionPane.PLAIN_MESSAGE);

            PreparedStatement stmt = con.prepareStatement("INSERT INTO SAN_ANTONIO VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            stmt.setInt(1, id);
            stmt.setString(2, name);
            stmt.setInt(3, age);
            stmt.setString(4, height);
            stmt.setInt(5, weight);
            stmt.setString(6, position.toUpperCase());
            stmt.setInt(7, salary);
            stmt.setDouble(8, points);
            stmt.setDouble(9, steals);
            stmt.setDouble(10, blocks);
            stmt.setDouble(11, assists);
            stmt.setDouble(12, rebounds);
            stmt.setString(13, "Healthy");

            stmt.executeUpdate();

            String player = getCurrent("COUNT(Player_ID)", "TotalPlayer") > 1 ? " players " : " player ";
            JOptionPane.showMessageDialog(jPlayerProfile.this,
                    "Congratulations on successfully recruiting a powerful player into your roster 'San_Antonio' \nYour roster have " + getCurrent("COUNT(Player_ID)", "TotalPlayer") + player + "now."
                    + displayEachPosition()
                    + "\n\nYour roster current total salary is " + getCurrent("SUM(Salary)", "TotalSalary"),
                    "Added Player Successfully",
                    JOptionPane.PLAIN_MESSAGE);

            // Refresh the form to show next player
            displayNextPlayer("CANDIDATE_LIST", id);

        } catch (SQLException ex) {
            Logger.getLogger(NBA_SQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //DIsplay next player
    public void displayNextPlayer(String tableName, int id) {
        try {
            String query = "", query2 = "";

            switch (tableName) {
                case "SAN_ANTONIO" -> {
                    query = "SELECT * FROM SAN_ANTONIO WHERE Player_ID > " + id + " ORDER BY Player_ID LIMIT 1";
                    query2 = "SELECT * FROM SAN_ANTONIO WHERE Player_ID < " + id + " ORDER BY Player_ID DESC LIMIT 1";
                }
                case "CANDIDATE_LIST" -> {
                    query = "SELECT * FROM CANDIDATE_LIST WHERE Player_ID > " + id + " AND Player_ID NOT IN (SELECT Player_ID FROM SAN_ANTONIO) LIMIT 1";
                    query2 = "SELECT * FROM CANDIDATE_LIST WHERE Player_ID < " + id + " AND Player_ID NOT IN (SELECT Player_ID FROM SAN_ANTONIO) ORDER BY Player_ID DESC LIMIT 1";
                }
            }
            
            Statement stmt = con.createStatement();
            ResultSet rs1 = stmt.executeQuery(query);

            String playerNameNext = "";
            
            if (rs1.next()) {
                playerNameNext = rs1.getString("Player_Name");

                //If last player
            } else {
                ResultSet rs2 = stmt.executeQuery(query2);

                if (rs2.next()) {
                    playerNameNext = rs2.getString("Player_Name");
                    System.out.println(playerNameNext);
                    
                    //If no player
                } else {
                    JOptionPane.showMessageDialog(this, "No more candidates available.");
                    btnAddRemove.setVisible(false);  // Disable add button if no more candidates
                }
            }

            dispose();
            new jPlayerProfile(playerNameNext, lastPage, buttonType).setVisible(true);

        } catch (SQLException ex) {
            Logger.getLogger(jPlayerProfile.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Error fetching next player: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Display if each position meet requirement
    public String displayEachPosition() {
        StringBuilder sb = new StringBuilder();

        if (booleanPlayerPosition()) {
            sb.append("\n\nAll positions meet the requirements.");

            for (int i = 0; i < positionArray.length; i++) {
                String player = positionNumber[i] > 1 ? " players" : " player";
                sb.append("\n\nPosition ").append(positionArray[i]).append(": ").append(positionNumber[i]).append(player);
            }
        } else {
            for (int i = 0; i < positionArray.length; i++) {
                String player = positionNumber[i] > 1 ? " players" : " player";
                sb.append("\n\nPosition ").append(positionArray[i]).append(": ").append(positionNumber[i]).append(player);

                if (positionNumber[i] - 2 < 0) {
                    String player2 = positionNumber[i] - 2 == -1 ? " player" : " players";
                    sb.append("\nPosition ").append(positionArray[i]).append(" needs to add more ").append(2 - positionNumber[i]).append(player2).append(" to achieve the requirement of 2 players.");
                } else {
                    sb.append("\nPosition ").append(positionArray[i]).append(" meets the requirement of 2 players.");
                }
            }
        }

        return sb.toString();
    }

    //Else method when player in team is full
    public void elseTeamFull() {
        String[] playerFull = {"Remove Player", "Return to menu page"};
        String player = getCurrent("COUNT(Player_ID)", "TotalPlayer") > 1 ? " players" : " player";

        int resultFull = showConfirmTwoOptions(jPlayerProfile.this,
                "Your roster have " + getCurrent("COUNT(Player_ID)", "TotalPlayer") + player + " now.\nIt reach the maximum number of players: " + playerMax + "\nYou are not allowed to add more player!",
                "Player Full!!",
                JOptionPane.YES_NO_OPTION,
                playerFull);

        switch (resultFull) {
            case JOptionPane.YES_OPTION -> {
                dispose();
                new jRemovePlayer().setVisible(true);
            }
            case JOptionPane.NO_OPTION -> {
                JOptionPane.showMessageDialog(jPlayerProfile.this, "You will be redirected to menu page.", "Redirected to Menu Page", JOptionPane.PLAIN_MESSAGE);

                dispose();
                new jRosterMenu().setVisible(true);
            }
            default -> {
                JOptionPane.showMessageDialog(jPlayerProfile.this, "Unknown Selection! Please try again!\nYou will be redirected to menu page.", "Unknown Selection!", JOptionPane.ERROR_MESSAGE);

                dispose();
                new jRosterMenu().setVisible(true);
            }

        }
    }

    public void elseSalaryFull() {
        String[] salaryFullOptions = {"Remove Player", "Return to menu page"};

//            "Your roster total salary will reach " + (getCurrent("SUM(Salary)", "TotalSalary") + salary) + " if you add this player: " + name +
        int resultSalaryFull = showConfirmTwoOptions(jPlayerProfile.this,
                "\nYour roster current total salary: " + getCurrent("SUM(Salary)", "TotalSalary")
                + "\nTotal salary cap: " + salaryMax
                + "\nPlease remove player if you want to add player.",
                "Salary Full!!",
                JOptionPane.YES_NO_OPTION,
                salaryFullOptions);

        switch (resultSalaryFull) {
            case JOptionPane.YES_OPTION -> {
                dispose();
                new jRemovePlayer().setVisible(true);
            }
            case JOptionPane.NO_OPTION -> {
                JOptionPane.showMessageDialog(jPlayerProfile.this, "You will be redirected to menu page.", "Redirected to Menu Page", JOptionPane.PLAIN_MESSAGE);
                dispose();
                new jRosterMenu().setVisible(true);
            }
            default -> {
                JOptionPane.showMessageDialog(jPlayerProfile.this, "Unknown Selection! Please try again!\nYou will be redirected to menu page.", "Unknown Selection!", JOptionPane.ERROR_MESSAGE);
                dispose();
                new jRosterMenu().setVisible(true);
            }

        }
    }

    //Update player profile when press btnNext
    private void updatePlayerProfile() {
        try {
            playerName.setText(rs.getString("Player_Name"));
            playerID.setText(rs.getString("Player_ID"));
            age.setText(rs.getString("Age"));
            height.setText(rs.getString("Height"));
            weight.setText(rs.getString("Weight"));
            position.setText(rs.getString("Position"));
            salary.setText(rs.getString("Salary"));
            points.setText(rs.getString("Points"));
            steals.setText(rs.getString("Steals"));
            blocks.setText(rs.getString("Blocks"));
            assists.setText(rs.getString("Assists"));
            rebounds.setText(rs.getString("Rebounds"));
            
            String page = lastPage.getLastPage();
            switch(page) {
                case "Add_Player" -> injury.setText("Healthy");
                default -> injury.setText(rs.getString("Injury_Status"));
            }
            
            currentName = rs.getString("Player_Name");
            currentID = rs.getString("Player_ID");

            String name = rs.getString("Player_Name").replace(" ", "");
            setImageMethod("src\\icons\\Player_Profile\\" + name + ".png", getPlayerProfile());

        } catch (SQLException ex) {
            Logger.getLogger(jPlayerProfile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Remove Player
    //Check if position meet requirement before removing player when player number reach 14
    public void checkPositionBeforeRemove(String position, String name, int id) {
        if (getCurrent("COUNT(Player_ID)", "TotalPlayer") <= 14) {
            getPlayerPosition();

            for (int i = 0; i < positionArray.length; i++) {
                if (positionArray[i].equalsIgnoreCase(position)) {
                    if (positionNumber[i] - 1 < 2) {
                        String[] confirmRemove = {"Add Player", "Remove Player", "Return to menu page"};                       

                        int resultConfirmRemove = showConfirmTwoOptions(jPlayerProfile.this,
                                "You should not remove this player with position: '" + positionArray[i]
                                + "'?\nYou will only left " + (positionNumber[i] - 1) + " player with this position: " + positionArray[i] + " if you remove him."
                                + "\nPlease add other player with this position if you want to remove this player!!",
                                "Insufficient Player " + positionArray[i] + "!!!",
                                JOptionPane.YES_NO_CANCEL_OPTION,
                                confirmRemove);

                        switch (resultConfirmRemove) {
                            case JOptionPane.YES_OPTION -> {
                                dispose();
                                new jAddPlayer().setVisible(true);
                            }                           
                            case JOptionPane.NO_OPTION -> {
                                dispose();
                                new jRemovePlayer().setVisible(true);
                            }
                            case JOptionPane.CANCEL_OPTION ->
                                returnMenu();
                            default ->
                                unknownInput();
                        }
                    } else {
                        removePlayer(name, id);
                    }
                }
            }
        } else {
            removePlayer(name, id);
        }
    }

    //Remove player from San Antonio
    public void removePlayer(String name, int id) {
        JOptionPane.showMessageDialog(jPlayerProfile.this, "Removing the selected player " + name + "  from your roster...", "Removing Player...", JOptionPane.PLAIN_MESSAGE);

        try {
            PreparedStatement pstmt = con.prepareStatement("DELETE FROM SAN_ANTONIO WHERE Player_Name = ?");
            pstmt.setString(1, name);
            pstmt.executeUpdate();

            //Remove player if exist in Injury Player Roster
            if (checkPlayer("INJURYPLAYERROSTER WHERE Player_Name = '" + name + "'")) {
                PreparedStatement pstmt2 = con.prepareStatement("DELETE FROM INJURYPLAYERROSTER WHERE Player_Name = ?");
                pstmt2.setString(1, name);
                pstmt2.executeUpdate();
            }

            String player = getCurrent("COUNT(Player_ID)", "TotalPlayer") > 1 ? " players" : " player";

            JOptionPane.showMessageDialog(jPlayerProfile.this, "Player is removed successfully.\nHope you will find a stronger player in future!\nYour roster have "
                    + getCurrent("COUNT(Player_ID)", "TotalPlayer") + player + " now."
                    + displayEachPosition()
                    + "\n\nYour roster current total salary is " + getCurrent("SUM(Salary)", "TotalSalary"),
                    "Removed Successfully!!",
                    JOptionPane.PLAIN_MESSAGE);

            String[] afterRemove = {"Remove Player", "Return to Menu Page"};

            int resultAfterRemove = showConfirmTwoOptions(jPlayerProfile.this,
                    "Do you want to remove player again?",
                    "Remove Player Again?",
                    JOptionPane.YES_NO_OPTION,
                    afterRemove);

            switch (resultAfterRemove) {
                case JOptionPane.YES_OPTION -> {
                    displayNextPlayer("SAN_ANTONIO", id);
                }
                case JOptionPane.NO_OPTION ->
                    returnMenu();
                default ->
                    unknownInput();
            }

        } catch (SQLException ex) {
            Logger.getLogger(NBA_SAN_ANTONIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Method for return menu
    public void returnMenu() {
        JOptionPane.showMessageDialog(jPlayerProfile.this, "You will be redirected to menu page.", "Redirected to Menu Page", JOptionPane.PLAIN_MESSAGE);
        dispose();
        new jRosterMenu().setVisible(true);
    }

    //Method for unknown input
    public void unknownInput() {
        JOptionPane.showMessageDialog(jPlayerProfile.this, "Unknown Selection! Please try again!\nYou will be redirected to menu page.", "Unknown Selection!", JOptionPane.ERROR_MESSAGE);
        dispose();
        new jRosterMenu().setVisible(true);
    }

    //Access methods
    public javax.swing.JLabel getPlayerName() {
        return playerName;
    }

    public javax.swing.JLabel getPlayerID() {
        return playerID;
    }

    public javax.swing.JLabel getAge() {
        return age;
    }

    public javax.swing.JLabel getPlayerHeight() {
        return height;
    }

    public javax.swing.JLabel getPlayerWeight() {
        return weight;
    }

    public javax.swing.JLabel getPosition() {
        return position;
    }

    public javax.swing.JLabel getSalary() {
        return salary;
    }

    public javax.swing.JLabel getPoints() {
        return points;
    }

    public javax.swing.JLabel getSteals() {
        return steals;
    }

    public javax.swing.JLabel getBlocks() {
        return blocks;
    }

    public javax.swing.JLabel getAssists() {
        return assists;
    }

    public javax.swing.JLabel getRebounds() {
        return rebounds;
    }

    public javax.swing.JLabel getIconInjury() {
        return iconInjury;
    }

    public javax.swing.JLabel getInjury() {
        return injury;
    }

    public javax.swing.JLabel getPlayerProfile() {
        return playerProfile;
    }
    
    public javax.swing.JLabel getLabelList() {
        return labelList;
    }

    public void setCurrentID(String currentID) {
        this.currentID = currentID;
    }

    public void setSelectedRow(int selectedRow) {
        this.selectedRow = selectedRow;
    }

    public javax.swing.JButton getBtnAddRemove() {
        return btnAddRemove;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nbaLogo = new javax.swing.JLabel();
        panelPlayer1 = new javax.swing.JPanel();
        iconAge = new javax.swing.JLabel();
        iconHeight = new javax.swing.JLabel();
        iconWeight = new javax.swing.JLabel();
        iconPosition = new javax.swing.JLabel();
        iconSalary = new javax.swing.JLabel();
        iconPlayerID = new javax.swing.JLabel();
        height = new javax.swing.JLabel();
        age = new javax.swing.JLabel();
        salary = new javax.swing.JLabel();
        position = new javax.swing.JLabel();
        weight = new javax.swing.JLabel();
        playerID = new javax.swing.JLabel();
        namePlayer13 = new javax.swing.JLabel();
        playerName = new javax.swing.JLabel();
        playerProfile = new javax.swing.JLabel();
        panelPlayer3 = new javax.swing.JPanel();
        iconPoints = new javax.swing.JLabel();
        iconSteals = new javax.swing.JLabel();
        iconBlocks = new javax.swing.JLabel();
        iconAssists = new javax.swing.JLabel();
        iconInjury = new javax.swing.JLabel();
        injury = new javax.swing.JLabel();
        blocks = new javax.swing.JLabel();
        assists = new javax.swing.JLabel();
        steals = new javax.swing.JLabel();
        points = new javax.swing.JLabel();
        namePlayer11 = new javax.swing.JLabel();
        iconRebounds = new javax.swing.JLabel();
        rebounds = new javax.swing.JLabel();
        labelList = new javax.swing.JLabel();
        iconCandidateList = new javax.swing.JLabel();
        btnAddRemove = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnPrevious = new javax.swing.JButton();
        backgroundBasketballCourt = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(969, 760));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        nbaLogo.setText("logo");
        getContentPane().add(nbaLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 80, 160));

        panelPlayer1.setBackground(new java.awt.Color(0, 0, 0));
        panelPlayer1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 0), 2));
        panelPlayer1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconAge.setText("Age:");
        panelPlayer1.add(iconAge, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 140, 50, 40));

        iconHeight.setText("Height:");
        panelPlayer1.add(iconHeight, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 200, 50, 50));

        iconWeight.setText("Weight:");
        panelPlayer1.add(iconWeight, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 280, 50, 40));

        iconPosition.setText("Position:");
        panelPlayer1.add(iconPosition, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 340, 50, 50));

        iconSalary.setText("Salary:");
        panelPlayer1.add(iconSalary, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 400, 50, 50));

        iconPlayerID.setText("Player ID:");
        panelPlayer1.add(iconPlayerID, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 50, 40));

        height.setFont(new java.awt.Font("HP Simplified Hans", 1, 18)); // NOI18N
        height.setForeground(new java.awt.Color(255, 255, 255));
        height.setText("height");
        panelPlayer1.add(height, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 210, 100, 40));

        age.setFont(new java.awt.Font("HP Simplified Hans", 1, 18)); // NOI18N
        age.setForeground(new java.awt.Color(255, 255, 255));
        age.setText("age");
        panelPlayer1.add(age, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 140, 100, 40));

        salary.setFont(new java.awt.Font("HP Simplified Hans", 1, 18)); // NOI18N
        salary.setForeground(new java.awt.Color(255, 255, 255));
        salary.setText("salary");
        panelPlayer1.add(salary, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 410, 100, 40));

        position.setFont(new java.awt.Font("HP Simplified Hans", 1, 18)); // NOI18N
        position.setForeground(new java.awt.Color(255, 255, 255));
        position.setText("position");
        panelPlayer1.add(position, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 350, 100, 40));

        weight.setFont(new java.awt.Font("HP Simplified Hans", 1, 18)); // NOI18N
        weight.setForeground(new java.awt.Color(255, 255, 255));
        weight.setText("weight");
        panelPlayer1.add(weight, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 280, 100, 40));

        playerID.setFont(new java.awt.Font("HP Simplified Hans", 1, 18)); // NOI18N
        playerID.setForeground(new java.awt.Color(255, 255, 255));
        playerID.setText("ID");
        panelPlayer1.add(playerID, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 70, 100, 40));

        namePlayer13.setFont(new java.awt.Font("HP Simplified Hans", 1, 24)); // NOI18N
        namePlayer13.setForeground(new java.awt.Color(255, 255, 255));
        namePlayer13.setText("Player's Info:");
        panelPlayer1.add(namePlayer13, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, 150, 50));

        getContentPane().add(panelPlayer1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, 280, 460));

        playerName.setFont(new java.awt.Font("HP Simplified Hans", 1, 24)); // NOI18N
        playerName.setForeground(new java.awt.Color(255, 255, 255));
        playerName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        playerName.setText("Player Name:");
        getContentPane().add(playerName, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 190, 310, 30));

        playerProfile.setText("Player Profile");
        getContentPane().add(playerProfile, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 240, 310, 460));

        panelPlayer3.setBackground(new java.awt.Color(0, 0, 0));
        panelPlayer3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 204, 0), 2));
        panelPlayer3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconPoints.setText("Points:");
        panelPlayer3.add(iconPoints, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 50, 50));

        iconSteals.setText("Steals:");
        panelPlayer3.add(iconSteals, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 140, 50, 40));

        iconBlocks.setText("Blocks:");
        panelPlayer3.add(iconBlocks, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 200, 50, 40));

        iconAssists.setText("Assists:");
        panelPlayer3.add(iconAssists, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 270, 50, 40));

        iconInjury.setText("Injury");
        panelPlayer3.add(iconInjury, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 400, 50, 40));

        injury.setFont(new java.awt.Font("HP Simplified Hans", 1, 18)); // NOI18N
        injury.setForeground(new java.awt.Color(255, 255, 255));
        injury.setText("injury");
        panelPlayer3.add(injury, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 400, 170, 40));

        blocks.setFont(new java.awt.Font("HP Simplified Hans", 1, 18)); // NOI18N
        blocks.setForeground(new java.awt.Color(255, 255, 255));
        blocks.setText("block");
        panelPlayer3.add(blocks, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 200, 100, 40));

        assists.setFont(new java.awt.Font("HP Simplified Hans", 1, 18)); // NOI18N
        assists.setForeground(new java.awt.Color(255, 255, 255));
        assists.setText("assist");
        panelPlayer3.add(assists, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 270, 100, 40));

        steals.setFont(new java.awt.Font("HP Simplified Hans", 1, 18)); // NOI18N
        steals.setForeground(new java.awt.Color(255, 255, 255));
        steals.setText("steal");
        panelPlayer3.add(steals, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 140, 100, 40));

        points.setFont(new java.awt.Font("HP Simplified Hans", 1, 18)); // NOI18N
        points.setForeground(new java.awt.Color(255, 255, 255));
        points.setText("point");
        panelPlayer3.add(points, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 70, 100, 40));

        namePlayer11.setFont(new java.awt.Font("HP Simplified Hans", 1, 24)); // NOI18N
        namePlayer11.setForeground(new java.awt.Color(255, 255, 255));
        namePlayer11.setText("Player's Stats:");
        panelPlayer3.add(namePlayer11, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, 170, 50));

        iconRebounds.setText("Rebounds:");
        panelPlayer3.add(iconRebounds, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 340, 50, 40));

        rebounds.setFont(new java.awt.Font("HP Simplified Hans", 1, 18)); // NOI18N
        rebounds.setForeground(new java.awt.Color(255, 255, 255));
        rebounds.setText("rebound");
        panelPlayer3.add(rebounds, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 340, 100, 40));

        getContentPane().add(panelPlayer3, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 240, 280, 460));

        labelList.setFont(new java.awt.Font("HP Simplified Hans", 1, 36)); // NOI18N
        labelList.setForeground(new java.awt.Color(255, 255, 255));
        labelList.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelList.setText("CANDIDATE LIST");
        getContentPane().add(labelList, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 40, -1, 60));

        iconCandidateList.setText("Candidate");
        getContentPane().add(iconCandidateList, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 30, 80, 80));

        btnAddRemove.setText("Add/Remove");
        btnAddRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddRemoveActionPerformed(evt);
            }
        });
        getContentPane().add(btnAddRemove, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 130, 80, 40));

        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        getContentPane().add(btnBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 130, 80, 40));

        btnNext.setText("Next");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });
        getContentPane().add(btnNext, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 190, 80, 40));

        btnPrevious.setText("Previous");
        btnPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviousActionPerformed(evt);
            }
        });
        getContentPane().add(btnPrevious, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 190, 80, 40));

        backgroundBasketballCourt.setText("background");
        getContentPane().add(backgroundBasketballCourt, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 970, 760));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        dispose();

        btnInjuryRoster.setBtnInjuryRoster("View");
        String page = lastPage.getLastPage();

        switch (page) {
            case "View_Roster" ->
                new jViewRoster(btnInjuryRoster).setVisible(true);
            case "Add_Player" ->
                new jAddPlayer().setVisible(true);
            case "Remove_Player" ->
                new jRemovePlayer().setVisible(true);
        }
    }//GEN-LAST:event_btnBackActionPerformed

    //Two function button (Add / Remove), Disappear when view roster
    private void btnAddRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddRemoveActionPerformed
        String page = lastPage.getLastPage();

        switch (page) {

            //Add function button
            case "Add_Player" -> {
                //Check if exceed salary limit
                if (getCurrent("SUM(Salary)", "totalSalary") <= salaryMax) {

                    //Check if exist player limit
                    if (getCurrent("COUNT(Player_ID)", "totalPlayer") < playerMax) {

                        int result = showConfirmDialog(jPlayerProfile.this, "Are you sure you want to add this player into your roster?", "Add Player into Roster.", JOptionPane.YES_NO_OPTION);

                        switch (result) {
                            case JOptionPane.YES_OPTION -> {
                                //Check whether this player duplicate
                                if (!checkPlayer("SAN_ANTONIO WHERE Player_ID = " + currentID)) {
                                    try {
                                        String position = null, name = null, height = null;
                                        int salary = 0, id = 0, age = 0, weight = 0;
                                        double points = 0, steals = 0, blocks = 0, assists = 0, rebounds = 0;

                                        PreparedStatement pstmt = con.prepareStatement("SELECT * FROM CANDIDATE_LIST WHERE Player_ID = ?");
                                        pstmt.setString(1, currentID);

                                        ResultSet rs2 = pstmt.executeQuery();
                                        if (rs2.next()) {
                                            id = rs2.getInt("Player_ID");
                                            name = rs2.getString("Player_Name");
                                            age = rs2.getInt("Age");
                                            height = rs2.getString("Height");
                                            weight = rs2.getInt("Weight");
                                            position = rs2.getString("Position");
                                            salary = rs2.getInt("Salary");
                                            points = rs2.getDouble("Points");
                                            steals = rs2.getDouble("Steals");
                                            blocks = rs2.getDouble("Blocks");
                                            assists = rs2.getDouble("Assists");
                                            rebounds = rs2.getDouble("Rebounds");
                                        }

                                        //Check whether current total salary + this salary exceed limit
                                        if (getCurrent("SUM(Salary)", "TotalSalary") + salary <= 20000) {

                                            //Check whether the player is swingman (can play 2 position)
                                            if (position.length() > 1) {
                                                String[] twoPosition = position.split("-");

                                                int resultSwingman = showConfirmTwoOptions(jPlayerProfile.this,
                                                        "\nAttention! The player you selected: " + name + " can play two positions.\n" + twoPosition[0] + " and " + twoPosition[1] + "\nPlease assign only a suitable position for the player you selected.",
                                                        "Swingman Found!",
                                                        JOptionPane.YES_NO_OPTION,
                                                        twoPosition);

                                                switch (resultSwingman) {
                                                    case JOptionPane.YES_OPTION ->
                                                        position = twoPosition[0];
                                                    case JOptionPane.NO_OPTION ->
                                                        position = twoPosition[1];
                                                    default -> {
                                                        JOptionPane.showMessageDialog(jPlayerProfile.this, "Unknown Selection! Please try again!\nYou will be redirected to menu page.", "Unknown Selection!", JOptionPane.ERROR_MESSAGE);
                                                        dispose();
                                                        new jRosterMenu().setVisible(true);
                                                    }

                                                }
                                            }

                                            //If he is not swingman, just call next function (Check position)
                                            checkPositionBeforeAdd(id, name, age, height, weight, position, salary, points, steals, blocks, assists, rebounds);
                                        } else {
                                            elseSalaryFull();
                                        }
                                    } catch (SQLException ex) {
                                        Logger.getLogger(jAddPlayer.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(jPlayerProfile.this,
                                            "The player you selected: " + currentName + " is existing in your roster already."
                                            + "\nPlease choose another player.",
                                            "Duplicate Player Found!!",
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                            case JOptionPane.NO_OPTION -> {
                                String[] chooseAgain = {"Yes", "No"};
                                int resultChooseAgain = showConfirmTwoOptions(jPlayerProfile.this,
                                        "Do you want to choose a player again?",
                                        "Choose Player Again?",
                                        JOptionPane.YES_NO_OPTION,
                                        chooseAgain);

                                switch (resultChooseAgain) {
                                    case JOptionPane.YES_OPTION -> {
                                        //Continue to choose player
                                    }
                                    case JOptionPane.NO_OPTION -> {
                                        JOptionPane.showMessageDialog(jPlayerProfile.this, "You will be redirected to menu page.", "Redirected to Menu Page", JOptionPane.PLAIN_MESSAGE);

                                        dispose();
                                        new jRosterMenu().setVisible(true);
                                    }
                                    default -> {
                                        JOptionPane.showMessageDialog(jPlayerProfile.this, "Unknown Selection! Please try again!\nYou will be redirected to menu page.", "Unknown Selection!", JOptionPane.ERROR_MESSAGE);

                                        dispose();
                                        new jRosterMenu().setVisible(true);
                                    }

                                }
                            }
                            default -> {
                                JOptionPane.showMessageDialog(jPlayerProfile.this, "Unknown Selection! Please try again!\nYou will be redirected to menu page.", "Unknown Selection!", JOptionPane.ERROR_MESSAGE);
                                dispose();
                                new jRosterMenu().setVisible(true);
                            }

                        }
                    } else {
                        elseTeamFull();
                    }
                } else {
                    elseSalaryFull();
                }
            }

            //Remove function button
            case "Remove_Player" -> {
                //Check if exist player limit
                if (getCurrent("COUNT(Player_ID)", "totalPlayer") > playerMin) {
                    try {
                        String position = null, name = null;
                        int id = 0;

                        PreparedStatement pstmt = con.prepareStatement("SELECT * FROM SAN_ANTONIO WHERE Player_ID = ?");
                        pstmt.setString(1, currentID);
                        ResultSet rs = pstmt.executeQuery();

                        if (rs.next()) {
                            name = rs.getString("Player_Name");
                            position = rs.getString("Position");
                            id = rs.getInt("Player_ID");

                            int resultRemove = showConfirmDialog(jPlayerProfile.this, "Are you sure you want to remove this player from SAN ANTONIO roster?", "Remove Player from Roster", JOptionPane.YES_NO_OPTION);

                            switch (resultRemove) {
                                case JOptionPane.YES_OPTION ->
                                    checkPositionBeforeRemove(position, name, id);
                                case JOptionPane.NO_OPTION -> {
                                    int resultReselect = showConfirmDialog(jPlayerProfile.this, "Hmm... It seems you have changed your mind.\nDo you want to reselect and remove player from SAN ANTONIO roster?", "Reselect and Remove Player?", JOptionPane.QUESTION_MESSAGE);

                                    switch (resultReselect) {
                                        case JOptionPane.YES_OPTION -> {
                                            dispose();
                                            new jRemovePlayer().setVisible(true);
                                        }
                                        case JOptionPane.NO_OPTION ->
                                            returnMenu();
                                        default ->
                                            unknownInput();
                                    }
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(jPlayerProfile.this, "No players found.", "Your Roster is Empty!", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (SQLException ex) {
                        Logger.getLogger(jRemovePlayer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    String[] lessPlayerOptions = {"Add Player", "Return to menu page"};

                    int resultLessPlayer = showConfirmTwoOptions(jPlayerProfile.this,
                            "You should not remove player from your roster as you only left " + getCurrent("COUNT(Player_ID)", "TotalPlayer") + " players now. \nYou should have a minimum of 10 players in your roster.",
                            "Player Full!!",
                            JOptionPane.YES_NO_OPTION,
                            lessPlayerOptions);

                    switch (resultLessPlayer) {
                        case JOptionPane.YES_OPTION -> {
                            dispose();
                            new jAddPlayer().setVisible(true);
                        }
                        case JOptionPane.NO_OPTION ->
                            returnMenu();
                        default ->
                            unknownInput();
                    }
                }
            }
        }
    }//GEN-LAST:event_btnAddRemoveActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        try {
            if (rs.next()) {
                updatePlayerProfile();
                currentName = rs.getString("Player_Name");
                currentID = rs.getString("Player_ID");

                btnNext.setVisible(!rs.isLast());
                btnPrevious.setVisible(!rs.isFirst());

            }

        } catch (SQLException ex) {
            Logger.getLogger(jPlayerProfile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousActionPerformed
        try {
            if (rs.previous()) {
                updatePlayerProfile();
                currentName = rs.getString("Player_Name");
                currentID = rs.getString("Player_ID");

                btnNext.setVisible(!rs.isLast());
                btnPrevious.setVisible(!rs.isFirst());

            }

        } catch (SQLException ex) {
            Logger.getLogger(jPlayerProfile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnPreviousActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(jPlayerProfile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(jPlayerProfile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(jPlayerProfile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(jPlayerProfile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new jPlayerProfile().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel age;
    private javax.swing.JLabel assists;
    private javax.swing.JLabel backgroundBasketballCourt;
    private javax.swing.JLabel blocks;
    private javax.swing.JButton btnAddRemove;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JLabel height;
    private javax.swing.JLabel iconAge;
    private javax.swing.JLabel iconAssists;
    private javax.swing.JLabel iconBlocks;
    private javax.swing.JLabel iconCandidateList;
    private javax.swing.JLabel iconHeight;
    private javax.swing.JLabel iconInjury;
    private javax.swing.JLabel iconPlayerID;
    private javax.swing.JLabel iconPoints;
    private javax.swing.JLabel iconPosition;
    private javax.swing.JLabel iconRebounds;
    private javax.swing.JLabel iconSalary;
    private javax.swing.JLabel iconSteals;
    private javax.swing.JLabel iconWeight;
    private javax.swing.JLabel injury;
    private javax.swing.JLabel labelList;
    private javax.swing.JLabel namePlayer11;
    private javax.swing.JLabel namePlayer13;
    private javax.swing.JLabel nbaLogo;
    private javax.swing.JPanel panelPlayer1;
    private javax.swing.JPanel panelPlayer3;
    private javax.swing.JLabel playerID;
    private javax.swing.JLabel playerName;
    private javax.swing.JLabel playerProfile;
    private javax.swing.JLabel points;
    private javax.swing.JLabel position;
    private javax.swing.JLabel rebounds;
    private javax.swing.JLabel salary;
    private javax.swing.JLabel steals;
    private javax.swing.JLabel weight;
    // End of variables declaration//GEN-END:variables
}
