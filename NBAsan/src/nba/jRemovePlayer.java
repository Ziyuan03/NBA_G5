/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package nba;

import java.awt.Component;
import java.awt.Image;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author user
 */
public class jRemovePlayer extends javax.swing.JFrame {

    Connection con;
    int playerMax = 15, playerMin = 10, salaryMax = 20000;
    DefaultTableModel model;
    String[] positionArray = {"G", "F", "C"};
    int[] positionNumber = new int[3];
    private LastPageStored lastPage = new LastPageStored();
    private PlayerTypeButton btnType = new PlayerTypeButton();
    private BtnInjuryOrRoster btnInjuryRoster = new BtnInjuryOrRoster();

    public jRemovePlayer() {
        initComponents();
        setImage();
        createConnection();
        displayTable("Salary >= 0", "Remove Player");
        updateInfo();
        model = (DefaultTableModel) table.getModel();
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(190);
        table.getColumnModel().getColumn(11).setPreferredWidth(90);
        table.getColumnModel().getColumn(12).setPreferredWidth(70);
    }

    private void setImage() {
        setLabelImageMethod("src\\icons\\removePlayer2.png", icon);
        setLabelImageMethod("src\\icons\\nbaLogo.png", nbaLogo1);
        setButtonImageMethod("src\\icons\\allPlayers.png", btnAllPlayers);
        setButtonImageMethod("src\\icons\\superstarPlayer.png", btnSuperstar);
        setButtonImageMethod("src\\icons\\nonSuperstarPlayer.png", btnNonSuperstar);
    }

    //Method set label image
    public void setLabelImageMethod(String imagePath, JLabel labelName) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage();
        Image imgScale = img.getScaledInstance(labelName.getWidth(), labelName.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(imgScale);
        labelName.setIcon(scaledIcon);
    }

    //Method set button image
    public void setButtonImageMethod(String imagePath, JButton labelName) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage();
        Image imgScale = img.getScaledInstance(labelName.getWidth(), labelName.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(imgScale);
        labelName.setIcon(scaledIcon);
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

    //Method Display Table 
    public void displayTable(String variable, String text) {
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM SAN_ANTONIO WHERE " + variable + " ORDER BY Player_ID ASC");
            ResultSet rs = pstmt.executeQuery();

            ResultSetMetaData stData = rs.getMetaData();
            int q = stData.getColumnCount();

            DefaultTableModel recordTable = (DefaultTableModel) table.getModel();
            recordTable.setRowCount(0);

            while (rs.next()) {
                Vector<Object> columnData = new Vector<>(q);

                columnData.add(rs.getString("Player_ID"));
                columnData.add(rs.getString("Player_Name"));
                columnData.add(rs.getString("Age"));
                columnData.add(rs.getString("Height"));
                columnData.add(rs.getString("Weight"));
                columnData.add(rs.getString("Position"));
                columnData.add(rs.getString("Salary"));
                columnData.add(rs.getString("Points"));
                columnData.add(rs.getString("Steals"));
                columnData.add(rs.getString("Blocks"));
                columnData.add(rs.getString("Assists"));
                columnData.add(rs.getString("Rebounds"));
                columnData.add(rs.getString("Injury_Status"));

                recordTable.addRow(columnData);
            }

            title.setText(text);

        } catch (SQLException ex) {
            Logger.getLogger(jViewRoster.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Calculate current player(s) in roster / total salary (Multiuse Method)
    public int getCurrent(String function, String functionName, String condition) {
        int current = 0;

        try {
            if (con == null) {
                current = 0;
            } else {
                Statement stmt = con.createStatement();

                ResultSet rs = stmt.executeQuery("SELECT " + function + " AS '" + functionName + "' FROM SAN_ANTONIO " + condition);

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

    //Update the text info
    public void updateInfo() {
        int playerNumber = getCurrent("COUNT(Player_ID)", "totalPlayers", "");
        int totalSalary = getCurrent("SUM(Salary)", "totalSalary", "");
        int healthyPlayer = getCurrent("COUNT(*)", "healthyPlayers", "WHERE Injury_Status = 'Healthy'");
        int injuryPlayer = playerNumber - healthyPlayer;

        String injuryUpdate;

        if (healthyPlayer == playerNumber) {
            injuryUpdate = "\n\nAll players are healthy.";
        } else if (injuryPlayer == playerNumber) {
            injuryUpdate = "\n\nAll players are injured.";
        } else {
            injuryUpdate = "\n\nHealthy" + getPlayerPlayers(healthyPlayer) + ": Injury" + getPlayerPlayers(injuryPlayer) + " = " + healthyPlayer + " : " + injuryPlayer;
        }

        textInfo.setText("Current roster" + getPlayerPlayers(playerNumber) + ": " + playerNumber
                + "\nCurrent roster salary: " + totalSalary
                + displayEachPosition()
                + injuryUpdate);
    }

    //Get "players" or "player"
    public String getPlayerPlayers(int player) {
        return player > 1 ? " players" : " player";
    }

    //Set JOptionPane positon and get the selection
    public int showConfirmDialog(Component parentComponent, Object message, String title, int optionType) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.QUESTION_MESSAGE, optionType);
        JDialog dialog = optionPane.createDialog(parentComponent, title);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        dialog.setLocation(400, 470);
        dialog.setVisible(true);

        return (int) optionPane.getValue();
    }

    //Set JOptionPane position and swingman selection
    public int showConfirmTwoOptions(Component parentComponent, Object message, String title, int optionType, Object[] options) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.QUESTION_MESSAGE, optionType, null, options, options[0]);
        JDialog dialog = optionPane.createDialog(parentComponent, title);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        dialog.setLocation(400, 470);
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

    //Check whether player' name entered exist
    public boolean playerExist(String condition) {
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM " + condition);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(jAddPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    //Method for return menu
    public void returnMenu() {
        JOptionPane.showMessageDialog(jRemovePlayer.this, "You will be redirected to menu page.", "Redirected to Menu Page", JOptionPane.PLAIN_MESSAGE);
        dispose();
        new jRosterMenu().setVisible(true);
    }

    //Method for unknown input
    public void unknownInput() {
        JOptionPane.showMessageDialog(jRemovePlayer.this, "Unknown Selection! Please try again!\nYou will be redirected to menu page.", "Unknown Selection!", JOptionPane.ERROR_MESSAGE);
        dispose();
        new jRosterMenu().setVisible(true);
    }

    //Check if position meet requirement before removing player when player number reach 14
    public void checkPositionBeforeRemove(String position, String name) {
        if (getCurrent("COUNT(Player_ID)", "TotalPlayer", "") <= 14) {
            getPlayerPosition();

            for (int i = 0; i < positionArray.length; i++) {
                if (positionArray[i].equalsIgnoreCase(position)) {
                    if (positionNumber[i] - 1 < 2) {
                        String[] confirmRemove = {"Add Player", "Remove Player", "Return to menu page"};                       

                        int resultConfirmRemove = showConfirmTwoOptions(jRemovePlayer.this,
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
                        removePlayer(name);
                    }
                }
            }
        } else {
            removePlayer(name);
        }
    }

    //Remove player from San Antonio
    public void removePlayer(String name) {
        JOptionPane.showMessageDialog(jRemovePlayer.this, "Removing the selected player " + name + "  from your roster...", "Removing Player...", JOptionPane.PLAIN_MESSAGE);

        try {
            PreparedStatement pstmt = con.prepareStatement("DELETE FROM SAN_ANTONIO WHERE Player_Name = ?");
            pstmt.setString(1, name);
            pstmt.executeUpdate();

            //Remove player if exist in Injury Player Roster
            if (playerExist("INJURYPLAYERROSTER WHERE Player_Name = '" + name + "'")) {
                PreparedStatement pstmt2 = con.prepareStatement("DELETE FROM INJURYPLAYERROSTER WHERE Player_Name = ?");
                pstmt2.setString(1, name);
                pstmt2.executeUpdate();
            }

            String player = getCurrent("COUNT(Player_ID)", "TotalPlayer", "") > 1 ? " players" : " player";

            JOptionPane.showMessageDialog(jRemovePlayer.this, "Player is removed successfully.\nHope you will find a stronger player in future!\nYour roster have "
                    + getCurrent("COUNT(Player_ID)", "TotalPlayer", "") + player + " now."
                    + displayEachPosition()
                    + "\n\nYour roster current total salary is " + getCurrent("SUM(Salary)", "TotalSalary", ""),
                    "Removed Successfully!!",
                    JOptionPane.PLAIN_MESSAGE);

            String[] afterRemove = {"Remove Player", "View Roster"};

            int resultAfterRemove = showConfirmTwoOptions(jRemovePlayer.this,
                    "Do you want to remove player again?",
                    "Remove Player Again?",
                    JOptionPane.YES_NO_OPTION,
                    afterRemove);

            switch (resultAfterRemove) {
                case JOptionPane.YES_OPTION -> {
                    if (getCurrent("SUM(Player_ID)", "totalPlayer", "") <= 10) {
                        outputPlayerLess();
                    } else {
                        dispose();
                        new jRemovePlayer().setVisible(true);
                    }
                }
                case JOptionPane.NO_OPTION -> {
                    dispose();
                    btnInjuryRoster.setBtnInjuryRoster("View");
                    new jViewRoster(btnInjuryRoster).setVisible(true);
                }
                default ->
                    unknownInput();
            }

        } catch (SQLException ex) {
            Logger.getLogger(NBA_SAN_ANTONIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Output method for player less than 10
    public void outputPlayerLess() {
        String[] lessPlayerOptions = {"Add Player", "Return to menu page"};

        int resultLessPlayer = showConfirmTwoOptions(jRemovePlayer.this,
                "You should not remove player from your roster as you only left " + getCurrent("COUNT(Player_ID)", "TotalPlayer", "") + " players now. \nYou should have a minimum of 10 players in your roster.",
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nbaLogo = new javax.swing.JLabel();
        panelSide = new javax.swing.JPanel();
        btnNonSuperstar = new javax.swing.JButton();
        btnAllPlayers = new javax.swing.JButton();
        btnSuperstar = new javax.swing.JButton();
        nbaLogo1 = new javax.swing.JLabel();
        panelCentre = new javax.swing.JPanel();
        panelTitle = new javax.swing.JPanel();
        title = new javax.swing.JLabel();
        icon = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        textInfo = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        labelReady = new javax.swing.JLabel();
        textID = new javax.swing.JTextField();
        btnRemove = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        nbaLogo.setText("nba");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(969, 760));
        setPreferredSize(new java.awt.Dimension(969, 760));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelSide.setBackground(new java.awt.Color(255, 51, 0));

        btnNonSuperstar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNonSuperstarActionPerformed(evt);
            }
        });

        btnAllPlayers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAllPlayersActionPerformed(evt);
            }
        });

        btnSuperstar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuperstarActionPerformed(evt);
            }
        });

        nbaLogo1.setText("nba");

        javax.swing.GroupLayout panelSideLayout = new javax.swing.GroupLayout(panelSide);
        panelSide.setLayout(panelSideLayout);
        panelSideLayout.setHorizontalGroup(
            panelSideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSideLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelSideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNonSuperstar, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSuperstar, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAllPlayers, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
            .addGroup(panelSideLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nbaLogo1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelSideLayout.setVerticalGroup(
            panelSideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSideLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(nbaLogo1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 150, Short.MAX_VALUE)
                .addComponent(btnAllPlayers, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSuperstar, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnNonSuperstar, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(174, 174, 174))
        );

        getContentPane().add(panelSide, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 100, 760));

        panelCentre.setBackground(new java.awt.Color(0, 0, 153));

        panelTitle.setBackground(new java.awt.Color(255, 255, 255));

        title.setFont(new java.awt.Font("Copperplate Gothic Bold", 1, 36)); // NOI18N
        title.setText("Remove Player");

        icon.setText("jLabel1");

        javax.swing.GroupLayout panelTitleLayout = new javax.swing.GroupLayout(panelTitle);
        panelTitle.setLayout(panelTitleLayout);
        panelTitleLayout.setHorizontalGroup(
            panelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTitleLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(title, javax.swing.GroupLayout.DEFAULT_SIZE, 652, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(icon, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59))
        );
        panelTitleLayout.setVerticalGroup(
            panelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTitleLayout.createSequentialGroup()
                .addComponent(icon, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 4, Short.MAX_VALUE))
            .addGroup(panelTitleLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        textInfo.setEditable(false);
        textInfo.setBackground(new java.awt.Color(0, 0, 153));
        textInfo.setColumns(20);
        textInfo.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        textInfo.setForeground(new java.awt.Color(255, 255, 255));
        textInfo.setRows(5);
        jScrollPane2.setViewportView(textInfo);

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Player_Name", "Age", "Height", "Weight", "Position", "Salary", "Points", "Steals", "Blocks", "Assists", "Rebounds", "Injury"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.getTableHeader().setReorderingAllowed(false);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table);

        labelReady.setFont(new java.awt.Font("Sitka Text", 1, 18)); // NOI18N
        labelReady.setForeground(new java.awt.Color(255, 255, 255));
        labelReady.setText("Ready? Type the Player_ID:");

        textID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textIDActionPerformed(evt);
            }
        });

        btnRemove.setText("Remove");
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelCentreLayout = new javax.swing.GroupLayout(panelCentre);
        panelCentre.setLayout(panelCentreLayout);
        panelCentreLayout.setHorizontalGroup(
            panelCentreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCentreLayout.createSequentialGroup()
                .addComponent(panelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(panelCentreLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(panelCentreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelCentreLayout.createSequentialGroup()
                        .addComponent(labelReady)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textID, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(125, 125, 125)
                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelCentreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane2)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelCentreLayout.setVerticalGroup(
            panelCentreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCentreLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(panelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelCentreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCentreLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelCentreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textID, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelCentreLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(labelReady, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(43, 43, 43))
        );

        getContentPane().add(panelCentre, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 0, 870, 760));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        if (textID.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(jRemovePlayer.this, "Please enter a player's name if you want to remove player from your roster!!", "No Player's Name found!", JOptionPane.ERROR_MESSAGE);
            textID.grabFocus();
        } else {
            if (playerExist("SAN_ANTONIO WHERE Player_ID = " + textID.getText())) {

                //Check if exist player limit
                if (getCurrent("COUNT(Player_ID)", "totalPlayer", "") > playerMin) {
                    try {
                        String position = null, name = null;
                        PreparedStatement pstmt = con.prepareStatement("SELECT * FROM SAN_ANTONIO WHERE Player_ID = ?");
                        pstmt.setString(1, textID.getText());
                        ResultSet rs = pstmt.executeQuery();

                        if (rs.next()) {
                            name = rs.getString("Player_Name");
                            position = rs.getString("Position");

                            displayTable("Player_ID = '" + textID.getText() + "'", "Remove Player");
                            int resultRemove = showConfirmDialog(jRemovePlayer.this, "Are you sure you want to remove this player from SAN ANTONIO roster?", "Remove Player from Roster", JOptionPane.YES_NO_OPTION);

                            switch (resultRemove) {
                                case JOptionPane.YES_OPTION ->
                                    checkPositionBeforeRemove(position, name);
                                case JOptionPane.NO_OPTION -> {
                                    int resultReselect = showConfirmDialog(jRemovePlayer.this, "Hmm... It seems you have changed your mind.\nDo you want to reselect and remove player from SAN ANTONIO roster?", "Reselect and Remove Player?", JOptionPane.QUESTION_MESSAGE);

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
                            JOptionPane.showMessageDialog(jRemovePlayer.this, "No players found.", "Your Roster is Empty!", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (SQLException ex) {
                        Logger.getLogger(jRemovePlayer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    outputPlayerLess();
                }
            } else {
                JOptionPane.showMessageDialog(jRemovePlayer.this, "The player's id you entered: '" + textID.getText() + "' does not exist in SAN_ANTONIO!!", "No Player Found!", JOptionPane.ERROR_MESSAGE);
                textID.grabFocus();
            }
        }
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        dispose();
        new jRosterMenu().setVisible(true);
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnAllPlayersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAllPlayersActionPerformed
        displayTable("Salary >= 0", "Remove Player");
        btnType.setButtonType("All_Players");
    }//GEN-LAST:event_btnAllPlayersActionPerformed

    private void btnSuperstarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuperstarActionPerformed
        displayTable("Salary = 3000", "Remove Superstar Player");
        btnType.setButtonType("Superstar_Players");
    }//GEN-LAST:event_btnSuperstarActionPerformed

    private void btnNonSuperstarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNonSuperstarActionPerformed
        displayTable("Salary = 1000", "Remove Non-Superstar Player");
        btnType.setButtonType("Non_Superstar_Players");
    }//GEN-LAST:event_btnNonSuperstarActionPerformed

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a player from table!", "Unknown player selected.", JOptionPane.ERROR_MESSAGE);
        } else {
            lastPage.setLastPage("Remove_Player");

            dispose();

            jPlayerProfile jap = new jPlayerProfile(model.getValueAt(selectedRow, 1).toString(), lastPage, btnType);
            jap.setVisible(true);

            jap.getPlayerName().setText(model.getValueAt(selectedRow, 1).toString());
            jap.getPlayerID().setText(model.getValueAt(selectedRow, 0).toString());
            jap.getAge().setText(model.getValueAt(selectedRow, 2).toString());
            jap.getPlayerHeight().setText(model.getValueAt(selectedRow, 3).toString());
            jap.getPlayerWeight().setText(model.getValueAt(selectedRow, 4).toString());
            jap.getPosition().setText(model.getValueAt(selectedRow, 5).toString());
            jap.getSalary().setText(model.getValueAt(selectedRow, 6).toString());
            jap.getPoints().setText(model.getValueAt(selectedRow, 7).toString());
            jap.getRebounds().setText(model.getValueAt(selectedRow, 8).toString());
            jap.getAssists().setText(model.getValueAt(selectedRow, 9).toString());
            jap.getSteals().setText(model.getValueAt(selectedRow, 10).toString());
            jap.getBlocks().setText(model.getValueAt(selectedRow, 11).toString());
            jap.getInjury().setText(model.getValueAt(selectedRow, 12).toString());

            //Set icon for iconInjury
            jap.setImageMethod("src\\icons\\iconPlayerProfile\\iconInjury.png", jap.getIconInjury());

            //Remove space in original name            
            String name = model.getValueAt(selectedRow, 1).toString().replace(" ", "");
            jap.setImageMethod("src\\icons\\Player_Profile\\" + name + ".png", jap.getPlayerProfile());

            //Store original id with space
            jap.setCurrentID(model.getValueAt(selectedRow, 0).toString());
            
            // Store the selected row index
            jap.setSelectedRow(selectedRow);
            
            //Set the Label List
            jap.getLabelList().setText("SAN ANTONIO");
        }
    }//GEN-LAST:event_tableMouseClicked

    private void textIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textIDActionPerformed

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
            java.util.logging.Logger.getLogger(jRemovePlayer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(jRemovePlayer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(jRemovePlayer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(jRemovePlayer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new jRemovePlayer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAllPlayers;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnNonSuperstar;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnSuperstar;
    private javax.swing.JLabel icon;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelReady;
    private javax.swing.JLabel nbaLogo;
    private javax.swing.JLabel nbaLogo1;
    private javax.swing.JPanel panelCentre;
    private javax.swing.JPanel panelSide;
    private javax.swing.JPanel panelTitle;
    private javax.swing.JTable table;
    private javax.swing.JTextField textID;
    private javax.swing.JTextArea textInfo;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables
}
