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
    private final ArrayList<Card> currentHand = new ArrayList<Card>(); //private final to ensure the hand is unchanged throughout the game

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

    //check if a card can be discarded
    public Card findCardToDiscardCard() {
        for (int card = 0; card < currentHand.size(); card++) {
            if (currentHand.get(card).getValue() != playerNumber) {
                System.out.println("This card is eligible to discard");
                return currentHand.get(card); //iterates from the first card and checks to see if they match with the player's number
            } 
        } return null; //returns null if there are no cards to discard
    }

    
    //checks if there is a card to discard. If there is, ensures that card can be discarded
    public void discardCard(CardDeck destinationDeck) {
        Card discardingCard = findCardToDiscardCard();
        if (discardingCard != null) {
            currentHand.remove(discardingCard); //if a card is to be discarded, it will be removed from the current hand
            destinationDeck.addCard(discardingCard); //this card is then added to a deck
        }
    }

    //draws a card from a source deck, removing the first card from the top, and adds it onto the hand/ current hand
    public void drawCard(CardDeck sourceDeck) {
        Card drawnCard = sourceDeck.removeCard();
        receiveCard(drawnCard);
    }

    
}