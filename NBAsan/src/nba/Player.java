/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package nba;

/**
 *
 * @author Ziyua
 */
public class Player implements Comparable<Player> {
    int Player_ID;
    String Player_Name;
    int Age;
    double Height;

    String Position;
    double Points;
    double Steals;
    double Blocks;
    double Assists;
    double Rebounds;
    String Injury_Status;
    double Composite_Score;
    
    public Player(int playerID, String playerName, 
                  String position, double points, double assists,
                  double steals, double rebounds, double blocks) {
        this.Player_ID = playerID;
        this.Player_Name = playerName;
        this.Position = position;
        this.Points = points;
        this.Steals = steals;
        this.Blocks = blocks;
        this.Assists = assists;
        this.Rebounds = rebounds;
        this.Composite_Score = calculateCompositeScore();
    }
    
    public int getPlayerID() {
        return Player_ID;
    }

    public String getPlayerName() {
        return Player_Name;
    }

    public int getAge() {
        return Age;
    }

    public double getHeight() {
        return Height;
    }

    public String getPosition() {
        return Position;
    }

    public double getPoints() {
        return Points;
    }

    public double getSteals() {
        return Steals;
    }

    public double getBlocks() {
        return Blocks;
    }

    public double getAssists() {
        return Assists;
    }

    public double getRebounds() {
        return Rebounds;
    }

    public String getInjuryStatus() {
        return Injury_Status;
    }

    public double getCompositeScore() {
        return Composite_Score;
    }
    
    public void setCompositeScore(double cs) {
         Composite_Score = cs;
    }

    private double calculateCompositeScore() {
        double score = 0.0;
        switch (this.Position) {
            case "G":
                score = Points * 0.30 + Assists * 0.30 + Steals * 0.20 + Rebounds * 0.10 + Blocks * 0.10;
                break;
            case "C":
                score = Points * 0.20 + Assists * 0.10 + Steals * 0.10 + Rebounds * 0.30 + Blocks * 0.30;
                break;
            case "F":
                score = Points * 0.25 + Assists * 0.20 + Steals * 0.15 + Rebounds * 0.25 + Blocks * 0.15;
                break;
        }
        return score;
    }

    @Override
    public int compareTo(Player other) {
        return Double.compare(other.Composite_Score, this.Composite_Score);  // For descending order
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
