/**
 * Represents a player in the game
 * Can be either human or computer
 */
public class Player {
    private String name;
    private int points;
    private boolean isComputer;
    private PowerUp currentPowerUp;
    private boolean powerUpUsed;

    public Player(String name, boolean isComputer) {
        this.name = name;
        this.points = 0;
        this.isComputer = isComputer;
        this.powerUpUsed = false;
        assignRandomPowerUp();
    }

    public void assignRandomPowerUp() {
        if (isComputer) {
            currentPowerUp = PowerUp.NONE;
            return;
        }
        PowerUp[] powerUps = {PowerUp.DOUBLE_TURN, PowerUp.ADD_PIECES};
        currentPowerUp = powerUps[(int)(Math.random() * powerUps.length)];
        powerUpUsed = false;
    }
    
    public String getName() {
        return name;
    }
    
    public int getPoints() {
        return points;
    }
    
    public void addPoints(int points) {
        this.points += points;
    }
    
    public boolean isComputer() {
        return isComputer;
    }
    
    /**
     * Computer's strategy for taking pieces
     */
    public int computerMove(int pileSize) {
        int maxAllowed = pileSize / 2;
        if (maxAllowed < 1) maxAllowed = 1;
        return (int)(Math.random() * maxAllowed) + 1;
    }

    public PowerUp getCurrentPowerUp() {
        return currentPowerUp;
    }

    public boolean hasPowerUp() {
        return !powerUpUsed && currentPowerUp != PowerUp.NONE;
    }

    public void usePowerUp() {
        powerUpUsed = true;
    }

}