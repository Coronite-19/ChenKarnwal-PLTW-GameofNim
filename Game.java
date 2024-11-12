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

    private void playOneGame() {
        isGameActive = true;
        gameBoard.randomizePileSize();
        
        // Assign new power-ups for each game
        for (Player player : players) {
            player.assignRandomPowerUp();
        }
        
        System.out.println("\nStarting new game with " + gameBoard.getPileSize() + " pieces");
        System.out.println(players[currentPlayerIndex].getName() + " goes first!");
        
        // Show power-ups to players
        for (Player player : players) {
            if (!player.isComputer()) {
                System.out.println(player.getName() + " received power-up: " + 
                                 player.getCurrentPowerUp().getName());
                System.out.println("(" + player.getCurrentPowerUp().getDescription() + ")");
            }
        }
        
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

    private void takeTurn() {
        Player currentPlayer = players[currentPlayerIndex];
        System.out.println("\n" + currentPlayer.getName() + "'s turn");
        
        // Show power-up option if available
        if (currentPlayer.hasPowerUp()) {
            handlePowerUpOption(currentPlayer);
        }

        int pieces;
        if (currentPlayer.isComputer()) {
            pieces = currentPlayer.computerMove(gameBoard.getPileSize());
            System.out.println("Computer takes " + pieces + " pieces");
        } else {
            pieces = getValidPlayerMove();
        }
        
        gameBoard.removePieces(pieces);
    }

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

    private void handlePowerUpOption(Player player) {
        System.out.println("\nYou have a " + player.getCurrentPowerUp().getName() + 
                          " power-up available!");
        System.out.println("Description: " + player.getCurrentPowerUp().getDescription());
        System.out.println("Would you like to use it? (y/n): ");
        
        if (scanner.nextLine().trim().toLowerCase().startsWith("y")) {
            executePowerUp(player);
        }
    }

    private void executePowerUp(Player player) {
        switch (player.getCurrentPowerUp()) {
            case DOUBLE_TURN:
                handleDoubleTurn(player);
                break;
            case ADD_PIECES:
                handleAddPieces(player);
                break;
        }
        player.usePowerUp();
    }

    private void handleDoubleTurn(Player player) {
        System.out.println("\n*** DOUBLE TURN ACTIVATED! ***");
        System.out.println(player.getName() + " will get another turn!");
        currentPlayerIndex = (currentPlayerIndex - 1 + players.length) % players.length;
    }

    private void handleAddPieces(Player player) {
        System.out.println("\n*** ADD PIECES ACTIVATED! ***");
        int maxAdd = 5;
        int minAdd = 1;
        System.out.println("How many pieces would you like to add? (" + minAdd + "-" + maxAdd + "): ");
        
        int piecesToAdd;
        do {
            while (!scanner.hasNextInt()) {
                System.out.println("Please enter a valid number!");
                scanner.next();
            }
            piecesToAdd = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        } while (piecesToAdd < minAdd || piecesToAdd > maxAdd);
        
        gameBoard.addPieces(piecesToAdd);
        System.out.println(piecesToAdd + " pieces added to the pile!");
    }

    private void handleGameEnd() {
        isGameActive = false;
        // Current player loses (took last piece)
        int winnerIndex = (currentPlayerIndex + 1) % 2;
        players[winnerIndex].addPoints(1);
        
        System.out.println("\n" + players[winnerIndex].getName() + " wins!");
    }

    private void switchPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
        
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