public class Enemy extends AbstractCharacter
{
    private Move currentDirection;
    private String name = null;
    private Floor floor;
    public Enemy(int x, int y, boolean vertical) {
        super(x, y);
        currentDirection = randomDirection(vertical);
    }

    public void changeDirection() {
        if (currentDirection == Move.DOWN) {
            currentDirection = Move.UP;
        } else if (currentDirection == Move.UP) {
            currentDirection = Move.DOWN;
        } else if (currentDirection == Move.LEFT) {
            currentDirection = Move.RIGHT;
        } else {
            currentDirection = Move.LEFT;
        }
    }
    public String getName() {
        return name;
    }
    public Move getCurrentDirection() {
        return currentDirection;
    }

    public void changeMove() {
        if ((currentDirection == Move.LEFT)&& getX()%20==0 && getX()%40!=0) {
            currentDirection = Move.DOWN;
        }

    }
    public void changeMove4() {
        if ((currentDirection == Move.LEFT)&& getX()%20==0 && getX()%40!=0) {
            currentDirection = Move.UP;
        }

    }
    public void changeMove1() {
        if ((currentDirection == Move.DOWN) && getY() % 20 == 0 && getY() % 40 != 0) {
            currentDirection = Move.RIGHT;

        }
    }
    public void changeMove5() {
        if ((currentDirection == Move.DOWN) && getY() % 20 == 0 && getY() % 40 != 0) {
            currentDirection = Move.LEFT;

        }
    }

    public void changeMove2() {
        if ((currentDirection == Move.RIGHT) && getX() % 20 == 0 && getX() % 40 != 0) {
            currentDirection = Move.UP;
        }
    }

    public void changeMove6() {
        if ((currentDirection == Move.RIGHT) && getX() % 20 == 0 && getX() % 40 != 0) {
            currentDirection = Move.DOWN;
        }
    }

    public void changeMove3() {
        if ((currentDirection == Move.UP) && getY() % 20 == 0 && getY() % 40 != 0) {
            currentDirection = Move.LEFT;
        }
    }
    public void changeMove7() {
        if ((currentDirection == Move.UP) && getY() % 20 == 0 && getY() % 40 != 0) {
            currentDirection = Move.RIGHT;
        }
    }

    private Move randomDirection(boolean vertical) {
        assert Move.values().length == 4;
        int pick = (int) (Math.random() * (Move.values().length-2));
        if(vertical) {
            return Move.values()[pick+1];
        }
        else{
            return Move.values()[pick+1];
        }

    }

}