import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Race {
    static int width = 1200;
    static int height = 600;
    static boolean stop = false;
    static int winner;
    public static JFrame mainFrame;
    public static void main(String[] args) throws IOException {
        mainFrame = new JFrame("race");

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame.setPreferredSize(new Dimension(Race.width, Race.height));
        mainFrame.setLocation((d.width - Race.width) / 2, (d.height - Race.height) / 2);
        mainFrame.getContentPane().setLayout(null);
        mainFrame.getContentPane().setBackground(Color.orange);

        mainFrame.pack();

        mainFrame.setVisible(true);

        CarLabel[] carLabels = new CarLabel[5];
        int h = Race.mainFrame.getContentPane().getHeight() / carLabels.length;
        for (int i = 0; i < carLabels.length; i++) {
            carLabels[i] = new CarLabel(ImageIO.read(new File("car.png")), i + 1);
            mainFrame.add(carLabels[i]);
            carLabels[i].setPosition(0, i * h + (h - carLabels[i].image.getHeight()) / 2);
        }

        try {
            Thread.sleep(300);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }

        for (CarLabel carLabel : carLabels) {
            (new RaceCar(carLabel)).start();
        }

        (new Timer(50, new CheckStopListener())).start();
    }
}

class RaceCar extends Thread {
    CarLabel car;

    public RaceCar(CarLabel car) {
        this.car = car;
    }

    @Override
    public void run() {
        super.run();

        int diff;
        while (!Race.stop) {
            Rectangle d = car.getBounds();

            diff = (Race.mainFrame.getContentPane().getWidth() - 5) - (d.x + d.width);

            car.distance = (int) (Math.random() * 10 + 5);

            if (diff > car.distance) {
                d.x += car.distance;
                car.setBounds(d);
            } else {
                d.x += diff;
                car.setBounds(d);
                Race.winner = car.number;
                Race.stop = true;
                break;
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

class CarLabel extends JLabel {
    int number;
    int distance;
    BufferedImage image;
    public CarLabel(BufferedImage image, int number) {
        this.distance = (int) (Math.random() * 15 + 5);
        this.number = number;
        this.image = image;
        setIcon(new ImageIcon(image));
    }

    public void setPosition(int x, int y) {
        setBounds(x, y, image.getWidth(), image.getHeight());
    }
}

class CheckStopListener implements ActionListener {
    public CheckStopListener() {
        System.out.println("timer started");
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (Race.stop) {
            ((Timer) e.getSource()).stop();

            System.out.println("timer stopped");

            JLabel label = new JLabel();
            Race.mainFrame.add(label);
            label.setText(String.valueOf(Race.winner));
            label.setFont(new Font("Serif", Font.PLAIN, 60));
            label.setBounds(100, Race.mainFrame.getContentPane().getHeight() / 2 - 50,100,100);
            label.setBackground(Color.blue);
        }
    }
}
