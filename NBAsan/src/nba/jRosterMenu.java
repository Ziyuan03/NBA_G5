/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package nba;


import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author user
 */
public class jRosterMenu extends javax.swing.JFrame {    
    Connection con;
    int playerMin = 10;
    private btnPressedStored btnStored = new btnPressedStored();
    private BtnInjuryOrRoster btnInjuryRoster = new BtnInjuryOrRoster();
    
    public jRosterMenu() {        
        setSize(969, 678);
        initComponents();
        setImage();
        createConnection();
    }
    
    private void setImage() {
        setImageMethod("src\\icons\\nbaLogo.png", nbaLogo);
        setImageMethod("src\\icons\\basketball.png", labelBasketball);
        setImageMethod("src\\icons\\playerList.png", viewPlayerIcon);
        setImageMethod("src\\icons\\addPlayer.png", addPlayerIcon);
        setImageMethod("src\\icons\\removePlayer.png", removePlayerIcon);     
        setImageMethod("src\\icons\\basketballTournament.png", deco);
    }
    
    public void setImageMethod(String imagePath, JLabel labelName) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage();
        Image imgScale = img.getScaledInstance(labelName.getWidth(), labelName.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(imgScale);
        labelName.setIcon(scaledIcon);
    }                      
    
    public void setTextArea(String textArea, String textLabel) {    
        jRosterMenuProceed jrmp = new jRosterMenuProceed(btnStored, btnInjuryRoster);
        jrmp.setVisible(true);

        if (jrmp.getInstruction() != null && jrmp.getProceedTitle() != null && jrmp.getIcon() != null) {
            jrmp.getInstruction().setText(textArea);
            jrmp.getProceedTitle().setText(textLabel);
            setImageMethod("src\\icons\\instruction.png", jrmp.getIcon());
            dispose();
        } else {
            System.err.println("Error: One or more components are not initialized in jRosterMenuProceed.");
        }
    }
    
    //Create connection with database when initiate this program everytime 
        public void createConnection() {
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
                if(con == null) {
                    current = 0;
                } else {
                    Statement stmt = con.createStatement();
                
                    ResultSet rs = stmt.executeQuery("SELECT " + function + " AS '" + functionName + "' FROM SAN_ANTONIO");
                    
                    if(rs.next()) {
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
        
        //Set JOptionPane position and swingman selection
    public int showConfirmTwoOptions(Component parentComponent, Object message, String title, int optionType, Object[] options) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.QUESTION_MESSAGE, optionType, null, options, options[0]);
        JDialog dialog  = optionPane.createDialog(parentComponent, title);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        dialog.setLocation(400, 470);
        dialog.setVisible(true);
        
        Object selectedOption = optionPane.getValue();
        if(selectedOption == null) {
            return JOptionPane.CLOSED_OPTION;
        }
        
        for(int i=0; i<options.length; i++) {
            if(options[i].equals(selectedOption)) {
                return i;
            }
        }
        
        return JOptionPane.CLOSED_OPTION;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        btnViewPlayer = new javax.swing.JButton();
        labelViewPlayer = new javax.swing.JLabel();
        viewPlayerIcon = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnAddPlayer = new javax.swing.JButton();
        labelAddPlayer = new javax.swing.JLabel();
        addPlayerIcon = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        btnRemovePlayer = new javax.swing.JButton();
        labelRemovePlayer = new javax.swing.JLabel();
        removePlayerIcon = new javax.swing.JLabel();
        deco = new javax.swing.JLabel();
        panelTitle = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        labelBasketball = new javax.swing.JLabel();
        btnMainMenu = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        nbaLogo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 0, 153));

        btnViewPlayer.setText("Proceed");
        btnViewPlayer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewPlayerActionPerformed(evt);
            }
        });

        labelViewPlayer.setFont(new java.awt.Font("Kristen ITC", 1, 18)); // NOI18N
        labelViewPlayer.setText("View Roster");

        viewPlayerIcon.setText("View player icon");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(138, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnViewPlayer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(viewPlayerIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(labelViewPlayer)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(viewPlayerIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelViewPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnViewPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );

        btnAddPlayer.setText("Proceed");
        btnAddPlayer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddPlayerActionPerformed(evt);
            }
        });

        labelAddPlayer.setFont(new java.awt.Font("Kristen ITC", 1, 18)); // NOI18N
        labelAddPlayer.setText("Add Player");

        addPlayerIcon.setText("Add player icon");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(73, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(labelAddPlayer)
                        .addGap(64, 64, 64))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(addPlayerIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAddPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(addPlayerIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelAddPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnAddPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        btnRemovePlayer.setText("Proceed");
        btnRemovePlayer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemovePlayerActionPerformed(evt);
            }
        });

        labelRemovePlayer.setFont(new java.awt.Font("Kristen ITC", 1, 18)); // NOI18N
        labelRemovePlayer.setText("Remove Player");

        removePlayerIcon.setText("Remove player icon");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(58, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnRemovePlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(removePlayerIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(labelRemovePlayer)
                        .addGap(46, 46, 46))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(removePlayerIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelRemovePlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnRemovePlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        deco.setText("jLabel2");

        panelTitle.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("Copperplate Gothic Bold", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Roster Menu");

        labelBasketball.setText("basketball");

        javax.swing.GroupLayout panelTitleLayout = new javax.swing.GroupLayout(panelTitle);
        panelTitle.setLayout(panelTitleLayout);
        panelTitleLayout.setHorizontalGroup(
            panelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTitleLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelBasketball, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
        );
        panelTitleLayout.setVerticalGroup(
            panelTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTitleLayout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
            .addGroup(panelTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelBasketball, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnMainMenu.setText("Back to Main Menu");
        btnMainMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMainMenuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(deco, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(127, 127, 127)
                        .addComponent(btnMainMenu))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(72, 72, 72))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(panelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(deco, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(15, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnMainMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48))))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(101, 0, 910, 710));

        jPanel6.setBackground(new java.awt.Color(255, 51, 0));

        nbaLogo.setText("nba");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nbaLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(nbaLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(525, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 710));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnViewPlayerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewPlayerActionPerformed
        btnStored.setBtnPressed("View");        
        btnInjuryRoster.setBtnInjuryRoster("View");
                
        setTextArea("""
                    \t     Welcome to view players in your roster!!!
                    
                           Please read the below instructions carefully......
                    
                           1. You can view all players in your roster.
                    
                           2. You can view Superstar players only in your roster.
                    
                           3. You can view Non-SUPERSTAR players only in your roster.
                    
                           4. You can get the pdf of your players in the roster.
                    
                           5. Try to click the table row to view the player profile!!
                    
                           6. Press the button on the left side to view respective group of players.
                    """, "Instructions Before View Players");
    }//GEN-LAST:event_btnViewPlayerActionPerformed

    private void btnAddPlayerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddPlayerActionPerformed
        if(getCurrent("SUM(Salary)", "totalSalary") <  19999) {
            
            if(getCurrent("COUNT(Player_ID)", "totalPlayer") < 15) {
                btnStored.setBtnPressed("Add");
                setTextArea("""
                            \t     Welcome to choose your roster member!!!

                                   Please read the below instructions carefully......

                                  1. Your roster must consist a minimum of 10 players 
                                      and a maximum of 15 players.

                                  2. Your roster must have at least 2 Guard, Forward, Centre.

                                  3. Superstar player' salary is 3000 while 
                                      non-Superstar player's salary is 1000.

                                  4. Your roster total salary should not exceed 20000.

                                  5. Build your roster wisely based on these conditions.

                                  6. You are encouraged to view the candidate list before you select player.
                            
                                  7. Try to click the table row to view the player profile!!
                            
                                  8. Press the button on the left side to view respective group of players.
                            """, "Instructions Before Add Players");
                
            } else {
                JOptionPane.showMessageDialog(jRosterMenu.this, "You should not add player from into roster as you have " + getCurrent("COUNT(Player_ID)", "TotalPlayer") + " players now. \nYou should have a maximum of 15 players in your roster.", "Redirected to Menu Page", JOptionPane.WARNING_MESSAGE);
            }            
        } else {
            JOptionPane.showMessageDialog(jRosterMenu.this, "You should not add player into your roster as your roster total salary is " + getCurrent("COUNT(Player_ID)", "TotalPlayer") + " which exceeds 20000. \nYou should remove player from your roster if you want to add player.", "Redirected to Menu Page", JOptionPane.WARNING_MESSAGE);
        }                
    }//GEN-LAST:event_btnAddPlayerActionPerformed

    private void btnRemovePlayerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemovePlayerActionPerformed
        if(getCurrent("COUNT(Player_ID)", "TotalPlayer") <= playerMin) {                                                                        
                   JOptionPane.showMessageDialog(jRosterMenu.this, "You should not remove player from your roster as you only left " + getCurrent("COUNT(Player_ID)", "TotalPlayer") + " players now. \nYou should have a minimum of 10 players in your roster.", "Redirected to Menu Page", JOptionPane.WARNING_MESSAGE);
        } else {
            btnStored.setBtnPressed("Remove");
                setTextArea("""
                    \t      Welcome to remove player in your roster!!!
                                         
                            Please read the below instructions carefully......

                            1. Your roster must consist a minimum of 10 players 
                                and a maximum of 15 players.

                            2. Your roster must have at least 2 Guard, Forward, Centre.

                            3. Superstar player' salary is 3000 while 
                                non-Superstar player's salary is 1000.

                            4. Your roster total salary should not exceed 20000.

                            5. Remove your roster's player wisely based on their performances
                                and conditions.
                            
                            6. Try to click the table row to view the player profile!!
                            
                            7. Press the button on the left side to view respective group of players.
                     """, "Instructions Before Remove Players");
            }                
    }//GEN-LAST:event_btnRemovePlayerActionPerformed

    private void btnMainMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMainMenuActionPerformed
        dispose();
        new jMainMenu().setVisible(true);
    }//GEN-LAST:event_btnMainMenuActionPerformed

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
            java.util.logging.Logger.getLogger(jRosterMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(jRosterMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(jRosterMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(jRosterMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new jRosterMenu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel addPlayerIcon;
    private javax.swing.JButton btnAddPlayer;
    private javax.swing.JButton btnMainMenu;
    private javax.swing.JButton btnRemovePlayer;
    private javax.swing.JButton btnViewPlayer;
    private javax.swing.JLabel deco;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel labelAddPlayer;
    private javax.swing.JLabel labelBasketball;
    private javax.swing.JLabel labelRemovePlayer;
    private javax.swing.JLabel labelViewPlayer;
    private javax.swing.JLabel nbaLogo;
    private javax.swing.JPanel panelTitle;
    private javax.swing.JLabel removePlayerIcon;
    private javax.swing.JLabel viewPlayerIcon;
    // End of variables declaration//GEN-END:variables
}
