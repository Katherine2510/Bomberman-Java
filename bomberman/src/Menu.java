import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Menu extends  JFrame implements ActionListener {

    JFrame f;
    SoundClass sound = new SoundClass();
    JButton soundButton, MuteButton;
    JButton PlayButton, ExitButton;
    JLabel background = new JLabel(new ImageIcon("D:\\Bomberman\\bomberman\\screen.png"));


    public Menu() {

        f = new JFrame("BomberMan");


        PlayButton = new JButton(new ImageIcon("D:\\Bomberman\\bomberman\\playButton.png"));
        PlayButton.setBounds(520, 360, 200, 50);

        ExitButton = new JButton(new ImageIcon("D:\\Bomberman\\bomberman\\exitButton.png"));
        ExitButton.setBounds(520, 420, 200, 50);


        soundButton = new JButton(new ImageIcon("D:\\Bomberman\\bomberman\\soundButton.png"));
        soundButton.setBounds(1140, 10, 40, 40);

        MuteButton = new JButton(new ImageIcon("D:\\Bomberman\\bomberman\\MuteButton.png"));
        MuteButton.setBounds(1190, 10, 40, 40);


        background.setBounds(0,0,1240,520);
        f.add(PlayButton);
        f.add(MuteButton);
        f.add(soundButton);
        f.add(ExitButton);

        PlayButton.addActionListener(this);

        sound.play("D:\\Bomberman\\bomberman\\MenuSound.wav");
        soundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                sound.play("D:\\Bomberman\\bomberman\\MenuSound.wav");
            }
        });

        MuteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                sound.stop();
            }
        });

        ExitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                sound.stop();
                f.dispose();
            }
        });

        //f.getContentPane().setBackground(new Color(6, 96, 126));
        f.setSize(1240, 520);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        f.setLocation(dim.width/2-f.getSize().width/2, dim.height/2-f.getSize().height/2);
        f.add(background);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setLayout(null);
        f.setVisible(true);


    }
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == PlayButton) {
            f.dispose();
            sound.stop();
            new Menu2();
        }
    }
}
