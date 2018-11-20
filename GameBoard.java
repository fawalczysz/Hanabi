
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Stack;

public class GameBoard {
	
	private static GameBoard INSTANCE;
	
	private Stack<Card>			    cardDeck;
	private Stack<Card>		    	discardDeck;
	private HashMap<Color, Integer> cardStacks;
	
	private int blueTokenStack;
	private int redTokenStack;

	private GameBoard() {
		ArrayList<Integer> valueDistribution = new ArrayList<>(Arrays.asList(1, 1, 1, 2, 2, 3, 3, 4, 4, 5));
		ArrayList<Color>   colorSet 		 = new ArrayList<>(Arrays.asList(Color.WHITE, Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN));

		cardDeck    = new Stack<>();
		discardDeck = new Stack<>();
		
		for (Color color : colorSet)
			for(Integer value : valueDistribution)
				cardDeck.push(new Card(color, value));
		
		Collections.shuffle(cardDeck);
		
		cardStacks = new HashMap<>();
		cardStacks.put(Color.WHITE, 0);
		cardStacks.put(Color.RED, 0);
		cardStacks.put(Color.BLUE, 0);
		cardStacks.put(Color.YELLOW, 0);
		cardStacks.put(Color.GREEN, 0);
		
		redTokenStack  = 0;
		blueTokenStack = 8;
	}

	static GameBoard getInstance() {
		if (INSTANCE == null) INSTANCE = new GameBoard();
		return INSTANCE;
	}
	
	static GameBoard initGameBoard() { 
		INSTANCE = new GameBoard();
		return INSTANCE;}

	int getBlueTokenStack() { return blueTokenStack; }
	int getRedTokenStack()  { return redTokenStack;  }
	
	int getCardStack(Color c) { return cardStacks.get(c); }
	
	int getCardDeckSize() { return cardDeck.size(); }

	Card draw() { return cardDeck.pop(); }

	void discard(Card c) { discardDeck.add(c); }

	void addBlueToken()    { blueTokenStack = Math.min(blueTokenStack + 1, 8); }
	
	void removeBlueToken() { blueTokenStack--; }
	
	void addRedToken()     { redTokenStack++;  }

	void addToStack(Color color) { cardStacks.put(color, cardStacks.get(color) + 1); }

	Stack<Card> getDiscardDeck() { return discardDeck; }
}
