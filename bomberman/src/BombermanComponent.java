import javax.swing.*;
import java.awt.*;
import java.util.AbstractMap;
import java.util.EnumMap;
import java.applet.Applet;

public class BombermanComponent extends JPanel implements FloorListener
{
    // Constants are static by definition.
    private final static int SQUARE_SIZE = 40;
    private final static int CHARACTER_ADJUSTMENT_FOR_PAINT = 15;
    private final static int SQUARE_MIDDLE = SQUARE_SIZE/2;
    private final static int BOMB_ADJUSTMENT_1 =5;

    private final static int BOMB_ADJUSTMENT_2 =10;
    // Defining painting parameters
    private final static int PAINT_PARAMETER_13 = 13;
    private final static int PAINT_PARAMETER_15 = 15;
    private final static int PAINT_PARAMETER_17 = 17;
    private final static int PAINT_PARAMETER_18 = 18;
    private final static int PAINT_PARAMETER_19 = 19;
    private final static int PAINT_PARAMETER_20 = 20;
    private final static int PAINT_PARAMETER_24 = 24;
    private final Floor floor;
    private final AbstractMap<FloorTile, Color> colorMap;
    public BombermanComponent(Floor floor) {
        this.floor = floor;

        colorMap = new EnumMap<>(FloorTile.class);
        colorMap.put(FloorTile.FLOOR, Color.GREEN);
        colorMap.put(FloorTile.DOOR, Color.RED);
        colorMap.put(FloorTile.UNBREAKABLEBLOCK, Color.BLACK);
        colorMap.put(FloorTile.BREAKABLEBLOCK, Color.RED);

    }

    // This method is static since each square has the same size.
    public static int getSquareSize() {
        return SQUARE_SIZE;
    }

    // This method is static since each square has the same size.
    public static int getSquareMiddle() {
        return SQUARE_MIDDLE;
    }

    public Dimension getPreferredSize() {
        super.getPreferredSize();
        return new Dimension(this.floor.getWidth() * SQUARE_SIZE, this.floor.getHeight() * SQUARE_SIZE);
    }

    public void floorChanged() {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;
        paintPlayer(floor.getPlayer(), g2d);


        for (int rowIndex = 0; rowIndex < floor.getHeight(); rowIndex++) {
            for (int colIndex = 0; colIndex < floor.getWidth(); colIndex++) {
                g2d.setColor(colorMap.get(this.floor.getFloorTile(rowIndex, colIndex)));
                if(floor.getFloorTile(rowIndex, colIndex)==FloorTile.BREAKABLEBLOCK){
                    paintBreakableBlock(rowIndex,colIndex,g2d);
                }
                else if(floor.getFloorTile(rowIndex, colIndex)==FloorTile.UNBREAKABLEBLOCK){
                    paintUnbreakableBlock(rowIndex, colIndex, g2d);

                }
                else if(floor.getFloorTile(rowIndex,colIndex) == FloorTile.DOOR) {
                    painDoor(rowIndex,colIndex,g2d);
                }
                else{
                    paintFloor(rowIndex, colIndex, g2d);
                }
            }
        }
        // Paint player:
        paintPlayer(floor.getPlayer(), g2d);

        //Paint enemies
        for (Enemy e: floor.getEnemyList()) {
            if(e.getName().equals("Balloom")) {
                paintBalloom(e,g2d);
            }
            if(e.getName().equals("Oneal")) {
                paintOneal(e,g2d);
            }
        }



        //Paint powerups
        for (AbstractPowerup p: floor.getPowerupList()) {
            if (p.getName().equals("BombCounter")) {
                g2d.drawImage(new ImageIcon("D:\\Bomberman\\bomberman\\powerup_bombs.jpg").getImage(),p.getX()-CHARACTER_ADJUSTMENT_FOR_PAINT - 5, p.getY()-CHARACTER_ADJUSTMENT_FOR_PAINT - 5,null);
            } else if (p.getName().equals("BombFaster")) {
                g2d.drawImage(new ImageIcon("D:\\Bomberman\\bomberman\\powerup_speed.png").getImage(),p.getX()-CHARACTER_ADJUSTMENT_FOR_PAINT - 5, p.getY()-CHARACTER_ADJUSTMENT_FOR_PAINT - 5,null);
            } else if (p.getName().equals("BombRadius")) {
                g2d.drawImage(new ImageIcon("D:\\Bomberman\\bomberman\\powerup_flames.jpg").getImage(),p.getX()-CHARACTER_ADJUSTMENT_FOR_PAINT - 5, p.getY()-CHARACTER_ADJUSTMENT_FOR_PAINT - 5,null);

            }
        }

        //Paint bombs
        for (Bomb b: floor.getBombList()) {
            //g2d.setColor(Color.RED);
            int bombX = floor.squareToPixel(b.getColIndex());
            int bombY = floor.squareToPixel(b.getRowIndex());
            g2d.drawImage(new ImageIcon("D:\\Bomberman\\bomberman\\bombb.gif").getImage(),bombX + BOMB_ADJUSTMENT_1 - 5, bombY + BOMB_ADJUSTMENT_1 - 5,null);
        }

        //Paint explosions

        for (Explosion tup: floor.getExplosionCoords()) {
            g2d.drawImage(new ImageIcon("D:\\Bomberman\\bomberman\\bom1.png").getImage(),floor.squareToPixel(tup.getColIndex()) + BOMB_ADJUSTMENT_1, floor.squareToPixel(tup.getRowIndex()) +
                    BOMB_ADJUSTMENT_1,null);
        }

        for (Explosion tup: floor.getExplosionCoord()) {
            g2d.drawImage(new ImageIcon("D:\\Bomberman\\bomberman\\bom.png").getImage(),floor.squareToPixel(tup.getColIndex()) + BOMB_ADJUSTMENT_1, floor.squareToPixel(tup.getRowIndex()) +
                    BOMB_ADJUSTMENT_1,null);
        }
    }

    private void paintBreakableBlock(int rowIndex, int colIndex, Graphics g2d){
        g2d.drawImage(new ImageIcon("D:\\Bomberman\\bomberman\\3.jpg").getImage(),colIndex * SQUARE_SIZE, rowIndex * SQUARE_SIZE,null);
    }

    private void paintUnbreakableBlock(int rowIndex, int colIndex, Graphics g2d){
        //g2d.fillRect(colIndex * SQUARE_SIZE, rowIndex * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
        g2d.drawImage(new ImageIcon("D:\\Bomberman\\bomberman\\1.jpg").getImage(),colIndex * SQUARE_SIZE, rowIndex * SQUARE_SIZE,null);
    }

    private void paintFloor(int rowIndex, int colIndex, Graphics g2d){
        g2d.setColor(new Color(37,120,10));
        g2d.fillRect(colIndex * SQUARE_SIZE, rowIndex * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
        //g2d.setColor(Color.CYAN);
        //g2d.drawLine(colIndex* SQUARE_SIZE+5, rowIndex*SQUARE_SIZE+10, colIndex * SQUARE_SIZE + 10, rowIndex * SQUARE_SIZE + 5);
        // g2d.drawLine(colIndex* SQUARE_SIZE+5, rowIndex*SQUARE_SIZE+SQUARE_MIDDLE, colIndex * SQUARE_SIZE + SQUARE_MIDDLE, rowIndex * SQUARE_SIZE + 5);
        //g2d.drawLine(colIndex* SQUARE_SIZE+5, rowIndex*SQUARE_SIZE+SQUARE_MIDDLE+10, colIndex * SQUARE_SIZE + SQUARE_MIDDLE + 10, rowIndex * SQUARE_SIZE + 5);
    }

    private void paintBalloom(Enemy e, Graphics g2d){
        g2d.drawImage(new ImageIcon("D:\\Bomberman\\bomberman\\14.gif").getImage(),e.getX()-CHARACTER_ADJUSTMENT_FOR_PAINT - 5, e.getY()-CHARACTER_ADJUSTMENT_FOR_PAINT - 5,null);
    }

    private void paintOneal(Enemy e, Graphics g2d){
        g2d.drawImage(new ImageIcon("D:\\Bomberman\\bomberman\\oneal.gif").getImage(),e.getX()-CHARACTER_ADJUSTMENT_FOR_PAINT - 5, e.getY()-CHARACTER_ADJUSTMENT_FOR_PAINT - 5,null);
    }

    private void paintPlayer(Player player, Graphics g2d){
        g2d.drawImage(player.ii.getImage(),player.getX()-CHARACTER_ADJUSTMENT_FOR_PAINT+PAINT_PARAMETER_15 - 20, player.getY()-CHARACTER_ADJUSTMENT_FOR_PAINT-4,null);
    }
    private void painDoor(int rowIndex, int colIndex, Graphics g2d) {
        g2d.drawImage(new ImageIcon("D:\\Bomberman\\bomberman\\ok.png").getImage(),colIndex * SQUARE_SIZE, rowIndex * SQUARE_SIZE,null);
    }
}