package nba;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author hp
 */

import nba.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.geom.AffineTransform;

class GraphPanel extends JPanel {

    private Map<String, Point> nodes;
    private Map<String, Map<String, Integer>> edges;
    private List<String> optimalPath = new ArrayList<>();

    public GraphPanel() {
        nodes = new HashMap<>();
        edges = new HashMap<>();
        initializeGraph();
    }

    public Map<String, Map<String, Integer>> getGraph() {
        return new HashMap<>(edges);  // Return a copy or the actual map depending on your needs
    }

    private void initializeGraph() {
        double scaleFactor = 0.70;  // Reduce size to 50%

// Update node positions with scaled coordinates
        nodes.put("Warriors", new Point((int) (50 * scaleFactor), (int) (50 * scaleFactor)));
        nodes.put("Lakers", new Point((int) (30 * scaleFactor), (int) (300 * scaleFactor)));
        nodes.put("Nuggets", new Point((int) (300 * scaleFactor), (int) (80 * scaleFactor)));
        nodes.put("Celtics", new Point((int) (750 * scaleFactor), (int) (73 * scaleFactor)));
        nodes.put("Suns", new Point((int) (190 * scaleFactor), (int) (400 * scaleFactor)));
        nodes.put("Thunder", new Point((int) (305 * scaleFactor), (int) (290 * scaleFactor)));
        nodes.put("Spurs", new Point((int) (295 * scaleFactor), (int) (550 * scaleFactor)));
        nodes.put("Rockets", new Point((int) (550 * scaleFactor), (int) (275 * scaleFactor)));
        nodes.put("Magic", new Point((int) (640 * scaleFactor), (int) (460 * scaleFactor)));
        nodes.put("Heat", new Point((int) (700 * scaleFactor), (int) (600 * scaleFactor)));

        connect("Warriors", "Lakers", 554);
        connect("Warriors", "Thunder", 2214);
        connect("Warriors", "Nuggets", 1507);
        connect("Lakers", "Thunder", 1901);
        connect("Lakers", "Suns", 577);
        connect("Suns", "Spurs", 500);
        connect("Spurs", "Thunder", 678);
        connect("Spurs", "Rockets", 983);
        connect("Spurs", "Magic", 1137);
        connect("Rockets", "Thunder", 778);
        connect("Rockets", "Magic", 458);
        connect("Rockets", "Celtics", 2584);
        connect("Magic", "Heat", 268);
        connect("Heat", "Celtics", 3045);
        connect("Nuggets", "Celtics", 2845);
        connect("Nuggets", "Thunder", 942);

    }

    private void connect(String node1, String node2, int distance) {
        if (!edges.containsKey(node1)) {
            edges.put(node1, new HashMap<>());
        }
        edges.get(node1).put(node2, distance);
    }

    public void setOptimalPath(List<String> path) {
        this.optimalPath = path;
        repaint();  // Repaint to update the view with the new path
    }

    

    @Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;

    // Set rendering hints for better aesthetics
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Draw all edges
    float thickness = 2.0f;
    g2.setStroke(new BasicStroke(thickness));
    g2.setColor(Color.GRAY);
    for (Map.Entry<String, Map<String, Integer>> entry : edges.entrySet()) {
        Point p1 = nodes.get(entry.getKey());
        for (Map.Entry<String, Integer> edge : entry.getValue().entrySet()) {
            Point p2 = nodes.get(edge.getKey());
            if (p1 != null && p2 != null) {
                g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                g2.drawString(edge.getValue().toString(), (p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
            }
        }
    }

    // Draw all nodes
    g2.setColor(Color.GRAY);
    int nodeDiameter = 16;
    int nodeRadius = nodeDiameter / 2;
    for (Map.Entry<String, Point> entry : nodes.entrySet()) {
        Point pt = entry.getValue();
        g2.fillOval(pt.x - nodeRadius, pt.y - nodeRadius, nodeDiameter, nodeDiameter);
        g2.drawString(entry.getKey(), pt.x - nodeRadius, pt.y - nodeRadius + nodeDiameter + 5);
    }

    // Draw the optimal path with arrows
    g2.setColor(Color.RED);
    g2.setStroke(new BasicStroke(3.0f));
    for (int i = 0; i < optimalPath.size() - 1; i++) {
        String from = optimalPath.get(i);
        String to = optimalPath.get(i + 1);
        Point p1 = nodes.get(from);
        Point p2 = nodes.get(to);
        if (p1 != null && p2 != null) {
            drawArrow(g2, p1.x, p1.y, p2.x, p2.y);
        }
    }
}

// Method to draw an arrow between two points
private void drawArrow(Graphics2D g2, int x1, int y1, int x2, int y2) {
    int ARR_SIZE = 10;
    double dx = x2 - x1, dy = y2 - y1;
    double angle = Math.atan2(dy, dx);
    AffineTransform oldAT = g2.getTransform(); // Save the current transform

    g2.translate(x1, y1);
    g2.rotate(angle);
    g2.drawLine(0, 0, (int) Math.sqrt(dx*dx + dy*dy), 0);
    g2.fillPolygon(new int[] {(int) Math.sqrt(dx*dx + dy*dy), (int) Math.sqrt(dx*dx + dy*dy)-ARR_SIZE, (int) Math.sqrt(dx*dx + dy*dy)-ARR_SIZE, (int) Math.sqrt(dx*dx + dy*dy)},
                   new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);

    g2.setTransform(oldAT); // Restore the original transform
}
}