package src;
import java.util.ArrayDeque;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CardDeck {
    private final int deckNumber; 
    private final ArrayDeque<Card> cards;


    //storing deck number and stores card objects  in that specific deck
    public CardDeck(int deckNumber) {
        this.deckNumber = deckNumber; 

        cards = new ArrayDeque<Card>();

    }

    public int getDeckNumber(){
        return deckNumber;
    }

    //checks if a deck is empty
    public synchronized boolean isEmpty(){
        return cards.isEmpty();
    }

    //checks the number of cards in a deck
    public synchronized int size(){
        return cards.size();

    //adds card to the back of the queue
    public synchronized void addCard(Card card){
        cards.addLast(card);
    }

    //removes the first card and stores it
    public synchronized Card removeCard(){
        return cards.removeFirst();
    }
        
    //prints the cards in a deck
    public synchronized void printDeck(){
        for (Card card: cards){
        System.out.println(card);
         }
    }

    //writes the output files   
    public synchronized void writeOutput(){
    String fileName = "deck" + deckNumber + "_output.txt";

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))){
        writer.write("deck" + deckNumber + " contents:");

        for (Card card : cards){
            writer.write(" " + card.getValue());
        }

        writer.newLine();
    }catch (IOException e){
        System.out.println("Could not write output file");
    }

}

}
