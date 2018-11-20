
public class Action {
	private int  type;
	private int  idPlayer;
	private Card card;
	private int  idHintPlayer;
	
	public Action(int idPlayer) {
		type		  = -1;
		this.idPlayer = idPlayer;
		card 		  = new Card();
		idHintPlayer  = -1;
	}
	//TODO change action system, we do not know cards and place if 2 cards are identic
	public Action(int type, int idPlayer, Card card, int idHintPlayer) {
		this.type		  = type;
		this.idPlayer	  = idPlayer;
		this.card		  = card;
		this.idHintPlayer = idHintPlayer;
	}

	int  getType() 		   { return type; }
	void setType(int type) { this.type = type; }
	
	int  getIdPlayer() 			   { return idPlayer; }
	void setIdPlayer(int idPlayer) { this.idPlayer = idPlayer; }
	
	Card getCard() 			{ return card; }
	void setCard(Card card) { this.card = card; }
	
	int  getIdHintPlayer() 		   		   { return idHintPlayer; }
	void setIdHintPlayer(int idHintPlayer) { this.idHintPlayer = idHintPlayer; }
	
	public String toString() {
		switch (type)
		{
			case 0 : return "Played card "    + card;
			case 1 : return "Discarded card " + card;
			default: return "Gave info "      + card + " to " + Hanabi.getPlayers().get(idHintPlayer).getName();
		}
	}
}
