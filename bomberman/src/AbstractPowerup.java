public class AbstractPowerup
{
    // Constants are static by definition.
    private final static int POWERUP_SIZE = 10;
    private final int x;
    private final int y;
    private String name = null;

    public AbstractPowerup(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void addToPlayer(Player player) {
    }

    public int getPowerupSize() {
        return POWERUP_SIZE;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getName() {
        return name;
    }
}