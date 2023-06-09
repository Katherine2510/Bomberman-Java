public class Oneal extends Enemy {
    public Oneal(int x, int y, boolean vertical) {
        super(x, y, vertical);
        this.setPixelsPerStep(2);
    }
    public String getName() {
        final String name = "Oneal";
        return name;
    }
}
