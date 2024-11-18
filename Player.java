public class Player {

    private String name;             // Name of the player
    private int points;              // Player's current points
    private boolean isComputer;      // Whether this player is controlled by AI
    private PowerUp currentPowerUp;  // Current power-up assigned to the player
    private boolean powerUpUsed;     // Tracks if the player has used their power-up

    // Constructor to initialize a new player with a name and whether it's a computer or not
    public Player(String name, boolean isComputer) {
        this.name = name;
        this.points = 0;               // Start with zero points
        this.isComputer = isComputer;
        this.powerUpUsed = false;

        // If the name contains "computer", treat this player as an AI
        this.isComputer = isComputer || name.toLowerCase().contains("computer");

        // Randomly assigns a power-up for the player if they are not a computer
        assignRandomPowerUp();
    }

    /**
     * Determines the move for a computer player based on the pile size.
     * Computer players choose a random legal move.
     * 
     * @param pileSize The current pile size
     * @return The number of pieces to take from the pile
     */
    public int computerMove(int pileSize) {
        int maxAllowed = pileSize / 2;     // Maximum pieces that can be taken per turn
        if (maxAllowed < 1) maxAllowed = 1;
        
        // Return a random number within the allowed range
        return (int)(Math.random() * maxAllowed) + 1;
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
     * AI players don't use power-ups, so they get "NONE".
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