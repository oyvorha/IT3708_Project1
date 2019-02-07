import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Visualization {

    private final static int SCREEN_WIDTH = 800;
    private final static int SCREEN_HEIGHT = 800;


    public void visualize(Map<Route, List<Node>> routes, double cost, double optimalCost) {
        JFrame frame = new JFrame();

        JLabel label = new JLabel();
        label.setText("Total route cost: " + cost + "   ----  Benchmark solution has cost: " + optimalCost);

        Painter painter = new Painter(routes);
        painter.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

        frame.add(painter, BorderLayout.CENTER);
        frame.add(label, BorderLayout.NORTH);

        frame.setLocation(150, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // EDIT
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }



    public class Painter extends JPanel {

        private static final long serialVersionUID = 5;

        private final Color[] colors = new Color[]{Color.BLACK, Color.BLUE, Color.RED, Color.DARK_GRAY, Color.MAGENTA, Color.PINK, Color.GRAY, Color.ORANGE, Color.GREEN, Color.RED};
        private final Color depotColor = Color.BLACK;
        private final static int SIZE = 10;


        private Map<Route, List<Node>> routes;

        Painter(Map<Route, List<Node>> routes) {
            this.routes = routes;
        }

        @Override
        protected void paintComponent( Graphics g){
            final int scale = findScale(routes.values().stream().flatMap(List::stream).collect(Collectors.toList()));
            routes.forEach((v, c) -> {
                int index = v.getStartDepot().getDepotID();
                Color clr = colors[index]; // Fix so it fetches actual color
                // Draw depot
                Random r = new Random();
                int maxBound = 200;
                int red = Math.min(255, clr.getRed() + r.nextInt(maxBound));
                int green = Math.min(255, clr.getGreen() + r.nextInt(maxBound));
                int blue = Math.min(255, clr.getBlue() + r.nextInt(maxBound));
                int alpha = Math.max(0, clr.getAlpha() - r.nextInt(maxBound / 2));

                Color color = new Color(red, green, blue);
                int depotX = (int) v.getStartDepot().getCoordinate().getX() * scale;
                int depotY = (int) v.getStartDepot().getCoordinate().getY() * scale;
                g.setColor(depotColor);
                g.fillRect(depotX, depotY, SIZE, SIZE);

                g.setColor(color);
                int offset = SIZE / 2;

                Coordinate lastCoord = c.get(0).getCoordinate();
                for (Node node : c.subList(1, c.size())) {
                    Coordinate coord = node.getCoordinate();

                    int x = coord.getX() * scale;
                    int y = coord.getY() * scale;
                    int lastX = lastCoord.getX() * scale;
                    int lastY = lastCoord.getY() * scale;

                    g.drawLine(x + offset, y + offset, lastX + offset, lastY + offset);
                    // If we're on last element, don't draw (it's a depot!)
                    if (node instanceof Customer) {
                        g.fillOval(x, y, SIZE, SIZE);
                    }

                    lastCoord = coord;
                }
            });
        }
    }

    private int findScale(List<Node> routes) {
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        List<Coordinate> allCoords = routes.stream().map(c -> c.getCoordinate()).collect(Collectors.toList());
        allCoords.addAll(routes.stream().map(d -> d.getCoordinate()).collect(Collectors.toList()));

        for (Coordinate c : allCoords ) {
            minX = Math.min(minX, (int) c.getX());
            maxX = Math.max(maxX, (int) c.getX());
            minY = Math.min(minY, (int) c.getY());
            maxY = Math.max(maxY, (int) c.getY());
        }
        // Not actually needed, as we assume minX/Y is 0. Maybe nice to have?
        int xDifference = maxX - minX;
        int yDifference = maxY - minY;

        int greatestDifference = Math.max(maxX, maxY);
        System.out.println(greatestDifference);
        return Math.floorDiv(Math.min(SCREEN_HEIGHT, SCREEN_WIDTH), greatestDifference);

    }


}