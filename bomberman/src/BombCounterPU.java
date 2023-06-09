public class BombCounterPU extends AbstractPowerup
{

    public BombCounterPU(int rowIndex, int colIndex) {
        super(colIndex, rowIndex);
    }

    public void addToPlayer(Player player) {
        player.setBombCount(5);
    }

    public String getName() {
        final String name = "BombCounter";
        return name;
    }
}