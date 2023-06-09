import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public final class Engine
{
    private static final int TIME_STEP = 30;
    private static int width = 31;
    private static int height = 13;
    private static int nrOfEnemies = 5;
    private static Timer clockTimer = null;
    private static int level = 1;
    private static boolean check = false;

    private Engine() {}

    public static void main(String[] args) {
        new Menu();
        //startGame();
    }

    public static void startGame() {

        Floor floor = new Floor(width , height , nrOfEnemies, level);
        BombermanFrame frame = new BombermanFrame("Bomberman", floor);
        int mm;

        frame.setVisible(true);
        frame.setSize(1258,600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        floor.addFloorListener(frame.getBombermanComponent());

        JPanel p = new JPanel();
        JButton b1 = new JButton("Mute");
        JLabel countTime = new JLabel();

        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                check = true;
            }
        });


        JButton b2 = new JButton("Music");


        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                check = false;
            }
        });

        Thread myThread = new Thread(){
            boolean checkTime = false;
            int counter = 200;
            public void run() {
                while(counter >= 0) {
                    countTime.setText("TIME     " + (counter--) + "                                                                            ");
                    try{
                        Thread.sleep(1000);
                    } catch(Exception e) {}
                    if(floor.getIsGameOver() || floor.getIsNextLevel()==1) {
                        counter=100;
                    }
                    if (counter == 0) {
                        frame.dispose();
                        new GameOver();
                    }

                }

            }
        };

        myThread.start();

        JButton b3 = new JButton("Pause");

        b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                clockTimer.stop();
                myThread.suspend();
            }
        });

        JButton b4 = new JButton("Continue");

        b4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                clockTimer.start();
                myThread.resume();

            }
        });


        p.add(countTime);
        p.add(b1);
        p.add(b2);
        p.add(b3);
        p.add(b4);

        frame.getContentPane().add(p, BorderLayout.NORTH);

        Action doOneStep = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e) {
                tick(frame, floor);
            }
        };
        clockTimer = new Timer(TIME_STEP, doOneStep);
        clockTimer.setCoalesce(true);
        clockTimer.start();
    }

    private static void gameOver(BombermanFrame frame, Floor floor) {
        clockTimer.stop();
        frame.dispose();
        new GameOver();
        nrOfEnemies=5;
    }

    private static void NextGame(BombermanFrame frame, Floor floor) {
        clockTimer.stop();
        frame.dispose();
        startGame();
    }

    private static void tick(BombermanFrame frame, Floor floor) {
        if (floor.getIsGameOver()) {
            level = 1;
            gameOver(frame, floor);
        } else {
            if (check == true) {

                floor.moveEnemies();
                floor.bombCountdown();
                floor.Mute(check);
                floor.explosionHandler();
                floor.characterInExplosion();
                floor.notifyListeners();
            } else {
                floor.moveEnemies();
                floor.bombCountdown();
                floor.explosionHandler();

                floor.characterInExplosion();
                floor.notifyListeners();
            }
            if(floor.getIsNextLevel() == 1) {
                level ++;
                nrOfEnemies = nrOfEnemies +2;
                NextGame(frame, floor);
                SoundClass soundClass = new SoundClass();
                soundClass.play1("D:\\Bomberman\\bomberman\\mixkit-game-level-completed-2059.wav");
            }


        }
    }

}

