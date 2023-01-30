// import
...

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    //get path to txt file
    public static final String savefile = "savegame.txt";
    //changed
    WorldGenerator WG = null;
    boolean enterGame = false;
    boolean endGame = false;
    boolean switchOn = true;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        // Create start interface
        ...
        //Reaction from user
        StringBuilder seed = new StringBuilder();
        boolean quitColon = false;

        while (true && !endGame) {
            //problem
            HUD();
            MouseHovering();
            if (StdDraw.hasNextKeyTyped()) {
                char next = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (next == 'n') {
                    //prompt
                    ...
                    while (!enterGame) {
                        if (StdDraw.hasNextKeyTyped()) {
                            char seedInt = Character.toLowerCase(StdDraw.nextKeyTyped());
                            if (seedInt == 's') {
                                enterGame = true;
                                WG = new WorldGenerator(String.valueOf(seed), new String[0], ter);
                            } else if (Character.isDigit(seedInt)) {
                                seed.append(seedInt);
                            }
                        }
                        //StdDraw setup
                        ...
                    }
                }
                //valid quit?
                else if (next == 'q') {
                    if (!enterGame) {
                        System.exit(1);
                    } else if (quitColon) {
                        //save needed parameters to txt file
                        saveGame(WG);
                        System.exit(1);
                    }
                } else if (next == ':') {
                    quitColon = true;
                } else if (next == 'l') {
                    //load saved game, update WG
                    WG = loadGame();
                    enterGame = true;
                } else if (next == 'o') {
                    //witch on and off
                    if (switchOn == true) {
                        switchOn = false;
                    } else {
                        switchOn = true;
                    }
                }
                //start game
                if (enterGame) {
                    HUD();
                    if (switchOn) {
                        WG.Update(next);
                        ter.renderFrame(WG.ReturnTile());
                        if (WG.pi.endPointPos.equals(WG.pi.playerPos)) {
                            endGame = true;
                            EndGameInterface();
                        }
//                        WG.mouseHover();
                    } else {
                        WG.Update(next);
                        ter.renderFrame(WG.ReturnPartTile());
                        if (WG.pi.endPointPos.equals(WG.pi.playerPos)) {
                            endGame = true;
                            EndGameInterface();
                        }
                    }
                }
            }
        }
    }

    private void MouseHovering() {...}

    private void HUD() {...}

    private void saveGame(WorldGenerator WG) {
        Out out = new Out(savefile);
        String saved = WG.saveParameters();
        out.println(saved);
    }

    private WorldGenerator loadGame() {
        In in = new In(savefile);
        String[] allparameter = in.readAll().split(",");
        String seed = allparameter[0];
        String[] movement = allparameter[1].split("");
        WorldGenerator savedWorld = new WorldGenerator(seed, movement, ter);
        return savedWorld;

    }

    //new win/loose
    public void EndGameInterface() {
        StdDraw.clear(Color.BLACK);
        Font font = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(40, 15, "You win!");
        StdDraw.show();
    }


    public TETile[][] interactWithInputString(String input) {
        WorldGenerator WG = new WorldGenerator(input, new String[0], ter);

        TETile[][] finalWorldFrame = WG.ReturnTile();
        return finalWorldFrame;
    }

}
