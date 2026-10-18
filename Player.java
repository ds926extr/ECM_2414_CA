import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Player implements Runnable {
    final private int playerNumber;
    private CardDeck sourceDeck;
    private CardDeck destinationDeck;
    final private AtomicBoolean gameIsOver;
    private PrintWriter writer;

    //initiates an empty array list using the Card objects, later used to store a player's hand
    private final ArrayList<Card> currentHand = new ArrayList<Card>(); //private final to ensure the hand is unchanged throughout the game

    //ensures the assigned player number, source and destination decks, and game over message are remained unchanged during the game
    public Player(int playerNumber, CardDeck sourceDeck,
                   CardDeck destinationDeck, AtomicBoolean gameIsOver) {
        this.playerNumber = playerNumber;
        this.sourceDeck = sourceDeck;
        this.destinationDeck = destinationDeck;
        this.gameIsOver = gameIsOver;
    }

    //retrieves the private player number when required during the game is executed
    public int getPlayerNumber() {
        return playerNumber;
    }

    //this method adds cards onto the empty array list
    public void receiveCard(Card card) {
        currentHand.add(card);
    }

    //checks if the player has a full hand
    public boolean hasFullHand() {
        if (currentHand.size() == 4) { 
            System.out.println("The hand is full, continue the game");
            return true; //if the hand (array list) is full, returns true
        } else {
            System.out.println("Incorrect number of cards");
            return false; //otherwise returns false
        }
    }

    //checks if a player has a winning hand
    public boolean hasWinningHand() {
       if (currentHand.size() != 4) {
        return false; //returns false if the hand size is not 4
       } for (int card = 1; card < currentHand.size(); card++) { 
        if (currentHand.get(card).getValue() != currentHand.get(0).getValue()) {
            System.out.println("The cards have different values");
            return false; //iterates through the 3, 4 and 5 card to check if its the same as the first card
        } 
        } return true;
    }

    public boolean fourCardsMatch() {
        for (int card = 0; card < currentHand.size(); card++) {
            int value = currentHand.get(card).getValue();
            int count = 0;

            for (int i = 0; i <currentHand.size(); i++) {
                if (currentHand.get(i).getValue() == value) {
                    count++;
                }
            }
            if (count >= 4) {
                return true;
            }
        }
        return false;
    }

    //check if a card can be discarded
    public Card findCardToDiscard() {
        for (int card = 0; card < currentHand.size(); card++) {
            if (currentHand.get(card).getValue() != playerNumber) {
                System.out.println("This card is eligible to discard");
                return currentHand.get(card); //iterates from the first card and checks to see if they match with the player's number
            } 
        } return null; //returns null if there are no cards to discard
    }

    
    //checks if there is a card to discard. If there is, ensures that card can be discarded
    public Card discardCard(CardDeck destinationDeck) {
        Card discardingCard = findCardToDiscard();
        if (discardingCard != null) {
            currentHand.remove(discardingCard); //if a card is to be discarded, it will be removed from the current hand
            destinationDeck.addCard(discardingCard); //this card is then added to a deck
        }
        return discardingCard;
    }

    //draws a card from a source deck, removing the first card from the top, and adds it onto the hand/ current hand
    public Card drawCard(CardDeck sourceDeck) {
        Card drawnCard = sourceDeck.removeCard();
        receiveCard(drawnCard);
        return drawnCard;
    }

    private void log(String message) {
        if (writer != null) {
            writer.println(message);
            writer.checkError();
        }
    }

    private String handToString() {
        StringBuilder s = new StringBuilder();
        for (Card c : currentHand) {
            s.append(c.getValue()).append(" ");
        }
        return s.toString().trim();
    }

    
    @Override 
    public void run() {
        try {
            writer = new PrintWriter(new FileWriter("player" + playerNumber + "_output.txt"));
        } catch (IOException e) {
            System.out.println("Error, the file could not be created for player" + playerNumber);
            return;
        }

        try {
            log("player " + playerNumber + " initial hand " + handToString());
            if (hasWinningHand()) {
                if (gameIsOver.compareAndSet(false, true)) {
                    System.out.println("Player" + playerNumber + "wins!");
                    log("player " + playerNumber + " wins");
                    log("player " + playerNumber + " exits");
                    log("player " + playerNumber + " final hand" + handToString());
                } 
                return;
            } 
            while (!gameIsOver.get()) {
                Card drawn = drawCard(sourceDeck);
                log("player " + playerNumber + " draws a " + drawn.getValue());
    
                if (fourCardsMatch()) {
                    if (gameIsOver.compareAndSet(false, true)) {
                        System.out.println("Player" + playerNumber + "wins!");
                        log("player " + playerNumber + " wins");
                    } 
                    break;
                } 
                Card discarded = discardCard(destinationDeck);
                if (discarded != null) {
                    log("player " + playerNumber + " discards a " + discarded.getValue());
                } 
                log("player " + playerNumber + " current hand " + handToString());
                
            }
            log("player " + playerNumber + " exits");
            log("player " + playerNumber + " final hand " + handToString());
        } finally {
            writer.close();
        }
    }

    



    
}
