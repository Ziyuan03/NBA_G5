/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package nba;
import java.awt.Image;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.PriorityQueue;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author Ziyua
 */
public class ContractExtension extends javax.swing.JFrame {
    private BtnInjuryOrRoster btnInjuryRoster = new BtnInjuryOrRoster();
    private PriorityQueue<ContractPlayer> contractQueue;
    private static final String URL = "jdbc:mysql://localhost:3306/nba";
    private static final String USER = "root";
    private static final String PASSWORD = "root123NBA.";
    /**
     * Creates new form ContractExtension
     */
    public ContractExtension() {
        initComponents();
        contractQueue = new PriorityQueue<>();
        createTable();
        loadPlayers();
        showAllPlayers();
        setImageMethod("src\\icons\\home (2).png", btnHome);                  
        setImageMethod("src\\icons\\icons8-trophy-48.png", ranking);        
        setImageMethod("src\\icons\\icons8-basketball-48_1.png", roster);        
    }
    
    //Method set label image
    public void setImageMethod(String imagePath, JButton labelName) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage();
        Image imgScale = img.getScaledInstance(labelName.getWidth(), labelName.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(imgScale);
        labelName.setIcon(scaledIcon);                        
    }
    
    public static void createTable() {
        // SQL statement to create the table
        String createTableSQL = "CREATE TABLE IF NOT EXISTS contract ("
                + "Player_ID VARCHAR(255) NOT NULL, "
                + "Player_Name VARCHAR(255) NOT NULL, "
                + "Ranking INT NOT NULL, "
                + "Composite_Score DOUBLE NOT NULL, "
                + "Contract VARCHAR(255) NOT NULL, "
                + "PRIMARY KEY (Player_ID))";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            // Execute the SQL statement
            stmt.execute(createTableSQL);
            System.out.println("Table 'contract' created successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error creating table: " + e.getMessage());
        }
    }
    
    private void loadPlayers() {
    try {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        String query = "SELECT ar.Player_ID, ar.Player_Name, ar.Ranking, ar.Composite_Score " +
                       "FROM allplayerranking ar " +
                       "JOIN san_antonio_rankings sa ON ar.Player_ID = sa.Player_ID " +
                       "ORDER BY ar.Ranking ASC";
        PreparedStatement pst = conn.prepareStatement(query);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            String id = rs.getString("Player_ID");
            String name = rs.getString("Player_Name");
            int ranking = rs.getInt("Ranking");
            double score = rs.getDouble("Composite_Score");

            // Check if player already has a renewed contract
            if (!hasRenewedContract(conn, id)) {
                ContractPlayer cplayer = new ContractPlayer(id, name, ranking, score);
                contractQueue.offer(cplayer);
            }
        }

        rs.close();
        pst.close();
        conn.close();

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error loading player data", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
private void removePlayersFromContract(String playerId) {
    try {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        String deleteQuery = "DELETE FROM contract WHERE Player_ID = ?";
        PreparedStatement pst = conn.prepareStatement(deleteQuery);
        pst.setString(1, playerId);
        pst.executeUpdate();
        
        pst.close();
        conn.close();

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error removing player from contract database", "Error", JOptionPane.ERROR_MESSAGE);
    }
}




    
    private boolean hasRenewedContract(Connection conn, String playerId) throws SQLException {
        String query = "SELECT COUNT(*) FROM contract WHERE ID = ? AND Contract_Status = 'Renewed'";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, playerId);
            ResultSet rs = pst.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        }
        return false;
    }

    private void showAllPlayers() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Clear the table before populating it

        for (ContractPlayer player : contractQueue) {
            model.addRow(new Object[]{
                player.getId(),
                player.getName(),
                player.getRanking(),
                player.getCompositeScore(),
                "Pending"
            });
        }
    }

    private void processContract(boolean renew) {
        if (!contractQueue.isEmpty()) {
            String enteredId = id.getText();
            ContractPlayer nextPlayer = contractQueue.peek();

            if (!enteredId.equals(nextPlayer.getId())) {
                JOptionPane.showMessageDialog(this, "Error: Entered Player ID does not match the ID of the player at the front of the queue. Please enter the correct Player ID to proceed.", "Queue Rule Violation", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ContractPlayer player = contractQueue.poll();
            String contractStatus = renew ? "Renewed" : "Declined";

            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement pst = conn.prepareStatement("INSERT INTO contract (Player_ID, Player_Name, Ranking, Composite_Score, Contract) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE Contract = ?")) {
                pst.setString(1, player.getId());
                pst.setString(2, player.getName());
                pst.setInt(3, player.getRanking());
                pst.setDouble(4, player.getCompositeScore());
                pst.setString(5, contractStatus);
                pst.setString(6, contractStatus);
                pst.executeUpdate();

                if (!renew) {
                    // Remove player from san_antonio_rankings and san_antonio tables
                    try (PreparedStatement deletePst = conn.prepareStatement("DELETE FROM san_antonio_rankings WHERE Player_ID = ?")) {
                        deletePst.setString(1, player.getId());
                        deletePst.executeUpdate();
                    }
                    try (PreparedStatement deletePst = conn.prepareStatement("DELETE FROM san_antonio WHERE Player_ID = ?")) {
                        deletePst.setString(1, player.getId());
                        deletePst.executeUpdate();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating contract status", "Error", JOptionPane.ERROR_MESSAGE);
            }

            if (renew) {
                JOptionPane.showMessageDialog(this, "Renewed contract with " + player.getName());
            } else {
                JOptionPane.showMessageDialog(this, "Did not renew contract with " + player.getName());
            }
            showAllPlayers();
        }
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
        jPanel2 = new javax.swing.JPanel();
        btnHome = new javax.swing.JButton();
        ranking = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        roster = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        id = new javax.swing.JTextField();
        decline = new javax.swing.JButton();
        renew = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 153));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 51, 0));

        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
            }
        });

        ranking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rankingActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel3.setText("Ranking");

        roster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rosterActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel4.setText("Roster");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ranking, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(roster, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))))
                .addContainerGap(54, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addComponent(ranking, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(41, 41, 41)
                .addComponent(roster, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addContainerGap(343, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 150, 700));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Copperplate Gothic Bold", 0, 48)); // NOI18N
        jLabel1.setText("Contract Extension");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(152, 152, 152)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 602, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(66, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 40, 820, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Player_ID", "Player_Name", "Ranking", "Composite_Score", "Contract"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 360, 790, 300));

        jLabel2.setFont(new java.awt.Font("Arial Black", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Player ID : ");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 220, -1, -1));

        id.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idActionPerformed(evt);
            }
        });
        jPanel1.add(id, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 220, 120, 30));

        decline.setText("decline");
        decline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                declineActionPerformed(evt);
            }
        });
        jPanel1.add(decline, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 300, -1, -1));

        renew.setText("renew");
        renew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renewActionPerformed(evt);
            }
        });
        jPanel1.add(renew, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 300, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idActionPerformed

    private void renewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_renewActionPerformed
        // TODO add your handling code here:
        processContract(true);
    }//GEN-LAST:event_renewActionPerformed

    private void declineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_declineActionPerformed
        // TODO add your handling code here:
        processContract(false);
    }//GEN-LAST:event_declineActionPerformed

    private void rankingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rankingActionPerformed
        // TODO add your handling code here:
        dispose();
        new RankingSanAntonio().setVisible(true);
    }//GEN-LAST:event_rankingActionPerformed

    private void rosterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rosterActionPerformed
        btnInjuryRoster.setBtnInjuryRoster("ContractExtension");

        jViewRoster ar = new jViewRoster(btnInjuryRoster);
        ar.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_rosterActionPerformed

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        // TODO add your handling code here:
        dispose();
        new jMainMenu().setVisible(true);
    }//GEN-LAST:event_btnHomeActionPerformed

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
            java.util.logging.Logger.getLogger(ContractExtension.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ContractExtension.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ContractExtension.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ContractExtension.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ContractExtension().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHome;
    private javax.swing.JButton decline;
    private javax.swing.JTextField id;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton ranking;
    private javax.swing.JButton renew;
    private javax.swing.JButton roster;
    // End of variables declaration//GEN-END:variables
}
