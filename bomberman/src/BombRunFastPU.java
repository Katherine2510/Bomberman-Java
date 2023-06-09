public class BombRunFastPU extends AbstractPowerup {

    public BombRunFastPU(int rowIndex, int colIndex) {
        super(colIndex, rowIndex);
    }
    public void addToPlayer(Player player) {
        player.setSpeed(40);
    }

    public String getName() {
        final String name = "BombFaster";
        return name;
    }
}
