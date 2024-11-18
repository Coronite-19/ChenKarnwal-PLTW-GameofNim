/**
 * Represents a game board for Nim
 * Manages the pile of pieces and validates moves
 */
public class Board {
    private int pileSize;  // Tracks the current number of pieces in the pile
    
    public Board() {
        randomizePileSize();  // Initialize pile with a random size
    }
    
    public static void populate() {
        // Initial setup method required by GameRunner to start the game
        System.out.println("Setting up the game board...");
    }
    
    /**
     * Generates a random pile size between 10 and 50 inclusive
     */
    public void randomizePileSize() {
        pileSize = (int)(Math.random() * 41) + 10; // Range of 41, minimum is 10
    }
    
    /**
     * Validates if a move is legal according to game rules
     * @param amount - number of pieces the player wants to take
     * @return true if the move is valid, false otherwise
     */
    public boolean checkMoveValid(int amount) {
        if (amount < 1) return false; // Players must take at least one piece
        int maxAllowed = pileSize / 2;  // Max number allowed is half the pile
        if (maxAllowed < 1) maxAllowed = 1;  // Allow taking at least one piece
        return amount <= maxAllowed;
    }
    
    /**
     * Removes pieces from the pile if move is valid
     * @param amount - number of pieces to remove
     * @return true if removal was successful, false otherwise
     */
    public boolean removePieces(int amount) {
        if (!checkMoveValid(amount)) return false; // Check if move is legal
        pileSize -= amount; // Deduct pieces from the pile
        return true;
    }

    /**
     * Adds a specified number of pieces to the pile
     */
    public boolean addPieces(int amount) {
        pileSize += amount;
        return true;
    }
    
    /**
     * Checks if the pile is empty
     * @return true if pile size is zero, false otherwise
     */
    public boolean isEmpty() {
        return pileSize == 0;
    }
    
    /**
     * Displays the current pile size
     */
    public void displayBoard() {
        System.out.println("\nCurrent pile size: " + pileSize);
    }
    
    /**
     * @return current pile size
     */
    public int getPileSize() {
        return pileSize;
    }
}
