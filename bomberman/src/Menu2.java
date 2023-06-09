import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu2 extends JFrame implements ActionListener {

    JFrame f;

    JButton PlayButton, ExitButton, MuteButton, soundButton;
    JLabel background = new JLabel(new ImageIcon("D:\\Bomberman\\bomberman\\Lead.png"));
    SoundClass sound = new SoundClass();


    public Menu2() {

        f = new JFrame("BomberMan");


        PlayButton = new JButton(new ImageIcon("D:\\Bomberman\\bomberman\\playButton.png"));
        PlayButton.setBounds(520, 380, 200, 50);
        background.setBounds(0,0,1240,520);
        ExitButton = new JButton(new ImageIcon("D:\\Bomberman\\bomberman\\exitButton.png"));
        ExitButton.setBounds(520, 440, 200, 50);


        soundButton = new JButton(new ImageIcon("D:\\Bomberman\\bomberman\\soundButton.png"));
        soundButton.setBounds(1140, 10, 40, 40);

        MuteButton = new JButton(new ImageIcon("D:\\Bomberman\\bomberman\\MuteButton.png"));
        MuteButton.setBounds(1190, 10, 40, 40);


        f.add(PlayButton); f.add(MuteButton);
        f.add(soundButton);
        f.add(ExitButton);

        PlayButton.addActionListener(this);
        sound.play("D:\\Bomberman\\bomberman\\soundtracks.wav");

        soundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                sound.play("D:\\Bomberman\\bomberman\\soundtracks.wav");
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
        f.setSize(1240, 540);
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
            sound.stop();
            f.dispose();
            Engine.startGame();


        }


    }
}
