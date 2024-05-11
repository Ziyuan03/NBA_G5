/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package nba_gmanager;


import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Stack;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author ziyuan
 */
public class InjuryPlayer extends javax.swing.JFrame {
    private static final String username = "root";
    private static final String password = "Ziyuan03";
    private static final String dataCon = "jdbc:mysql://localhost:3306/injuryplayer?useSSL=false&serverTimezone=Asia/Kuala_Lumpur";

    /**
     * Creates new form InjuryPlayer
     */
    public InjuryPlayer() {
        initComponents();
    }
    
    public class ImageFrame extends JFrame {
        public ImageFrame() {  
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(963, 897);
            setLocationRelativeTo(null); // Center the frame
           
            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/Downloads/InjuryPlayerImage.jpg"));
            
            JLabel label = new JLabel(imageIcon);
           
            getContentPane().add(label);
          
            setVisible(true);
        }
    }
        
    Connection Con = null;
    PreparedStatement pst = null;
    ResultSet Rs = null;
    int q, i, id,deleteItem;
    
    public class Player {

        private String id;
        private String name;
        private String injuryStatus;
        private Date injuryDate;

        public Player(String id, String name, String injuryStatus, Date injuryDate) {
            this.id = id;
            this.name = name;
            this.injuryStatus = injuryStatus;
            this.injuryDate = injuryDate;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getInjuryStatus() {
            return injuryStatus;
        }

        public Date getInjuryDate() {
            return injuryDate;
        }

        public void setInjuryStatus(String status) {
            this.injuryStatus = status;
        }
    }
    Stack<Player> injuryReserve = new Stack<>();
    Stack<Player> activeRoster = new Stack<>();
    
    
    public void upDateDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Con = DriverManager.getConnection(dataCon,username, password);
            pst = Con.prepareStatement("select * from injuryplayer.new_table ORDER BY insertion_time ASC ");
            Rs = pst.executeQuery();
            ResultSetMetaData stData = Rs.getMetaData();
            q = stData.getColumnCount();
            DefaultTableModel RecordTable = (DefaultTableModel)jTable1.getModel();
            RecordTable.setRowCount(0);
            int index = 1;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            while(Rs.next()){
                Vector<Object> columnData = new Vector<>(q);
                
                columnData.add(index); 
                columnData.add(Rs.getString("ID"));
                columnData.add(Rs.getString("Name"));
                columnData.add(Rs.getString("Injury Status"));
                columnData.add(Rs.getString("Injury Date"));
                columnData.add(Rs.getTimestamp("insertion_time"));
                Timestamp timestamp = Rs.getTimestamp("insertion_time");
                String formattedTimestamp = dateFormat.format(timestamp);
                columnData.add(formattedTimestamp);
                RecordTable.addRow(columnData);
                index++;

            }
            jTable1.repaint();
            jTable1.revalidate();
        
            Rs.close();
            pst.close();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,ex);
        }
    }
    
    private Stack<Vector<Object>> removedPlayersStack = new Stack<>();

    private void UpdatePlayerInStack(Player updatedPlayer) {
        for (int i = 0; i < injuryReserve.size(); i++) {
            Player player = injuryReserve.get(i);
            if (player.getId().equals(updatedPlayer.getId())) {
                injuryReserve.set(i, updatedPlayer); // Replace the old player with the updated player
                break;
            }
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

        HOME = new javax.swing.JButton();
        ADD = new javax.swing.JButton();
        DELETE = new javax.swing.JButton();
        UPDATE = new javax.swing.JButton();
        CLEAR = new javax.swing.JButton();
        ID = new javax.swing.JTextField();
        InjuryStatus = new javax.swing.JTextField();
        InjuryDate = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        Name = new javax.swing.JTextField();
        JLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        HOME.setBackground(new java.awt.Color(255, 153, 153));
        HOME.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        HOME.setForeground(new java.awt.Color(0, 0, 0));
        HOME.setText("HOME");
        HOME.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HOMEActionPerformed(evt);
            }
        });
        getContentPane().add(HOME, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 160, 100, 30));

        ADD.setBackground(new java.awt.Color(255, 153, 153));
        ADD.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        ADD.setForeground(new java.awt.Color(0, 0, 0));
        ADD.setText("ADD");
        ADD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ADDActionPerformed(evt);
            }
        });
        getContentPane().add(ADD, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 410, 90, 30));

        DELETE.setBackground(new java.awt.Color(255, 153, 153));
        DELETE.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        DELETE.setForeground(new java.awt.Color(0, 0, 0));
        DELETE.setText("DELETE");
        DELETE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DELETEActionPerformed(evt);
            }
        });
        getContentPane().add(DELETE, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 410, 100, 30));

        UPDATE.setBackground(new java.awt.Color(255, 153, 153));
        UPDATE.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        UPDATE.setForeground(new java.awt.Color(0, 0, 0));
        UPDATE.setText("UPDATE");
        UPDATE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UPDATEActionPerformed(evt);
            }
        });
        getContentPane().add(UPDATE, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 410, 100, 30));

        CLEAR.setBackground(new java.awt.Color(255, 153, 153));
        CLEAR.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        CLEAR.setForeground(new java.awt.Color(0, 0, 0));
        CLEAR.setText("CLEAR");
        CLEAR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CLEARActionPerformed(evt);
            }
        });
        getContentPane().add(CLEAR, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 410, 100, 30));

        ID.setBackground(new java.awt.Color(255, 255, 255));
        ID.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        ID.setBorder(null);
        ID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IDActionPerformed(evt);
            }
        });
        getContentPane().add(ID, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 240, 150, 30));

        InjuryStatus.setBackground(new java.awt.Color(255, 255, 255));
        InjuryStatus.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        InjuryStatus.setForeground(new java.awt.Color(0, 0, 0));
        InjuryStatus.setBorder(null);
        getContentPane().add(InjuryStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 240, 150, 30));

        InjuryDate.setBackground(new java.awt.Color(255, 255, 255));
        InjuryDate.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        InjuryDate.setForeground(new java.awt.Color(0, 0, 0));
        InjuryDate.setBorder(null);
        InjuryDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InjuryDateActionPerformed(evt);
            }
        });
        getContentPane().add(InjuryDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 320, 150, 30));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "NO", "ID", "Name", "Injury Status", "Injury Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
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
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(4).setResizable(false);
        }

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 490, 950, 310));

        Name.setBackground(new java.awt.Color(255, 255, 255));
        Name.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        Name.setBorder(null);
        getContentPane().add(Name, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 320, 150, 30));

        JLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Untitled (3).png"))); // NOI18N
        JLabel1.setText("jLabel1");
        getContentPane().add(JLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 970, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void IDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IDActionPerformed

    private void InjuryDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InjuryDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_InjuryDateActionPerformed

    private void ADDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ADDActionPerformed
        // TODO add your handling code here:
        String injuryDateString = "2024-05-01";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            String id = ID.getText();
            String name = Name.getText();
            String injuryStatus = InjuryStatus.getText();
            java.util.Date parsed = format.parse(InjuryDate.getText());
            java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
            Player newPlayer = new Player(id, name, injuryStatus, sqlDate);
            injuryReserve.push(newPlayer);

            Class.forName("com.mysql.cj.jdbc.Driver");
            Con = DriverManager.getConnection(dataCon, username, password);
            pst = Con.prepareStatement("SELECT COUNT(*) FROM injuryplayer.new_table WHERE ID = ?");
            pst.setString(1, ID.getText());
            ResultSet rs = pst.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            if (count > 0) {
                JOptionPane.showMessageDialog(this, "ID already exists. Please use a different ID.");
            } else {
                pst = Con.prepareStatement("INSERT INTO injuryplayer.new_table (ID, Name, `Injury Status`, `Injury Date`) VALUES (?, ?, ?, ?)");
                pst.setString(1, ID.getText());
                pst.setString(2, Name.getText());
                pst.setString(3, InjuryStatus.getText());
                pst.setDate(4, sqlDate);

                pst.executeUpdate();
                pst.close();
                JOptionPane.showMessageDialog(this, "Injury Player Added");
            }

            upDateDB();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_ADDActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        DefaultTableModel RecordTable = (DefaultTableModel)jTable1.getModel();
        int SelectedRows = jTable1.getSelectedRow();

        ID.setText(RecordTable.getValueAt(SelectedRows,1).toString());
        Name.setText(RecordTable.getValueAt(SelectedRows,2).toString());
        InjuryStatus.setText(RecordTable.getValueAt(SelectedRows,3).toString());
        InjuryDate.setText(RecordTable.getValueAt(SelectedRows,4).toString());
    }//GEN-LAST:event_jTable1MouseClicked

    private void DELETEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DELETEActionPerformed
        // TODO add your handling code here:
        if (!injuryReserve.isEmpty()) {
            // Peek at the top player on the stack
            Player topPlayer = injuryReserve.peek();

            // Check if the selected player matches the top player on the stack
            String selectedID = ID.getText(); // assuming ID is the identifier of the selected row
            if (!topPlayer.getId().equals(selectedID)) {
                JOptionPane.showMessageDialog(this, "You can only delete the most recently added player from the injury reserve.");
                return;
            }

            // Prompt user for confirmation
            int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this player?", "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response != JOptionPane.YES_OPTION) {
                // User did not confirm, exit the method
                return;
            }

            // Pop the top player from the injury reserve stack
            Player clearedPlayer = injuryReserve.pop();

            try {
                // Delete the player from the database
                Class.forName("com.mysql.cj.jdbc.Driver");
                Con = DriverManager.getConnection(dataCon, username, password);
                pst = Con.prepareStatement("DELETE FROM injuryplayer.new_table WHERE ID = ?");
                pst.setString(1, clearedPlayer.getId());
                pst.executeUpdate();

                // Optionally, add the player to the active roster stack
                activeRoster.push(clearedPlayer);

                // Provide feedback to the user
                JOptionPane.showMessageDialog(this, clearedPlayer.getName() + " has been cleared to play and added to the active roster.");

                // Update the table
                upDateDB();

                pst.close();
                Con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "No players are currently in the injury reserve.");
        }
    }//GEN-LAST:event_DELETEActionPerformed

    private void UPDATEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UPDATEActionPerformed
        // TODO add your handling code here:
        if (ID.getText().isEmpty() || Name.getText().isEmpty() || InjuryStatus.getText().isEmpty() || InjuryDate.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Missing Information");
        } else {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Con = DriverManager.getConnection(dataCon, username, password);

                String query = "UPDATE injuryplayer.new_table SET Name = ?, `Injury Status` = ?, `Injury Date` = ? WHERE ID = ?";
                pst = Con.prepareStatement(query);

                String name = Name.getText();
                String injuryStatus = InjuryStatus.getText();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date parsed = format.parse(InjuryDate.getText());
                java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
                String id = ID.getText();

                pst.setString(1, name);
                pst.setString(2, injuryStatus);
                pst.setDate(3, sqlDate);
                pst.setString(4, id);

                pst.executeUpdate();

                Player updatedPlayer = new Player(id, name, injuryStatus, sqlDate);
                UpdatePlayerInStack(updatedPlayer);

                JOptionPane.showMessageDialog(this, "Information Updated");
                upDateDB();

                pst.close();
                Con.close();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }//GEN-LAST:event_UPDATEActionPerformed

    private void CLEARActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CLEARActionPerformed
        // TODO add your handling code here:
        ID.setText("");
        Name.setText("");
        InjuryStatus.setText("");
        InjuryDate.setText("");
    }//GEN-LAST:event_CLEARActionPerformed

    private void HOMEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HOMEActionPerformed
        // TODO add your handling code here:
        MainPage mainpage = new MainPage();
        mainpage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_HOMEActionPerformed

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
            java.util.logging.Logger.getLogger(InjuryPlayer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InjuryPlayer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InjuryPlayer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InjuryPlayer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InjuryPlayer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ADD;
    private javax.swing.JButton CLEAR;
    private javax.swing.JButton DELETE;
    private javax.swing.JButton HOME;
    private javax.swing.JTextField ID;
    private javax.swing.JTextField InjuryDate;
    private javax.swing.JTextField InjuryStatus;
    private javax.swing.JLabel JLabel1;
    private javax.swing.JTextField Name;
    private javax.swing.JButton UPDATE;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
