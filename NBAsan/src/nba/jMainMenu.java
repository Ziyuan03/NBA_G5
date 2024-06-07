/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package nba;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.RoundRectangle2D;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

/**
 *
 * @author user
 */
public class jMainMenu extends javax.swing.JFrame {
    
    Connection con;

    //Hover effect
    BevelBorder outerBevel = new BevelBorder(BevelBorder.RAISED, new Color(0, 0, 153), new Color(153, 153, 255), new Color(0, 0, 102), new Color(51, 51, 153));
    BevelBorder innerBevel = new BevelBorder(BevelBorder.RAISED, new Color(153, 0, 0), new Color(255, 153, 153), new Color(102, 0, 0), new Color(153, 51, 51));
    Border hoverPanel = new CompoundBorder(outerBevel, innerBevel);

    //Default Effect
    LineBorder defaultBorder = new LineBorder(Color.GRAY, 3);

    public jMainMenu() {
        initComponents();
        setImage();
        createConnection();
        
        //update roster info
        int playerNumber = Integer.parseInt(getCurrent("COUNT(Player_ID)", "playerNumber"));
        String player = playerNumber > 1 ? " players" : " player";
        labelPlayerNumber.setText(playerNumber + player);
        labelTotalSalary.setText("$" + getCurrent("SUM(Salary)", "totalSalary"));
    }

    private void setImage() {
        setImageMethod("src\\icons\\nbaLogo.png", nbaLogo);
        setImageMethod("src\\icons\\backgroundMichaelJordan.png", backgroundViewRoster);
        setImageMethod("src\\icons\\backgroundGameSchedule.png", backgroundGameSchedule);
        setImageMethod("src\\icons\\backgroundInjuryPlayer.png", backgroundInjuryPlayers);
        setImageMethod("src\\icons\\backgroundRanking.png", backgroundRanking);
        setImageMethod("src\\icons\\backgroundDynamicSearch.png", backgroundDynamicSearch);
        setImageMethod("src\\icons\\backgroundContract.png", backgroundContract);
        setImageMethod("src\\icons\\backgroundBasketballCourt.jpg", backgroundBasketballCourt);
        setImageMethod("src\\icons\\iconManager.png", iconManager);
        setImageMethod("src\\icons\\iconLogOut.png", iconLogOut);
    }

    public void setImageMethod(String imagePath, JLabel labelName) {
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

    //Calculate current player(s) in roster / total salary (Multiuse Method)
    private String getCurrent(String function, String functionName) {
        StringBuilder sb = new StringBuilder();
        
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
        
        return sb.append(current).toString();
    }
    
    public javax.swing.JLabel getLabelManager() {
        return labelManager;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        viewRosterPanel = new javax.swing.JPanel();
        labelTotalSalary = new javax.swing.JLabel();
        labelViewRoster2 = new javax.swing.JLabel();
        backgroundViewRoster = new javax.swing.JLabel();
        labelViewRoster1 = new javax.swing.JLabel();
        labelPlayerNumber = new javax.swing.JLabel();
        gameSchedulePanel = new javax.swing.JPanel();
        labelGameSchedule2 = new javax.swing.JLabel();
        labelGameSchedule1 = new javax.swing.JLabel();
        backgroundGameSchedule = new javax.swing.JLabel();
        rankingPanel = new javax.swing.JPanel();
        labelRanking = new javax.swing.JLabel();
        backgroundRanking = new javax.swing.JLabel();
        injuryPlayerPanel = new javax.swing.JPanel();
        labelInjuryPlayer1 = new javax.swing.JLabel();
        backgroundInjuryPlayers = new javax.swing.JLabel();
        labelInjuryPlayer2 = new javax.swing.JLabel();
        dynamicSearchPanel = new javax.swing.JPanel();
        labelDynamicSearch1 = new javax.swing.JLabel();
        labelDynamicSearch2 = new javax.swing.JLabel();
        backgroundDynamicSearch = new javax.swing.JLabel();
        panelManager = new javax.swing.JPanel();
        labelManager = new javax.swing.JLabel();
        iconLogOut = new javax.swing.JLabel();
        iconManager = new javax.swing.JLabel();
        nbaLogo = new javax.swing.JLabel();
        contractPanel = new javax.swing.JPanel();
        labelContract = new javax.swing.JLabel();
        backgroundContract = new javax.swing.JLabel();
        backgroundBasketballCourt = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(969, 760));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        viewRosterPanel.setBackground(new java.awt.Color(0, 0, 0));
        viewRosterPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 3, true));
        viewRosterPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                viewRosterPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                viewRosterPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                viewRosterPanelMouseExited(evt);
            }
        });
        viewRosterPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelTotalSalary.setFont(new java.awt.Font("HP Simplified Hans", 1, 24)); // NOI18N
        labelTotalSalary.setForeground(new java.awt.Color(255, 255, 255));
        labelTotalSalary.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTotalSalary.setText("2000");
        viewRosterPanel.add(labelTotalSalary, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 120, 30));

        labelViewRoster2.setFont(new java.awt.Font("HP Simplified Hans", 1, 24)); // NOI18N
        labelViewRoster2.setForeground(new java.awt.Color(255, 255, 255));
        labelViewRoster2.setText("Roster");
        viewRosterPanel.add(labelViewRoster2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 100, 40));

        backgroundViewRoster.setText("background");
        viewRosterPanel.add(backgroundViewRoster, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 5, 286, 271));

        labelViewRoster1.setFont(new java.awt.Font("HP Simplified Hans", 1, 24)); // NOI18N
        labelViewRoster1.setForeground(new java.awt.Color(255, 255, 255));
        labelViewRoster1.setText("View");
        viewRosterPanel.add(labelViewRoster1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 70, 30));

        labelPlayerNumber.setFont(new java.awt.Font("HP Simplified Hans", 1, 24)); // NOI18N
        labelPlayerNumber.setForeground(new java.awt.Color(255, 255, 255));
        labelPlayerNumber.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelPlayerNumber.setText("14 Players");
        viewRosterPanel.add(labelPlayerNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 120, 30));

        getContentPane().add(viewRosterPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 240, 440, 280));

        gameSchedulePanel.setBackground(new java.awt.Color(0, 0, 0));
        gameSchedulePanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 3, true));
        gameSchedulePanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                gameSchedulePanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                gameSchedulePanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                gameSchedulePanelMouseExited(evt);
            }
        });

        labelGameSchedule2.setFont(new java.awt.Font("HP Simplified Hans", 1, 24)); // NOI18N
        labelGameSchedule2.setForeground(new java.awt.Color(255, 255, 255));
        labelGameSchedule2.setText("Schedule");

        labelGameSchedule1.setFont(new java.awt.Font("HP Simplified Hans", 1, 24)); // NOI18N
        labelGameSchedule1.setForeground(new java.awt.Color(255, 255, 255));
        labelGameSchedule1.setText("Game ");

        backgroundGameSchedule.setText("background");

        javax.swing.GroupLayout gameSchedulePanelLayout = new javax.swing.GroupLayout(gameSchedulePanel);
        gameSchedulePanel.setLayout(gameSchedulePanelLayout);
        gameSchedulePanelLayout.setHorizontalGroup(
            gameSchedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gameSchedulePanelLayout.createSequentialGroup()
                .addGroup(gameSchedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(gameSchedulePanelLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(labelGameSchedule1))
                    .addGroup(gameSchedulePanelLayout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(labelGameSchedule2, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(231, Short.MAX_VALUE))
            .addGroup(gameSchedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gameSchedulePanelLayout.createSequentialGroup()
                    .addGap(0, 176, Short.MAX_VALUE)
                    .addComponent(backgroundGameSchedule, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        gameSchedulePanelLayout.setVerticalGroup(
            gameSchedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gameSchedulePanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(labelGameSchedule1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelGameSchedule2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
            .addGroup(gameSchedulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(backgroundGameSchedule, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE))
        );

        getContentPane().add(gameSchedulePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 240, 400, -1));

        rankingPanel.setBackground(new java.awt.Color(0, 0, 0));
        rankingPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 3, true));
        rankingPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rankingPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                rankingPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                rankingPanelMouseExited(evt);
            }
        });

        labelRanking.setFont(new java.awt.Font("HP Simplified Hans", 1, 24)); // NOI18N
        labelRanking.setForeground(new java.awt.Color(255, 255, 255));
        labelRanking.setText("Ranking");

        backgroundRanking.setText("background");

        javax.swing.GroupLayout rankingPanelLayout = new javax.swing.GroupLayout(rankingPanel);
        rankingPanel.setLayout(rankingPanelLayout);
        rankingPanelLayout.setHorizontalGroup(
            rankingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rankingPanelLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(labelRanking, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(309, Short.MAX_VALUE))
            .addGroup(rankingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rankingPanelLayout.createSequentialGroup()
                    .addGap(0, 145, Short.MAX_VALUE)
                    .addComponent(backgroundRanking, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        rankingPanelLayout.setVerticalGroup(
            rankingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rankingPanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(labelRanking, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(47, Short.MAX_VALUE))
            .addGroup(rankingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(backgroundRanking, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE))
        );

        getContentPane().add(rankingPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 530, -1, -1));

        injuryPlayerPanel.setBackground(new java.awt.Color(0, 0, 0));
        injuryPlayerPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 3, true));
        injuryPlayerPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                injuryPlayerPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                injuryPlayerPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                injuryPlayerPanelMouseExited(evt);
            }
        });

        labelInjuryPlayer1.setFont(new java.awt.Font("HP Simplified Hans", 1, 24)); // NOI18N
        labelInjuryPlayer1.setForeground(new java.awt.Color(255, 255, 255));
        labelInjuryPlayer1.setText("Injury");

        backgroundInjuryPlayers.setText("background");

        labelInjuryPlayer2.setFont(new java.awt.Font("HP Simplified Hans", 1, 24)); // NOI18N
        labelInjuryPlayer2.setForeground(new java.awt.Color(255, 255, 255));
        labelInjuryPlayer2.setText("Players");

        javax.swing.GroupLayout injuryPlayerPanelLayout = new javax.swing.GroupLayout(injuryPlayerPanel);
        injuryPlayerPanel.setLayout(injuryPlayerPanelLayout);
        injuryPlayerPanelLayout.setHorizontalGroup(
            injuryPlayerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(injuryPlayerPanelLayout.createSequentialGroup()
                .addGroup(injuryPlayerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(injuryPlayerPanelLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(labelInjuryPlayer1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(injuryPlayerPanelLayout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(labelInjuryPlayer2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(backgroundInjuryPlayers, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        injuryPlayerPanelLayout.setVerticalGroup(
            injuryPlayerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, injuryPlayerPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelInjuryPlayer1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelInjuryPlayer2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
            .addGroup(injuryPlayerPanelLayout.createSequentialGroup()
                .addComponent(backgroundInjuryPlayers, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        getContentPane().add(injuryPlayerPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 390, 400, 130));

        dynamicSearchPanel.setBackground(new java.awt.Color(0, 0, 0));
        dynamicSearchPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 3, true));
        dynamicSearchPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dynamicSearchPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                dynamicSearchPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                dynamicSearchPanelMouseExited(evt);
            }
        });

        labelDynamicSearch1.setFont(new java.awt.Font("HP Simplified Hans", 1, 24)); // NOI18N
        labelDynamicSearch1.setForeground(new java.awt.Color(255, 255, 255));
        labelDynamicSearch1.setText("Dynamic");

        labelDynamicSearch2.setFont(new java.awt.Font("HP Simplified Hans", 1, 24)); // NOI18N
        labelDynamicSearch2.setForeground(new java.awt.Color(255, 255, 255));
        labelDynamicSearch2.setText("Search");

        backgroundDynamicSearch.setText("background");

        javax.swing.GroupLayout dynamicSearchPanelLayout = new javax.swing.GroupLayout(dynamicSearchPanel);
        dynamicSearchPanel.setLayout(dynamicSearchPanelLayout);
        dynamicSearchPanelLayout.setHorizontalGroup(
            dynamicSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dynamicSearchPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dynamicSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelDynamicSearch1)
                    .addComponent(labelDynamicSearch2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(backgroundDynamicSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        dynamicSearchPanelLayout.setVerticalGroup(
            dynamicSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dynamicSearchPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(labelDynamicSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelDynamicSearch2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dynamicSearchPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(backgroundDynamicSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(dynamicSearchPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 530, 190, -1));

        panelManager.setBackground(new java.awt.Color(0, 0, 0));
        panelManager.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 3, true));

        labelManager.setFont(new java.awt.Font("HP Simplified Hans", 1, 18)); // NOI18N
        labelManager.setForeground(new java.awt.Color(255, 255, 255));
        labelManager.setText("Welcome Back, root!");

        iconLogOut.setText("logOut");
        iconLogOut.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconLogOutMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconLogOutMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconLogOutMouseExited(evt);
            }
        });

        iconManager.setText("manager");
        iconManager.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        iconManager.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iconManagerMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                iconManagerMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                iconManagerMouseExited(evt);
            }
        });

        javax.swing.GroupLayout panelManagerLayout = new javax.swing.GroupLayout(panelManager);
        panelManager.setLayout(panelManagerLayout);
        panelManagerLayout.setHorizontalGroup(
            panelManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelManagerLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(labelManager, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(iconManager, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(iconLogOut, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        panelManagerLayout.setVerticalGroup(
            panelManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelManagerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelManagerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelManager, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(iconLogOut, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(iconManager, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(panelManager, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 70, 390, 80));

        nbaLogo.setText("logo");
        getContentPane().add(nbaLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, 90, 170));

        contractPanel.setBackground(new java.awt.Color(0, 0, 0));
        contractPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 3, true));
        contractPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                contractPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                contractPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                contractPanelMouseExited(evt);
            }
        });

        labelContract.setFont(new java.awt.Font("HP Simplified Hans", 1, 21)); // NOI18N
        labelContract.setForeground(new java.awt.Color(255, 255, 255));
        labelContract.setText("Contract");

        backgroundContract.setText("background");

        javax.swing.GroupLayout contractPanelLayout = new javax.swing.GroupLayout(contractPanel);
        contractPanel.setLayout(contractPanelLayout);
        contractPanelLayout.setHorizontalGroup(
            contractPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contractPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelContract)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(backgroundContract, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        contractPanelLayout.setVerticalGroup(
            contractPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contractPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(labelContract, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(backgroundContract, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        getContentPane().add(contractPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 530, 200, 125));

        backgroundBasketballCourt.setText("background");
        getContentPane().add(backgroundBasketballCourt, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 970, 760));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    //Hover Effect
    private void viewRosterPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewRosterPanelMouseEntered
        viewRosterPanel.setBorder(hoverPanel);
        viewRosterPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        Cursor[] cursors = {
//            Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR),
//            Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR),
//            Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR),
//            Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR),
//            Cursor.getPredefinedCursor(Cursor.HAND_CURSOR),
//            Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR),
//            Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR),
//            Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR),
//            Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR),
//            Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR),
//            Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR),
//            Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR),
//            Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR),
//            Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR)
//        };                            
    }//GEN-LAST:event_viewRosterPanelMouseEntered

    private void viewRosterPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewRosterPanelMouseExited
        viewRosterPanel.setBorder(defaultBorder);
        viewRosterPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_viewRosterPanelMouseExited

    private void gameSchedulePanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_gameSchedulePanelMouseEntered
        gameSchedulePanel.setBorder(hoverPanel);
        gameSchedulePanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_gameSchedulePanelMouseEntered

    private void gameSchedulePanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_gameSchedulePanelMouseExited
        gameSchedulePanel.setBorder(defaultBorder);
        gameSchedulePanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_gameSchedulePanelMouseExited

    private void injuryPlayerPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_injuryPlayerPanelMouseEntered
        injuryPlayerPanel.setBorder(hoverPanel);
        injuryPlayerPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_injuryPlayerPanelMouseEntered

    private void injuryPlayerPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_injuryPlayerPanelMouseExited
        injuryPlayerPanel.setBorder(defaultBorder);
        gameSchedulePanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_injuryPlayerPanelMouseExited

    private void rankingPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rankingPanelMouseEntered
        rankingPanel.setBorder(hoverPanel);
        rankingPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_rankingPanelMouseEntered

    private void rankingPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rankingPanelMouseExited
        rankingPanel.setBorder(defaultBorder);
        rankingPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_rankingPanelMouseExited

    private void dynamicSearchPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dynamicSearchPanelMouseEntered
        dynamicSearchPanel.setBorder(hoverPanel);
        dynamicSearchPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_dynamicSearchPanelMouseEntered

    private void dynamicSearchPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dynamicSearchPanelMouseExited
        dynamicSearchPanel.setBorder(defaultBorder);
        dynamicSearchPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_dynamicSearchPanelMouseExited

    private void contractPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_contractPanelMouseEntered
        contractPanel.setBorder(hoverPanel);
        contractPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_contractPanelMouseEntered

    private void contractPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_contractPanelMouseExited
        contractPanel.setBorder(defaultBorder);
        contractPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_contractPanelMouseExited

    private void iconManagerMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconManagerMouseEntered
        iconManager.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));        
    }//GEN-LAST:event_iconManagerMouseEntered

    private void iconManagerMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconManagerMouseExited
        iconManager.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_iconManagerMouseExited

    private void iconLogOutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconLogOutMouseEntered
        iconLogOut.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_iconLogOutMouseEntered

    private void iconLogOutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconLogOutMouseExited
        iconLogOut.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_iconLogOutMouseExited

    //EventListener
    private void viewRosterPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewRosterPanelMouseClicked
        dispose();
        new jRosterMenu().setVisible(true);
    }//GEN-LAST:event_viewRosterPanelMouseClicked

    private void gameSchedulePanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_gameSchedulePanelMouseClicked
        dispose();
        new GraphFrame().setVisible(true);
    }//GEN-LAST:event_gameSchedulePanelMouseClicked

    private void injuryPlayerPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_injuryPlayerPanelMouseClicked
        dispose();
        new InjuryPlayerMainPage().setVisible(true);
    }//GEN-LAST:event_injuryPlayerPanelMouseClicked

    private void rankingPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rankingPanelMouseClicked
        dispose();
        new Ranking().setVisible(true);
    }//GEN-LAST:event_rankingPanelMouseClicked

    private void dynamicSearchPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dynamicSearchPanelMouseClicked
        dispose();
        new DynamicSearchingPage().setVisible(true);
    }//GEN-LAST:event_dynamicSearchPanelMouseClicked

    private void contractPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_contractPanelMouseClicked
        dispose();
        new ContractExtension().setVisible(true);
    }//GEN-LAST:event_contractPanelMouseClicked

    private void iconManagerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconManagerMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_iconManagerMouseClicked

    private void iconLogOutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iconLogOutMouseClicked
        dispose();
        new Login().setVisible(true);
    }//GEN-LAST:event_iconLogOutMouseClicked
          
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
            java.util.logging.Logger.getLogger(jMainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(jMainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(jMainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(jMainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new jMainMenu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel backgroundBasketballCourt;
    private javax.swing.JLabel backgroundContract;
    private javax.swing.JLabel backgroundDynamicSearch;
    private javax.swing.JLabel backgroundGameSchedule;
    private javax.swing.JLabel backgroundInjuryPlayers;
    private javax.swing.JLabel backgroundRanking;
    private javax.swing.JLabel backgroundViewRoster;
    private javax.swing.JPanel contractPanel;
    private javax.swing.JPanel dynamicSearchPanel;
    private javax.swing.JPanel gameSchedulePanel;
    private javax.swing.JLabel iconLogOut;
    private javax.swing.JLabel iconManager;
    private javax.swing.JPanel injuryPlayerPanel;
    private javax.swing.JLabel labelContract;
    private javax.swing.JLabel labelDynamicSearch1;
    private javax.swing.JLabel labelDynamicSearch2;
    private javax.swing.JLabel labelGameSchedule1;
    private javax.swing.JLabel labelGameSchedule2;
    private javax.swing.JLabel labelInjuryPlayer1;
    private javax.swing.JLabel labelInjuryPlayer2;
    private javax.swing.JLabel labelManager;
    private javax.swing.JLabel labelPlayerNumber;
    private javax.swing.JLabel labelRanking;
    private javax.swing.JLabel labelTotalSalary;
    private javax.swing.JLabel labelViewRoster1;
    private javax.swing.JLabel labelViewRoster2;
    private javax.swing.JLabel nbaLogo;
    private javax.swing.JPanel panelManager;
    private javax.swing.JPanel rankingPanel;
    private javax.swing.JPanel viewRosterPanel;
    // End of variables declaration//GEN-END:variables
}
