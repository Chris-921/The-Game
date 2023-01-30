
//import
...

public class WorldGenerator {
    //setup
    ...

    public WorldGenerator(String s, String[] movement, TERenderer ter) {
        this.SEED = Long.valueOf(s);
        this.RANDOM = new Random(SEED);
        this.ter = ter;

        fillWithRandomTiles(randomTiles); //pass in and add Floors

        HashSet<int[]> hallways = ConnectRoomsWithCenter(RoomCenters);
        Floors.addAll(hallways);

        for (int[] floor : Floors) {
            randomTiles[floor[0]][floor[1]] = Tileset.FLOORWOOD;
        }
        // draw walls around floors
        for (int[] floor : Floors) {
            DrawWalls(floor[0], floor[1]);
        }

        for (int[] wall : Walls) {
            randomTiles[wall[0]][wall[1]] = Tileset.WALL;
        }


        int xPos = RoomArray.get(0).get(4);
        int yPos = RoomArray.get(0).get(5);
        ArrayList<Integer> initalPos = new ArrayList<>();
        initalPos.add(xPos);
        initalPos.add(yPos);

        //pre set end point
        int indexOfRoomArrayForEndPoint = RoomArray.size() - 1;
        int xPosEndPoint = RoomArray.get(indexOfRoomArrayForEndPoint).get(4);
        int yPosEndPoint = RoomArray.get(indexOfRoomArrayForEndPoint).get(5);
        ArrayList<Integer> endPointPos = new ArrayList<>();
        endPointPos.add(xPosEndPoint);
        endPointPos.add(yPosEndPoint);

        //set end point on bottom up right or left
        if (randomTiles[xPosEndPoint][yPosEndPoint - RoomArray.get(indexOfRoomArrayForEndPoint).get(3) / 2] == Tileset.WALL) {
            yPosEndPoint = yPosEndPoint - RoomArray.get(indexOfRoomArrayForEndPoint).get(3) / 2;
            endPointPos.set(1, yPosEndPoint);
            randomTiles[xPosEndPoint][yPosEndPoint] = Tileset.UNLOCKED_DOOR;
        } else if (randomTiles[xPosEndPoint][yPosEndPoint + RoomArray.get(indexOfRoomArrayForEndPoint).get(3) / 2] == Tileset.WALL) {
            yPosEndPoint = yPosEndPoint + RoomArray.get(indexOfRoomArrayForEndPoint).get(3) / 2;
            endPointPos.set(1, yPosEndPoint);
            randomTiles[xPosEndPoint][yPosEndPoint] = Tileset.UNLOCKED_DOOR;
        } else if (randomTiles[xPosEndPoint + RoomArray.get(indexOfRoomArrayForEndPoint).get(2) / 2][yPosEndPoint] == Tileset.WALL) {
            xPosEndPoint = xPosEndPoint + RoomArray.get(indexOfRoomArrayForEndPoint).get(2) / 2;
            endPointPos.set(0, xPosEndPoint);
            randomTiles[xPosEndPoint][yPosEndPoint] = Tileset.UNLOCKED_DOOR;
        } else {
            xPosEndPoint = xPosEndPoint - RoomArray.get(indexOfRoomArrayForEndPoint).get(2) / 2;
            endPointPos.set(0, xPosEndPoint);
            randomTiles[xPosEndPoint][yPosEndPoint] = Tileset.UNLOCKED_DOOR;
        }

        // set avata and end point
        pi = new PlayerInput(initalPos, endPointPos, randomTiles);
        //difference between new world & load world
        if (movement.length == 0) {
            Update('i');
        } else {
            for (int i = 0; i < movement.length; i++) {
                Update(movement[i].charAt(0));
            }
        }

    }

    //update avatar position by updating PlayerInput & store wasd in movement
    public void Update(char input) {
        if (input == 'w' || input == 'a' || input == 's' || input == 'd') {
            movement += input;
            pi.updatePos(input);
        }

        randomTiles[pi.playerPos.get(0)][pi.playerPos.get(1)] = Tileset.ME;
    }

    //Fow swich on and off(when swich off show part of tiles)
    public TETile[][] ReturnPartTile() {
        int xpos = pi.playerPos.get(0);
        int ypos = pi.playerPos.get(1);
        int xlowerbond = xpos - 4;
        int ylowerbond = ypos - 4;
        fillWithRandomTiles(partialTiles);
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (Math.sqrt(Math.pow(xpos - i, 2) + Math.pow(ypos - j, 2)) <= 4) {
                    partialTiles[i][j] = randomTiles[i][j];
                }
            }
        }
        return partialTiles;
    }

    //save game here
    public String saveParameters() {
        String returned = SEED + "," + movement;
        return returned;
    }

    private void DrawWalls(int x, int y) {
        // add walls with index around floor
        // 1 2 3
        // 4 f 5
        // 6 7 8
        if ((x - 1) >= 0 && (y + 1) < HEIGHT && randomTiles[x - 1][y + 1] == Tileset.NOTHING) { // 1
            Walls.add(new int[]{x - 1, y + 1});
        }
        if ((y + 1) < HEIGHT && randomTiles[x][y + 1] == Tileset.NOTHING) { // 2
            Walls.add(new int[]{x, y + 1});
        }
        if ((x + 1) < WIDTH && (y + 1) < HEIGHT && randomTiles[x + 1][y + 1] == Tileset.NOTHING) { // 3
            Walls.add(new int[]{x + 1, y + 1});
        }
        if ((x - 1) >= 0 && randomTiles[x - 1][y] == Tileset.NOTHING) { // 4
            Walls.add(new int[]{x - 1, y});
        }
        if ((x + 1) < WIDTH && randomTiles[x + 1][y] == Tileset.NOTHING) { // 5
            Walls.add(new int[]{x + 1, y});
        }
        if ((x - 1) >= 0 && (y - 1) >= 0 && randomTiles[x - 1][y - 1] == Tileset.NOTHING) { // 6
            Walls.add(new int[]{x - 1, y - 1});
        }
        if ((y - 1) >= 0 && randomTiles[x][y - 1] == Tileset.NOTHING) { // 7
            Walls.add(new int[]{x, y - 1});
        }
        if ((x + 1) < WIDTH && (y - 1) >= 0 && randomTiles[x + 1][y - 1] == Tileset.NOTHING) { // 8
            Walls.add(new int[]{x + 1, y - 1});
        }
    }

    public HashSet<int[]> ConnectRoomsWithCenter(ArrayList<int[]> centers) {
        HashSet<int[]> hallways = new HashSet<>();
        int[] current = centers.get(0);
        centers.remove(current);

        while (centers.size() > 0) {
            int[] closest = ClosestCenter(centers, current);
            centers.remove(closest);
            HashSet<int[]> newHallway = CreateHallways(current, closest);
            current = closest;
            hallways.addAll(newHallway);
        }
        return hallways;
    }

    public HashSet<int[]> CreateHallways(int[] currentCenter, int[] destination) {
        HashSet<int[]> hallway = new HashSet<>();
        int[] position = currentCenter;
        hallway.add(position);
        //link left and right
        while (position[0] != destination[0]) {
            if (destination[0] > position[0]) {
                position[0]++;
            } else if (destination[0] < position[0]) {
                position[0]--;
            }
            int[] temp = new int[2];
            temp[0] = position[0];
            temp[1] = position[1];
            hallway.add(temp);
        }
        //link up and down
        while (position[1] != destination[1]) {
            if (destination[1] > position[1]) {
                position[1]++;
            } else if (destination[1] < position[1]) {
                position[1]--;
            }
            int[] temp = new int[2];
            temp[0] = position[0];
            temp[1] = position[1];
            hallway.add(temp);
        }
        return hallway;
    }

    public int[] ClosestCenter(ArrayList<int[]> roomCenters, int[] currentCenter) {
        int[] closest = new int[2];
        double length = Double.MAX_VALUE;

        int x1 = currentCenter[0];
        int y1 = currentCenter[1];
        for (int[] position : roomCenters) {
            int x2 = position[0];
            int y2 = position[1];
            double currentLength = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
            if (currentLength < length) {
                length = currentLength;
                closest = position;
            }
        }
        return closest;
    }

    public static TETile[][] ReturnTile() {
        return randomTiles;
    }

    public void fillWithRandomTiles(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        //first fill with nothing to initialize
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }

        //draw random n rooms
        int n = RandomUtils.uniform(RANDOM, 5, WIDTH / 2);
        while (n > 0) {
            randomTile();
            n -= 1;
        }
    } //consider different theme, potential character

    private void randomTile() {
        //random room width and height
        ...
    }

    public void mouseHover() {

        int mouseX = (int) StdDraw.mouseX();
        int mouseY = (int) StdDraw.mouseY();

        StdDraw.setPenColor(Color.BLACK);


        if (mouseX < WIDTH && mouseY < HEIGHT) {
            double x = 6.0;
            StdDraw.filledRectangle(x, HEIGHT - 1, 8.0, 1.0);
            if (randomTiles[mouseX][mouseY].equals(Tileset.WALL)) {
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.textLeft(2, HEIGHT - 1, "This is wall");
                StdDraw.show();
                StdDraw.pause(15);
            } else if (randomTiles[mouseX][mouseY].equals(Tileset.FLOORWOOD)) {
                ...
            } else if (randomTiles[mouseX][mouseY].equals(Tileset.ME)) {
                ...
            } else if (randomTiles[mouseX][mouseY].equals(Tileset.UNLOCKED_DOOR)) {
                ...
            }
        }
    }

}
