package poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

//class which contains an array of 52 cards to be used for a poker game
public class DeckOfCards {
	
	private final static int FULL_DECK_SIZE = 52;
	
	private List<PlayingCard> deck;
	//used to count number of cards that have been dealt, which when == 52 indicates no more available cards to deal
	private int cardsDealtCount = 0;
	
	//deck is reinitialized and shuffled for a new game
	public void reset(){
		//all cards are made available for dealing
		cardsDealtCount = 0;
		
		char[] suits = new char[4];
		suits[0] = PlayingCard.HEARTS;
		suits[1] = PlayingCard.DIAMONDS;
		suits[2] = PlayingCard.CLUBS;
		suits[3] = PlayingCard.SPADES;
		
		//making array list thread safe
		deck = Collections.synchronizedList(new ArrayList<PlayingCard>());
		
		int deck_index = 0;
		//for each suit, it adds the Ace, then loops from 2-10, then adds Jack, Queen, King after.
		for(int i=0; i<suits.length; i++){
			deck.add(deck_index++, new PlayingCard("A", suits[i], 1, 14));
			for(int j=2; j<=10; j++){
				deck.add(deck_index++, new PlayingCard(Integer.toString(j), suits[i], j, j));
			}
			deck.add(deck_index++, new PlayingCard("J", suits[i], 11, 11));
			deck.add(deck_index++, new PlayingCard("Q", suits[i], 12, 12));
			deck.add(deck_index++, new PlayingCard("K", suits[i], 13, 13));
			
		}
		shuffle();
	}
	
	//gets two random indexes of the deck and swaps both cards at these indexes, does this deck-size squared number of times
	public void shuffle(){
		Random rand = new Random();
		for(int i=0; i<FULL_DECK_SIZE*FULL_DECK_SIZE; i++){
			int num1 = rand.nextInt(FULL_DECK_SIZE);
			int num2 = rand.nextInt(FULL_DECK_SIZE);
			PlayingCard temp = deck.get(num1);
			deck.set(num1, deck.get(num2));
			deck.set(num2, temp);
		}
	}
	
	//removes next card to be dealt from deck and returns the card.
	public PlayingCard dealNext(){
		//if cardsDealtCount == 52 then the deck is empty or only contains discarded cards, so no cards available - returns null
		if(cardsDealtCount == FULL_DECK_SIZE){
			return null;
		}
		PlayingCard temp = deck.remove(0);
		cardsDealtCount++;
		return temp;
	}
	
	//adds card discarded by player to the bottom/back of deck
	public void returnCard(PlayingCard discarded){
		deck.add(discarded);
	}
	
	public static void main(String[] args){
		DeckOfCards deck_test = new DeckOfCards();  
		deck_test.reset();
		 
		//deals each card then prints it
        for (int i = 0; i < FULL_DECK_SIZE; i++) {
            PlayingCard card = deck_test.dealNext();
            System.out.println(i + " " + card);
            //returns 6 discarded cards overall at indexes 0, 10, 20, 30, 40, 50
            if(i%10 == 0) {
            	deck_test.returnCard(card);
            }
        }
        //making sure the 6 discarded cards can't be dealt again - should return null
        for (int i = 0; i < 6; i++) {
        	PlayingCard card = deck_test.dealNext();
            System.out.println(card);
        }
        
        //making sure all cards are available to be dealt after deck is reset
        deck_test.reset();
        for (int i = 0; i < FULL_DECK_SIZE; i++) {
            PlayingCard card = deck_test.dealNext();
            System.out.println(i + " " + card);
        }
	}
}
