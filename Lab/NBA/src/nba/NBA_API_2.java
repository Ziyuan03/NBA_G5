package nba;

import java.io.IOException;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class NBA_API_2 {
    private String URL, token;
    Connection con;
    
    public NBA_API_2(String URL, String token) {
        this.URL = URL;
        this.token = token;
    }
    
    public JSONArray fetchNBAData() {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .header("Authorization", token)
                    .build();
            
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            
            JSONParser parser = new JSONParser();
            JSONObject jsonResponse = (JSONObject) parser.parse(httpResponse.body());
            
            JSONArray jsonArray = (JSONArray) jsonResponse.get("data");
            
            return jsonArray;
            
        } catch (IOException | InterruptedException | ParseException ex) {
            Logger.getLogger(NBA_API_2.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public void getAllPlayersData() {
        try {
            JSONArray nbaData = fetchNBAData();
            
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nba", "root", "root123NBA.");
            
            PreparedStatement ps = con.prepareStatement("INSERT INTO ALLPLAYERS VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            
            for(Object obj : nbaData) {
                JSONObject player = (JSONObject) obj;
                JSONObject team = (JSONObject) player.get("team");
                
                ps.setObject(1, player.get("id"));
                ps.setObject(2, player.get("first_name"));
                ps.setObject(3, player.get("last_name"));
                ps.setObject(4, null);
                ps.setObject(5, player.get("position"));
                ps.setObject(6, player.get("height"));
                ps.setObject(7, player.get("weight"));
                ps.setObject(8, player.get("jersey_number"));
                ps.setObject(9, player.get("college"));
                ps.setObject(10, player.get("country"));
                ps.setObject(11, player.get("draft_year"));
                ps.setObject(12, player.get("draft_round"));
                ps.setObject(13, player.get("draft_number"));
                
                ps.setObject(14, team.get("id"));
                ps.setObject(15, team.get("conference"));
                ps.setObject(16, team.get("division"));
                ps.setObject(17, team.get("city"));
                ps.setObject(18, team.get("name"));
                ps.setObject(19, team.get("full_name"));
                ps.setObject(20, team.get("abbreviation"));
                
                ps.addBatch();
            }
            
            ps.executeBatch();
            System.out.println("All Players Data Inserted");
            
        } catch (SQLException ex) {
            Logger.getLogger(NBA_API_2.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    public void getAverageStats(int i) {
        try {
            JSONArray nbaData = fetchNBAData();
            
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nba", "root", "root123NBA.");
            
            PreparedStatement ps = con.prepareStatement("INSERT INTO AVERAGESTATS VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            
            for(Object obj : nbaData) {
                JSONObject stats = (JSONObject) obj;                
                
                ps.setObject(1, stats.get("player_id"));
                ps.setObject(2, stats.get("season"));
                ps.setObject(3, null);
                ps.setObject(4, stats.get("pts"));
                ps.setObject(5, stats.get("ast"));
                ps.setObject(6, stats.get("stl"));
                ps.setObject(7, stats.get("blk"));
                ps.setObject(8, stats.get("reb"));
                ps.setObject(9, stats.get("games_played"));
                ps.setObject(10, stats.get("turnover"));
                ps.setObject(11, stats.get("pf"));
                ps.setObject(12, stats.get("fga"));
                ps.setObject(13, stats.get("fgm"));
                ps.setObject(14, stats.get("fta"));
                ps.setObject(15, stats.get("ftm"));
                ps.setObject(16, stats.get("fg3a"));
                ps.setObject(17, stats.get("fg3m"));
                ps.setObject(18, stats.get("oreb"));
                ps.setObject(19, stats.get("dreb"));
                ps.setObject(20, stats.get("fg_pct"));
                ps.setObject(21, stats.get("fg3_pct"));
                ps.setObject(22, stats.get("ft_pct"));
                ps.setObject(23, stats.get("min"));
                
                ps.addBatch();
                
            }
            
            ps.executeBatch();  
            System.out.println(nbaData);
            System.out.println("Average Stats Player " + i + " Inserted");
        } catch (SQLException ex) {
            Logger.getLogger(NBA_API_2.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
}
