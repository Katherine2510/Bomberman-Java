import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;


public class Player extends AbstractCharacter
{

    private final static int PLAYER_START_X = 60;
    private final static int PLAYER_START_Y = 60;
    private static int PLAYER_PIXELS_BY_STEP = 20;
    private int explosionRadius;
    private int bombCount;
    private int faster;
    private int speed=20;
    soundPlayer so;
    ImageIcon ii = new ImageIcon("D:\\Bomberman\\bomberman\\10.gif");

    private Floor floor;

    public Action up = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            movePlayer(Move.UP);
            ii = new ImageIcon("D:\\Bomberman\\bomberman\\13.gif");

        }
    };

    public Action right = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            movePlayer(Move.RIGHT);
            ii = new ImageIcon("D:\\Bomberman\\bomberman\\12.gif");

        }
    };

    public Action down = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            movePlayer(Move.DOWN);
            ii = new ImageIcon("D:\\Bomberman\\bomberman\\10.gif");

        }
    };

    public Action left = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            movePlayer(Move.LEFT);
            ii = new ImageIcon("D:\\Bomberman\\bomberman\\11.gif");
        }
    };


    public Action dropBomb = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e) {
            if(!floor.squareHasBomb(getRowIndex(), getColIndex()) && floor.getBombListSize() < getBombCount()){
                floor.addToBombList(new Bomb(getRowIndex(), getColIndex(), getExplosionRadius()));
            }
            floor.notifyListeners();
        }
    };

    public Player(BombermanComponent bombermanComponent, Floor floor) {
        super(PLAYER_START_X, PLAYER_START_Y);
        explosionRadius = 1;
        this.setPixelsPerStep(speed);
        bombCount = 1;
        faster = 1;
        this.floor = floor;
        setPlayerButtons(bombermanComponent);
    }

    public void setPlayerButtons(BombermanComponent bombermanComponent){
        bombermanComponent.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
        bombermanComponent.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
        bombermanComponent.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), "moveUp");
        bombermanComponent.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
        bombermanComponent.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_B, 0, false), "dropBomb");
        bombermanComponent.getActionMap().put("moveRight", right);
        bombermanComponent.getActionMap().put("moveLeft", left);
        bombermanComponent.getActionMap().put("moveUp", up);
        bombermanComponent.getActionMap().put("moveDown", down);
        bombermanComponent.getActionMap().put("dropBomb", dropBomb);
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getBombCount() {
        return bombCount;
    }

    public void setBombCount(int bombCount) {
        this.bombCount = bombCount;
    }

    public int getExplosionRadius() {
        return explosionRadius;
    }

    public void setExplosionRadius(int explosionRadius) {
        this.explosionRadius = explosionRadius;
    }


    private void movePlayer(Move move) {
        move(move);

        so = new soundPlayer();
        so.play("D:\\Bomberman\\bomberman\\Step.wav");



        if(floor.collisionWithBlock(this)){
            moveBack(move);
        }
        if(floor.collisionWithBombs(this)){
            moveBack(move);
        }
        if(floor.collisionWithEnemies()){
            floor.setIsGameOver(true);
        }

        floor.checkIfPlayerLeftBomb();
        floor.collisionWithPowerup();
        floor.notifyListeners();
    }
    public void MutePlayer() {
        try {
            so.stop();
        } catch (NullPointerException ex) {

        }
    }

}
