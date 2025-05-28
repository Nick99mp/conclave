import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

// Class to manage scrutinies
public class Conclave extends Thread {
    static Board board;
    static ArrayList<Cardinal> cardinals;
    static int[] votes;
    static int pope;
    static Object isPopeElected = new Object();
    static boolean popeElected = false;

    public Conclave(int boardSize, String csvPath) throws ConclaveSetupException {
        setBoard(boardSize);
        setCardinals(csvPath);
    }

    @Override
    public void run() {

        try {           

            while (true) {
                
                spawnCardinals();
                sleep(20000);

                votes = new int[cardinals.size()];
                for (int i = 0; i < votes.length; i++) {
                    votes[i] = 0;
                }
                pope = 0;

                for (Cardinal cardinal : cardinals) {
                    cardinal.interrupt();
                }

                for (Cardinal cardinal : cardinals) {
                    while (cardinal.isAlive()) {
                    }
                }

                for (int i = 0; i < votes.length; i++) {
                    pope = votes[i] > votes[pope] ? i : pope;
                }

                // System.out.println("ALL DONE");
                if (votes[pope] > (int) Math.floor((cardinals.size() / 3)) * 2) {

                    System.out.println("Pope elected: " + cardinals.get(pope).name + " " + cardinals.get(pope).surname + " with " + votes[pope] + " votes. (target: " + (int) Math.floor((cardinals.size() / 3)) * 2 + ")");

                    synchronized (isPopeElected) {
                        isPopeElected.notify();
                    }

                } else {

                    System.out.println("No pope elected. The cardinal with the most votes is " + cardinals.get(pope).name + " " + cardinals.get(pope).surname + " with " + votes[pope] + " votes. (target: " + (int) Math.floor((cardinals.size() / 3)) * 2 + ")");
                    System.out.println("The conclave will continue.");
                }

            }


        } catch (InterruptedException e) {

            for (Cardinal cardinal : cardinals) {
                cardinal.interrupt();
            }
        }
    }
    

    // Create room's board
    void setBoard(int side) {
        if (side < 2) {
            throw new IllegalArgumentException("Board size not allowed.");
        }

        board = new Board(side);
    }

    // Populate cardinals' list
    void setCardinals(String csvPath) throws ConclaveSetupException {
        if (csvPath.isBlank()) {
            throw new IllegalArgumentException("Blank file path.");
        }

        cardinals = new ArrayList<>();

        try {
            Reader reader = new Reader(csvPath);
            while(reader.hasRow()) {
                String[] data = reader.getRowData();
                cardinals.add(
                    new Cardinal(
                        data[0],
                        data[1],
                        Integer.parseInt(data[2])
                    )
                );
            }
        } catch (FileNotFoundException e) {
            throw new ConclaveSetupException("CSV file path not found.");
        }
    }

    void spawnCardinals() {
        for (Cardinal cardinal : cardinals) {
            cardinal.start();
        }
    }
}
