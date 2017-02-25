package poker;

import java.util.ArrayList;

//class which contains an array of 5 PlayingCards, with boolean functions to represent hand options and calculation of the hand's game value

public class HandOfCards {
	
	//each max hand possible for each hand can get to 100000's so 1million space in between suffices and is clean for an interval 
	public static final int ONE_PAIR_DEFAULT = 1000000;
	public static final int TWO_PAIR_DEFAULT = 2000000;
	public static final int THREE_OF_A_KIND_DEFAULT = 3000000;
	public static final int STRAIGHT_DEFAULT = 4000000;
	public static final int FLUSH_DEFAULT = 5000000;
	public static final int FULL_HOUSE_DEFAULT = 6000000;
	public static final int FOUR_OF_A_KIND_DEFAULT = 7000000;
	public static final int STRAIGHT_FLUSH_DEFAULT = 8000000;
	public static final int ROYAL_FLUSH_DEFAULT = 9000000;
	
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
	
	//insertion sort of cards in hand by game value, in descending order
	private void sort(){
		for(int i=1; i<hand.size(); i++){
			int j=i;
			while((j>0) && (hand.get(j).getGameValue() > hand.get(j-1).getGameValue())){
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
	
	//helper function to check if the cards are in sequential game value rank
	public boolean fiveSequential(){
		return (hand.get(1).getGameValue() == hand.get(0).getGameValue()-1 && hand.get(2).getGameValue() == hand.get(1).getGameValue()-1 && 
			hand.get(3).getGameValue() == hand.get(2).getGameValue()-1 && hand.get(4).getGameValue() == hand.get(3).getGameValue()-1);
	}
	
	/*helper function which checks for ace low sequential hand: A 2 3 4 5 - hand sorted by gameValue so ace will be first followed by 5. 
	 * Checks if 2nd card is 9 less than first, indicating A&5 cards and 2nd-5th are sequential*/
	public boolean fiveSequentialAceLow(){
		return ((hand.get(1).getGameValue() == hand.get(0).getGameValue()-9 && hand.get(2).getGameValue() == hand.get(1).getGameValue()-1 && 
				hand.get(3).getGameValue() == hand.get(2).getGameValue()-1 && hand.get(4).getGameValue() == hand.get(3).getGameValue()-1));
	}
	
	//checks if all suits are the same and the cards are A K Q J 10 
	public boolean isRoyalFlush(){
		return (allSameSuit() && fiveSequential() && hand.get(0).getGameValue() == 14); 
	}
	
	//checks if all suits are the same and all cards are 1 less than the previous card
	public boolean isStraightFlush(){
		if(isRoyalFlush()){
			return false;
		}
		return 	(allSameSuit() && (fiveSequential() || fiveSequentialAceLow()));
	}
	
	//checks that four cards have the same face
	public boolean isFourOfAKind(){
		return ((hand.get(0).getGameValue() == hand.get(1).getGameValue() && 
				hand.get(0).getGameValue() == hand.get(2).getGameValue() && 
				hand.get(0).getGameValue() == hand.get(3).getGameValue()) ^
				(hand.get(1).getGameValue() == hand.get(2).getGameValue() && 
				hand.get(1).getGameValue() == hand.get(3).getGameValue() && 
				hand.get(1).getGameValue() == hand.get(4).getGameValue()));
	}
	
	//checks for a three of a kind and a pair 
	public boolean isFullHouse(){
		return ((hand.get(0).getGameValue() == hand.get(1).getGameValue() && 
				(hand.get(2).getGameValue() == hand.get(3).getGameValue() && hand.get(2).getGameValue() == hand.get(4).getGameValue())) ^
				(hand.get(3).getGameValue() == hand.get(4).getGameValue() && 
				(hand.get(0).getGameValue() == hand.get(1).getGameValue() && hand.get(0).getGameValue() == hand.get(2).getGameValue())));
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
		return fiveSequential() || fiveSequentialAceLow(); 
	}
	
	//checks for that three cards have the same face
	public boolean isThreeOfAKind(){
		if(isFourOfAKind() || isFullHouse()){
			return false;
		}
		return ((hand.get(0).getGameValue() == hand.get(1).getGameValue() && 
				hand.get(0).getGameValue() == hand.get(2).getGameValue()) ^
				(hand.get(1).getGameValue() == hand.get(2).getGameValue() && 
				hand.get(1).getGameValue() == hand.get(3).getGameValue()) ^ 
				(hand.get(2).getGameValue() == hand.get(3).getGameValue() && 
				hand.get(2).getGameValue() == hand.get(4).getGameValue()));
	}
	
	//checks for two distinct pairs
	public boolean isTwoPair(){
		if(isFullHouse() || isFourOfAKind()) {
			return false;
		}
		return((hand.get(0).getGameValue() == hand.get(1).getGameValue()) && 
				(hand.get(3).getGameValue() == hand.get(4).getGameValue())) ^ 
				((hand.get(0).getGameValue() == hand.get(1).getGameValue()) && 
						(hand.get(2).getGameValue() == hand.get(3).getGameValue())) ^
				((hand.get(1).getGameValue() == hand.get(2).getGameValue()) && 
						(hand.get(3).getGameValue() == hand.get(4).getGameValue())); 
	}
	
	//checks that ant two cards have same face
	public boolean isOnePair(){
		if(isFourOfAKind() || isFullHouse() || isThreeOfAKind() || isTwoPair()){
			return false;
		}
		return((hand.get(0).getGameValue() == hand.get(1).getGameValue()) ^ 
				(hand.get(1).getGameValue() == hand.get(2).getGameValue()) ^ 
				(hand.get(2).getGameValue() == hand.get(3).getGameValue()) ^
				(hand.get(3).getGameValue() == hand.get(4).getGameValue()));
	}
	
	//if not other hand exists player chooses highest card they have
	public boolean isHighHand(){
		return !(isRoyalFlush() || isStraightFlush() || isFourOfAKind() || isFullHouse() || isFlush() ||
				isStraight() || isThreeOfAKind() || isTwoPair() || isOnePair());
	}
	
	//returns a list of the cards involved in the four of a kind
	private ArrayList<PlayingCard> getFourOfAKindCards(){
		ArrayList<PlayingCard> cards = new ArrayList<PlayingCard>();
		for(int i=0; i<HAND_SIZE-3; i++){
				if(hand.get(i).getGameValue() == hand.get(i+1).getGameValue() && hand.get(i+1).getGameValue() == hand.get(i+2).getGameValue() &&
					hand.get(i+2).getGameValue() == hand.get(i+3).getGameValue()){
					cards.add(hand.get(i));
					cards.add(hand.get(i+1));
					cards.add(hand.get(i+2));
					cards.add(hand.get(i+3));
				}
		}
		return cards;
	}
	
	//returns a list of the cards involved in the three of a kind
	private ArrayList<PlayingCard> getThreeOfAKindCards(){
		ArrayList<PlayingCard> cards = new ArrayList<PlayingCard>();
		for(int i=0; i<HAND_SIZE-2; i++){
				if(hand.get(i).getGameValue() == hand.get(i+1).getGameValue() && hand.get(i+1).getGameValue() == hand.get(i+2).getGameValue()){
					cards.add(hand.get(i));
					cards.add(hand.get(i+1));
					cards.add(hand.get(i+2));
				}
		}
		return cards;
	}
	
	//returns list of cards involved in either onePair or twoPair
	private ArrayList<PlayingCard> getPairCards(){
		ArrayList<PlayingCard> cards = new ArrayList<PlayingCard>();
		for(int i=0; i<HAND_SIZE-1; i++){
				if(hand.get(i).getGameValue() == hand.get(i+1).getGameValue()){
					cards.add(hand.get(i));
					cards.add(hand.get(i+1));
				}
		}
		return cards;
	}
	
	//returns a list of the cards not involved in any poker hands, i.e. high cards/kickers
	private ArrayList<PlayingCard> getOtherCards(ArrayList<PlayingCard> cards){
		ArrayList<PlayingCard> otherCards = new ArrayList<PlayingCard>();
			for(int i=0; i<HAND_SIZE; i++){
				if(isTwoPair()){
					if(hand.get(i).getGameValue() != cards.get(0).getGameValue() && hand.get(i).getGameValue() != cards.get(2).getGameValue()) {
						otherCards.add(hand.get(i));
					}
				}
				else if(hand.get(i).getGameValue() != cards.get(0).getGameValue()) {
					otherCards.add(hand.get(i));
				}
			}
			return otherCards;
	}
	
	/*calculates the "worth" of a hand to be compared with other hands in a poker game.
	 * Powers are used when cards involved in poker hands need to be weighted much greater than the other cards.
	 * It stops unimportant cards from having a big weight on hand values
	 */
	public int getGameValue(){
		int val = 0;
		
		//returns default value only as there is no variation of hands
		if(isRoyalFlush()){
			val += HandOfCards.ROYAL_FLUSH_DEFAULT;
		}
		
		//default + highest value card
		if(isStraightFlush()){
			val += HandOfCards.STRAIGHT_FLUSH_DEFAULT + hand.get(0).getFaceValue();
		}
		
		//default + card involved in four of a kind
		if(isFourOfAKind()){
			ArrayList<PlayingCard> fourCards = getFourOfAKindCards();
			val += HandOfCards.FOUR_OF_A_KIND_DEFAULT + fourCards.get(0).getGameValue(); 
		}
		
		//default + card in three of a kind + card in pair. Card in three is weighted more than card in pair
		if(isFullHouse()){
			ArrayList<PlayingCard> pairCards = getPairCards();
			ArrayList<PlayingCard> threeCards = getThreeOfAKindCards();
			val += HandOfCards.FULL_HOUSE_DEFAULT + Math.pow(threeCards.get(0).getGameValue(), 3) + Math.pow(pairCards.get(0).getGameValue(), 2);
		}
		
		//default + each card in hand. Higher value cards are weighted more
		if(isFlush()){
			val += HandOfCards.FLUSH_DEFAULT + Math.pow(hand.get(0).getGameValue(), 5) + Math.pow(hand.get(1).getGameValue(), 4) + 
					Math.pow(hand.get(2).getGameValue(), 3) +	Math.pow(hand.get(3).getGameValue(), 2) + hand.get(4).getGameValue();
		}
		
		//default + highest card
		if(isStraight()){
			//in case of aceLow straight, the 5 card must be used as the highest card
			if(fiveSequentialAceLow()){
				//use 2nd card ("5") as high card, as ace is used as value of 1
				val += HandOfCards.STRAIGHT_DEFAULT + hand.get(1).getGameValue();
			}
			//non-aceLow straight, adds highest card in hand
			else{
				val += HandOfCards.STRAIGHT_DEFAULT + hand.get(0).getGameValue();
			}
		}
		
		//default + card involved in three of a kind
		if(isThreeOfAKind()){
			ArrayList<PlayingCard> threeCards = getThreeOfAKindCards();
			val += HandOfCards.THREE_OF_A_KIND_DEFAULT + threeCards.get(0).getGameValue();
		}
		
		//default + card from each pair + remaining card. Higher ranked pair gets more weight
		if(isTwoPair()){
			ArrayList<PlayingCard> pairCards = getPairCards();
			ArrayList<PlayingCard> otherCards = getOtherCards(pairCards);
			//hand is sorted so first 2 values in pairCards will be highest rank pair
			val += HandOfCards.TWO_PAIR_DEFAULT + Math.pow(pairCards.get(0).getGameValue(), 5) + 
					Math.pow(pairCards.get(2).getGameValue(), 3) + otherCards.get(0).getGameValue();
		}
		
		//default + card from pair + each of the remaining cards. Card from pair given most weight, then decreasing weight for decreasing high cards
		if(isOnePair()) {
			ArrayList<PlayingCard> pairCards = getPairCards();
			ArrayList<PlayingCard> otherCards = getOtherCards(pairCards);
			//cards in otherCards should be sorted
			val += HandOfCards.ONE_PAIR_DEFAULT + Math.pow(pairCards.get(0).getGameValue(), 5) + Math.pow(otherCards.get(0).getGameValue(), 3) + 
					Math.pow(otherCards.get(1).getGameValue(), 2) + otherCards.get(2).getGameValue();
		}
		
		//adds each card with highest card given the most weight and lowest card given the least weight
		if(isHighHand()){
			val += Math.pow(hand.get(0).getGameValue(), 5) + Math.pow(hand.get(1).getGameValue(), 4) + Math.pow(hand.get(2).getGameValue(), 3) +
					Math.pow(hand.get(3).getGameValue(), 2) + hand.get(4).getGameValue();
		}
		return val;
	}
	
	//used for testing
	public String toString(){
		return hand.get(0).getType()+hand.get(0).getSuit() + " " + hand.get(1).getType()+hand.get(1).getSuit() + " " + 
				hand.get(2).getType()+hand.get(2).getSuit() + " " + hand.get(3).getType()+hand.get(3).getSuit() + " " + hand.get(4).getType()+hand.get(4).getSuit();
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
			System.out.println(test_hand.hand.get(i).getGameValue());
		}
		test_hand.sort();
		System.out.println("Sorted hand:");
		for(int i=0; i<test_hand.hand.size(); i++){
			System.out.println(test_hand.hand.get(i).getGameValue());
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
		System.out.println("\nOne pair test: " + test_hand);
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
		System.out.println("\nTwo pair test: " + test_hand);
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
		System.out.println("\nThree of a kind test: " + test_hand);
		System.out.println("Three of a kind?: "+test_hand.isThreeOfAKind());
		System.out.println("Two pair?: "+test_hand.isTwoPair());
		System.out.println("One pair?: "+test_hand.isOnePair());
		
		
		//straight test - A 2 3 4 5
		test_hand = new HandOfCards(deck);
		test_hand.hand.clear();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand.add(new PlayingCard("2", PlayingCard.CLUBS, 2, 2));
		test_hand.hand.add(new PlayingCard("A", PlayingCard.CLUBS, 1, 14));
		test_hand.hand.add(new PlayingCard("5", PlayingCard.HEARTS, 5, 5));
		test_hand.hand.add(new PlayingCard("3", PlayingCard.DIAMONDS, 3, 3));
		test_hand.hand.add(new PlayingCard("4", PlayingCard.SPADES, 4, 4));
		test_hand.sort();
		System.out.println("\nStraight test (Ace low): " + test_hand);
		System.out.println("Straight?: "+test_hand.isStraight());
		System.out.println("Straight Flush?: "+test_hand.isStraightFlush());
		System.out.println("Royal Flush?: "+test_hand.isRoyalFlush());
		
		
		//straight test
		test_hand = new HandOfCards(deck);
		test_hand.hand.clear();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand.add(new PlayingCard("10", PlayingCard.CLUBS, 10, 10));
		test_hand.hand.add(new PlayingCard("A", PlayingCard.CLUBS, 1, 14));
		test_hand.hand.add(new PlayingCard("K", PlayingCard.HEARTS, 13, 13));
		test_hand.hand.add(new PlayingCard("J", PlayingCard.DIAMONDS, 11, 11));
		test_hand.hand.add(new PlayingCard("Q", PlayingCard.SPADES, 12, 12));
		test_hand.sort();
		System.out.println("\nStraight test: " + test_hand);
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
		System.out.println("\nFlush test: " + test_hand);
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
		System.out.println("\nFull House test: " + test_hand);
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
		System.out.println("\nFour of a kind test: " + test_hand);
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
		System.out.println("\nStraight Flush test: " + test_hand);
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
		System.out.println("\nRoyal Flush test: " + test_hand);
		System.out.println("Royal Flush?: "+test_hand.isRoyalFlush());
		System.out.println("Straight Flush?: "+test_hand.isStraightFlush());
		System.out.println("Flush?: "+test_hand.isFlush());
		System.out.println("Straight?: "+test_hand.isStraight());
		
		//onePair game value tests
		HandOfCards test_hand2;
		test_hand = new HandOfCards(deck);
		test_hand.hand.clear();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand.add(new PlayingCard("2", PlayingCard.CLUBS, 2, 2));
		test_hand.hand.add(new PlayingCard("3", PlayingCard.CLUBS, 3, 3));
		test_hand.hand.add(new PlayingCard("J", PlayingCard.HEARTS, 11, 11));
		test_hand.hand.add(new PlayingCard("3", PlayingCard.DIAMONDS, 3, 3));
		test_hand.hand.add(new PlayingCard("6", PlayingCard.SPADES, 6, 6));
		test_hand.sort();
		test_hand2 = new HandOfCards(deck);
		test_hand2.hand.clear();
		test_hand2.hand = new ArrayList<PlayingCard>();
		test_hand2.hand.add(new PlayingCard("2", PlayingCard.CLUBS, 2, 2));
		test_hand2.hand.add(new PlayingCard("10", PlayingCard.CLUBS, 10, 10));
		test_hand2.hand.add(new PlayingCard("J", PlayingCard.HEARTS, 11, 11));
		test_hand2.hand.add(new PlayingCard("2", PlayingCard.DIAMONDS, 2, 2));
		test_hand2.hand.add(new PlayingCard("6", PlayingCard.SPADES, 6, 6));
		test_hand2.sort();
		System.out.println("\nOne pair test: (1) " + test_hand + "  vs.  (2) " + test_hand2);
		System.out.println(test_hand.getGameValue() + " " + test_hand2.getGameValue());
		System.out.println("Game value of (1) is > (2): "+(test_hand.getGameValue() > test_hand2.getGameValue()));
		
		test_hand = new HandOfCards(deck);
		test_hand.hand.clear();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand.add(new PlayingCard("K", PlayingCard.CLUBS, 13, 13));
		test_hand.hand.add(new PlayingCard("K", PlayingCard.HEARTS, 13, 13));
		test_hand.hand.add(new PlayingCard("4", PlayingCard.HEARTS, 4, 4));
		test_hand.hand.add(new PlayingCard("2", PlayingCard.DIAMONDS, 2, 2));
		test_hand.hand.add(new PlayingCard("3", PlayingCard.SPADES, 3, 3));
		test_hand.sort();
		test_hand2 = new HandOfCards(deck);
		test_hand2.hand.clear();
		test_hand2.hand = new ArrayList<PlayingCard>();
		test_hand2.hand.add(new PlayingCard("K", PlayingCard.DIAMONDS, 13, 13));
		test_hand2.hand.add(new PlayingCard("Q", PlayingCard.SPADES, 12, 12));
		test_hand2.hand.add(new PlayingCard("Q", PlayingCard.HEARTS, 12, 12));
		test_hand2.hand.add(new PlayingCard("J", PlayingCard.DIAMONDS, 11, 11));
		test_hand2.hand.add(new PlayingCard("10", PlayingCard.SPADES, 10, 10));
		test_hand2.sort();
		System.out.println("\nOne pair test: (1) " + test_hand + "  vs.  (2) " + test_hand2);
		System.out.println(test_hand.getGameValue() + " " + test_hand2.getGameValue());
		System.out.println("Game value of (1) is > (2): "+(test_hand.getGameValue() > test_hand2.getGameValue()));
		deck.reset();
		
		test_hand = new HandOfCards(deck);
		test_hand.hand.clear();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand.add(new PlayingCard("K", PlayingCard.CLUBS, 13, 13));
		test_hand.hand.add(new PlayingCard("K", PlayingCard.HEARTS, 13, 13));
		test_hand.hand.add(new PlayingCard("10", PlayingCard.HEARTS, 10, 10));
		test_hand.hand.add(new PlayingCard("2", PlayingCard.DIAMONDS, 2, 2));
		test_hand.hand.add(new PlayingCard("3", PlayingCard.SPADES, 3, 3));
		test_hand.sort();
		test_hand2 = new HandOfCards(deck);
		test_hand2.hand.clear();
		test_hand2.hand = new ArrayList<PlayingCard>();
		test_hand2.hand.add(new PlayingCard("K", PlayingCard.DIAMONDS, 13, 13));
		test_hand2.hand.add(new PlayingCard("K", PlayingCard.SPADES, 13, 13));
		test_hand2.hand.add(new PlayingCard("9", PlayingCard.HEARTS, 9, 9));
		test_hand2.hand.add(new PlayingCard("8", PlayingCard.DIAMONDS, 8, 8));
		test_hand2.hand.add(new PlayingCard("7", PlayingCard.SPADES, 7, 7));
		test_hand2.sort();
		System.out.println("\nOne pair test: (1) " + test_hand + "  vs.  (2) " + test_hand2);
		System.out.println(test_hand.getGameValue() + " " + test_hand2.getGameValue());
		System.out.println("Game value of (1) is > (2): "+(test_hand.getGameValue() > test_hand2.getGameValue()));
		
		
		//two pair game value tests
		test_hand = new HandOfCards(deck);
		test_hand.hand.clear();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand.add(new PlayingCard("K", PlayingCard.DIAMONDS, 13, 13));
		test_hand.hand.add(new PlayingCard("K", PlayingCard.SPADES, 13, 13));
		test_hand.hand.add(new PlayingCard("7", PlayingCard.HEARTS, 7, 7));
		test_hand.hand.add(new PlayingCard("7", PlayingCard.DIAMONDS, 7, 7));
		test_hand.hand.add(new PlayingCard("10", PlayingCard.SPADES, 10, 10));
		test_hand.sort();
		test_hand2 = new HandOfCards(deck);
		test_hand2.hand.clear();
		test_hand2.hand = new ArrayList<PlayingCard>();
		test_hand2.hand.add(new PlayingCard("Q", PlayingCard.CLUBS, 12, 12));
		test_hand2.hand.add(new PlayingCard("Q", PlayingCard.HEARTS, 12, 12));
		test_hand2.hand.add(new PlayingCard("4", PlayingCard.HEARTS, 4, 4));
		test_hand2.hand.add(new PlayingCard("4", PlayingCard.DIAMONDS, 4, 4));
		test_hand2.hand.add(new PlayingCard("3", PlayingCard.SPADES, 3, 3));
		test_hand2.sort();
		System.out.println("\nTwo pair test: (1) " + test_hand + "  vs.  (2) " + test_hand2);
		System.out.println(test_hand.getGameValue() + " " + test_hand2.getGameValue());
		System.out.println("Game value of (1) is > (2): "+(test_hand.getGameValue() > test_hand2.getGameValue()));
		
		test_hand = new HandOfCards(deck);
		test_hand.hand.clear();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand.add(new PlayingCard("K", PlayingCard.DIAMONDS, 13, 13));
		test_hand.hand.add(new PlayingCard("K", PlayingCard.SPADES, 13, 13));
		test_hand.hand.add(new PlayingCard("7", PlayingCard.HEARTS, 7, 7));
		test_hand.hand.add(new PlayingCard("7", PlayingCard.DIAMONDS, 7, 7));
		test_hand.hand.add(new PlayingCard("10", PlayingCard.SPADES, 10, 10));
		test_hand.sort();
		test_hand2 = new HandOfCards(deck);
		test_hand2.hand.clear();
		test_hand2.hand = new ArrayList<PlayingCard>();
		test_hand2.hand.add(new PlayingCard("K", PlayingCard.CLUBS, 13, 13));
		test_hand2.hand.add(new PlayingCard("K", PlayingCard.HEARTS, 13, 13));
		test_hand2.hand.add(new PlayingCard("4", PlayingCard.HEARTS, 4, 4));
		test_hand2.hand.add(new PlayingCard("4", PlayingCard.DIAMONDS, 4, 4));
		test_hand2.hand.add(new PlayingCard("3", PlayingCard.SPADES, 3, 3));
		test_hand2.sort();
		System.out.println("\nTwo pair test: (1) " + test_hand + "  vs.  (2) " + test_hand2);
		System.out.println(test_hand.getGameValue() + " " + test_hand2.getGameValue());
		System.out.println("Game value of (1) is > (2): "+(test_hand.getGameValue() > test_hand2.getGameValue()));
		
		test_hand = new HandOfCards(deck);
		test_hand.hand.clear();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand.add(new PlayingCard("K", PlayingCard.DIAMONDS, 13, 13));
		test_hand.hand.add(new PlayingCard("K", PlayingCard.SPADES, 13, 13));
		test_hand.hand.add(new PlayingCard("7", PlayingCard.CLUBS, 7, 7));
		test_hand.hand.add(new PlayingCard("7", PlayingCard.SPADES, 7, 7));
		test_hand.hand.add(new PlayingCard("4", PlayingCard.SPADES, 4, 4));
		test_hand.sort();
		test_hand2 = new HandOfCards(deck);
		test_hand2.hand.clear();
		test_hand2.hand = new ArrayList<PlayingCard>();
		test_hand2.hand.add(new PlayingCard("K", PlayingCard.CLUBS, 13, 13));
		test_hand2.hand.add(new PlayingCard("K", PlayingCard.HEARTS, 13, 13));
		test_hand2.hand.add(new PlayingCard("7", PlayingCard.HEARTS, 7, 7));
		test_hand2.hand.add(new PlayingCard("7", PlayingCard.DIAMONDS, 7, 7));
		test_hand2.hand.add(new PlayingCard("3", PlayingCard.DIAMONDS, 3, 3));
		test_hand2.sort();
		System.out.println("\nTwo pair test: (1) " + test_hand + "  vs.  (2) " + test_hand2);
		System.out.println(test_hand.getGameValue() + " " + test_hand2.getGameValue());
		System.out.println("Game value of (1) is > (2): "+(test_hand.getGameValue() > test_hand2.getGameValue()));
		
		
		//threeOfAKind game value tests
		test_hand = new HandOfCards(deck);
		test_hand.hand.clear();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand.add(new PlayingCard("K", PlayingCard.DIAMONDS, 13, 13));
		test_hand.hand.add(new PlayingCard("K", PlayingCard.SPADES, 13, 13));
		test_hand.hand.add(new PlayingCard("K", PlayingCard.HEARTS, 13, 13));
		test_hand.hand.add(new PlayingCard("2", PlayingCard.DIAMONDS, 2, 2));
		test_hand.hand.add(new PlayingCard("3", PlayingCard.SPADES, 3, 3));
		test_hand.sort();
		test_hand2 = new HandOfCards(deck);
		test_hand2.hand.clear();
		test_hand2.hand = new ArrayList<PlayingCard>();
		test_hand2.hand.add(new PlayingCard("Q", PlayingCard.CLUBS, 12, 12));
		test_hand2.hand.add(new PlayingCard("Q", PlayingCard.HEARTS, 12, 12));
		test_hand2.hand.add(new PlayingCard("Q", PlayingCard.DIAMONDS, 12, 12));
		test_hand2.hand.add(new PlayingCard("A", PlayingCard.CLUBS, 1, 14));
		test_hand2.hand.add(new PlayingCard("K", PlayingCard.CLUBS, 13, 13));
		test_hand2.sort();
		System.out.println("\nThree of a kind test: (1) " + test_hand + "  vs.  (2) " + test_hand2);
		System.out.println(test_hand.getGameValue() + " " + test_hand2.getGameValue());
		System.out.println("Game value of (1) is > (2): "+(test_hand.getGameValue() > test_hand2.getGameValue()));
		
		deck.reset();
		
		test_hand = new HandOfCards(deck);
		test_hand.hand.clear();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand.add(new PlayingCard("A", PlayingCard.DIAMONDS, 1, 14));
		test_hand.hand.add(new PlayingCard("A", PlayingCard.SPADES, 1, 14));
		test_hand.hand.add(new PlayingCard("A", PlayingCard.HEARTS, 1, 14));
		test_hand.hand.add(new PlayingCard("10", PlayingCard.DIAMONDS, 10, 10));
		test_hand.hand.add(new PlayingCard("9", PlayingCard.SPADES, 9, 9));
		test_hand.sort();
		test_hand2 = new HandOfCards(deck);
		test_hand2.hand.clear();
		test_hand2.hand = new ArrayList<PlayingCard>();
		test_hand2.hand.add(new PlayingCard("K", PlayingCard.CLUBS, 13, 13));
		test_hand2.hand.add(new PlayingCard("K", PlayingCard.HEARTS, 13, 13));
		test_hand2.hand.add(new PlayingCard("K", PlayingCard.HEARTS, 13, 13));
		test_hand2.hand.add(new PlayingCard("Q", PlayingCard.DIAMONDS, 12, 12));
		test_hand2.hand.add(new PlayingCard("J", PlayingCard.SPADES, 11, 11));
		test_hand2.sort();
		System.out.println("\nThree of a kind test: (1) " + test_hand + "  vs.  (2) " + test_hand2);
		System.out.println(test_hand.getGameValue() + " " + test_hand2.getGameValue());
		System.out.println("Game value of (1) is > (2): "+(test_hand.getGameValue() > test_hand2.getGameValue()));
		
		//straight game value tests
		test_hand = new HandOfCards(deck);
		test_hand.hand.clear();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand.add(new PlayingCard("A", PlayingCard.DIAMONDS, 1, 14));
		test_hand.hand.add(new PlayingCard("K", PlayingCard.SPADES, 13, 13));
		test_hand.hand.add(new PlayingCard("Q", PlayingCard.HEARTS, 12, 12));
		test_hand.hand.add(new PlayingCard("10", PlayingCard.DIAMONDS, 10, 10));
		test_hand.hand.add(new PlayingCard("J", PlayingCard.SPADES, 11, 11));
		test_hand.sort();
		test_hand2 = new HandOfCards(deck);
		test_hand2.hand.clear();
		test_hand2.hand = new ArrayList<PlayingCard>();
		test_hand2.hand.add(new PlayingCard("K", PlayingCard.CLUBS, 13, 13));
		test_hand2.hand.add(new PlayingCard("Q", PlayingCard.HEARTS, 12, 12));
		test_hand2.hand.add(new PlayingCard("J", PlayingCard.HEARTS, 11, 11));
		test_hand2.hand.add(new PlayingCard("10", PlayingCard.DIAMONDS, 10, 10));
		test_hand2.hand.add(new PlayingCard("9", PlayingCard.SPADES, 9, 9));
		test_hand2.sort();
		System.out.println("\nStraight test: (1) " + test_hand + "  vs.  (2) " + test_hand2);
		System.out.println(test_hand.getGameValue() + " " + test_hand2.getGameValue());
		System.out.println("Game value of (1) is > (2): "+(test_hand.getGameValue() > test_hand2.getGameValue()));
		
		test_hand = new HandOfCards(deck);
		test_hand.hand.clear();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand.add(new PlayingCard("6", PlayingCard.DIAMONDS, 6, 6));
		test_hand.hand.add(new PlayingCard("5", PlayingCard.SPADES, 5, 5));
		test_hand.hand.add(new PlayingCard("4", PlayingCard.HEARTS, 4, 4));
		test_hand.hand.add(new PlayingCard("3", PlayingCard.DIAMONDS, 3, 3));
		test_hand.hand.add(new PlayingCard("2", PlayingCard.SPADES, 2, 2));
		test_hand.sort();
		test_hand2 = new HandOfCards(deck);
		test_hand2.hand.clear();
		test_hand2.hand = new ArrayList<PlayingCard>();
		test_hand2.hand.add(new PlayingCard("A", PlayingCard.CLUBS, 1, 14));
		test_hand2.hand.add(new PlayingCard("2", PlayingCard.HEARTS, 2, 2));
		test_hand2.hand.add(new PlayingCard("3", PlayingCard.HEARTS, 3, 3));
		test_hand2.hand.add(new PlayingCard("4", PlayingCard.DIAMONDS, 4, 4));
		test_hand2.hand.add(new PlayingCard("5", PlayingCard.SPADES, 5, 5));
		test_hand2.sort();
		System.out.println("\nStraight test: (1) " + test_hand + "  vs.  (2) " + test_hand2);
		System.out.println(test_hand.getGameValue() + " " + test_hand2.getGameValue());
		System.out.println("Game value of (1) is > (2): "+(test_hand.getGameValue() > test_hand2.getGameValue()));
		
		//flush game value tests
		test_hand = new HandOfCards(deck);
		test_hand.hand.clear();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand.add(new PlayingCard("J", PlayingCard.HEARTS, 11, 11));
		test_hand.hand.add(new PlayingCard("2", PlayingCard.HEARTS, 2, 2));
		test_hand.hand.add(new PlayingCard("3", PlayingCard.HEARTS, 3, 3));
		test_hand.hand.add(new PlayingCard("4", PlayingCard.HEARTS, 4, 4));
		test_hand.hand.add(new PlayingCard("5", PlayingCard.HEARTS, 5, 5));
		test_hand.sort();
		test_hand2 = new HandOfCards(deck);
		test_hand2.hand.clear();
		test_hand2.hand = new ArrayList<PlayingCard>();
		test_hand2.hand = new ArrayList<PlayingCard>();
		test_hand2.hand.add(new PlayingCard("2", PlayingCard.DIAMONDS, 2, 2));
		test_hand2.hand.add(new PlayingCard("4", PlayingCard.DIAMONDS, 4, 4));
		test_hand2.hand.add(new PlayingCard("6", PlayingCard.DIAMONDS, 6, 6));
		test_hand2.hand.add(new PlayingCard("8", PlayingCard.DIAMONDS, 8, 8));
		test_hand2.hand.add(new PlayingCard("10", PlayingCard.DIAMONDS, 10, 10));
		test_hand2.sort();
		System.out.println("\nFlush test: (1) " + test_hand + "  vs.  (2) " + test_hand2);
		System.out.println(test_hand.getGameValue() + " " + test_hand2.getGameValue());
		System.out.println("Game value of (1) is > (2): "+(test_hand.getGameValue() > test_hand2.getGameValue()));
		
		test_hand = new HandOfCards(deck);
		test_hand.hand.clear();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand.add(new PlayingCard("J", PlayingCard.HEARTS, 11, 11));
		test_hand.hand.add(new PlayingCard("10", PlayingCard.HEARTS, 10, 10));
		test_hand.hand.add(new PlayingCard("3", PlayingCard.HEARTS, 3, 3));
		test_hand.hand.add(new PlayingCard("4", PlayingCard.HEARTS, 4, 4));
		test_hand.hand.add(new PlayingCard("5", PlayingCard.HEARTS, 5, 5));
		test_hand.sort();
		test_hand2 = new HandOfCards(deck);
		test_hand2.hand.clear();
		test_hand2.hand = new ArrayList<PlayingCard>();
		test_hand2.hand = new ArrayList<PlayingCard>();
		test_hand2.hand.add(new PlayingCard("2", PlayingCard.DIAMONDS, 2, 2));
		test_hand2.hand.add(new PlayingCard("4", PlayingCard.DIAMONDS, 4, 4));
		test_hand2.hand.add(new PlayingCard("5", PlayingCard.DIAMONDS, 5, 5));
		test_hand2.hand.add(new PlayingCard("J", PlayingCard.DIAMONDS, 11, 11));
		test_hand2.hand.add(new PlayingCard("10", PlayingCard.DIAMONDS, 10, 10));
		test_hand2.sort();
		System.out.println("\nFlush test: (1) " + test_hand + "  vs.  (2) " + test_hand2);
		System.out.println(test_hand.getGameValue() + " " + test_hand2.getGameValue());
		System.out.println("Game value of (1) is > (2): "+(test_hand.getGameValue() > test_hand2.getGameValue()));
		
		deck.reset();
		
		//four of a kind game value tests
		test_hand = new HandOfCards(deck);
		test_hand.hand.clear();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand.add(new PlayingCard("J", PlayingCard.HEARTS, 11, 11));
		test_hand.hand.add(new PlayingCard("J", PlayingCard.CLUBS, 11, 11));
		test_hand.hand.add(new PlayingCard("J", PlayingCard.DIAMONDS, 11, 11));
		test_hand.hand.add(new PlayingCard("J", PlayingCard.SPADES, 11, 11));
		test_hand.hand.add(new PlayingCard("9", PlayingCard.HEARTS, 9, 9));
		test_hand.sort();
		test_hand2 = new HandOfCards(deck);
		test_hand2.hand.clear();
		test_hand2.hand = new ArrayList<PlayingCard>();
		test_hand2.hand = new ArrayList<PlayingCard>();
		test_hand2.hand.add(new PlayingCard("10", PlayingCard.DIAMONDS, 10, 10));
		test_hand2.hand.add(new PlayingCard("10", PlayingCard.CLUBS, 10, 10));
		test_hand2.hand.add(new PlayingCard("10", PlayingCard.SPADES, 10, 10));
		test_hand2.hand.add(new PlayingCard("10", PlayingCard.HEARTS, 10, 10));
		test_hand2.hand.add(new PlayingCard("A", PlayingCard.DIAMONDS, 1, 14));
		test_hand2.sort();
		System.out.println("\nFour of a kind test: (1) " + test_hand + "  vs.  (2) " + test_hand2);
		System.out.println(test_hand.getGameValue() + " " + test_hand2.getGameValue());
		System.out.println("Game value of (1) is > (2): "+(test_hand.getGameValue() > test_hand2.getGameValue()));
		
		test_hand = new HandOfCards(deck);
		test_hand.hand.clear();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand.add(new PlayingCard("A", PlayingCard.HEARTS, 1, 14));
		test_hand.hand.add(new PlayingCard("A", PlayingCard.CLUBS, 1, 14));
		test_hand.hand.add(new PlayingCard("A", PlayingCard.DIAMONDS, 1, 14));
		test_hand.hand.add(new PlayingCard("A", PlayingCard.SPADES, 1, 14));
		test_hand.hand.add(new PlayingCard("9", PlayingCard.HEARTS, 9, 9));
		test_hand.sort();
		test_hand2 = new HandOfCards(deck);
		test_hand2.hand.clear();
		test_hand2.hand = new ArrayList<PlayingCard>();
		test_hand2.hand = new ArrayList<PlayingCard>();
		test_hand2.hand.add(new PlayingCard("10", PlayingCard.DIAMONDS, 10, 10));
		test_hand2.hand.add(new PlayingCard("10", PlayingCard.CLUBS, 10, 10));
		test_hand2.hand.add(new PlayingCard("10", PlayingCard.SPADES, 10, 10));
		test_hand2.hand.add(new PlayingCard("10", PlayingCard.HEARTS, 10, 10));
		test_hand2.hand.add(new PlayingCard("K", PlayingCard.DIAMONDS, 13, 13));
		test_hand2.sort();
		System.out.println("\nFour of a kind test: (1) " + test_hand + "  vs.  (2) " + test_hand2);
		System.out.println(test_hand.getGameValue() + " " + test_hand2.getGameValue());
		System.out.println("Game value of (1) is > (2): "+(test_hand.getGameValue() > test_hand2.getGameValue()));
		
		
		//straight flush game value tests
		test_hand = new HandOfCards(deck);
		test_hand.hand.clear();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand.add(new PlayingCard("10", PlayingCard.HEARTS, 10, 10));
		test_hand.hand.add(new PlayingCard("9", PlayingCard.HEARTS, 9, 9));
		test_hand.hand.add(new PlayingCard("8", PlayingCard.HEARTS, 8, 8));
		test_hand.hand.add(new PlayingCard("7", PlayingCard.HEARTS, 7, 7));
		test_hand.hand.add(new PlayingCard("6", PlayingCard.HEARTS, 6, 6));
		test_hand.sort();
		test_hand2 = new HandOfCards(deck);
		test_hand2.hand.clear();
		test_hand2.hand = new ArrayList<PlayingCard>();
		test_hand2.hand = new ArrayList<PlayingCard>();
		test_hand2.hand.add(new PlayingCard("7", PlayingCard.DIAMONDS, 7, 7));
		test_hand2.hand.add(new PlayingCard("6", PlayingCard.DIAMONDS, 6, 6));
		test_hand2.hand.add(new PlayingCard("5", PlayingCard.DIAMONDS, 5, 5));
		test_hand2.hand.add(new PlayingCard("4", PlayingCard.DIAMONDS, 4, 4));
		test_hand2.hand.add(new PlayingCard("3", PlayingCard.DIAMONDS, 3, 3));
		test_hand2.sort();
		System.out.println("\nStraight flush test: (1) " + test_hand + "  vs.  (2) " + test_hand2);
		System.out.println(test_hand.getGameValue() + " " + test_hand2.getGameValue());
		System.out.println("Game value of (1) is > (2): "+(test_hand.getGameValue() > test_hand2.getGameValue()));
		
		test_hand = new HandOfCards(deck);
		test_hand.hand.clear();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand.add(new PlayingCard("6", PlayingCard.HEARTS, 6, 6));
		test_hand.hand.add(new PlayingCard("5", PlayingCard.HEARTS, 5, 5));
		test_hand.hand.add(new PlayingCard("4", PlayingCard.HEARTS, 4, 4));
		test_hand.hand.add(new PlayingCard("3", PlayingCard.HEARTS, 3, 3));
		test_hand.hand.add(new PlayingCard("2", PlayingCard.HEARTS, 2, 2));
		test_hand.sort();
		test_hand2 = new HandOfCards(deck);
		test_hand2.hand.clear();
		test_hand2.hand = new ArrayList<PlayingCard>();
		test_hand2.hand = new ArrayList<PlayingCard>();
		test_hand2.hand.add(new PlayingCard("A", PlayingCard.DIAMONDS, 1, 14));
		test_hand2.hand.add(new PlayingCard("2", PlayingCard.DIAMONDS, 2, 2));
		test_hand2.hand.add(new PlayingCard("3", PlayingCard.DIAMONDS, 3, 3));
		test_hand2.hand.add(new PlayingCard("4", PlayingCard.DIAMONDS, 4, 4));
		test_hand2.hand.add(new PlayingCard("5", PlayingCard.DIAMONDS, 5, 5));
		test_hand2.sort();
		System.out.println("\nStraight flush test: (1) " + test_hand + "  vs.  (2) " + test_hand2);
		System.out.println(test_hand.getGameValue() + " " + test_hand2.getGameValue());
		System.out.println("Game value of (1) is > (2): "+(test_hand.getGameValue() > test_hand2.getGameValue()));
		
		deck.reset();
		
		//royal flush game value tests
		test_hand = new HandOfCards(deck);
		test_hand.hand.clear();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand.add(new PlayingCard("A", PlayingCard.HEARTS, 1, 14));
		test_hand.hand.add(new PlayingCard("K", PlayingCard.HEARTS, 13, 13));
		test_hand.hand.add(new PlayingCard("Q", PlayingCard.HEARTS, 12, 12));
		test_hand.hand.add(new PlayingCard("J", PlayingCard.HEARTS, 11, 11));
		test_hand.hand.add(new PlayingCard("10", PlayingCard.HEARTS, 10, 10));
		test_hand.sort();
		test_hand2 = new HandOfCards(deck);
		test_hand2.hand.clear();
		test_hand2.hand = new ArrayList<PlayingCard>();
		test_hand2.hand = new ArrayList<PlayingCard>();
		test_hand2.hand.add(new PlayingCard("A", PlayingCard.DIAMONDS, 1, 14));
		test_hand2.hand.add(new PlayingCard("K", PlayingCard.DIAMONDS, 13, 13));
		test_hand2.hand.add(new PlayingCard("Q", PlayingCard.DIAMONDS, 12, 12));
		test_hand2.hand.add(new PlayingCard("J", PlayingCard.DIAMONDS, 11, 11));
		test_hand2.hand.add(new PlayingCard("10", PlayingCard.DIAMONDS, 10, 10));
		test_hand2.sort();
		System.out.println("\nRoyal flush test: (1) " + test_hand + "  vs.  (2) " + test_hand2);
		System.out.println(test_hand.getGameValue() + " " + test_hand2.getGameValue());
		System.out.println("Game value of (1) is == (2): "+(test_hand.getGameValue() == test_hand2.getGameValue()));
		
		test_hand = new HandOfCards(deck);
		test_hand.hand.clear();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand.add(new PlayingCard("A", PlayingCard.HEARTS, 1, 14));
		test_hand.hand.add(new PlayingCard("K", PlayingCard.HEARTS, 13, 13));
		test_hand.hand.add(new PlayingCard("Q", PlayingCard.HEARTS, 12, 12));
		test_hand.hand.add(new PlayingCard("J", PlayingCard.HEARTS, 11, 11));
		test_hand.hand.add(new PlayingCard("10", PlayingCard.HEARTS, 10, 10));
		test_hand.sort();
		test_hand2 = new HandOfCards(deck);
		test_hand2.hand.clear();
		test_hand2.hand = new ArrayList<PlayingCard>();
		test_hand2.hand = new ArrayList<PlayingCard>();
		test_hand2.hand.add(new PlayingCard("A", PlayingCard.DIAMONDS, 1, 14));
		test_hand2.hand.add(new PlayingCard("K", PlayingCard.CLUBS, 13, 13));
		test_hand2.hand.add(new PlayingCard("Q", PlayingCard.DIAMONDS, 12, 12));
		test_hand2.hand.add(new PlayingCard("J", PlayingCard.SPADES, 11, 11));
		test_hand2.hand.add(new PlayingCard("10", PlayingCard.DIAMONDS, 10, 10));
		test_hand2.sort();
		System.out.println("\nRoyal flush test: (1) " + test_hand + "  vs.  (2) " + test_hand2);
		System.out.println(test_hand.getGameValue() + " " + test_hand2.getGameValue());
		System.out.println("Game value of (1) is > (2): "+(test_hand.getGameValue() > test_hand2.getGameValue()));
		
		test_hand = new HandOfCards(deck);
		test_hand.hand.clear();
		test_hand.hand = new ArrayList<PlayingCard>();
		test_hand.hand.add(new PlayingCard("A", PlayingCard.HEARTS, 1, 14));
		test_hand.hand.add(new PlayingCard("K", PlayingCard.HEARTS, 13, 13));
		test_hand.hand.add(new PlayingCard("Q", PlayingCard.HEARTS, 12, 12));
		test_hand.hand.add(new PlayingCard("J", PlayingCard.HEARTS, 11, 11));
		test_hand.hand.add(new PlayingCard("10", PlayingCard.HEARTS, 10, 10));
		test_hand.sort();
		test_hand2 = new HandOfCards(deck);
		test_hand2.hand.clear();
		test_hand2.hand = new ArrayList<PlayingCard>();
		test_hand2.hand = new ArrayList<PlayingCard>();
		test_hand2.hand.add(new PlayingCard("9", PlayingCard.DIAMONDS, 9, 9));
		test_hand2.hand.add(new PlayingCard("K", PlayingCard.DIAMONDS, 13, 13));
		test_hand2.hand.add(new PlayingCard("Q", PlayingCard.DIAMONDS, 12, 12));
		test_hand2.hand.add(new PlayingCard("J", PlayingCard.DIAMONDS, 11, 11));
		test_hand2.hand.add(new PlayingCard("10", PlayingCard.DIAMONDS, 10, 10));
		test_hand2.sort();
		System.out.println("\nRoyal flush test: (1) " + test_hand + "  vs.  (2) " + test_hand2);
		System.out.println(test_hand.getGameValue() + " " + test_hand2.getGameValue());
		System.out.println("Game value of (1) is > (2): "+(test_hand.getGameValue() > test_hand2.getGameValue()));
	}
}
