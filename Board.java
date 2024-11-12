/**
 * Represents a game board for Nim
 * Manages the pile of pieces and validates moves
 */
public class Board {
    private int pileSize;
    
    public Board() {
        randomizePileSize();
    }
    
    public static void populate() {
        // Initial setup and required by GameRunner to run
        System.out.println("Setting up the game board...");
    }
    
    /**
     * Generates a random pile size between 10 and 50 inclusive
     */
    public void randomizePileSize() {
        pileSize = (int)(Math.random() * 41) + 10; // 41 is range (50-10+1), 10 is min
    }
    
    /**
     * Validates if a move is legal according to game rules
     */
    public boolean checkMoveValid(int amount) {
        if (amount < 1) return false;
        int maxAllowed = pileSize / 2;
        if (maxAllowed < 1) maxAllowed = 1;  // Always allow taking 1 piece
        return amount <= maxAllowed;
    }
    
    /**
     * Removes pieces from the pile if move is valid
     */
    public boolean removePieces(int amount) {
        if (!checkMoveValid(amount)) return false;
        pileSize -= amount;
        return true;
    }

    public boolean addPieces(int amount) {
        pileSize += amount;
        return true;
    }
    
    public boolean isEmpty() {
        return pileSize == 0;
    }
    
    public void displayBoard() {
        System.out.println("\nCurrent pile size: " + pileSize);
    }
    
    public int getPileSize() {
        return pileSize;
    }
}
