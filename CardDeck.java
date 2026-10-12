import java.util.ArrayDeque;

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

    //adds card to the back of the queue
    public synchronized void addCard(Card card){
        cards.addLast(card);
    }

    //removes the first card and stores it
    public synchronized Card removeCard(){
        Card card = cards.removeFirst();

       return card;
    }
    //prints the cards in a deck
    public synchronized void printDeck(){
        for (Card card: cards){
        System.out.println(card);
         }
    }

}
