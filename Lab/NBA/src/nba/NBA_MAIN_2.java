package nba;

public class NBA_MAIN_2 {
    public static void main(String[] args) {
        NBA_SQL_2 ns = new NBA_SQL_2();       
//        NBA_API na = new NBA_API("https://api.balldontlie.io/v1/players?per_page=100", "e1125c55-e453-4b15-afcd-1d3742e0f2a3");
        
//        for(int i=61; i<=70; i++) {
//            NBA_API na = new NBA_API("https://api.balldontlie.io/v1/season_averages?season=2023&player_ids[]=" + i, "e1125c55-e453-4b15-afcd-1d3742e0f2a3");
//            na.getAverageStats(i);
//        }

        NBA_SAN_ANTONIO_2 ntsa = new NBA_SAN_ANTONIO_2();
        
//        ns.createConnection();
//        ns.createAllPlayersTable();
//        ns.createAverageStatsTable();
//        ns.createTriggger();
//        ns.createView();
//       ns.createSanAntonioTable();
        
        //na.getAllPlayersData();

       ntsa.teamStart();                                         
    }
    
}

/* Draft Roster
Point Guard:        Damian Lillard, Tyrese Haliburton, John Murant
Shooting Guard:  Stephen Curry, Luka Doncic
Small Forward:    Lebron James, Paul George
Power Forward:  Giannis Antetokounmpo, Kevin Durant, Zion Williamsom
Center:               Nikola Jokic, Joel Embiid, Victor Wembanyama
*/