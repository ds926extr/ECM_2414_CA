package cardsTest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import src.Card;

public class cardTest {
    @Test 
    void cardStoresCorrectValue(){
        Card card = new Card(7);

        assertEquals(7, card.getValue());
    }

    @Test 
    void cardCanStoreZero(){
        Card card = new Card(0);

        assertEquals(0, card.getValue());
    }

    @Test 
    void cardCanStoreLargeValue(){
        Card card = new Card(100);

        assertEquals(100, card.getValue());
    }
}
