package poker;

import java.util.ArrayList;

//class which contains an array of 5 PlayingCards, with boolean functions to represent hand options

public class HandOfCards {
	
	public static final int HAND_SIZE = 5;
	
	private ArrayList<PlayingCard> hand;
	private DeckOfCards deck;
	
	public DeckOfCards getDeck(){
		return deck;
	}
	
	public HandOfCards(DeckOfCards deck){
		this.deck = deck;
		hand = new ArrayList<PlayingCard>();
		for(int i=0; i<HAND_SIZE; i++){
			hand.add(deck.dealNext());
		}
		sort();
	}
	
	//insertion sort of cards in hand
	private void sort(){
		for(int i=1; i<hand.size(); i++){
			int j=i;
			while((j>0) && (hand.get(j).getFaceValue() > hand.get(j-1).getFaceValue())){
				PlayingCard temp = hand.get(j);
				hand.set(j, hand.get(j-1));
				hand.set(j-1, temp);
				j--;
			}
		}
	}
	
	//helper function which checks all cards in hand are of the same suit
	public boolean allSameSuit(){
		return ((hand.get(0).getSuit() == hand.get(1).getSuit() && hand.get(0).getSuit() == hand.get(2).getSuit() && 
				hand.get(0).getSuit() == hand.get(3).getSuit() && hand.get(0).getSuit() == hand.get(4).getSuit()));
	}
	
	//helper function to check if the cards are in sequential face rank
	public boolean fiveSequential(){
		return (hand.get(1).getFaceValue() == hand.get(0).getFaceValue()-1 && hand.get(2).getFaceValue() == hand.get(1).getFaceValue()-1 && 
			hand.get(3).getFaceValue() == hand.get(2).getFaceValue()-1 && hand.get(4).getFaceValue() == hand.get(3).getFaceValue()-1);
	}
	
	/*helper function which checks for aceHigh sequential hand: A K Q J 10 - hand sorted by faceValue so ace will be last. 
	 * Checks first 4 cards are sequential and then if last card is 9 less than 2nd last (10 & ace)*/
	public boolean fiveSequentialAceHigh(){
		return ((hand.get(1).getFaceValue() == hand.get(0).getFaceValue()-1 && hand.get(2).getFaceValue() == hand.get(1).getFaceValue()-1 && 
				hand.get(3).getFaceValue() == hand.get(2).getFaceValue()-1 && hand.get(4).getFaceValue() == hand.get(3).getFaceValue()-9));
	}
	
	//checks if all suits are the same and the cards are A K Q J 10 
	public boolean isRoyalFlush(){
		return allSameSuit()&& fiveSequentialAceHigh(); 
	}
	
	//checks if all suits are the same and all cards are 1 less than the previous card
	public boolean isStraightFlush(){
		if(isRoyalFlush()){
			return false;
		}
		return 	(allSameSuit() && fiveSequential());
	}
	
	//checks that four cards have the same face
	public boolean isFourOfAKind(){
		return ((hand.get(0).getFaceValue() == hand.get(1).getFaceValue() && 
				hand.get(0).getFaceValue() == hand.get(2).getFaceValue() && 
				hand.get(0).getFaceValue() == hand.get(3).getFaceValue()) ^
				(hand.get(1).getFaceValue() == hand.get(2).getFaceValue() && 
				hand.get(1).getFaceValue() == hand.get(3).getFaceValue() && 
				hand.get(1).getFaceValue() == hand.get(4).getFaceValue()));
	}
	
	//checks for a three of a kind and a pair 
	public boolean isFullHouse(){
		return ((hand.get(0).getFaceValue() == hand.get(1).getFaceValue() && 
				(hand.get(2).getFaceValue() == hand.get(3).getFaceValue() && hand.get(2).getFaceValue() == hand.get(4).getFaceValue())) ^
				(hand.get(3).getFaceValue() == hand.get(4).getFaceValue() && 
				(hand.get(0).getFaceValue() == hand.get(1).getFaceValue() && hand.get(0).getFaceValue() == hand.get(2).getFaceValue())));
	}
	
	//checks that all suits are the same, but not for a royak or straight flush
	public boolean isFlush(){
		if(isRoyalFlush() || isStraightFlush()){
			return false;
		}
		return allSameSuit();
	}
	
	//checks that all cards are 1 less than the previous card, not for a straightFlush or royal flush
	public boolean isStraight() {
		if(isStraightFlush() || isRoyalFlush()){
			return false;
		}
		return fiveSequential() || fiveSequentialAceHigh(); 
	}
	
	//checks for that three cards have the same face
	public boolean isThreeOfAKind(){
		if(isFourOfAKind() || isFullHouse()){
			return false;
		}
		return ((hand.get(0).getFaceValue() == hand.get(1).getFaceValue() && 
				hand.get(0).getFaceValue() == hand.get(2).getFaceValue()) ^
				(hand.get(1).getFaceValue() == hand.get(2).getFaceValue() && 
				hand.get(1).getFaceValue() == hand.get(3).getFaceValue()) ^ 
				(hand.get(2).getFaceValue() == hand.get(3).getFaceValue() && 
				hand.get(2).getFaceValue() == hand.get(4).getFaceValue()));
	}
	
	//checks for two distinct pairs
	public boolean isTwoPair(){
		if(isFullHouse() || isFourOfAKind()) {
			return false;
		}
		return((hand.get(0).getFaceValue() == hand.get(1).getFaceValue()) && 
				(hand.get(3).getFaceValue() == hand.get(4).getFaceValue())) ^ 
				((hand.get(0).getFaceValue() == hand.get(1).getFaceValue()) && 
						(hand.get(2).getFaceValue() == hand.get(3).getFaceValue())) ^
				((hand.get(1).getFaceValue() == hand.get(2).getFaceValue()) && 
						(hand.get(3).getFaceValue() == hand.get(4).getFaceValue())); 
	}
	
	//checks that ant two cards have same face
	public boolean isOnePair(){
		if(isFourOfAKind() || isFullHouse() || isThreeOfAKind() || isTwoPair()){
			return false;
		}
		return((hand.get(0).getFaceValue() == hand.get(1).getFaceValue()) ^ 
				(hand.get(1).getFaceValue() == hand.get(2).getFaceValue()) ^ 
				(hand.get(2).getFaceValue() == hand.get(3).getFaceValue()) ^
				(hand.get(3).getFaceValue() == hand.get(4).getFaceValue()));
	}
	
	//if not other hand exists player chooses highest card they have
	public boolean isHighHand(){
		return !(isRoyalFlush() || isStraightFlush() || isFourOfAKind() || isFullHouse() || isFlush() ||
				isStraight() || isThreeOfAKind() || isTwoPair() || isOnePair());
	}
		
	
	public static void main(String[] args){
		DeckOfCards deck = new DeckOfCards();
		deck.reset();
		HandOfCards test_hand;
		
		//sort test
		test_hand = new HandOfCards(deck);
		test_hand.hand.clear();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand.add(new PlayingCard("2", PlayingCard.CLUBS, 2, 2));
		test_hand.hand.add(new PlayingCard("10", PlayingCard.CLUBS, 10, 10));
		test_hand.hand.add(new PlayingCard("J", PlayingCard.HEARTS, 11, 11));
		test_hand.hand.add(new PlayingCard("K", PlayingCard.DIAMONDS, 13, 13));
		test_hand.hand.add(new PlayingCard("6", PlayingCard.SPADES, 6, 6));
		System.out.println("Unsorted hand:");
		for(int i=0; i<test_hand.hand.size(); i++){
			System.out.println(test_hand.hand.get(i).getFaceValue());
		}
		test_hand.sort();
		System.out.println("Sorted hand:");
		for(int i=0; i<test_hand.hand.size(); i++){
			System.out.println(test_hand.hand.get(i).getFaceValue());
		}
		
		
		//onePair test
		test_hand = new HandOfCards(deck);
		test_hand.hand.clear();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand.add(new PlayingCard("2", PlayingCard.CLUBS, 2, 2));
		test_hand.hand.add(new PlayingCard("10", PlayingCard.CLUBS, 10, 10));
		test_hand.hand.add(new PlayingCard("J", PlayingCard.HEARTS, 11, 11));
		test_hand.hand.add(new PlayingCard("2", PlayingCard.DIAMONDS, 2, 2));
		test_hand.hand.add(new PlayingCard("6", PlayingCard.SPADES, 6, 6));
		test_hand.sort();
		System.out.println("\nOne pair test:");
		System.out.println("One Pair?: "+test_hand.isOnePair());
		System.out.println("High hand?: "+test_hand.isHighHand());
		
		
		//twoPair test
		test_hand = new HandOfCards(deck);
		test_hand.hand.clear();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand.add(new PlayingCard("2", PlayingCard.CLUBS, 2, 2));
		test_hand.hand.add(new PlayingCard("10", PlayingCard.CLUBS, 10, 10));
		test_hand.hand.add(new PlayingCard("6", PlayingCard.HEARTS, 6, 6));
		test_hand.hand.add(new PlayingCard("2", PlayingCard.DIAMONDS, 2, 2));
		test_hand.hand.add(new PlayingCard("6", PlayingCard.SPADES, 6, 6));
		test_hand.sort();
		System.out.println("\nTwo pair test:");
		System.out.println("Two pair?: "+test_hand.isTwoPair());
		System.out.println("One pair?: "+test_hand.isOnePair());

		
		//threeOfAKind test
		test_hand = new HandOfCards(deck);
		test_hand.hand.clear();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand.add(new PlayingCard("2", PlayingCard.CLUBS, 2, 2));
		test_hand.hand.add(new PlayingCard("10", PlayingCard.CLUBS, 10, 10));
		test_hand.hand.add(new PlayingCard("6", PlayingCard.HEARTS, 6, 6));
		test_hand.hand.add(new PlayingCard("2", PlayingCard.DIAMONDS, 2, 2));
		test_hand.hand.add(new PlayingCard("2", PlayingCard.SPADES, 2, 2));
		test_hand.sort();
		System.out.println("\nThree of a kind test:");
		System.out.println("Three of a kind?: "+test_hand.isThreeOfAKind());
		System.out.println("Two pair?: "+test_hand.isTwoPair());
		System.out.println("One pair?: "+test_hand.isOnePair());
		
		
		//straight test
		test_hand = new HandOfCards(deck);
		test_hand.hand.clear();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand.add(new PlayingCard("2", PlayingCard.CLUBS, 2, 2));
		test_hand.hand.add(new PlayingCard("A", PlayingCard.CLUBS, 1, 14));
		test_hand.hand.add(new PlayingCard("5", PlayingCard.HEARTS, 5, 5));
		test_hand.hand.add(new PlayingCard("3", PlayingCard.DIAMONDS, 3, 3));
		test_hand.hand.add(new PlayingCard("4", PlayingCard.SPADES, 4, 4));
		test_hand.sort();
		System.out.println("\nStraight test:");
		System.out.println("Straight?: "+test_hand.isStraight());
		System.out.println("Straight Flush?: "+test_hand.isStraightFlush());
		System.out.println("Royal Flush?: "+test_hand.isRoyalFlush());
		
		
		//straight test - A K Q J 10
		test_hand = new HandOfCards(deck);
		test_hand.hand.clear();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand.add(new PlayingCard("10", PlayingCard.CLUBS, 10, 10));
		test_hand.hand.add(new PlayingCard("A", PlayingCard.CLUBS, 1, 14));
		test_hand.hand.add(new PlayingCard("K", PlayingCard.HEARTS, 13, 13));
		test_hand.hand.add(new PlayingCard("J", PlayingCard.DIAMONDS, 11, 11));
		test_hand.hand.add(new PlayingCard("Q", PlayingCard.SPADES, 12, 12));
		test_hand.sort();
		System.out.println("\nStraight test (Ace High):");
		System.out.println("Straight?: "+test_hand.isStraight());
		System.out.println("Straight Flush?: "+test_hand.isStraightFlush());
		System.out.println("Royal Flush?: "+test_hand.isRoyalFlush());
		
		
		//flush test
		test_hand = new HandOfCards(deck);
		test_hand.hand.clear();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand.add(new PlayingCard("10", PlayingCard.CLUBS, 10, 10));
		test_hand.hand.add(new PlayingCard("A", PlayingCard.CLUBS, 1, 14));
		test_hand.hand.add(new PlayingCard("7", PlayingCard.CLUBS, 7, 7));
		test_hand.hand.add(new PlayingCard("J", PlayingCard.CLUBS, 11, 11));
		test_hand.hand.add(new PlayingCard("2", PlayingCard.CLUBS, 2, 2));
		test_hand.sort();
		System.out.println("\nFlush test:");
		System.out.println("Flush?: "+test_hand.isFlush());
		System.out.println("Straight Flush?: "+test_hand.isStraightFlush());
		System.out.println("Royal Flush?: "+test_hand.isRoyalFlush());
		
		
		//full house test
		test_hand = new HandOfCards(deck);
		test_hand.hand.clear();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand.add(new PlayingCard("10", PlayingCard.CLUBS, 10, 10));
		test_hand.hand.add(new PlayingCard("K", PlayingCard.HEARTS, 13, 13));
		test_hand.hand.add(new PlayingCard("10", PlayingCard.SPADES, 10, 10));
		test_hand.hand.add(new PlayingCard("K", PlayingCard.CLUBS, 13, 13));
		test_hand.hand.add(new PlayingCard("10", PlayingCard.DIAMONDS, 10, 10));
		test_hand.sort();
		System.out.println("\nFull House test:");
		System.out.println("Full House?: "+test_hand.isFullHouse());
		System.out.println("Three of a kind?: "+test_hand.isThreeOfAKind());
		System.out.println("Two pair?: "+test_hand.isTwoPair());
		System.out.println("One pair?: "+test_hand.isOnePair());
		
		
		//four of a kind test
		test_hand = new HandOfCards(deck);
		test_hand.hand.clear();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand.add(new PlayingCard("10", PlayingCard.CLUBS, 10, 10));
		test_hand.hand.add(new PlayingCard("10", PlayingCard.HEARTS, 10, 10));
		test_hand.hand.add(new PlayingCard("10", PlayingCard.SPADES, 10, 10));
		test_hand.hand.add(new PlayingCard("K", PlayingCard.CLUBS, 13, 13));
		test_hand.hand.add(new PlayingCard("10", PlayingCard.DIAMONDS, 10, 10));
		test_hand.sort();
		System.out.println("\nFour of a kind test:");
		System.out.println("Four of a kind?: "+test_hand.isFourOfAKind());
		System.out.println("Three of a kind?: "+test_hand.isThreeOfAKind());
		System.out.println("Two pair?: "+test_hand.isTwoPair());
		System.out.println("One pair?: "+test_hand.isOnePair());
		
		
		//straight flush test
		test_hand = new HandOfCards(deck);
		test_hand.hand.clear();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand.add(new PlayingCard("10", PlayingCard.CLUBS, 10, 10));
		test_hand.hand.add(new PlayingCard("6", PlayingCard.CLUBS, 6, 6));
		test_hand.hand.add(new PlayingCard("8", PlayingCard.CLUBS, 8, 8));
		test_hand.hand.add(new PlayingCard("7", PlayingCard.CLUBS, 7, 7));
		test_hand.hand.add(new PlayingCard("9", PlayingCard.CLUBS, 9, 9));
		test_hand.sort();
		System.out.println("\nStraight Flush test:");
		System.out.println("Straight Flush?: "+test_hand.isStraightFlush());
		System.out.println("Royal Flush?: "+test_hand.isRoyalFlush());
		System.out.println("Flush?: "+test_hand.isFlush());
		System.out.println("Straight?: "+test_hand.isStraight());
		
		
		//royal flush test
		deck.reset();
		test_hand = new HandOfCards(deck);
		test_hand.hand.clear();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand.add(new PlayingCard("10", PlayingCard.CLUBS, 10, 10));
		test_hand.hand.add(new PlayingCard("Q", PlayingCard.CLUBS, 12, 12));
		test_hand.hand.add(new PlayingCard("J", PlayingCard.CLUBS, 11, 11));
		test_hand.hand.add(new PlayingCard("A", PlayingCard.CLUBS, 1, 14));
		test_hand.hand.add(new PlayingCard("K", PlayingCard.CLUBS, 13, 13));
		test_hand.sort();
		System.out.println("\nRoyal Flush test:");
		System.out.println("Royal Flush?: "+test_hand.isRoyalFlush());
		System.out.println("Straight Flush?: "+test_hand.isStraightFlush());
		System.out.println("Flush?: "+test_hand.isFlush());
		System.out.println("Straight?: "+test_hand.isStraight());
	}
}
