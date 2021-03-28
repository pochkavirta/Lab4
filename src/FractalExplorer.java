import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class FractalExplorer {
    private int sizeDisplay;
    private JImageDisplay jImageDisplay;
    private FractalGenerator fractalGenerator;
    private Rectangle2D.Double rectangle2D;

    public FractalExplorer(int sizeDisplay) {
        this.sizeDisplay = sizeDisplay;
        this.fractalGenerator = new Mandelbrot();
        this.rectangle2D = new Rectangle2D.Double();
        this.fractalGenerator.getInitialRange(rectangle2D);
        this.jImageDisplay = new JImageDisplay(sizeDisplay, sizeDisplay);
    }

    public static void main(String[] args) {
        FractalExplorer displayExplorer = new FractalExplorer(800);
        displayExplorer.createAndShowGUI();
        displayExplorer.drawFractal();
    }

    private void createAndShowGUI() {
        jImageDisplay.setLayout(new BorderLayout());
        JFrame myframe = new JFrame("Запуск фрактала");
        myframe.add(jImageDisplay, BorderLayout.CENTER);
        JButton resetButton = new JButton("Сброс отображения");
        ActionListenerImpl handler = new ActionListenerImpl();
        resetButton.addActionListener(handler);
        myframe.add(resetButton, BorderLayout.SOUTH);
        MouseListener click = new MouseListener();
        jImageDisplay.addMouseListener(click);
        myframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myframe.pack();
        myframe.setVisible(true);
        myframe.setResizable(false);
    }

    private void drawFractal() {
        for (int x = 0; x < sizeDisplay; x++) {
            for (int y = 0; y < sizeDisplay; y++) {
                double xCoord = FractalGenerator.getCoord(rectangle2D.x,
                        rectangle2D.x + rectangle2D.width, sizeDisplay, x);
                double yCoord = FractalGenerator.getCoord(rectangle2D.y,
                        rectangle2D.y + rectangle2D.height, sizeDisplay, y);
                int iteration = fractalGenerator.numIterations(xCoord, yCoord);
                if (iteration == -1) {
                    jImageDisplay.drawPixel(x, y, 0);
                } else {
                    float hue = 0.7f + (float) iteration / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    jImageDisplay.drawPixel(x, y, rgbColor);
                }
            }
        }
        jImageDisplay.repaint();
    }

    public class ActionListenerImpl implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            fractalGenerator.getInitialRange(rectangle2D);
            drawFractal();
        }
    }

    public class MouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            double xCoord = FractalGenerator.getCoord(rectangle2D.x, rectangle2D.x + rectangle2D.width, sizeDisplay, x);
            int y = e.getY();
            double yCoord = FractalGenerator.getCoord(rectangle2D.y, rectangle2D.y + rectangle2D.height, sizeDisplay, y);
            fractalGenerator.recenterAndZoomRange(rectangle2D, xCoord, yCoord, 0.5);
            drawFractal();
        }
    }
}
