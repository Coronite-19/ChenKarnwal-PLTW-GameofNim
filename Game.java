import java.util.Scanner;
/**
 * Main game class that controls game flow
 */
public class Game {
    private Player[] players;
    private Board gameBoard;
    private boolean isGameActive;
    private int currentPlayerIndex;
    private Scanner scanner;
    
    public Game() {
        scanner = new Scanner(System.in);
        initialize();
    }
    
    /**
     * Sets up the game with players and board
     */
    private void initialize() {
        players = new Player[2];
        
        System.out.println("\nPlayer 1, enter your name: ");
        players[0] = new Player(scanner.nextLine(), false);
        
        System.out.println("Would you like to play against computer? (y/n): ");
        boolean vsComputer = scanner.nextLine().trim().toLowerCase().startsWith("y");
        
        if (vsComputer) {
            players[1] = new Player("Computer", true);
        } else {
            System.out.println("Player 2, enter your name: ");
            players[1] = new Player(scanner.nextLine(), false);
        }
        
        gameBoard = new Board();
        currentPlayerIndex = (int)(Math.random() * 2);  // Randomly choose first player
    }
    
    public void play() {
        do {
            playOneGame();
        } while (playAgain());
        
        announceGameResults();
    }
    
    /**
     * Plays a single game of Nim
     */
    private void playOneGame() {
        isGameActive = true;
        gameBoard.randomizePileSize();
        
        System.out.println("\nStarting new game with " + gameBoard.getPileSize() + " pieces");
        System.out.println(players[currentPlayerIndex].getName() + " goes first!");
        
        while (isGameActive) {
            gameBoard.displayBoard();
            takeTurn();
            
            if (gameBoard.isEmpty()) {
                handleGameEnd();
            } else {
                switchPlayer();
            }
        }
    }
    
    /**
     * Handles a single player's turn
     */
    private void takeTurn() {
        Player currentPlayer = players[currentPlayerIndex];
        System.out.println("\n" + currentPlayer.getName() + "'s turn");
        
        int pieces;
        if (currentPlayer.isComputer()) {
            pieces = currentPlayer.computerMove(gameBoard.getPileSize());
            System.out.println("Computer takes " + pieces + " pieces");
        } else {
            pieces = getValidPlayerMove();
        }
        
        gameBoard.removePieces(pieces);
    }
    
    /**
     * Gets and validates player move
     */
    private int getValidPlayerMove() {
        int pieces;
        do {
            System.out.println("How many pieces would you like to take? (1-" + 
                             (gameBoard.getPileSize() / 2) + "): ");
            while (!scanner.hasNextInt()) {
                System.out.println("Please enter a valid number!");
                scanner.next();
            }
            pieces = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            if (!gameBoard.checkMoveValid(pieces)) {
                System.out.println("Invalid move! Please try again.");
            }
        } while (!gameBoard.checkMoveValid(pieces));
        
        return pieces;
    }
    
    private void handleGameEnd() {
        isGameActive = false;
        // Current player loses (took last piece)
        int winnerIndex = (currentPlayerIndex + 1) % 2;
        players[winnerIndex].addPoints(1);
        
        System.out.println("\n" + players[winnerIndex].getName() + " wins!");
    }
    
    private void switchPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 2;
    }
    
    private boolean playAgain() {
        System.out.println("\nWould you like to play again? (y/n): ");
        return scanner.nextLine().trim().toLowerCase().startsWith("y");
    }
    
    private void announceGameResults() {
        System.out.println("\nFinal Scores:");
        for (Player player : players) {
            System.out.println(player.getName() + ": " + player.getPoints() + " wins");
        }
        System.out.println("Thanks for playing!");
    }
}