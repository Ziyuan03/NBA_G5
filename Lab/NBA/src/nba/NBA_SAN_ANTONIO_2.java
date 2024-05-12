package nba;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NBA_SAN_ANTONIO_2 {
        Connection con;
        Scanner sc = new Scanner(System.in);
        
        private final int playerMin, playerMax, salaryMax;
        String[] positionArray = {"G", "F", "C"};
        int[] positionNumber = new int[3];
        
        //Constructor (may be useless)
        public NBA_SAN_ANTONIO_2() {
            playerMin = 10;
            playerMax = 15;           
            salaryMax = 20000;
        }
        
        //Create connection with database when initiate this program everytime 
        public void createConnection() {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nba", "root", "root123NBA.");
                
                System.out.println("Database is Connected. Program will be started soon...");
                
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(NBA_SAN_ANTONIO_2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //First method called (Introduction)
        public void teamStart() {
            createConnection();
            sc.useDelimiter("\n");
            
            System.out.println("\nWelcome to San Antonio! As a roster manager, you need to build a strong roster to embark on the NBA championship journey!");
            System.out.println("\nAre you ready for it?? (Yes / No)");
            System.out.println("Yes: Proceed to next step.");
            System.out.println("No : I'm not ready. Return to home page.");
            String ready = sc.next();
            
            if(ready.equalsIgnoreCase("Yes")) {
                System.out.println("\nAs a roster manager, You have the rights to manage your roster.");
                teamMenu();
            } else if(ready.equalsIgnoreCase("No")) {
                System.out.println("\nYou will be redirected to home page...");
                System.out.println("But the thing is there is no home page now ^-^");
            } else {
                System.out.println("\nPlease enter the correct value! Only 'Yes' or 'No' is acceptable!\n");
                teamStart();
            }
        }        
        
        //Menu
        public void teamMenu() {      
            sc.useDelimiter("\n");
            
            System.out.println("\n| MENU |");
            System.out.println("Enter (1 / 2 / 3 / 4 / 5 / 6) to proceed to next step:");
            System.out.println("1: View your current Championship Roster");            
            System.out.println("2: Add player to your current Championship Roster");
            System.out.println("3: Remove a player from your current Championship Roster");
            System.out.println("4: Return to home page");
            System.out.println("5: Quit this stupid program");
            String choice = sc.next();
            
            switch (choice) {
                case "1" -> {
                    viewRosterOrList("SAN_ANTONIO");
                }
                case "2" -> { 
                    //Check exceed salary limit
                    if(getCurrent("SUM(Salary)", "TotalSalary") < salaryMax) {
                        
                        //Check whether exceed number of player limit
                        if(getCurrent("COUNT(Player_ID)", "TotalSalary") < playerMax) {
                            acceptViewCandidateList();
                        } else {
                            elseTeamFull();
                        }                                                                        
                    } else {
                        elseSalaryFull();
                    }
                }
                case "3" -> {
                    beforeRemovePlayer();
                }
                case "4" -> {
                    System.out.println("\nYou will be redirected to home page.");
                    System.out.println("But the thing is there is no home page now ^-^");
                }
                case "5" -> {
                    System.out.println("\nBye Bye~~");
                }
                default -> {
                    System.out.println("\nUnknown input! Please try again!");
                    teamMenu();
                }
            }
        }        
        
        //View Roster or Candidate List
        public void viewRosterOrList(String tableName) {            
            System.out.println("\nEnter (1 / 2 / 3 / 4) to proceed to next step.");
            System.out.println("1: View All Player(s) in your " + tableName);
            System.out.println("2: View Superstar Player(s) in your " + tableName);
            System.out.println("3: View Non-Superstar Player(s) in your " + tableName);
            
            switch(tableName) {
                case "SAN_ANTONIO" -> System.out.println("4: Return to menu page");
                case "CANDIDATE_LIST" -> System.out.println("4: Ready to add player (PLEASE view the data of player before you add palyer)");
            }            
            
            String choiceRoster = sc.next();
            
            switch (choiceRoster) {
                case "1" -> viewSuperstar(tableName, ">= 0", 1);
                case "2" -> viewSuperstar(tableName, "= 3000", 2);
                case "3" -> viewSuperstar(tableName, "= 1000", 3);
                case "4" -> {
                    switch(tableName) {
                        case "SAN_ANTONIO" -> {
                            System.out.println("\nYou will be redirected to roster menu.");
                            teamMenu();
                        }
                        case "CANDIDATE_LIST" -> {
                            choosePlayerAdd();
                        }
                    }
                }
                default -> {            
                    System.out.println("\nUnknow input. Only '1' or '2' or '3' or '4' is acceptable!");
                    System.out.println("You will be redirected to menu page.");
                    teamMenu();
                }
            }
        }
        
        //View All, Superstar and non-Superstar player (Combine method to be used in viewRoster(San Antonio) and addPlayer(Candidate List)
        public void viewSuperstar(String tableName, String salary, int choiceSuperstar) {
            try {
                sc.useDelimiter("\n");
                
                String superstar = null;
                
                Statement stmt = con.createStatement();                                
                ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + " WHERE Salary " + salary);                                
                
                switch(choiceSuperstar) {
                    case 1 -> superstar = " ";
                    case 2 -> superstar = " Superstar ";
                    case 3 -> superstar = " Non-Superstar ";
                    default -> System.out.println("Impossible of having number other than '1' or '2' or '3'. Just Ignored");
                }                                
                
                if(rs.next()) {
                    System.out.println("\nBelow is all the" + superstar + "Player(s) in your " + tableName + ".");
                    
                    handleTableName(tableName, rs);
                    
                    System.out.println("\nFinish Viewing?");
                    System.out.println("Enter (1 / 2) to proceed to next step.");
                    System.out.println("1: Return to view selection page");                                        
                    
                    //Recall this method to view another selection or add player
                    switch(tableName) {
                        case "SAN_ANTONIO" -> {
                            System.out.println("2: Return to menu page");
                        }
                        case "CANDIDATE_LIST" -> {
                            System.out.println("2: Proceed to add player");
                        }
                        default -> System.out.println("Impossible of having number other than '1' or '2' or '3'. Just Ignored");
                    }
                    
                    String viewSuperstar = sc.next();
                    
                    switch(viewSuperstar) {
                        case "1" -> viewRosterOrList(tableName);
                        case "2" -> {
                            switch(tableName) {
                                case "SAN_ANTONIO" -> {
                                    System.out.println("\nYou will be redirected to menu page.");
                                    teamMenu();
                                }
                                case "CANDIDATE_LIST" -> {
                                    choosePlayerAdd();
                                }
                            }                            
                        }
                        default -> {
                            System.out.println("Unknown Input. You will be redirected to menu page!");
                            teamMenu();
                        }
                    }
                } else {
                    System.out.println("\nThere is no available" + superstar + "Player data in " + tableName + ".");
                    
                    if(tableName.equalsIgnoreCase("SAN_ANTONIO")) {
                        System.out.println("You should add a potential player into your roster first.");
                    }                    
                    
                    System.out.println("You will be redirected to menu page.");
                    teamMenu();
                }                                
                
            } catch (SQLException ex) {
                Logger.getLogger(NBA_SAN_ANTONIO_2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }       
        
        //Method to handle "SAN_ANTONIO" and "CANDIDATE_LIST"
        public void handleTableName(String tableName, ResultSet rs) {
            try {
                int maxNameLength = getMaxNameLength(tableName);

                switch(tableName) {
                    case "SAN_ANTONIO" -> {
                        playerSanAntonioColumnName(maxNameLength);
                        
                        do {
                            playerSanAntonio(rs, maxNameLength);
                        } while(rs.next());
                    }
                    case "CANDIDATE_LIST" -> {
                        viewCandidateListColumnName(maxNameLength);

                        do {
                            viewCandidateList(rs, maxNameLength);
                        } while(rs.next());
                    }
                    default -> {
                        System.out.println("Impossible of having number other than '1' or '2' or '3'. Just Ignored.");
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(NBA_SAN_ANTONIO_2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //Accept to view candidate list before choose player
        public void acceptViewCandidateList() {
            sc.useDelimiter("\n");
            
            try {                
                System.out.println("\nWelcome to choose your roster member.");
                System.out.println("Read below info carefully...");
                System.out.println("\nYour roster must consist of a minimum of 10 players and maximum of 15 players.");
                System.out.println("Your roster have " + getCurrent("COUNT(Player_ID)", "TotalPlayer") + " player(s) now.");
                System.out.println("\nYour roster must have at least 2 Guard, Forward, Centre.");
                displayEachPosition();
                System.out.println("\nYour roster total salary should not exceed 20000.");
                System.out.println("Your roster current total salary is " + getCurrent("SUM(Salary)", "TotalSalary"));
                
                //Check whether salary reach limit
                if(getCurrent("SUM(Salary)", "TotalSalary") <= salaryMax) {
                    
                    //Check whether number of player reach limit
                    if(getCurrent("COUNT(Player_ID)", "TotalPlayer") < playerMax) {
                        Statement stmt = con.createStatement();

                        ResultSet rs = stmt.executeQuery("SELECT * FROM Candidate_List");

                        if(rs.next()) {
                            System.out.println("\nBefore you choose a player, you are encouraged to analyze players from the candidate list first to make a best choice in choosing best players.");                    
                            System.out.println("Enter (1 / 2) to proceed to next step.");
                            System.out.println("1: View Candidate List");
                            System.out.println("2: Return to menu page");
                            String choiceView = sc.next();

                            switch (choiceView) {
                                case "1" -> viewRosterOrList("CANDIDATE_LIST");
                                case "2" -> {
                                    System.out.println("\nYou will be redirected to menu page");
                                    teamMenu();
                                }
                                default -> {
                                    System.out.println("\nInvalid input. Only '1' or '2' is acceptable.");                                                
                                    System.out.println("You need to start over at this step.");
                                    acceptViewCandidateList();
                                }
                            }
                        } else {
                            System.out.println("\nThere is no available player data in Candidate List.");
                            System.out.println("Please try to import candidate into your candidate list.");
                            System.out.println("You will be redirected to menu page.");

                            teamMenu();
                        }
                    } else {
                        elseTeamFull();
                    }
                } else {
                    elseSalaryFull();
                }                                                                                          
                
            } catch (SQLException ex) {
                Logger.getLogger(NBA_SQL_2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //Else method when player in team is full
        public void elseTeamFull() {
            sc.useDelimiter("\n");
            
            System.out.println("\nYour roster have " + getCurrent("COUNT(Player_ID)", "TotalPlayer") + " player(s) now.");            
            System.out.println("It reach the maximum number of players: " + playerMax);
            System.out.println("You are not allowed to add more player!");
            System.out.println("\nEnter (1 / 2) to proceed to next step.");
            System.out.println("1: Remove player if you want to add player");
            System.out.println("2: Return to menu page");
            String choicePlayerFull = sc.next();
            
            switch(choicePlayerFull) {
                case "1" -> beforeRemovePlayer();
                case "2" -> {
                    System.out.println("\nYou will be redirected to menu page");
                    teamMenu();
                }                    
                default -> {
                    System.out.println("\nUnknown input. Only '1' or '2' is acceptable!");
                    System.out.println("You will be redirected to menu page");
                    teamMenu();
                }
            }
        }
        
        //Else method when salary is reach the max
        public void elseSalaryFull() {
            System.out.println("\nYour roster current total salary: " + getCurrent("SUM(Salary)", "TotalSalary"));
            System.out.println("Total salary cap: " + salaryMax);
            System.out.println("Please remove player if you want to add this player.");
            System.out.println("\nEnter (1 / 2) to proceed to next step.");
            System.out.println("1: Remove player");
            System.out.println("2: Return to menu page");
            String salaryFull = sc.next();
            
            switch(salaryFull) {
                case "1" -> beforeRemovePlayer();
                case "2" -> {
                    System.out.println("You will be redirected to menu page.");
                    teamMenu();
                }
                default -> {
                    System.out.println("You will be redirected to menu page.");
                    teamMenu();
                }
            }                    
        }
        
        //Choose player to add into team
        public void choosePlayerAdd() {
            try {
                sc.useDelimiter("\n");

                int id = 0, age = 0, weight = 0, salary = 0;
                double points, rebounds, assists, steals, blocks;
                String name = null, height = null, position = null;
                
                //Check whether salary reach limit
                if(getCurrent("SUM(Salary)", "TotalSalary") <= salaryMax) {
                    
                    //Check whether the number of player reach limit
                    if(getCurrent("COUNT(Player_ID)", "TotalPlayer") < playerMax) {                        
                        System.out.println("\nYour roster have " + getCurrent("COUNT(Player_ID)", "TotalPlayer") + " player(s) now.");
                        displayEachPosition();
                        System.out.println("\nYour roster current total salary is " + getCurrent("SUM(Salary)", "TotalSalary"));
                        System.out.println("\nIf you are READY, enter the potential player's name you want to insert into your roster: ");
                        String nameEnter = sc.next();
                        
                        PreparedStatement pstmt = con.prepareStatement("SELECT * FROM candidate_list where Player_Name = ?");
                        pstmt.setString(1, nameEnter);
                        
                        ResultSet rs2 = pstmt.executeQuery();
                        
                        if(rs2.next()) {
                            System.out.println("\nThese are the data and stats of the player you chose from CANDIDATE LIST:");
                            int nameLength = nameEnter.length();
                            
                            viewCandidateListColumnName(nameLength);
                            
                            id = rs2.getInt("Player_ID");
                            name = rs2.getString("Player_Name");
                            age = rs2.getInt("Age");
                            height = rs2.getString("Height");
                            weight = rs2.getInt("Weight");
                            position = rs2.getString("Position");
                            salary = rs2.getInt("Salary");
                            points = rs2.getDouble("Points");
                            rebounds = rs2.getDouble("Rebounds");
                            assists = rs2.getDouble("Assists");
                            steals = rs2.getDouble("Steals");
                            blocks = rs2.getDouble("Blocks");
                            
                            System.out.printf("| %9s | %" + nameLength + "s | %3s | %6s | %6s | %8s | %6s | %6s | %8s | %7s | %6s | %6s |", id, name, age, height, weight, position, salary, points, rebounds, assists, steals, blocks);
                            System.out.println("\n----------------------------------------------------------------------------------------------------------------------------------");
                            
                            System.out.println("\nAre you sure you want to add this player into your current roster? (Yes / No)");
                            String choiceChoose = sc.next();
                            
                            if(choiceChoose.equalsIgnoreCase("Yes")) {

                                //Check whether this player duplicate
                                if(!checkPlayerDuplicate(name)) {
    
                                    //Check whether current total salary + this salary exceed limit
                                    if(getCurrent("SUM(Salary)", "TotalSalary") + salary <= 20000) {
    
                                        //Check whether the player is swingman (can play 2 position)
                                        if(position.length() > 1) {
                                            String[] twoPosition = position.split("-");
                                            System.out.println("\nAttention! The player you selected: " + name + " can play two positions.");
                                            System.out.println(twoPosition[0] + " and " + twoPosition[1]);
                                            System.out.println("Please assign only a suitable position for the player you selected.");
                                            String positionUpdate = sc.next();
                                            
                                            if(positionUpdate.equalsIgnoreCase(twoPosition[0])) {
                                                position = positionUpdate;
                                            } else if(positionUpdate.equalsIgnoreCase(twoPosition[1])) {
                                                position = positionUpdate;
                                            } else {
                                                System.out.println("Unknown input. Please try reselect the player again!");
                                                choosePlayerAdd();
                                            }
                                        }

                                        //If he is not swingman, just call next function (Check position)
                                        checkPositionBeforeAdd(id, name, age, height, weight, position, salary);
                                    } else {
                                        System.out.println("\nYour roster total salary will reach " + (getCurrent("SUM(Salary)", "TotalSalary") + salary) + " if you add this player: " + name + ".");
                                        elseSalaryFull();
                                    }
                                } else {
                                    System.out.println("\nThe player you selected: " + name + " is existing in your roster already.");
                                    System.out.println("Please choose another player.");
                                    System.out.println("You will be redirected to choose player page again.");
                                    acceptViewCandidateList();
                                }
                            } else if(choiceChoose.equalsIgnoreCase("No")) {
                                System.out.println("\nDo you want to choose a player again? (Yes / No)");
                                System.out.println("Yes: Choose potential player again");
                                System.out.println("No: Return to menu page");
                                String choiceChooseNo = sc.next();
                                
                                if(choiceChooseNo.equalsIgnoreCase("Yes")) {
                                    choosePlayerAdd();
                                } else if(choiceChooseNo.equalsIgnoreCase("No")) {
                                    System.out.println("\nYou will be redirected to menu page.");
                                    teamMenu();
                                } else {
                                    System.out.println("\nPlease enter correct values! Only 'Yes' or 'No' input is acceptable!");
                                    System.out.println("You will be redirected to menu page.");
                                    teamMenu();
                                }
                            } else {
                                System.out.println("\nPlease try again! Only 'Yes' or 'No' input is acceptable!");
                                choosePlayerAdd();
                            }
                        } else {
                            System.out.println("\nThere is no avaiable player data in CANDIDATE LIST.");
                            System.out.println("Please try again.");
                            choosePlayerAdd();
                        }
                    } else {
                        elseTeamFull();
                    }
                } else {
                    elseSalaryFull();
                }                                                                                                                                                                                                                                                                       
                
            } catch (SQLException ex) {
                Logger.getLogger(NBA_SAN_ANTONIO_2.class.getName()).log(Level.SEVERE, null, ex);
            }        
        }        
        
        //Check if position meet requirement before adding player when reach 6 players
        public void checkPositionBeforeAdd(int id, String name, int age, String height, int weight, String position, int salary) {
            if(getCurrent("COUNT(Player_ID)", "TotalPlayer") >= 6) {
                    if(booleanPlayerPosition()) {
                        addPlayer(id, name, age, height, weight, position, salary);
                    } else {                                                              
                        for(int i=0; i<positionArray.length; i++) {
                            if(positionArray[i].equalsIgnoreCase(position)) {
                                if(2 - positionNumber[i] > 0) {
                                    addPlayer(id, name, age, height, weight, position, salary);
                                } else {
                                    System.out.println("\nYou should not add this player as this position: '" + position + "' already meets the requirement.");
                                    System.out.println("You can add this player again after you meet the other position requirements."); 
                                    System.out.println("\nYou will be redirected to add player page.");
                                    acceptViewCandidateList();
                                }
                            } else {
                                System.out.println("Impossible to have error at this part. Check the array.");
                            }
                        }
                    }
                } else {
                    addPlayer(id, name, age, height, weight, position, salary);
            }
        }

        //Add player into team
        public void addPlayer(int id, String name, int age, String height, int weight, String position, int salary) {                        
            try {                                                                
                System.out.println("\nAdding the selected player " + name + " into your roster...");
                
                PreparedStatement stmt = con.prepareStatement("INSERT INTO SAN_ANTONIO VALUES(?, ?, ?, ?, ?, ?, ?);");
                stmt.setInt(1, id);
                stmt.setString(2, name);
                stmt.setInt(3, age);
                stmt.setString(4, height);
                stmt.setInt(5, weight);
                stmt.setString(6, position.toUpperCase());
                stmt.setInt(7, salary);
                
                stmt.executeUpdate();                                
                                
                System.out.println("\nCongratulations on successfully recruiting a powerful player into your roster 'San_Antonio'");
                System.out.println("Your roster have " + getCurrent("COUNT(Player_ID)", "TotalPlayer") + " player(s) now.");
                displayEachPosition();
                
                System.out.println("\nYour roster current total salary is " + getCurrent("SUM(Salary)", "TotalSalary"));
                
                if(getCurrent("COUNT(Player_ID)", "TotalPlayer") == playerMax) {
                    elseTeamFull();
                } else {
                    System.out.println("\nDo you want to continue adding player into your roster?");
                    System.out.println("Enter (1 / 2) to proceed to next step.");
                    System.out.println("1: Continue adding player");
                    System.out.println("2: Return to menu page");
                    String continueAdd = sc.next();

                    switch(continueAdd) {
                        case "1" -> {
                            if(getCurrent("SUM(Salary)", "TotalSalary") >= 20000) {
                                elseSalaryFull();
                            } else {
                                viewRosterOrList("CANDIDATE_LIST");
                            }                            
                        }
                        case "2" -> {
                            System.out.println("You will be redirected to menu page.");
                            teamMenu();
                        }
                        default -> {
                            System.out.println("You will be redirected to menu page.");
                            teamMenu();
                        }
                    }
                }                                
                                
            } catch (SQLException ex) {
                Logger.getLogger(NBA_SQL_2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //Check whether player exceed min limit before remove player
        public void beforeRemovePlayer() {     
            sc.useDelimiter("\n");
            
            System.out.println("\nWelcome to remove player from your roster.");
            System.out.println("We are encouraged you to kick off the stupid player from your roster as he will affect your road to NBA championship.");
            System.out.println("\nYour roster have " + getCurrent("COUNT(Player_ID)", "TotalPlayer") + " player(s) now.");
            displayEachPosition();
            System.out.println("\nYour roster current total salary is " + getCurrent("SUM(Salary)", "TotalSalary"));
            
            if(getCurrent("COUNT(Player_ID)", "TotalPlayer") <= playerMin) {                                
                handleRemoveSmaller10();
            } else {
                choosePlayerRemove();
            }
        }
        
        //Display error if want to remove player when player number <= 10
        public void handleRemoveSmaller10() {
            System.out.println("\nYou should not remove player from your roster as you only left " + getCurrent("COUNT(Player_ID)", "TotalPlayer") + " players now.");
            System.out.println("You should have a minimum of 10 players in your roster.");
            System.out.println("\nEnter (1 / 2) to proceed to next step.");
            System.out.println("1: Add player");
            System.out.println("2: Return to menu page");
            String beforeRemove = sc.next();
            
            switch(beforeRemove) {
                case "1" -> viewRosterOrList("CANDIDATE_LIST");
                case "2" -> {
                    System.out.println("\nYou will be redirected to menu page.");
                    teamMenu();
                }
                default -> {
                    System.out.println("\nUnknown input. Only '1' or '2' is acceptable!");
                    System.out.println("\nYou will be redirected to menu page.");
                    teamMenu();
                }
            }
        }

        //Choose player to remove
        public void choosePlayerRemove() {
            sc.useDelimiter("\n");
            
            try {                                
                if(getCurrent("COUNT(Player_ID)", "TotalPlayer") <= playerMin) {                                
                    handleRemoveSmaller10();
                } else {
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM SAN_ANTONIO");

                    if(rs.next()) {
                        System.out.println("\nBelow is your current roster member(s):");

                        int maxNameLength = getMaxNameLength("SAN_ANTONIO");

                        playerSanAntonioColumnName(maxNameLength);                    

                        do {
                            playerSanAntonio(rs, maxNameLength);
                        } while(rs.next());

                        System.out.println("\nYour roster have " + getCurrent("COUNT(Player_ID)", "TotalPlayer") + " player(s) now.");
                        displayEachPosition();
                        System.out.println("\nYour roster current total salary is " + getCurrent("SUM(Salary)", "TotalSalary"));
                        System.out.println("\nEnter the player's name you want to remove from your SAN ANTONIO roster: ");
                        String name = sc.next();

                        PreparedStatement pstmt = con.prepareStatement("SELECT * FROM SAN_ANTONIO WHERE Player_Name = ?");
                        pstmt.setString(1, name);

                        ResultSet rs2 = pstmt.executeQuery();                                

                        if(rs2.next()) {
                            System.out.println("\nThese are the data of the player you chose from your Championship SAN ANTONIO roster:");

                            int nameLength = name.length();

                            playerSanAntonioColumnName(nameLength);
                            playerSanAntonio(rs2, nameLength);

                            String position = rs2.getString("Position");

                            System.out.println("\nAre you sure you want to remove this player from SAN ANTONIO roster? (Yes / No)");
                            String choiceRemove = sc.next();

                            if(choiceRemove.equalsIgnoreCase("Yes")) {
                                checkPositionBeforeRemove(position, name);
                            } else if(choiceRemove.equalsIgnoreCase("No")) {
                                System.out.println("\nHmm... It seems you have changed your mind.");
                                System.out.println("Do you want to reselect and remove player from SAN ANTONIO roster? (Yes / No)");
                                System.out.println("Yes: Remove player");
                                System.out.println("No: Return to menu page");
                                String choiceRemoveNo = sc.next();

                                if(choiceRemoveNo.equalsIgnoreCase("Yes")) {
                                    choosePlayerRemove();
                                } else if(choiceRemoveNo.equalsIgnoreCase("No")) {
                                    System.out.println("\nYou will be redirected to menu page.");
                                    teamMenu();
                                } else {
                                    System.out.println("\nPlease enter correct value. Only 'Yes' or 'No' input is acceptable!.");
                                    System.out.println("You will be redirected to menu page.");
                                    teamMenu();
                                }
                            } else {
                                System.out.println("\nPlease try again! Only 'Yes' or 'No' input is acceptable!");
                                choosePlayerRemove();
                            }
                        } else {
                            System.out.println("\nThere is no availalbe player data in SAN ANTONIO roster.");
                            System.out.println("You will be redirected to menu page.");
                            teamMenu();
                        }                    
                    } else {
                        System.out.println("\nYour roster have " + getCurrent("COUNT(Player_ID)", "TotalPlayer") + " player(s) now.");
                        System.out.println("There is no available player data in SAN ANTONIO roster.");
                        System.out.println("You should add a potential player into your roster first.");
                        System.out.println("You will be redirected to menu page.");
                        teamMenu();
                    }
                }                                                

            } catch (SQLException ex) {
                Logger.getLogger(NBA_SQL_2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //Check if position meet requirement before removing player when player number reach 10
        public void checkPositionBeforeRemove(String position, String name) {
            sc.useDelimiter("\n");
            
            if(getCurrent("COUNT(Player_ID)", "TotalPlayer") <= 10) {
                for(int i=0; i<positionArray.length; i++) {
                    if(positionArray[i].equalsIgnoreCase(position)) {
                        if(positionNumber[i] - 1 < 2) {
                            System.out.println("\nAre you sure you want to remove this player with position: '" + positionArray[i] + "'?");
                            System.out.println("You will only left " + (positionNumber[i] - 1) + " player with this position: " + positionArray[i] + " if you remove him.");
                            System.out.println("\nEnter (1 / 2) to proceed to next step.");
                            System.out.println("1: Remove this player and replace another player later");
                            System.out.println("2: Return to menu page");
                            String beforeRemove = sc.next();
                            
                            switch(beforeRemove) {
                                case "1" -> removePlayer(name);
                                case "2" -> {
                                    System.out.println("You will be redirected to menu page.");
                                    teamMenu();
                                }
                                default -> {
                                    System.out.println("Unknown input. Please try again!");
                                    choosePlayerRemove();
                                }
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
            sc.useDelimiter("\n");
            System.out.println("\nRemoving the selected player " + name + "  from your roster...");
            
            try {
                PreparedStatement pstmt = con.prepareStatement("DELETE FROM SAN_ANTONIO WHERE Player_Name = ?");
                pstmt.setString(1, name);
                pstmt.executeUpdate();                                
                
                System.out.println("\nPlayer is removed successfully.");
                System.out.println("Hope you will find a stronger player in future!");
                System.out.println("\nYour roster have " + getCurrent("COUNT(Player_ID)", "TotalPlayer") + " player(s) now.");
                displayEachPosition();
                System.out.println("\nYour roster current total salary is " + getCurrent("SUM(Salary)", "TotalSalary"));
                
                System.out.println("\nDo you want to remove player again?");
                System.out.println("Enter (1 / 2) to proceed to next step.");
                System.out.println("1: Remove player");
                System.out.println("2: View your current roster");
                
                String choiceAfterRemove = sc.next();
                
                switch(choiceAfterRemove) {
                    case "1" -> choosePlayerRemove();
                    case "2" -> viewRosterOrList("SAN_ANTONIO");
                    default -> {
                        System.out.println("\nUnknown Input. Only '1' or '2' is acceptable.");
                        System.out.println("You will be redirected to menu page.");
                        teamMenu();
                    }
                }                                
                
            } catch (SQLException ex) {
                Logger.getLogger(NBA_SAN_ANTONIO_2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //Print column name of San Antonio
        public void playerSanAntonioColumnName(int nameLength) {
            System.out.println("\n-----------------------------------------------------------------------");
            System.out.printf("| %9s | %" + nameLength + "s | %3s | %6s | %6s | %8s | %6s |", "Player_ID", "Player_Name", "Age", "Height", "Weight", "Position", "Salary");
            System.out.println("\n-----------------------------------------------------------------------");
        }
        
        //Method to be used in viewRoster() and removePlayer()
        public void playerSanAntonio(ResultSet rs, int maxNameLength) {
            try {                                
                int id = rs.getInt("Player_ID");
                String name = rs.getString("Player_Name");                
                int age = rs.getInt("Age"); 
                String height = rs.getString("Height");
                int weight = rs.getInt("Weight");
                String position = rs.getString("Position");
                int salary = rs.getInt("Salary");
                      
               System.out.printf("| %9s | %" + maxNameLength + "s | %3s | %6s | %6s | %8s | %6s |", id, name, age, height, weight, position, salary);
               System.out.println("\n-----------------------------------------------------------------------");
                   
            } catch (SQLException ex) {
                Logger.getLogger(NBA_SAN_ANTONIO_2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //Print column name of candidates list
        public void viewCandidateListColumnName(int maxNameLength) {
            System.out.println("\n----------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("| %9s | %" + maxNameLength + "s | %3s | %6s | %6s | %8s | %6s | %6s | %8s | %7s | %6s | %6s |", "Player_ID", "Player_Name", "Age", "Height", "Weight", "Position", "Salary", "Points", "Rebounds", "Assists", "Steals", "Blocks");
            System.out.println("\n----------------------------------------------------------------------------------------------------------------------------------");
        }
        
        //Print data of player in candidates list
        public void viewCandidateList(ResultSet rs, int maxNameLength) {
            try {                                                                            
                int id = rs.getInt("Player_ID");
                String name = rs.getString("Player_Name");
                int age = rs.getInt("Age");
                String height = rs.getString("Height");
                int weight = rs.getInt("Weight");
                String position = rs.getString("Position");
                int salary = rs.getInt("Salary");
                double points = rs.getDouble("Points");
                double rebounds = rs.getDouble("Rebounds");
                double assists = rs.getDouble("Assists");
                double steals = rs.getDouble("Steals");
                double blocks = rs.getDouble("Blocks");
                
                System.out.printf("| %9s | %" + maxNameLength + "s | %3s | %6s | %6s | %8s | %6s | %6s | %8s | %7s | %6s | %6s |", id, name, age, height, weight, position, salary, points, rebounds, assists, steals, blocks);
                System.out.println("\n----------------------------------------------------------------------------------------------------------------------------------");
                
            } catch (SQLException ex) {
                Logger.getLogger(NBA_SAN_ANTONIO_2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //Calculate the max name length
        public int getMaxNameLength(String tableName) {
            int maxNameLength = 0;
            
            try {
                Statement stmt = con.createStatement();
                //Use java if this method is not allowed
                ResultSet rsLength = stmt.executeQuery("SELECT MAX(LENGTH(Player_Name)) AS maxNameLength FROM " + tableName);                                
                
                if(rsLength.next()) {
                    maxNameLength = rsLength.getInt("maxNameLength");
                } else {
                    System.out.println("No number found.");
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(NBA_SAN_ANTONIO_2.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return maxNameLength;
        }
        
        //Calculate current player(s) in roster / total salary (Multiuse Method)
        public int getCurrent(String function, String functionName) {
            int current = 0;
            
            try {
                if(con == null) {
                    current = 0;
                } else {
                    Statement stmt = con.createStatement();
                
                    ResultSet rs = stmt.executeQuery("SELECT " + function + " AS " + functionName + " FROM SAN_ANTONIO");
                    
                    if(rs.next()) {
                        current = rs.getInt(functionName);      
                    } else {
                        current = 0;
                    }
                }                
                
            } catch (SQLException ex) {
                Logger.getLogger(NBA_SAN_ANTONIO_2.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return current;
        }                
        
        //Get each position number
        public void getPlayerPosition() {                      
            int playerPosition = 0;
            
            if(con == null) {
                System.out.println("Connection loss.");
            } else {
                try {                                        
                    for(int i=0; i<positionArray.length; i++) {
                        PreparedStatement pstmt = con.prepareStatement("SELECT COUNT(Position) AS PositionCount FROM SAN_ANTONIO WHERE Position = ?");
                        pstmt.setString(1, positionArray[i]);
                        ResultSet rs = pstmt.executeQuery();

                        if(rs.next()) {
                            playerPosition = rs.getInt("PositionCount");
                            positionNumber[i] = playerPosition;
                        } else {
                            positionNumber[i] = 0;                            
                        }
                    }                                                            
                    
                } catch (SQLException ex) {
                    Logger.getLogger(NBA_SAN_ANTONIO_2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }                        
        }
        
        //Boolean for position number
        public boolean booleanPlayerPosition() {
            getPlayerPosition();
            
            int meet = 0;
            
            for(int i=0; i<positionArray.length; i++) {
                if(positionNumber[i] - 2 >= 0) {
                    meet++;
                } 
            }
            
            return meet == 3;
        }
        
        //Display if each position meet requirement
        public void displayEachPosition() {
            System.out.println("\nCurrent Roster Position:");
            if(booleanPlayerPosition()) {
                System.out.println("All positions meet the requirements.");
                
                for(int i=0; i<positionArray.length; i++) {
                    String player = positionNumber[i] > 1 ? " players" : " player";
                    System.out.println("Position " + positionArray[i] + ": " + positionNumber[i] + player);
                }
            } else {
                for(int i=0; i<positionArray.length; i++) {
                    String player = positionNumber[i] > 1 ? " players" : " player";
                    System.out.println("Position " + positionArray[i] + ": " + positionNumber[i] + player);
                    
                    if(positionNumber[i] - 2 < 0) {
                        System.out.println("Position " + positionArray[i] + " needs to add " + (2 - positionNumber[i]) + " player(s) to achive the requirement.");
                    } else {
                        System.out.println("Position " + positionArray[i] + " meets the requirement of 2 players.");
                    }  
                }
            }
        }
        
        //Method check whether player duplicate
        public boolean checkPlayerDuplicate(String name) {
            try {
                PreparedStatement pstmt = con.prepareStatement("SELECT * FROM SAN_ANTONIO WHERE Player_Name = ?");
                pstmt.setString(1, name);
                ResultSet rs = pstmt.executeQuery();
                
                return rs.next();
                
            } catch (SQLException ex) {
                Logger.getLogger(NBA_SAN_ANTONIO_2.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return false;
        }
}
