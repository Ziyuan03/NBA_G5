package nba;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author hp
 */

import java.awt.Image;
import nba.*;
import java.util.Map;
import java.util.HashMap;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class GraphFrame extends javax.swing.JFrame {
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root123NBA.";
    private static final String DATA_CON = "jdbc:mysql://localhost:3306/nba";

    /**
     * Creates new form GraphFrame
     */
    public GraphFrame() {
        initComponents();
        attachButtonListener();
        setupDatabase();
        setImageMethod("src\\icons\\home (2).png", btnHome);        
        setImageMethod("src\\icons\\warriors.png", warriors);        
        setImageMethod("src\\icons\\lakers.jpg", lakers); 
        setImageMethod("src\\icons\\suns.png", suns);
        setImageMethod("src\\icons\\san-antonio-spurs6036.jpg", spurs);  
        setImageMethod("src\\icons\\thunder_new.jpg", thunder);  
        setImageMethod("src\\icons\\nuggets.jpg", nuggets);  
        setImageMethod("src\\icons\\celtics.jpg", celtics);  
        setImageMethod("src\\icons\\rockets.jpg", rockets);  
        setImageMethod("src\\icons\\heat.png", heat);  
        setImageMethod("src\\icons\\magic.jpg", magic);  
    }
    
    //Method set label image
    public void setImageMethod(String imagePath, JButton labelName) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage();
        Image imgScale = img.getScaledInstance(labelName.getWidth(), labelName.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(imgScale);
        labelName.setIcon(scaledIcon);                            
    }
    
    private void setupDatabase() {
        createScheduleTable();
        insertScheduleData(buildGraph());
    }

    private void createScheduleTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS schedule (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    team_from VARCHAR(50) NOT NULL,
                    team_to VARCHAR(50) NOT NULL,
                    distance INT NOT NULL
                );
                """;

        try (Connection conn = DriverManager.getConnection(DATA_CON, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.execute();
            System.out.println("Schedule table has been created.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void insertScheduleData(Map<String, Map<String, Integer>> graph) {
        String sql = """
                INSERT INTO schedule (team_from, team_to, distance) VALUES (?, ?, ?)
                """;

        try (Connection conn = DriverManager.getConnection(DATA_CON, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (String teamFrom : graph.keySet()) {
                for (Map.Entry<String, Integer> entry : graph.get(teamFrom).entrySet()) {
                    insertData(pstmt, teamFrom, entry.getKey(), entry.getValue());
                }
            }

            System.out.println("Schedule data has been inserted.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void insertData(PreparedStatement pstmt, String teamFrom, String teamTo, int distance) throws SQLException {
        pstmt.setString(1, teamFrom);
        pstmt.setString(2, teamTo);
        pstmt.setInt(3, distance);
        pstmt.executeUpdate();
    }


    private void performTSPComputation() {
        Map<String, Map<String, Integer>> graph = buildGraph();  // Prepare the graph
        BranchAndBoundTSP.findShortestRoute(graph, "Spurs");
        List<String> optimalPath = BranchAndBoundTSP.getOptimalPath();  // Get the optimal path
        ((GraphPanel) jPanel1).setOptimalPath(optimalPath);
        updateDisplay();
    }

    private Map<String, Map<String, Integer>> buildGraph() {
        Map<String, Map<String, Integer>> graph = new HashMap<>();
        graph.put("Spurs", Map.of("Suns", 500, "Thunder", 678, "Rockets", 983, "Magic", 1137));
        graph.put("Suns", Map.of("Lakers", 577, "Spurs", 500));
        graph.put("Thunder", Map.of("Warriors", 2214, "Lakers", 1901, "Nuggets", 942, "Rockets", 778, "Spurs", 678));
        graph.put("Rockets", Map.of("Spurs", 983, "Thunder", 778, "Magic", 458, "Celtics", 2584));
        graph.put("Magic", Map.of("Spurs", 1137, "Rockets", 458, "Heat", 268));
        graph.put("Heat", Map.of("Magic", 268, "Celtics", 3045));
        graph.put("Nuggets", Map.of("Warriors", 1507, "Celtics", 2845, "Thunder", 942));
        graph.put("Celtics", Map.of("Nuggets", 2845, "Rockets", 2584, "Heat", 3045));
        graph.put("Warriors", Map.of("Lakers", 554, "Thunder", 2214, "Nuggets", 1507));
        graph.put("Lakers", Map.of("Warriors", 554, "Thunder", 1901, "Suns", 577));
        // Add the rest of your connections
        return graph;
    }

    private void attachButtonListener() {
        StartButton.addActionListener(e -> {
            performTSPComputation();
        });
    }

    private void updateDisplay() {
        String results = BranchAndBoundTSP.getResultString();
        DisplayShortestPathNDistance.setText(results);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jLayeredPane2 = new javax.swing.JLayeredPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        StartButton = new javax.swing.JButton();
        ShortestPathAlgorithm = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        heat = new javax.swing.JButton();
        warriors = new javax.swing.JButton();
        lakers = new javax.swing.JButton();
        spurs = new javax.swing.JButton();
        magic = new javax.swing.JButton();
        suns = new javax.swing.JButton();
        jPanel1 = new GraphPanel();
        celtics = new javax.swing.JButton();
        rockets = new javax.swing.JButton();
        thunder = new javax.swing.JButton();
        nuggets = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        btnHome = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        DisplayShortestPathNDistance = new javax.swing.JTextArea();

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(940, 760));
        setPreferredSize(new java.awt.Dimension(969, 760));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLayeredPane2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 0, 153));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Copperplate Gothic Bold", 0, 36)); // NOI18N
        jLabel1.setText("GAME SCHEDULE");
        jPanel4.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 23, 405, -1));

        jLabel2.setFont(new java.awt.Font("Copperplate Gothic Bold", 0, 14)); // NOI18N
        jLabel2.setText("\"Navigate to greatness with our map.\"");
        jPanel4.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 350, -1));

        StartButton.setFont(new java.awt.Font("Kristen ITC", 1, 14)); // NOI18N
        StartButton.setText("Start");
        jPanel4.add(StartButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(639, 20, 107, -1));

        ShortestPathAlgorithm.setFont(new java.awt.Font("Kristen ITC", 1, 14)); // NOI18N
        ShortestPathAlgorithm.setText("Algorithm");
        ShortestPathAlgorithm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShortestPathAlgorithmActionPerformed(evt);
            }
        });
        jPanel4.add(ShortestPathAlgorithm, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 60, 132, 30));
        jPanel4.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(529, 20, -1, -1));
        jPanel4.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(885, 20, -1, -1));
        jPanel4.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 30, -1, -1));
        jPanel4.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 30, -1, -1));

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 880, 110));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        heat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                heatActionPerformed(evt);
            }
        });

        warriors.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                warriorsActionPerformed(evt);
            }
        });

        lakers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lakersActionPerformed(evt);
            }
        });

        spurs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spursActionPerformed(evt);
            }
        });

        magic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                magicActionPerformed(evt);
            }
        });

        suns.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sunsActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(153, 153, 255));
        jPanel1.setForeground(new java.awt.Color(204, 255, 204));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 569, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        celtics.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                celticsActionPerformed(evt);
            }
        });

        rockets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rocketsActionPerformed(evt);
            }
        });

        thunder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                thunderActionPerformed(evt);
            }
        });

        nuggets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuggetsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(thunder, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spurs, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(suns, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lakers, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(warriors, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(heat, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rockets, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(magic, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(celtics, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nuggets, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(23, 23, 23))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(nuggets, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                                .addComponent(celtics, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32)
                                .addComponent(rockets, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(heat, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(magic, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(warriors, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(lakers, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(suns, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(spurs, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(thunder, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(14, 14, 14))))
        );

        jPanel2.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 140, 790, 490));

        jPanel3.setBackground(new java.awt.Color(255, 51, 0));

        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(46, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(559, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(-20, 0, 140, 770));

        DisplayShortestPathNDistance.setColumns(20);
        DisplayShortestPathNDistance.setFont(new java.awt.Font("Kristen ITC", 1, 12)); // NOI18N
        DisplayShortestPathNDistance.setRows(5);
        jScrollPane1.setViewportView(DisplayShortestPathNDistance);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 650, 790, 80));

        jLayeredPane2.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 990, 760));

        getContentPane().add(jLayeredPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 970, 760));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void ShortestPathAlgorithmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ShortestPathAlgorithmActionPerformed
        // TODO add your handling code here:
        JTextArea messageTextArea = new JTextArea(10, 40); // 10 rows, 40 columns size
        messageTextArea.setText("Branch and Bound is suitable for the TSP when the number of cities (nodes) "
                + "is not excessively large due to its exponential complexity in the worst case. "
                + "It is used in scenarios where finding the absolute shortest route is crucial "
                + "and computational resources allow for an exhaustive search method enhanced by "
                + "intelligent pruning. This approach is more computationally intensive than "
                + "heuristic methods (like nearest neighbor or genetic algorithms) but provides "
                + "exact results, making it ideal for critical applications or smaller datasets "
                + "where precision is paramount.");
        messageTextArea.setWrapStyleWord(true);
        messageTextArea.setLineWrap(true);
        messageTextArea.setCaretPosition(0);
        messageTextArea.setEditable(false);

        // Place the JTextArea in a JScrollPane
        JScrollPane scrollPane = new JScrollPane(messageTextArea);

        // Display the JScrollPane in a JOptionPane dialog
        JOptionPane.showMessageDialog(this, scrollPane, "Branch and Bound Algorithm", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_ShortestPathAlgorithmActionPerformed

    private void celticsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_celticsActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(this,
                "One of the most storied franchises in the NBA, the Boston Celtics were established in 1946 and are based in Boston,\nMassachusetts. The Celtics have won 17 NBA championships, tied for the most in NBA history, with a rich history of legendary players and intense rivalries.",
                "Bonston Celtics Info",
                JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_celticsActionPerformed

    private void rocketsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rocketsActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(this,
                "Established in 1967 and based in Houston, Texas, the Houston Rockets have won two NBA championships.\nKnown for their \"Clutch City\" era in the mid-1990s and more recently for adopting a radical small-ball lineup.",
                "Houston Rockets Info",
                JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_rocketsActionPerformed

    private void magicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_magicActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(this,
                "Founded in 1989 and based in Orlando, Florida, the Orlando Magic have reached the NBA Finals twice but have not won a championship.\nThey are known for their early success with stars like Shaquille O'Neal and later with Dwight Howard.",
                "Orlando Magic Info",
                JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_magicActionPerformed

    private void warriorsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_warriorsActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(this,
                "Founded in 1946 and based in San Francisco, California, the Golden State Warriors have won a total of seven NBA championships.\nKnown for their revolutionary play style centered around shooting and small-ball tactics, they became a dominant force in the 2010s.",
                "Golden State Warriors Info",
                JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_warriorsActionPerformed

    private void heatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_heatActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(this,
                "Established in 1988 and based in Miami, Florida, the Miami Heat have won three NBA championships.\nKnown for their strong defensive play and the creation of the \"Big Three\" era with LeBron James, Dwyane Wade, and Chris Bosh in the early 2010s.",
                "Miami Heat Info",
                JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_heatActionPerformed

    private void lakersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lakersActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(this,
                "Founded in 1947 and now based in Los Angeles, California, the Lakers have won 17 NBA championships, tying them with the Boston Celtics for the most in the league.\nThey are known for their glamorous \"Showtime\" era in the 1980s and a series of superstar players through the decades.",
                "Los Angeles Lakers Info",
                JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_lakersActionPerformed

    private void sunsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sunsActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(this,
                "Established in 1968 and based in Phoenix, Arizona, the Phoenix Suns have appeared in the NBA Finals three times but have yet to win\na championship. They are known for their innovative offenses, particularly the fast-paced \"Seven Seconds or Less\" strategy in the mid-2000s.",
                "Phoenix Suns Info",
                JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_sunsActionPerformed

    private void nuggetsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuggetsActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(this,
                "Established in 1967 as part of the ABA and joined the NBA in 1976, based in Denver, Colorado.\nThe Nuggets have had consistent playoff appearances but have yet to reach the pinnacle of winning an NBA championship.",
                "Denver Nuggets Info",
                JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_nuggetsActionPerformed

    private void thunderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_thunderActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(this,
                "Originally founded as the Seattle SuperSonics in 1967 and relocated to Oklahoma City in 2008, the Thunder reached the NBA Finals once in 2012.\nThey are known for their strong fan support and the development of several young, talented players who have become NBA stars.",
                "Oklahoma City Thunder",
                JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_thunderActionPerformed

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        // TODO add your handling code here:
        dispose();
        new jMainMenu().setVisible(true);
    }//GEN-LAST:event_btnHomeActionPerformed

    private void spursActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spursActionPerformed
        // TODO add your handling code here:

        JOptionPane.showMessageDialog(this,
            "The San Antonio Spurs, founded in 1967 and based in Texas, have won five NBA championships with strong team play.",
            "San Antonio Spurs Info",
            JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_spursActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        Map<String, Map<String, Integer>> graph = new HashMap<>();
        graph.put("Spurs", Map.of("Suns", 500, "Thunder", 678, "Rockets", 983, "Magic", 1137));
        graph.put("Suns", Map.of("Lakers", 577, "Spurs", 500));
        graph.put("Thunder", Map.of("Warriors", 2214, "Lakers", 1901, "Nuggets", 942, "Rockets", 778, "Spurs", 678));
        graph.put("Rockets", Map.of("Spurs", 983, "Thunder", 778, "Magic", 458, "Celtics", 2584));
        graph.put("Magic", Map.of("Spurs", 1137, "Rockets", 458, "Heat", 268));
        graph.put("Heat", Map.of("Magic", 268, "Celtics", 3045));
        graph.put("Nuggets", Map.of("Warriors", 1507, "Celtics", 2845, "Thunder", 942));
        graph.put("Celtics", Map.of("Nuggets", 2845, "Rockets", 2584, "Heat", 3045));
        graph.put("Warriors", Map.of("Lakers", 554, "Thunder", 2214, "Nuggets", 1507));
        graph.put("Lakers", Map.of("Warriors", 554, "Thunder", 1901, "Suns", 577));
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
            java.util.logging.Logger.getLogger(GraphFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GraphFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GraphFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GraphFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new GraphFrame().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea DisplayShortestPathNDistance;
    private javax.swing.JButton ShortestPathAlgorithm;
    private javax.swing.JButton StartButton;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton celtics;
    private javax.swing.JButton heat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLayeredPane jLayeredPane2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton lakers;
    private javax.swing.JButton magic;
    private javax.swing.JButton nuggets;
    private javax.swing.JButton rockets;
    private javax.swing.JButton spurs;
    private javax.swing.JButton suns;
    private javax.swing.JButton thunder;
    private javax.swing.JButton warriors;
    // End of variables declaration//GEN-END:variables
}
