package nba;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.sql.ResultSetMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.awt.Image;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author user
 */
public class jViewRoster extends javax.swing.JFrame {

    Connection con;
    DefaultTableModel model;
    String[] positionArray = {"G", "F", "C"};
    int[] positionNumber = new int[3];
    private LastPageStored lastPage = new LastPageStored();
    private PlayerTypeButton btnType = new PlayerTypeButton();
    private BtnInjuryOrRoster btnInjuryRoster;

    public jViewRoster() {
        createConnection();
        initComponents();
        updateInfo();
        displayTable(">= 0");
        setImage();
        model = (DefaultTableModel) tablePlayers.getModel();
        tablePlayers.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablePlayers.getColumnModel().getColumn(1).setPreferredWidth(190);
        tablePlayers.getColumnModel().getColumn(11).setPreferredWidth(90);
        tablePlayers.getColumnModel().getColumn(12).setPreferredWidth(70);
    }

    public jViewRoster(BtnInjuryOrRoster btnInjuryRoster) {
        this.btnInjuryRoster = btnInjuryRoster;
        createConnection();
        initComponents();
        updateInfo();
        displayTable(">= 0");
        setImage();
        model = (DefaultTableModel) tablePlayers.getModel();
        tablePlayers.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablePlayers.getColumnModel().getColumn(1).setPreferredWidth(190);
        tablePlayers.getColumnModel().getColumn(11).setPreferredWidth(90);
        tablePlayers.getColumnModel().getColumn(12).setPreferredWidth(70);
    }

    private void setImage() {
        setLabelImageMethod("src\\icons\\nbaLogo.png", nbaLogo);
        setLabelImageMethod("src\\icons\\player.png", iconPlayer);
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
    public void displayTable(String salary) {
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM SAN_ANTONIO WHERE Salary " + salary + " ORDER BY Player_ID ASC");
            ResultSet rs = pstmt.executeQuery();

            ResultSetMetaData stData = rs.getMetaData();
            int q = stData.getColumnCount();

            DefaultTableModel recordTable = (DefaultTableModel) tablePlayers.getModel();
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
                columnData.add(rs.getString("Blocks"));
                columnData.add(rs.getString("Injury_Status"));

                recordTable.addRow(columnData);
            }

        } catch (SQLException ex) {
            Logger.getLogger(jViewRoster.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Check if player exist in Injury Player Roster
    public boolean checkPlayerInjury(String playerID) {
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM INJURYPLAYERROSTER WHERE PLAYER_ID = ?");
            pstmt.setString(1, playerID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(jViewRoster.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    //Check if no player SAN_ANTONIO
    public boolean checkNoPlayer() {
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM SAN_ANTONIO");
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(jViewRoster.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    //Multi-use method for all players, superstar and non-superstar
    public void displayPlayer(String salary, String text) {
        if (checkNoPlayer()) {
            JOptionPane.showMessageDialog(jViewRoster.this, "There is no player in your roster. Please add a player!");
        } else {
            titleViewRoster.setText(text);
            displayTable(salary);
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

                ResultSet rs = stmt.executeQuery("SELECT " + function + " AS " + functionName + " FROM SAN_ANTONIO " + condition);

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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        panelSide = new javax.swing.JPanel();
        btnNonSuperstar = new javax.swing.JButton();
        btnSuperstar = new javax.swing.JButton();
        btnAllPlayers = new javax.swing.JButton();
        nbaLogo = new javax.swing.JLabel();
        panelCenter = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        titleViewRoster = new javax.swing.JLabel();
        iconPlayer = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        textInfo = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablePlayers = new javax.swing.JTable();
        btnBack = new javax.swing.JButton();
        btnPdf = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(969, 760));
        setPreferredSize(new java.awt.Dimension(969, 760));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelSide.setBackground(new java.awt.Color(255, 51, 0));

        btnNonSuperstar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNonSuperstarActionPerformed(evt);
            }
        });

        btnSuperstar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuperstarActionPerformed(evt);
            }
        });

        btnAllPlayers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAllPlayersActionPerformed(evt);
            }
        });

        nbaLogo.setText("nba");

        javax.swing.GroupLayout panelSideLayout = new javax.swing.GroupLayout(panelSide);
        panelSide.setLayout(panelSideLayout);
        panelSideLayout.setHorizontalGroup(
            panelSideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSideLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(panelSideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nbaLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelSideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnNonSuperstar, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnSuperstar, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnAllPlayers, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelSideLayout.setVerticalGroup(
            panelSideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSideLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(nbaLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53)
                .addComponent(btnAllPlayers, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(btnSuperstar, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(btnNonSuperstar, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(155, 155, 155))
        );

        jPanel1.add(panelSide, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 760));

        panelCenter.setBackground(new java.awt.Color(0, 0, 153));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        titleViewRoster.setFont(new java.awt.Font("Copperplate Gothic Bold", 1, 36)); // NOI18N
        titleViewRoster.setText("View Roster");

        iconPlayer.setText("Player");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(titleViewRoster, javax.swing.GroupLayout.PREFERRED_SIZE, 631, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                .addComponent(iconPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(iconPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(titleViewRoster, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        textInfo.setEditable(false);
        textInfo.setBackground(new java.awt.Color(0, 0, 153));
        textInfo.setColumns(20);
        textInfo.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        textInfo.setForeground(new java.awt.Color(255, 255, 255));
        textInfo.setRows(5);
        jScrollPane2.setViewportView(textInfo);

        tablePlayers.setModel(new javax.swing.table.DefaultTableModel(
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
        tablePlayers.getTableHeader().setReorderingAllowed(false);
        tablePlayers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablePlayersMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablePlayers);

        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        btnPdf.setText("Get PDF");
        btnPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPdfActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelCenterLayout = new javax.swing.GroupLayout(panelCenter);
        panelCenter.setLayout(panelCenterLayout);
        panelCenterLayout.setHorizontalGroup(
            panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCenterLayout.createSequentialGroup()
                .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelCenterLayout.createSequentialGroup()
                        .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelCenterLayout.createSequentialGroup()
                                .addGap(321, 321, 321)
                                .addComponent(btnPdf, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelCenterLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 820, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 820, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelCenterLayout.setVerticalGroup(
            panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCenterLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPdf, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(61, Short.MAX_VALUE))
        );

        jPanel1.add(panelCenter, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 0, 850, 760));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 970, 760));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnNonSuperstarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNonSuperstarActionPerformed
        displayPlayer("= 1000", "Non-Superstar Players");
        btnType.setButtonType("Non_Superstar_Players");
    }//GEN-LAST:event_btnNonSuperstarActionPerformed

    //Display all players
    private void btnAllPlayersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAllPlayersActionPerformed
        displayPlayer(">= 0", "All Players");
        btnType.setButtonType("All_Players");
    }//GEN-LAST:event_btnAllPlayersActionPerformed

    private void btnSuperstarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuperstarActionPerformed
        displayPlayer("= 3000", "Superstar Players");
        btnType.setButtonType("Superstar_Players");
    }//GEN-LAST:event_btnSuperstarActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        dispose();

        String btn = btnInjuryRoster.getBtnInjuryRoster();

        if (btn == null) {
            btnInjuryRoster.setBtnInjuryRoster("View");
        }

        switch (btn) {
            case "View" ->
                new jRosterMenu().setVisible(true);
            case "Injury_View" ->
                new InjuryPlayerView().setVisible(true);
            case "Injury_Add" ->
                new InjuryPlayerAdd().setVisible(true);
            case "Injury_Delete" ->
                new InjuryPlayerDelete().setVisible(true);
            case "ContractExtension" ->
                new ContractExtension().setVisible(true);
            default ->
                new jRosterMenu().setVisible(true);
        }
    }//GEN-LAST:event_btnBackActionPerformed

    private void tablePlayersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablePlayersMouseClicked
        int selectedRow = tablePlayers.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a player from table!", "Unknown player selected.", JOptionPane.ERROR_MESSAGE);
        } else {
            lastPage.setLastPage("View_Roster");

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

            //Remove space in original name            
            String name = model.getValueAt(selectedRow, 1).toString().replace(" ", "");

            //Set Player Image 
            jap.setImageMethod("src\\icons\\Player_Profile\\" + name + ".png", jap.getPlayerProfile());

            //Store original name with space
            jap.setCurrentID(model.getValueAt(selectedRow, 0).toString());

            //Set the Label List
            jap.getLabelList().setText("SAN ANTONIO");
        }
    }//GEN-LAST:event_tablePlayersMouseClicked

    private void btnPdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPdfActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            if (!filePath.endsWith(".pdf")) {
                filePath += ".pdf";
            }

            Document document = new Document();

            try {
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();
                
                PdfPTable pdfTable = new PdfPTable(tablePlayers.getColumnCount());
                
                //Add column headers
                for(int i=0; i<tablePlayers.getColumnCount(); i++) {
                    pdfTable.addCell(tablePlayers.getColumnName(i));                    
                }
                
                //Add rows data
                for(int i=0; i<tablePlayers.getRowCount(); i++) {
                    for(int j=0; j<tablePlayers.getColumnCount(); j++) {
                        pdfTable.addCell(tablePlayers.getModel().getValueAt(i, j).toString());
                    }
                }
                
                document.add(new Paragraph("Data Exported from SAN ANTONIO"));
                document.add(new Paragraph(" "));
                document.add(pdfTable);
                document.close();
                
                JOptionPane.showMessageDialog(this, "PDF was created successfully at " + filePath);
                
            } catch (FileNotFoundException | DocumentException ex) {
                Logger.getLogger(jViewRoster.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnPdfActionPerformed

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
            java.util.logging.Logger.getLogger(jViewRoster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(jViewRoster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(jViewRoster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(jViewRoster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
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
                new jViewRoster().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAllPlayers;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnNonSuperstar;
    private javax.swing.JButton btnPdf;
    private javax.swing.JButton btnSuperstar;
    private javax.swing.JLabel iconPlayer;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel nbaLogo;
    private javax.swing.JPanel panelCenter;
    private javax.swing.JPanel panelSide;
    private javax.swing.JTable tablePlayers;
    private javax.swing.JTextArea textInfo;
    private javax.swing.JLabel titleViewRoster;
    // End of variables declaration//GEN-END:variables
}
