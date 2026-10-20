package cardsTest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicBoolean;

import src.CardDeck;
import src.Card;
import src.Player;

public class PlayerTest {
    @Test
    void winningHandIsDetected() {
        AtomicBoolean gameIsOver = new AtomicBoolean(false);

        CardDeck deck1 = new CardDeck(1);
        CardDeck deck2 = new CardDeck(2);

        Player player1 = new Player(1, deck1, deck2, gameIsOver);

        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(1));

        assertTrue(player1.hasWinningHand());
    }

    @Test
    void mixedHandIsNotWinning() { 
        AtomicBoolean gameIsOver = new AtomicBoolean(false);

        CardDeck deck1 = new CardDeck(1);
        CardDeck deck2 = new CardDeck(2);

        Player player1 = new Player(1, deck1, deck2, gameIsOver);

        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(2));

        assertFalse(player1.hasWinningHand());
    }

    @Test
    void findsNonPreferredCardToDiscard() {
        AtomicBoolean gameIsOver = new AtomicBoolean(false);

        CardDeck deck1 = new CardDeck(1);
        CardDeck deck2 = new CardDeck(2);

        Player player1 = new Player(1, deck1, deck2, gameIsOver);

        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(2));
        player1.receiveCard(new Card(3));

        Card cardToDiscard = player1.findCardToDiscard();

        assertEquals(2, cardToDiscard.getValue());
    }

    @Test
    void returnsNullWhenAllCardsArePreferred() {
        AtomicBoolean gameIsOver = new AtomicBoolean(false);

        CardDeck deck1 = new CardDeck(1);
        CardDeck deck2 = new CardDeck(2);

        Player player1 = new Player(1, deck1, deck2, gameIsOver);

        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(1));

        Card cardToDiscard = player1.findCardToDiscard();

        assertNull(cardToDiscard);
    }

    @Test
    void discardCardMovesCardToDestinationDeck() {
        AtomicBoolean gameIsOver = new AtomicBoolean(false);

        CardDeck deck1 = new CardDeck(1);
        CardDeck deck2 = new CardDeck(2);

        Player player1 = new Player(1, deck1, deck2, gameIsOver);

        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(2));
        player1.receiveCard(new Card(3));
        player1.receiveCard(new Card(4));

        Card discarded = player1.discardCard(deck2);

        // Check the chosen card was the first non-preferred card.
        assertEquals(2, discarded.getValue());

        // Hand should have gone from 5 cards back to 4.
        assertEquals(4, player1.getHandSize());

        // Destination deck should contain the discarded card.
        Card cardInDeck = deck2.removeCard();
        assertEquals(2, cardInDeck.getValue());
    }

    @Test
    void discardCardStillDiscardsWhenAllCardsArePreferred() {
        AtomicBoolean gameIsOver = new AtomicBoolean(false);

        CardDeck deck1 = new CardDeck(1);
        CardDeck deck2 = new CardDeck(2);

        Player player1 = new Player(1, deck1, deck2, gameIsOver);

        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(1));

        Card discarded = player1.discardCard(deck2);

        assertNotNull(discarded);
        assertEquals(1, discarded.getValue());

        assertEquals(4, player1.getHandSize());

        Card cardInDeck = deck2.removeCard();
        assertEquals(1, cardInDeck.getValue());
    }

    @Test
    void drawCardMovesCardFromDeckToHand() {
        AtomicBoolean gameIsOver = new AtomicBoolean(false);

        CardDeck deck1 = new CardDeck(1);
        CardDeck deck2 = new CardDeck(2);

        Player player1 = new Player(1, deck1, deck2, gameIsOver);

        // Give the player an initial 4-card hand
        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(2));
        player1.receiveCard(new Card(3));

        // Put a card in the source deck
        deck1.addCard(new Card(7));

        Card drawn = player1.drawCard(deck1);

        // Check the drawn card's value
        assertEquals(7, drawn.getValue());
        // Check the hand has increased from 4 to 5
        assertEquals(5, player1.getHandSize());
        // Check the source deck is now empty
        assertTrue(deck1.isEmpty());
    }

    @Test
    void performTurnDoesNothingIfGameIsAlreadyOver() {
        AtomicBoolean gameIsOver = new AtomicBoolean(true);

        CardDeck deck1 = new CardDeck(1);
        CardDeck deck2 = new CardDeck(2);

        Player player1 = new Player(1, deck1, deck2, gameIsOver);

        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(2));
        player1.receiveCard(new Card(3));

        // Put a card in the source deck.
        // If performTurn accidentally continues, this could be drawn.
        deck1.addCard(new Card(7));

        boolean result = player1.performTurn();

        assertFalse(result);
        assertEquals(4, player1.getHandSize());
        assertEquals(1, deck1.size());
        assertTrue(deck2.isEmpty());
        assertTrue(gameIsOver.get());
    }

}
