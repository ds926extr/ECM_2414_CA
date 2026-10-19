import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class CardGame {
    public static void main(String[] args) {
        ArrayList<Card> cards;
        Scanner myObj = new Scanner(System.in);

        int numberOfPlayers;

        //gets and validates number of players from user input
        while (true) {
            System.out.println("Enter number of players");

            if (!myObj.hasNextInt()) { //checks if input is an integer
                System.out.println("Invalid input. Please enter a positive integer");
                myObj.next();
                continue;
            }

            numberOfPlayers = myObj.nextInt();

            if (numberOfPlayers <= 0) { //checks if input is a positive integer
                System.out.println("Invalid input. Please enter a positive integer");
                continue;
            }

            break;
        }

        System.out.println("number of players is " + numberOfPlayers);

        myObj.nextLine();

        while (true) {
            //opens and validates pack file
            System.out.println("Enter filename: ");

            String fileName = myObj.nextLine();

            ArrayList<Card> tempCards = new ArrayList<>();

            try { //opens file
                File packFile = new File(fileName);

                Scanner fileScanner = new Scanner(packFile);

                int count = 0;

                boolean packValid = true;

                System.out.println("File opened successfully");

                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine().trim();

                    try {
                        int value = Integer.parseInt(line);

                        if (value < 0) {
                            System.out.println("Content of file is invalid. Must be a non negative integer");
                            packValid = false;
                        } else {
                            count++;

                            Card card = new Card(value);
                            tempCards.add(card);
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Content of file is invalid. Must contain one integer per line");
                        packValid = false;
                    }
                }

                fileScanner.close();

                //validates content of pack file
                if (packValid && count == 8 * numberOfPlayers) { //checks pack file is valid and includes 8n cards
                    System.out.println("Pack is valid");
                    cards = tempCards;
                    break;
                } else {
                    System.out.println("Pack is invalid. Please enter a valid pack.");
                }

            } catch (FileNotFoundException e) {
                System.out.println("File could not be found");
            }
        }

        myObj.close();

        //creates n number of decks
        ArrayList<CardDeck> decks = new ArrayList<>();

        for (int i = 1; i <= numberOfPlayers; i++) {
            CardDeck deck = new CardDeck(i);
            decks.add(deck);
        }

        //creates players and assigns decks to each player
        AtomicBoolean gameIsOver = new AtomicBoolean(false);

        ArrayList<Player> players = new ArrayList<>();

        for (int i = 0; i < numberOfPlayers; i++) {
            int playerNumber = i + 1;

            CardDeck sourceDeck = decks.get(i);

            CardDeck destinationDeck = decks.get((i + 1) % numberOfPlayers);

            Player player = new Player(playerNumber, sourceDeck, destinationDeck, gameIsOver);

            players.add(player);
        }

        //hands out 4 cards to each player
        int cardIndex = 0;

        for (int round = 0; round < 4; round++) {
            for (int i = 0; i < players.size(); i++) {
                Card card = cards.get(cardIndex);

                Player player = players.get(i);

                player.receiveCard(card);

                cardIndex++;
            }
        }

        //puts the rest of the cards in decks
        while (cardIndex < cards.size()) {
            for (int i = 0; i < decks.size() && cardIndex < cards.size(); i++) {
                Card card = cards.get(cardIndex);

                CardDeck deck = decks.get(i);

                deck.addCard(card);

                cardIndex++;
            }
        }

        //starts threads
        ArrayList<Thread> threads = new ArrayList<>();

        for (Player player : players) {
            Thread thread = new Thread(player);
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Interrupted while waiting for player threads.");
                return;
            }
        }

        for (CardDeck deck : decks) {
            deck.writeOutput();
        }
    }
}
