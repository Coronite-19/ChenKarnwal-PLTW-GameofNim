public class Player {
    private String name;
    private int points;
    private boolean isComputer;
    private PowerUp currentPowerUp;
    private boolean powerUpUsed;
    private boolean isStrategic; // New field to determine if computer uses strategy

    public Player(String name, boolean isComputer) {
        this.name = name;
        this.points = 0;
        this.isComputer = isComputer;
        this.powerUpUsed = false;
        // If name contains "computer" (case insensitive), it's an AI player
        this.isComputer = isComputer || name.toLowerCase().contains("computer");
        // We'll make Computer1 strategic and Computer2 random for comparison
        this.isStrategic = name.toLowerCase().contains("computer1");
        assignRandomPowerUp();
    }

    /**
     * Implements optimal Nim strategy
     * The winning strategy is to leave a number of pieces that is one less than a power of 2
     * (i.e., 3, 7, 15, 31, 63)
     */
    public int computerMove(int pileSize) {
        int maxAllowed = pileSize / 2;
        if (maxAllowed < 1) maxAllowed = 1;

        if (!isStrategic) {
            // Random strategy for Computer2
            return (int)(Math.random() * maxAllowed) + 1;
        }

        // Strategic play for Computer1
        // Find the largest power of 2 less than or equal to pileSize
        int target = findNextTarget(pileSize);
        
        // Calculate how many pieces to remove to reach target
        int piecesToTake = pileSize - target;
        
        // If pieces to take is invalid (0 or > maxAllowed), take a legal move
        if (piecesToTake == 0 || piecesToTake > maxAllowed) {
            if (pileSize == 1) {
                return 1; // Forced to take last piece
            }
            // If we can't make an optimal move, at least avoid leaving one piece
            if (maxAllowed > 1 && pileSize - maxAllowed == 1) {
                return maxAllowed - 1;
            }
            return maxAllowed; // Take maximum allowed if no better move exists
        }

        return piecesToTake;
    }

    /**
     * Finds the next target position (one less than a power of 2)
     * that is less than the current pile size
     */
    private int findNextTarget(int pileSize) {
        // Common target positions in Nim: 3, 7, 15, 31, 63
        int[] targets = {3, 7, 15, 31, 63};
        
        // Find the largest target that's less than pile size
        for (int i = targets.length - 1; i >= 0; i--) {
            if (targets[i] < pileSize) {
                return targets[i];
            }
        }
        return 1; // If no larger target found, aim for 1
    }

    // Rest of the Player class methods remain the same...
    public String getName() { return name; }
    public int getPoints() { return points; }
    public void addPoints(int points) { this.points += points; }
    public boolean isComputer() { return isComputer; }
    public PowerUp getCurrentPowerUp() { return currentPowerUp; }
    public boolean hasPowerUp() { return !powerUpUsed && currentPowerUp != PowerUp.NONE; }
    public void usePowerUp() { powerUpUsed = true; }
    
    public void assignRandomPowerUp() {
        if (isComputer) {
            currentPowerUp = PowerUp.NONE;
            return;
        }
        PowerUp[] powerUps = {PowerUp.DOUBLE_TURN, PowerUp.ADD_PIECES};
        currentPowerUp = powerUps[(int)(Math.random() * powerUps.length)];
        powerUpUsed = false;
    }
}