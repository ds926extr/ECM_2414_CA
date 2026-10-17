import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

final private CardGame AtomicBoolean = new AtomicBoolean(false);

public class CardGame {
    public static void main(String[] args){
        ArrayList<Card> cards = null;
        Scanner myObj = new Scanner(System.in);

        int numberOfPlayers;
        
        //gets and validates number of players from user input
        while(true){
            System.out.println("Enter number of players");

            if(!myObj.hasNextInt()){    //checks if input is an integer
                System.out.println("Invalid input. Please enter a positive input");
                myObj.next();
                continue;
            }

            numberOfPlayers = myObj.nextInt();

            if (numberOfPlayers <= 0){  //checks if input is a positive integer
                System.out.println("Invalid input. Please enter a positive integer");
                continue;
            }

            

            break;
        }
        System.out.println("number of players is " + numberOfPlayers);

        myObj.nextLine();

     while (true){
        //opens and validates pack file
        System.out.println("Enter filename: ");

        String fileName = myObj.nextLine();

        ArrayList<Card> tempCards = new ArrayList<>();
        

        try{    //opens file
            File packFile = new File(fileName);

            Scanner fileScanner = new Scanner(packFile);

            int count = 0;

            boolean packValid = true;

            System.out.println("File opened successfully");

            while (fileScanner.hasNext()){
                
                //checks if file only includes non negative integers / non integer content
                if (fileScanner.hasNextInt()){
                    
                    int value = fileScanner.nextInt();

                    if (value < 0){
                        System.out.println("Content of file is invalid. Must be a non negative integer");
                        packValid = false;
                    }else{
                        count++;

                        Card card = new Card(value);
                        tempCards.add(card);
                    }

                    //counts the number of cards in a pack

                }else{
                    System.out.println("Content of file is invalid. Must be a non negative integer");
                    packValid = false;
                    fileScanner.next();

                }
            }

            fileScanner.close();

            //validates content of pack file
            if (packValid && count == 8 * numberOfPlayers){ //checks pack file is valid and includes 8n cards
                System.out.println("Pack is valid");
                cards = tempCards;
                break;
            }else{
                System.out.println("Pack is invalid. Please enter a valid pack.");
                
            }

           

        } catch (FileNotFoundException e){
            System.out.println("File could not be found");

        }

    }  

    myObj.close();


    //creates n number of decks
    ArrayList<CardDeck> decks = new ArrayList<>();

    for (int i = 1; i <= numberOfPlayers; i++){
        CardDeck deck = new CardDeck(i);
        decks.add(deck);
    }


    }
}

