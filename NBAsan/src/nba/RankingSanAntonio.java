/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package nba;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author Ziyua
 */
public class RankingSanAntonio extends javax.swing.JFrame {
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root123NBA.";
    private static final String DATA_CON = "jdbc:mysql://localhost:3306/nba";
    
    /**
     * Creates new form RankingAllPlayer
     */
    public RankingSanAntonio() {
        initComponents();
        createRankingTable();
        calculateAndInsertRankings();
        loadRankingsIntoTable();
        
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(190);
                
        setImageMethod("src\\icons\\800px-Back-Arrowsvg-1.png", btnBack);    
        setImageMethod("src\\icons\\8665833_rotate_refresh_icon (1).png", btnRefresh);        
    }
    
    //Method set label image
    public void setImageMethod(String imagePath, JButton labelName) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage();
        Image imgScale = img.getScaledInstance(labelName.getWidth(), labelName.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(imgScale);
        labelName.setIcon(scaledIcon);        
    }
    
    public void createRankingTable() {
        String createTableSQL = """
        CREATE TABLE IF NOT EXISTS san_antonio_rankings (
            Player_ID INT,
            Player_Name VARCHAR(255),
            Ranking INT,
            Composite_Score DECIMAL(10,2),
            Position VARCHAR(50),
            Points DECIMAL(10,2),
            Assists DECIMAL(10,2),
            Steals DECIMAL(10,2),
            Rebounds DECIMAL(10,2),
            Blocks DECIMAL(10,2),
            PRIMARY KEY (Player_ID)
        )
        """;
        try (Connection con = DriverManager.getConnection(DATA_CON, USERNAME, PASSWORD); Statement stmt = con.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Table checked/created successfully");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database initialization error: " + ex.getMessage());
        }
    }
    
    public void calculateAndInsertRankings() {
        // SQL query to fetch data from the san_antonio table
        String fetchSql = "SELECT Player_ID, Player_Name, Position, Points, Assists, Steals, Rebounds, Blocks "
                + "FROM san_antonio";
        List<Player> players = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DATA_CON, USERNAME, PASSWORD);
                Statement fetchStmt = conn.createStatement();
                ResultSet rs = fetchStmt.executeQuery(fetchSql)) {

            while (rs.next()) {
                // Create Player objects from fetched data
                Player player = new Player(
                        rs.getInt("Player_ID"),
                        rs.getString("Player_Name"),
                        rs.getString("Position"),
                        rs.getDouble("Points"),
                        rs.getDouble("Assists"),
                        rs.getDouble("Steals"),
                        rs.getDouble("Rebounds"),
                        rs.getDouble("Blocks")
                );
                player.setCompositeScore(calculateCompositeScore(player.getPosition(), player.getPoints(), player.getAssists(), player.getSteals(), player.getRebounds(), player.getBlocks()));
                players.add(player);
            }

            // Sort players by composite score in descending order
            players.sort((p1, p2) -> Double.compare(p2.getCompositeScore(), p1.getCompositeScore()));

            // Prepare SQL for inserting or updating rankings
            String insertSql = "INSERT INTO san_antonio_rankings (Player_ID, Player_Name, Ranking, Composite_Score, Position, Points, Assists, Steals, Rebounds, Blocks) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                    + "ON DUPLICATE KEY UPDATE Player_Name = VALUES(Player_Name), Ranking = VALUES(Ranking), Composite_Score = VALUES(Composite_Score), Position = VALUES(Position), Points = VALUES(Points), Assists = VALUES(Assists), Steals = VALUES(Steals), Rebounds = VALUES(Rebounds), Blocks = VALUES(Blocks)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);

            int rank = 1;
            for (Player player : players) {
                insertStmt.setInt(1, player.getPlayerID());
                insertStmt.setString(2, player.getPlayerName());
                insertStmt.setInt(3, rank++);
                insertStmt.setDouble(4, player.getCompositeScore());
                insertStmt.setString(5, player.getPosition());
                insertStmt.setDouble(6, player.getPoints());
                insertStmt.setDouble(7, player.getAssists());
                insertStmt.setDouble(8, player.getSteals());
                insertStmt.setDouble(9, player.getRebounds());
                insertStmt.setDouble(10, player.getBlocks());
                insertStmt.executeUpdate();
            }
            System.out.println("Rankings calculated and inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error during fetching or processing: " + e.getMessage());
        }
    }




    
    private double calculateCompositeScore(String position, double points, double assists, double steals, double rebounds, double blocks) {
        double score = 0.0;
        switch (position) {
            case "G": // Guard
                score = points * 0.30 + assists * 0.30 + steals * 0.20 + rebounds * 0.10 + blocks * 0.10;
                break;
            case "C": // Center
                score = points * 0.20 + assists * 0.10 + steals * 0.10 + rebounds * 0.30 + blocks * 0.30;
                break;
            case "F": // Forward
                score = points * 0.25 + assists * 0.20 + steals * 0.15 + rebounds * 0.25 + blocks * 0.15;
                break;
            default: // Default case if position is not specified
                score = points * 0.20 + assists * 0.20 + steals * 0.20 + rebounds * 0.20 + blocks * 0.20;
                break;
        }
        return score;
    }
    
    private void loadRankingsIntoTable() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);  // Clear existing data

        String query = "SELECT * FROM san_antonio_rankings ORDER BY Ranking";
        try (Connection conn = DriverManager.getConnection(DATA_CON, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Object[] row = new Object[12];
                row[0] = rs.getInt("Player_ID");
                row[1] = rs.getString("Player_Name");
                row[2] = rs.getInt("Ranking");
                row[3] = rs.getDouble("Composite_Score");
                row[4] = rs.getString("Position");
                row[5] = rs.getDouble("Points");
                row[6] = rs.getDouble("Assists");
                row[7] = rs.getDouble("Steals");
                row[8] = rs.getDouble("Rebounds");
                row[9] = rs.getDouble("Blocks");
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading rankings from database", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void refreshData(){
        try (Connection conn = DriverManager.getConnection(DATA_CON, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement()) {

            // Clear the existing data in the rankings table
            stmt.executeUpdate("DELETE FROM san_antonio_rankings");

            // Recalculate and insert the latest rankings
            calculateAndInsertRankings();

            // Load the latest rankings into the table
            loadRankingsIntoTable();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error refreshing data", "Database Error", JOptionPane.ERROR_MESSAGE);
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
        btnBack = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnRefresh = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 153));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 51, 0));

        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(618, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 100, 700));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Copperplate Gothic Bold", 1, 48)); // NOI18N
        jLabel1.setText("Ranking San_Antonio");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(120, 120, 120)
                .addComponent(jLabel1)
                .addContainerGap(138, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, 870, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Player_ID", "Player_Name", "Ranking", "Composite_Score", "Position", "Points", "Assists", "Steals", "Rebounds", "Blocks"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 200, 850, 440));

        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        jPanel1.add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 650, 50, 40));

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

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseClicked

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        dispose();
        new Ranking().setVisible(true);
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
        refreshData();
    }//GEN-LAST:event_btnRefreshActionPerformed

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
            java.util.logging.Logger.getLogger(RankingSanAntonio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RankingSanAntonio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RankingSanAntonio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RankingSanAntonio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RankingSanAntonio().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
