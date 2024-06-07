package nba;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author hp
 */
import java.util.*;

public class BranchAndBoundTSP {
    private static int minPathCost = Integer.MAX_VALUE;
    private static List<String> bestPath = null;

    public static String findShortestRoute(Map<String, Map<String, Integer>> graph, String start) {
        Set<String> visited = new HashSet<>();
        List<String> path = new ArrayList<>();
        path.add(start);
        visited.add(start);
        
        minPathCost = Integer.MAX_VALUE;
        bestPath = null;

        branchAndBound(graph, path, visited, 0);

        System.out.println("Minimum Path Cost: " + minPathCost);
        System.out.println("Best Path: " + bestPath);
        
        return "Minimum Path Cost: " + minPathCost + "\nBest Path: " + bestPath;
    }
    
    public static String getResultString() {
        
        System.out.println("Minimum Path Cost: " + minPathCost);
        System.out.println("Best Path: " + bestPath);
        
        return "Minimum Path Cost: " + minPathCost +"km "+"\nBest Path: " + bestPath;
    }
    
    public static List<String> getOptimalPath() {
    return bestPath;  // Make sure bestPath is the list of node names of the optimal path
}

    private static void branchAndBound(Map<String, Map<String, Integer>> graph, List<String> path, Set<String> visited, int currentCost) {
        if (path.size() == graph.size()) {
            // As it's an open TSP, we stop here without returning to the starting point
            if (currentCost < minPathCost) {
                minPathCost = currentCost;
                bestPath = new ArrayList<>(path);
            }
            return;
        }

        String currentCity = path.get(path.size() - 1);
        for (Map.Entry<String, Integer> neighbor : graph.get(currentCity).entrySet()) {
            if (!visited.contains(neighbor.getKey())) {
                int nextCost = currentCost + neighbor.getValue();
                if (nextCost < minPathCost) {  // Pruning step
                    visited.add(neighbor.getKey());
                    path.add(neighbor.getKey());
                    branchAndBound(graph, path, visited, nextCost);
                    visited.remove(neighbor.getKey());
                    path.remove(path.size() - 1);
                }
            }
        }
    }

    public static void main(String[] args) {
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

        findShortestRoute(graph, "Spurs");
        getResultString();
        
        
        
        
        
    }
}
