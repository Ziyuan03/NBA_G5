package nba;


public class NBA_MAIN {
    public static void main(String[] args) {
        NBA_SQL ns = new NBA_SQL();       
        //NBA_API na = new NBA_API("https://api.balldontlie.io/v1/players?per_page=100", "e1125c55-e453-4b15-afcd-1d3742e0f2a3");
        
//        for(int i=91; i<=100; i++) {
//            NBA_API na = new NBA_API("https://api.balldontlie.io/v1/season_averages?season=2023&player_ids[]=" + i, "e1125c55-e453-4b15-afcd-1d3742e0f2a3");
//            na.getAverageStats(i);
//        }

        //NBA_SAN_ANTONIO ntsa = new NBA_SAN_ANTONIO();
        ns.createTable();
        ns.createConnection();
        //ns.createAllPlayersTable();
        //ns.createAverageStatsTable();
        //ns.createTriggger();
//        ns.createView();
//       ns.createSanAntonioTable();
        
        //na.getAllPlayersData();

       //                                     
    }
    
}

/* Draft Roster
Point Guard:        Damian Lillard, Tyrese Haliburton, John Murant
Shooting Guard:  Stephen Curry, Luka Doncic
Small Forward:    Lebron James, Paul George
Power Forward:  Giannis Antetokounmpo, Kevin Durant, Zion Williamsom
Center:               Nikola Jokic, Joel Embiid, Victor Wembanyama
*/