package cardsTest;

import org.junit.jupiter.api.Test;

import src.CardDeck;
import src.Player;
import src.Card;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicBoolean;

public class CardDeckTest {
    @Test
    void cardsAreRemovedInFIFOOrder(){
        CardDeck deck = new CardDeck(1);

        deck.addCard(new Card(10));
        deck.addCard(new Card(20));
        deck.addCard(new Card(30));

        Card removedFirst = deck.removeCard();
        Card removedSecond = deck.removeCard();
        Card removedThird = deck.removeCard();

        assertEquals(10, removedFirst.getValue());
        assertEquals(20, removedSecond.getValue());
        assertEquals(30, removedThird.getValue());

    }

    @Test
    void deckNumberIsStoredCorrectly(){
        CardDeck deck = new CardDeck(3);

        assertEquals(3, deck.getDeckNumber());
    }

    @Test
    void newDeckIsEmpty(){
        CardDeck deck = new CardDeck(1);

        assertTrue(deck.isEmpty());
    }

    @Test
    void deckIsNotEmptyAfterAddingCard(){
        CardDeck deck = new CardDeck(1);

        deck.addCard(new Card(5));

        assertFalse(deck.isEmpty());
    }

    @Test 
    void deckIsEmptyAfterRemovingLastCard(){
        CardDeck deck = new CardDeck(1);

        deck.addCard(new Card(5));
        deck.removeCard();

        assertTrue(deck.isEmpty());
    }

    @Test
    void nonWinningTurnKeepsFourCards() {

        AtomicBoolean gameIsOver = new AtomicBoolean(false);

        CardDeck deck1 = new CardDeck(1);
        CardDeck deck2 = new CardDeck(2);

        Player player1 = new Player(1, deck1, deck2, gameIsOver);

        // Starting hand is not a winning hand
        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(2));
        player1.receiveCard(new Card(3));

        // Card available to draw
        deck1.addCard(new Card(4));

        player1.performTurn();

        assertEquals(4, player1.getHandSize());
        assertFalse(player1.hasWinningHand());
        assertFalse(gameIsOver.get());
    }

    @Test
    void emptySourceDeckDoesNotChangeHand() {

        AtomicBoolean gameIsOver = new AtomicBoolean(false);

        CardDeck deck1 = new CardDeck(1);
        CardDeck deck2 = new CardDeck(2);

        Player player1 = new Player(1, deck1, deck2, gameIsOver);

        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(2));
        player1.receiveCard(new Card(3));

        boolean result = player1.performTurn();

        assertFalse(result);
        assertEquals(4, player1.getHandSize());
        assertFalse(gameIsOver.get());
        assertTrue(deck2.isEmpty());
    }

    
}
