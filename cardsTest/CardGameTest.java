package cardsTest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import src.CardGame;
import src.CardDeck;
import src.Card;
import src.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.nio.file.Files;
import java.nio.file.Path;

public class CardGameTest {

    @Test
    void validPackIsAccepted(){
        File file = new File("pack4.txt");

        boolean result = CardGame.isValidPack(file, 4);

        assertTrue(result);
    }

    @Test 
    void packWithTooFewCardsIsRejected(){
        File file = new File("tooFewCards.txt");

        boolean result = CardGame.isValidPack(file, 4);

        assertFalse(result);
    }

    @Test 
    void packWithTooManyCardsisRejected(){
        File file = new File("tooManyCards.txt");

        boolean result = CardGame.isValidPack(file, 4);

        assertFalse(result);
    }

    @Test 
    void packWithNegativeCardIsRejected(){
        File file = new File("negativeCard.txt");

        boolean result = CardGame.isValidPack(file, 4);

        assertFalse(result);
    }

    @Test 
    void packWithNonIntegerIsRejected(){
        File file = new File("nonInteger.txt");

        boolean result = CardGame.isValidPack(file, 4);

        assertFalse(result);
    }

    @Test 
    void missingPackFileIsRejected(){
        File file = new File("cards123.txt");

        boolean result = CardGame.isValidPack(file, 4);

        assertFalse(result);
    }

    @Test 
    void playersAreDealtCardsRoundRobin(){
        // create cards
        ArrayList<Card> cards = new ArrayList<>();

        cards.add(new Card(10));
        cards.add(new Card(20));
        cards.add(new Card(11));
        cards.add(new Card(21));
        cards.add(new Card(12));
        cards.add(new Card(22));
        cards.add(new Card(13));
        cards.add(new Card(23));

        // create the two decks
        CardDeck deck1 = new CardDeck(1);
        CardDeck deck2 = new CardDeck(2);

        AtomicBoolean gameIsOver = new AtomicBoolean(false);

        // create the two players
        Player player1 = new Player(1, deck1, deck2, gameIsOver);
        Player player2 = new Player(2, deck2, deck1, gameIsOver);

        ArrayList<Player> players = new ArrayList<>();

        players.add(player1);
        players.add(player2);

        // perform the deal
        int result =CardGame.dealInitialHands(cards, players);

        // check both players received four cards
        assertEquals(4, player1.getHandSize());
        assertEquals(4, player2.getHandSize());

        // check player 1's cards
        assertEquals(10, player1.getCardValue(0));
        assertEquals(11, player1.getCardValue(1));
        assertEquals(12, player1.getCardValue(2));
        assertEquals(13, player1.getCardValue(3));

        // check player 2's cards
        assertEquals(20, player2.getCardValue(0));
        assertEquals(21, player2.getCardValue(1));
        assertEquals(22, player2.getCardValue(2));
        assertEquals(23, player2.getCardValue(3));

        assertEquals(8, result);

    }

    @Test
    void remainingCardsAreDealtToDecksRoundRobin() {
        ArrayList<Card> cards = new ArrayList<>();

        // first 8 cards represent cards already dealt to players
        for (int i = 0; i < 8; i++) {
            cards.add(new Card(0));
        }

        cards.add(new Card(30));
        cards.add(new Card(40));
        cards.add(new Card(31));
        cards.add(new Card(41));
        cards.add(new Card(32));
        cards.add(new Card(42));
        cards.add(new Card(33));
        cards.add(new Card(43));

        CardDeck deck1 = new CardDeck(1);
        CardDeck deck2 = new CardDeck(2);

        ArrayList<CardDeck> decks = new ArrayList<>();
        decks.add(deck1);
        decks.add(deck2);

        int result = CardGame.dealRemainingCards(cards, decks, 8);

        assertEquals(16, result);

        assertEquals(30, deck1.removeCard().getValue());
        assertEquals(31, deck1.removeCard().getValue());
        assertEquals(32, deck1.removeCard().getValue());
        assertEquals(33, deck1.removeCard().getValue());

        assertEquals(40, deck2.removeCard().getValue());
        assertEquals(41, deck2.removeCard().getValue());
        assertEquals(42, deck2.removeCard().getValue());
        assertEquals(43, deck2.removeCard().getValue());
    }

    @Test
    void everyDeckStartsWithFourCards() {
        ArrayList<Card> cards = new ArrayList<>();

        // 16 cards total because 8 × 2 players = 16
        for (int i = 0; i < 16; i++) {
            cards.add(new Card(i));
        }

        CardDeck deck1 = new CardDeck(1);
        CardDeck deck2 = new CardDeck(2);

        ArrayList<CardDeck> decks = new ArrayList<>();
        decks.add(deck1);
        decks.add(deck2);

        // first 8 cards would already have gone to the players
        int cardIndex = 8;

        CardGame.dealRemainingCards(cards, decks, cardIndex);

        assertEquals(4, deck1.size());
        assertEquals(4, deck2.size());
    }
    
    @Test
    void gameEndsWhenPlayerWins() throws InterruptedException {

        AtomicBoolean gameIsOver = new AtomicBoolean(false);

        CardDeck deck1 = new CardDeck(1);
        CardDeck deck2 = new CardDeck(2);

        Player player1 = new Player(1, deck1, deck2, gameIsOver);
        Player player2 = new Player(2, deck2, deck1, gameIsOver);

        // Give player 1 an initial winning hand
        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(1));

        // Give player 2 a non-winning hand
        player2.receiveCard(new Card(1));
        player2.receiveCard(new Card(2));
        player2.receiveCard(new Card(3));
        player2.receiveCard(new Card(4));

        Thread thread1 = new Thread(player1);

        thread1.start();
        thread1.join();

        // Player 1 should have ended the game
        assertTrue(gameIsOver.get());

        Thread thread2 = new Thread(player2);

        thread2.start();
        thread2.join(1000);

        // Player 2 should stop because the game is already over
        assertFalse(thread2.isAlive());

        assertEquals(4, player1.getHandSize());
        assertEquals(4, player2.getHandSize());
    }

    @Test
    void playerCanWinAfterDrawingAndDiscarding() throws InterruptedException {

        AtomicBoolean gameIsOver = new AtomicBoolean(false);

        CardDeck deck1 = new CardDeck(1);
        CardDeck deck2 = new CardDeck(2);

        Player player1 = new Player(1, deck1, deck2, gameIsOver);

        // non-winning initial hand
        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(2));

        // card needed to complete the winning hand
        deck1.addCard(new Card(1));

        Thread thread = new Thread(player1);

        thread.start();
        thread.join(1000);

        assertFalse(thread.isAlive());
        assertTrue(gameIsOver.get());
        assertEquals(4, player1.getHandSize());
        assertTrue(player1.hasWinningHand());
        assertEquals(2, deck2.removeCard().getValue());
    }

    @Test
    void nonWinningTurnKeepsFourCards() throws InterruptedException {

        AtomicBoolean gameIsOver = new AtomicBoolean(false);

        CardDeck deck1 = new CardDeck(1);
        CardDeck deck2 = new CardDeck(2);

        Player player1 = new Player(1, deck1, deck2, gameIsOver);

        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(2));
        player1.receiveCard(new Card(3));

        deck1.addCard(new Card(4));

        // run only long enough for one turn / controlled stop

        assertEquals(4, player1.getHandSize());
        assertFalse(player1.hasWinningHand());
        assertFalse(gameIsOver.get());
    }  
    
    @Test
    void outputFilesAreCreatedAfterGameFinishes() throws InterruptedException {

        AtomicBoolean gameIsOver = new AtomicBoolean(false);

        CardDeck deck1 = new CardDeck(1);
        CardDeck deck2 = new CardDeck(2);

        Player player1 = new Player(1, deck1, deck2, gameIsOver);
        Player player2 = new Player(2, deck2, deck1, gameIsOver);

        // Player 1 starts with a winning hand
        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(1));

        // Player 2 has a non-winning hand
        player2.receiveCard(new Card(1));
        player2.receiveCard(new Card(2));
        player2.receiveCard(new Card(3));
        player2.receiveCard(new Card(4));

        // Remove old files first so they cannot cause a false pass
        new File("player1_output.txt").delete();
        new File("player2_output.txt").delete();
        new File("deck1_output.txt").delete();
        new File("deck2_output.txt").delete();

        Thread thread1 = new Thread(player1);
        Thread thread2 = new Thread(player2);

        thread1.start();
        thread1.join();

        thread2.start();
        thread2.join();

        // CardGame normally does this after all threads finish
        deck1.writeOutput();
        deck2.writeOutput();

        assertTrue(new File("player1_output.txt").exists());
        assertTrue(new File("player2_output.txt").exists());
        assertTrue(new File("deck1_output.txt").exists());
        assertTrue(new File("deck2_output.txt").exists());
    }

    @Test
    void playerOutputContainsExpectedInformation() throws Exception {
        AtomicBoolean gameIsOver = new AtomicBoolean(false);

        CardDeck deck1 = new CardDeck(1);
        CardDeck deck2 = new CardDeck(2);

        Player player1 = new Player(1, deck1, deck2, gameIsOver);

        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(1));
        player1.receiveCard(new Card(1));

        File outputFile = new File("player1_output.txt");
        outputFile.delete();

        Thread thread = new Thread(player1);

        thread.start();
        thread.join();

        String output = Files.readString(Path.of("player1_output.txt"));

        assertTrue(output.contains("player 1 initial hand 1 1 1 1"));
        assertTrue(output.contains("player 1 wins"));
        assertTrue(output.contains("player 1 exits"));
        assertTrue(output.contains("player 1 final hand 1 1 1 1"));
    }

    @Test
    void deckOutputContainsCorrectCards() throws Exception {
        CardDeck deck = new CardDeck(1);

        deck.addCard(new Card(5));
        deck.addCard(new Card(8));
        deck.addCard(new Card(3));

        File outputFile = new File("deck1_output.txt");
        outputFile.delete();

        deck.writeOutput();

        String output = Files.readString(Path.of("deck1_output.txt")).trim();

        assertEquals("deck1 contents: 5 8 3", output);
    }

}
