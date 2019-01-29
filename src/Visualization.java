import javax.swing.JFrame;
import java.awt.*;
import java.util.ArrayList;

public class Visualization extends Canvas {

    int r = 5;

    public static void main(String[] args){
        JFrame frame= new JFrame("Visualization of solution");
        Canvas canvas = new Visualization();
        canvas.setSize(400, 400);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
    }

    public void paint(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillOval(200-r, 100-r, r *2, r *2);
        g.fillOval(100-r, 100-r, r *2, r *2);
        g.drawLine(200, 100, 100, 100 );
    }


    public void paint(Graphics g, ArrayList<Coordinate> Coordinates) {
        for (int i=0; i < Coordinates.size(); i+=2) {
            g.fillOval(Coordinates.get(i).getX()-r, Coordinates.get(i).getY()-r, r*2, r*2);
            g.fillOval(Coordinates.get(i+1).getX()-r, Coordinates.get(i+1).getY()-r, r*2, r*2);
            g.drawLine(Coordinates.get(i).getX(), Coordinates.get(i).getY(), Coordinates.get(i+1).getX(),
                    Coordinates.get(i+1).getY());
        }
    }

}
