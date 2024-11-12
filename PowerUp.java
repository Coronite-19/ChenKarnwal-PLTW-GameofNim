/**
 * Represents available power-ups in the game
 * enum is a special class that represents a group of constants (unchangeable variables)
 */

 public enum PowerUp {
    // Define the power-ups with names and descriptions for each
    DOUBLE_TURN("Double Turn", "Take two turns in a row"),   
    ADD_PIECES("Add Pieces", "Add 1-5 pieces back to the pile"), 
    NONE("None", "No power-up available");                    

    // Store the name and description of the power-up
    private final String name;
    private final String description;

    // Constructor to initialize the name and description for each power-up
    PowerUp(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getter method to retrieve the name of the power-up
    public String getName() {
        return name;
    }

    // Getter method to retrieve the description of the power-up
    public String getDescription() {
        return description;
    }
}