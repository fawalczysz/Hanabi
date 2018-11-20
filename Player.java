
import java.util.ArrayList;
import java.util.Arrays;

public class Player {
	private int				id;
	private String			name;
	private ArrayList<Card> hand;
	private ArrayList<Card> knownHand;
	public  ArrayList<ArrayList<Bool>> valueMatrix;
	public  ArrayList<ArrayList<Bool>> colorMatrix;
	

	  public  ArrayList<Integer> whiteCardsLeft;
	  public  ArrayList<Integer> redCardsLeft;
	  public  ArrayList<Integer> blueCardsLeft;
	  public  ArrayList<Integer> yellowCardsLeft;
	  public  ArrayList<Integer> greenCardsLeft;
	  
	  void initLeftCards() {
	    whiteCardsLeft = new ArrayList<>(Arrays.asList(3, 2, 2, 2, 1));
	    redCardsLeft = new ArrayList<>(Arrays.asList(3, 2, 2, 2, 1));
	    blueCardsLeft = new ArrayList<>(Arrays.asList(3, 2, 2, 2, 1));
	    yellowCardsLeft = new ArrayList<>(Arrays.asList(3, 2, 2, 2, 1));
	    greenCardsLeft = new ArrayList<>(Arrays.asList(3, 2, 2, 2, 1));
	  }
	  
	  void matrixDisplay(ArrayList<ArrayList<Bool>> matrix) {
		  for (int i=0;i<matrix.size();i++) 
				  System.out.println(matrix.get(i));
	  }
	  
	  
	 

	
	Player(String name) {
		id 		  	= Hanabi.getPlayers().size();
		this.name 	= name;
		hand      	= new ArrayList<>();
		knownHand 	= new ArrayList<>();
		
		valueMatrix = new ArrayList<>();
		colorMatrix = new ArrayList<>();
		
		for(int i = 0; i < Utils.HAND_SIZE; i++) knownHand.add(new Card());
		
		for (int i=0; i<4;i++) {
			valueMatrix.add(new ArrayList<>(Arrays.asList(Bool.NONE, Bool.NONE, Bool.NONE, Bool.NONE, Bool.NONE)));
			colorMatrix.add(new ArrayList<>(Arrays.asList(Bool.NONE, Bool.NONE, Bool.NONE, Bool.NONE, Bool.NONE)));
		}
	}

	String			getName() 	   { return name; }
	ArrayList<Card> getHand()	   { return hand; }
	ArrayList<Card> getKnownHand() { return knownHand; }

	
	Action getAction() { return getActionFromInput(); }
	
	public ArrayList<ArrayList<Bool>> getValueMatrix() {return valueMatrix;}
	public ArrayList<ArrayList<Bool>> getColoreMatrix() {return colorMatrix;}
	
	private Action getActionFromInput() {
		Action  action = new Action(id);
		String	prompt;
		
		action.setType(Utils.getIntFromInput("Choisissez une action : (Jouer une carte (1) | Défausser une carte (2) | Donner une information (3) )", 1, 3) - 1);
		
		switch (action.getType())
		{
			case 0 : action.setCard(hand.get(Utils.getIntFromInput("Selectionnez la carte à jouer : (1) | (2) | (3) | (4)", 	1, 4) - 1)); break;
			case 1 : action.setCard(hand.get(Utils.getIntFromInput("Selectionnez la carte à défausser : (1) | (2) | (3) | (4)", 1, 4) - 1)); break;
			case 2 :
				prompt = "Choissez à qui donner une information : ";
				for (Player p : Hanabi.getPlayers())
					if (Hanabi.getPlayers().indexOf(p) != id) prompt += p.getName() + " (" + (Hanabi.getPlayers().indexOf(p) + 1) + ") ";
				
				do action.setIdHintPlayer(Utils.getIntFromInput(prompt, 0, Hanabi.getPlayers().size()) - 1);
				while (action.getIdHintPlayer() == id);
				
				if (Utils.getIntFromInput("Sélectionnez le type d'information à donner : Couleur (1) | Valeur (2)", 1, 2) == 1)
					switch(Utils.getIntFromInput("Selectionnez la couleur à donner : Blanc (1) | Rouge (2) | Bleu (3) | Jaune (4) | Vert (5)", 1, 5))
					{
						case 1 : action.setCard(new Card(Color.WHITE,  0)); break;
						case 2 : action.setCard(new Card(Color.RED,    0)); break;
						case 3 : action.setCard(new Card(Color.BLUE,   0)); break;
						case 4 : action.setCard(new Card(Color.YELLOW, 0)); break;
						case 5 : action.setCard(new Card(Color.GREEN,  0)); break;
					}
				else action.setCard(new Card(Color.NONE, Utils.getIntFromInput("Selectionnez la valeur à donner : (1) | (2) | (3) | (4) | (5)", 1, 5)));
				break;
		}
		
		return action;
	}
	
	void drawCard() {
		if(GameBoard.getInstance().getCardDeckSize() > 0) {
			hand.add(GameBoard.getInstance().draw());
			knownHand.add(new Card());
		}
	}

	void discardCard(Card card) {

		GameBoard.getInstance().discard(card);
		for (int i=0;i<5;i++) {
			valueMatrix.get(hand.indexOf(card)).set(i, Bool.NONE);
			colorMatrix.get(hand.indexOf(card)).set(i, Bool.NONE);
		}
		knownHand.remove(hand.indexOf(card));
		hand.remove(card);
		GameBoard.getInstance().addBlueToken();
	}

	void playCard(Card card) {
		GameBoard gameboard = GameBoard.getInstance();
		
		for (int i=0;i<5;i++) {
			valueMatrix.get(hand.indexOf(card)).set(i, Bool.NONE);
			colorMatrix.get(hand.indexOf(card)).set(i, Bool.NONE);
		}
		if (gameboard.getCardStack(card.getColor()) == card.getValue() - 1)
		{
			gameboard.addToStack(card.getColor());
			if (gameboard.getCardStack(card.getColor()) == 5) gameboard.addBlueToken();
		}
		else
		{
			gameboard.addRedToken();
			gameboard.discard(card);
		}

		knownHand.remove(hand.indexOf(card));
		hand.remove(card);
	}
	
	void addInfo(Card card) {
		for(int i = 0; i < hand.size(); i++) {
			if (hand.get(i).getColor() == card.getColor()) {
				knownHand.get(i).setColor(card.getColor());
				colorMatrix.get(i).set(Utils.colorToInt(card.getColor()),Bool.TRUE);
				
			}
			else if (card.getColor()!=Color.NONE)
				colorMatrix.get(i).set(Utils.colorToInt(card.getColor()),Bool.FALSE);
				
			if (hand.get(i).getValue() == card.getValue()) {
				knownHand.get(i).setValue(card.getValue());
				valueMatrix.get(i).set(card.getValue()-1,Bool.TRUE);
			}
			else if (card.getValue()!=0 && card.getValue()!=-1)
				valueMatrix.get(i).set(card.getValue()-1,Bool.FALSE);
		}

			  
	}

	public String toString() { return toString(true); }
	
	String toString(boolean trueHand) {
		String toString = name + " : ";
		
		for (int i = 0; i < hand.size(); i++) toString += (trueHand ? hand : knownHand).get(i) + " ";
		
		return toString;
	}
	
	public boolean canPlay() {
		boolean val=false;
		for (Card card : knownHand) 
			if (card.getColor()!=Color.NONE && card.getValue()!=0)
				val=true;
		return val;
	}
	
	
}
