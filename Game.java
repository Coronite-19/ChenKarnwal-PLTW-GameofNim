import java.util.Scanner;

/**
 * Main game class that controls game flow
 */
public class Game {
    private Player[] players;           // Array to hold player objects
    private Board gameBoard;            // The board object managing the pile of pieces
    private boolean isGameActive;       // Flag to keep track of game status
    private int currentPlayerIndex;     // Tracks the index of the current player
    private Scanner scanner;            // Scanner for user input

    // Constructor initializes the game and sets up players
    public Game() {
        scanner = new Scanner(System.in);
        initialize();
    }

    // Sets up players, game board, and randomly selects the first player
    private void initialize() {
        players = new Player[2];
        gameBoard = new Board();  // Add this line to create the game board
        Random random = new Random();
        
        System.out.println("\nPlayer 1, enter your name: ");
        String player1Name = scanner.nextLine();
        players[0] = new Player(player1Name, false);
        
        // If player1 is Computer1, automatically set player2 as Computer2
        if (player1Name.toLowerCase().contains("computer1")) {
            players[1] = new Player("computer2", true);
            System.out.println("Auto-setting Player 2 as computer2");
        } else {
            // Regular flow for human players
            System.out.println("Would you like to play against computer? (y/n): ");
            boolean vsComputer = scanner.nextLine().trim().toLowerCase().startsWith("y");
            
            if (vsComputer) {
                players[1] = new Player("computer", true);
            } else {
                System.out.println("Player 2, enter your name: ");
                players[1] = new Player(scanner.nextLine(), false);
            }
        }
    
        // Randomly swap players to randomize who goes first
        if (random.nextBoolean()) {
            Player temp = players[0];
            players[0] = players[1];
            players[1] = temp;
            System.out.println("\n" + players[0].getName() + " will go first!");
        } else {
            System.out.println("\n" + players[0].getName() + " will go first!");
        }
    }

    // Main method to control the gameplay loop
    public void play() {
        do {
            playOneGame();      // Plays a single game
        } while (playAgain());  // Asks if players want to play again
        
        announceGameResults();  // Displays final results after all games are done
    }

    // Manages the flow of a single game
    private void playOneGame() {
        isGameActive = true;
        gameBoard.randomizePileSize();  // Sets up a random pile size for the game
        
        // Assigns a new random power-up to each player at the start of each game
        for (Player player : players) {
            player.assignRandomPowerUp();
        }
        
        System.out.println("\nStarting new game with " + gameBoard.getPileSize() + " pieces");
        System.out.println(players[currentPlayerIndex].getName() + " goes first!");
        
        // Display power-ups for non-computer players
        for (Player player : players) {
            if (!player.isComputer()) {
                System.out.println(player.getName() + " received power-up: " + 
                                 player.getCurrentPowerUp().getName());
                System.out.println("(" + player.getCurrentPowerUp().getDescription() + ")");
            }
        }
        
        // Loop until the game ends
        while (isGameActive) {
            gameBoard.displayBoard();    // Display current pile status
            takeTurn();                  // Handle current player's turn
            
            if (gameBoard.isEmpty()) {   // Check if the pile is empty after the turn
                handleGameEnd();         // End the game if no pieces are left
            } else {
                switchPlayer();          // Move to the next player's turn
            }
        }
    }

    // Handles a single turn for the current player
    private void takeTurn() {
        Player currentPlayer = players[currentPlayerIndex];
        System.out.println("\n" + currentPlayer.getName() + "'s turn");
        
        // Allow player to use their power-up if available
        if (currentPlayer.hasPowerUp()) {
            handlePowerUpOption(currentPlayer);
        }

        int pieces;
        if (currentPlayer.isComputer()) {
            // For AI players, use computerMove() to determine the number of pieces to take
            pieces = currentPlayer.computerMove(gameBoard.getPileSize());
            System.out.println("Computer takes " + pieces + " pieces");
        } else {
            // For human players, ask for a valid move
            pieces = getValidPlayerMove();
        }
        
        gameBoard.removePieces(pieces);  // Update board after pieces are taken
    }

    // Prompts human player for a valid move within allowed limits
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
            scanner.nextLine(); // Consume newline character
            
            // Check if the move is valid
            if (!gameBoard.checkMoveValid(pieces)) {
                System.out.println("Invalid move! Please try again.");
            }
        } while (!gameBoard.checkMoveValid(pieces));
        
        return pieces;
    }

    // Handles the player's choice to use their power-up during their turn
    private void handlePowerUpOption(Player player) {
        System.out.println("\nYou have a " + player.getCurrentPowerUp().getName() + 
                          " power-up available!");
        System.out.println("Description: " + player.getCurrentPowerUp().getDescription());
        System.out.println("Would you like to use it? (y/n): ");
        
        if (scanner.nextLine().trim().toLowerCase().startsWith("y")) {
            executePowerUp(player);
        }
    }

    // Executes the chosen power-up action
    private void executePowerUp(Player player) {
        switch (player.getCurrentPowerUp()) {
            case DOUBLE_TURN:
                handleDoubleTurn(player);
                break;
            case ADD_PIECES:
                handleAddPieces(player);
                break;
        }
        player.usePowerUp(); // Mark power-up as used
    }

    // Executes the DOUBLE_TURN power-up, giving the player an extra turn
    private void handleDoubleTurn(Player player) {
        System.out.println("\n*** DOUBLE TURN ACTIVATED! ***");
        System.out.println(player.getName() + " will get another turn!");
        currentPlayerIndex = (currentPlayerIndex - 1 + players.length) % players.length;
    }

    // Executes the ADD_PIECES power-up, adding pieces back to the pile
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
            scanner.nextLine(); // Consume newline character
        } while (piecesToAdd < minAdd || piecesToAdd > maxAdd);
        
        gameBoard.addPieces(piecesToAdd); // Add pieces to the pile
        System.out.println(piecesToAdd + " pieces added to the pile!");
    }

    // Handles the end of a game round when the pile is empty
    private void handleGameEnd() {
        isGameActive = false;
        int winnerIndex = (currentPlayerIndex + 1) % 2; // The other player wins
        players[winnerIndex].addPoints(1);              // Add a point to the winner's score
        
        System.out.println("\n" + players[winnerIndex].getName() + " wins!");
    }

    // Switches the turn to the other player
    private void switchPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
    }

    // Asks the players if they would like to play another game
    private boolean playAgain() {
        System.out.println("\nWould you like to play again? (y/n): ");
        return scanner.nextLine().trim().toLowerCase().startsWith("y");
    }

    // Displays the final scores of all players
    private void announceGameResults() {
        System.out.println("\nFinal Scores:");
        for (Player player : players) {
            System.out.println(player.getName() + ": " + player.getPoints() + " wins");
        }
        System.out.println("Thanks for playing!");
    }
}
