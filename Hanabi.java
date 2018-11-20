
import java.io.PrintWriter;
import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class Hanabi {
	private static ArrayList<Player> players;
	private static int 				 currentPlayer;
	private static ArrayList<Action> history;
	private static int				 nbTurn;
	
	public static boolean display=true;   //display variable
	public static boolean graphic=false;   //screen display
	
	
	public static ArrayList<Integer> priorities;

	public static boolean next;
	
	static ArrayList<Player> getPlayers() { return players; }
	static int getCurrentPlayer() { return currentPlayer; }
	static ArrayList<Action> getHistory() { return history; }
	
	public static void main(String[] args) {
		Action		action;
		PrintWriter writer;
		GameBoard   gameboard;
		Affichage 	graphique = null;
		double sum;

		gameboard = GameBoard.initGameBoard();
		players		  = new ArrayList<>();
		currentPlayer = 0;
		history		  = new ArrayList<>();
		nbTurn		  = 1;
		
		players.add(new Player("Joueur 1"));
		players.add(new Player("Joueur 2"));
		players.add(new Player("Joueur 3"));
		players.add(new Player("Joueur 4"));
		
		
		for (Player p : players) {
			for (int i = 0; i < Utils.HAND_SIZE; i++) p.drawCard();
			
		}
		if (graphic) {
			graphique = new Affichage();
			graphique.init();
			graphique.update();
			graphique.bind();
			graphique.setVisible(true);
		}
		
		while(!isGameOver())
		{
			//for (Player p :Hanabi.getPlayers())
				//p.updateLeftCards();
			
			if (display) {
				if (currentPlayer == 0) System.out.println("\nTour n° " + nbTurn);
				System.out.println("\nTour de " + players.get(currentPlayer).getName());
				
				//for(Player p : players)
				for (int i=0; i<4;i++)
				{
					Player p=players.get(i);
					if(i%2 !=currentPlayer%2 ) System.out.println(p);
					//if(p != players.get(currentPlayer) ) System.out.println(p);
					else System.out.println(p.toString(false));
				}
				System.out.println("Blanc : "    + gameboard.getCardStack(Color.WHITE)  + " | Rouge : " + gameboard.getCardStack(Color.RED) + " | Bleu : " + gameboard.getCardStack(Color.BLUE) +
								   " | Jaune : " + gameboard.getCardStack(Color.YELLOW) + " | Vert : "  + gameboard.getCardStack(Color.GREEN));
				System.out.println("Cartes dans la pile : " + gameboard.getCardDeckSize() + " | Jetons rouges : " + gameboard.getRedTokenStack() + " |  Jetons bleus : " + gameboard.getBlueTokenStack() + "\n");
			}
			
			
			/*
			if (graphic) graphique.update();
			next=false;
			while (!next) {
				try {
					Thread.sleep(10);
				}catch (InterruptedException e ){}
			}*/
			action = players.get(currentPlayer).getAction();
			
			
			switch (action.getType())
			{
				case 0 :
					players.get(action.getIdPlayer()).playCard(action.getCard());
					players.get(action.getIdPlayer()).drawCard();
					break;
				case 1 :
					players.get(action.getIdPlayer()).discardCard(action.getCard());
					players.get(action.getIdPlayer()).drawCard();
					break;
				case 2 :
					//System.out.println(action.getIdHintPlayer());
					//System.out.println(action.getCard());
					players.get(action.getIdHintPlayer()).addInfo(action.getCard());
					gameboard.removeBlueToken();
					break;
			}
			
						
			if (graphic) {
				graphique.update();
				try {
					Thread.sleep(500);
				}catch (InterruptedException e ){}
				
			}

			if (++currentPlayer == players.size()) {
				currentPlayer = 0;
				nbTurn++;
			}
		}
		
		if (display) System.out.println("\nGAME OVER !");

		Utils.inputReader.close();



	}

	private static boolean isGameOver() {
		return GameBoard.getInstance().getRedTokenStack() == 3 ||
			   (GameBoard.getInstance().getCardStack(Color.WHITE)  == 5 && GameBoard.getInstance().getCardStack(Color.RED)   == 5  && GameBoard.getInstance().getCardStack(Color.BLUE)  == 5 &&
			    GameBoard.getInstance().getCardStack(Color.YELLOW) == 5 && GameBoard.getInstance().getCardStack(Color.GREEN) == 5) ||
			   (GameBoard.getInstance().getCardDeckSize() == 0 && currentPlayer == 0);
	}

}
