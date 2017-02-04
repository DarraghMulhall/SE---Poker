package poker;

//card to be used in poker game. Has a type, suit, face value and game value.

public class PlayingCard {

	public static final char HEARTS = 'H';
	public static final char DIAMONDS = 'D';
	public static final char CLUBS = 'C';	
	public static final char SPADES = 'S';	
	
	private String type;
	private char suit;
	private int faceValue;
	private int gameValue;
	
	public PlayingCard(String type, char suit, int faceValue, int gameValue){
		this.type = type;
		this.suit = suit;
		this.faceValue = faceValue;
		this.gameValue = gameValue;
	}
	public char getSuit(){
		return suit;
	}
	public int getFaceValue(){
		return faceValue;
	}
	public int getGameValue(){
		return gameValue;
	}
	
	//returns a string representation of the card - card type + card suit
	public String toString() {
		String str = this.type + Character.toString(this.suit);
		return str;
	}
	
	//main used for testing card class.
	//loops through an array of the suits, creating an instance of every possible type of card for each suit and adding to an array for print checking
	public static void main(String[] args){
		char[] suits = new char[4];
		suits[0] = PlayingCard.HEARTS;
		suits[1] = PlayingCard.DIAMONDS;
		suits[2] = PlayingCard.CLUBS;
		suits[3] = PlayingCard.SPADES;
		
		PlayingCard[] cards = new PlayingCard[52];
		int deck_index = 0;
		//for each suit, it adds the Ace, then loops from 2-10, then adds Jack, Queen, King after.
		for(int i=0; i<suits.length; i++){
			cards[deck_index++] = new PlayingCard("A", suits[i], 1, 14);
			for(int j=2; j<=10; j++){
				cards[deck_index++] = new PlayingCard(Integer.toString(j), suits[i], j, j);
			}
			cards[deck_index++] = new PlayingCard("J", suits[i], 11, 11);
			cards[deck_index++] = new PlayingCard("Q", suits[i], 12, 12);
			cards[deck_index++] = new PlayingCard("K", suits[i], 13, 13);
		}
		for(int i=0; i<cards.length; i++){
			System.out.println(cards[i]);
		}
	}
}
