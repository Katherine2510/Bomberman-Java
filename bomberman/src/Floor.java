import java.util.*;


public class Floor extends AbstractCharacter {
    // Constants are static by definition.
    private final static double CHANCE_FOR_BREAKABLE_BLOCK = 0.4;
    private final FloorTile[][] tiles;
    private int width;
    private int height;
    private Collection<FloorListener> floorListeners = new ArrayList<>();
    private Player player = null;
    private Collection<Enemy> enemyList = new ArrayList<>();
    private List<Bomb> bombList= new ArrayList<>();
    private Collection<AbstractPowerup> powerupList = new ArrayList<>();
    private Collection<Bomb> explosionList= new ArrayList<>();
    private Collection<Explosion> explosionCoords= new ArrayList<>();
    private Collection<Explosion> explosionCoord= new ArrayList<>();
    private boolean isGameOver = false;
    private int isNextLevel = 0;
    private int fast = 2;
    private int x1=60;
    private int y1=60;
    private int v=20;
    public SoundBomb soundBomb;


    public Floor(int width, int height, int nrOfEnemies, int level) {
        super(width,height);
        this.width = width;
        this.height = height;
        this.tiles = new FloorTile[height][width];
        placeBreakable(level);
        placeUnbreakableAndGrass();
        spawnEnemies(nrOfEnemies);
    }

    public static int pixelToSquare(int pixelCoord){
        return ((pixelCoord + BombermanComponent.getSquareSize()-1) / BombermanComponent.getSquareSize())-1;
    }

    public FloorTile getFloorTile(int rowIndex, int colIndex) {
        return tiles[rowIndex][colIndex];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Player getPlayer() {
        return player;
    }

    public Collection<Enemy> getEnemyList() {
        return enemyList;
    }

    public Iterable<Bomb> getBombList() {
        return bombList;
    }

    public int getBombListSize() {
        return bombList.size();
    }

    public Iterable<AbstractPowerup> getPowerupList() {
        return powerupList;
    }

    public Iterable<Explosion> getExplosionCoords() {
        return explosionCoords;
    }

    public Iterable<Explosion> getExplosionCoord() {
        return explosionCoord;
    }

    public boolean getIsGameOver() {
        return isGameOver;
    }

    public int getIsNextLevel() {
        return isNextLevel;
    }

    public void setIsNextLevel(int value) {
        isNextLevel = value;
    }

    public void setIsGameOver(boolean value) {
        isGameOver = value;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public int getFast() {
        return fast;
    }

    public void setFast(int value) {
        fast = value;
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 =x1;
    }


    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public void addToBombList(Bomb bomb) {
        bombList.add(bomb);
    }

    public void createPlayer(BombermanComponent bombermanComponent, Floor floor){
        player = new Player(bombermanComponent, floor);
        if(fast==3) {
            player = new Player(bombermanComponent, floor);

        }
    }

    public int squareToPixel(int squareCoord){
        return squareCoord * BombermanComponent.getSquareSize();
    }

    public void moveEnemies() {
        if (enemyList.isEmpty() && collisionWithDoor()) {
            isNextLevel =1;
            fast = 2;
        }

        for (Enemy e: enemyList){
            Move currentDirection = e.getCurrentDirection();

            if (currentDirection == Move.DOWN) {
                e.move(Move.DOWN);
            } else if (currentDirection == Move.UP) {
                e.move(Move.UP);
            } else if (currentDirection == Move.LEFT) {
                e.move(Move.LEFT);
            } else {
                e.move(Move.RIGHT);
            }

            if (collisionWithBlock(e)) {
                e.changeDirection();
            }
            if (cantMovee(e)) {
                if(canmove1(e)) {
                    e.changeMove();
                } else {
                    e.changeMove6();
                }
            }

            if(canmove1(e)) {
                if(cantMovee(e)) {
                    e.changeMove7();
                } else {
                    e.changeMove1();
                }
            }
            if(canmove2(e)) {
                if(canmove1(e)) {
                    e.changeMove4();
                } else {
                    e.changeMove2();
                }
            }

            if(canmove3(e)) {
                if(canmove2(e)) {
                    e.changeMove5();
                } else {
                    e.changeMove3();
                }
            }

            if (collisionWithBombs(e)) {
                e.changeDirection();
            }

            if (collisionWithEnemies()) {
                fast=2;
                isGameOver = true;
            }
        }
    }

    public void addFloorListener(FloorListener bl) {
        floorListeners.add(bl);
    }

    public void notifyListeners() {
        for (FloorListener b : floorListeners) {
            b.floorChanged();
        }
    }

    /**
     * This method creates a bomb if the given demands are satisfied.
     */
    public void bombCountdown(){
        Collection<Integer> bombIndexesToBeRemoved = new ArrayList<>();
        explosionList.clear();
        int index = 0;
        for (Bomb b: bombList) {
            b.setTimeToExplosion(b.getTimeToExplosion() - 1);
            if(b.getTimeToExplosion() == 0){
                bombIndexesToBeRemoved.add(index);
                explosionList.add(b);
            }
            index++;
        }
        for (int i: bombIndexesToBeRemoved){
            soundBomb.play("D:\\Bomberman\\bomberman\\mixkit-bomb-explosion-in-battle-2800.wav");
            bombList.remove(i);
        }
    }

    public void explosionHandler(){
        Collection<Explosion> explosionsToBeRemoved = new ArrayList<>();
        for (Explosion e:explosionCoords) {
            e.setDuration(e.getDuration()-1);
            if(e.getDuration()==0){
                explosionsToBeRemoved.add(e);
            }
        }
        for (Explosion e: explosionsToBeRemoved){explosionCoords.remove(e);}

        for (Bomb e: explosionList) {
            int eRow = e.getRowIndex();
            int eCol = e.getColIndex();
            boolean northOpen = true;
            boolean southOpen = true;
            int kt=0;
            int kt1=0;
            explosionCoords.add(new Explosion(eRow, eCol));
            for (int i = 1; i < e.getExplosionRadius()+1; i++) {
                if (eRow - i >= 1 && northOpen) {
                    kt++;
                    if(kt==1 || kt==2) {
                        northOpen = bombCoordinateCheck(eRow-i, eCol, northOpen);
                    }
                }
                if (eRow - i <= height && southOpen) {
                    kt1++;
                    if(kt1==1 || kt1==2) {
                        southOpen = bombCoordinateCheck(eRow+i, eCol, southOpen);
                    }
                }

            }

        }

        for (Explosion e:explosionCoord) {
            e.setDuration(e.getDuration()-1);
            if(e.getDuration()==0){
                explosionsToBeRemoved.add(e);
            }
        }
        for (Explosion e: explosionsToBeRemoved){explosionCoord.remove(e);}

        for (Bomb e: explosionList) {
            int eRow = e.getRowIndex();
            int eCol = e.getColIndex();
            boolean westOpen = true;
            boolean eastOpen = true;
            int kt=0;
            int kt1=0;
            explosionCoord.add(new Explosion(eRow, eCol));
            for (int i = 1; i < e.getExplosionRadius()+1; i++) {
                if (eCol - i >= 1 && westOpen) {
                    kt++;
                    if(kt==1 || kt==2) {
                        westOpen = bombCoordinateCheck1(eRow, eCol - i, westOpen);
                    }
                }
                if (eCol + i <= width && eastOpen) {
                    kt1++;
                    if(kt1==1 || kt1==2) {
                        eastOpen = bombCoordinateCheck1(eRow, eCol+i, eastOpen);
                    }
                }
            }

        }
    }

    public void playerInExplosion(){
        for (Explosion tup:explosionCoords) {
            if(collidingCircles(player, squareToPixel(tup.getColIndex()), squareToPixel(tup.getRowIndex()))){
                isGameOver = true;
                fast=2;
            }
        }
        for (Explosion tup:explosionCoord) {
            if(collidingCircles(player, squareToPixel(tup.getColIndex()), squareToPixel(tup.getRowIndex()))){
                isGameOver = true;
                fast=2;
            }
        }
    }

    public void enemyInExplosion(){
        for (Explosion tup:explosionCoords) {
            Collection<Enemy> enemiesToBeRemoved = new ArrayList<>();
            for (Enemy e : enemyList) {
                if(collidingCircles(e, squareToPixel(tup.getColIndex()), squareToPixel(tup.getRowIndex()))){
                    enemiesToBeRemoved.add(e);
                }
            }
            for (Enemy e: enemiesToBeRemoved ) {
                enemyList.remove(e);
            }
        }
        for (Explosion tup:explosionCoord) {
            Collection<Enemy> enemiesToBeRemoved = new ArrayList<>();
            for (Enemy e : enemyList) {
                if(collidingCircles(e, squareToPixel(tup.getColIndex()), squareToPixel(tup.getRowIndex()))){
                    enemiesToBeRemoved.add(e);
                }
            }
            for (Enemy e: enemiesToBeRemoved ) {
                enemyList.remove(e);
            }
        }
    }

    public boolean cantMovee(AbstractCharacter abstractCharacter){
        //Maybe create if statements to only check nearby squares
        for (int i = 1; i < height - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                if( getFloorTile(i + 1, j) == FloorTile.FLOOR){
                    boolean isIntersecting = squareCircleInstersect(i  , j , abstractCharacter);
                    if (isIntersecting) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean canmove1(AbstractCharacter abstractCharacter) {

        for (int i = 1; i < height - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                if( getFloorTile(i, j+1) == FloorTile.FLOOR){
                    boolean isIntersecting = squareCircleInstersect(i  , j , abstractCharacter);
                    if (isIntersecting) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean canmove2(AbstractCharacter abstractCharacter) {

        for (int i = 1; i < height - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                if( getFloorTile(i-1, j) == FloorTile.FLOOR){
                    boolean isIntersecting = squareCircleInstersect(i  , j , abstractCharacter);
                    if (isIntersecting) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean canmove3(AbstractCharacter abstractCharacter) {

        for (int i = 1; i < height - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                if( getFloorTile(i, j-1) == FloorTile.FLOOR){
                    boolean isIntersecting = squareCircleInstersect(i  , j , abstractCharacter);
                    if (isIntersecting) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void characterInExplosion(){
        playerInExplosion();
        enemyInExplosion();
    }

    private void placeBreakable (int level) {
        if(level == 1) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if ((i == 7 && j == 1) || (i == 8 && j == 1) || (i == 10 && j == 1) || (i == 15 && j == 1) || (i == 19 && j == 1)
                            || (i == 22 && j == 1) || (i == 24 && j == 1) || (i == 26 && j == 1) || (i == 7 && j == 2) || (i == 13 && j == 2)
                            || (i == 15 && j == 2) || (i == 23 && j == 2) || (i == 25 && j == 2) || (i == 27 && j == 2) || (i == 4 && j == 3)
                            || (i == 10 && j == 3) || (i == 11 && j == 3) || (i == 12 && j == 3) || (i == 15 && j == 3) || (i == 22 && j == 3)
                            || (i == 26 && j == 3) || (i == 28 && j == 3) || (i == 11 && j == 4) || (i == 17 && j == 4) || (i == 19 && j == 4) || (i == 29 && j == 4)
                            || (i == 13 && j == 5) || (i == 14 && j == 4) || (i == 17 && j == 5) || (i == 20 && j == 5)
                            || (i == 19 && j == 6) || (i == 23 && j == 6) || (i == 1 && j == 7) || (i == 4 && j == 7) || (i == 11 && j == 7)
                            || (i == 14 && j == 7) || (i == 21 && j == 7) || (i == 9 && j == 8) || (i == 17 && j == 8) || (i == 19 && j == 3)
                            || (i == 1 && j == 9) || (i == 6 && j == 9) || (i == 7 && j == 9) || (i == 10 && j == 9) || (i == 18 && j == 9)
                            || (i == 3 && j == 10) || (i == 10 && j == 10) || (i == 17 && j == 10) || (i == 12 && j == 11) || (i == 16 && j == 11)
                            || (i == 19 && j == 11)) {
                        tiles[j][i] = FloorTile.BREAKABLEBLOCK;
                    }
                }
            }
        }
        if(level == 2) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if ((i == 7 && j == 1) || (i == 8 && j == 1) || (i == 10 && j == 1) || (i == 15 && j == 1) || (i == 19 && j == 1)
                            || (i == 22 && j == 1) || (i == 24 && j == 1) || (i == 26 && j == 1) || (i == 7 && j == 2) || (i == 13 && j == 2)
                            || (i == 29 && j == 4)
                            || (i == 13 && j == 5) || (i == 14 && j == 4) || (i == 17 && j == 5) || (i == 20 && j == 5)
                            || (i == 19 && j == 6) || (i == 23 && j == 6) || (i == 1 && j == 7) || (i == 4 && j == 7) || (i == 11 && j == 7)

                            || (i == 19 && j == 11)) {
                        tiles[j][i] = FloorTile.BREAKABLEBLOCK;
                    }
                }
            }
        }
        if(level == 3) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if ((i == 7 && j == 1) || (i == 8 && j == 1) || (i == 10 && j == 1) || (i == 15 && j == 1) || (i == 19 && j == 1)
                            || (i == 22 && j == 1) || (i == 24 && j == 1) || (i == 26 && j == 1) || (i == 7 && j == 2) || (i == 13 && j == 2)
                            || (i == 15 && j == 2) || (i == 23 && j == 2) || (i == 25 && j == 2) || (i == 27 && j == 2) || (i == 4 && j == 3)
                            || (i == 10 && j == 3) || (i == 11 && j == 3) || (i == 12 && j == 3) || (i == 15 && j == 3) || (i == 22 && j == 3)
                            || (i == 26 && j == 3) || (i == 28 && j == 3) || (i == 11 && j == 4) || (i == 17 && j == 4) || (i == 19 && j == 4) || (i == 29 && j == 4)
                            || (i == 13 && j == 5) || (i == 14 && j == 4) || (i == 17 && j == 5) || (i == 20 && j == 5)
                            ) {
                        tiles[j][i] = FloorTile.BREAKABLEBLOCK;
                    }
                }
            }
        }
    }

    private void clearSpawn () {
        tiles[1][1] = FloorTile.FLOOR;
        tiles[1][2] = FloorTile.FLOOR;
        tiles[2][1] = FloorTile.FLOOR;
    }

    private void spawnPowerup (int rowIndex, int colIndex) {
        double r = Math.random();
        if (r >0.8) {
            powerupList.add(new BombRadiusPU(squareToPixel(rowIndex) + BombermanComponent.getSquareMiddle(), squareToPixel(colIndex) + BombermanComponent.getSquareMiddle()));
        } else if (r<0.3) {
            powerupList.add(new BombCounterPU(squareToPixel(rowIndex) + BombermanComponent.getSquareMiddle(), squareToPixel(colIndex) + BombermanComponent.getSquareMiddle()));
        } else if(r >=0.5 && r<=0.8) {
            powerupList.add(new BombRunFastPU(squareToPixel(rowIndex) + BombermanComponent.getSquareMiddle(), squareToPixel(colIndex) + BombermanComponent.getSquareMiddle()));

        }
    }

    private void placeUnbreakableAndGrass () {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                //Makes frame of unbreakable
                if ((i == 0) || (j == 0) || (i == height - 1) || (j == width - 1) || i % 2 == 0 && j % 2 == 0) {
                    tiles[i][j] = FloorTile.UNBREAKABLEBLOCK;
                    //Every-other unbreakable
                } else if (tiles[i][j] != FloorTile.BREAKABLEBLOCK) {
                    tiles[i][j] = FloorTile.FLOOR;
                }
            }
        }
    }

    private void spawnEnemies (int nrOfEnemies) {
        for (int e = 0; e < nrOfEnemies; e++){
            while(true) {
                int randRowIndex = 1 + (int) (Math.random() * (height - 2));
                int randColIndex = 1 + (int) (Math.random() * (width - 2));
                if(getFloorTile(randRowIndex, randColIndex) != FloorTile.FLOOR){
                    continue;
                }
                if(randRowIndex==1&&randColIndex==1||randRowIndex==1&&randColIndex==2||randRowIndex==2&&randColIndex==1){
                    continue;
                }
                if(e<5) {
                    if ((randRowIndex % 2) == 0) {
                        enemyList.add(new Balloom(squareToPixel(randColIndex) + BombermanComponent.getSquareMiddle(), squareToPixel(randRowIndex) + BombermanComponent.getSquareMiddle(), true));
                    } else {
                        enemyList.add(new Balloom(squareToPixel(randColIndex) + BombermanComponent.getSquareMiddle(), squareToPixel(randRowIndex) + BombermanComponent.getSquareMiddle(), false));
                    }
                }
                if(e>=5) {
                    if ((randRowIndex % 2) == 0 && e<5) {
                        enemyList.add(new Balloom(squareToPixel(randColIndex) + BombermanComponent.getSquareMiddle(), squareToPixel(randRowIndex) + BombermanComponent.getSquareMiddle(), true));
                    } else {
                        enemyList.add(new Oneal(squareToPixel(randColIndex) + BombermanComponent.getSquareMiddle(), squareToPixel(randRowIndex) + BombermanComponent.getSquareMiddle(), true));

                    }
                }
                break;
            }
        }
    }



    public boolean collisionWithEnemies(){
        for (Enemy enemy : enemyList) {
            if(collidingCircles(player, enemy.getX()-BombermanComponent.getSquareMiddle(), enemy.getY()-BombermanComponent.getSquareMiddle())){
                return true;
            }
        }
        return false;
    }

    public boolean collisionWithBombs(AbstractCharacter abstractCharacter) {
        boolean playerLeftBomb = true;

        for (Bomb bomb : bombList) {
            if (abstractCharacter instanceof Player) {
                playerLeftBomb = bomb.isPlayerLeft();
            }
            if(playerLeftBomb && collidingCircles(abstractCharacter, squareToPixel(bomb.getColIndex()), squareToPixel(bomb.getRowIndex()))){
                return true;
            }
        }
        return false;
    }


    public boolean collisionWithBlock(AbstractCharacter abstractCharacter){
        //Maybe create if statements to only check nearby squares
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if(getFloorTile(i, j) != FloorTile.FLOOR && getFloorTile(i,j) != FloorTile.DOOR){
                    boolean isIntersecting = squareCircleInstersect(i, j, abstractCharacter);
                    if (isIntersecting) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean collisionWithDoor() {
        if(collidingCircles(player,680,200)) {
            return true;
        }
        return false;
    }

    public void collisionWithPowerup() {
        for (AbstractPowerup powerup : powerupList) {
            if(collidingCircle(player, powerup.getX(), powerup.getY())){
                powerup.addToPlayer(player);
                if (powerup.getName().equals("BombFaster")) {
                    player.setPixelsPerStep(player.getSpeed());
                }
                powerupList.remove(powerup);
                break;
            }

        }
    }



    public boolean squareHasBomb(int rowIndex, int colIndex){
        for (Bomb b: bombList) {
            if(b.getRowIndex()==rowIndex && b.getColIndex()==colIndex){
                return true;
            }
        }
        return false;
    }


    public void checkIfPlayerLeftBomb(){
        for (Bomb bomb: bombList) {
            if(!bomb.isPlayerLeft()){
                if(!collidingCircles(player, squareToPixel(bomb.getColIndex()), squareToPixel(bomb.getRowIndex()))){
                    bomb.setPlayerLeft(true);
                }
            }
        }
    }

    private boolean bombCoordinateCheck(int eRow, int eCol, boolean open){

        if(tiles[eRow][eCol] != FloorTile.FLOOR){open = false;}
        if(tiles[eRow][eCol] == FloorTile.BREAKABLEBLOCK){
            if(eRow==5 && eCol == 17) {
                tiles[eRow][eCol] = FloorTile.DOOR;
            } else {
                tiles[eRow][eCol] = FloorTile.FLOOR;
                spawnPowerup(eRow, eCol);
            }
        }
        if(tiles[eRow][eCol] != FloorTile.UNBREAKABLEBLOCK){explosionCoords.add(new Explosion(eRow, eCol));}
        return open;
    }
    private boolean bombCoordinateCheck1(int eRow, int eCol, boolean open){
        if(tiles[eRow][eCol] != FloorTile.FLOOR){open = false;}
        if(tiles[eRow][eCol] == FloorTile.BREAKABLEBLOCK){
            if(eRow==5 && eCol == 17) {
                tiles[eRow][eCol] = FloorTile.DOOR;
            } else {
                tiles[eRow][eCol] = FloorTile.FLOOR;
                spawnPowerup(eRow, eCol);
            }
        }
        if(tiles[eRow][eCol] != FloorTile.UNBREAKABLEBLOCK){explosionCoord.add(new Explosion(eRow, eCol));}
        return open;
    }

    private boolean collidingCircles(AbstractCharacter abstractCharacter, int x, int y){
        int a = abstractCharacter.getX() - x - BombermanComponent.getSquareMiddle();
        int b = abstractCharacter.getY() - y - BombermanComponent.getSquareMiddle();
        int a2 = a * a;
        int b2 = b * b;
        double c = Math.sqrt(a2 + b2);
        return(abstractCharacter.getSize() > c);
    }

    private boolean collidingCircle(AbstractCharacter abstractCharacter, int x, int y){
        if(abstractCharacter.getX()==x && abstractCharacter.getY()==y) {
            return true;
        } else return false;
    }

    private boolean squareCircleInstersect(int row, int col, AbstractCharacter abstractCharacter) {
        //http://stackoverflow.com/questions/401847/circle-rectangle-collision-detection-intersection
        int characterX = abstractCharacter.getX();
        int characterY = abstractCharacter.getY();

        int circleRadius = abstractCharacter.getSize() / 2;
        int squareSize = BombermanComponent.getSquareSize();
        int squareCenterX = (col*squareSize)+(squareSize/2);
        int squareCenterY = (row*squareSize)+(squareSize/2);

        int circleDistanceX = Math.abs(characterX - squareCenterX);
        int circleDistanceY = Math.abs(characterY - squareCenterY);

        if (circleDistanceX > (squareSize/2 + circleRadius)) { return false; }
        if (circleDistanceY > (squareSize/2 + circleRadius)) { return false; }

        if (circleDistanceX <= (squareSize/2)) { return true; }
        if (circleDistanceY <= (squareSize/2)) { return true; }

        int cornerDistance = (circleDistanceX - squareSize/2)^2 +
                (circleDistanceY - squareSize/2)^2;

        return (cornerDistance <= (circleRadius^2));
    }
    public void Mute (boolean check) {
        if (check == true ) {
            try {
                player.MutePlayer();
                //soundClass1.stop();
                soundBomb.stop();

            } catch (NullPointerException ex) {

            }
        }


    }
}