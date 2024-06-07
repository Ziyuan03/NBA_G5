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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Connection;
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
public class jAddPlayer extends javax.swing.JFrame {

    Connection con;
    DefaultTableModel model;
    String[] positionArray = {"G", "F", "C"};
    int[] positionNumber = new int[3];
    int salaryMax = 20000;
    int playerMax = 15, playerMin = 10;
    private LastPageStored lastPage = new LastPageStored();
    private PlayerTypeButton btnType = new PlayerTypeButton();

    public jAddPlayer() {
        createConnection();
        initComponents();
        setImage();
        displayTable("Salary >= 0", "Add Player");
        updateInfo();
        model = (DefaultTableModel) table.getModel();
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(190);
        table.getColumnModel().getColumn(8).setPreferredWidth(90);
    }

    private void setImage() {
        setLabelImageMethod("src\\icons\\addPlayer2.png", icon);
        setLabelImageMethod("src\\icons\\nbaLogo.png", nbaLogo);
        setButtonImageMethod("src\\icons\\allPlayers.png", btnAllCandidates);
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
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM CANDIDATE_LIST WHERE " + variable + " AND Player_ID NOT IN (SELECT Player_ID FROM SAN_ANTONIO)");
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
                columnData.add(rs.getString("Rebounds"));
                columnData.add(rs.getString("Assists"));
                columnData.add(rs.getString("Steals"));
                columnData.add(rs.getString("Blocks"));

                recordTable.addRow(columnData);
            }

            title.setText(text);

        } catch (SQLException ex) {
            Logger.getLogger(jViewRoster.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Check whether player' name entered exist
    public boolean playerExist(String id, String tableName) {
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM " + tableName + " WHERE Player_ID = ?");
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(jAddPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
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

    //Method check whether player duplicate
    public boolean checkPlayerDuplicate(String id) {
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM SAN_ANTONIO WHERE Player_ID = ?");
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            return rs.next();

        } catch (SQLException ex) {
            Logger.getLogger(NBA_SAN_ANTONIO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    //Check if position meet requirement before adding player when reach 6 players
    public void checkPositionBeforeAdd(int id, String name, int age, String height, int weight, String position, int salary, double points, double steals, double blocks, double assists, double rebounds) {
        if (getCurrent("COUNT(Player_ID)", "TotalPlayer", "") >= 6) {
            if (booleanPlayerPosition()) {
                addPlayer(id, name, age, height, weight, position, salary, points, steals, blocks, assists, rebounds);
            } else {
                for (int i = 0; i < positionArray.length; i++) {
                    if (positionArray[i].equalsIgnoreCase(position)) {
                        if (2 - positionNumber[i] > 0) {
                            addPlayer(id, name, age, height, weight, position, salary, points, steals, blocks, assists, rebounds);
                        } else {
                            JOptionPane.showMessageDialog(jAddPlayer.this,
                                    "You should not add this player as this position: '" + position + "' already meets the requirement. \nYou can add this player again after you meet the other position requirements. \nYou will be redirected to add player page.");
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

    //Add player into team
    public void addPlayer(int id, String name, int age, String height, int weight, String position, int salary, double points, double steals, double blocks, double assists, double rebounds) {
        try {
            JOptionPane.showMessageDialog(jAddPlayer.this, "Adding the selected player " + name + " into your roster...", "Adding Player...", JOptionPane.PLAIN_MESSAGE);

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

            String player = getCurrent("COUNT(Player_ID)", "TotalPlayer", "") > 1 ? " players " : " player ";
            JOptionPane.showMessageDialog(jAddPlayer.this,
                    "Congratulations on successfully recruiting a powerful player into your roster 'San_Antonio' \nYour roster have " + getCurrent("COUNT(Player_ID)", "TotalPlayer", "") + player + "now."
                    + displayEachPosition()
                    + "\n\nYour roster current total salary is " + getCurrent("SUM(Salary)", "TotalSalary", ""),
                    "Added Player Successfully",
                    JOptionPane.PLAIN_MESSAGE);

            if (getCurrent("COUNT(Player_ID)", "TotalPlayer", "") == playerMax) {
                elseTeamFull();
            } else {
                String[] continueAdd = {"Continue Add Player", "Return to menu page"};

                int resultContinueAdd = showConfirmTwoOptions(jAddPlayer.this,
                        "Do you want to continue adding player into your roster?",
                        "Continue Adding Player?",
                        JOptionPane.YES_NO_OPTION,
                        continueAdd);

                switch (resultContinueAdd) {
                    case JOptionPane.YES_OPTION -> {
                        if (getCurrent("SUM(Salary)", "TotalSalary", "") >= 20000) {
                            elseSalaryFull();
                        } else {
                            dispose();
                            new jAddPlayer().setVisible(true);
                        }
                    }
                    case JOptionPane.NO_OPTION -> {
                        JOptionPane.showMessageDialog(jAddPlayer.this, "You will be redirected to menu page.", "Redirected to Menu Page", JOptionPane.PLAIN_MESSAGE);

                        dispose();
                        new jRosterMenu().setVisible(true);
                    }
                    default -> {
                        JOptionPane.showMessageDialog(jAddPlayer.this, "Unknown Selection! Please try again!\nYou will be redirected to menu page.", "Unknown Selection!", JOptionPane.ERROR_MESSAGE);

                        dispose();
                        new jRosterMenu().setVisible(true);
                    }


                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(NBA_SQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Else method when player in team is full
    public void elseTeamFull() {
        String[] playerFull = {"Remove Player", "Return to menu page"};
        String player = getCurrent("COUNT(Player_ID)", "TotalPlayer", "") > 1 ? " players" : " player";

        int resultFull = showConfirmTwoOptions(jAddPlayer.this,
                "Your roster have " + getCurrent("COUNT(Player_ID)", "TotalPlayer", "") + player + " now.\nIt reach the maximum number of players: " + playerMax + "\nYou are not allowed to add more player!",
                "Player Full!!",
                JOptionPane.YES_NO_OPTION,
                playerFull);

        switch (resultFull) {
            case JOptionPane.YES_OPTION -> {
                dispose();
                new jRemovePlayer().setVisible(true);
            }
            case JOptionPane.NO_OPTION -> {
                JOptionPane.showMessageDialog(jAddPlayer.this, "You will be redirected to menu page.", "Redirected to Menu Page", JOptionPane.PLAIN_MESSAGE);

                dispose();
                new jRosterMenu().setVisible(true);
            }
            default -> {
                JOptionPane.showMessageDialog(jAddPlayer.this, "Unknown Selection! Please try again!\nYou will be redirected to menu page.", "Unknown Selection!", JOptionPane.ERROR_MESSAGE);

                dispose();
                new jRosterMenu().setVisible(true);
            }


        }
    }

    public void elseSalaryFull() {
        String[] salaryFullOptions = {"Remove Player", "Return to menu page"};

//            "Your roster total salary will reach " + (getCurrent("SUM(Salary)", "TotalSalary") + salary) + " if you add this player: " + name +
        int resultSalaryFull = showConfirmTwoOptions(jAddPlayer.this,
                "\nYour roster current total salary: " + getCurrent("SUM(Salary)", "TotalSalary", "")
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
                JOptionPane.showMessageDialog(jAddPlayer.this, "You will be redirected to menu page.", "Redirected to Menu Page", JOptionPane.PLAIN_MESSAGE);
                dispose();
                new jRosterMenu().setVisible(true);
            }
            default -> {
                JOptionPane.showMessageDialog(jAddPlayer.this, "Unknown Selection! Please try again!\nYou will be redirected to menu page.", "Unknown Selection!", JOptionPane.ERROR_MESSAGE);
                dispose();
                new jRosterMenu().setVisible(true);
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
        
        if(healthyPlayer == playerNumber) {
            injuryUpdate = "\n\nAll players are healthy.";
        } else if(injuryPlayer == playerNumber) {
            injuryUpdate = "\n\nAll players are injured.";
        } else {            
            injuryUpdate = "\n\nHealthy" +  getPlayerPlayers(healthyPlayer) + ": Injury"  + getPlayerPlayers(injuryPlayer) + " = " + healthyPlayer + " : " + injuryPlayer;
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        panelSide = new javax.swing.JPanel();
        btnNonSuperstar = new javax.swing.JButton();
        btnAllCandidates = new javax.swing.JButton();
        btnSuperstar = new javax.swing.JButton();
        nbaLogo = new javax.swing.JLabel();
        panelCenter = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        btnBack = new javax.swing.JButton();
        labelReady = new javax.swing.JLabel();
        textID = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();
        panelTitle = new javax.swing.JPanel();
        title = new javax.swing.JLabel();
        icon = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        textInfo = new javax.swing.JTextArea();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(969, 760));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelSide.setBackground(new java.awt.Color(255, 51, 0));
        panelSide.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnNonSuperstar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNonSuperstarActionPerformed(evt);
            }
        });
        panelSide.add(btnNonSuperstar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 540, 70, 70));

        btnAllCandidates.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAllCandidatesActionPerformed(evt);
            }
        });
        panelSide.add(btnAllCandidates, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 70, 70));

        btnSuperstar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuperstarActionPerformed(evt);
            }
        });
        panelSide.add(btnSuperstar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 440, 70, 70));

        nbaLogo.setText("nba");
        panelSide.add(nbaLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 80, 150));

        getContentPane().add(panelSide, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 100, 820));

        panelCenter.setBackground(new java.awt.Color(0, 0, 153));
        panelCenter.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Player_ID", "Player_Name", "Age", "Height", "Weight", "Position", "Salary", "Points", "Rebounds", "Assists", "Steals", "Blocks"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
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

        panelCenter.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 380, 820, 280));

        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        panelCenter.add(btnBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 670, 100, 40));

        labelReady.setFont(new java.awt.Font("Sitka Text", 1, 18)); // NOI18N
        labelReady.setForeground(new java.awt.Color(255, 255, 255));
        labelReady.setText("Ready? Type the Player_ID:");
        panelCenter.add(labelReady, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 660, 250, 60));
        panelCenter.add(textID, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 670, 200, 40));

        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        panelCenter.add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 670, 100, 40));

        panelTitle.setBackground(new java.awt.Color(255, 255, 255));

        title.setFont(new java.awt.Font("Copperplate Gothic Bold", 1, 36)); // NOI18N
        title.setText("Add Player");

        icon.setText("jLabel1");

        javax.swing.GroupLayout panelTitleLayout = new javax.swing.GroupLayout(panelTitle);
        panelTitle.setLayout(panelTitleLayout);
        panelTitleLayout.setHorizontalGroup(
            panelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTitleLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 618, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                .addComponent(icon, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59))
        );
        panelTitleLayout.setVerticalGroup(
            panelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(icon, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTitleLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        panelCenter.add(panelTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 870, 100));

        textInfo.setEditable(false);
        textInfo.setBackground(new java.awt.Color(0, 0, 153));
        textInfo.setColumns(20);
        textInfo.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        textInfo.setForeground(new java.awt.Color(255, 255, 255));
        textInfo.setRows(5);
        jScrollPane2.setViewportView(textInfo);

        panelCenter.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 820, 240));

        getContentPane().add(panelCenter, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 0, 870, 760));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAllCandidatesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAllCandidatesActionPerformed
        displayTable("Salary >= 0", "Add Player");
        btnType.setButtonType("All_Players");
    }//GEN-LAST:event_btnAllCandidatesActionPerformed

    private void btnSuperstarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuperstarActionPerformed
        displayTable("Salary = 3000", "Add Superstar Player");
        btnType.setButtonType("Superstar_Players");
    }//GEN-LAST:event_btnSuperstarActionPerformed

    private void btnNonSuperstarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNonSuperstarActionPerformed
        displayTable("Salary = 1000", "Add Non-Superstar Player");
        btnType.setButtonType("Non_Superstar_Players");
    }//GEN-LAST:event_btnNonSuperstarActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        if (textID.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(jAddPlayer.this, "Please enter a player's name if you want to add player into your roster!!", "No Player's Name found!", JOptionPane.ERROR_MESSAGE);
            textID.grabFocus();
        } else {
            //Check player exist in CANDIDATE_LIST or not
            if (playerExist(textID.getText(), "CANDIDATE_LIST")) {

                //Check player exist in INJURY PLAYER ROSTER or not
                if (!playerExist(textID.getText(), "INJURYPLAYERROSTER")) {
                    
                    //Check if exceed salary limit
                    if (getCurrent("SUM(Salary)", "totalSalary", "") <= salaryMax) {

                        //Check if exist player limit
                        if (getCurrent("COUNT(Player_ID)", "totalPlayer", "") < playerMax) {

                            displayTable("Player_ID = '" + textID.getText() + "'", "Add Player");
                            int result = showConfirmDialog(jAddPlayer.this, "Are you sure you want to add this player into your roster?", "Add Player into Roster.", JOptionPane.YES_NO_OPTION);

                            switch (result) {
                                case JOptionPane.YES_OPTION -> {
                                    //Check whether this player duplicate
                                    if (!checkPlayerDuplicate(textID.getText())) {
                                        try {
                                            String position = null, name = null, height = null;
                                            int salary = 0, id = 0, age = 0, weight = 0;
                                            double points = 0, steals = 0, blocks = 0, assists = 0, rebounds = 0;

                                            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM CANDIDATE_LIST WHERE Player_ID = ?");
                                            pstmt.setString(1, textID.getText());

                                            ResultSet rs = pstmt.executeQuery();

                                            if (rs.next()) {
                                                id = rs.getInt("Player_ID");
                                                name = rs.getString("Player_Name");
                                                age = rs.getInt("Age");
                                                height = rs.getString("Height");
                                                weight = rs.getInt("Weight");
                                                position = rs.getString("Position");
                                                salary = rs.getInt("Salary");
                                                points = rs.getDouble("Points");
                                                steals = rs.getDouble("Steals");
                                                blocks = rs.getDouble("Blocks");
                                                assists = rs.getDouble("Assists");
                                                rebounds = rs.getDouble("Rebounds");
                                            }

                                            //Check whether current total salary + this salary exceed limit
                                            if (getCurrent("SUM(Salary)", "TotalSalary", "") + salary <= 20000) {

                                                //Check whether the player is swingman (can play 2 position)
                                                if (position.length() > 1) {
                                                    String[] twoPosition = position.split("-");

                                                    int resultSwingman = showConfirmTwoOptions(jAddPlayer.this,
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
                                                            JOptionPane.showMessageDialog(jAddPlayer.this, "Unknown Selection! Please try again!\nYou will be redirected to menu page.", "Unknown Selection!", JOptionPane.ERROR_MESSAGE);
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
                                        JOptionPane.showMessageDialog(jAddPlayer.this,
                                                "The player you selected with Player_ID: " + textID.getText() + " is existing in your roster already."
                                                + "\nPlease choose another player."
                                                + "\nYou will be redirected to choose player page again.",
                                                "Duplicate Player Found!!",
                                                JOptionPane.ERROR_MESSAGE);

                                        dispose();
                                        new jAddPlayer().setVisible(true);
                                    }
                                }
                                case JOptionPane.NO_OPTION -> {
                                    String[] chooseAgain = {"Yes", "No"};
                                    int resultChooseAgain = showConfirmTwoOptions(jAddPlayer.this,
                                            "Do you want to choose a player again?",
                                            "Choose Player Again?",
                                            JOptionPane.YES_NO_OPTION,
                                            chooseAgain);
                                    switch (resultChooseAgain) {
                                        case JOptionPane.YES_OPTION -> {
                                            dispose();
                                            new jAddPlayer().setVisible(true);
                                        }
                                        case JOptionPane.NO_OPTION -> {
                                            JOptionPane.showMessageDialog(jAddPlayer.this, "You will be redirected to menu page.", "Redirected to Menu Page", JOptionPane.PLAIN_MESSAGE);

                                            dispose();
                                            new jRosterMenu().setVisible(true);
                                        }
                                        default -> {
                                            JOptionPane.showMessageDialog(jAddPlayer.this, "Unknown Selection! Please try again!\nYou will be redirected to menu page.", "Unknown Selection!", JOptionPane.ERROR_MESSAGE);

                                            dispose();
                                            new jRosterMenu().setVisible(true);
                                        }
                                    }
                                }
                                default -> {
                                    JOptionPane.showMessageDialog(jAddPlayer.this, "Unknown Selection! Please try again!\nYou will be redirected to menu page.", "Unknown Selection!", JOptionPane.ERROR_MESSAGE);
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
                } else {
                    JOptionPane.showMessageDialog(jAddPlayer.this, "The player's id you entered: '" + textID.getText() + "' already exist in Injury Player Roster!!", "Player Found in Injury Player Roster!", JOptionPane.ERROR_MESSAGE);
                    textID.grabFocus();
                }
            } else {
                JOptionPane.showMessageDialog(jAddPlayer.this, "The player's id you entered: '" + textID.getText() + "' does not exist in Candidate List!!", "No Player Found!", JOptionPane.ERROR_MESSAGE);
                textID.grabFocus();
            }
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        dispose();
        new jRosterMenu().setVisible(true);
    }//GEN-LAST:event_btnBackActionPerformed

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a player from table!", "Unknown player selected.", JOptionPane.ERROR_MESSAGE);
        } else {
            lastPage.setLastPage("Add_Player");
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

            //Remove space in original name            
            String name = model.getValueAt(selectedRow, 1).toString().replace(" ", "");
            jap.setImageMethod("src\\icons\\Player_Profile\\" + name + ".png", jap.getPlayerProfile());

            //Store original name with space
            jap.setCurrentID(model.getValueAt(selectedRow, 0).toString());
            
            //Store the selected row index
            jap.setSelectedRow(selectedRow);
            
            //Set the Label List
            jap.getLabelList().setText("CANDIDATE LIST");
        }
    }//GEN-LAST:event_tableMouseClicked

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
            java.util.logging.Logger.getLogger(jAddPlayer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(jAddPlayer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(jAddPlayer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(jAddPlayer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new jAddPlayer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAllCandidates;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnNonSuperstar;
    private javax.swing.JButton btnSuperstar;
    private javax.swing.JLabel icon;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelReady;
    private javax.swing.JLabel nbaLogo;
    private javax.swing.JPanel panelCenter;
    private javax.swing.JPanel panelSide;
    private javax.swing.JPanel panelTitle;
    private javax.swing.JTable table;
    private javax.swing.JTextField textID;
    private javax.swing.JTextArea textInfo;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables
}