/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package nba;

/**
 *
 * @author Ziyua
 */
public class ContractPlayer implements Comparable<ContractPlayer> {
    private String id;
    private String name;
    private int ranking;
    private double compositeScore;
    private String contractStatus;

    public ContractPlayer(String id, String name, int ranking, double compositeScore) {
        this.id = id;
        this.name = name;
        this.ranking = ranking;
        this.compositeScore = compositeScore;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRanking() {
        return ranking;
    }

    public double getCompositeScore() {
        return compositeScore;
    }
    
     public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    public int compareTo(ContractPlayer other) {
        return Integer.compare(this.ranking, other.ranking); // Higher ranking has higher priority
    }

    @Override
    public String toString() {
        return "Player{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", ranking=" + ranking +
                ", compositeScore=" + compositeScore +
                '}';
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
