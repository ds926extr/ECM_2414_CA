package src;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class CardGame {

    //checks if a pack is valid
    public static boolean isValidPack(File file, int numberOfPlayers){
        int count = 0;
        boolean packValid = true;

        try{
            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()){
                String line = fileScanner.nextLine().trim();

                try{
                    int value = Integer.parseInt(line);

                        if (value < 0) {
                            System.out.println("Content of file is invalid. Must be a non negative integer");
                            packValid = false;
                        } else {
                            count++;

                        }
                }catch (NumberFormatException e){
                    packValid = false;
                }
                
            }
            fileScanner.close();
            return packValid && count == 8 * numberOfPlayers;
        }catch (FileNotFoundException e){
            return false;
        }
    }

    //deals the starting cards to the players
    public static int dealInitialHands(ArrayList<Card> cards, ArrayList<Player> players){
        int cardIndex = 0;

        for (int round = 0; round < 4; round++) {
            for (int i = 0; i < players.size(); i++) {
                Card card = cards.get(cardIndex);

                Player player = players.get(i);

                player.receiveCard(card);

                cardIndex++;
            }
        }
        return cardIndex;
    }

    //deals the remaining cards after dealing cards to the players first
    public static int dealRemainingCards(ArrayList<Card> cards, ArrayList<CardDeck> decks, int cardIndex){
        
        while (cardIndex < cards.size()) {
            for (int i = 0; i < decks.size() && cardIndex < cards.size(); i++) {
                Card card = cards.get(cardIndex);

                CardDeck deck = decks.get(i);

                deck.addCard(card);

                cardIndex++;
            }
        }
        return cardIndex;
        
    }

    public static void main(String[] args) {
        ArrayList<Card> cards;
        Scanner myObj = new Scanner(System.in);

        int numberOfPlayers;

        //gets and validates number of players from user input
        while (true) {
            System.out.println("Enter number of players");

            //checks if input is an integer
            if (!myObj.hasNextInt()) { 
                System.out.println("Invalid input. Please enter a positive integer");
                myObj.next();
                continue;
            }

            numberOfPlayers = myObj.nextInt();
            //checks if input is a positive integer
            if (numberOfPlayers <= 0) { 
                System.out.println("Invalid input. Please enter a positive integer");
                continue;
            }

            break;
        }

        System.out.println("number of players is " + numberOfPlayers);

        myObj.nextLine();

        while (true) {

    System.out.println("Enter filename: ");
    String fileName = myObj.nextLine();

    File packFile = new File(fileName);

    // use the validation method
    if (!isValidPack(packFile, numberOfPlayers)) {
        System.out.println("Pack is invalid. Please enter a valid pack.");
        continue;
    }

    // pack is valid, so now create the Card objects
    ArrayList<Card> tempCards = new ArrayList<>();

    try {
        Scanner fileScanner = new Scanner(packFile);

        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine().trim();
            int value = Integer.parseInt(line);

            tempCards.add(new Card(value));
        }

        fileScanner.close();

    } catch (FileNotFoundException e) {
        System.out.println("File could not be found");
        continue;
    }

    cards = tempCards;

    System.out.println("Pack is valid");
    break;
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
        int cardIndex = dealInitialHands(cards, players);


        //puts the rest of the cards in decks
        cardIndex = dealRemainingCards(cards, decks, cardIndex);

        //creates a list to keep track of all threads
        ArrayList<Thread> threads = new ArrayList<>();

        //creates and starts threads for each player
        for (Player player : players) {
            Thread thread = new Thread(player);
            threads.add(thread);
            thread.start();
        }

        //waits for threads to finish before continuing
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Interrupted while waiting for player threads.");
                return;
            }
        }

        //writes the decks output files
        for (CardDeck deck : decks) {
            deck.writeOutput();
        }
    }
}
