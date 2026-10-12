import java.util.ArrayList;

public class Player {
    //obtains the player number
    //final private as this variable should not change once the game starts
    final private int playerNumber;

    //sets the current player number as the parameter used throughout the game
    public Player(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    //retrieves the private player number when required during the game is executed
    public int getPlayerNumber() {
        return playerNumber;
    }

    //initiates an empty array list using the Card objects, later used to store a player's hand
    ArrayList<Card> currentHand = new ArrayList<Card>();

}