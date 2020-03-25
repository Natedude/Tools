package visual;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static javax.imageio.ImageIO.read;

public class Screen extends JPanel {

    public static final int SCREEN_WIDTH = 200;//1280;
    public static final int SCREEN_HEIGHT = 200;//960;

    private BufferedImage buffer;
    private Graphics2D bufferGraphics;
    private JFrame jFrame;
    //private KeyListener keyListener;

    public Screen(){
        this.buffer = new BufferedImage(Screen.SCREEN_WIDTH, Screen.SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        this.jFrame = new JFrame("Test Screen");
        this.jFrame.setLayout(new BorderLayout());
        this.jFrame.add(this);
        //this.jFrame.addKeyListener(keyListener);
        this.jFrame.setSize(Screen.SCREEN_WIDTH, Screen.SCREEN_HEIGHT);
        this.jFrame.setResizable(false); //TODO
        jFrame.setLocationRelativeTo(null); //?
        this.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jFrame.setVisible(true);

//        JFrame test = new JFrame();
//        test.setSize(200,200);
//        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        test.setVisible(true);
    }

    private void init() {
        /*
        this.buffer = new BufferedImage(Screen.SCREEN_WIDTH, Screen.SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        this.jFrame = new JFrame("Test Screen");
        this.jFrame.setLayout(new BorderLayout());
        this.jFrame.add(this);
        //this.jFrame.addKeyListener(tankOneControl);
        this.jFrame.setSize(Screen.SCREEN_WIDTH, Screen.SCREEN_HEIGHT);
        this.jFrame.setResizable(false); //TODO
        jFrame.setLocationRelativeTo(null); //?
        this.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jFrame.setVisible(true);

         */
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
        bufferGraphics = buffer.createGraphics();
        bufferGraphics.setColor(Color.BLACK);
        bufferGraphics.fillRect(0,0,getWidth(),getHeight());
        //this.tankOne.drawImage(bufferGraphics);
        g2.drawImage(buffer,0,0,null);
    }
}
