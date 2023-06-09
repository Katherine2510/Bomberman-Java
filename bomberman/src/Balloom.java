public class Balloom extends Enemy {

    public Balloom(int x, int y, boolean vertical) {
        super(x, y, vertical);
        this.setPixelsPerStep(1);
    }


    public String getName() {
        final String name = "Balloom";
        return name;
    }
}
