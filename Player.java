public class Player {

    private String name;             // Name of the player
    private int points;              // Player's current points
    private boolean isComputer;      // Whether this player is controlled by AI
    private PowerUp currentPowerUp;  // Current power-up assigned to the player
    private boolean powerUpUsed;     // Tracks if the player has used their power-up
    private boolean isStrategic;     // For computer players, defines if they use a strategic approach

    // Constructor to initialize a new player with a name and whether it's a computer or not
    public Player(String name, boolean isComputer) {
        this.name = name;
        this.points = 0;               // Start with zero points
        this.isComputer = isComputer;
        this.powerUpUsed = false;

        // If the name contains "computer", treat this player as an AI
        this.isComputer = isComputer || name.toLowerCase().contains("computer");
        
        // Mark as strategic if named "Computer1" (for strategic vs. random AI comparison)
        this.isStrategic = name.toLowerCase().contains("computer1");

        // Randomly assigns a power-up for the player if they are not a computer
        assignRandomPowerUp();
    }

    /**
     * Determines the move for a computer player based on the pile size.
     * 
     * Strategic computers use an optimal Nim strategy:
     * The goal is to leave a pile size that is one less than a power of 2 (3, 7, 15, etc.)
     * Non-strategic computers choose a random legal move
     * 
     * @param pileSize The current pile size
     * @return The number of pieces to take from the pile
     */
    public int computerMove(int pileSize) {
        int maxAllowed = pileSize / 2;     // Maximum pieces that can be taken per turn
        if (maxAllowed < 1) maxAllowed = 1;

        if (!isStrategic) {
            // For non-strategic computers, return a random number within the allowed range
            return (int)(Math.random() * maxAllowed) + 1;
        }

        // Strategic play for "Computer1": targets optimal pile sizes using the Nim strategy
        int target = findNextTarget(pileSize);
        
        // Calculate the number of pieces needed to reach the target pile size
        int piecesToTake = pileSize - target;
        
        // If move is illegal (0 or greater than maxAllowed), choose the best alternative
        if (piecesToTake == 0 || piecesToTake > maxAllowed) {
            if (pileSize == 1) {
                return 1; // Take the last piece if it's the only option
            }
            // If no optimal move, avoid leaving only 1 piece if possible
            if (maxAllowed > 1 && pileSize - maxAllowed == 1) {
                return maxAllowed - 1;
            }
            return maxAllowed; // Default to taking the maximum allowed pieces
        }

        return piecesToTake;
    }

    /**
     * Finds the nearest target pile size (one less than a power of 2) that is smaller than the current pile size.
     * @param pileSize The current pile size
     * @return The target pile size
     */
    private int findNextTarget(int pileSize) {
        // Common winning target sizes in Nim: 3, 7, 15, 31, 63, etc.
        int[] targets = {3, 7, 15, 31, 63};
        
        // Look for the largest target less than the current pile size
        for (int i = targets.length - 1; i >= 0; i--) {
            if (targets[i] < pileSize) {
                return targets[i];
            }
        }
        return 1; // Default target if no larger target found
    }

    // Basic getter and setter methods
    public String getName() { return name; }
    public int getPoints() { return points; }
    public void addPoints(int points) { this.points += points; }
    public boolean isComputer() { return isComputer; }
    public PowerUp getCurrentPowerUp() { return currentPowerUp; }
    public boolean hasPowerUp() { return !powerUpUsed && currentPowerUp != PowerUp.NONE; }
    public void usePowerUp() { powerUpUsed = true; }
    
    /**
     * Assigns a random power-up to the player.
     * AI players don’t use power-ups, so they get "NONE".
     */
    public void assignRandomPowerUp() {
        if (isComputer) {
            currentPowerUp = PowerUp.NONE;
            return;
        }
        // Assign a random power-up if the player is human
        PowerUp[] powerUps = {PowerUp.DOUBLE_TURN, PowerUp.ADD_PIECES};
        currentPowerUp = powerUps[(int)(Math.random() * powerUps.length)];
        powerUpUsed = false;
    }
}
