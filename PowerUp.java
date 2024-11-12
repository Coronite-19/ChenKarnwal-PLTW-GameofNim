/**
 * Represents available power-ups in the game
 */

 enum PowerUp {
    DOUBLE_TURN("Double Turn", "Take two turns in a row"),
    ADD_PIECES("Add Pieces", "Add 1-5 pieces back to the pile"),
    NONE("None", "No power-up available");

    private final String name;
    private final String description;

    PowerUp(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}